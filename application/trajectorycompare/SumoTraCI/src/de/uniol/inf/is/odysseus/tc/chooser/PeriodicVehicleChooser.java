/*
 * com.maddyhome.idea.copyright.pattern.ProjectInfo@5652754b
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

package de.uniol.inf.is.odysseus.tc.chooser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.tc.vehicle.VehicleInfo;

/**
 * Created by marcus on 08.12.14.
 */
public class PeriodicVehicleChooser extends AbstractVehicleChooser {

	@SuppressWarnings("unused")
	private final Logger LOGGER = LoggerFactory.getLogger(PeriodicVehicleChooser.class);
	
	@Override
	protected boolean choose(VehicleInfo vi) {
		return vi.getState() % 25 == 0;
	}

	@Override
	protected void onFirstAdded(VehicleInfo vi) {
	}

	@Override
	protected void onRemoved(VehicleInfo vi) {
	}
}
