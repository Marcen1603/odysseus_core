package de.uniol.inf.is.odysseus.codegenerator.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.codegenerator.CAnalyseServiceBinding;
import de.uniol.inf.is.odysseus.codegenerator.model.TransformationParameter;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * This class is used by the QueryCodegenerationPreParserKeyword class, to 
 * generate a new queryCodegenerationCommand. 
 * 
 * @author MarcPreuschaft
 *
 */
public class QueryCodegenerationCommand extends AbstractExecutorCommand {

	//transformationParamter for the query
	private TransformationParameter transformationParameter;
	//name of the query
	private Resource queryname;
	
	private static final long serialVersionUID = 7441618115814307603L;

	public QueryCodegenerationCommand(ISession caller,TransformationParameter transformationParameter, Resource queryname) {
		super(caller);
		this.transformationParameter = transformationParameter;
		this.queryname = queryname;
	}

	
	@Override
	public void execute(IDataDictionaryWritable dd, IUserManagementWritable um,
			IServerExecutor executor) {

		Collection<Integer> queryIds = executor.getLogicalQueryIds(getCaller());
		List<Integer> queryIdList = new ArrayList<Integer>(queryIds);
		
		//if quername not null and not empty get the queryID by queryname
		if(queryname!=null && !queryname.equals("")){
			ILogicalQuery logicalQuery = executor.getLogicalQueryByName(queryname, getCaller());
			transformationParameter.setQueryId(logicalQuery.getID());
		}else{
			//no queryname is set, then take the last added queryID
			transformationParameter.setQueryId(queryIdList.get(queryIdList.size()-1));
		}
		
		// start query analyse for codegeneration
		CAnalyseServiceBinding.getAnalyseComponent().startQueryTransformation(transformationParameter);
		
	}
	

}
