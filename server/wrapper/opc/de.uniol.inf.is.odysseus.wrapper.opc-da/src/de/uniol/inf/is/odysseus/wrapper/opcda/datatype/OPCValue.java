/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
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
package de.uniol.inf.is.odysseus.wrapper.opcda.datatype;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class OPCValue {
    private long timestamp;
    private double value;
    private short quality;
    private int error;

    /**
     * Class constructor.
     *
     * @param timestamp
     * @param value
     * @param quality
     * @param error
     */
    public OPCValue(long timestamp, double value, short quality, int error) {
        super();
        this.timestamp = timestamp;
        this.value = value;
        this.quality = quality;
        this.error = error;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return this.timestamp;
    }

    /**
     * @param timestamp
     *            the timestamp to set
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the value
     */
    public double getValue() {
        return this.value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * @return the quality
     */
    public short getQuality() {
        return this.quality;
    }

    /**
     * @param quality
     *            the quality to set
     */
    public void setQuality(short quality) {
        this.quality = quality;
    }

    /**
     * @return the error
     */
    public int getError() {
        return this.error;
    }

    /**
     * @param error
     *            the error to set
     */
    public void setError(int error) {
        this.error = error;
    }

}
