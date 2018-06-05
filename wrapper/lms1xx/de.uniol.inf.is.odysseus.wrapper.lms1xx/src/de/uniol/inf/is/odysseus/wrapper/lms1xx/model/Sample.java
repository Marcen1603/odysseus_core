/*******************************************************************************
 * LMS1xx protocol handler for the Odysseus data stream management system
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.uniol.inf.is.odysseus.wrapper.lms1xx.model;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class Sample {

    private final int index;
    private final double angle;
    private float dist1;
    private float dist2;
    private float rssi1;
    private float rssi2;

    /**
     * @param index
     *            The index of the sample
     * @param angle
     *            The angle in radiant of the sample
     */
    public Sample(final int index, final double angle) {
        this.index = index;
        this.angle = angle;
    }

    /**
     * @return The angle in radiant
     */
    public double getAngle() {
        return this.angle;
    }

    /**
     * @return The distance of the first beam in millimeter
     */
    public float getDist1() {
        return this.dist1;
    }

    /**
     * 
     * @param dist1
     *            The distance of the first beam
     */
    public void setDist1(final float dist1) {
        this.dist1 = dist1;
    }

    /**
     * @return The distance of the second beam in millimeter
     */
    public float getDist2() {
        return this.dist2;
    }

    /**
     * 
     * @param dist2
     *            The distance of the second beam
     */
    public void setDist2(final float dist2) {
        this.dist2 = dist2;
    }

    /**
     * 
     * @return The index
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * 
     * @return The remission of the first beam.
     */
    public float getRssi1() {
        return this.rssi1;
    }

    /**
     * 
     * @param rssi1
     *            The remission of the first beam
     */
    public void setRssi1(final float rssi1) {
        this.rssi1 = rssi1;
    }

    /**
     * 
     * @return The remission of the second beam.
     */
    public float getRssi2() {
        return this.rssi2;
    }

    /**
     * 
     * @param rssi2
     *            The remission of the second beam
     */
    public void setRssi2(final float rssi2) {
        this.rssi2 = rssi2;
    }

    /**
     * 
     * @return The x coordinate
     */
    public double getX() {
        return this.getDist1() * Math.cos(this.getAngle());
    }

    /**
     * 
     * @return The y coordinate
     */
    public double getY() {
        return this.getDist1() * Math.sin(this.getAngle());
    }

    @Override
    public String toString() {
        return "Sample [dist1=" + this.dist1 + ", dist2=" + this.dist2 + ", rssi1=" + this.rssi1 + ", rssi2=" + this.rssi2 + ", angle=" + this.angle + "]";
    }

}
