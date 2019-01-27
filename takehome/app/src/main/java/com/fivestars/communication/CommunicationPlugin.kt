package com.fivestars.communication


import org.apache.cordova.CallbackContext
import org.apache.cordova.CordovaPlugin
import org.json.JSONArray
import org.json.JSONException




/**
 * This class echoes a string called from JavaScript.
 */
class CommunicationPlugin : CordovaPlugin() {

    var callbackContext: CallbackContext? = null

    @Throws(JSONException::class)
    override fun execute(action: String, args: JSONArray, callbackContext: CallbackContext): Boolean {
        return true
    }
}
