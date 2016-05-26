package de.uniol.inf.is.odysseus.server.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Injector;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.server.StreamingsparqlStandaloneSetup;

public class StreamingSparqlQueryParser implements IQueryParser{
	

	@Override
	public List<IExecutorCommand> parse(String query, ISession user, IDataDictionary dd, Context context,
			IMetaAttribute metaAttribute, IServerExecutor executor) throws QueryParseException {

		Injector injector = new StreamingsparqlStandaloneSetup().createInjectorAndDoEMFRegistration();
		IStreamingSparqlParser parser = injector.getInstance(IStreamingSparqlParser.class);

		executor.addQuery(parser.parse(query, user, dd, context, metaAttribute, executor), "OdysseusScript", OdysseusRCPPlugIn.getActiveSession(), context);
		return new ArrayList<>();
	}

	@Override
	public String getLanguage() {
		return "StreamingSparql";
	}


	@Override
	public Map<String, List<String>> getTokens(ISession user) {
		Map<String, List<String>> tokens = new HashMap<>();
		List<String> tokensList = new ArrayList<String>();
		tokens.put("TOKEN", tokensList);
		return tokens;
	}

	@Override
	public List<String> getSuggestions(String hint, ISession user) {
		return new ArrayList<>();
	}

}
