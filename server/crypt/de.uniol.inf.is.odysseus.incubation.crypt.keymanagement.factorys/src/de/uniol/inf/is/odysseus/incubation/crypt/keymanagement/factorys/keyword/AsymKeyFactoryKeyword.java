package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.factorys.keyword;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.factorys.DefaultAsymKeyFactory;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.ASymKeyWrapper;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

/**
 * A PreParserKeyword, used to create a AsymKeyPair
 * 
 * @author MarkMilster
 *
 */
public class AsymKeyFactoryKeyword extends AbstractPreParserKeyword {

	public static final String NAME = "CREATEASYMKEYS";

	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context,
			IServerExecutor executor) throws OdysseusScriptException {
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller,
			Context context, IServerExecutor executor) throws OdysseusScriptException {
		// TODO use the size as second parameter by ssing KeywordParameterParser
		ASymKeyWrapper keys = DefaultAsymKeyFactory.getInstance().createAsymKeyPair(parameter, 1024, null,
				"Created by Keyword");
		DefaultAsymKeyFactory.getInstance().saveAsymKeys(keys);
		return null;
	}

	@Override
	public Collection<String> getAllowedParameters(ISession caller) {
		return new ArrayList<String>(Arrays.asList(DefaultAsymKeyFactory.ALGORITHMS));
	}

}
