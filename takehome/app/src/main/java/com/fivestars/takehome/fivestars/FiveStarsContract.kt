package com.fivestars.takehome.fivestars

import com.fivestars.takehome.fivestars.model.AccountDetails
import com.fivestars.takehome.fivestars.model.Time

interface FiveStarsContract {
    interface View {
        fun setTimeCallback()
        fun setTime(time: Time)
        fun setAccountDetailsModel(accountDetails: AccountDetails)
    }

    interface Presenter {
        fun onViewEvent(event: String)
    }
}