package de.uniol.inf.is.odysseus.viewer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.viewer.ctrl.DefaultController;
import de.uniol.inf.is.odysseus.viewer.ctrl.IController;
import de.uniol.inf.is.odysseus.viewer.model.create.IModelProvider;
import de.uniol.inf.is.odysseus.viewer.swt.SWTExceptionWindow;
import de.uniol.inf.is.odysseus.viewer.swt.SWTMainWindow;

public class ViewerStarter implements Runnable {

	private IModelProvider<IPhysicalOperator> modelProvider;
	private static final String SHELL_TITLE = "ODYSSEUS - Query Plan Viewer";
	private static final int SHELL_SIZE = 800;

	private static final Logger logger = LoggerFactory
			.getLogger(ViewerStarter.class);
	private IController<IPhysicalOperator> controller;
	private SWTMainWindow viewer;
	private ViewerStarterConfiguration config;

	public ViewerStarter(IModelProvider<IPhysicalOperator> modelProvider) {
		this(modelProvider, null);
	}

	public ViewerStarter() {
		this(null);
	}

	public ViewerStarter(IModelProvider<IPhysicalOperator> modelProvider,
			ViewerStarterConfiguration cfg) {
		setModelProvider(modelProvider);
		config = cfg;
		if (config == null)
			config = new ViewerStarterConfiguration();
	}

	public void setModelProvider(IModelProvider<IPhysicalOperator> modelProvider) {
		this.modelProvider = modelProvider;
		if (controller != null) {
			controller.setModelProvider(modelProvider);

			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					viewer.reloadModel();
				}
			});

		}
	}

	@Override
	public void run() {

		final Display display = new Display();
		try {

			controller = new DefaultController<IPhysicalOperator>();
			if (modelProvider != null) {
				controller.setModelProvider(modelProvider);
			}

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

			while (!shell.isDisposed()
					&& !Thread.currentThread().isInterrupted()) {
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
}
