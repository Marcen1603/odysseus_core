package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.visibility;

import de.uniol.inf.is.odysseus.scars.testdata.provider.ExtendedProvider;

public class VisibilityFactory {

	private static VisibilityFactory instance;

	private VisibilityFactory() {

	}

	public static synchronized VisibilityFactory getInstance() {
		if (instance == null)
			instance = new VisibilityFactory();
		return instance;
	}

	public IVisibility buildVisibility(String visibilityID, String schemaID) {
		if (ExtendedProvider.VISIBILITY_SCARS_FRONT.equals(visibilityID)
				&& ExtendedProvider.SCHEMA_SCARS_DEFAULT.equals(schemaID)) {
			return new FrontDefaultVisibility();
		} else if (ExtendedProvider.VISIBILITY_SCARS_FRONT.equals(visibilityID)
				&& ExtendedProvider.SCHEMA_SCARS_ALTERNATIVE.equals(schemaID)) {
			return new FrontAlternativeVisibility();
		}
		return null;
	}

}
