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

public class EditorStarter implements CommandProvider{
	
	private static final Logger logger = LoggerFactory.getLogger(EditorStarter.class);
	
	
	private IAdvancedExecutor executor;
	
	public void build() throws IOException {

		Display d = new Display();
		
		try {
			URL xmlFile = Activator.getContext().getBundle().getEntry("editor_cfg/resources.xml");
			URL xsdFile = Activator.getContext().getBundle().getEntry("editor_cfg/resourcesSchema.xsd");
			XMLResourceConfiguration cfg = new XMLResourceConfiguration(xmlFile, xsdFile);
			SWTResourceManager.getInstance().load(d, cfg);
		} catch (IOException e) {
			logger.error("Could not load XMLConfiguration because ");
			logger.error(e.getMessage());
		}
		 
		
		SWTMainWindow main = new SWTMainWindow(Display.getDefault(), executor);
		
		SWTResourceManager.getInstance().freeAllResources();
	}
	
	public void _visual(CommandInterpreter interpreter) {
		try {
			build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.getStackTrace();
		}
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void bindExecutor(IAdvancedExecutor executor) {
		this.executor = executor;
	}
	
	private void unbindExecutor(IAdvancedExecutor executor) {
		if(this.executor == executor) {
			this.executor = null;
		}
	}
}
