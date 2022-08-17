package com.example.permissions

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.permissions.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // you may use only 1 launcher if the response handling logic is the same
    // for all permissions

    private val requestCameraPermissionLauncher = registerForActivityResult(
        RequestPermission(),    // contract for requesting 1 permission
        ::onGotCameraPermissionResult
    )

    private val requestRecordAudioAndLocationPermissionsLauncher = registerForActivityResult(
        RequestMultiplePermissions(),   // contract for requesting more than 1 permission
        ::onGotRecordAudioAndLocationPermissionsResult
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.requestCameraPermissionButton.setOnClickListener {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
//                PackageManager.PERMISSION_GRANTED) {
//                onCameraPermissionGranted()
//            } else {
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(Manifest.permission.CAMERA),
//                    RQ_PERMISSIONS_FOR_FEATURE_1_CODE
//                )
//            }
        }

        binding.requestRecordAudioAndLocationPermissionsButton.setOnClickListener {
            requestRecordAudioAndLocationPermissionsLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECORD_AUDIO)
            )
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECORD_AUDIO),
//                RQ_PERMISSIONS_FOR_FEATURE_2_CODE
//            )
        }
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            RQ_PERMISSIONS_FOR_FEATURE_1_CODE ->{
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    onCameraPermissionGranted()
//                } else {
//                    if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
//                        askUserForOpeningAppSettings()
//                    } else {
//                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//            RQ_PERMISSIONS_FOR_FEATURE_2_CODE -> {
//                if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
//                    Toast.makeText(this, "Location & record permission granted", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }


    private fun onGotCameraPermissionResult(granted: Boolean) {
        if (granted) {
            onCameraPermissionGranted()
        } else {
            // example of handling 'Deny & don't ask again' user choice
            if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                askUserForOpeningAppSettings()
            } else {
                Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onGotRecordAudioAndLocationPermissionsResult(grantResults: Map<String, Boolean>) {
        if (grantResults.entries.all { it.value }) {
            onRecordAudioAndLocationPermissionsGranted()
        }
    }

    private fun askUserForOpeningAppSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
        if (packageManager.resolveActivity(appSettingsIntent, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            Toast.makeText(this, "Permissions are denied forever", Toast.LENGTH_SHORT).show()
        } else {
            AlertDialog.Builder(this)
                .setTitle("Permissions denied")
                .setMessage("You have denied permission forever. " +
                        "You can change your decision in app settings.\n\n" +
                        "Would you like to open app settings?")
                .setPositiveButton("Open") {_, _ ->
                    startActivity(appSettingsIntent)
                }
                .create()
                .show()
        }
    }

    private fun onRecordAudioAndLocationPermissionsGranted() {
        Toast.makeText(this, R.string.audio_and_location_permissions_granted, Toast.LENGTH_SHORT).show()
    }
    private fun onCameraPermissionGranted() {
        Toast.makeText(this, "Camera permission is granted", Toast.LENGTH_SHORT).show()
    }

//    private companion object{
//        const val RQ_PERMISSIONS_FOR_FEATURE_1_CODE = 1
//        const val RQ_PERMISSIONS_FOR_FEATURE_2_CODE = 2
//    }
}