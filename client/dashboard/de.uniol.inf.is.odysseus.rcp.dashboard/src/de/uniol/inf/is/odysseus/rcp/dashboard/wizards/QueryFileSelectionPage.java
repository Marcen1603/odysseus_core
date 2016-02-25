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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.planmanagement.ViewInformation;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractMultiSourceDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartUtil;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartQueryTextProvider;
import de.uniol.inf.is.odysseus.rcp.dashboard.queryprovider.GraphQueryFileProvider;
import de.uniol.inf.is.odysseus.rcp.dashboard.queryprovider.ResourceFileQueryTextProvider;
import de.uniol.inf.is.odysseus.rcp.dashboard.queryprovider.RunningQueryProvider;
import de.uniol.inf.is.odysseus.rcp.dashboard.queryprovider.SimpleQueryTextProvider;

public class QueryFileSelectionPage extends WizardPage {

	private final DashboardPartTypeSelectionPage typeSelectionPage;
	private final ContainerSelectionPage containerSelectionPage;
	private DashboardPartConfigurationPage configurationPage;

	private Button chooseSourceRadio;
	private Button chooseQueryFileRadio;
	private Button chooseQueryRadio;

	private Button addAnotherSourceCheckbox;

	private Combo sourceCombo;
	private Combo queryCombo;

	private Button chooseFileButton;
	private IFile selectedFile;

	private Composite rootComposite;

	// To show the source number in the explanation text
	private int sourceNumber;

	protected QueryFileSelectionPage(String pageName, ContainerSelectionPage containerSelectionPage,
			DashboardPartTypeSelectionPage typeSelectionPage, NewDashboardPartWizard wizard,
			DashboardPartConfigurationPage configurationPage, int sourceNumber) {
		super(pageName);
		this.containerSelectionPage = containerSelectionPage;
		this.typeSelectionPage = typeSelectionPage;
		this.configurationPage = configurationPage;
		this.sourceNumber = sourceNumber;

		setTitle("Choose " + sourceNumber + ". query");
		setDescription("Choose the " + sourceNumber + ". query to execute to get the data.");
	}

	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		rootComposite = new Composite(parent, SWT.NONE);
		rootComposite.setLayoutData(new GridData((GridData.FILL_BOTH)));
		rootComposite.setLayout(new GridLayout(1, true));

		createChooseQueryFileControls(rootComposite);
		createChooseSourceControls(rootComposite);
		createChooseQueryControls(rootComposite);

		// Create a checkBox that is shown if the DashboadPart can handle
		// multiple sources
		addAnotherSourceCheckbox = new Button(rootComposite, SWT.CHECK);
		addAnotherSourceCheckbox.setText("Add another source.");
		addAnotherSourceCheckbox.setSelection(false);
		addAnotherSourceCheckbox.setVisible(false);

