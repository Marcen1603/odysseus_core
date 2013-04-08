/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.rcp.views.query;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.google.common.base.Preconditions;

public class QueryView extends ViewPart {

	private QueryTableViewer tableViewer;
	private final Collection<IQueryViewData> queries = new ArrayList<IQueryViewData>();
	private IQueryViewDataProvider dataProvider;

	@Override
	public void createPartControl(Composite parent) {

		final Composite tableComposite = new Composite(parent, SWT.NONE);
		final TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		tableViewer = new QueryTableViewer(tableComposite, SWT.MULTI | SWT.FULL_SELECTION);
		dataProvider = determineDataProvider(this);
		refreshData(dataProvider, queries);
		tableViewer.setInput(queries);
		getSite().setSelectionProvider(tableViewer);

		createContextMenu();
	}

	@Override
	public void dispose() {
		try {
			dataProvider.dispose();
			dataProvider = null;
		} catch (final Exception ex) {
			// ignore
		}

		super.dispose();
	}

	public QueryTableViewer getTableViewer() {
		return tableViewer;
	}

	public void refreshTable() {
		if (!PlatformUI.getWorkbench().getDisplay().isDisposed()) {
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					if (dataProvider != null) {
						refreshData(dataProvider, queries);

						if (!tableViewer.getControl().isDisposed()) {
							tableViewer.refresh();
						}
					}
				}

			});
		}
	}

	@Override
	public void setFocus() {
		tableViewer.getTable().setFocus();
	}

	private void createContextMenu() {
		// Contextmenu
		final MenuManager menuManager = new MenuManager();
		final Menu contextMenu = menuManager.createContextMenu(tableViewer.getTable());
		// Set the MenuManager
		tableViewer.getTable().setMenu(contextMenu);
		getSite().registerContextMenu(menuManager, tableViewer);
	}

	private static IQueryViewDataProvider determineDataProvider(QueryView view) {
		Preconditions.checkArgument(QueryViewDataProviderManager.hasQueryViewDataProvider(), "QueryView must have at least one data provider!");
		final IQueryViewDataProvider dataProvider = QueryViewDataProviderManager.getQueryViewDataProvider();
		dataProvider.init(view);
		return dataProvider;
	}

	private static void refreshData(IQueryViewDataProvider dataProvider, Collection<IQueryViewData> toRefresh) {
		toRefresh.clear();
		toRefresh.addAll(dataProvider.getData());
	}
}