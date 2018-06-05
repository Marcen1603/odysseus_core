package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd;


import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class CreateDatatypeCommand extends AbstractExecutorCommand {

	private static final long serialVersionUID = 567854863615540518L;
	
	private SDFDatatype newType;

	public CreateDatatypeCommand(SDFDatatype newType, ISession caller) {
		super(caller);
		this.newType = newType;
	}

	@Override
	public void execute(IDataDictionaryWritable dd,
			IUserManagementWritable um, IServerExecutor executor) {
		dd.addDatatype(newType);
	}

}
