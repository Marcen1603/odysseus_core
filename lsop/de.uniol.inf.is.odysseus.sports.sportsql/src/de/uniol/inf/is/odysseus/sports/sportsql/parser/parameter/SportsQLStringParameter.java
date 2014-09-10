package de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter;


public class SportsQLStringParameter implements ISportsQLParameter {
	
	//Speed
	public static final String SPEED_PARAMETER_STANDING = "standing";
	public static final String SPEED_PARAMETER_TROT = "trot";
	public static final String SPEED_PARAMETER_LOW = "low";
	public static final String SPEED_PARAMETER_MEDIUM = "medium";
	public static final String SPEED_PARAMETER_HIGH = "high";
	public static final String SPEED_PARAMETER_SPRINT = "sprint";
	
	//Output
	public static final String OUTPUT_PARAMETER_OVERVIEW = "overview";
	public static final String OUTPUT_PARAMETER_DETAIL = "detail";
	public static final String OUTPUT_PARAMETER_PATH = "path";
	
	private String value;

	public SportsQLStringParameter(String value) {
		this.value = value;
	}
	
	public SportsQLStringParameter() {

	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
