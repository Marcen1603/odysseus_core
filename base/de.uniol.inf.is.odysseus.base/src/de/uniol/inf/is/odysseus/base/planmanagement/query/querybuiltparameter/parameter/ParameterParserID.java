package de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.parameter;

import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.AbstractQueryBuildParameter;

/**
 * {@link AbstractQueryBuildParameter} which provides an ID of the parser which
 * should be used for translating the query.
 * 
 * @author Wolf Bauer
 * 
 */
public class ParameterParserID extends AbstractQueryBuildParameter<String> {

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