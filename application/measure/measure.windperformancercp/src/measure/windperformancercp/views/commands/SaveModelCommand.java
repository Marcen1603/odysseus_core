package measure.windperformancercp.views.commands;

import measure.windperformancercp.controller.MainController;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;


public class SaveModelCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		MainController _cont = MainController.getInstance();
		_cont.saveData();
		
		return null;
	}

}
