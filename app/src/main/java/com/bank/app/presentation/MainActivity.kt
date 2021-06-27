package com.bank.app.presentation

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels()

    private var connectivityCallback: ConnectivityManager.NetworkCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.bank.bank_app.R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        if (Build.VERSION.SDK_INT < 23) {
            val window = window
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // finally change the color
            window.statusBarColor = ContextCompat.getColor(
                this,
                com.bank.bank_app.R.color.darkStatusBar
            )
        }
    }

    override fun onResume() {
        super.onResume()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            registerConnectivityManager()
//        }
    }

    override fun onPause() {
        super.onPause()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            unregisterConnectivityManager()
//        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun registerConnectivityManager() {
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val connectivityCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                //take action when network connection is gained
                Log.d("TAG", "onAvailable")
                viewModel.updateOnAvailable()
            }

            override fun onUnavailable() {
                super.onUnavailable()
                Log.d("TAG", "onUnavailable")
                viewModel.updateOnAvailable = true
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Log.d("TAG", "onLost")
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                Log.d("TAG", "onCapabilitiesChanged")
            }
        }
        connectivityManager.registerDefaultNetworkCallback(connectivityCallback)
        this.connectivityCallback = connectivityCallback
    }

    private fun unregisterConnectivityManager() {
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityCallback?.let {
            connectivityManager.unregisterNetworkCallback(it)
        }
    }
}