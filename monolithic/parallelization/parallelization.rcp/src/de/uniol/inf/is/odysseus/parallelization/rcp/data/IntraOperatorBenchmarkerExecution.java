package de.uniol.inf.is.odysseus.parallelization.rcp.data;

import de.uniol.inf.is.odysseus.parallelization.intraoperator.keyword.IntraOperatorGlobalKeywordBuilder;

public class IntraOperatorBenchmarkerExecution extends
		AbstractBenchmarkerExecution {

	private Integer degree;

	public IntraOperatorBenchmarkerExecution(Integer degree) {
		this.degree = degree;
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
				degree));
		return builder.toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Intra-Operator Parallelization: Degree: " + degree);
		return builder.toString();
	}

}
