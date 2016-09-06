package de.uniol.inf.is.odysseus.iql.basic.executor;

import java.util.List;

import javax.inject.Inject;



import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.BasicIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;

public class BasicIQLExecutor extends AbstractIQLExecutor<BasicIQLTypeDictionary, BasicIQLTypeUtils> {

	@Inject
	public BasicIQLExecutor(BasicIQLTypeDictionary typeDictionary, BasicIQLTypeUtils typeUtils) {
		super(typeDictionary, typeUtils);
	}

	@Override
	public List<IExecutorCommand> parse(String text, IDataDictionary dd,
			ISession session, Context context) throws QueryParseException {
		// TODO Auto-generated method stub
		return null;
	}



}
