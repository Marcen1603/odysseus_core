package de.uniol.inf.is.odysseus.incubation.graph.keyword;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.incubation.graph.provider.GraphDataStructureProvider;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

/**
 * Class implementing a keyword for removing all versions of a graph from provider.
 * 
 * @author Kristian Bruns
 */
public class GraphdatastructureDropKeyword extends AbstractPreParserKeyword {

	public static final String GRAPHDATASTRUCTURE = "DROPGRAPHDATASTRUCTURE";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context,
			IServerExecutor executor) throws OdysseusScriptException {		
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller,
			Context context, IServerExecutor executor) throws OdysseusScriptException {
		GraphDataStructureProvider.getInstance().removeGraphDataStructure(parameter);
		return null;
	}
	
	public Collection<String> getAllowedParameters(ISession caller) {
		return GraphDataStructureProvider.getInstance().getStructureNames();
	}

}
