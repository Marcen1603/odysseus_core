package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.entrance;

import java.util.List;

import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.ICarModel;

public interface IEntrance {
	public boolean freeEntranceSlot(List<ICarModel> state);
}
