/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.offis.salsa.lms.model;

import java.io.Serializable;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class Sample implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6067095433021331482L;
	private int index;
	private float dist1;
	private float dist2;
	private float rssi1;
	private float rssi2;
	private double angle;

	public double getAngle() {
		return this.angle;
	}

	public float getDist1() {
		return this.dist1;
	}

	public float[] getDist1Vector() {
		final float[] vector = new float[2];
		final double angle = Math.toRadians(this.getAngle());
		vector[0] = (float) (this.getDist1() * Math.cos(angle)) / 10;
		vector[1] = (float) (this.getDist1() * Math.sin(angle)) / 10;
		return vector;
	}

	public float getDist2() {
		return this.dist2;
	}

	public float[] getDist2Vector() {
		final float[] vector = new float[2];
		final double angle = Math.toRadians(this.getAngle());
		vector[0] = (float) (this.getDist2() * Math.cos(angle)) / 10;
		vector[1] = (float) (this.getDist2() * Math.sin(angle)) / 10;
		return vector;
	}

	public int getIndex() {
		return this.index;
	}

	public float getRssi1() {
		return this.rssi1;
	}

	public float getRssi2() {
		return this.rssi2;
	}

	public double getX() {
		return this.getDist1() * Math.cos(Math.toRadians(this.getAngle())) / 10;
	}

	public double getY() {
		return this.getDist1() * Math.sin(Math.toRadians(this.getAngle())) / 10;
	}

	public void setAngle(final double angle) {
		this.angle = angle;
	}

	public void setDist1(final float dist1) {
		this.dist1 = dist1;
	}

	public void setDist2(final float dist2) {
		this.dist2 = dist2;
	}

	public void setIndex(final int index) {
		this.index = index;
	}

	public void setRssi1(final float rssi1) {
		this.rssi1 = rssi1;
	}

	public void setRssi2(final float rssi2) {
		this.rssi2 = rssi2;
	}

	@Override
	public String toString() {
		return "Sample [dist1=" + this.dist1 + ", dist2=" + this.dist2
				+ ", rssi1=" + this.rssi1 + ", rssi2=" + this.rssi2
				+ ", angle=" + this.angle + "]";
	}

}
