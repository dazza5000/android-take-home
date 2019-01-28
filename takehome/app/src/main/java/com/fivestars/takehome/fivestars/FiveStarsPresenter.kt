package com.fivestars.takehome.fivestars

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import com.fivestars.takehome.fivestars.model.AccountDetails
import com.fivestars.takehome.fivestars.model.Time
import java.text.SimpleDateFormat
import java.util.*

// With more time I would abstract SharedPreferences to get the context out of the presenter and unit test
// the presenter
class FiveStarsPresenter(context: Context, val view: FiveStarsContract.View) : FiveStarsContract.Presenter {

    private var purchaseCount: Int = 0
    private var sharedPreferences: SharedPreferences? = null
    private var clockStarted = false

    init {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, 0)
        purchaseCount = sharedPreferences?.getInt(KEY_PURCHASE_COUNT, 0) ?: 0

    }

    override fun onViewEvent(event: String) {
        when (event) {
            "makePurchase" -> {
                this.makePurchase()
            }
            "queryPurchaseCount" -> {
                queryPurchaseCount()
            }
            "redeemAward" -> {
                redeemAward()
            }
            "time" -> {
                startClock()
            }
        }
    }

    private fun startClock() {

        if (clockStarted) {
            return
        }
        view.setTimeCallback()
        clockStarted = true

        val handler = Handler(Looper.getMainLooper())

        val runThis = object : Runnable {
            override fun run() {
                view.setTime(getTimeResponsePayload())
                handler.postDelayed(this as Runnable, 500)
            }
        }

        handler.post(runThis)
    }

    private fun queryPurchaseCount() {
        view.setAccountDetailsModel(getTransactionResponsePayload(purchaseCount))
    }

    private fun makePurchase() {
        sharedPreferences?.edit()?.putInt(KEY_PURCHASE_COUNT, ++purchaseCount)?.apply()
        view.setAccountDetailsModel(getTransactionResponsePayload(purchaseCount))
    }

    private fun redeemAward() {
        purchaseCount = 0
        sharedPreferences?.edit()?.putInt(KEY_PURCHASE_COUNT, purchaseCount)?.apply()
        val accountDetails = getTransactionResponsePayload(purchaseCount)
        accountDetails.rewardRedeemed = true
        view.setAccountDetailsModel(accountDetails)
    }

    private fun getTransactionResponsePayload(purchaseCount: Int): AccountDetails {
        val showRedeemButton = (purchaseCount >= REWARD_INCREMENT)
        return AccountDetails(purchaseCount, getRewardLevel(purchaseCount), showRedeemButton)
    }

    fun getTimeResponsePayload(): Time {
        return Time(SimpleDateFormat("HH:mm").format(Date()))
    }

    private fun getRewardLevel(purchaseCount: Int): String {
        val level: Int = purchaseCount / REWARD_INCREMENT
        return when (level) {
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
        private const val REWARD_INCREMENT = 5
    }
}