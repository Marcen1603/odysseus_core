package de.uniol.inf.is.odysseus.parallelization.rcp.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.parallelization.rcp.windows.InterOperatorParallelizationBenchmarkerWindow;

public class InterOperatorParallelizationBenchmarkerWindowHandler extends
		AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		InterOperatorParallelizationBenchmarkerWindow window = new InterOperatorParallelizationBenchmarkerWindow(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		window.show();

		return null;
	}

}
