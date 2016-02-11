package de.uniol.inf.is.odysseus.rcp.editor.script.blocks;

import java.util.List;
import java.util.Map;

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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlock;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptContainer;
import de.uniol.inf.is.odysseus.rcp.editor.script.VisualOdysseusScriptException;
import de.uniol.inf.is.odysseus.rcp.editor.script.impl.VisualOdysseusScriptPlugIn;
import de.uniol.inf.is.odysseus.rcp.util.CellLabelProviderAndSorter;

public class DefinesVisualOdysseusScriptBlock implements IVisualOdysseusScriptBlock {

	private static class StringPair extends Pair<String, String> {
		private static final long serialVersionUID = 1L;

		public StringPair(String str1, String str2) {
			super(str1, str2);
		}
	}

	private List<StringPair> pairs = Lists.newArrayList();

	public DefinesVisualOdysseusScriptBlock(Map<String, String> keyValuePairs) {
		Preconditions.checkNotNull(keyValuePairs, "keyValuePairs must not be null!");

		for (String key : keyValuePairs.keySet()) {
			pairs.add(new StringPair(key, keyValuePairs.get(key)));
		}
	}
	
	@Override
	public String getTitle() {
		return "Definitions";
	}

	@Override
	public void createPartControl(Composite parent, IVisualOdysseusScriptContainer container) {
		Composite topComposite = new Composite(parent, SWT.NONE);
		topComposite.setLayout(new GridLayout());
			
		Composite contentComposite = new Composite(topComposite, SWT.NONE);
		contentComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout contentLayout = new GridLayout(2, false);
		contentLayout.horizontalSpacing = 0;
		contentComposite.setLayout(contentLayout);
		
		Composite buttonComposite = new Composite(contentComposite, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginTop = -5;
		buttonComposite.setLayout(gridLayout);
		GridData buttonCompositeGridData = new GridData();
		buttonCompositeGridData.verticalAlignment = SWT.TOP;
		buttonComposite.setLayoutData(buttonCompositeGridData);

		/*** Table ***/
		Composite tableComposite = new Composite(contentComposite, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.verticalAlignment = SWT.TOP;
		gd.grabExcessVerticalSpace = true;
		tableComposite.setLayoutData(gd);
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		TableViewer tableViewer = new TableViewer(tableComposite, SWT.SINGLE | SWT.FULL_SELECTION);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());

		/************* Key ****************/
		TableViewerColumn keyColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		keyColumn.getColumn().setText("Key");
		keyColumn.setLabelProvider(new CellLabelProviderAndSorter<StringPair, String>(tableViewer, keyColumn) {
			@Override
			protected String getValue(StringPair pair) {
				return pair.getE1();
			}
		});
		tableColumnLayout.setColumnData(keyColumn.getColumn(), new ColumnWeightData(10, 15, true));

		/************* Value ****************/
		TableViewerColumn valueColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		valueColumn.getColumn().setText("Value");
		valueColumn.setLabelProvider(new CellLabelProviderAndSorter<StringPair, String>(tableViewer, valueColumn) {
			@Override
			protected String getValue(StringPair pair) {
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
				StringPair pair = (StringPair) element;
				if ("key".equals(property)) {
					return pair.getE1();
				}
				return pair.getE2();
			}

			@Override
			public void modify(Object element, String property, Object value) {
				TableItem tableItem = (TableItem) element;
				StringPair row = (StringPair) tableItem.getData();
				String valueStr = (String) value;
				if ("key".equals(property)) {
					if (!row.getE1().equals(valueStr)) {
						row.setE1((String) value);
						
						container.setDirty(true);
						tableViewer.refresh();
					}
				} else {
					if (!row.getE2().equals(valueStr)) {
						row.setE2((String) value);
						
						container.setDirty(true);
						tableViewer.refresh();
					}
				}
			}
		});

		tableViewer.setColumnProperties(new String[] { "key", "value" });
		tableViewer.setCellEditors(new CellEditor[] { new TextCellEditor(tableViewer.getTable()), new TextCellEditor(tableViewer.getTable()) });

		tableViewer.setInput(pairs);
		
		/*** Buttons ***/
		Button addButton = new Button(buttonComposite, SWT.PUSH);
		addButton.setImage(VisualOdysseusScriptPlugIn.getImageManager().get("add"));
		addButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				pairs.add(new StringPair("key", "value"));
				tableViewer.refresh();
				
				container.layoutAll();
				topComposite.layout();
				
				container.setDirty(true);
			}
		});
		
		Button removeButton = new Button(buttonComposite, SWT.PUSH);
		removeButton.setImage(VisualOdysseusScriptPlugIn.getImageManager().get("remove"));
		removeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Optional<StringPair> optPair = getSelection(tableViewer);
				if( optPair.isPresent() ) {
					pairs.remove(optPair.get());
					tableViewer.refresh();
					
					container.layoutAll();
					topComposite.layout();
					container.setDirty(true);
				}
			}
		});
		
		Button moveUpButton = new Button(buttonComposite, SWT.PUSH);
		moveUpButton.setImage(VisualOdysseusScriptPlugIn.getImageManager().get("moveUp"));
		moveUpButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		moveUpButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Optional<StringPair> optPair = getSelection(tableViewer);
				if( optPair.isPresent() ) {
					int index = pairs.indexOf(optPair.get());
					if( index > 0 ) {
						StringPair temp = pairs.get(index - 1);
						pairs.set(index - 1, optPair.get());
						pairs.set(index, temp);

						tableViewer.refresh();
						container.setDirty(true);
					}
				}
			}
		});
		
		Button moveDownButton = new Button(buttonComposite, SWT.PUSH);
		moveDownButton.setImage(VisualOdysseusScriptPlugIn.getImageManager().get("moveDown"));
		moveDownButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		moveDownButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Optional<StringPair> optPair = getSelection(tableViewer);
				if( optPair.isPresent() ) {
					int index = pairs.indexOf(optPair.get());
					if( index < pairs.size() - 1 ) {
						StringPair temp = pairs.get(index + 1);
						pairs.set(index + 1, optPair.get());
						pairs.set(index, temp);
						
						tableViewer.refresh();
						container.setDirty(true);
					}
				}
			}
		});

	}

	@Override
	public void dispose() {

	}
	
	@SuppressWarnings("unchecked")
	private static <T> Optional<T> getSelection( TableViewer tableViewer ) {
		ISelection selection = tableViewer.getSelection();
		if( selection.isEmpty() ) {
			return Optional.absent();
		}
		
		IStructuredSelection structSelection = (IStructuredSelection) selection;
		return Optional.of((T)structSelection.getFirstElement());
	}

	@Override
	public String generateOdysseusScript() throws VisualOdysseusScriptException {
		StringBuilder scriptBuilder = new StringBuilder();

		for( int i = 0; i < pairs.size(); i++ ) {
			StringPair pair = pairs.get(i);
			
			if( i != 0 ) {
				scriptBuilder.append("\n");
			}
			
			scriptBuilder.append("#DEFINE ").append(pair.getE1()).append(" ").append(pair.getE2());
		}
		
		return scriptBuilder.toString();
	}

}
