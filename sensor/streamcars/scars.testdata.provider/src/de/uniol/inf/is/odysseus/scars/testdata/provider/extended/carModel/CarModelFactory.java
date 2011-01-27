package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel;

import de.uniol.inf.is.odysseus.scars.testdata.provider.ExtendedProvider;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.calcmodel.IAlternativeCalcModel;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.calcmodel.IDefaultCalcModel;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.calcmodel.IGenericCalcModel;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.calcmodel.ILaserCalcModel;

public class CarModelFactory {
	
	private static CarModelFactory instance;
	
	private CarModelFactory() {
		
	}
	
	public static synchronized CarModelFactory getInstance() {
		if (instance == null)
			instance = new CarModelFactory();
		return instance;
	}
	
	public ICarModel buildCarModel(String schemaID, int id, IGenericCalcModel calcModel) {
		if (schemaID.equals(ExtendedProvider.SCHEMA_SCARS_DEFAULT)) {
			return new DefaultCarModel(id, (IDefaultCalcModel)calcModel);
		} else if (schemaID.equals(ExtendedProvider.SCHEMA_SCARS_ALTERNATIVE)) {
			return new AlternativeCarModel(id, (IAlternativeCalcModel)calcModel);
		} else if (schemaID.equals(ExtendedProvider.SCHEMA_SCARS_LASER)) {
			return new LaserCarModel(id, (ILaserCalcModel)calcModel);
		}
		return null;
	}

}
