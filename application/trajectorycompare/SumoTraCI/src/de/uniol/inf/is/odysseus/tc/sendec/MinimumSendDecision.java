/*
 * com.maddyhome.idea.copyright.pattern.ProjectInfo@30d36dcb
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

package de.uniol.inf.is.odysseus.tc.sendec;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import de.uniol.inf.is.odysseus.tc.interaction.ISumoInteraction;
import de.uniol.inf.is.odysseus.tc.vehicle.VehicleInfo;

/**
 * Created by marcus on 03.12.14.
 */
public class MinimumSendDecision implements ISendDecision {

    private final ISumoInteraction sumoInteraction;

    @Inject
    public MinimumSendDecision(ISumoInteraction sumoInteraction) {
        this.sumoInteraction = sumoInteraction;
    }

    @Override
    public List<Object[]> getValuesToSend(List<VehicleInfo> vList) {
    	List<Object[]> result = new ArrayList<>(vList.size());
    	for(final VehicleInfo vi : vList) {
    		result.add(new Object[] {
                    this.sumoInteraction.step(),
                    vi.getType(),
                    vi.getId(),
                    vi.getPosition(),//null
                    vi.getState()//null
    		});
    	}
        return result;
    }
}
