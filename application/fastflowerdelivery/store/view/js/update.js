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
		ajax("/initial", { 'store': LOGIN_NAME }, Update.initial);
	},
	
	poll : function() {
		ajax("/update", { 'store': LOGIN_NAME }, Update.update);
	},
	
	update : function(data) {
		var event = null;
		for(var i = 0; i < data.events.length; i++) {
			event = data.events[i];
			if(event.type == "Driver")
				Update.addDriver(event);
			if(event.type == "DeliveryConfirmation")
				Update.confirmDelivery(event);
			if(event.type == "ManualAssignmentTimeoutAlert")
				Update.alertTimeout(event);
			if(event.type == "PickupAlert")
				Update.alertPickup(event);
			if(event.type == "DeliveryAlert")
				Update.alertDelivery(event);
		}
	},
	
	alertTimeout : function(alert) {
		Dialog.showDialog("Alert", "You have passed the maximum time required to assign a driver.", false, function() { View.openPage(alert.requestId);});
	},
	
	alertPickup : function(alert) {
		Dialog.showDialog("Alert", "Driver "+alert.driverReference+" has passed the maximum time required to pickup the flowers.", false, function() { View.openPage(alert.requestId);});
	},
	
	alertDelivery : function(alert) {
		Dialog.showDialog("Alert", "Driver "+alert.driverReference+" has passed the maximum time required to deliver the flowers.", false, function() { View.openPage(alert.requestId);});
	},
	
	addDriver : function(driver) {
		Data.get(driver.requestId).drivers.push(driver)
		Dialog.showDialog("New Driver", "A driver has placed a bid on Delivery " + driver.requestId + ".", true, function() { View.openPage(driver.requestId);});
	},
	
	confirmDelivery : function(event) {
		Dialog.showDialog("Delivery Confirmation", event.driverReference + " has fulfilled your Delivery " + event.requestId + " successfully.", true, function() { 
			View.openHome();
			Data.remove(event.requestId);
			View.removePage(event.requestId);
		});
	},
	
	initial : function(data) {		
		for(var i = 0; i < data.events.length; i++) {
			Data.addDeliveryRequest(data.events[i]);
			View.addPage(data.events[i].requestId);
		}
	}
};

/**
 * Handles any requests to the webserver from forms and buttons
 */ 
var Request = {
	
	init: function() {
		
		// Login
		$('.login').submit(function() {
			
			var params = {"username" : $("input[name='name']").val()};
			
			ajax("/login", params, function(data) {
				LOGIN_NAME = params.username;
				Update.init();
				View.init();
				$('.login').hide();
			});
			
			return false;
		});
		
		// Delivery Request Message
		$(".deliveryRequest").submit(function() {
		
			var customer = $("input[name='customer']").val();
			var hood = $("input[name='hood']").val();
			var pickup = $("input[name='pickup']").val();
			var delivery = $("input[name='delivery']").val();
			
			var params = { 
				'Customer': customer,
				'Neighborhood': hood,
				'PickupOffset': pickup,
				'DeliveryOffset' : delivery,
				'store' : LOGIN_NAME
			};
			
			ajax("/deliveryRequest", params, function(data) {
				var dr = data.event;
				Data.addDeliveryRequest(dr);
				Dialog.showDialog("Success", "Delivery Request " + dr.requestId + " has been successfully created.", true, function() {
					View.addPage(dr.requestId);
				});
				
				$("input[name='customer']").val('Heinrich');
				$("input[name='hood']").val('Wechloy');
				$("input[name='pickup']").val('10');				
				$("input[name='delivery']").val('30');
			});
			
			return false;
		
		});
		
		// Driver Pickup Message
		$(".driverPickup").submit(function() {
			
			var driverReference = $('.editRequest .assigned').html();
			var requestId = $('.editRequest .requestId').html();
			
			var params = { 
					'driverReference' : driverReference,
					'requestId' : requestId,
					'storeReference' : LOGIN_NAME
				};
			
			ajax("/pickup", params, function(data) {

				Data.get(requestId).pickupDone = true;
				
				View.openPage(requestId);
			});
			
			return false;
		});
		
		// Cancel Request Message
		$(".cancelDeliveryRequest").submit(function() {
			
			var requestId = $('.editRequest .requestId').html();
			
			var params = { 'requestId' : requestId };
			
			ajax("/cancel", params, function(data) {
				Dialog.showDialog("Delivery Cancelled", "This delivery has been cancelled.", true, function() {
					View.openHome();
					Data.remove(requestId);
					View.removePage(requestId);
				});
			});
			
			return false;
		});
	},
	
	assignDriver : function(jqry) {
		var driverReference = jqry.html();
		var requestId = $('.editRequest .requestId').html();
		
		var params = { 
				'driverReference' : driverReference,
				'requestId' : requestId,
				'store' : LOGIN_NAME
			};
		
		ajax("/assignment", params, function(data) {
			
			// Assign the driver on clientSide if it was accepted on serverside
			var drivers = Data.get(requestId).drivers;
			for(var i = 0; i < drivers.length; i++)
				if(drivers[i].reference == driverReference)
					drivers[i].assigned = true;
			
			View.openPage(requestId);
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