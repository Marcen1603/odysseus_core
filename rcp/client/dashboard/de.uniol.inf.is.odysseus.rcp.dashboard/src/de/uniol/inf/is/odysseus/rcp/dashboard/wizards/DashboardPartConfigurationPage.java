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
import java.util.List;
import java.util.Map;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableCollection;

import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractMultiSourceDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartRegistry;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartConfigurer;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartQueryTextProvider;
import de.uniol.inf.is.odysseus.rcp.dashboard.controller.QueryExecutionHandler;

public class DashboardPartConfigurationPage extends WizardPage {

	private static final Logger LOG = LoggerFactory.getLogger(DashboardPartConfigurationPage.class);
	private static final InfoService INFO = InfoServiceFactory.getInfoService(DashboardPartConfigurationPage.class);

	private final DashboardPartTypeSelectionPage selectionPage;
	private final ContextMapPage contextMapPage;
	private final ContainerSelectionPage containerPage;

	private List<IDashboardPartQueryTextProvider> queryTextProviders;
	private List<QueryFileSelectionPage> querySelectionPages;

	private Composite configComposite;
	private Composite rootComposite;
	private IDashboardPart selectedDashboardPart;

	private IDashboardPartConfigurer<IDashboardPart> selectedConfigurer;
	private QueryExecutionHandler handler;

	public DashboardPartConfigurationPage(String pageName, DashboardPartTypeSelectionPage selectionPage,
			ContextMapPage contextMapPage, ContainerSelectionPage containerPage) {
		super(pageName);

		setTitle("Configure DashboardPart");
		setDescription("Configure the DashboardPart.");

		this.selectionPage = selectionPage;
		this.contextMapPage = contextMapPage;
		this.containerPage = containerPage;

		queryTextProviders = new ArrayList<>();
		querySelectionPages = new ArrayList<>();
	}

	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		rootComposite = new Composite(parent, SWT.NONE);
		rootComposite.setLayoutData(new GridData((GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL)));
		rootComposite.setLayout(new GridLayout(1, true));

		finishCreation(rootComposite);
	}

	private void startQueryIfNeeded() {
		if (handler == null && selectedDashboardPart != null) {
			handler = new QueryExecutionHandler(selectedDashboardPart);
		}

		try {
			if (!handler.isStarted()) {
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

		if (visible != true) {
			stopQueryExecution();
		} else {
			selectDashboardPart(selectionPage.getSelectedDashboardPartName());
		}
	}

	private void stopQueryExecution() {
		if (handler != null) {
			handler.stop();
		}
	}

	@SuppressWarnings("unchecked")
	private void selectDashboardPart(String partName) {

		// Get queryTextProviders from all source selection pages
		for (QueryFileSelectionPage page : querySelectionPages) {
			queryTextProviders.add(page.getQueryTextProvider());
		}

		disposeConfigurer();

		if (selectedDashboardPart != null) {
			selectedDashboardPart.dispose();
		}

		if (configComposite != null) {
			configComposite.dispose();
		}

		configComposite = new Composite(rootComposite, SWT.NONE);
		configComposite.setLayout(new GridLayout(1, true));
		configComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		try {
			final IDashboardPart newDashboardPart = DashboardPartRegistry.createDashboardPart(partName);

			if (queryTextProviders.size() > 1) {
				// We have a DashboadPart with more than one source (e.g. a map)
				if (newDashboardPart instanceof AbstractMultiSourceDashboardPart) {
					// Yes, and we can use them
					for (IDashboardPartQueryTextProvider provider : queryTextProviders) {
						((AbstractMultiSourceDashboardPart) newDashboardPart).addQueryTextProvider(provider);
					}
				}
			} else {
				// We have only one
				newDashboardPart.setProject(containerPage.getProject());
				if (queryTextProviders.size() > 0)
					newDashboardPart.setQueryTextProvider(queryTextProviders.get(0));
			}

			insertInto(newDashboardPart, contextMapPage.getContextMap());
			selectedDashboardPart = newDashboardPart;

			startQueryIfNeeded();

			selectedConfigurer = (IDashboardPartConfigurer<IDashboardPart>) DashboardPartRegistry
					.createDashboardPartConfigurer(partName);
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
		for (String key : keys) {
			part.removeContext(key);
		}

		for (String entry : contextMap.keySet()) {
			part.addContext(entry, contextMap.get(entry));
		}
	}

	private void disposeConfigurer() {
		if (selectedConfigurer != null) {
			selectedConfigurer.dispose();
		}
	}

	/**
	 * The {@link DashboardPartConfigurationPage} needs to know all
	 * querySelectionPages to get the queryTextProviders. Here you can add a
	 * selectionPage to the list. Only add the page if a query was selected, not
	 * when it was only created.
	 * 
	 * @param page
	 *            The querySelectionPage to add
	 */
	public void addQuerySelectionPage(QueryFileSelectionPage page) {
		this.querySelectionPages.add(page);
	}

	public IDashboardPart getDashboardPart() {
		return selectedDashboardPart;
	}
}
