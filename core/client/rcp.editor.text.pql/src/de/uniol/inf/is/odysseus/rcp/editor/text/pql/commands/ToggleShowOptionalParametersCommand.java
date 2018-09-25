package de.uniol.inf.is.odysseus.rcp.editor.text.pql.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.uniol.inf.is.odysseus.rcp.editor.text.pql.PQLOperatorView;

public class ToggleShowOptionalParametersCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		PQLOperatorView view = ViewHelper.getPQLOperatorView();
		if( view != null ) {
			view.toggleShowOptionalParameters();
			return true;
		}
		return false;

	}

}
