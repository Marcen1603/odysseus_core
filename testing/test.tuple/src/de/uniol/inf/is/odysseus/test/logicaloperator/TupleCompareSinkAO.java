package de.uniol.inf.is.odysseus.test.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name = "TupleCompareSink", minInputPorts = 1, maxInputPorts = 1)
public class TupleCompareSinkAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -9070744467387287793L;
	private String fileName = null;
	
	public TupleCompareSinkAO(){
		super();
	}

	
	public TupleCompareSinkAO(String fileName) {
		super();
		this.fileName = fileName;
	}

	public TupleCompareSinkAO(TupleCompareSinkAO testSink) {
		super(testSink);		
		this.fileName = testSink.getFileName();
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new TupleCompareSinkAO(this);
	}
	
	@Parameter(name = "FILENAME", type = StringParameter.class, optional = false)
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}
	
}
