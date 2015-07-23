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
package de.uniol.inf.is.odysseus.parallelization.rcp.data;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.parallelization.intraoperator.keyword.IntraOperatorGlobalKeywordBuilder;
import de.uniol.inf.is.odysseus.parallelization.intraoperator.keyword.IntraOperatorParallelizationPreParserKeywordBuilder;

public class IntraOperatorBenchmarkerExecution extends
		AbstractBenchmarkerExecution {

	private Integer degree;
	private List<String> selectedOperators = new ArrayList<String>();
	private int buffersize;

	public IntraOperatorBenchmarkerExecution(Integer degree,
			List<String> selectedOperators, int buffersize) {
		this.degree = degree;
		this.selectedOperators = selectedOperators;
		this.buffersize = buffersize;
	}

	public IntraOperatorBenchmarkerExecution(
			IntraOperatorBenchmarkerExecution other) {
		this.degree = other.degree;
		this.selectedOperators = new ArrayList<String>(other.selectedOperators);
		this.buffersize = other.buffersize;
	}

	public IntraOperatorBenchmarkerExecution clone() {
		return new IntraOperatorBenchmarkerExecution(this);
	}

	public Integer getDegree() {
		return degree;
	}

	public void setDegree(Integer degree) {
		this.degree = degree;
	}

	@Override
	public String getOdysseusScript() {
		StringBuilder builder = new StringBuilder();
		builder.append(IntraOperatorGlobalKeywordBuilder.buildParameterString(
				degree, buffersize));
		if (selectedOperators != null && !selectedOperators.isEmpty()){
			builder.append(IntraOperatorParallelizationPreParserKeywordBuilder
					.buildParameterString(degree, buffersize, selectedOperators));			
		}
		return builder.toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Intra-Operator Parallelization: Degree: " + degree);
		builder.append(", Buffersize: " + buffersize);
		if (selectedOperators != null && !selectedOperators.isEmpty()){
			builder.append(", Use only operators with id: " + selectedOperators);			
		}
		return builder.toString();
	}

}
