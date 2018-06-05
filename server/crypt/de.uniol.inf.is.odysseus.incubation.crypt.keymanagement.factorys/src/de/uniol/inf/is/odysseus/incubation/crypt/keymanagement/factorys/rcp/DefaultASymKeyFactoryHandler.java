package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.factorys.rcp;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.factorys.DefaultAsymKeyFactory;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.ASymKeyWrapper;

public class DefaultASymKeyFactoryHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ASymKeyWrapper keys = DefaultAsymKeyFactory.getInstance().createAsymKeyPair("RSA", 1024, null,
				"Created by OdysseusStudio");
		DefaultAsymKeyFactory.getInstance().saveAsymKeys(keys);
		return keys;
	}

}
