package de.uniol.inf.is.odysseus.rcp.logging.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.rcp.logging.view.LoggingView;

public class AddLogTabCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Optional<LoggingView> optInstance = LoggingView.getInstance();
		if( optInstance.isPresent() ) {
			LoggingView loggingView = optInstance.get();
			loggingView.addNewFilterTab();
		}
		return null;
	}

}
