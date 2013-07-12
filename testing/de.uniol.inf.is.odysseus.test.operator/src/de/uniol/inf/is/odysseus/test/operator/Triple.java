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
package de.uniol.inf.is.odysseus.test.operator;

/**
 * @author Merlin Wasmann
 *
 */
public class Triple<T, K, M> {
	
	private T first;
	private K second;
	private M third;
	
	public Triple(T first, K second, M third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}
	
	public T getFirst() {
		return this.first;
	}
	public K getSecond() {
		return this.second;
	}
	public M getThird() {
		return this.third;
	}
	
	public void setFirst(T first) {
		this.first = first;
	}
	
	public void setSecond(K second) {
		this.second = second;
	}
	public void setThird(M third) {
		this.third = third;
	}
	
	@Override
	public Triple<T,K,M> clone() {
		return new Triple<T,K,M>(this.first, this.second, this.third);
	}
}
