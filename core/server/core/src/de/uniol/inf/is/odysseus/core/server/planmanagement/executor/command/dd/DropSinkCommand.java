package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd;


import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class DropSinkCommand extends AbstractExecutorCommand {

	private static final long serialVersionUID = -6356946239267417635L;
	
	private String sinkname;
	private boolean ifExists;

	public DropSinkCommand(String sinkname, boolean ifExists, ISession caller) {
		super(caller);
		this.sinkname = sinkname;
		this.ifExists = ifExists;
		
	}

	@Override
	public void execute(IDataDictionaryWritable dd, IUserManagementWritable um, IServerExecutor executor) {
		if (ifExists) {
			if(dd.containsSink(sinkname, getCaller())){
				dd.removeSink(sinkname, getCaller());
			}			
		} else {
			dd.removeSink(sinkname, getCaller());
		}
	}
	
	public String getName() {
		return sinkname;
	}

}
