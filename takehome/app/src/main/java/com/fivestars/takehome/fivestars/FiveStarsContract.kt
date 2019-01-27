package com.fivestars.takehome.fivestars

import org.apache.cordova.CallbackContext

interface FiveStarsContract {
    interface View {
        fun showTime(time: String)
    }
    interface Presenter {
        fun execute(action: String, callbackContext: CallbackContext)
    }
}