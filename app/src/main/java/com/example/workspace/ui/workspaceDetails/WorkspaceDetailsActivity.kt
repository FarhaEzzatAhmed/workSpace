package com.example.workspace.ui.workspaceDetails

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationRequest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.example.workspace.R
import com.example.workspace.databinding.ActivityWorkspaceDetailsBinding
import com.example.workspace.databinding.ItemWorkspaceBinding
import com.example.workspace.ui.base.BaseActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task

class WorkspaceDetailsActivity : BaseActivity() ,OnMapReadyCallback{
    lateinit var viewBinding: ActivityWorkspaceDetailsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var googleMap: GoogleMap?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityWorkspaceDetailsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        // setContentView(R.layout.activity_workspace_details)

        val intent = intent

        viewBinding.title?.setText(intent!!.getStringExtra("workspaceName"))

        Glide.with(this)
            .load(intent!!.getStringExtra("workspaceImage"))
            .transition(
                DrawableTransitionOptions.withCrossFade(
                    DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
                )
            )
            .into((viewBinding.itemImage) as ImageView)




        val mapFragment = supportFragmentManager.
        findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (isGPSPermissionAllowed()) {
            getUserLocation()
        } else {
              requestPermission()

        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        drawUserMarkerOnMap()

    }
    var userLocation :Location?=null

    fun drawUserMarkerOnMap(){
        if(userLocation == null) return
        if (googleMap==null)return
        val latlng = LatLng(userLocation?.latitude?:0.0,userLocation?.longitude?:0.0)
        val markerOptions = MarkerOptions()
        markerOptions.position (latlng)// get this from user location
        markerOptions.title ("current location")
        googleMap?.addMarker(markerOptions)
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,17.0f))

    }

    val requestGPSPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.

                getUserLocation()
            } else {
                showDialog("we Can't get nearest workspace,"+"to use this feature allow location permission")
            }
        }

    @RequiresApi(Build.VERSION_CODES.M)
    fun requestPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
        // show explanation to the user

            showDialog(message = "Please enable location permission ,"+"to show nearest workspace"
                , posActionName = "yes"
                ,posAction = { dialogInterface, i ->
                    dialogInterface.dismiss()
                    requestGPSPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)

                },
                negActionName = "No"
                ,negAction = { dialogInterface, i ->

                    dialogInterface.dismiss()
                }


            )

    }else{
        requestGPSPermissionLauncher.launch(
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    }
}
    fun isGPSPermissionAllowed():Boolean{

       return ContextCompat.checkSelfPermission(this,
           Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED
    }
    val locationCallBack: LocationCallback = object : LocationCallback(){
        override fun onLocationResult(result: LocationResult) {
            result?:return
            for (location in result.locations){
                Log.e("location update",""+location.latitude+""+location.longitude)
                userLocation =location
                drawUserMarkerOnMap()
            }
        }

    }
    val locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
        // interval ben kol update 10sawanii
        interval = 10000
        fastestInterval = 5000
        // priority hig accuracy 3lshan ygeb al location mn al gps

        priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    val  REQUEST_LOCATION_CODE =120
    @SuppressLint("MissingPermission")
    fun getUserLocation(){
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { locationSettingsResponse ->
            // All location settings are satisfied. The client can initialize
            // location requests here.
            // ...
            fusedLocationClient.requestLocationUpdates(locationRequest,locationCallBack,Looper.getMainLooper())
        }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException){
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(this@WorkspaceDetailsActivity,
                        REQUEST_LOCATION_CODE)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_LOCATION_CODE){
            if(requestCode == RESULT_OK){
                getUserLocation()
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallBack)
    }

}