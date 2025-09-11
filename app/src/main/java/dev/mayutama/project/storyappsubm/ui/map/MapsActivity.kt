package dev.mayutama.project.storyappsubm.ui.map

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import dev.mayutama.project.storyappsubm.R
import dev.mayutama.project.storyappsubm.data.remote.dto.res.ErrorRes
import dev.mayutama.project.storyappsubm.data.remote.dto.res.StoryRes
import dev.mayutama.project.storyappsubm.databinding.ActivityMapsBinding
import dev.mayutama.project.storyappsubm.factory.ViewModelFactory
import dev.mayutama.project.storyappsubm.util.ResultState
import dev.mayutama.project.storyappsubm.util.disableScreenAction
import dev.mayutama.project.storyappsubm.util.enableScreenAction
import dev.mayutama.project.storyappsubm.util.showToast
import timber.log.Timber

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val boundsBuilder = LatLngBounds.Builder()

    private val viewModel by viewModels<MapViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        setMapStyle()
        observeStory()
    }

    private fun setupView() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupAction() {

    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))

            if (!success) {
                Timber.e("Style parsing failed")
            }

        } catch (e: Resources.NotFoundException){
            Timber.e("Cant find style. Error: $e")
        }
    }

    fun observeStory() {
        viewModel.stories.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showLoading()
                    }
                    is ResultState.Success -> {
                        setLocationStory(result.data.listStory)
                        hideLoading()
                    }
                    is ResultState.Error<*> -> {
                        showToast(this@MapsActivity, (result.error as ErrorRes).message)
                        hideLoading()
                    }
                }
            }
        }
    }

    fun setLocationStory(stories: List<StoryRes.Story>) {
        stories.forEach { story ->
            val latLng = LatLng(story.lat, story.lon)
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(story.name)
                    .snippet("${story.description.substring(
                        0, 
                        if (story.description.length >= 20) 20 else story.description.length
                    )}${ if (story.description.length > 20) "..." else ""}")
            )
            boundsBuilder.include(latLng)
        }

        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )
    }

    fun showLoading(){
        binding.loadingLayout.root.visibility = View.VISIBLE
        disableScreenAction(window)
    }

    fun hideLoading(){
        binding.loadingLayout.root.visibility = View.GONE
        enableScreenAction(window)
    }
}