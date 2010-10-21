package de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter;

import de.uniol.inf.is.odysseus.planmanagement.configuration.Setting;


/**
 * {@link IQueryBuildSetting} which provides an ID of the parser which
 * should be used for translating the query.
 * 
 * @author Wolf Bauer
 * 
 */
public class ParameterParserID extends Setting<String> implements IQueryBuildSetting<String> {

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