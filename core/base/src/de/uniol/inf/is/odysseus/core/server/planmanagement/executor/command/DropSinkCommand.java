package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class DropSinkCommand extends AbstractExecutorCommand {

	private String sinkname;
	private boolean ifExists;

	public DropSinkCommand(String sinkname, boolean ifExists, ISession caller) {
		super(caller);
		this.sinkname = sinkname;
		this.ifExists = ifExists;
		
	}

	@Override
	public Collection<Integer> execute(IDataDictionaryWritable dd) {
		if (ifExists) {
			if(dd.containsSink(sinkname, getCaller())){
				dd.removeSink(sinkname, getCaller());
			}			
		} else {
			dd.removeSink(sinkname, getCaller());
		}
		return getEmptyCollection();
	}

}
