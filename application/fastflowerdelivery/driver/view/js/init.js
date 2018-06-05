// global vars
var LOGIN_NAME = "Hubert";
var POLL_DELAY = 1000; // 1 second

// Globally initializes all Packages that need initialization
var initialize = function() {
	Request.init();
};

// Entrypoint of the script: Calls the initialize function as soon as the document is ready
$(document).ready(initialize);