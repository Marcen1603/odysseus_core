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


/**
 * Bean which contains a tuple.
 * 
 * @author Merlin Wasmann
 * 
 */
public class Tuple implements Comparable<Tuple> {

	private final String[] attributes;

	private final long startTimestamp;
	private final long endTimestamp;

	public Tuple(String[] attributes, long startTimestamp, long endTimestamp) {
		this.attributes = attributes;
		this.startTimestamp = startTimestamp;
		this.endTimestamp = endTimestamp;
	}

	@Override
	public boolean equals(Object other) {
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
		for(int i = 0; i < attributes.length; i++) {
			sb.append(attributes[i] + (i == attributes.length - 1 ? "" : " | "));
		}
		return sb.toString();
	}
}
