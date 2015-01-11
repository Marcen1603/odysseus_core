package de.uniol.inf.is.odysseus.tc.shuffle;

import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.tc.vehicle.VehicleInfo;

public class RandomVehicleSchuffler implements IVehicleShuffler {

	@Override
	public List<VehicleInfo> shuffle(List<VehicleInfo> vList) {
		Collections.shuffle(vList);
		return vList;
	}

}
