package com.fivestars.takehome.fivestars.model

data class AccountDetails(val purchaseCount: Int = 0,
                          val rewardLevel: String = "",
                          val showRedeemButton: Boolean,
                          var rewardRedeemed: Boolean = false)