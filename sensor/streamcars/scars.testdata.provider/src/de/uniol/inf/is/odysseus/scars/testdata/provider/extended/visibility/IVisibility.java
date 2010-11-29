package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.visibility;

import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.ICarModel;

public interface IVisibility {

	/**
	 * returns true iff the car model is visible for the sensor
	 * @return
	 */
	public boolean isVisible(ICarModel carModel);
	
}
