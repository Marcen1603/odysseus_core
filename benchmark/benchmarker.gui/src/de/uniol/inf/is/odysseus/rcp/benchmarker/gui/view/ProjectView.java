/** Copyright [2011] [The Odysseus Team]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.view;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller.BenchmarkHolderContentProvider;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller.BenchmarkHolderLabelProvider;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller.BenchmarkHolderSelectionListener;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.Benchmark;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkGroup;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkHolder;

/**
 * Diese Klasse zeichnet die Projekt-View/TreeViewer
 * 
 * @author Stefanie Witzke
 * 
 */
public class ProjectView extends ViewPart {

	public static final String ID = "de.uniol.inf.is.odysseus.rcp.benchmarker.gui.projectView";

	private TreeViewer treeViewer;
	private static ProjectView projectView;

	/**
	 * Diese Methode erstellt einen Treeviewer mit dem Rootelement
	 * BenchmarkHolder
	 */
	@Override
	public void createPartControl(Composite parent) {

		treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		treeViewer.setContentProvider(new BenchmarkHolderContentProvider());
		treeViewer.setLabelProvider(new BenchmarkHolderLabelProvider());
		treeViewer.setInput(BenchmarkHolder.INSTANCE.getBenchmarkGroups());
		treeViewer.addSelectionChangedListener(new BenchmarkHolderSelectionListener());
		treeViewer.expandAll();
		getViewSite().getActionBars().getToolBarManager().add(new FilterTextTreeViewer("Filter", treeViewer));
	}

	public static ProjectView getDefault() {
		return projectView;
	}

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		projectView = this;
	}

	@Override
	public void setFocus() {
	}

	public void refresh() {
		treeViewer.refresh();
	}
	
	public void refresh(BenchmarkHolder holder){
		treeViewer.refresh(holder, true);
	}

	public void refresh(Benchmark benchmark) {
		treeViewer.refresh(benchmark, true);
	}

	public void refresh(BenchmarkGroup group) {
		treeViewer.refresh(group, true);
	}
}