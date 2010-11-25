package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.entrance;

import de.uniol.inf.is.odysseus.scars.testdata.provider.ExtendedProvider;

public class EntranceFactory {

	private static EntranceFactory instance;

	private EntranceFactory() {

	}

	public static synchronized EntranceFactory getInstance() {
		if (instance == null)
			instance = new EntranceFactory();
		return instance;
	}

	public IEntrance buildEntrance(String schemaID, String calcModelID) {
		if (schemaID.equals(ExtendedProvider.SCHEMA_SCARS_DEFAULT) && 
				calcModelID.equals(ExtendedProvider.CALCMODEL_SCARS_OVERTAKE)) {
			return new DefaultOvertakeEntrance();
		} else if (schemaID.equals(ExtendedProvider.SCHEMA_SCARS_ALTERNATIVE) && 
				calcModelID.equals(ExtendedProvider.CALCMODEL_SCARS_OVERTAKE)) {
			return new AlternativeOvertakeEntrance();
		}
		return null;
	}
}
