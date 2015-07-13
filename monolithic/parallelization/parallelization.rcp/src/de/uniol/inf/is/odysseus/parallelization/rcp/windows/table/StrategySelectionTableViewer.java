package de.uniol.inf.is.odysseus.parallelization.rcp.windows.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.IParallelTransformationStrategy;
import de.uniol.inf.is.odysseus.parallelization.rcp.data.BenchmarkDataHandler;
import de.uniol.inf.is.odysseus.parallelization.rcp.data.BenchmarkInitializationResult;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractStaticFragmentAO;

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

		Table table = configureTable();
		createColumns(tableColumnLayout);
		configureTableViewer(table);
	}

	private Table configureTable() {
		Table table = tableViewer.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		return table;
	}

	private void configureTableViewer(Table table) {
		tableViewer.setContentProvider(new StrategyContentProvider(
				strategySelectionRows));
		tableViewer.setLabelProvider(new StrategyLabelProvider());

		CellEditor[] editors = new CellEditor[NUMBER_OF_COLUMNS];
		editors[2] = new TextCellEditor(table);
		editors[3] = new TextCellEditor(table);
		tableViewer.setColumnProperties(PROPS);
		tableViewer.setCellModifier(new StrategyCellModifier(tableViewer));
		tableViewer.setCellEditors(editors);

		tableViewer.setInput(strategySelectionRows);

		tableViewer.setAllChecked(true);
	}

	private void createColumns(TableColumnLayout tableColumnLayout) {
		int[] weight = new int[PROPS.length];
		weight[0] = 20;
		weight[1] = 10;
		weight[2] = 10;
		weight[3] = 35;
		weight[4] = 30;
		weight[5] = 30;

		for (int i = 0; i < PROPS.length; i++) {
			TableViewerColumn column = new TableViewerColumn(tableViewer,
					SWT.NONE);
			column.getColumn().setText(PROPS[i]);
			tableColumnLayout.setColumnData(column.getColumn(),
					new ColumnWeightData(weight[i], 10, true));
		}
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
				List<Class<? extends AbstractStaticFragmentAO>> allowedFragmentationTypes = strategy
						.getAllowedFragmentationTypes();
				for (Class<? extends AbstractStaticFragmentAO> fragmentationType : allowedFragmentationTypes) {
					rows.add(new StrategySelectionRow(counter, logicalOperator,
							logicalOperator.getUniqueIdentifier(), strategy,
							fragmentationType));
					counter++;
				}
			}
		}
		return rows;
	}
}
