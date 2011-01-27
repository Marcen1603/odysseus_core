package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.visibility;

import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.DefaultCarModel;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.ICarModel;

public class FrontDefaultVisibility implements IVisibility {

	@Override
	public boolean isVisible(ICarModel carModel) {
		DefaultCarModel cm = (DefaultCarModel) carModel;
		if (cm.getPosx() > 150 || cm.getPosx() < 0 || cm.getPosy() < -100
				|| cm.getPosy() > 100) {
			return false;
		}
		return true;
	}

}
