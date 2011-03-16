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

import java.util.List;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.Benchmark;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkGroup;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkHolder;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkParam;
import de.uniol.inf.is.odysseus.rcp.benchmarker.utils.StringUtils;

/**
 * Diese Klasse fügt dem Wizard die Seiten hinzu und ruft den BenchmarkEditor
 * auf
 * 
 * @author Stefanie Witzke
 * 
 */
public class BenchmarkWizard extends Wizard {

	private BenchmarkWizardPage page;
	private Benchmark benchmark;
	private List<BenchmarkGroup> groups;

	public BenchmarkWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		page = new BenchmarkWizardPage();
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		// gibt den directorypfad bzw. den directiorynamen aus
		String path = page.getDropDown();
		String pathname = StringUtils.splitString(path);

		setGroups(BenchmarkHolder.INSTANCE.getBenchmarkGroups());

		benchmark = new Benchmark(new BenchmarkParam());

		BenchmarkGroup benchmarkGroup = null;
		for (BenchmarkGroup group : BenchmarkHolder.INSTANCE.getBenchmarkGroups()) {
			if (pathname.equals(group.getName())) {
				benchmarkGroup = group;
				benchmark.setParentGroup(benchmarkGroup);
				break;
			}
		}

		if (benchmarkGroup == null) {
			benchmarkGroup = new BenchmarkGroup(pathname);
			benchmark.setParentGroup(benchmarkGroup);
			benchmark.getParam().setId(benchmarkGroup.getNextId());
			BenchmarkHolder.INSTANCE.addBenchmarkGroup(benchmarkGroup);
		} else {
			benchmark.getParam().setId(benchmark.getParentGroup().getNextId());
		}

		benchmark.getParentGroup().addBenchmark(benchmark);

		openBenchmark(getPage(), benchmark); // neu
		refreshNavigator();
		return true;
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

	private static IWorkbenchPage getPage() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	}

	private void refreshNavigator() {
		ProjectView projectView = (ProjectView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(ProjectView.ID);
		projectView.refresh();
	}

	public void setGroups(List<BenchmarkGroup> groups) {
		this.groups = groups;
	}

	public List<BenchmarkGroup> getGroups() {
		return groups;
	}
}
