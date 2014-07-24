package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd;


import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;


public class CreateStreamCommand extends AbstractExecutorCommand {
	
	private ILogicalOperator rootAO;
	private String name;

	public CreateStreamCommand(String name, ILogicalOperator rootAO, ISession caller) {
		super(caller);
		this.rootAO = rootAO;
		this.name = name;	
	}
	
	@Override
	public void execute(IDataDictionaryWritable dd, IUserManagementWritable um, IServerExecutor executor) {
		dd.setStream(name, rootAO, getCaller());		
	}
	
	@Override
	public String toString() {
		return "Create STREAM "+name;
	}

	public void setOutputSchema(SDFSchema sdfSchema) {
		rootAO.setOutputSchema(sdfSchema);
	}
}
