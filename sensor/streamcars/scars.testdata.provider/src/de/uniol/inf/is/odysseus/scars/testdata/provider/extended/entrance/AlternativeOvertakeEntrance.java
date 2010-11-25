package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.entrance;

import java.util.List;

import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.AlternativeCarModel;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.ICarModel;

public class AlternativeOvertakeEntrance implements IEntrance {

	@Override
	public boolean freeEntranceSlot(List<ICarModel> state) {
		for (ICarModel cm : state) {
			if (((AlternativeCarModel)cm).getPosx() < 20) {
				return false;
			}
		}
		return true;
	}

}
