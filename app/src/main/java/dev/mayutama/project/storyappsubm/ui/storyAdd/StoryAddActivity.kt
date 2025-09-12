package dev.mayutama.project.storyappsubm.ui.storyAdd

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dev.mayutama.project.storyappsubm.R
import dev.mayutama.project.storyappsubm.data.remote.dto.res.ErrorRes
import dev.mayutama.project.storyappsubm.databinding.ActivityStoryAddBinding
import dev.mayutama.project.storyappsubm.factory.ViewModelFactory
import dev.mayutama.project.storyappsubm.util.ResultState
import dev.mayutama.project.storyappsubm.util.disableScreenAction
import dev.mayutama.project.storyappsubm.util.enableScreenAction
import dev.mayutama.project.storyappsubm.util.getImageUri
import dev.mayutama.project.storyappsubm.util.reduceFileImage
import dev.mayutama.project.storyappsubm.util.showToast
import dev.mayutama.project.storyappsubm.util.uriToFile
import java.io.File

class StoryAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryAddBinding
    private val viewModel by viewModels<StoryAddViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private var currentImageUri: Uri? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var location: Location? = null

    private val launchGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            showToast(this@StoryAddActivity, "No image selected")
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) {isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            val file = File(currentImageUri?.path ?: "")
            if (file.exists()) file.delete()
            currentImageUri = null
        }
    }

    private val launcherRequestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {isGranted ->
        if (isGranted) {
            Toast.makeText(this, "Permission request granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permission request denied", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingPermission")
    private val launcherRequestMultiplePermission =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
            }
        }

    private fun allPermissionsGranted(permission: String) = ContextCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupView() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setSupportActionBar(binding.topBar)
        supportActionBar?.let {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        if (!allPermissionsGranted(REQUIRED_PERMISSION_CAMERA)) {
            launcherRequestPermission.launch(REQUIRED_PERMISSION_CAMERA)
        }
    }

    @SuppressLint("MissingPermission")
    private fun setupAction() {
        binding.btnGallery.setOnClickListener {
            startGallery()
        }

        binding.btnCamera.setOnClickListener {
            startCamera()
        }

        binding.btnUpload.setOnClickListener {
            uploadStory()
        }

        binding.swLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getMyLastLocation()
            } else {
                location = null
            }
        }
    }

    private fun startGallery() {
        launchGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private fun uploadStory() {
        val edtDescription = binding.edtDescription

        if (
            edtDescription.error == null
            && currentImageUri != null
        ) {
            val imageFile = uriToFile(currentImageUri!!, this@StoryAddActivity).reduceFileImage()
            val description = edtDescription.text.toString()
            val lat = location?.latitude?.toFloat()
            val lon = location?.longitude?.toFloat()

            viewModel.addStory(imageFile, description, lat, lon).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Loading -> {
                            showLoading()
                        }
                        is ResultState.Success -> {
                            showToast(this@StoryAddActivity, result.data.message)
                            val intent = Intent()
                            setResult(STORY_ADD_RESULT, intent)
                            finish()
                            hideLoading()
                        }
                        is ResultState.Error<*> -> {
                            showToast(this@StoryAddActivity, (result.error as ErrorRes).message)
                            hideLoading()
                        }
                    }
                }
            }
        } else {
            showToast(this@StoryAddActivity, getString(R.string.form_validate_message))
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.imgStory.setImageURI(it)
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun getMyLastLocation() {
        if     (allPermissionsGranted(Manifest.permission.ACCESS_FINE_LOCATION) &&
            allPermissionsGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    this.location = location
                } else {
                    Toast.makeText(
                        this@StoryAddActivity,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            launcherRequestMultiplePermission.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    fun showLoading(){
        binding.loadingLayout.root.visibility = View.VISIBLE
        disableScreenAction(window)
    }

    fun hideLoading(){
        binding.loadingLayout.root.visibility = View.GONE
        enableScreenAction(window)
    }

    companion object {
        const val REQUIRED_PERMISSION_CAMERA = Manifest.permission.CAMERA
        const val STORY_ADD_RESULT = 201
    }
}