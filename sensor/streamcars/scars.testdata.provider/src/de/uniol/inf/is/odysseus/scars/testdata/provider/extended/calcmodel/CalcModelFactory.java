package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.calcmodel;

import de.uniol.inf.is.odysseus.scars.testdata.provider.ExtendedProvider;

public class CalcModelFactory {

	private static CalcModelFactory instance;

	private CalcModelFactory() {

	}

	public static synchronized CalcModelFactory getInstance() {
		if (instance == null) {
			instance = new CalcModelFactory();
		}
		return instance;
	}

	/**
	 * creates a new calculation model instance for the given schemaID and
	 * calcModelID. if one of the parameters is invalid or if there is no
	 * matching implementation for their combination null is returned.
	 * 
	 * @param schemaID
	 *            id of schema, not null.
	 * @param calcModelID
	 *            id of calculation model, not null.
	 * @return calculation model or null in case of errors.
	 */
	public IGenericCalcModel buildCalcModel(String schemaID, String calcModelID) {
		if (schemaID.equals(ExtendedProvider.SCHEMA_SCARS_DEFAULT)) {
			if (calcModelID.equals(ExtendedProvider.CALCMODEL_SCARS_OVERTAKE)) {
				return new DefaultOvertakeCalcModel();
			}
		}
		return null;
	}

}
