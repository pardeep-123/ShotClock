package com.app.shotclock.utils

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.intuit.sdp.BuildConfig
import dagger.android.support.AndroidSupportInjection


abstract class LocationUpdateUtility<VB : ViewBinding> : Fragment() {

    var baseView: View? = null
    private var _binding: VB? = null
    val binding get() = _binding!!
    var isLoaded = false

    private val TAG = "LocationUpdateUtility"
//    private lateinit var binding: ActivityMainBinding

    private lateinit var mActivity: Activity
    private var locationRequest: LocationRequest? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        //   AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onAttach(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = getViewBinding()
        return binding.root
    }

    abstract fun getViewBinding(): VB

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d(TAG, "${it.key} = ${it.value}")
            }

            val fineLocation = permissions[Manifest.permission.ACCESS_FINE_LOCATION]
            val coarseLocation = permissions[Manifest.permission.ACCESS_COARSE_LOCATION]

            if (fineLocation == true && coarseLocation == true) {
                Log.e(TAG, "Permission Granted Successfully")
                checkGpsOn()
            } else {
                Log.e(TAG, "Permission not granted")
                if (permissions.keys.size > 0)
                    checkPermissionDenied(permissions.keys.first())
            }

        }


    private val gpsOnLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                Log.e(TAG, "GPS Turned on successfully")
                startLocationUpdates()
            } else if (result.resultCode == RESULT_CANCELED) {
                Log.e(TAG, "GPS Turned on failed")
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    com.app.shotclock.R.string.gps_required,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(com.app.shotclock.R.string.ok) {
                        // Request GPS On
                        checkGpsOn()
                    }.show()

            }
        }

    open fun getLiveLocation(activity: Activity) {

        mActivity = activity

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity)

        checkLocationPermissions()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                p0 ?: return
                for (location in p0.locations) {
                    // Update UI with location data
                    // ...
                    Log.e(
                        TAG, "==========" + location.latitude.toString() + ", " +
                                location.longitude + "========="
                    )

                    updatedLatLng(location.latitude, location.longitude)
                }
            }

        }
    }

    private fun checkLocationPermissions() {
        if (hasPermissions(permissions)) {
            Log.e(TAG, "Permissions Granted")
            checkGpsOn()
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            checkPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            checkPermissionDenied(Manifest.permission.ACCESS_COARSE_LOCATION)
        } else {
            Log.e(TAG, "Request for Permissions")
            requestPermission()
        }
    }

    // util method
    private fun hasPermissions(permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(mActivity, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        requestMultiplePermissions.launch(permissions)
    }

    private fun checkPermissionDenied(permission: String) {
        if (shouldShowRequestPermissionRationale(permission)) {
            Log.e(TAG, "Permissions Denied")
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                com.app.shotclock.R.string.permission_rationale,
                Snackbar.LENGTH_LONG
            )
                .setAction(com.app.shotclock.R.string.ok) {
                    // Request permission
                    requestPermission()
                }.show()


        } else {
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                com.app.shotclock.R.string.permission_denied_explanation,
                Snackbar.LENGTH_LONG
            )
                .setAction(com.app.shotclock.R.string.settings) {
                    // Build intent that displays the App settings screen.
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts(
                        "package",
                        BuildConfig.APPLICATION_ID,
                        null
                    )
                    intent.data = uri
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }.show()


        }
    }

    private fun locationPermission(permissions: Array<String>): Boolean {
        return ActivityCompat.checkSelfPermission(
            mActivity,
            permissions[0]
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            mActivity,
            permissions[1]
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun checkGpsOn() {
        locationRequest = LocationRequest.create()
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest?.interval = 5000
        locationRequest?.fastestInterval = 2000

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest!!)
        builder.setAlwaysShow(true)


        val result = LocationServices.getSettingsClient(mActivity)
            .checkLocationSettings(builder.build())

        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(ApiException::class.java)
                Log.e(TAG, "==========GPS is ON=============")

                startLocationUpdates()
            } catch (e: ApiException) {
                when (e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        val resolvableApiException = e as ResolvableApiException
                        gpsOnLauncher.launch(
                            IntentSenderRequest.Builder(resolvableApiException.resolution).build()
                        )

                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {

                    }
                }
            }
        }

    }

    //call startLocationUpdates() method for start live location update
    fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                mActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            hasPermissions(permissions)
            return
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest!!,
            locationCallback,
            Looper.getMainLooper()
        )
        Log.e(TAG, "Get Live Location Start")
    }


    //call stopLocationUpdates() method for stop live location update
    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        Log.e(TAG, "Get Live Location Stop")
    }

    abstract fun updatedLatLng(lat: Double, lng: Double)
}
