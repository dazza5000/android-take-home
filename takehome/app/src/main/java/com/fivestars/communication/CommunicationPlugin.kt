package com.fivestars.communication


import android.content.SharedPreferences
import org.apache.cordova.CallbackContext
import org.apache.cordova.CordovaInterface
import org.apache.cordova.CordovaPlugin
import org.apache.cordova.CordovaWebView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


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
        when (action) {
            "makePurchase" -> {
                this.makePurchase(callbackContext)
                return true
            }
            "queryPurchaseCount" -> {
                queryPurchaseCount(callbackContext)
                return true
            }
            "redeemAward" -> {
                redeemAward(callbackContext)
                return true
            }
            else -> return false
        }
    }

    private fun queryPurchaseCount(callbackContext: CallbackContext) {
        callbackContext.success(getTransactionResponsePayload(purchaseCount).toString())
    }

    private fun makePurchase(callbackContext: CallbackContext) {
        sharedPreferences?.edit()?.putInt(KEY_PURCHASE_COUNT, ++purchaseCount)?.apply()
        val jsonObject = getTransactionResponsePayload(purchaseCount)
        callbackContext.success(jsonObject.toString())
    }

    private fun redeemAward(callbackContext: CallbackContext) {
        purchaseCount = 0
        sharedPreferences?.edit()?.putInt(KEY_PURCHASE_COUNT, purchaseCount)?.apply()
        val jsonObject = getTransactionResponsePayload(purchaseCount)
        jsonObject.put("rewardRedeemed", true)
        callbackContext.success(jsonObject.toString())
    }

    private fun getTransactionResponsePayload(purchaseCount: Int): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("purchaseCount", purchaseCount)
        jsonObject.put("rewardLevel", getRewardLevel(purchaseCount))
        return jsonObject
    }

    private fun getRewardLevel(purchaseCount: Int) : String {
        var level: Int = purchaseCount / 5
        return when(level) {
            0 -> "Never Give Up"
            1 -> "Bronze"
            2 -> "Silver"
            3 -> "Gold"
            4 -> "Platinum"
            5 -> "Diamond"
            6 -> "Double Black Diamond"
            else -> "Elon Musk"
        }
    }

    companion object {

        private const val SHARED_PREFERENCES = "com.fivestars.sharedpreferences"
        private const val KEY_PURCHASE_COUNT = "purchase_count"
    }
}
