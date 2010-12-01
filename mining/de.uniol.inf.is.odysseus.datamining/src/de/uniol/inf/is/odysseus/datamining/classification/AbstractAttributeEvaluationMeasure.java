package de.uniol.inf.is.odysseus.datamining.classification;

public abstract class AbstractAttributeEvaluationMeasure {

	public abstract Double getEvaluationMeasure(DataCube statistics,
			int attribute);

	public Integer getSplitAttribute(Double probability) {
		return 0;
	}
}
