package de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter;




public class SportsQLDistanceParameter implements ISportsQLParameter {
	
	public static final String DISTANCE_PARAMETER_SHORT = "short";
	public static final String DISTANCE_PARAMETER_LONG = "long";
	public static final String DISTANCE_PARAMETER_ALL = "all";

	private int minDistance;
	private int maxDistance;
	private String distance; // For "short", long, ...

	public SportsQLDistanceParameter(int minDistance, int maxDistance, String distance) {
		this.minDistance = minDistance;
		this.maxDistance = maxDistance;
		this.distance = distance;
	}
	
	public SportsQLDistanceParameter() {

	}

	public int getMinDistance() {
		return minDistance;
	}

	public void setMinDistance(int minDistance) {
		this.minDistance = minDistance;
	}

	public int getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(int maxDistance) {
		this.maxDistance = maxDistance;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	

}
