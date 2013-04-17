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
package de.uniol.inf.is.odysseus.equivalentoutput.tuple;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Bean which contains a tuple.
 * 
 * WARNING: Tuples for which equals is true have NOT the same hashCode.
 * 
 * @author Merlin Wasmann
 * 
 */
public class Tuple implements Comparable<Tuple> {

	private final String[] attributes;

	private final String[] attributesWithoutTimestamp;

	private final long startTimestamp;
	private final long endTimestamp;

	public Tuple(String[] attributes, long startTimestamp, long endTimestamp) {
		this.attributes = attributes;
		this.attributesWithoutTimestamp = new String[attributes.length - 2];
		for (int i = 0; i < attributes.length - 2; i++) {
			this.attributesWithoutTimestamp[i] = attributes[i];
		}
		this.startTimestamp = startTimestamp;
		this.endTimestamp = endTimestamp;
	}

	public String[] getAttributes() {
		return this.attributes;
	}

	public String[] getAttributesWithoutTimestamps() {
		return this.attributesWithoutTimestamp;
	}

	public long getStartTimestamp() {
		return this.startTimestamp;
	}

	public long getEndTimestamp() {
		return this.endTimestamp;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (other == this) {
			return true;
		}
		if (other instanceof Tuple) {
			Tuple tuple = (Tuple) other;
			if (tuple.startTimestamp == this.startTimestamp
					&& tuple.endTimestamp == this.endTimestamp
					&& tuple.attributes.length == this.attributes.length) {
				for (int i = 0; i < this.attributes.length; i++) {
					if (!tuple.attributes[i].equals(this.attributes[i])) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	public boolean valueEquals(Tuple other) {
		if (other == null) {
			return false;
		}
		if (other == this) {
			return true;
		}
		if (this.attributesWithoutTimestamp.length != other.attributesWithoutTimestamp.length) {
			return false;
		}
		for (int i = 0; i < this.attributesWithoutTimestamp.length; i++) {
			if (!this.attributesWithoutTimestamp[i]
					.equals(other.attributesWithoutTimestamp[i])) {
				return false;
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Tuple tuple) {
		if (tuple.startTimestamp < this.startTimestamp) {
			return -1;
		}
		if (tuple.startTimestamp > this.startTimestamp) {
			return 1;
		}
		return 0;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Tuple (#" + hashCode() + ") ");
		for (int i = 0; i < attributes.length; i++) {
			sb.append(attributes[i] + (i == attributes.length - 1 ? "" : " | "));
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder(17, 31);
		for (int i = 0; i < this.attributesWithoutTimestamp.length; i++) {
			hcb.append(this.attributesWithoutTimestamp[i]);
		}
		return hcb.toHashCode();
	}
}
