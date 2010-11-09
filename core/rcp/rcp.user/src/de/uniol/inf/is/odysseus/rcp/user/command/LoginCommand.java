package de.uniol.inf.is.odysseus.rcp.user.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uniol.inf.is.odysseus.rcp.user.Login;

public class LoginCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		Login.loginWindow(HandlerUtil.getActiveShell(event), true, true);
		
		return null;
	}

}