		finishCreation(rootComposite);
	}

	private void createChooseQueryFileControls(Composite rootComposite) {
		createChooseQueryRadioButton(rootComposite);
		createQueryFilesTable(rootComposite);
	}

	public void setConfigurationPage(DashboardPartConfigurationPage page) {
		this.configurationPage = page;
	}

	private void createQueryFilesTable(Composite rootComposite) {
		Composite tableComposite = new Composite(rootComposite, SWT.NONE);
		tableComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		tableComposite.setLayout(new GridLayout(3, false));

		Label label = new Label(tableComposite, SWT.NONE);
		label.setText("File");
		final Text inputFile = new Text(tableComposite, SWT.BORDER | SWT.READ_ONLY);
		inputFile.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		chooseFileButton = new Button(tableComposite, SWT.PUSH);
		chooseFileButton.setToolTipText("Choose file");
		chooseFileButton.setImage(DashboardPlugIn.getImageManager().get("chooseFile"));
		chooseFileButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IProject project = containerSelectionPage.getProject();

				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), new WorkbenchLabelProvider(),
						new WorkbenchContentProvider());
				dialog.setInput(project);
				dialog.setAllowMultiple(false);
				if (dialog.open() == Window.OK) {
					IResource selectedResource = (IResource) dialog.getFirstResult();
					if (selectedResource != null && selectedResource instanceof IFile) {
						selectedFile = (IFile) selectedResource;
						inputFile.setText(selectedFile.getFullPath().toString());
						setPageComplete(true);
					} else {
						setPageComplete(false);
						selectedFile = null;
						inputFile.setText("");
					}
				}
			}

		});

	}

	private void createChooseQueryRadioButton(Composite rootComposite) {
		chooseQueryFileRadio = DashboardPartUtil.createRadioButton(rootComposite, "Use query file");
		chooseQueryFileRadio.setSelection(true);

		chooseQueryFileRadio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				chooseFileButton.setEnabled(chooseQueryFileRadio.getSelection());
			}
		});
	}

	private void createChooseSourceControls(Composite rootComposite) {
		chooseSourceRadio = DashboardPartUtil.createRadioButton(rootComposite, "Use source");

		String[] availableSources = determineAvailableSources();
		if (availableSources.length > 0) {
			sourceCombo = DashboardPartUtil.createCombo(rootComposite, availableSources, availableSources[0]);

			chooseSourceRadio.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					sourceCombo.setEnabled(chooseSourceRadio.getSelection());
					setPageComplete(chooseSourceRadio.getSelection());
				}
			});
		} else {
			sourceCombo = DashboardPartUtil.createCombo(rootComposite, null);
			chooseSourceRadio.setEnabled(false);
		}
		sourceCombo.setEnabled(false);
	}

	private static String[] determineAvailableSources() {
		List<ViewInformation> streamsAndViews = DashboardPlugIn.getExecutor()
				.getStreamsAndViewsInformation(OdysseusRCPPlugIn.getActiveSession());
		List<String> names = Lists.newArrayList();
		for (ViewInformation streamOrView : streamsAndViews) {
			// FIXME: Use Resource
			names.add(getPlainSourceName(streamOrView.getName().toString()));
		}
		Collections.sort(names);
		return names.toArray(new String[names.size()]);
	}

	private static String getPlainSourceName(String fullSourcename) {
		int pos = fullSourcename.indexOf(".");
		if (pos != -1) {
			return fullSourcename.substring(pos + 1);
		}
		return fullSourcename;
	}

	private void createChooseQueryControls(Composite rootComposite) {
		chooseQueryRadio = DashboardPartUtil.createRadioButton(rootComposite, "Use running query");

		String[] availableQueries = determineAvailableQueries();
		if (availableQueries.length > 0) {
			queryCombo = DashboardPartUtil.createCombo(rootComposite, availableQueries, availableQueries[0]);

			chooseQueryRadio.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					queryCombo.setEnabled(chooseQueryRadio.getSelection());
					setPageComplete(chooseQueryRadio.getSelection());
				}
			});
		} else {
			queryCombo = DashboardPartUtil.createCombo(rootComposite, null);
			chooseQueryRadio.setEnabled(false);
		}
	}

	private static String[] determineAvailableQueries() {
		IExecutor executor = DashboardPlugIn.getExecutor();
		ISession session = OdysseusRCPPlugIn.getActiveSession();
		Collection<Integer> queryIds = executor.getLogicalQueryIds(session);

		List<String> queryNames = Lists.newArrayList();
		for (Integer queryId : queryIds) {
			ILogicalQuery query = executor.getLogicalQueryById(queryId, session);
			queryNames.add(query.getName());
		}

		return queryNames.toArray(new String[queryNames.size()]);
	}

	public IDashboardPartQueryTextProvider getQueryTextProvider() {
		if (isQueryFileSelected()) {
			if (getSelectedQueryFile().getFileExtension().equalsIgnoreCase("grp")) {
				return new GraphQueryFileProvider(getSelectedQueryFile());
			}
			return new ResourceFileQueryTextProvider(getSelectedQueryFile());
		}

		if (isSourceSelected()) {
			List<String> sourceSelectAllText = Lists.newArrayList();
			sourceSelectAllText.add("#PARSER CQL");
			sourceSelectAllText.add("#TRANSCFG Standard");
			sourceSelectAllText.add("#RUNQUERY");
			sourceSelectAllText.add("SELECT * FROM " + getSelectedSourceName());

			return new SimpleQueryTextProvider(sourceSelectAllText);
		}

		if (isQuerySelected()) {
			return new RunningQueryProvider(getSelectedQueryName());
		}

		throw new RuntimeException("Could not determine query text provider!");
	}

	private boolean isQueryFileSelected() {
		return chooseQueryFileRadio.getSelection();
	}

	private boolean isSourceSelected() {
		return chooseSourceRadio.getSelection();
	}

	private boolean isQuerySelected() {
		return chooseQueryRadio.getSelection();
	}

	private IFile getSelectedQueryFile() {
		if (chooseQueryFileRadio.getSelection()) {
			return selectedFile;
		}

		throw new RuntimeException("Query file was not selected here");
	}

	private String getSelectedSourceName() {
		if (chooseSourceRadio.getSelection()) {
			return sourceCombo.getText();
		}

		throw new RuntimeException("There was no source selected here");
	}

	private String getSelectedQueryName() {
		if (chooseQueryRadio.getSelection()) {
			return queryCombo.getText();
		}

		throw new RuntimeException("There was no query name selected here");
	}

	private void finishCreation(Composite rootComposite) {
		setErrorMessage(null);
		setMessage(null);
		setControl(rootComposite);
		setPageComplete(false);
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);

		if (visible) {
			showMultipleSourceCheckbox();
		}
	}

	/**
	 * If necessary (if multiple sources are possible), an option for the user
	 * is made visible where it is possible to choose to add another source.
	 */
	private void showMultipleSourceCheckbox() {
		if (typeSelectionPage.getSelectedDashboardPart() instanceof AbstractMultiSourceDashboardPart) {
			addAnotherSourceCheckbox.setVisible(true);
		} else {
			addAnotherSourceCheckbox.setVisible(false);
		}
	}

	/**
	 * Returns the next page of the wizard: If user wants to choose another
	 * source, another source selection page will be shown. If not, the wizard
	 * continues as normal.
	 * 
	 * Override is necessary to give the user the possibility to choose more
	 * than one source.
	 */
	@Override
	public IWizardPage getNextPage() {

		if (addAnotherSourceCheckbox.getSelection()) {
			// The user input is such that we need an additional page to
			// append to the wizard.
			IWizardPage nextPage = new QueryFileSelectionPage("Select query", containerSelectionPage, typeSelectionPage,
					(NewDashboardPartWizard) this.getWizard(), configurationPage, sourceNumber + 1);
			nextPage.setWizard(this.getWizard());
			configurationPage.addQuerySelectionPage((QueryFileSelectionPage) nextPage);

			return nextPage;

		} else {
			IWizardPage nextPage = this.getWizard()
					.getNextPage(((NewDashboardPartWizard) this.getWizard()).getFirstQuerySelectionPage());
			return nextPage;
		}

	}

}
