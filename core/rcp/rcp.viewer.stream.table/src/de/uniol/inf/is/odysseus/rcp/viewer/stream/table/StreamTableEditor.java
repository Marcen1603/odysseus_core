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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.rcp.viewer.editors.StreamEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorType;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.table.activator.ViewerStreamTablePlugIn;

public class StreamTableEditor implements IStreamEditorType {

	private static final Logger LOG = LoggerFactory.getLogger(StreamTableEditor.class);

	private TableViewer viewer;
	private SDFSchema schema;
	private Composite parent;
	private Label toolbarLabel;
	private StreamEditor editor;

	private List<Tuple<?>> tuples = new ArrayList<Tuple<?>>();
	private List<Integer> shownAttributes = new ArrayList<Integer>();
	private int maxTuplesCount;
	private boolean isRefreshing;
	
	private boolean isDesync;
	private RefreshTableThread desyncThread;

	public StreamTableEditor(int maxTuples) {
		setMaxTuplesCount(maxTuples);
	}

	@Override
	public void init(StreamEditor editorPart, IStreamEditorInput editorInput) {
		// TODO: Adapt to multiple sources
		setSchema(editorInput.getStreamConnection().getOutputSchema());
		editor = editorPart;
	}

	@Override
	public void createPartControl(Composite parent) {
		setParent(parent);

		if (hasSchema() && getSchema().size() > 0) {
			setTableViewer(createTableViewer(parent));
			getTableViewer().setContentProvider(createContentProvider());
			getTableViewer().setInput(tuples);

		} else {
			// Kein Schema vorhanden
			Label label = new Label(parent, SWT.NONE);
			label.setText("Operator provides no schema");
		}
	}

