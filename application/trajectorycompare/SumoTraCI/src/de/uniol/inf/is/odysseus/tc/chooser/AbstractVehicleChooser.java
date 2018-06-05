package de.uniol.inf.is.odysseus.tc.chooser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.tc.interaction.ISumoInteraction.VehicleNextResult;
import de.uniol.inf.is.odysseus.tc.vehicle.VehicleInfo;

public abstract class AbstractVehicleChooser implements IVehicleChooser {

	private final Logger LOGGER = LoggerFactory.getLogger(PeriodicVehicleChooser.class);

    private final Map<String, Integer> map = new HashMap<>();
	
	@Override
	public List<VehicleInfo> choose(VehicleNextResult vehicleNextResult) {
    	final List<VehicleInfo> result = new LinkedList<>();
    	
    	for(VehicleInfo vi : vehicleNextResult.getRemoved()) {
    		vi.setState(-1);
    		this.map.remove(vi.getId());
    		result.add(vi);
    		this.onRemoved(vi);
    		LOGGER.info("finished: " + vi.getId());
    	}
    	
    	
    	for(VehicleInfo vi : vehicleNextResult.getNext()) {
    		Integer i = this.map.get(vi.getId());
    		if(i == null) {
    			i = -1;
    		}
    		vi.setState(i + 1);
    		this.map.put(vi.getId(), vi.getState());;
    		if(vi.getState() == 0) {
    			LOGGER.info("begin: " + vi.getId());
    		}
    		if(vi.getState() == 0) {
    			result.add(vi);
    			this.onFirstAdded(vi);
    		} else if(this.choose(vi)) {
    			result.add(vi);
    		}
    	}

    	return result;
	}
	
	protected abstract void onFirstAdded(VehicleInfo vi);
	
	protected abstract void onRemoved(VehicleInfo vi);
	
	protected abstract boolean choose(VehicleInfo vi);
}
