package de.uniol.inf.is.odysseus.parallelization.rcp.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.parallelization.rcp.windows.ParallelizationBenchmarkerWindow;

public class InterOperatorParallelizationBenchmarkerWindowHandler extends
		AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ParallelizationBenchmarkerWindow window = new ParallelizationBenchmarkerWindow(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		window.show();

		return null;
	}

}
