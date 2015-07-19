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

public class StrategySelectionHelper {

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

	public StrategySelectionHelper(Composite parent,
			BenchmarkDataHandler data) {
		strategySelectionRows = convertData(data);

		parent.setLayout(new GridLayout());

		Composite tableComposite = new Composite(parent, SWT.NONE);
		tableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		
		tableViewer = StrategyTableViewer.newCheckList(tableComposite,
				SWT.BORDER | SWT.FULL_SELECTION, strategySelectionRows );

		createColumns(tableColumnLayout);
		
		tableViewer.setContentProvider(new StrategyContentProvider(
				strategySelectionRows));
		tableViewer.setLabelProvider(new StrategyLabelProvider());
		tableViewer.setCellModifier(new StrategyCellModifier(tableViewer));

		CellEditor[] editors = new CellEditor[NUMBER_OF_COLUMNS];
		editors[2] = new TextCellEditor(tableViewer.getTable());
		editors[3] = new TextCellEditor(tableViewer.getTable());
		getTableViewer().setColumnProperties(PROPS);
		getTableViewer().setCellEditors(editors);
		
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
			TableViewerColumn column = new TableViewerColumn(getTableViewer(),
					SWT.NONE);
			column.getColumn().setText(PROPS[i]);
			tableColumnLayout.setColumnData(column.getColumn(),
					new ColumnWeightData(weight[i], 10, true));
		}
	}
	
	public List<StrategySelectionRow> getSelectedStratgies() {
		return tableViewer.getSelectedStratgies();
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

	public CheckboxTableViewer getTableViewer() {
		return tableViewer;
	}
}
