package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.evaluation;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;


public class AddManyBadlyShareableQueriesCommand extends AbstractHandler implements IHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		AddManyBadlyShareableQueries test = new AddManyBadlyShareableQueries();
		test.start();
		return null;
	}
}
