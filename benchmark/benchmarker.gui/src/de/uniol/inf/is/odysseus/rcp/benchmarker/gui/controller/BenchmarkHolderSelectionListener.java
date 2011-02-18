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
package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IEditorPart;

import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller.commands.OpenBenchmarkHandler;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkParam;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.Benchmark;
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
		Object parentElem = ((ITreeSelection) event.getSelection()).getPaths()[0].getParentPath().getLastSegment();
		
		if (selectedElem instanceof BenchmarkParam) {
			IEditorPart benchmarkEditor = OpenBenchmarkHandler.openBenchmark((Benchmark) parentElem);
		}
	}
}
