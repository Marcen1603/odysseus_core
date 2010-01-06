package de.uniol.inf.is.odysseus.viewer;

import java.util.ArrayList;
import java.util.ListIterator;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.viewer.ctrl.DefaultController;
import de.uniol.inf.is.odysseus.viewer.ctrl.IController;
import de.uniol.inf.is.odysseus.viewer.model.create.IModelProvider;
import de.uniol.inf.is.odysseus.viewer.model.create.OdysseusModelProviderSink;
import de.uniol.inf.is.odysseus.viewer.swt.SWTExceptionWindow;
import de.uniol.inf.is.odysseus.viewer.swt.SWTMainWindow;

public class ViewerStarter implements Runnable, IPlanModificationListener  {

	private static final String SHELL_TITLE = "ODYSSEUS - Query Plan Viewer";
	private static final int SHELL_SIZE = 800;

	private static final Logger logger = LoggerFactory.getLogger(ViewerStarter.class);
	private IController<IPhysicalOperator> controller = new DefaultController<IPhysicalOperator>();
	private SWTMainWindow viewer;
	private ViewerStarterConfiguration config;

	public ViewerStarter() {
		this(null);
	}

	public ViewerStarter(ViewerStarterConfiguration cfg) {
		config = cfg;
		if (config == null)
			config = new ViewerStarterConfiguration();
	}

	@Override
	public void run() {

		final Display display = new Display();
		try {
			
			// View erzeugen
			final Shell shell = new Shell(display);
			shell.setText(SHELL_TITLE);
			shell.setSize(SHELL_SIZE, SHELL_SIZE);

			if (config.useOGL)
				logger.info("Using OpenGL for rendering");
			else
				logger.info("Using SWT for rendering");
			viewer = new SWTMainWindow(shell, controller, config.useOGL);

			shell.open();

			logger.info("Viewer started!");

			while (!shell.isDisposed() && !Thread.currentThread().isInterrupted()) {
				try {
					if (!display.readAndDispatch())
						display.sleep();
				} catch (Exception ex) {
					ex.printStackTrace();
					new SWTExceptionWindow(shell, ex);
				}
			}

		} finally {
			logger.info("Viewer closed!");

			if (viewer != null)
				viewer.dispose();

			if (!display.isDisposed())
				display.dispose();
		}
	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		try {
			ArrayList<IPhysicalOperator> roots = eventArgs.getSender().getSealedPlan().getRoots();
			updateModel(roots);
		} catch (PlanManagementException e) {
			e.printStackTrace();
		}
	}
	

	private void updateModel(ArrayList<IPhysicalOperator> roots) {
		if (!roots.isEmpty()) {
			
			ListIterator<IPhysicalOperator> li = roots.listIterator(roots.size());
			IPhysicalOperator lastRoot = null;
			do {
				lastRoot = li.previous();
			} while (li.hasPrevious() && lastRoot == null);
			if (lastRoot != null && lastRoot instanceof ISink<?>) {
				IModelProvider<IPhysicalOperator> provider = new OdysseusModelProviderSink((ISink<?>) lastRoot);
				controller.getModelManager().addModel(provider.get());
			}
		}
	}
}
