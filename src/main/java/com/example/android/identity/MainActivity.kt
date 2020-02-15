package com.example.android.identity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import me.dm7.barcodescanner.zxing.ZXingScannerView.ResultHandler
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), ResultHandler {

    private val REQUEST_CAMERA = 1
    private var scannerView: ZXingScannerView? = null


    override fun handleResult(p0: Result?) {

        val result: String? = p0?.text
        val vibrator:Vibrator = applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(1000)

//        val builder = AlertDialog.Builder(this)
//        builder.setTitle("Result")
//        builder.setPositiveButton("OK") {dialog, wihch ->
//            scannerView?.resumeCameraPreview(this@MainActivity)
//            startActivity(intent)
//
//        }
//        builder.setMessage(result)
//        val alert:AlertDialog = builder.create()
//        alert.show()


        val intent = Intent(this, ActivityShow::class.java)

        intent.putExtra("message", result)

        startActivity(intent)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scannerView= ZXingScannerView(this)
        setContentView(scannerView)

        if (!checkPermission())
            requestPermision()


    }

    private fun checkPermission() : Boolean {
        return ContextCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermision(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA)


    }


    override fun onResume() {
        super.onResume()
        if (checkPermission()) {
            if (scannerView == null){
                scannerView = ZXingScannerView(this)
                setContentView(scannerView)

            }
            scannerView?.setResultHandler(this)
            scannerView?.startCamera()

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        scannerView?.stopCamera()
    }




    fun verifyAvailableNetwork(activity:AppCompatActivity):Boolean{
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }


}
