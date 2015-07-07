package de.uniol.inf.is.odysseus.parallelization.rcp.windows;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.IParallelTransformationStrategy;
import de.uniol.inf.is.odysseus.parallelization.rcp.data.BenchmarkDataHandler;
import de.uniol.inf.is.odysseus.parallelization.rcp.data.BenchmarkInitializationResult;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;

public class StrategySelectionTableViewer {

	public static final String OPERATOR_TYPE = "Operator type";
	public static final String OPERATOR_ID = "OperatorId";
	public static final String END_OPERATOR_ID = "End OperatorId";
	public static final String DEGREES = "Degrees (comma-seperated or GLOBAL)";
	public static final String PARALLELIZATION_STRATEGY = "Parallelization Strategy";
	public static final String FRAGMENTATION = "Fragmentation";

	public static final String[] PROPS = { OPERATOR_TYPE, OPERATOR_ID,
			END_OPERATOR_ID, DEGREES, PARALLELIZATION_STRATEGY, FRAGMENTATION };

	private static final int NUMBER_OF_COLUMNS = PROPS.length;

	private CheckboxTableViewer tableViewer;
	private List<StrategySelectionRow> strategySelectionRows;

	public StrategySelectionTableViewer(Composite parent,
			BenchmarkDataHandler data) {
		strategySelectionRows = convertData(data);

		parent.setLayout(new GridLayout());

		Composite tableComposite = new Composite(parent, SWT.NONE);
		tableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		tableViewer = CheckboxTableViewer.newCheckList(tableComposite,
				SWT.BORDER | SWT.FULL_SELECTION);

		Table table = tableViewer.getTable();

		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		CellEditor[] editors = new CellEditor[NUMBER_OF_COLUMNS];
		editors[2] = new TextCellEditor(table);
		editors[3] = new TextCellEditor(table);

		// columns
		TableViewerColumn attributeOperatorColumn = new TableViewerColumn(
				tableViewer, SWT.NONE);
		attributeOperatorColumn.getColumn().setText(OPERATOR_TYPE);
		tableColumnLayout.setColumnData(attributeOperatorColumn.getColumn(),
				new ColumnWeightData(5, 20, true));

		TableViewerColumn attributeOperatorIdColumn = new TableViewerColumn(
				tableViewer, SWT.NONE);
		attributeOperatorIdColumn.getColumn().setText(OPERATOR_ID);
		tableColumnLayout.setColumnData(attributeOperatorIdColumn.getColumn(),
				new ColumnWeightData(5, 10, true));

		TableViewerColumn attributeEndOperatorIdColumn = new TableViewerColumn(
				tableViewer, SWT.NONE);
		attributeEndOperatorIdColumn.getColumn().setText(END_OPERATOR_ID);
		tableColumnLayout.setColumnData(attributeEndOperatorIdColumn.getColumn(),
				new ColumnWeightData(5, 10, true));

		TableViewerColumn attributeDegreeColumn = new TableViewerColumn(
				tableViewer, SWT.NONE);
		attributeDegreeColumn.getColumn().setText(DEGREES);
		tableColumnLayout.setColumnData(attributeDegreeColumn.getColumn(),
				new ColumnWeightData(10, 35, true));

		TableViewerColumn attributeStrategyColumn = new TableViewerColumn(
				tableViewer, SWT.NONE);
		attributeStrategyColumn.getColumn().setText(PARALLELIZATION_STRATEGY);
		tableColumnLayout.setColumnData(attributeStrategyColumn.getColumn(),
				new ColumnWeightData(10, 30, true));

		TableViewerColumn attributefragmentColumn = new TableViewerColumn(
				tableViewer, SWT.NONE);
		attributefragmentColumn.getColumn().setText(FRAGMENTATION);
		tableColumnLayout.setColumnData(attributefragmentColumn.getColumn(),
				new ColumnWeightData(10, 30, true));

		tableViewer.setContentProvider(new StrategyContentProvider());
		tableViewer.setLabelProvider(new StrategyLabelProvider());

		tableViewer.setColumnProperties(PROPS);
		tableViewer.setCellModifier(new StrategyCellModifier(tableViewer));
		tableViewer.setCellEditors(editors);

		tableViewer.setInput(strategySelectionRows);
		tableViewer.setAllChecked(true);
	}

	public List<StrategySelectionRow> getSelectedStratgies() {
		ArrayList<StrategySelectionRow> checkedElementResult = new ArrayList<StrategySelectionRow>();
		Object[] checkedElements = tableViewer.getCheckedElements();
		if (checkedElements.length > 0) {
			if (checkedElements[0] instanceof StrategySelectionRow) {
				for (int i = 0; i < checkedElements.length; i++) {
					StrategySelectionRow row = (StrategySelectionRow) checkedElements[i];
					checkedElementResult.add(row);
				}
			}
		}
		return checkedElementResult;
	}

	private List<StrategySelectionRow> convertData(BenchmarkDataHandler data) {
		BenchmarkInitializationResult benchmarkInitializationResult = data
				.getBenchmarkInitializationResult();
		Map<ILogicalOperator, List<IParallelTransformationStrategy<? extends ILogicalOperator>>> strategiesForOperator = benchmarkInitializationResult
				.getStrategiesForOperator();

		List<StrategySelectionRow> rows = new ArrayList<StrategySelectionRow>();

		int counter = 0;
		for (ILogicalOperator logicalOperator : strategiesForOperator.keySet()) {
			List<IParallelTransformationStrategy<? extends ILogicalOperator>> strategies = strategiesForOperator
					.get(logicalOperator);
			for (IParallelTransformationStrategy<? extends ILogicalOperator> strategy : strategies) {
				List<Class<? extends AbstractFragmentAO>> allowedFragmentationTypes = strategy
						.getAllowedFragmentationTypes();
				for (Class<? extends AbstractFragmentAO> fragmentationType : allowedFragmentationTypes) {
					rows.add(new StrategySelectionRow(counter, logicalOperator,
							logicalOperator.getUniqueIdentifier(), strategy,
							fragmentationType));
					counter++;
				}
			}
		}
		return rows;
	}

	class StrategyContentProvider implements IStructuredContentProvider {

		@Override
		public void dispose() {
			// TODO Auto-generated method stub

		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub

		}

		@Override
		public Object[] getElements(Object inputElement) {
			return strategySelectionRows.toArray();
		}

	}
}
