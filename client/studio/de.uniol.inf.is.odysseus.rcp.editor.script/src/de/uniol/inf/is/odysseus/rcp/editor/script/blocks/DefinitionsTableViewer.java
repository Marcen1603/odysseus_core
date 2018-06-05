package de.uniol.inf.is.odysseus.rcp.editor.script.blocks;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.rcp.util.CellLabelProviderAndSorter;

public class DefinitionsTableViewer implements IDefinitionsListListener {

	private final DefinitionsList definitionsList;
	private final TableViewer tableViewer;

	public DefinitionsTableViewer(Composite parent, DefinitionsList pairs) {
		Preconditions.checkNotNull(parent, "parent must not be null!");
		Preconditions.checkNotNull(pairs, "pairs must not be null!");

		definitionsList = pairs;

		/*** Table ***/
		Composite tableComposite = new Composite(parent, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.verticalAlignment = SWT.TOP;
		gd.grabExcessVerticalSpace = true;
		gd.minimumHeight = 120;
		tableComposite.setLayoutData(gd);
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		tableViewer = new TableViewer(tableComposite, SWT.SINGLE | SWT.FULL_SELECTION);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());

		/************* Key ****************/
		TableViewerColumn keyColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		keyColumn.getColumn().setText("Key");
		keyColumn.setLabelProvider(new CellLabelProviderAndSorter<Definition, String>(tableViewer, keyColumn) {
			@Override
			protected String getValue(Definition pair) {
				return pair.getE1();
			}
		});
		tableColumnLayout.setColumnData(keyColumn.getColumn(), new ColumnWeightData(10, 15, true));

		/************* Value ****************/
		TableViewerColumn valueColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		valueColumn.getColumn().setText("Value");
		valueColumn.setLabelProvider(new CellLabelProviderAndSorter<Definition, String>(tableViewer, valueColumn) {
			@Override
			protected String getValue(Definition pair) {
				return pair.getE2();
			}
		});
		tableColumnLayout.setColumnData(valueColumn.getColumn(), new ColumnWeightData(10, 15, true));

		// cell editing
		tableViewer.setCellModifier(new ICellModifier() {

			@Override
			public boolean canModify(Object element, String property) {
				return true;
			}

			@Override
			public Object getValue(Object element, String property) {
				Definition pair = (Definition) element;
				if ("key".equals(property)) {
					return pair.getE1();
				}
				return pair.getE2();
			}

			@Override
			public void modify(Object element, String property, Object value) {
				TableItem tableItem = (TableItem) element;
				Definition pair = (Definition) tableItem.getData();
				String valueStr = (String) value;
				if ("key".equals(property)) {
					if (!pair.getE1().equals(valueStr)) {
						pair.setE1((String) value);

						definitionsList.fireListeners();
					}
				} else {
					if (!pair.getE2().equals(valueStr)) {
						pair.setE2((String) value);

						definitionsList.fireListeners();
					}
				}
			}
		});

		tableViewer.setColumnProperties(new String[] { "key", "value" });
		tableViewer.setCellEditors(new CellEditor[] { new TextCellEditor(tableViewer.getTable()), new TextCellEditor(tableViewer.getTable()) });

		tableViewer.setInput(pairs);
		definitionsList.addListener(this);
	}

	@Override
	public void definitionListChanged(DefinitionsList sender) {
		refresh();
	}

	public void dispose() {
		definitionsList.removeListener(this);
	}

	public void refresh() {
		if (tableViewer.getTable().isDisposed()) {
			return;
		}

		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (!tableViewer.getTable().isDisposed()) {
					tableViewer.refresh();
					tableViewer.getTable().getParent().layout();
				}
			}
		});
	}

	public Optional<Definition> getSelection() {
		ISelection selection = tableViewer.getSelection();
		if (selection.isEmpty()) {
			return Optional.absent();
		}

		IStructuredSelection structSelection = (IStructuredSelection) selection;
		return Optional.of((Definition) structSelection.getFirstElement());
	}
	
	public Optional<Integer> getSelectionIndex() {
		Optional<Definition> optSel = getSelection();
		if( optSel.isPresent() ) {
			int index = definitionsList.indexOf(optSel.get());
			return index != -1 ? Optional.of(index) : Optional.absent();
		}
		return Optional.absent();
	}
	
	public TableViewer getTableViewer() {
		return tableViewer;
	}

}
