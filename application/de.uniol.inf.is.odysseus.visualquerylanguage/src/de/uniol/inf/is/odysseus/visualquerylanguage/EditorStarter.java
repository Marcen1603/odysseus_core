package de.uniol.inf.is.odysseus.visualquerylanguage;

import java.io.IOException;
import java.net.URL;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.viewer.swt.resource.SWTResourceManager;
import de.uniol.inf.is.odysseus.viewer.swt.resource.XMLResourceConfiguration;
import de.uniol.inf.is.odysseus.visualquerylanguage.swt.SWTMainWindow;

public class EditorStarter implements CommandProvider, Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(EditorStarter.class);

	private IAdvancedExecutor executor;

	public void _visual(CommandInterpreter interpreter) {
		
		Display d = Display.getDefault();
		d.asyncExec(this);
		
//		Thread thread = new Thread(this);
//		thread.setDaemon(true);
//		thread.start();
	}

	@Override
	public String getHelp() {
		return null;
	}

	@SuppressWarnings("unused")
	private void bindExecutor(IAdvancedExecutor executor) {
		this.executor = executor;
	}

	@SuppressWarnings("unused")
	private void unbindExecutor(IAdvancedExecutor executor) {
		if (this.executor == executor) {
			this.executor = null;
		}
	}

	@Override
	public void run() {

		try {
			URL xmlFile = de.uniol.inf.is.odysseus.viewer.Activator.getContext().getBundle().getEntry(
					"editor_cfg/resources.xml");
			URL xsdFile = de.uniol.inf.is.odysseus.viewer.Activator.getContext().getBundle().getEntry(
					"editor_cfg/resourcesSchema.xsd");
			XMLResourceConfiguration cfg = new XMLResourceConfiguration(
					xmlFile, xsdFile);
//			SWTResourceManager.getInstance().freeAllResources();
			SWTResourceManager.getInstance().load(Display.getDefault(), cfg);
		} catch (IOException e) {
			logger.error("Could not load XMLConfiguration because ");
			logger.error(e.getMessage());
		}

		try {
			@SuppressWarnings("unused")
			SWTMainWindow main = new SWTMainWindow(Display.getDefault(), executor);
		} catch (IOException e) {
			logger.error("SWTMainWindow not loaded");
		}

//		SWTResourceManager.getInstance().freeAllResources();

	}
}
