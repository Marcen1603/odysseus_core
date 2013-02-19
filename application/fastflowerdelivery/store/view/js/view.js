/**
 * Handles the updating of the view 
 * Most important is creation of and switching between pages
 */ 
var View = {
	init: function() {
		$('#new').click(function() {
			View.openNewRequest();
		});
	},
	
	addPage : function(id) {
		$('.menu').append('<div class="item" id="edit' + id + '" style="display: none;">Manage Delivery ' + id + '</div>');
		$('#edit' + id).click(function() {
			View.openPage(id);
		});
		$('#edit' + id).slideDown(400);
	},
	
	removePage : function(id) {
		$('.menu #edit' + id).remove();
	},
	
	openNewRequest: function() {
		$('.page').children().hide();
		$('.newRequest').show();
	},
	
	openHome: function() {
		$('.page').children().hide();
		$('.home').show();
	},
	
	openPage: function(id) {
		$('.page').children().hide();
		
		// Get data
		var deliveryRequest = Data.get(id);
		
		if(deliveryRequest == null) {
			$('.home').show();				
		} else {
			$('.editRequest .requestId').html(deliveryRequest.requestId);
			$('.editRequest .requestCustomer').html(deliveryRequest.adresseeLocation);
			$('.editRequest .requestNeighborhood').html(deliveryRequest.neighborhood);
			$('.editRequest .requestPickup').html(Data.timestampToTime(deliveryRequest.requiredPickupTime));
			$('.editRequest .requestDelivery').html(Data.timestampToTime(deliveryRequest.requiredDeliveryTime));
			var assignmentDone = false;
			if(deliveryRequest.drivers.length != 0) {
				$('.editRequest .drivers .driver').remove();
				var driver = null;
				for(var i = 0; i < deliveryRequest.drivers.length; i++) {
					driver = deliveryRequest.drivers[i];
					$('.editRequest .drivers').append('<div class="driver'+(driver.assigned ? ' assigned' : '')+'">'+driver.reference+'</div>');
					if(driver.assigned)
						assignmentDone = true;
				}
			} else {
				$('.editRequest .drivers .driver').remove();
				$('.editRequest .drivers').append('<div class="driver">Currently in bid phase...</div>');
			}
			if(assignmentDone) {
				$('.editRequest .drivers .driver').not('.assigned').remove();
				$('.driverPickup').show();
			} else {
				$('.editRequest .drivers .driver').not('.assigned').click(function() {
					Request.assignDriver($(this));
				});
				$('.driverPickup').hide();
			}
			
			if(deliveryRequest.pickedUp)
				$('.driverPickup').hide();
			
			$('.editRequest').show();
		}
	}
};

/**
 * Shows a Dialog containing an information or an error
 */
var Dialog = {

	showDialog : function(title, content, bInfo, callback) {
		if(bInfo)
			$('.dialogIcon').removeClass("error").addClass("info");
		else
			$('.dialogIcon').removeClass("info").addClass("error");
		
		$("#showDialog").slideDown(800);
		$("#showDialog").click(function(){$("#showDialog").slideUp(200); if(typeof callback != "undefined") callback();});
		$('.dialogContent').html(content);
		$('#showDialog div.title').html(title);
	}

};