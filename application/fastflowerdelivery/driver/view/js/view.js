/**
 * Handles the updating of the view 
 * Most important is switching between pages
 */ 
var View = {
	
	init : function() {
		$('.requests').click(function() {
			View.openDeliveries();
		});
		$('.activeRequests').click(function() {
			View.openActive();
		});
	},
	
	refresh : function(){
		View.refreshRequests();
		View.refreshActive();
	},
		
	refreshRequests : function() {
		var html = "";
		var r;
		for(var i = 0; i < Data.requests.length; i++) {
			r = Data.requests[i];
			if(!r.assigned)
				html += '<div class="delivery'+(r.bidPlaced ? " bidded" : "")+'" id="delivery'+r.requestId+'">'
					 + '<div class="store"><span class="store">'+r.storeReference+'</span> #<span class="requestId">'+r.requestId+'</span></div>'
					 + '<div class="customer">'+r.adresseeLocation+'</div>'
					 + '<div class="time">Pickup: <span class="pickupTime">'+Data.timestampToTime(r.requiredPickupTime)+'</span> Delivery: <span class="deliveryTime">'+Data.timestampToTime(r.requiredDeliveryTime)+'</span></div>'
					 + '</div>';
		}
		$('.deliveries').html(html);
		// Hang in click handlers
		for(var i = 0; i < Data.requests.length; i++) {
			r = Data.requests[i];
			if(!r.assigned && !r.bidPlaced)
				$('#delivery' + r.requestId).one('click', Request.placeBid); 
		}
	},
	
	refreshActive : function() {
		var html = "";
		var r;
		for(var i = 0; i < Data.requests.length; i++) {
			r = Data.requests[i];
			if(r.assigned && r.assignedTo == LOGIN_NAME)
				html += '<div class="delivery'+(r.pickedUp ? " bidded" : "")+'" id="active'+r.requestId+'">'
					 + '<div class="store"><span class="store">'+r.storeReference+'</span> #<span class="requestId">'+r.requestId+'</span></div>'
					 + '<div class="customer">'+r.adresseeLocation+'</div>'
					 + '<div class="time">Pickup: <span class="pickupTime">'+Data.timestampToTime(r.requiredPickupTime)+'</span> Delivery: <span class="deliveryTime">'+Data.timestampToTime(r.requiredDeliveryTime)+'</span></div>'
					 + '</div>';
		}
		$('.active').html(html);
		// Hang in click handlers
		for(var i = 0; i < Data.requests.length; i++) {
			r = Data.requests[i];
			if(r.assigned && r.assignedTo == LOGIN_NAME && r.pickedUp)
				$('#active' + r.requestId).one('click', Request.delivered).css('cursor', 'pointer');
		}
	},
	
	openDeliveries : function() {
		$('.page').children().hide();
		$('.deliveries').show();
		$('.requests').css('background-color', '#404080');
		$('.activeRequests').css('background-color', '#202020');
	},
	
	openActive : function() {		
		$('.page').children().hide();
		$('.active').show();
		$('.requests').css('background-color', '#202020');
		$('.activeRequests').css('background-color', '#404080');
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
		
		$("#showDialog").show();
		$("#showDialog").one("click",function(){$("#showDialog").hide(); if(typeof callback != "undefined") callback();});
		$('.dialogContent').html(content);
		$('#showDialog div.title').html(title);
	}

};