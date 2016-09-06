package de.uniol.inf.is.odysseus.eca.plugin;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class EcaParser implements IQueryParser {
	private static Context context;

	@Override
	public String getLanguage() {
		return "ACL";
	}

	@Override
	public List<IExecutorCommand> parse(String query, ISession user, IDataDictionary dd, Context context,
			IMetaAttribute metaAttribute, IServerExecutor executor) throws QueryParseException {
		return null;
	}

	@Override
	public Map<String, List<String>> getTokens(ISession user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getSuggestions(String hint, ISession user) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		EcaParser.context = context;
	}

}
