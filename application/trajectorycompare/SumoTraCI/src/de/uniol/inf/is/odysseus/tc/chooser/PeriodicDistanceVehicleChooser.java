package de.uniol.inf.is.odysseus.tc.chooser;

import de.uniol.inf.is.odysseus.tc.vehicle.VehicleInfo;

public class PeriodicDistanceVehicleChooser extends AbstractVehicleChooser {

	private final DistanceVehicleChooser distanceVehicleChooser = new DistanceVehicleChooser();
	
	private final PeriodicVehicleChooser periodicVehicleChooser = new PeriodicVehicleChooser();
	
	@Override
	protected void onFirstAdded(VehicleInfo vi) {
		this.periodicVehicleChooser.onFirstAdded(vi);
		this.distanceVehicleChooser.onFirstAdded(vi);
	}

	@Override
	protected void onRemoved(VehicleInfo vi) {
		this.periodicVehicleChooser.onRemoved(vi);
		this.distanceVehicleChooser.onRemoved(vi);
	}

	@Override
	protected boolean choose(VehicleInfo vi) {
		return this.periodicVehicleChooser.choose(vi) && this.distanceVehicleChooser.choose(vi);
	}



}
