package com.fivestars.communication


import org.apache.cordova.CallbackContext
import org.apache.cordova.CordovaPlugin
import org.json.JSONArray
import org.json.JSONException


/**
 * This class echoes a string called from JavaScript.
 */
class CommunicationPlugin : CordovaPlugin() {

    private var communicationListener: CommunicationPlugin.CommunicationListener? = null


    @Throws(JSONException::class)
    override fun execute(action: String, args: JSONArray, callbackContext: CallbackContext): Boolean {
        communicationListener?.let {
            it.execute(action, callbackContext)
        }
        return true
    }

    fun setCommunicationListener(communicationListener: CommunicationListener) {
        this.communicationListener = communicationListener
    }

    interface CommunicationListener {
        fun execute(action: String, callbackContext: CallbackContext)
    }
}
