package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.view;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller.BenchmarkHolderContentProvider;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller.BenchmarkHolderLabelProvider;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller.BenchmarkHolderSelectionListener;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.Benchmark;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkHolder;

/**
 * Diese Klasse zeichnet die Projekt-View
 * 
 * @author Stefanie Witzke
 *
 */
public class ProjectView extends ViewPart {

	public static final String ID = "de.uniol.inf.is.odysseus.rcp.benchmarker.gui.projectView";

	private TreeViewer treeViewer;

	/**
	 * Diese Methode erstellt einen Treeviewer mit dem Rootelement BenchmarkHolder
	 */
	@Override
	public void createPartControl(Composite parent) {
		treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		treeViewer.setContentProvider(new BenchmarkHolderContentProvider());
		treeViewer.setLabelProvider(new BenchmarkHolderLabelProvider());
		treeViewer.setInput(BenchmarkHolder.INSTANCE.getBenchmarks());
		treeViewer.addSelectionChangedListener(new BenchmarkHolderSelectionListener());
		treeViewer.expandAll();
	}

	@Override
	public void setFocus() {
		
	}
	
	public void refresh() {
		treeViewer.refresh();
	}
	
	public void refresh(Benchmark benchmark) {
		treeViewer.refresh(benchmark, true);
	}
}
