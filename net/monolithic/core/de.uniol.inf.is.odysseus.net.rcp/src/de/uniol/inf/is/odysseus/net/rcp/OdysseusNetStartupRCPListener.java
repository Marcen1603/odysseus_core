package de.uniol.inf.is.odysseus.net.rcp;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.net.IOdysseusNetStartupListener;
import de.uniol.inf.is.odysseus.net.IOdysseusNetStartupManager;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;

public class OdysseusNetStartupRCPListener implements IOdysseusNetStartupListener {

	private String oldTitle;
	
	@Override
	public void odysseusNetStarted(IOdysseusNetStartupManager sender, IOdysseusNode localNode) {
		setWindowTitle(determineTitleSuffix(), false);
	}

	@Override
	public void odysseusNetStopped(IOdysseusNetStartupManager sender) {
		setWindowTitle(oldTitle, true);
	}

	private static String determineTitleSuffix() {
		StringBuilder sb = new StringBuilder();
		sb.append( determineLocalNodeGroup() );
		sb.append( ":" );
		sb.append( determineLocalNodeName() );
		return sb.toString(); 
	}
	
	private static String determineLocalNodeName() {
		try {
			return OdysseusNetRCPPlugIn.getOdysseusNodeManager().getLocalNode().getName();
		} catch (OdysseusNetException e) {
			return "<no name>";
		}
	}

	private static Object determineLocalNodeGroup() {
		try {
			IOdysseusNode localNode = OdysseusNetRCPPlugIn.getOdysseusNodeManager().getLocalNode();
			Optional<String> optNodeGroup = localNode.getProperty("nodeGroup");
			return optNodeGroup.isPresent() ? optNodeGroup.get() : "<no group>" ;
		} catch (OdysseusNetException e) {
			return "<no group>";
		}
	}

	public void setWindowTitle(final String title, boolean replace) {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		workbench.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
				if (window != null) {
					if( !replace ) {
						oldTitle = window.getShell().getText();
						window.getShell().setText( oldTitle + " - " + title);
					} else {
						window.getShell().setText( title );
					}
				}
			}
		});
	}
}
