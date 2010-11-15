package de.uniol.inf.is.odysseus.scars.testdata.provider.extended;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;

public class ProviderUtil {

	public static MVRelationalTuple<? extends IProbability> createTuple(
			int attributeCount) {
		MVRelationalTuple<StreamCarsMetaData<Object>> tuple = new MVRelationalTuple<StreamCarsMetaData<Object>>(
				attributeCount);
		return tuple;
	}
	
}
