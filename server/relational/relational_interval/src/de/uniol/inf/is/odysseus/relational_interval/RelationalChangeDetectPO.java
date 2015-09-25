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
package de.uniol.inf.is.odysseus.relational_interval;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ChangeDetectPO;

public class RelationalChangeDetectPO extends ChangeDetectPO<Tuple<?>> {

	final protected int[] comparePositions;
	final List<Pair<Integer, Integer>> comparePositions2;
	private int suppressAttributePos = -1;

	public RelationalChangeDetectPO(int[] comparePositions) {
		super();
		this.comparePositions = comparePositions;
		comparePositions2 = null;
		StringBuffer tmp = new StringBuffer(" ");
		for (int i : comparePositions) {
			tmp.append(i).append(",");
		}
		setName(getName() + tmp);
	}

	public RelationalChangeDetectPO(
			List<Pair<Integer, Integer>> comparePositions) {
		super();
		this.comparePositions = null;
		comparePositions2 = new ArrayList<Pair<Integer, Integer>>(
				comparePositions);
		StringBuffer tmp = new StringBuffer(" ");
		for (Pair<Integer, Integer> i : comparePositions2) {
			tmp.append(i).append(",");
		}
		setName(getName() + tmp);
	}

	public RelationalChangeDetectPO(RelationalChangeDetectPO pipe) {
		super(pipe);
		this.comparePositions = pipe.comparePositions;
		this.comparePositions2 = pipe.comparePositions2;
	}

	@Override
	public OutputMode getOutputMode() {
		return suppressAttributePos < 0 ? OutputMode.INPUT
				: OutputMode.NEW_ELEMENT;
	}

	public void setSuppressAttribute(int suppressAttributePos) {
		this.suppressAttributePos = suppressAttributePos;
	}

	@Override
	protected boolean areDifferent(Tuple<?> object, Tuple<?> lastElement) {
		if (comparePositions != null){
			return !Tuple.equalsAt(object, lastElement, comparePositions);
		}else{
			return !Tuple.equalsAt(object, lastElement, comparePositions2);
		}
	}

	@Override
	protected Tuple<?> enrichObject(Tuple<?> object, long suppressedElements) {
		if (suppressAttributePos > 0) {
			object = object.append(suppressedElements);
		}
		return object;
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof RelationalChangeDetectPO)) {
			return false;
		}

		RelationalChangeDetectPO other = (RelationalChangeDetectPO) ipo;

		if (this.comparePositions == null && other.comparePositions != null) {
			return false;
		}

		if (this.comparePositions != null && other.comparePositions == null) {
			return false;
		}

		if (this.comparePositions != null && other.comparePositions != null) {
			if (this.comparePositions.length != other.comparePositions.length) {
				return false;
			}

			for (int i = 0; i < this.comparePositions.length; i++) {
				if (comparePositions[i] != other.comparePositions[i]) {
					return false;
				}
			}
		}

		if (this.comparePositions2 == null && other.comparePositions2 != null) {
			return false;
		}

		if (this.comparePositions2 != null && other.comparePositions2 == null) {
			return false;
		}

		if (this.comparePositions2 != null && other.comparePositions2 != null) {
			if (this.comparePositions2.size() != other.comparePositions2.size()) {
				return false;
			}

			for (int i = 0; i < this.comparePositions2.size(); i++) {
				if (!comparePositions2.get(i).equals(other.comparePositions2.get(i))) {
					return false;
				}
			}
		}
		
		return super.isSemanticallyEqual(other);
	}

}
