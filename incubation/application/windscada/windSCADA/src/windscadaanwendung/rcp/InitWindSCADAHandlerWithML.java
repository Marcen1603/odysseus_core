package windscadaanwendung.rcp;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import windscadaanwendung.ca.WindSCADAInitializer;

public class InitWindSCADAHandlerWithML extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		WindSCADAInitializer.init(true);
		return null;
	}



}
