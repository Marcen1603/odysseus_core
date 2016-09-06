package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.factorys.rcp;

import java.security.Key;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.factorys.DefaultSymKeyFactory;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.secret.DefaultSymKeyVault;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.secret.ISymKeyVault;

public class DefaultSymKeyFactoryHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		KeyWrapper<Key> keyWrapper = DefaultSymKeyFactory.getInstance().createSymKey("AES", null,
				"Created by OdysseusStudio");
		// save symKey
		ISymKeyVault symKeyVault = new DefaultSymKeyVault();
		symKeyVault.setSymKey(keyWrapper);
		return keyWrapper;
	}

}
