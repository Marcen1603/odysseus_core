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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.mep.ParseException;
import de.uniol.inf.is.odysseus.rcp.exception.ExceptionWindow;
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

	protected List<Tuple<?>> tuples = new ArrayList<Tuple<?>>();
	private List<Integer> shownAttributes = new ArrayList<Integer>();

	private Object filterMonitor = new Object();
	private String filterExpressionString;
	private TupleFilter filter;

	private int maxTuplesCount;
	private Boolean isRefreshing = false;

	private boolean isDesync;
	private RefreshTableThread desyncThread;
	private boolean isShowingMetadata = true;
	private boolean isShowingHashCode = false;

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
	public void streamElementReceived(IPhysicalOperator senderOperator, Object element, int port) {
		if (!(element instanceof Tuple<?>)) {
			System.out.println("Warning: StreamTable is only for relational tuple!");
			return;
		}

		updateTuples((Tuple<?>) element);

		refresh();
	}

	protected void refresh() {
		synchronized (isRefreshing) {
			if (!isDesync && !isRefreshing && hasTableViewer() && !getTableViewer().getTable().isDisposed()) {

				isRefreshing = true;

				PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						synchronized (isRefreshing) {
							try {
								if (!getTableViewer().getTable().isDisposed()) {
									getTableViewer().refresh();
								}
							} catch (Exception e) {
								// ignore
							} finally {
								isRefreshing = false;
							}
						}
					}
				});
				editor.activateIfNeeded();
			}
		}
	}

	public void updateTuples(Tuple<?> element) {

		synchronized (filterMonitor) {
			if (filter != null && filter.isFiltered(element)) {
				return; // discared filtered elements
			}
		}

		synchronized (tuples) {
			tuples.add(0, element.clone());
			if (maxTuplesCount > 0 && tuples.size() > maxTuplesCount) {
				tuples.remove(tuples.size() - 1);
			}
		}
	}

	@Override
	public void punctuationElementReceived(IPhysicalOperator senderOperator, IPunctuation punctuation, int port) {
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
				FilterWindow window = new FilterWindow(PlatformUI.getWorkbench().getDisplay(), schema, shownAttributes,
						filterExpressionString);
				window.show();

				if (!window.isCanceled()) {

					if (!window.getSelectedAttributeIndices().isEmpty()) {
						createColumns(getTableViewer(), window.getSelectedAttributeIndices());
						if (getSchema().size() != window.getSelectedAttributeIndices().size()) {
							toolbarLabel.setText(window.getSelectedAttributeIndices().size() + " of "
									+ getSchema().size() + " attributes show.");
						} else {
							toolbarLabel.setText("");
						}
					}

					filterExpressionString = window.getFilterExpression();

					if (!Strings.isNullOrEmpty(filterExpressionString)) {
						try {
							synchronized (filterMonitor) {
								filter = new TupleFilter(filterExpressionString, getSchema());
							}

							toolbarLabel.setText(toolbarLabel.getText() + " [ " + filter.getExpressionString() + " ]");
						} catch (ParseException e1) {
							new ExceptionWindow("Could not apply filter '" + filterExpressionString + "'", e1);
						}
					} else {
						synchronized (filterMonitor) {
							filter = null;
						}
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
				if (isDesync) {
					desyncThread = new RefreshTableThread(getTableViewer());
					desyncThread.start();
				} else {
					stopRefreshThread();
				}
			}
		});

		final ToolItem metadataButton = new ToolItem(toolbar, SWT.CHECK);
		metadataButton.setImage(ViewerStreamTablePlugIn.getImageManager().get("metadata"));
		metadataButton.setToolTipText("Show metadata");
		metadataButton.setSelection(isShowingMetadata);
		metadataButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isShowingMetadata = metadataButton.getSelection();
				createAllColumns(getTableViewer());
			}

		});

		final ToolItem hashCodeButton = new ToolItem(toolbar, SWT.CHECK);
		hashCodeButton.setImage(ViewerStreamTablePlugIn.getImageManager().get("metadata"));
		hashCodeButton.setToolTipText("Show hashCode");
		hashCodeButton.setSelection(isShowingHashCode);
		hashCodeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isShowingHashCode = hashCodeButton.getSelection();
				createAllColumns(getTableViewer());
			}

		});

		final ToolItem exportButton = new ToolItem(toolbar, SWT.PUSH);
		exportButton.setImage(ViewerStreamTablePlugIn.getImageManager().get("export"));
		exportButton.setToolTipText("Export");
		exportButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				try {
					export(getTableViewer());
				} catch (IOException e) {
					LOG.error(e.getMessage(), e);
				}
			}
		});

		ToolItem clearButton = new ToolItem(toolbar, SWT.PUSH);
		clearButton.setImage(ViewerStreamTablePlugIn.getImageManager().get("clear"));
		clearButton.setToolTipText("Clear");
		clearButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (MessageDialog.openConfirm(parent.getShell(), "Clear table data",
						"Are you sure to clear the table?")) {
					synchronized (tuples) {
						tuples.clear();
					}

					refresh();
				}
			}
		});

		toolbarLabel = new Label(toolbar.getParent(), SWT.NONE);
		toolbarLabel.setText(
				"                                                                                                                                                                                     ");
	}

	public final SDFSchema getSchema() {
		return schema;
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

		col.getColumn().setText(attribute.getAttributeName());
		col.getColumn().setToolTipText(attribute.getURI());
		col.getColumn().setAlignment(SWT.CENTER);
		col.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				try {
					Object attr = ((Tuple<?>) cell.getElement()).getAttribute(attributeIndex);
					if (attr != null) {
						if (attr instanceof Object[]) {
							cell.setText(Arrays.deepToString((Object[]) attr));
						} else if (attr.getClass().isArray()) {
							cell.setText(Arrays.toString((double[]) attr));
						} else {
							cell.setText(attr.toString());
						}
					} else {
						cell.setText("<null>");
					}
					cell.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
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
				if (attr1 != null && attr2 != null) {
					return Objects.compare(attr1, attr2, new ObjectComparator());
				}
				return 0;
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
		shownAttributes = Objects.requireNonNull(attributeIndexes, "List of indexes must not be null");
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

			int weight = 1000 / (attributeIndexes.size() + (isShowingMetadata ? 1 : 0));
			for (Integer attributeIndex : attributeIndexes) {
				TableViewerColumn col = createColumn(tableViewer, getSchema().get(attributeIndex));
				layout.setColumnData(col.getColumn(), new ColumnWeightData(weight, 25, true));
			}

			if (isShowingMetadata) {

				List<SDFMetaSchema> metaschemaList = getSchema().getMetaschema();
				if (metaschemaList != null) {
					for (int i = 0; i < metaschemaList.size(); i++) {
						SDFSchema metaschema = metaschemaList.get(i);
						for (int j = 0; j < metaschema.size(); j++) {
							SDFAttribute metaAttribute = metaschema.get(j);
							TableViewerColumn metadataColumn = createMetadataAttributeColumn(tableViewer, metaAttribute,
									i, j);
							layout.setColumnData(metadataColumn.getColumn(), new ColumnWeightData(weight, 25, true));
						}
					}
				} else {
					TableViewerColumn metadataColumn = createMetadataColumn(tableViewer);
					layout.setColumnData(metadataColumn.getColumn(), new ColumnWeightData(weight, 25, true));
				}
			}

		} finally {
			getParent().layout();
			tableViewer.getTable().setRedraw(true);
			tableViewer.refresh();
		}
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

	private TableViewerColumn createMetadataAttributeColumn(TableViewer tableViewer, SDFAttribute metadataAttribute,
			final int metaschemaIndex, final int metaAttributeIndex) {
		TableViewerColumn col = new TableViewerColumn(tableViewer, SWT.NONE);
		col.getColumn().setText(metadataAttribute.getAttributeName());
		col.getColumn().setAlignment(SWT.CENTER);

		col.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				try {
					Tuple<?> tuple = (Tuple<?>) cell.getElement();
					if (tuple.getMetadata() != null) {
						Object metadata = tuple.getMetadata().getValue(metaschemaIndex, metaAttributeIndex);
						if (metadata != null) {
							cell.setText(metadata.toString());
						} else {
							cell.setText("<null>");
						}
					} else {
						cell.setText("<null>");
					}
					cell.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));

				} catch (Throwable t) {
					LOG.error("Could not retrieve metadata attribute ", t);
					cell.setText("<Error>");
				}
			}
		});

		return col;
	}

	private void stopRefreshThread() {
		if (desyncThread != null) {
			desyncThread.stopRunning();
			desyncThread = null;
		}
	}

	private void export(TableViewer tableViewer) throws IOException {
		String CSV_SEPARATOR = ";";
		Table table = tableViewer.getTable();
		int columns = table.getColumnCount();
		TableItem[] items = table.getItems();
		FileDialog fd = new FileDialog(table.getShell(), SWT.SAVE);
		fd.setText("Save");
		fd.setFilterPath(Paths.get(System.getProperty("user.home")).toString());
		String[] filterExt = { "*.csv", "*.*" };
		fd.setFilterExtensions(filterExt);
		String selected = fd.open();
		File output = Paths.get(selected).toFile();
		output.createNewFile();
		try (BufferedWriter out = new BufferedWriter(new FileWriter(output))) {
			StringBuilder line = new StringBuilder();
			for (int i = 0; i < columns; i++) {
				if (i > 0) {
					line.append(CSV_SEPARATOR);
				}
				String text = table.getColumns()[i].getText();
				line.append(text);
			}
			out.write(line.toString());
			out.newLine();
			for (TableItem item : items) {
				line = new StringBuilder();
				for (int i = 0; i < columns; i++) {
					if (i > 0) {
						line.append(CSV_SEPARATOR);
					}
					String text = item.getText(i);
					line.append(text);
				}
				out.write(line.toString());
				out.newLine();
			}
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
