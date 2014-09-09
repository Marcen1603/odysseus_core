package de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter;




public class SportsQLSpeedParameter implements ISportsQLParameter {
	
	public static final String SPEED_PARAMETER_STANDING = "standing";
	public static final String SPEED_PARAMETER_TROT = "trot";
	public static final String SPEED_PARAMETER_LOW = "low";
	public static final String SPEED_PARAMETER_MEDIUM = "medium";
	public static final String SPEED_PARAMETER_HIGH = "high";
	public static final String SPEED_PARAMETER_SPRINT = "sprint";


	private String speed; // For "standing", "trot", ...

	public SportsQLSpeedParameter(String speed) {
		this.speed = speed;
	}
	
	public SportsQLSpeedParameter() {

	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	
}
