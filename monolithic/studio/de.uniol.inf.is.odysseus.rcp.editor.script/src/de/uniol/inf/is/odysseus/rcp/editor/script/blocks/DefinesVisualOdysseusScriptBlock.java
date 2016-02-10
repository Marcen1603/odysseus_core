package de.uniol.inf.is.odysseus.rcp.editor.script.blocks;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlock;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptContainer;
import de.uniol.inf.is.odysseus.rcp.editor.script.VisualOdysseusScriptException;
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
	public void createPartControl(Composite parent, IVisualOdysseusScriptContainer container) {
		Composite topComposite = new Composite(parent, SWT.NONE);
		topComposite.setLayout(new GridLayout());
		
		Label titleLabel = new Label(topComposite, SWT.NONE);
		titleLabel.setText("Definitions");
		titleLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		titleLabel.setAlignment(SWT.CENTER);
		
		Composite tableComposite = new Composite(topComposite, SWT.NONE);
		tableComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		TableViewer tableViewer = new TableViewer(tableComposite, SWT.MULTI | SWT.FULL_SELECTION);
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
	}

	@Override
	public void dispose() {

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
