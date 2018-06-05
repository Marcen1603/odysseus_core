/*
 * com.maddyhome.idea.copyright.pattern.ProjectInfo@6f750526
 * Copyright (c) ${year}, ${owner}, All rights reserved.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package de.uniol.inf.is.odysseus.tc.interaction;

import it.polito.appeal.traci.SumoTraciConnection;
import it.polito.appeal.traci.Vehicle;
import it.polito.appeal.traci.VehicleLifecycleObserver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.uniol.inf.is.odysseus.tc.ISumoParams;
import de.uniol.inf.is.odysseus.tc.vehicle.VehicleInfo;

/**
 * Created by marcus on 28.11.14.
 */
public class SumoInteraction implements ISumoInteraction {

    private final static Logger LOGGER = LoggerFactory.getLogger(SumoInteraction.class);

    private final SumoTraciConnection conn;

    @Inject
    public SumoInteraction(ISumoParams sumoParams) {
        this.conn = new SumoTraciConnection(
                sumoParams.getSumoFilePath(sumoParams.getConfigFilename()),  // config file
                sumoParams.getSeed()                                 		 // random seed
        );
        this.conn.setTcpNoDelay(true);
        try {
            this.conn.runServer();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            System.exit(-1);
        }
        this.conn.setTcpNoDelay(false);
        conn.addVehicleLifecycleObserver(new VehicleLifecycleObserver() {
			
			@Override
			public void vehicleTeleportStarting(Vehicle arg0) {}
			
			@Override
			public void vehicleTeleportEnding(Vehicle arg0) {}
			
			@Override
			public void vehicleDeparted(Vehicle arg0) {}
			
			@Override
			public void vehicleArrived(Vehicle arg0) {
				vehicleRemovedList.add(new VehicleInfo(arg0));
			}
		});
    }
    
    private List<VehicleInfo> vehicleRemovedList = new LinkedList<>();

    @Override
    public VehicleNextResult next() {
    	try {
            this.conn.nextSimStep();
            final List<VehicleInfo> vehicleList = new ArrayList<VehicleInfo>();
            for(final Vehicle v : this.conn.getVehicleRepository().getAll().values()) {
            	vehicleList.add(new VehicleInfo(v));
            }
            final VehicleNextResult result = new VehicleNextResult(vehicleList, vehicleRemovedList);
            this.vehicleRemovedList = new LinkedList<VehicleInfo>();
            return result;
    	} catch(Exception e) {  }
    	return null;
    }

    @Override
    public long step() {
        return this.conn.getCurrentSimStep();
    }
}
