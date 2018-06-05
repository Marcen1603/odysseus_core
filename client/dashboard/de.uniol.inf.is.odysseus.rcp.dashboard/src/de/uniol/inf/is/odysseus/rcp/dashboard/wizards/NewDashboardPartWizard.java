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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.handler.XMLDashboardPartHandler;

//@formatter:off
/**
 * A wizard to create a new DashboardPart. The wizard works as follows: 
 * 0. select the file where you want to save the DashboardPart
 * 1. select the type of the DashboardPart 
 * ---> optional loop begin 
 * 2. select a source / query
 * 3. add context (always the same page)
 * <--- end optional loop
 * 4. configure the DashboardPart 
 * 
 * @author original author: Timo Michelsen; extended and commented by Tobias
 *         Brandt
 *
 */
//@formatter:on
public class NewDashboardPartWizard extends Wizard implements INewWizard {

	private static final Logger LOG = LoggerFactory.getLogger(NewDashboardPartWizard.class);
	private static final String DEFAULT_DASHBOARD_FILENAME = "DashboardPart."
			+ DashboardPlugIn.DASHBOARD_PART_EXTENSION;

	private DashboardPartTypeSelectionPage partSelectionPage;
	private ContainerSelectionPage containerPage;
	private DashboardPartConfigurationPage configurePage;
	private QueryFileSelectionPage queryFilePage;
	private ContextMapPage contextMapPage;

	public NewDashboardPartWizard() {
		super();
		setWindowTitle("New DashboardPart");
	}

	@Override
	public void addPages() {
		addPage(containerPage);
		addPage(partSelectionPage);
		addPage(queryFilePage);
		addPage(contextMapPage);
		addPage(configurePage);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		containerPage = new ContainerSelectionPage("Select file name", selection, DEFAULT_DASHBOARD_FILENAME);
		partSelectionPage = new DashboardPartTypeSelectionPage("Select type of dashboad part");
		contextMapPage = new ContextMapPage("Configure context", containerPage, partSelectionPage, 1);
		queryFilePage = new QueryFileSelectionPage("Select query", containerPage, partSelectionPage, contextMapPage, 1);
		configurePage = new DashboardPartConfigurationPage("Configure Dashboard Part", partSelectionPage,
				contextMapPage, containerPage);

		// Some pages need other pages that can only be created after them. To
		// avoid NullPointers, we add them after they were created.
		contextMapPage.setQueryFilePage(queryFilePage);
		contextMapPage.setConfigurationPage(configurePage);
		queryFilePage.setConfigurationPage(configurePage);
	}

	@Override
	public boolean performFinish() {
		try {
			final String dashboardPartFileName = getDashboardPartFileName(containerPage);

			final IPath path = containerPage.getContainerFullPath().append(dashboardPartFileName);
			final IFile dashboardPartFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			dashboardPartFile.create(null, true, null);

			final IDashboardPart part = configurePage.getDashboardPart();
			part.setQueryTextProvider(queryFilePage.getQueryTextProvider());

			final IDashboardPartHandler handler = new XMLDashboardPartHandler();
			handler.save(part, dashboardPartFile);

			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.openEditor(new FileEditorInput(dashboardPartFile), DashboardPlugIn.DASHBOARD_PART_EDITOR_ID, true);

			return true;
		} catch (final CancelException ex) {
			return false;
		} catch (final Exception ex) {
			LOG.error("Could not finish wizard", ex);
			return false;
		}
	}

	/**
	 * The wizard can add multiple sources for some Dashboard parts. When the
	 * user does not want to add more sources, the next page after the loop is
	 * the configuration page. To be able to show this page, it can be retrieved
	 * here.
	 * 
	 * @return The configuration page for the DashboardPart
	 */
	public IWizardPage getConfigurePage() {
		return this.configurePage;
	}

	private static String getDashboardPartFileName(ContainerSelectionPage containerPage) throws CancelException {
		String queryFileName = containerPage.getFileName();

		final Optional<String> optionalExtension = getFileExtension(queryFileName);
		if (!optionalExtension.isPresent()) {
			queryFileName = queryFileName + "." + DashboardPlugIn.DASHBOARD_PART_EXTENSION;
		} else {
			final String extension = optionalExtension.get();
			if (!extension.equals(DashboardPlugIn.DASHBOARD_PART_EXTENSION)) {

				if (!isOtherExtensionOk(extension, DashboardPlugIn.DASHBOARD_PART_EXTENSION)) {
					queryFileName = queryFileName.replace(extension, DashboardPlugIn.DASHBOARD_PART_EXTENSION);
				}
			}
		}
		return queryFileName;
	}

	private static Optional<String> getFileExtension(String fileName) {
		final int lastPoint = fileName.lastIndexOf(".");
		if (lastPoint != -1) {
			return Optional.of(fileName.substring(lastPoint + 1));
		}
		return Optional.absent();
	}

	private static boolean isOtherExtensionOk(String desiredExtension, String standardExtension)
			throws CancelException {
		final MessageDialog dlg = new MessageDialog(Display.getCurrent().getActiveShell(), "Custom file extension",
				null,
				"Should the file extension '" + desiredExtension + "' be replaced by '" + standardExtension + "'?",
				MessageDialog.QUESTION, new String[] { "Replace", "Keep", "Cancel" }, 0);
		final int ret = dlg.open();
		if (ret == 1) {
			return true;
		} else if (ret == 0) {
			return false;
		}
		throw new CancelException();
	}
}

@SuppressWarnings("serial")
class CancelException extends Exception {
};
