package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd;


import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;


public class CreateStreamCommand extends AbstractCreateStreamOrViewCommand {
	
	private static final long serialVersionUID = 9213019419682030013L;

	public CreateStreamCommand(String name, ILogicalOperator rootAO, ISession caller) {
		super(name, rootAO, caller);
	}
	
	@Override
	public void execute(IDataDictionaryWritable dd, IUserManagementWritable um, IServerExecutor executor) {
		checkAccessAONames(dd);
		dd.setStream(name, new LogicalPlan(rootAO), getCaller());		
	}

	public void setOutputSchema(SDFSchema sdfSchema) {
		rootAO.setOutputSchema(sdfSchema);
	}
}
