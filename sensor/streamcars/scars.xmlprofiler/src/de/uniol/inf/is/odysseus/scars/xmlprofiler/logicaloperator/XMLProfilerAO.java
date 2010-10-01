package de.uniol.inf.is.odysseus.scars.xmlprofiler.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class XMLProfilerAO<M extends IProbability> extends UnaryLogicalOp {

	String operatorName;
	String fileName;

	private static final long serialVersionUID = 1L;

	public XMLProfilerAO() {
		super();
	}

	public XMLProfilerAO(XMLProfilerAO<M> clone) {
		super(clone);
		this.operatorName = clone.operatorName;
		this.fileName = clone.fileName;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return this.getInputSchema();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new XMLProfilerAO<M>(this);
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
