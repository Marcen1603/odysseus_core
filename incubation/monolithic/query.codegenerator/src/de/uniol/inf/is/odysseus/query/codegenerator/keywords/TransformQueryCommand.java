package de.uniol.inf.is.odysseus.query.codegenerator.keywords;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.TransformationParameter;
import de.uniol.inf.is.odysseus.query.codegenerator.CAnalyseServiceBinding;

public class TransformQueryCommand extends AbstractExecutorCommand {

	private TransformationParameter transformationParameter;
	private static final long serialVersionUID = 7441618115814307603L;

	public TransformQueryCommand(ISession caller,TransformationParameter transformationParameter) {
		super(caller);
		this.transformationParameter = transformationParameter;
	}

	@Override
	public void execute(IDataDictionaryWritable dd, IUserManagementWritable um,
			IServerExecutor executor) {
	

		Collection<Integer> queryIds = executor.getLogicalQueryIds(getCaller());
		transformationParameter.setQueryId(queryIds.iterator().next());
		
		
		CAnalyseServiceBinding.getAnalyseComponent().startQueryTransformation(transformationParameter, null);
		
	}
	
	

	

}
