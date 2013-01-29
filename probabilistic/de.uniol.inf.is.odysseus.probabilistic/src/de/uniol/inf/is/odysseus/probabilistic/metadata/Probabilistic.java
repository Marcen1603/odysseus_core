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

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.probabilistic.math.PBox;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class Probabilistic implements IProbabilistic {
	/**
	 * 
	 */
	private static final long serialVersionUID = -147594856639774242L;
	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] classes = new Class[] { IProbabilistic.class };
	@SuppressWarnings("unused")
	private final Map<Integer, PBox> pBoxes = new HashMap<Integer, PBox>();

	/** Tuple existence probability */
	private double existence;

	public Probabilistic() {
		this.existence = 1.0;
	}

	public Probabilistic(final double existence) {
		this.existence = existence;
	}

	public Probabilistic(final Probabilistic probability) {
		this.existence = probability.existence;

	}

	@Override
	public String csvToString() {
		return "" + this.existence;
	}

	@Override
	public String csvToString(final boolean withMetada) {
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

	@Override
	public double getExistence() {
		return this.existence;
	}

	@Override
	public void setExistence(final double existence) {
		this.existence = existence;
	}

	@Override
	public String toString() {
		return "TEP: " + this.existence;
	}

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return classes;
	}

}
