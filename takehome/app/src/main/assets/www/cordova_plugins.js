cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [];
module.exports.metadata = 
// TOP OF METADATA
{
  "com.fivestars.communication": "1.0.0"
};
// BOTTOM OF METADATA
});

require('cordova/channel').onCordovaReady.subscribe(function() {
               require('cordova/exec')(win, null, 'CommunicationPlugin', 'messageChannel', []);
              }
);
