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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableCollection;

import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartRegistry;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartConfigurer;
import de.uniol.inf.is.odysseus.rcp.dashboard.controller.QueryExecutionHandler;

public class DashboardPartTypeSelectionPage extends WizardPage {

	private static final Logger LOG = LoggerFactory.getLogger(DashboardPartTypeSelectionPage.class);
	private static final InfoService INFO = InfoServiceFactory
			.getInfoService(DashboardPartTypeSelectionPage.class);
	
	private final List<String> dashboardPartNames;
	private final QueryFileSelectionPage queryFilePage;
	private final ContextMapPage contextMapPage;
	private final ContainerSelectionPage containerPage;

	private Combo choosePartNameCombo;

	private Composite configComposite;
	private Composite rootComposite;
	private IDashboardPart selectedDashboardPart;
	private int selectedIndex = -1;

	private IDashboardPartConfigurer<IDashboardPart> selectedConfigurer;
	private QueryExecutionHandler handler;
	
	public DashboardPartTypeSelectionPage(String pageName, QueryFileSelectionPage queryFilePage, ContextMapPage contextMapPage, ContainerSelectionPage containerPage) {
		super(pageName);

		setTitle("Choose type of DashboardPart");
		setDescription("Choose one type of DashboardPart and configure it.");

		dashboardPartNames = determineDashboardPartNames();
		this.queryFilePage = queryFilePage;
		this.contextMapPage = contextMapPage;
		this.containerPage = containerPage;
	}

	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		rootComposite = new Composite(parent, SWT.NONE);
		rootComposite.setLayoutData(new GridData((GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL)));
		rootComposite.setLayout(new GridLayout(1, true));

		final Composite choosePartNameComposite = new Composite(rootComposite, SWT.NONE);
		choosePartNameComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		choosePartNameComposite.setLayout(new GridLayout(2, false));

		final Label choosePartNameLabel = new Label(choosePartNameComposite, SWT.NONE);
		choosePartNameLabel.setText("Type");

		choosePartNameCombo = new Combo(choosePartNameComposite, SWT.BORDER | SWT.READ_ONLY);
		choosePartNameCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		choosePartNameCombo.setItems(dashboardPartNames.toArray(new String[0]));
		choosePartNameCombo.setText("");
		choosePartNameCombo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				selectDashboardPart(choosePartNameCombo.getSelectionIndex());
			}
		});

		finishCreation(rootComposite);
	}
	
	private void startQueryIfNeeded() {
		if( handler == null && selectedDashboardPart != null ) {
			handler = new QueryExecutionHandler(selectedDashboardPart);
		}
		
		try {
			if( !handler.isStarted() ) {
				handler.start();
			}
			
			setPageComplete(true);
		} catch (Exception ex) {
			INFO.error("Could not execute query", ex);
			LOG.error("Could not execute query", ex);
			setErrorMessage("Selected Odysseus Script has some errors. See log for details.");
		}
		
	}
	
	@Override
	public void dispose() {
		stopQueryExecution();
		
		disposeConfigurer();
		super.dispose();
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
		
		if( visible != true ) {
			stopQueryExecution();
		}
	}

	private void stopQueryExecution() {
		if( handler != null ) {
			handler.stop();
		}
	}

	private String getDashboardPartName(int index) {
		return dashboardPartNames.get(index);
	}

	@SuppressWarnings("unchecked")
	private void selectDashboardPart(int index) {
		if( selectedIndex == index ) {
			return;
		}
		
		choosePartNameCombo.select(index);
		selectedIndex = index;
		
		disposeConfigurer();
		
		if( selectedDashboardPart != null ) {
			selectedDashboardPart.dispose();
		}
		
		if( configComposite != null ) {
			configComposite.dispose();
		} 
		
		final String dashboardPartName = getDashboardPartName(index);

		configComposite = new Composite(rootComposite, SWT.NONE);
		configComposite.setLayout(new GridLayout(1, true));
		configComposite.setLayoutData( new GridData(GridData.FILL_BOTH));
		
		try {
			final IDashboardPart newDashboardPart = DashboardPartRegistry.createDashboardPart(dashboardPartName);
			newDashboardPart.setProject(containerPage.getProject());
			newDashboardPart.setQueryTextProvider(queryFilePage.getQueryTextProvider());
			insertInto(newDashboardPart, contextMapPage.getContextMap());
			selectedDashboardPart = newDashboardPart;
			
			startQueryIfNeeded();
			
			selectedConfigurer = (IDashboardPartConfigurer<IDashboardPart>) DashboardPartRegistry.createDashboardPartConfigurer(dashboardPartName);
			selectedConfigurer.init(newDashboardPart, handler.getRoots());
			
			selectedConfigurer.createPartControl(configComposite);
			
			rootComposite.layout();
		} catch (Exception e) {
			LOG.error("Could not create DashboardPart", e);
			INFO.error("Could not create DashboardPart", e);
		}
	}
	
	private static void insertInto(IDashboardPart part, Map<String, String> contextMap) {
		ImmutableCollection<String> keys = part.getContextKeys();
		for( String key : keys ) {
			part.removeContext(key);
		}
		
		for( String entry : contextMap.keySet()) {
			part.addContext(entry, contextMap.get(entry));
		}
	}

	private void disposeConfigurer() {
		if( selectedConfigurer != null ) {
			selectedConfigurer.dispose();
		}
	}

	private static List<String> determineDashboardPartNames() {
		List<String> names =  new ArrayList<String>(DashboardPartRegistry.getDashboardPartNames());
		Collections.sort(names);
		return names;
	}

	public IDashboardPart getDashboardPart() {
		return selectedDashboardPart;
	}
}
