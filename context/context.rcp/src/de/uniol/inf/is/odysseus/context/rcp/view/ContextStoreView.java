/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/** Copyright 2012 The Odysseus Team
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

package de.uniol.inf.is.odysseus.context.rcp.view;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.context.IContextManagementListener;
import de.uniol.inf.is.odysseus.context.IContextStoreListener;
import de.uniol.inf.is.odysseus.context.store.ContextStoreManager;
import de.uniol.inf.is.odysseus.context.store.IContextStore;

/**
 * 
 * @author Dennis Geesen Created at: 27.04.2012
 */
public class ContextStoreView extends ViewPart implements IContextManagementListener, ISelectionChangedListener, IContextStoreListener {

	private static final Logger LOG = LoggerFactory.getLogger(ContextStoreView.class);

	public static final String VIEW_ID = "de.uniol.inf.is.odysseus.context.rcp.contextstoreview";

	private TreeViewer treeViewer;
	private TableViewer tableViewer;

	private IContextStore<?> selectedStore;

	@Override
	public void createPartControl(Composite parent) {
		FillLayout layout = new FillLayout();
		layout.type = SWT.VERTICAL;		
		parent.setLayout(layout);

		SashForm sashForm = new SashForm(parent, SWT.VERTICAL);
		
		treeViewer = new TreeViewer(sashForm, SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
		treeViewer.setContentProvider(new ContextViewTreeContentProvider());
		treeViewer.setLabelProvider(new ContextViewTreeLabelProvider());
		treeViewer.addSelectionChangedListener(this);

		Composite tableComposite = new Composite(sashForm, SWT.NONE);
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);				
		tableViewer = new TableViewer(tableComposite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		tableViewer.setContentProvider(new ContextViewTableContentProvider());
		// table.setLabelProvider(new ContextViewTreeLabelProvider());
		final Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		TableViewerColumn col = new TableViewerColumn(tableViewer, SWT.NONE);		
		col.getColumn().setText("Values");
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return element.toString();
			}
		});
		tableColumnLayout.setColumnData(col.getColumn(), new ColumnWeightData(100, true));



		refresh();

		ContextStoreManager.addListener(this);
		getSite().setSelectionProvider(treeViewer);

		// Contextmenu
		MenuManager menuManager = new MenuManager();
		Menu contextMenu = menuManager.createContextMenu(treeViewer.getControl());
		// Set the MenuManager
		treeViewer.getControl().setMenu(contextMenu);
		getSite().registerContextMenu(menuManager, treeViewer);

	}

	@Override
	public void dispose() {
		ContextStoreManager.removeListener(this);
		if(this.selectedStore!=null){
			this.selectedStore.removeListener(this);
		}
		super.dispose();		
	}

	private void refresh() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				try {
					treeViewer.setInput(ContextStoreManager.getStores());
					treeViewer.refresh();
				} catch (Exception e) {
					LOG.error("Exception during setting input for treeViewer in ContextView", e);
				}
			}

		});

	}

	@Override
	public void setFocus() {
		treeViewer.getControl().setFocus();

	}

	@Override
	public void contextManagementChanged() {
		refresh();
	}

	public void refreshAll() {
		refresh();
	}

	private IContextStore<?> getStoreFromTreeSelection(TreeSelection sel) {
		TreePath[] paths = sel.getPaths();
		if (paths.length > 0) {
			Object o = paths[0].getFirstSegment();
			if (o instanceof IContextStore) {
				return (IContextStore<?>) o;
			}
		}
		return null;
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		if (treeViewer.getSelection() != null) {
			ISelection selection = treeViewer.getSelection();
			if (selection instanceof TreeSelection) {
				TreeSelection structSel = (TreeSelection) selection;

				IContextStore<?> newStore = getStoreFromTreeSelection(structSel);
				if (this.selectedStore != null) {
					if (!this.selectedStore.equals(newStore)) {
						this.selectedStore.removeListener(this);
						this.selectedStore = newStore;
						this.selectedStore.addListener(this);
					}
				}else{
					this.selectedStore = newStore;
					this.selectedStore.addListener(this);				
				}

			}
		}
	}

	@Override
	public void contextStoreChanged(final IContextStore<?> store) {
		if (store.equals(selectedStore)) {			
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					try {
						tableViewer.setInput(store);

					} catch (Exception e) {
						LOG.error("Exception during setting input for tableviewer in ContextView", e);
					}
				}

			});
			
		}
	}

}