	@Override
	public void streamElementRecieved(Object element, int port) {
		if (!(element instanceof Tuple<?>)) {
			System.out.println("Warning: StreamTable is only for relational tuple!");
			return;
		}

		tuples.add(0, (Tuple<?>) element);
		if (tuples.size() > getMaxTuplesCount()) {
			tuples.remove(tuples.size() - 1);
		}

		if (!isDesync && !isRefreshing && hasTableViewer() && !getTableViewer().getTable().isDisposed()) {
			isRefreshing = true;
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					try {
						if (!getTableViewer().getTable().isDisposed()) {
							getTableViewer().refresh();
						}
					} finally {
						isRefreshing = false;
					}
				}
			});
			editor.activateIfNeeded();
		}
	}

	@Override
	public void punctuationElementRecieved(IPunctuation punctuation, int port) {
	}

	@Override
	public void securityPunctuationElementRecieved(ISecurityPunctuation sp, int port) {
	}

	@Override
	public void setFocus() {
		if (hasTableViewer()) {
			getTableViewer().getControl().setFocus();
		}
	}

	@Override
	public void dispose() {
		stopRefreshThread();
	}

	@Override
	public void initToolbar(ToolBar toolbar) {
		ToolItem filterButton = new ToolItem(toolbar, SWT.PUSH);
		filterButton.setImage(ViewerStreamTablePlugIn.getImageManager().get("filter"));
		filterButton.setToolTipText("Filter columns");
		filterButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FilterWindow window = new FilterWindow(PlatformUI.getWorkbench().getDisplay(), schema, shownAttributes);
				window.show();

				if (!window.isCanceled() && !window.getSelectedAttributeIndices().isEmpty()) {
					createColumns(getTableViewer(), window.getSelectedAttributeIndices());
					if (getSchema().size() != window.getSelectedAttributeIndices().size()) {
						toolbarLabel.setText(window.getSelectedAttributeIndices().size() + " of " + getSchema().size() + " attributes show.");
					} else {
						toolbarLabel.setText("");
					}
					getParent().layout();
				}
			}
		});
		
		final ToolItem desyncButton = new ToolItem(toolbar, SWT.CHECK);
		desyncButton.setImage(ViewerStreamTablePlugIn.getImageManager().get("desync"));
		desyncButton.setToolTipText("Desyncronize");
		desyncButton.setSelection(isDesync);
		desyncButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isDesync = desyncButton.getSelection();
				if( isDesync ) {
					desyncThread = new RefreshTableThread(getTableViewer());
					desyncThread.start();
				} else {
					stopRefreshThread();
				}
			}

		});

		toolbarLabel = new Label(toolbar.getParent(), SWT.NONE);
		toolbarLabel
				.setText("                                                                                                                                                                                     ");
	}

	public final SDFSchema getSchema() {
		return schema;
	}

	public final int getMaxTuplesCount() {
		return maxTuplesCount;
	}

	public final Composite getParent() {
		return parent;
	}

	public final boolean hasSchema() {
		return getSchema() != null;
	}

	protected IContentProvider createContentProvider() {
		return ArrayContentProvider.getInstance();
	}

	protected TableViewer createTableViewer(Composite parent) {
		TableViewer tableViewer = new TableViewer(parent, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);

		createAllColumns(tableViewer);

		return tableViewer;
	}

	protected void disposeColumn(TableColumn column) {
		column.dispose();
	}

	protected TableViewerColumn createColumn(TableViewer tableViewer, SDFAttribute attribute) {
		TableViewerColumn col = new TableViewerColumn(tableViewer, SWT.NONE);
		final int attributeIndex = getSchema().indexOf(attribute);

		col.getColumn().setText(attribute.getURI());
		col.getColumn().setAlignment(SWT.CENTER);
		col.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				try {
					Object attr = ((Tuple<?>) cell.getElement()).getAttribute(attributeIndex);
					if (attr != null) {
						cell.setText(attr.toString());
					} else {
						cell.setText("<null>");
					}
				} catch (Throwable t) {
					LOG.error("Could not retrieve attributeValue", t);
					cell.setText("<Error>");
				}
			}
		});
		TupleColumnViewerSorter sorter = new TupleColumnViewerSorter(tableViewer, col) {
			@Override
			protected int doCompare(Viewer viewer, Tuple<?> e1, Tuple<?> e2) {
				Object attr1 = e1.getAttribute(attributeIndex);
				Object attr2 = e2.getAttribute(attributeIndex);
				return Objects.compare(attr1, attr2, new ObjectComparator());
			}
		};
		sorter.setSorter(sorter, TupleColumnViewerSorter.NONE);
		
		return col;
	}
	
	private TableViewerColumn createMetadataColumn(TableViewer tableViewer) {
		TableViewerColumn col = new TableViewerColumn(tableViewer, SWT.NONE);
		col.getColumn().setText("Metadata");
		col.getColumn().setAlignment(SWT.CENTER);

		col.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				try {
					Object metadata = ((Tuple<?>) cell.getElement()).getMetadata();
					if (metadata != null) {
						cell.setText(metadata.toString());
					} else {
						cell.setText("<null>");
					}
					cell.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
					
				} catch (Throwable t) {
					LOG.error("Could not retrieve metadata", t);
					cell.setText("<Error>");
				}
			}
		});
		TupleColumnViewerSorter sorter = new TupleColumnViewerSorter(tableViewer, col) {
			@Override
			protected int doCompare(Viewer viewer, Tuple<?> e1, Tuple<?> e2) {
				Object attr1 = e1.getMetadata();
				Object attr2 = e2.getMetadata();
				return Objects.compare(attr1, attr2, new ObjectComparator());
			}
		};
		sorter.setSorter(sorter, TupleColumnViewerSorter.NONE);
		
		return col;
	}
	
	protected final TableViewer getTableViewer() {
		return viewer;
	}

	protected final boolean hasTableViewer() {
		return getTableViewer() != null;
	}

	protected final List<Integer> getSelectedAttributeIndexes() {
		return shownAttributes;
	}

	private void disposeAllColumns(TableViewer tableViewer) {
		while (tableViewer.getTable().getColumnCount() > 0) {
			disposeColumn(tableViewer.getTable().getColumn(0));
		}
	}

	private void setMaxTuplesCount(int maxTuples) {
		if (maxTuples > 0)
			this.maxTuplesCount = maxTuples;
		else
			this.maxTuplesCount = Integer.MAX_VALUE;
	}

	private void setTableViewer(TableViewer viewer) {
		this.viewer = viewer; // kann null sein
	}

	private void setSchema(SDFSchema schema) {
		this.schema = schema; // kann auch null sein!
	}

	private void setSelectedAttributeIndexes(List<Integer> attributeIndexes) {
		shownAttributes = Preconditions.checkNotNull(attributeIndexes, "List of indexes must not be null");
	}

	private void setParent(Composite parent) {
		this.parent = parent;
	}

	private void createAllColumns(TableViewer tableViewer) {
		createColumns(tableViewer, createIdentity(getSchema().size()));
	}

	private void createColumns(TableViewer tableViewer, List<Integer> attributeIndexes) {
		try {
			tableViewer.getTable().setRedraw(false);

			TableColumnLayout layout = new TableColumnLayout();
			getParent().setLayout(layout);

			disposeAllColumns(tableViewer);
			setSelectedAttributeIndexes(attributeIndexes);

			int weight = 1000 / ( attributeIndexes.size() + 1);
			for (Integer attributeIndex : attributeIndexes) {
				TableViewerColumn col = createColumn(tableViewer, getSchema().get(attributeIndex));
				layout.setColumnData(col.getColumn(), new ColumnWeightData(weight, 25, true));
			}
			
			TableViewerColumn col = createMetadataColumn(tableViewer);
			layout.setColumnData(col.getColumn(), new ColumnWeightData(weight, 25, true));
			
		} finally {
			tableViewer.getTable().setRedraw(true);
		}
	}


	private void stopRefreshThread() {
		if( desyncThread != null ) {
			desyncThread.stopRunning();
			desyncThread = null;
		}
	}

	private static List<Integer> createIdentity(int max) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < max; i++) {
			list.add(i);
		}
		return list;
	}
}
