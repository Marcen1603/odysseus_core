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

import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.window.Window;
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
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartUtil;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartQueryTextProvider;
import de.uniol.inf.is.odysseus.rcp.dashboard.queryprovider.GraphQueryFileProvider;
import de.uniol.inf.is.odysseus.rcp.dashboard.queryprovider.ResourceFileQueryTextProvider;
import de.uniol.inf.is.odysseus.rcp.dashboard.queryprovider.SimpleQueryTextProvider;

public class QueryFileSelectionPage extends WizardPage {

	private final ContainerSelectionPage page1;

	private Button chooseSourceRadio;
	private Button chooseQueryRadio;
	private Combo sourceCombo;
	
	private Button chooseFileButton;
	private IFile selectedFile;

	protected QueryFileSelectionPage(String pageName, ContainerSelectionPage page1) {
		super(pageName);
		this.page1 = page1;

		setTitle("Choose query");
		setDescription("Choose the query to execute to get the data.");
	}

	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite rootComposite = new Composite(parent, SWT.NONE);
		rootComposite.setLayoutData(new GridData((GridData.FILL_BOTH)));
		rootComposite.setLayout(new GridLayout(1, true));
		
		createChooseQueryControls(rootComposite);
		createChooseSourceControls(rootComposite);

		finishCreation(rootComposite);
	}

	private void createChooseQueryControls(Composite rootComposite) {
		createChooseQueryRadioButton(rootComposite);
		createQueryFilesTable(rootComposite);
	}

	private void createQueryFilesTable(Composite rootComposite) {
		Composite tableComposite = new Composite(rootComposite, SWT.NONE);
		tableComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		tableComposite.setLayout(new GridLayout(3, false));

		Label label = new Label(tableComposite, SWT.NONE);
		label.setText("File");
		final Text inputFile = new Text(tableComposite, SWT.BORDER | SWT.READ_ONLY );
		inputFile.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		chooseFileButton = new Button(tableComposite, SWT.PUSH);
		chooseFileButton.setToolTipText("Choose file");
		chooseFileButton.setImage(DashboardPlugIn.getImageManager().get("chooseFile"));
		chooseFileButton.addSelectionListener(new SelectionAdapter() {
		
			@Override
			public void widgetSelected(SelectionEvent e) {
				IPath path = page1.getContainerFullPath();
				IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(path);
				IProject project = resource.getProject();
				
				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), new WorkbenchLabelProvider(), new WorkbenchContentProvider());
				dialog.setInput(project);
				dialog.setAllowMultiple(false);
				if (dialog.open() == Window.OK) {
					IResource selectedResource = (IResource) dialog.getFirstResult();					
					if (selectedResource != null && selectedResource instanceof IFile ) {
						selectedFile = (IFile)selectedResource;
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
		chooseQueryRadio = DashboardPartUtil.createRadioButton(rootComposite, "Use query");
		chooseQueryRadio.setSelection(true);

		chooseQueryRadio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				chooseFileButton.setEnabled(chooseQueryRadio.getSelection());
			}
		});
	}

	private void createChooseSourceControls(Composite rootComposite) {
		chooseSourceRadio = DashboardPartUtil.createRadioButton(rootComposite, "Use source");
		
		String[] availableSources = determineAvailableSources();
		if( availableSources.length > 0 ) {
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
		List<ViewInformation> streamsAndViews = DashboardPlugIn.getExecutor().getStreamsAndViewsInformation(OdysseusRCPPlugIn.getActiveSession());
		List<String> names = Lists.newArrayList();
		for( ViewInformation streamOrView : streamsAndViews ) {
			// FIXME: Use Resource
			names.add(getPlainSourceName(streamOrView.getName().toString()));
		}
		Collections.sort(names);
		return names.toArray(new String[names.size()]);
	}

	private static String getPlainSourceName(String fullSourcename) {
		int pos = fullSourcename.indexOf(".");
		if( pos != -1 ) {
			return fullSourcename.substring(pos+1);
		}
		return fullSourcename;
	}

	private IFile getSelectedQueryFile() {
		if( chooseQueryRadio.getSelection() ) {
			return selectedFile;
		} 
		
		throw new RuntimeException("Query file was not selected here");
	}
	
	private String getSelectedSourceName() {
		if( chooseSourceRadio.getSelection() ) {
			return sourceCombo.getText();
		}
		
		throw new RuntimeException("There was no source selected here");
	}
	
	private boolean isQueryFileSelected() {
		return chooseQueryRadio.getSelection();
	}
	
	public IDashboardPartQueryTextProvider getQueryTextProvider() {
		if( isQueryFileSelected() ) {
			if( getSelectedQueryFile().getFileExtension().equalsIgnoreCase("grp")) {
				return new GraphQueryFileProvider(getSelectedQueryFile());
			} 
			return new ResourceFileQueryTextProvider(getSelectedQueryFile());
		}
		
		List<String> sourceSelectAllText = Lists.newArrayList();
		sourceSelectAllText.add("#PARSER CQL");
		sourceSelectAllText.add("#TRANSCFG Standard");
		sourceSelectAllText.add("#RUNQUERY");
		sourceSelectAllText.add("SELECT * FROM " + getSelectedSourceName());
		
		return new SimpleQueryTextProvider(sourceSelectAllText);
	}

	private void finishCreation(Composite rootComposite) {
		setErrorMessage(null);
		setMessage(null);
		setControl(rootComposite);
		setPageComplete(false);
	}
}
