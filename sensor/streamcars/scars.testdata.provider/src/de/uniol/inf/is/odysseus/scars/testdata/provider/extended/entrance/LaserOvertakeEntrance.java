package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.entrance;

import java.util.List;

import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.ICarModel;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.LaserCarModel;

public class LaserOvertakeEntrance implements IEntrance {

	@Override
	public boolean freeEntranceSlot(List<ICarModel> state) {
		for (ICarModel cm : state) {
			if (((LaserCarModel)cm).getPosx() < 20) {
				return false;
			}
		}
		return true;
	}

}
