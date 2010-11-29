package de.uniol.inf.is.odysseus.scars.testdata.provider.test;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.scars.testdata.provider.ExtendedProvider;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.calcmodel.DefaultOvertakeCalcModel;

public class ExtendedProviderTest {

	public static void main(String[] args) {
		Map<String, String> options = new HashMap<String, String>();
		options.put(ExtendedProvider.CALCMODEL, ExtendedProvider.CALCMODEL_SCARS_OVERTAKE);
		options.put(ExtendedProvider.SCHEMA, ExtendedProvider.SCHEMA_SCARS_ALTERNATIVE);
		Map<String, Object> calcModelParams = new HashMap<String, Object>();
		calcModelParams.put(DefaultOvertakeCalcModel.LANE_SHIFT_FACTOR, new Float(1.5));
		ExtendedProvider provider = new ExtendedProvider(options, calcModelParams);
		provider.setDelay(50);
		provider.setNumOfCars(5);
		provider.init();
		for (int i = 0; i < 200; i++) {
			System.out.println(provider.nextTuple());
//			provider.nextTuple();
		}
	}
	
}
