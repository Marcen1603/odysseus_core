package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class CreateViewCommand extends AbstractExecutorCommand {

	private ILogicalOperator rootAO;
	private String name;

	public CreateViewCommand(String name, ILogicalOperator rootAO, ISession caller) {
		super(caller);
		this.rootAO = rootAO;
		this.name = name;
	}
	
	@Override
	public Collection<Integer> execute(IDataDictionaryWritable dd) {
		dd.setView(name, rootAO, getCaller());		
		return getEmptyCollection();
	}
	
	@Override
	public String toString() {
		return "Create VIEW "+name;
	}

	
}
