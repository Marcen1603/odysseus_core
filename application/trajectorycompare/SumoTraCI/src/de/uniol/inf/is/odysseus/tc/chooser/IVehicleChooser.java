/*
 * com.maddyhome.idea.copyright.pattern.ProjectInfo@dd3d7bd
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

import java.util.List;

import de.uniol.inf.is.odysseus.tc.interaction.ISumoInteraction.VehicleNextResult;
import de.uniol.inf.is.odysseus.tc.vehicle.VehicleInfo;

/**
 * Created by marcus on 29.11.14.
 */
public interface IVehicleChooser {

    public List<VehicleInfo> choose(VehicleNextResult vehicleNextResult);
}
