package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.factorys.keyword;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.factorys.DefaultSymKeyFactory;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.secret.DefaultSymKeyVault;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.secret.ISymKeyVault;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

/**
 * A PreParserKeyword, to create a symmetric Key
 * 
 * @author MarkMilster
 *
 */
public class SymKeyFactoryKeyword extends AbstractPreParserKeyword {

	public static final String NAME = "CREATESYMKEY";

	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context,
			IServerExecutor executor) throws OdysseusScriptException {
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller,
			Context context, IServerExecutor executor) throws OdysseusScriptException {
		KeyWrapper<Key> keyWrapper = DefaultSymKeyFactory.getInstance().createSymKey(parameter, null,
				"Created by Keyword");
		// save symKey
		ISymKeyVault symKeyVault = new DefaultSymKeyVault();
		symKeyVault.setSymKey(keyWrapper);
		return null;
	}

	@Override
	public Collection<String> getAllowedParameters(ISession caller) {
		return new ArrayList<String>(Arrays.asList(DefaultSymKeyFactory.ALGORITHMS));
	}

}
