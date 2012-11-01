package de.uniol.inf.is.odysseus.rcp.viewer.stream.thematicalmap;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapWizardEarlyStartup implements IStartup{
	private static final Logger LOG = LoggerFactory.getLogger(MapWizardEarlyStartup.class);
	 public void earlyStartup() {
	      IWorkbench workbench = PlatformUI.getWorkbench();
	      workbench.getDisplay().syncExec(new Runnable() {
	         public void run() {
	        	 LOG.info("Erstelle MapWizardEarlyStartup");
	         }
	      });
	   }
}
