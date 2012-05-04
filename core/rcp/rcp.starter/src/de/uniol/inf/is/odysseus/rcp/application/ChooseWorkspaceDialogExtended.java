package de.uniol.inf.is.odysseus.rcp.application;

import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.osgi.util.TextProcessor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.ide.ChooseWorkspaceData;
import org.eclipse.ui.internal.ide.ChooseWorkspaceDialog;

@SuppressWarnings("restriction")
public class ChooseWorkspaceDialogExtended extends ChooseWorkspaceDialog {

	private final ChooseWorkspaceData launchData;
	
	public ChooseWorkspaceDialogExtended(Shell parentShell, ChooseWorkspaceData launchData, boolean suppressAskAgain, boolean centerOnMonitor) {
		super(parentShell, launchData, suppressAskAgain, centerOnMonitor);
		
		this.launchData = launchData;
	}
	
	@Override
	protected void okPressed() {
		
		String workspaceSelection = TextProcessor.deprocess(getWorkspaceLocation());
		
		if (workspaceSelection != null) {
			launchData.workspaceSelected(workspaceSelection);
			launchData.writePersistedData();

			if( !releaseAndSetLocation(workspaceSelection) ) {
				setErrorMessage("Could not set workspace " + workspaceSelection + ".\nPlease choose a different one.");
				return;
			}
		}
		
		setReturnCode(OK);
		close();
	}

	private static boolean releaseAndSetLocation(String selection) {
		try {
			Location instanceLoc = Platform.getInstanceLocation();
			if (instanceLoc.isSet()) {
				instanceLoc.release();
			}
	
			return instanceLoc.set(new URL("file", null, selection), true);
		} catch( Exception ex ) {
			return false;
		}
	}
}
