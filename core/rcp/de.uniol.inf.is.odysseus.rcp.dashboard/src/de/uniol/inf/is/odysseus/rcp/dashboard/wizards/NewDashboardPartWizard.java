/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard.wizards;

import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.rcp.dashboard.Configuration;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartRegistry;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartQueryTextProvider;
import de.uniol.inf.is.odysseus.rcp.dashboard.handler.XMLDashboardPartHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.queryprovider.ResourceFileQueryTextProvider;
import de.uniol.inf.is.odysseus.rcp.dashboard.queryprovider.SimpleQueryTextProvider;
import de.uniol.inf.is.odysseus.rcp.dashboard.util.FileUtil;

public class NewDashboardPartWizard extends Wizard implements INewWizard {

	private static final Logger LOG = LoggerFactory.getLogger(NewDashboardPartWizard.class);
	private static final String DEFAULT_DASHBOARD_FILENAME = "DashboardPart." + DashboardPlugIn.DASHBOARD_PART_EXTENSION;
	
	private ContainerSelectionPage containerPage;
	private DashboardPartTypeSelectionPage partTypePage;
	private QueryFileSelectionPage queryFilePage;
	
	public NewDashboardPartWizard() {
		super();
		setWindowTitle("New DashboardPart");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		containerPage = new ContainerSelectionPage("Select file name", selection, DEFAULT_DASHBOARD_FILENAME);
		partTypePage = new DashboardPartTypeSelectionPage("Select type of Dashboard Part");
		queryFilePage = new QueryFileSelectionPage("Select query", containerPage);
	}
	
	@Override
	public void addPages() {
		addPage(containerPage);
		addPage(partTypePage);
		addPage(queryFilePage);
	}
	
	@Override
	public boolean performFinish() {
		try {
			String dashboardPartFileName = getDashboardPartFileName(containerPage);

			IPath path = containerPage.getContainerFullPath().append(dashboardPartFileName);
			IFile dashboardPartFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			dashboardPartFile.create(null, IResource.NONE, null);
			
			IDashboardPart part = DashboardPartRegistry.createDashboardPart(partTypePage.getSelectedDashboardPartName());
			Configuration defaultConfiguration = part.getConfiguration();
			Map<String, String> settings = partTypePage.getSelectedSettings();
			for( String key : settings.keySet() ) {
				defaultConfiguration.setAsString(key, settings.get(key));
			}			
			part.setQueryTextProvider(createQueryTextProvider(queryFilePage.isQueryFileCopy(), queryFilePage.getQueryFile()));
			
			IDashboardPartHandler handler = new XMLDashboardPartHandler();
			List<String> lines = handler.save(part);
			FileUtil.write(lines, dashboardPartFile);
			
			return true;
		} catch (CancelException ex) {
			return false;
		} catch (Exception ex) {
			LOG.error("Could not finish wizard", ex);
			return false;
		}
	}

	private static String getDashboardPartFileName(ContainerSelectionPage containerPage) throws CancelException {
		String queryFileName = containerPage.getFileName();

		Optional<String> optionalExtension = getFileExtension(queryFileName);
		if (!optionalExtension.isPresent()) {
			queryFileName = queryFileName + "." + DashboardPlugIn.DASHBOARD_PART_EXTENSION;
		} else {
			String extension = optionalExtension.get();
			if (!extension.equals(DashboardPlugIn.DASHBOARD_PART_EXTENSION)) {

				if (!isOtherExtensionOk(extension, DashboardPlugIn.DASHBOARD_PART_EXTENSION)) {
					queryFileName = queryFileName.replace(extension, DashboardPlugIn.DASHBOARD_PART_EXTENSION);
				}
			}
		}
		return queryFileName;
	}

	private static boolean isOtherExtensionOk(String desiredExtension, String standardExtension) throws CancelException {
		MessageDialog dlg = new MessageDialog(Display.getCurrent().getActiveShell(), "Custom file extension", null, "Should the file extension '" + desiredExtension + "' be replaced by '" + standardExtension + "'?", MessageDialog.QUESTION, new String[] { "Replace", "Keep", "Cancel" }, 0);
		int ret = dlg.open();
		if (ret == 1) {
			return true;
		} else if (ret == 0) {
			return false;
		}
		throw new CancelException();
	}

	private static Optional<String> getFileExtension(String fileName) {
		int lastPoint = fileName.lastIndexOf(".");
		if (lastPoint != -1) {
			return Optional.of(fileName.substring(lastPoint + 1));
		}
		return Optional.absent();
	}

	private static IDashboardPartQueryTextProvider createQueryTextProvider(boolean queryFileCopy, IFile queryFile) {
		if( queryFileCopy ) {
			return new SimpleQueryTextProvider(queryFile);
		} else {
			return new ResourceFileQueryTextProvider(queryFile);
		}
	}
}

@SuppressWarnings("serial")
class CancelException extends Exception {
};
