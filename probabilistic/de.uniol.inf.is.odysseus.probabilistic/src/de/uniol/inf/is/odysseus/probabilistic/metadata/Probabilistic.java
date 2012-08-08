/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.probabilistic.metadata;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.probabilistic.math.PBox;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class Probabilistic implements IProbabilistic {
	@SuppressWarnings("unused")
	private final Map<Integer, PBox> pBoxes = new HashMap<Integer, PBox>();

	/**
	 * 
	 */
	private static final long serialVersionUID = -147594856639774242L;
	private double[] probabilities;

	public Probabilistic() {

	}

	public Probabilistic(Probabilistic probability) {
		this.probabilities = probability.probabilities.clone();

	}

	@Override
	public double getProbability(int pos) {
		return this.probabilities[pos];
	}

	@Override
	public void setProbability(int pos, double value) {
		this.probabilities[pos] = value;
	}

	@Override
	public String csvToString() {
		return "" + this.probabilities;
	}

	@Override
	public String csvToString(boolean withMetada) {
		return this.csvToString();
	}

	@Override
	public String getCSVHeader() {
		return "probability";
	}

	@Override
	public IProbabilistic clone() {
		return new Probabilistic(this);
	}

}
