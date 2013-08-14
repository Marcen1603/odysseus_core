package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class CreateDatatypeCommand extends AbstractExecutorCommand {

	private SDFDatatype newType;

	public CreateDatatypeCommand(SDFDatatype newType, ISession caller) {
		super(caller);
		this.newType = newType;
	}

	@Override
	public Collection<Integer> execute(IDataDictionaryWritable dd,
			IUserManagementWritable um) {
		dd.addDatatype(newType);
		return getEmptyCollection();
	}

}
