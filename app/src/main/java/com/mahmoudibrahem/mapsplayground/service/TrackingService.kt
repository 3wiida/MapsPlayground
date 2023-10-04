package com.mahmoudibrahem.mapsplayground.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.mahmoudibrahem.mapsplayground.R
import com.mahmoudibrahem.mapsplayground.ui.main.MainActivity
import com.mahmoudibrahem.mapsplayground.util.Constants.NOTIFICATION_CHANNEL_ID
import com.mahmoudibrahem.mapsplayground.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.mahmoudibrahem.mapsplayground.util.Constants.NOTIFICATION_ID
import com.mahmoudibrahem.mapsplayground.util.Constants.PENDING_INTENT_ACTION
import com.mahmoudibrahem.mapsplayground.util.Constants.PENDING_INTENT_REQ_CODE
import com.mahmoudibrahem.mapsplayground.util.Constants.TRACKING_SERVICE_ACTION_START
import com.mahmoudibrahem.mapsplayground.util.Constants.TRACKING_SERVICE_ACTION_STOP
import com.mahmoudibrahem.mapsplayground.util.tracker_util.TrackerUtil
import java.util.Timer
import java.util.TimerTask

class TrackingService : LifecycleService() {

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null
    private var notificationManager: NotificationManager? = null
    private var notification: NotificationCompat.Builder? = null
    private val timer = Timer()

    companion object {
        val steps = MutableLiveData<MutableList<LatLng>>()
        val elapsedTime = MutableLiveData<String>()
        var isRunning = false
    }

    override fun onCreate() {
        super.onCreate()
        steps.postValue(mutableListOf())
        elapsedTime.postValue("00:00")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                result.lastLocation?.let {
                    val lastLatLng = LatLng(it.latitude, it.longitude)
                    steps.value?.apply {
                        add(lastLatLng)
                        steps.postValue(this)
                    }
                    updateNotification(
                        TrackerUtil.calculateDistance(
                            steps.value!!.first(),
                            steps.value!!.last()
                        )
                    )
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            TRACKING_SERVICE_ACTION_START -> {
                isRunning = true
                steps.postValue(mutableListOf())
                startForegroundService()
                TrackerUtil.getLocationUpdates(fusedLocationClient!!, locationCallback!!)
                startElapsedTime(0, 0)
            }

            TRACKING_SERVICE_ACTION_STOP -> {
                fusedLocationClient?.removeLocationUpdates(locationCallback!!)
                stopSelf()
                (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).cancel(
                    NOTIFICATION_ID
                )
                isRunning = false
                timer.cancel()
            }

            else -> {}
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {
        createNotificationChannel()
        createNotification()
        startForeground(
            NOTIFICATION_ID,
            notification?.build()
        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager?.createNotificationChannel(notificationChannel)
        }
    }

    private fun createNotification() {
        notification = NotificationCompat.Builder(
            applicationContext,
            NOTIFICATION_CHANNEL_ID
        ).apply {
            setAutoCancel(false)
            setOngoing(true)
            setSilent(true)
            setContentTitle("Maps Playground")
            setContentText("Traveled distance is 0 Km")
            setSmallIcon(R.drawable.notificaiton_icon)
            setContentIntent(
                PendingIntent.getActivity(
                    applicationContext,
                    PENDING_INTENT_REQ_CODE,
                    Intent(applicationContext, MainActivity::class.java).apply {
                        this.action = PENDING_INTENT_ACTION
                    },
                    if (Build.VERSION.SDK_INT >= 31) {
                        PendingIntent.FLAG_MUTABLE
                    } else {
                        PendingIntent.FLAG_UPDATE_CURRENT
                    }
                )
            )
        }
    }

    private fun updateNotification(distance: String) {
        notification?.apply {
            setContentText("Traveled distance is $distance Km")
        }
        notificationManager?.notify(NOTIFICATION_ID, notification?.build())
    }

    private fun startElapsedTime(
        min: Int,
        sec: Int,
    ) {
        var minCounter = min
        var secCounter = sec
        var minResult: String
        var secResult: String
        timer.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    secCounter += 1
                    if (secCounter == 60) {
                        minCounter += 1
                        secCounter = 0
                    }
                    secResult = if (secCounter.toString().length < 2) {
                        "0$secCounter"
                    } else {
                        "$secCounter"
                    }
                    minResult = if (minCounter.toString().length < 2) {
                        "0$minCounter"
                    } else {
                        "$minCounter"
                    }
                    elapsedTime.postValue("$minResult:$secResult")
                }
            },
            0,
            1000
        )
    }


    override fun onDestroy() {
        super.onDestroy()
        notification = null
        notificationManager = null
        fusedLocationClient?.removeLocationUpdates(locationCallback!!)
        locationCallback = null
        fusedLocationClient = null
    }
}