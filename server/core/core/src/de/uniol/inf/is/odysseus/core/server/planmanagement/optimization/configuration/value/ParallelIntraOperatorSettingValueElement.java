/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.value;

/**
 * This element class contains configuration for specific operators in intra
 * operator parallelization, used in ParallelIntraOperatorSettingValue
 * 
 * @author ChrisToenjesDeye
 */
public class ParallelIntraOperatorSettingValueElement {
	private int individualDegree = 0;
	private int individualBuffersize = 0;

	public ParallelIntraOperatorSettingValueElement(int individualDegree,
			int individualBuffersize) {
		this.individualDegree = individualDegree;
		this.individualBuffersize = individualBuffersize;
	}

	public int getIndividualDegree() {
		return individualDegree;
	}

	public void setIndividualDegree(int individualDegree) {
		this.individualDegree = individualDegree;
	}

	public int getIndividualBuffersize() {
		return individualBuffersize;
	}

	public void setIndividualBuffersize(int individualBuffersize) {
		this.individualBuffersize = individualBuffersize;
	}

}
