/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.IParallelTransformationStrategy;
import de.uniol.inf.is.odysseus.parallelization.rcp.data.BenchmarkDataHandler;
import de.uniol.inf.is.odysseus.parallelization.rcp.data.BenchmarkInitializationResult;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractStaticFragmentAO;

/**
 * Helper class for creating and configuring the strategy selection table
 * 
 * @author ChrisToenjesDeye
 *
 */
public class StrategySelectionHelper {

	// column headers
	public static final String OPERATOR_TYPE = "Operator type";
	public static final String OPERATOR_ID = "OperatorId";
	public static final String END_OPERATOR_ID = "End OperatorId";
	public static final String DEGREES = "Custom degrees (comma-seperated)";
	public static final String PARALLELIZATION_STRATEGY = "Parallelization Strategy";
	public static final String FRAGMENTATION = "Fragmentation";
	public static final String[] PROPS = { OPERATOR_TYPE, OPERATOR_ID,
			END_OPERATOR_ID, DEGREES, PARALLELIZATION_STRATEGY, FRAGMENTATION };
	private static final int NUMBER_OF_COLUMNS = PROPS.length;

	private StrategyTableViewer tableViewer;
	private List<StrategySelectionRow> strategySelectionRows;

	public StrategySelectionHelper(Composite parent, BenchmarkDataHandler data) {
		strategySelectionRows = convertData(data);

		parent.setLayout(new GridLayout());

		// create composite
		Composite tableComposite = new Composite(parent, SWT.NONE);
		tableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		// create table viewer
		tableViewer = StrategyTableViewer.newCheckList(tableComposite,
				SWT.BORDER | SWT.FULL_SELECTION, strategySelectionRows);

		// create columns
		createColumns(tableColumnLayout);

		// add content provider, label provider and cell modifier
		tableViewer.setContentProvider(new StrategyContentProvider(
				strategySelectionRows));
		tableViewer.setLabelProvider(new StrategyLabelProvider());
		tableViewer.setCellModifier(new StrategyCellModifier(tableViewer));

		// create and add cell editors
		CellEditor[] editors = new CellEditor[NUMBER_OF_COLUMNS];
		editors[2] = new TextCellEditor(tableViewer.getTable());
		editors[3] = new TextCellEditor(tableViewer.getTable());
		getTableViewer().setColumnProperties(PROPS);
		getTableViewer().setCellEditors(editors);

		// set the input for this table
		tableViewer.setInput(strategySelectionRows);

		// select all rows
		tableViewer.setAllChecked(true);

	}

	/**
	 * create all column headers with a different weight
	 * 
	 * @param tableColumnLayout
	 */
	private void createColumns(TableColumnLayout tableColumnLayout) {
		int[] weight = new int[PROPS.length];
		weight[0] = 20;
		weight[1] = 10;
		weight[2] = 10;
		weight[3] = 35;
		weight[4] = 30;
		weight[5] = 30;

		for (int i = 0; i < PROPS.length; i++) {
			TableViewerColumn column = new TableViewerColumn(getTableViewer(),
					SWT.NONE);
			column.getColumn().setText(PROPS[i]);
			tableColumnLayout.setColumnData(column.getColumn(),
					new ColumnWeightData(weight[i], 10, true));
		}
	}

	/**
	 * return the selected strategies
	 * 
	 * @return
	 */
	public List<StrategySelectionRow> getSelectedStratgies() {
		return tableViewer.getSelectedStratgies();
	}

	/**
	 * converts the initialization data to rows
	 * 
	 * @param data
	 * @return
	 */
	private List<StrategySelectionRow> convertData(BenchmarkDataHandler data) {
		BenchmarkInitializationResult benchmarkInitializationResult = data
				.getBenchmarkInitializationResult();
		Map<ILogicalOperator, List<IParallelTransformationStrategy<ILogicalOperator>>> strategiesForOperator = benchmarkInitializationResult
				.getStrategiesForOperator();

		List<StrategySelectionRow> rows = new ArrayList<StrategySelectionRow>();

		int counter = 0;
		// create a new row for every operator
		for (ILogicalOperator logicalOperator : strategiesForOperator.keySet()) {
			List<IParallelTransformationStrategy<ILogicalOperator>> strategies = strategiesForOperator
					.get(logicalOperator);
			// also create new row for each strategy
			for (IParallelTransformationStrategy<ILogicalOperator> strategy : strategies) {
				List<Class<? extends AbstractStaticFragmentAO>> allowedFragmentationTypes = strategy
						.getAllowedFragmentationTypes();
				// also create new row for each possible fragmentation type
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

	public CheckboxTableViewer getTableViewer() {
		return tableViewer;
	}
}
