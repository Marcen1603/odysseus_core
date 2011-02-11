package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller.commands.OpenBenchmarkHandler;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkParam;

/**
 * 
 * Diese Klasse öffnet den Benchmarkparameter-Editor, wenn ein Element in der
 * Projektview angeklickt wird
 * 
 * @author Stefanie Witzke
 * 
 */
public class BenchmarkHolderSelectionListener implements ISelectionChangedListener {

	@Override
	public void selectionChanged(SelectionChangedEvent event) {

		Object selectedElem = ((ITreeSelection) event.getSelection()).getFirstElement();
		if (selectedElem instanceof BenchmarkParam) {
			IEditorPart benchmarkEditor = OpenBenchmarkHandler.openBenchmark((BenchmarkParam) selectedElem);
		}
	}
}
