package de.uniol.inf.is.odysseus.visualquerylanguage;

import java.io.IOException;
import java.net.URL;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.INodeContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.resource.XMLParameterParser;
import de.uniol.inf.is.odysseus.visualquerylanguage.swt.SWTMainWindow;
import de.uniol.inf.is.odysseus.vqlinterfaces.ctrl.DefaultController;
import de.uniol.inf.is.odysseus.vqlinterfaces.ctrl.IController;
import de.uniol.inf.is.odysseus.vqlinterfaces.model.graph.DefaultGraphModel;
import de.uniol.inf.is.odysseus.vqlinterfaces.model.graph.IGraphModel;
import de.uniol.inf.is.odysseus.vqlinterfaces.swt.resource.SWTResourceManager;
import de.uniol.inf.is.odysseus.vqlinterfaces.swt.resource.XMLResourceConfiguration;

public class EditorStarter implements CommandProvider{
	
	private static final Logger logger = LoggerFactory.getLogger(EditorStarter.class);
	
	
	private IAdvancedExecutor executor;
	
	public void build() throws IOException {

		Display d = new Display();
		
		try {
			//URL resources = context.getBundle().getResource("/editor_cfg/resources.xml");
			XMLResourceConfiguration cfg = new XMLResourceConfiguration("C:/Informatik/Odysseus/de.uniol.inf.is.odysseus.visualquerylanguage/editor_cfg/resources.xml");
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
