/********************************************************************************** 
 * Copyright 2014 The Odysseus Team
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
package de.uniol.inf.is.odysseus.image.common.datatype;

import java.util.Arrays;

import de.uniol.inf.is.odysseus.core.IClone;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class Image implements IClone, Cloneable {

	private final int width;
	private final int height;
	private final double[] buffer;

	public Image(final int width, final int height) {
		this.width = width;
		this.height = height;
		this.buffer = new double[this.width * this.height];
	}

	public Image(final int width, final int height, final double[] buffer) {
		this(width, height);
		System.arraycopy(buffer, 0, this.buffer, 0, buffer.length);
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return this.height;
	}

	public double get(final int x, final int y) {
		return this.buffer[(y * this.width) + x];
	}

	public double[] getBuffer() {
		return this.buffer;
	}

	public void set(final int x, final int y, final double value) {
		this.buffer[(y * this.width) + x] = value;
	}

	public double[][] get(final int x1, final int y1, final int x2, final int y2) {
		double[][] value = new double[y2 - y1 + 1][x2 - x1 + 1];

		for (int i = 0; i < value.length; i++) {
			try {

				System.arraycopy(this.buffer, (i + y1) * this.width + x1,
						value[i], 0, value[i].length);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	public void set(final int x1, final int y1, final int x2, final int y2,
			final double value) {
		double[] valueArray = new double[x2 - x1 + 1];
		Arrays.fill(valueArray, value);
		for (int i = y1; i <= y2; i++) {
			System.arraycopy(valueArray, 0, this.buffer, i * this.width + x1,
					valueArray.length);
		}
	}

	public void setBuffer(final double[] value) {
		System.arraycopy(this.buffer, 0, value, 0, value.length);
	}

	public void fill(final double value) {
		Arrays.fill(this.buffer, value);
	}

	@Override
	public Image clone() {
		final Image image = new Image(this.width, this.height, this.buffer);
		return image;
	}

	@Override
	public String toString() {
		return "{Width: " + this.width + " Height: " + this.height + "}";
	}

}
