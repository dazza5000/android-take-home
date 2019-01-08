# android-take-home

## Preamble

This repo has a working version of cordova-android and a sample plugin already initialized, you should be able to open up android studio and just run the project.

(When opening the project in Android studio do not just open the root directory, you should open the `takehome` directory and open the build.gradle)

There are two gradle modules included, the main application and the CordovaLib. Both will need to be modified.

## Problem Statement

Given this starting code, the goal is to host the Cordova application in a "Chat Head" style overlay. [example](https://medium.com/@kevalpatel2106/create-chat-heads-like-facebook-messenger-32f7f1a62064)

Once the sample Cordova application is running within the chat head, the secondary goal is to establish bi-directional communication with the web application using a Cordova Plugin.
The plugin is already created for you, called `CommunicationPlugin`, you'll just need to modify this.

Showcase the bi-directional communication by updating the Webapp UI from a Native-land button, and update the Native UI from a Webapp-land button. Get creative, and have fun with it!

## Notes

- The included `CordovaLib` module will need to be modified
- Cordova is designed to work within an Android Activity, however chat heads work by running in an Android Service
  - You'll need to replicate (copy/paste and modify) the setup that Cordova provides in the `CordovaActivity` (mostly setting up some plumbing and creating/managing the actual webview)
  - Given that Cordova will internally use an Activity for things, you will have to modify the Cordova framework to NOT use an activity, possibly replacing with comparable code
  - Not all of these need to be completely solved to get the bare minimum service running
- Cordova uses Plugins to enable communication to the native side, you should be using that mechanism in order to get bi-directional communication.
  - It's up to you what messages you want to pass and how you demonstrate bi-directional communication
  - [plugin documentation](https://cordova.apache.org/docs/en/latest/guide/hybrid/plugins)
  - There should be enough functionality in Cordova to enable bi-directional communication without using a message bus library or something similar. Reading the `CordovaLib` source code should help.
- The web app code will be located under `assets/www`
