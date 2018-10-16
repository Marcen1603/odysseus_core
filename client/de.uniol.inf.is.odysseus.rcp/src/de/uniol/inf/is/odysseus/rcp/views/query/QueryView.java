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

import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public final class QueryView extends ViewPart {

	private final List<IQueryViewData> data = Lists.newArrayList();
	
	private QueryTableViewer tableViewer;
	private boolean refreshing;
	private IQueryViewDataProvider dataProvider;

	@Override
	public void createPartControl(Composite parent) {

		final Composite tableComposite = new Composite(parent, SWT.NONE);
		final TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		tableViewer = new QueryTableViewer(tableComposite, SWT.MULTI | SWT.FULL_SELECTION);
		tableViewer.setInput(data);
		getSite().setSelectionProvider(tableViewer);

		createContextMenu();
		
		dataProvider = determineDataProvider();
		dataProvider.init(this);
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
	
	public void refreshData( int id ) {
		// Preconditions.checkArgument(id >= 0, "Id to update query view data must be non-negative");
		
		final Optional<IQueryViewData> optElement = getData(id);

		if( optElement.isPresent() ) {
			if (!PlatformUI.getWorkbench().getDisplay().isDisposed()) {
				PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
	
					@Override
					public void run() {
						if (!tableViewer.getControl().isDisposed()) {
							tableViewer.refresh(optElement.get());
						}
					}
	
				});
			}
		} else {
			throw new IllegalArgumentException("Element " + id + " is not known in query view");
		}
	}
	
	public void addData( final IQueryViewData element ) {
		// Preconditions.checkNotNull(element, "QueryViewData to add must not be null!");
		// Preconditions.checkArgument(!data.contains(element), "QueryViewData-instance is already added");
		// Preconditions.checkArgument(!getData(element.getId()).isPresent(), "QueryViewData with id %s  is already added", element.getId());
		
		data.add(element);
	}
	
	public void removeData( int id ) {
		// Preconditions.checkArgument(id >= 0, "Id to remove query view data must be non-negative");
		
		Optional<IQueryViewData> optData = getData(id);
		if( optData.isPresent() ) {
			data.remove(optData.get());
		}
	}
	
	public Optional<IQueryViewData> getData( int id ) {
		// Preconditions.checkArgument(id >= 0, "Id to get query view data must be non-negative");
		
		for( IQueryViewData dat : data ) {
			if( dat.getId() == id ) {
				return Optional.of(dat);
			}
		}
		
		return Optional.absent();
	}

	@Override
	public void setFocus() {
		tableViewer.getTable().setFocus();
	}

	public void refreshTable() {
		if( refreshing ) {
			return;
		}
		
		if (!PlatformUI.getWorkbench().getDisplay().isDisposed()) {
			refreshing = true;
			dataProvider.onRefresh(this);
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					if (!tableViewer.getControl().isDisposed()) {
						tableViewer.refresh();
					}
					refreshing = false;
				}

			});
		}
	}
	
	public void clear() {
		data.clear();
	}
	
	private void createContextMenu() {
		// Contextmenu
		final MenuManager menuManager = new MenuManager();
		final Menu contextMenu = menuManager.createContextMenu(tableViewer.getTable());
		// Set the MenuManager
		tableViewer.getTable().setMenu(contextMenu);
		getSite().registerContextMenu(menuManager, tableViewer);
	}

	private static IQueryViewDataProvider determineDataProvider() {
		// Preconditions.checkArgument(QueryViewDataProviderManager.hasQueryViewDataProvider(), "QueryView must have at least one data provider!");
		final IQueryViewDataProvider dataProvider = QueryViewDataProviderManager.getQueryViewDataProvider();
		return dataProvider;
	}
}