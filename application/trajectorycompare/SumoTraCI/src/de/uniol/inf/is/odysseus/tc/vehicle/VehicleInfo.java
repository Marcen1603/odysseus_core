/*
 * com.maddyhome.idea.copyright.pattern.ProjectInfo@31845613
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

package de.uniol.inf.is.odysseus.tc.vehicle;

import java.io.IOException;

import it.polito.appeal.traci.Vehicle;

import com.vividsolutions.jts.geom.Point;

/**
 * Created by marcus on 29.11.14.
 */
public class VehicleInfo {

    private Point p;

    private double internX;

    private double internY;

    private String type;

    private String id = null;

    private int state = -1;

    public VehicleInfo(final Vehicle vehicle) {
            try {
				this.internX = vehicle.getPosition().getX();
	            this.internY = vehicle.getPosition().getY();
	            this.type = vehicle.getType();
	            this.id = vehicle.getID();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }

    public VehicleInfo setP(Point p) {
        this.p = p;
        return this;
    }

    public Point getP() {
        return p;
    }

    public String getType() {
        return this.type;
    }

    public String getId() {
        return this.id;
    }

    public double getInternX() {
        return this.internX;
    }

    public double getInternY() {
        return this.internY;
    }

    public int getState() {
        return state;
    }

    public int setState(int state) {
        return this.state = state;
    }
}