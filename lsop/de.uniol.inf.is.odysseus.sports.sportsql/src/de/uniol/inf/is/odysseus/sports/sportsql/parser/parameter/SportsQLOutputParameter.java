package de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter;




public class SportsQLOutputParameter implements ISportsQLParameter {
	
	public static final String OUTPUT_PARAMETER_OVERVIEW = "overview";
	public static final String OUTPUT_PARAMETER_DETAIL = "detail";
	public static final String OUTPUT_PARAMETER_PATH = "path";



	private String output; // For "overview", "detail", "path"

	public SportsQLOutputParameter(String output) {
		this.output = output;
	}
	
	public SportsQLOutputParameter() {

	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	
}
