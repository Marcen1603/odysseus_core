package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
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
	public Collection<Integer> execute(IDataDictionaryWritable dd, IUserManagementWritable um) {
		dd.addSink(name, sinkAO, getCaller());
		return getEmptyCollection();
	}


	
}
