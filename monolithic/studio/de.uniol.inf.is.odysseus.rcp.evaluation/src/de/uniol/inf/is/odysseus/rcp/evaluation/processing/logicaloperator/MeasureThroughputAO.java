package de.uniol.inf.is.odysseus.rcp.evaluation.processing.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name="THROUGHPUT", minInputPorts=1, maxInputPorts=1, doc="Measure the current throughput",category={LogicalOperatorCategory.BENCHMARK})
public class MeasureThroughputAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -8773782766638078153L;
	private int each = 100;
	private boolean dump = false;
	private boolean active = true;
	private String filename = "";	
	
	@Override
	public AbstractLogicalOperator clone() {
		return new MeasureThroughputAO(this);
	}

	public MeasureThroughputAO(MeasureThroughputAO mtAO) {
		this.each = mtAO.each;
		this.active = mtAO.active;
		this.filename = mtAO.filename;		
		this.dump = mtAO.dump;
	}
	
	public MeasureThroughputAO(){
		
	}

	public int getEach() {
		return each;
	}

	@Parameter(name="each", type=IntegerParameter.class)
	public void setEach(int each) {
		this.each = each;
	}

	public boolean isActive() {
		return active;
	}

	@Parameter(name="active", type=BooleanParameter.class, optional=true)
	public void setActive(boolean active) {
		this.active = active;
	}

	public String getFilename() {
		return filename;
	}

	@Parameter(name="filename", type=StringParameter.class)
	public void setFilename(String filename) {
		this.filename = filename;
	}

	public boolean isDump() {
		return dump;
	}

	@Parameter(name="dump", type=BooleanParameter.class, optional=true)
	public void setDump(boolean dump) {
		this.dump = dump;
	}
	
	

}
