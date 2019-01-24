package com.fivestars.communication


import android.content.SharedPreferences
import org.apache.cordova.CallbackContext
import org.apache.cordova.CordovaInterface
import org.apache.cordova.CordovaPlugin
import org.apache.cordova.CordovaWebView
import org.json.JSONArray
import org.json.JSONException


/**
 * This class echoes a string called from JavaScript.
 */
class CommunicationPlugin : CordovaPlugin() {

    private var purchaseCount: Int = 0
    private var sharedPreferences: SharedPreferences? = null

    override fun initialize(cordova: CordovaInterface?, webView: CordovaWebView?) {
        super.initialize(cordova, webView)

        sharedPreferences = cordova?.context?.getSharedPreferences(SHARED_PREFERENCES, 0)
        purchaseCount = sharedPreferences?.getInt(KEY_PURCHASE_COUNT, 0) ?: 0
    }

    @Throws(JSONException::class)
    override fun execute(action: String, args: JSONArray, callbackContext: CallbackContext): Boolean {
        if (action == "makePurchase") {
            this.makePurchase(callbackContext)
            return true
        } else if (action == "queryPurchaseCount") {
            queryPurchaseCount(callbackContext)
            return true
        }
        return false
    }

    private fun queryPurchaseCount(callbackContext: CallbackContext) {
        callbackContext.success(purchaseCount)
    }

    private fun makePurchase(callbackContext: CallbackContext) {
        sharedPreferences?.edit()?.putInt(KEY_PURCHASE_COUNT, ++purchaseCount)?.apply()
        callbackContext.success(purchaseCount)
    }

    companion object {

        private const val SHARED_PREFERENCES = "com.fivestars.sharedpreferences"
        private const val KEY_PURCHASE_COUNT = "purchase_count"
    }
}
