/**
 * Copyright 2013 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.mining.physicaloperator;

import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class Node {
	private double x;
	private double y;
	private double z;
	private double[] weights;
	private final DistanceMeasure distance = new EuclideanDistance();

	/**
	 * @return the x
	 */
	public double getX() {
		return this.x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(final double x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return this.y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(final double y) {
		this.y = y;
	}

	/**
	 * @return the z
	 */
	public double getZ() {
		return this.z;
	}

	/**
	 * @param z
	 *            the z to set
	 */
	public void setZ(final double z) {
		this.z = z;
	}

	public double getWeightDistance(final double[] input) {
		return this.distance.compute(this.weights, input);
	}

	/**
	 * @param input
	 * @param learningRate
	 * @param distance
	 */
	public void adjustWeights(final double[] input, final double learningRate, final double distance) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param temp
	 * @return
	 */
	public double distanceTo(final Node temp) {
		// TODO Auto-generated method stub
		return 0;
	}
}
