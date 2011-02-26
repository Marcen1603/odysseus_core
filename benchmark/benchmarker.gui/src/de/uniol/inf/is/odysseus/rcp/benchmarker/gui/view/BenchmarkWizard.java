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
import de.uniol.inf.is.odysseus.rcp.benchmarker.utils.BenchmarkStoreUtil;
import de.uniol.inf.is.odysseus.rcp.benchmarker.utils.StringUtils;

public class BenchmarkWizard extends Wizard {

	private BenchmarkWizardPage page;
	private Benchmark benchmark;
	private List<BenchmarkGroup> groups;
	private BenchmarkGroup benchmarkGroup;

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
		// TODO: Wir brauchen hier noch die BEnchmarkgroup
		// Entweder aus BenchmarkHolder holen oder neue Group anlegen

		groups = BenchmarkHolder.INSTANCE.getBenchmarkGroups();

		benchmark = new Benchmark(new BenchmarkParam());

		// BenchmarkGroup benchmarkGroup;
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
		} else {
			benchmark.getParam().setId(benchmark.getParentGroup().getNextId());
		}

		benchmark.getParentGroup().addBenchmark(benchmark);

		// TODO: Benchmark erzeugen und ParentGroup zuweisen!*
		// TODO: Benchmark übergeben statt Param - aber mit nächster ID aus der
		// Gruppe!
		// Holt die aktuelle View von event

		openBenchmark(getPage(), benchmark); // new
		refreshNavigator(benchmark.getParentGroup()); 
		
		// BenchmarkParam(benchmarkGroup.getNextId());//new
		// Benchmark(benchmarkGroup.getNextId()));

		return true;
	}

	// private BenchmarkGroup getBenchmarkGroup(String name) {
	// for (BenchmarkGroup bg : groups) {
	// if (bg.equals(new BenchmarkGroup(name)))
	// return bg;
	// }
	// return null;
	//
	// }

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

	private void refreshNavigator(BenchmarkGroup group) {
		ProjectView projectView = (ProjectView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(ProjectView.ID);
		projectView.refresh(group);
	}
}
