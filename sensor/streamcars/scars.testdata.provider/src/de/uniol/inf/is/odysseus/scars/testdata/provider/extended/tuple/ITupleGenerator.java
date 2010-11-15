package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.tuple;

import java.util.List;

import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.ICarModel;

public interface ITupleGenerator {

	public Object nextTuple(List<ICarModel> state, long timestamp);
	
}
