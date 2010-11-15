package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.tuple;

import de.uniol.inf.is.odysseus.scars.testdata.provider.ExtendedProvider;

public class TupleGeneratorFactory {

	private static TupleGeneratorFactory instance;
	
	private TupleGeneratorFactory() {
		
	}
	
	public static synchronized TupleGeneratorFactory getInstance() {
		if (instance == null)
			instance = new TupleGeneratorFactory();
		return instance;
	}
	
	public ITupleGenerator buildTupleGenerator(String schemaID) {
		if (schemaID.equals(ExtendedProvider.SCHEMA_SCARS_DEFAULT)) {
			return new DefaultTupleGenerator();
		}
		return null;
	}
	
}
