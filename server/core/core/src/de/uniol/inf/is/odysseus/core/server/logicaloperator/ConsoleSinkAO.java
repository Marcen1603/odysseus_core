package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;

@LogicalOperator(name = "ConsoleSink", minInputPorts = 1, maxInputPorts = Integer.MAX_VALUE, doc = "Print input to standard out.", category = { LogicalOperatorCategory.SINK })
public class ConsoleSinkAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -4314825703308226640L;

	private boolean dumpPunctuation = false;
	private boolean printPort = false;
	
	
	public ConsoleSinkAO() {
		super();
	}

	public ConsoleSinkAO(ConsoleSinkAO sink) {
		super(sink);
		this.dumpPunctuation = sink.dumpPunctuation;
		this.printPort = sink.printPort;
	}

	@Parameter(name="dumpPunctuation", type=BooleanParameter.class, optional = true, doc = "Set to true, if punctuations should be printed. Default is false")
	public void setDumpPunctuation(boolean dumpPunctuation) {
		this.dumpPunctuation = dumpPunctuation;
	}
	
	public boolean isDumpPunctuation() {
		return dumpPunctuation;
	}


	/**
	 * @return the printPort
	 */
	public boolean isPrintPort() {
		return printPort;
	}

	/**
	 * @param printPort the printPort to set
	 */
	@Parameter(name="printPort", type=BooleanParameter.class, optional = true, doc = "Set to true, if input port should be printed. Default is false")
	public void setPrintPort(boolean printPort) {
		this.printPort = printPort;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new ConsoleSinkAO(this);
	}

}
