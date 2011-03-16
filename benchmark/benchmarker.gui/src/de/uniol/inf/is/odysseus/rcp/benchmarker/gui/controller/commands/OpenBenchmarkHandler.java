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
package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.Benchmark;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkGroup;
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

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		return null;
	}

	public static IEditorPart openBenchmark(final Benchmark benchmark) {
		return openBenchmark(getPage(), benchmark);
	}

	private static IEditorPart openBenchmark(IWorkbenchPage page, Benchmark benchmark) {
		try {
			// Öffnet den Benchmarkparameter-Editor
			return (BenchmarkEditorPart) page.openEditor(new BenchmarkEditorInput(benchmark), BenchmarkEditorPart.ID);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Die Methode öffnet eine Copy des Benchmarkparameter-Editors
	 * 
	 * @param benchmark
	 */
	public static IEditorPart copyAndOpenBenchmark(final Benchmark benchmark) {
		BenchmarkGroup parentGroup = benchmark.getParentGroup();

		BenchmarkParam copyParam = benchmark.getParam().clone();
		copyParam.setId(parentGroup.getNextId());
		Benchmark copyBenchmark = new Benchmark(copyParam);
		copyBenchmark.setParentGroup(parentGroup);
		copyBenchmark.getParam().setReadOnly(false);
		parentGroup.addBenchmark(copyBenchmark);
		return openBenchmark(getPage(), copyBenchmark);
	}

	private static IWorkbenchPage getPage() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	}
}