package de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter;


/**
 * {@link AbstractQueryBuildSetting} which provides an ID of the parser which
 * should be used for translating the query.
 * 
 * @author Wolf Bauer
 * 
 */
public class ParameterParserID extends AbstractQueryBuildSetting<String> {

	/**
	 * Creates a ParameterParserID.
	 * 
	 * @param value
	 *            ID of the parser which should be used for translating the
	 *            query.
	 */
	public ParameterParserID(String value) {
		super(value);
	}
}