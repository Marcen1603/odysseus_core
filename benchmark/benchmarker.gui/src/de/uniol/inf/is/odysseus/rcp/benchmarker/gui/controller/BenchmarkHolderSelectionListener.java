/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller.commands.OpenBenchmarkHandler;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.Benchmark;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkMetadata;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkParam;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkResult;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.view.MetadataEditorInput;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.view.MetadataEditorPart;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.view.ResultEditorInput;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.view.ResultEditorPart;

/**
 * 
 * Diese Klasse öffnet die passende View/Editor für das Element, welches in dem
 * TreeViewer angeklickt wird
 * 
 * @author Stefanie Witzke
 * 
 */
public class BenchmarkHolderSelectionListener implements ISelectionChangedListener {

	@Override
	public void selectionChanged(SelectionChangedEvent event) {

		Object selectedElem = ((ITreeSelection) event.getSelection()).getFirstElement();
		Object parentElem = null;
		TreePath[] parentPaths = ((ITreeSelection) event.getSelection()).getPaths();
		if (parentPaths.length > 0) {
			parentElem = parentPaths[0].getParentPath().getLastSegment();
		}
		if (selectedElem instanceof BenchmarkParam && parentElem != null) {
			OpenBenchmarkHandler.openBenchmark((Benchmark) parentElem);
			// Benchmarkparameter-Editor öffnen:
		} else if (selectedElem instanceof Benchmark) {
			OpenBenchmarkHandler.openBenchmark((Benchmark) selectedElem);
			// Benchmarkmetadata-View öffnen:
		} else if (selectedElem instanceof BenchmarkMetadata) {
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			try {
				page.openEditor(new MetadataEditorInput(((BenchmarkMetadata) selectedElem).getMetadata(),
						(Benchmark) parentElem), MetadataEditorPart.ID);
			} catch (PartInitException e) {
				System.out.println("Hier catcht er");
				e.printStackTrace();
			}
			// Benchmarkresult-View öffnen:
		} else if (selectedElem instanceof BenchmarkResult) {
			TreePath[] pathsForElement = ((ITreeSelection) event.getSelection()).getPathsFor(selectedElem);
			TreePath treePathToResult = pathsForElement[0];
			Benchmark benchmarkOfResult = null;
			// Suche nach dem entsprechenden Benchmark zum Result
			for (int i = 0; i < treePathToResult.getSegmentCount(); i++) {
				if (treePathToResult.getSegment(i) instanceof Benchmark) {
					benchmarkOfResult = (Benchmark) treePathToResult.getSegment(i);
				}
			}
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			try {
				page.openEditor(new ResultEditorInput(((BenchmarkResult) selectedElem), benchmarkOfResult),
						ResultEditorPart.ID);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}
	}
}
