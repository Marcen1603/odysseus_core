package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.util.projection;

public class Mercator {
	    
	    private Mercator() {
	    }
	    
	 
	    public static int longToX(double longitudeDegrees, double radius) {
	        double longitude = Math.toRadians(longitudeDegrees);
	        return (int)(radius * longitude);
	    }
	    
	    public static int latToY(double latitudeDegrees, double radius) {
	        double latitude = Math.toRadians(latitudeDegrees);
	        double y = radius/2.0 *
	                Math.log((1.0+Math.sin(latitude))/
	                         (1.0-Math.sin(latitude)) );
	        return (int)y;
	    }
	    
	    public static double xToLong(int x, double radius) {
	        double longRadians = x/radius;
	        double longDegrees = Math.toDegrees(longRadians);
	        /* The user could have panned around the world a lot of times.
	        Lat long goes from -180 to 180.  So every time a user gets 
	        to 181 we want to subtract 360 degrees.  Every time a user
	        gets to -181 we want to add 360 degrees. */
	        int rotations = (int)Math.floor((longDegrees+180)/360);
	        double longitude = longDegrees - (rotations * 360);
	        return longitude;
	    }
	    
	    public static double yToLat(int y, double radius) {
	        double latitude = (Math.PI/2) -
	                (2 * Math.atan(
	                    Math.exp(-1.0 * y / radius)));
	        return Math.toDegrees(latitude);
	    }

}
