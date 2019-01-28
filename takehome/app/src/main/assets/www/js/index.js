/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
var app = {
    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicitly call 'app.receivedEvent(...);'
    onDeviceReady: function() {

        var clock = document.getElementById("time");
        var purchaseCountText = document.getElementById("purchase-count");
        var purchaseButton = document.getElementById("make-purchase-button");
        var rewardLevel = document.getElementById("reward-level");
        var redeemButton = document.getElementById("redeem-award-button");

        var plugin = "CommunicationPlugin";
        var transactionCallback = function(string) {
            var transactionResponse = JSON.parse(string);

            purchaseCountText.textContent = transactionResponse.purchaseCount;
            rewardLevel.textContent = transactionResponse.rewardLevel;
            
            if (transactionResponse.showRedeemButton === true) {
                redeemButton.style.visibility = "visible";
            } else {
                redeemButton.style.visibility = "hidden";
            }

            if (transactionResponse.rewardRedeemed) {
                alert("Congratulations. Here is your reward!");
            }
        };

        var timeCallback = function(string) {
            var transactionResponse = JSON.parse(string);
            clock.textContent = transactionResponse.time;
        }

        cordova.exec(timeCallback, function(err) {}, plugin, "time", null);

        // Fire of a query for the current purchase count
        cordova.exec(transactionCallback, function(err) {}, plugin, "queryPurchaseCount", null);

        function makePurchase() {
            cordova.exec(transactionCallback, function(err) {}, plugin, "makePurchase", null);
        };

        function redeemAward() {
            cordova.exec(transactionCallback, function(err) {}, plugin, "redeemAward", null);
         };

        purchaseButton.addEventListener("click", makePurchase);
        redeemButton.addEventListener("click", redeemAward);
    },
};

app.initialize();