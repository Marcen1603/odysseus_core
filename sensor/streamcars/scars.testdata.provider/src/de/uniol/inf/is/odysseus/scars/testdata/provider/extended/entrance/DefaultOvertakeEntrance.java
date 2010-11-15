package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.entrance;

import java.util.List;

import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.DefaultCarModel;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.ICarModel;

public class DefaultOvertakeEntrance implements IEntrance {

	@Override
	public boolean freeEntranceSlot(List<ICarModel> state) {
		for (ICarModel cm : state) {
			if (((DefaultCarModel)cm).getPosx() < 20) {
				return false;
			}
		}
		return true;
	}

}
