package com.fivestars.takehome

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebViewClient
import com.fivestars.communication.CommunicationPlugin
import org.apache.cordova.*

import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

abstract class CordovaService : Service() {

    protected var mWindowManager: WindowManager? = null
    protected var contentView: View? = null
    private var appView: CordovaWebView? = null
    private var preferences: CordovaPreferences? = null
    private var cordovaInterface: CordovaInterfaceImpl? = null
    private var launchUrl: String? = null
    private var pluginEntries: ArrayList<PluginEntry>? = null
    protected var params: WindowManager.LayoutParams? = null

    internal abstract val layoutToInflate: Int

    internal abstract val appViewParentLayoutId: Int

    override fun onCreate() {
        super.onCreate()
        cordovaInterface = makeCordovaInterface()
        loadConfig()
        launchUrl?.let{
            loadUrl(it)
        }

        //Inflate the layout to show to the user
        contentView = LayoutInflater.from(this).inflate(layoutToInflate, null)

        (contentView?.findViewById<View>(appViewParentLayoutId) as ViewGroup).addView(appView?.view)

        //Add the view to the window.

        val viewSize = applicationContext.resources.getDimension(R.dimen.five_star_size).toInt()

        params = WindowManager.LayoutParams(
                viewSize,
                viewSize,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT)

        //Add the view to the window
        mWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        mWindowManager?.addView(contentView, params)

        var communicationPlugin = appView?.pluginManager?.getPlugin("CommunicationPlugin") as CommunicationPlugin

        val handler = Handler(Looper.getMainLooper())

        var count = 777

        var runThis = object : Runnable {
            override fun run() {
                communicationPlugin.callbackContext?.let{
                    Log.e("darran", "we have a callback context")
                    var result =  PluginResult(PluginResult.Status.OK, communicationPlugin.getTransactionResponsePayload(count).toString())
                    result.keepCallback = true
                    it.sendPluginResult(result)
                    count += 777
                }
                handler.postDelayed(this as Runnable, 1000)
            }
        }

        handler.post(runThis)
    }

    private fun loadUrl(url: String) {
        if (appView == null) {
            init()
        }
        appView?.loadUrlIntoView(url, true)
    }

    private fun loadConfig() {
        val parser = ConfigXmlParser()
        parser.parse(this)
        preferences = parser.preferences
        launchUrl = parser.launchUrl
        pluginEntries = parser.pluginEntries
        Config.parser = parser
    }

    private fun init() {
        appView = makeWebView()
        createViews()
        if (!appView!!.isInitialized) {
            appView!!.init(cordovaInterface, pluginEntries, preferences)
        }
        cordovaInterface?.onCordovaInit(appView!!.pluginManager)
    }

    //Suppressing warnings in AndroidStudio
    private fun createViews() {
        appView?.view?.requestFocusFromTouch()
    }

    /**
     * Construct the default web view object.
     *
     *
     * Override this to customize the webview that is used.
     */
    private fun makeWebView(): CordovaWebView {
        return CordovaWebViewImpl(makeWebViewEngine())
    }

    private fun makeWebViewEngine(): CordovaWebViewEngine {
        return CordovaWebViewImpl.createEngine(this, preferences)
    }

    private fun makeCordovaInterface(): CordovaInterfaceImpl {
        return object : CordovaInterfaceImpl(this) {
            override fun onMessage(id: String, data: Any): Any? {
                // Plumb this to CordovaActivity.onMessage for backwards compatibility
                return this@CordovaService.onMessage(id, data)
            }
        }
    }

    /**
     * Called when a message is sent to plugin.
     *
     * @param id   The message id
     * @param data The message data
     * @return Object or null
     */
    fun onMessage(id: String, data: Any): Any? {
        if ("onReceivedError" == id) {
            val d = data as JSONObject
            try {
                this.onReceivedError(d.getInt("errorCode"), d.getString("description"), d.getString("url"))
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        } else if ("exit" == id) {
            stopSelf()
        }
        return null
    }

    /**
     * Report an error to the host application. These errors are unrecoverable (i.e. the main resource is unavailable).
     * The errorCode parameter corresponds to one of the ERROR_* constants.
     *
     * @param errorCode   The error code corresponding to an ERROR_* value.
     * @param description A String describing the error.
     * @param failingUrl  The url that failed to load.
     */
    private fun onReceivedError(errorCode: Int, description: String, failingUrl: String) {
        val me = this

        val handler = Handler(Looper.getMainLooper())

        // If errorUrl specified, then load it
        val errorUrl = preferences?.getString("errorUrl", null)
        if (errorUrl != null && failingUrl != errorUrl && appView != null) {
            // Load URL on UI thread
            handler.post { me.appView?.showWebPage(errorUrl, false, true, null) }
        } else {
            val exit = errorCode != WebViewClient.ERROR_HOST_LOOKUP
            handler.post {
                if (exit) {
                    me.appView?.view?.visibility = View.GONE
                }
            }
        }// If not, then display error dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        if (contentView != null) mWindowManager?.removeView(contentView)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
