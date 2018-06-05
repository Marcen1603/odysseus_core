/**
 * Handles clientside data management
 * Shares the models with serverside
 */ 
var Data = {
	requests : [],
	
	addRequest : function(event) {
		Data.requests.push(event);
	},
	
	get : function(id) {
		for (var i = 0; i < Data.requests.length; i++)
		    if(Data.requests[i].requestId == id)
		    	return Data.requests[i];
		return null;
	},
	
	remove : function(id) {
		var remove = -1;
		for (var i = 0; i < Data.requests.length; i++)
		    if(Data.requests[i].requestId == id)
		    	remove = i;
		if(remove != -1)
			Data.requests.splice(remove, 1);
	},
	
	timestampToTime : function(timestamp) {
		var date = new Date(timestamp);
		var hours = date.getHours();
		var minutes = date.getMinutes();

		var prependZero = function(digit) {
			if(digit < 10)
				return "0" + digit;
			else
				return "" + digit;
		};
		
		// will display time in 10:30 format
		return prependZero(hours) + ':' + prependZero(minutes);
	}
};