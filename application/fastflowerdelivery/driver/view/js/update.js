/**
 * Handles any updates from the server.
 * To assure live updates, a webservice on the serverside is polled frequently.
 * Also manages any initial data. Otherwise on page refresh everything would be lost.
 */ 
var Update = {
	init : function() {
		// Start polling
		var interval = setInterval(Update.poll, POLL_DELAY);
		
		// Retrieve initial data (otherwise active deliveries go lost on refresh)
		ajax("/initial", { 'driver': LOGIN_NAME }, Update.initial);
	},
	
	poll : function() {
		ajax("/update", { 'driver': LOGIN_NAME }, Update.update);
		View.updateActive();
	},
	
	update : function(data) {
		var event = null;
		for(var i = 0; i < data.events.length; i++) {
			event = data.events[i];
			if(event.type == "BidRequest")
				Update.bidRequest(event);
			if(event.type == "Assignment")
				Update.assignment(event);
			if(event.type == "PickupConfirmation")
				Update.pickup(event);
			if(event.type == "IdleDriverReport")
				Update.rated("idle driver");
			if(event.type == "ImprovingDriverReport")
				Update.rated("improving driver");
			if(event.type == "PermanentWeakDriverReport")
				Update.rated("permanent weak driver");
			if(event.type == "ConsistentWeakDriverReport")
				Update.rated("consistent weak driver");
			if(event.type == "ConsistentStrongDriverReport")
				Update.rated("consistent strong driver");
			if(event.type == "ImprovingNote")
				Update.improved();
			if(event.type == "DeliveryRequestCancellation")
				Update.cancel(event);
		}
	},
	
	cancel : function(event) {		
		var params = {
				'requestId' : event.requestId,
				'driver' : LOGIN_NAME,
				'internal': true
			};
			
		ajax("/delivered", params, function(data) {
			Data.remove(event.requestId);
			View.refreshActive();
			View.refreshRequests();
			Dialog.showDialog("Delivery Cancelled", "The delivery #"+event.requestId+" has been cancelled.", true);
		});
	},
	
	pickup : function(event) {
		var request = Data.get(event.requestId);
		request.pickedUp = true;
		$('#active' + event.requestId).addClass('bidded');
		View.refresh();
	},
	
	rated : function(rating) {
		Dialog.showDialog("Driver Report", "You have been rated "+rating+" this month.", true);
	},
	
	improved : function(rating) {
		Dialog.showDialog("Improving", "Your ratings are improving!.", true);
	},
	
	assignment : function(event) {
		var request = Data.get(event.requestId);
		request.assigned = true;
		request.assignedTo = event.driverReference;
		View.refresh();
		if(event.driverReference == LOGIN_NAME) {
			Dialog.showDialog("Assignment", request.storeReference + "has chosen you to deliver request #" + request.requestId, true);
			View.openActive();
		}
	},
	
	bidRequest : function(event) {
		if(event.driverReference == LOGIN_NAME) {
			Data.addRequest(event);
			View.refreshRequests();
		}
	},
	
	initial : function(data) {
		var event = null;
		for(var i = 0; i < data.events.length; i++) {
			event = data.events[i];
			if(event.driverReference == LOGIN_NAME)
				Data.addRequest(event);
		}
		View.refresh();
	}
};

/**
 * Handles any requests to the webserver from forms and buttons
 */ 
var Request = {
		
	init : function() {
		$('.login').submit(function() {
			
			var params = {"username" : $("input[name='name']").val()};
			
			ajax("/login", params, function(data) {
				LOGIN_NAME = params.username;
				Update.init();
				View.init();
				View.openDeliveries();
			});
			
			return false;
		});
	},
		
	placeBid : function(jqry) {	
		var id = $(this).find('.requestId').html()
		var params = { 
			'requestId' : id,
			'driver' : LOGIN_NAME
		};
		
		ajax("/placeBid", params, function(data) {
			$('#delivery' + id).addClass('bidded');
			Data.get(id).bidPlaced = true;
		});
	},
	
	delivered : function(jqry) {
		var id = $(this).find('.requestId').html()
		var params = { 
			'requestId' : id,
			'driver' : LOGIN_NAME,
			'internal': false
		};
		
		ajax("/delivered", params, function(data) {
			Data.remove(id);
			View.refreshActive();
		});
	}

};

/**
 * global function for ease of use of ajax
 */
var ajax = function(uri, params, callback) {
	$.ajax({
		url: uri,
		cache: false,
		dataType: "json",
		data: params,
		success: function(data) {		
			if(data.hasOwnProperty("error")) {
				Dialog.showDialog("Error", data.error, false);
			} else {
				callback(data);
			}
		},
		type: "post"
	});
};