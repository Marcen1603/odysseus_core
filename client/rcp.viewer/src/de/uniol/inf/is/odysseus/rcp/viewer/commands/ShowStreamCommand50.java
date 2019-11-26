package de.uniol.inf.is.odysseus.rcp.viewer.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class ShowStreamCommand50 extends ShowStreamCommand {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		return super.execute(event,"de.uniol.inf.is.odysseus.rcp.streams.List50");
	}

}
