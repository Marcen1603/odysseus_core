package windscadaanwendung.rcp;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import windscadaanwendung.ca.WindSCADAInitializer;

/**
 * Command that lets the WindSCADAInitializer initialize windSCADA without
 * machine learning algorithms
 * 
 * @author Dennis Nowak
 * 
 */
public class InitWindSCADAHandlerWithML extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		WindSCADAInitializer.init(true);
		return null;
	}

}
