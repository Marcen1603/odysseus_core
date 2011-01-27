package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.visibility;

import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.ICarModel;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.LaserCarModel;

public class FrontLaserVisibility implements IVisibility {

	@Override
	public boolean isVisible(ICarModel carModel) {
		LaserCarModel cm = (LaserCarModel) carModel;
		if (cm.getPosx() > 150 || cm.getPosx() < 0 || cm.getPosy() < -100
				|| cm.getPosy() > 100) {
			return false;
		}
		return true;
	}

}
