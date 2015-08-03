package de.uniol.inf.is.odysseus.iql.basic.parser;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.resources.IProject;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLFile;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;

public class BasicIQLParser extends AbstractIQLParser<BasicIQLTypeFactory, BasicIQLTypeUtils> {

	@Inject
	public BasicIQLParser(BasicIQLTypeFactory typeFactory, BasicIQLTypeUtils typeUtils) {
		super(typeFactory, typeUtils);
	}

	@Override
	public void parse(IQLFile file, IProject project) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getLanguageName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IExecutorCommand> parse(String text, IDataDictionary dd,
			ISession session, Context context) throws QueryParseException {
		// TODO Auto-generated method stub
		return null;
	}



}
