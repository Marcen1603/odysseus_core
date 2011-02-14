package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkIdHolder;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkParam;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.view.BenchmarkEditorInput;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.view.BenchmarkEditorPart;

/**
 * Diese Klasse öffnet den Benchmarkparameter-Editor
 * 
 * @author Stefanie Witzke
 * 
 */
public class OpenBenchmarkHandler extends AbstractHandler {

	// private static int count = 0;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		// Holt die aktuelle View von event
		openBenchmark(HandlerUtil.getActiveWorkbenchWindow(event).getActivePage(), new BenchmarkParam(
				BenchmarkIdHolder.INSTANCE.generateNextId()));
		return null;
	}

	public static IEditorPart openBenchmark(final BenchmarkParam benchmarkParam) {
		return openBenchmark(getPage(), benchmarkParam);
	}

	private static IEditorPart openBenchmark(IWorkbenchPage page, BenchmarkParam benchmarkParam) {
		try {
			// Öffnet den Benchmarkparameter-Editor
			return (BenchmarkEditorPart) page.openEditor(new BenchmarkEditorInput(benchmarkParam),
					BenchmarkEditorPart.ID);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static IEditorPart copyAndOpenBenchmark(final BenchmarkParam benchmarkParam) {
		BenchmarkParam copyParam = benchmarkParam.clone();
		copyParam.setId(BenchmarkIdHolder.INSTANCE.generateNextId());
		return openBenchmark(getPage(), copyParam);
	}

	private static IWorkbenchPage getPage() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	}
}