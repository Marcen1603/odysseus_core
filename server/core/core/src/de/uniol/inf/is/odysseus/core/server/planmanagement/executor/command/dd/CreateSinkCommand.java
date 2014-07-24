package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd;


import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class CreateSinkCommand extends AbstractExecutorCommand {

	final private String name;
	final private ILogicalOperator sinkAO;

	public CreateSinkCommand(String name, ILogicalOperator sinkAO, ISession caller) {
		super(caller);
		this.name = name;
		this.sinkAO = sinkAO;
	}
	
	@Override
	public void execute(IDataDictionaryWritable dd, IUserManagementWritable um, IServerExecutor executor) {
		dd.addSink(name, sinkAO, getCaller());
	}


	
}
