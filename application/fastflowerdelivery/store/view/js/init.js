// global vars
var LOGIN_NAME = "FlowerStore1";
var ASSIGNMENT_METHOD = "manual"; // value can be "manual" or "automatic"
var POLL_DELAY = 1000; // 1 second

// Globally initializes all Packages that need initialization
// Registers onclick handlers
var initialize = function() {
	Request.init();
	
	// Set initial data
	$("input[name='customer']").val('Heinrich');
	$("input[name='hood']").val('Wechloy');
	$("input[name='pickup']").val('10');				
	$("input[name='delivery']").val('30');
};

// Entrypoint of the script: Calls the Initialize function as soon as the document is ready
$(document).ready(initialize);