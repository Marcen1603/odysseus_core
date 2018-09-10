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
package de.uniol.inf.is.odysseus.parallelization.rcp.windows.composite;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.parallelization.interoperator.constants.InterOperatorParallelizationConstants;
import de.uniol.inf.is.odysseus.parallelization.interoperator.helper.LogicalGraphHelper;
import de.uniol.inf.is.odysseus.parallelization.intraoperator.constants.IntraOperatorParallelizationConstants;
import de.uniol.inf.is.odysseus.parallelization.rcp.constants.ParallelizationBenchmarkerConstants;
import de.uniol.inf.is.odysseus.parallelization.rcp.data.BenchmarkDataHandler;
import de.uniol.inf.is.odysseus.parallelization.rcp.data.BenchmarkerConfiguration;
import de.uniol.inf.is.odysseus.parallelization.rcp.windows.ParallelizationBenchmarkerWindow;
import de.uniol.inf.is.odysseus.parallelization.rcp.windows.table.StrategySelectionHelper;
import de.uniol.inf.is.odysseus.parallelization.rcp.windows.table.StrategySelectionRow;

/**
 * composite class for configuration content of benchmarker UI
 * 
 * @author ChrisToenjesDeye
 *
 */
public class BenchmarkConfigureComposite extends AbstractBenchmarkComposite {
	private final String BOTH = "Threaded & Non-Threaded Buffer";
	private final String THREADED = "Only Threaded Buffer";
	private final String NON_THREADED = "Only Non-Threaded Buffer";
	private final String[] BUFFER_COMBO = { BOTH, THREADED, NON_THREADED };

	private BenchmarkDataHandler data;
	private ParallelizationBenchmarkerWindow window;

	private Text numberOfElementsText;
	private Text maxExecutionTimeText;
	private Text numberOfExecutionsText;

	private Button useInterOperatorParallelization;
	private Text interOperatorDegreeText;
	private Text interOperatorBuffersizeText;
	private Button allowPostOptimizationButton;
	private Button useThreadedOperatorsButton;
	private Combo buffertypeCombo;
	private StrategySelectionHelper strategySelectionHelper;

	private Button useIntraOperatorParallelization;
	private Text intraOperatorDegreeText;
	private Text intraOperatorSelectedOperatorsText;
	private Text intraOperatorBufferSizeText;

	public BenchmarkConfigureComposite(Composite parent, int style,
			int windowWidth, UUID benchmarkProcessId,
			ParallelizationBenchmarkerWindow window) {
		super(parent, style);
		this.window = window;

		GridData contentGridData = new GridData(GridData.FILL_BOTH);
		contentGridData.widthHint = windowWidth;
		this.setLayoutData(contentGridData);
		GridLayout gridLayout = new GridLayout();
		this.setLayout(gridLayout);

		// check if the current query contains operators for parallelization
		this.data = BenchmarkDataHandler
				.getExistingInstance(benchmarkProcessId);
		if (data.getBenchmarkInitializationResult().getStrategiesForOperator()
				.isEmpty()) {
			window.createErrorMessage(
					this,
					new Exception(
							"Running Benchmark not possible. No operator for parallelization found. \n Maybe you need to add unique ids to operators you want to parallelize."));
			return;
		}
		// if strategies are found, create config content
		createContent();

		parent.pack();
		parent.setVisible(true);
	}

	/**
	 * prepare the analysis execution (validation and creating of config
	 * elements
	 * 
	 * @return
	 */
	public boolean prepareParallelizationAnalysis() {
		List<StrategySelectionRow> selectedStratgies = null;
		try {
			// get selected strategies from table
			selectedStratgies = getStrategySelectionTableViewer()
					.getSelectedStratgies();
			// validate and create config
			validateConfiguration(selectedStratgies);
			createConfiguration(selectedStratgies);
		} catch (Exception ex) {
			window.createErrorMessage(ex);
			return false;
		}
		return true;
	}

	public StrategySelectionHelper getStrategySelectionTableViewer() {
		return strategySelectionHelper;
	}

	/**
	 * create the content for configuration
	 */
	private void createContent() {
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.widthHint = 220;

		createGlobalConfigurationContent(gridData);
		createInterOperatorConfigurationContent(gridData);
		createIntraOperatorConfigurationContent(gridData);
	}

	/**
	 * creates the global config
	 * 
	 * @param gridData
	 */
	private void createGlobalConfigurationContent(GridData gridData) {
		// global configuration
		createLabelWithSeperator(this, "Global configuration");
		Composite globalConfigComposite = createDefaultComposite();
		numberOfElementsText = createTextWithLabel(
				globalConfigComposite,
				gridData,
				"Number of elements for analyse: ",
				String.valueOf(ParallelizationBenchmarkerConstants.DEFAULT_NUMBER_OF_ELEMENTS));
		maxExecutionTimeText = createTextWithLabel(
				globalConfigComposite,
				gridData,
				"Maximum time for each analyse in ms: ",
				String.valueOf(ParallelizationBenchmarkerConstants.DEFAULT_MAX_EXECUTION_TIME));
		numberOfExecutionsText = createTextWithLabel(
				globalConfigComposite,
				gridData,
				"Number of executions for each configuration: ",
				String.valueOf(ParallelizationBenchmarkerConstants.DEFAULT_NUMBER_OF_EXECUTIONS));
	}

	/**
	 * creates the config for inter operator parallelization
	 * 
	 * @param gridData
	 */
	private void createInterOperatorConfigurationContent(GridData gridData) {
		// Configuration for inter operator parallelization
		createLabelWithSeperator(this,
				"Configure Inter-operator parallelization");
		useInterOperatorParallelization = createCheckButton(this,
				"Use Inter-Operator Parallelization");
		Composite interOperatorconfigComposite = createDefaultComposite();
		interOperatorDegreeText = createTextWithLabel(
				interOperatorconfigComposite,
				gridData,
				"Degrees (comma-seperated): ",
				ParallelizationBenchmarkerConstants.DEFAULT_INTER_OPERATOR_DEGREES);
		interOperatorBuffersizeText = createTextWithLabel(
				interOperatorconfigComposite,
				gridData,
				"Buffersize: ",
				String.valueOf(InterOperatorParallelizationConstants.DEFAULT_BUFFER_SIZE));
		buffertypeCombo = createComboWithLabel(interOperatorconfigComposite,
				gridData, BUFFER_COMBO, "Select buffer type: ", 0);
		allowPostOptimizationButton = createCheckButton(this,
				"Allow post optimization");
		useThreadedOperatorsButton = createCheckButton(this,
				"Use threaded operators if possible");
		createStrategySelectionTable();
	}

	/**
	 * creates the config for intra operator configuration
	 * 
	 * @param gridData
	 */
	private void createIntraOperatorConfigurationContent(GridData gridData) {
		// Configuration for intra operator parallelization
		createLabelWithSeperator(this,
				"Configure Intra-operator parallelization");
		useIntraOperatorParallelization = createCheckButton(this,
				"Use Intra-Operator Parallelization");
		Composite intraOperatorconfigComposite = createDefaultComposite();
		intraOperatorDegreeText = createTextWithLabel(
				intraOperatorconfigComposite,
				gridData,
				"Degrees (comma-seperated): ",
				ParallelizationBenchmarkerConstants.DEFAULT_INTRA_OPERATOR_DEGREES);
		intraOperatorSelectedOperatorsText = createTextWithLabel(
				intraOperatorconfigComposite, gridData,
				"Only operators with id: ", "");
		intraOperatorBufferSizeText = createTextWithLabel(
				intraOperatorconfigComposite,
				gridData,
				"Buffersize: ",
				String.valueOf(IntraOperatorParallelizationConstants.DEFAULT_BUFFERSIZE));
	}

	/**
	 * creates the table for inter operator parallelization strategies
	 */
	private void createStrategySelectionTable() {
		createLabel(
				this,
				"Following strategies are possible for the selected query. Please select at least one strategy for "
						+ "parallelization. If more than one strategy for one operator is compatible, each strategy \n is "
						+ "benchmarked. Note that executing the benchmark depends one the number of combinations.");

		strategySelectionHelper = new StrategySelectionHelper(this, data);

		GridData selectGridData = new GridData(GridData.FILL_BOTH);

		Composite selectComposite = new Composite(this, SWT.NONE);
		GridLayout selectGridLayout = new GridLayout(2, false);
		selectComposite.setLayout(selectGridLayout);

		// checkbox table view to check and uncheck the different strategies
		final CheckboxTableViewer tableViewer = getStrategySelectionTableViewer()
				.getTableViewer();

		// select all strategies
		Button selectAllButton = new Button(selectComposite, SWT.PUSH);
		selectAllButton.setText("Select all");
		selectAllButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		selectAllButton.setLayoutData(selectGridData);
		selectAllButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableViewer.setAllChecked(true);
			}
		});

		// unselect all streatgies
		Button unselectAllButton = new Button(selectComposite, SWT.PUSH);
		unselectAllButton.setText("Unselect all");
		unselectAllButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		unselectAllButton.setLayoutData(selectGridData);
		unselectAllButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableViewer.setAllChecked(false);
			}
		});
	}

	/**
	 * validate the whole configuration
	 * 
	 * @param selectedStratgies
	 */
	protected void validateConfiguration(
			List<StrategySelectionRow> selectedStratgies) {
		validateGlobalConfiguration();
		validateInterOperatorConfiguration(selectedStratgies);
		validateIntraOperatorConfiguration();
	}

	/**
	 * validate the global configuration
	 */
	private void validateGlobalConfiguration() {
		// maximum execution time
		String maxExecutionTimeString = this.maxExecutionTimeText.getText();
		if (maxExecutionTimeString.isEmpty()) {
			throw new IllegalArgumentException(
					"No maximum execution time definied");
		} else {
			long value = Long.parseLong(maxExecutionTimeString);
			if (value < 1000) {
				throw new IllegalArgumentException(
						"Maximum execution need to be greater equal 1000");
			}
		}

		// number of executions
		String numberOfExecutionString = numberOfExecutionsText.getText();
		if (numberOfExecutionString.isEmpty()) {
			throw new IllegalArgumentException(
					"No number of executions definied");
		} else {
			int value = Integer.parseInt(numberOfExecutionString);
			if (value > 100) {
				throw new IllegalArgumentException(
						"number of executions need to be less equal 100");
			}
		}

		// number of elements
		String numberOfElementsString = this.numberOfElementsText.getText();
		if (numberOfElementsString.isEmpty()) {
			throw new IllegalArgumentException("No number of elements definied");
		} else {
			int value = Integer.parseInt(numberOfElementsString);
			if (value < 1) {
				throw new IllegalArgumentException(
						"Number of elements need to be greater than 0");
			}
		}

		// at least one type of parallelization need to be selected
		if (!useInterOperatorParallelization.getSelection()
				&& !useIntraOperatorParallelization.getSelection()) {
			throw new IllegalArgumentException(
					"You need to enable inter- or intra-operator-parallelization or both.");
		}
	}

	/**
	 * validate inter operator parallelization
	 * 
	 * @param selectedStratgies
	 */
	private void validateInterOperatorConfiguration(
			List<StrategySelectionRow> selectedStratgies) {
		if (useInterOperatorParallelization.getSelection()) {
			// degree values
			List<Integer> degrees = new ArrayList<Integer>();
			String degreeString = this.interOperatorDegreeText.getText();
			if (degreeString.isEmpty()) {
				throw new IllegalArgumentException(
						"No degree for inter operator parallelization definied");
			} else {
				String[] degreeValues = degreeString.trim().split(",");
				for (int i = 0; i < degreeValues.length; i++) {
					degrees.add(Integer.parseInt(degreeValues[i]));
				}
			}

			// buffersize
			String buffersizeString = this.interOperatorBuffersizeText
					.getText();
			if (buffersizeString.isEmpty()) {
				throw new IllegalArgumentException(
						"No buffersize for inter operator parallelization definied");
			} else {
				Integer.parseInt(buffersizeString);
			}

			// selected strategies
			if (selectedStratgies.isEmpty()) {
				throw new IllegalArgumentException("No strategy selected");
			} else {
				for (StrategySelectionRow strategySelectionRow : selectedStratgies) {
					strategySelectionRow.validate(selectedStratgies, degrees);
				}
			}
		}
	}

	/**
	 * validate intra operator parallelization
	 */
	private void validateIntraOperatorConfiguration() {
		if (useIntraOperatorParallelization.getSelection()) {
			// degree values
			List<Integer> degrees = new ArrayList<Integer>();
			String degreeString = this.intraOperatorDegreeText.getText();
			if (degreeString.isEmpty()) {
				throw new IllegalArgumentException(
						"No degree for intra operator parallelization definied");
			} else {
				String[] degreeValues = degreeString.trim().split(",");
				for (int i = 0; i < degreeValues.length; i++) {
					degrees.add(Integer.parseInt(degreeValues[i]));
				}
			}

			// operator ids, check if ids exists in logical plans
			String selectedOperatorString = intraOperatorSelectedOperatorsText
					.getText().trim();
			if (!selectedOperatorString.isEmpty()) {
				String[] splittedOperatorIds = selectedOperatorString
						.split(",");
				for (int i = 0; i < splittedOperatorIds.length; i++) {
					splittedOperatorIds[i] = splittedOperatorIds[i].trim();
					ILogicalOperator logicalOperator = null;
					for (ILogicalQuery logicalQuery : data
							.getBenchmarkInitializationResult()
							.getLogicalQueries()) {
						logicalOperator = LogicalGraphHelper
								.findOperatorWithId(splittedOperatorIds[i],
										logicalQuery.getLogicalPlan().getRoot());
					}
					if (logicalOperator == null) {
						throw new IllegalArgumentException("OperatorId: "
								+ splittedOperatorIds[i] + " is invalid");
					}
				}
			}

			// buffersize
			String buffersizeString = intraOperatorBufferSizeText.getText()
					.trim();
			if (buffersizeString.isEmpty()) {
				throw new IllegalArgumentException(
						"No buffersize for intra operator parallelization defined");
			}
			Integer.parseInt(buffersizeString);

		}
	}

	/**
	 * creates the configuration for benchmarker execution after validation
	 * 
	 * @param selectedStratgies
	 */
	protected void createConfiguration(
			List<StrategySelectionRow> selectedStratgies) {
		BenchmarkerConfiguration configuration = new BenchmarkerConfiguration();

		createGlobalConfiguration(configuration);
		createInterOperatorConfiguration(selectedStratgies, configuration);
		createIntraOperatorConfiguration(configuration);

		data.setConfiguration(configuration);
	}

	/**
	 * set values for global configuration
	 * 
	 * @param configuration
	 */
	private void createGlobalConfiguration(
			BenchmarkerConfiguration configuration) {
		// number of elements to count
		String numberOfElementsString = this.numberOfElementsText.getText();
		configuration.setNumberOfElements(Integer
				.parseInt(numberOfElementsString));

		// maximum execution time
		String maxExecutionTimeString = this.maxExecutionTimeText.getText();
		configuration.setMaximumExecutionTime(Long
				.parseLong(maxExecutionTimeString));

		// number of executions for every configuration
		String numberOfExecutionString = numberOfExecutionsText.getText();
		configuration.setNumberOfExecutions(Integer
				.parseInt(numberOfExecutionString));
	}

	/**
	 * set values for inter operator parallelization if this type is selected
	 * 
	 * @param selectedStratgies
	 * @param configuration
	 */
	private void createInterOperatorConfiguration(
			List<StrategySelectionRow> selectedStratgies,
			BenchmarkerConfiguration configuration) {
		if (useInterOperatorParallelization.getSelection()) {
			configuration.setUseInterOperatorParallelization(true);
			// degrees for inter operator parallelization
			List<Integer> degrees = new ArrayList<Integer>();
			String degreeString = this.interOperatorDegreeText.getText();
			String[] degreeValues = degreeString.trim().split(",");
			for (int i = 0; i < degreeValues.length; i++) {
				degrees.add(Integer.parseInt(degreeValues[i]));
			}
			configuration.setInterOperatorDegrees(degrees);

			// buffersize
			String buffersizeString = this.interOperatorBuffersizeText
					.getText();
			configuration.setInterOperatorBuffersize(Integer
					.parseInt(buffersizeString));

			// buffertype
			setBufferTypeFromCombo(configuration);

			// allow post optimization
			configuration.setAllowPostOptimization(allowPostOptimizationButton
					.getSelection());

			// use threaded operators
			configuration.setUseThreadedOperators(useThreadedOperatorsButton
					.getSelection());

			// set selected strategies
			configuration.setSelectedStratgies(selectedStratgies);
		}
	}

	/**
	 * set values for intra operator parallelization if this type is selected
	 * 
	 * @param configuration
	 */
	private void createIntraOperatorConfiguration(
			BenchmarkerConfiguration configuration) {
		if (useIntraOperatorParallelization.getSelection()) {
			configuration.setUseIntraOperatorParallelization(true);

			// degrees for inter operator parallelization
			List<Integer> degrees = new ArrayList<Integer>();
			String degreeString = this.intraOperatorDegreeText.getText();
			String[] degreeValues = degreeString.trim().split(",");
			for (int i = 0; i < degreeValues.length; i++) {
				degrees.add(Integer.parseInt(degreeValues[i]));
			}
			configuration.setIntraOperatorDegrees(degrees);

			// selected operators
			List<String> operatorIDs = new ArrayList<String>();

			String selectedOperatorString = intraOperatorSelectedOperatorsText
					.getText().trim();
			if (!selectedOperatorString.isEmpty()) {
				String[] splittedOperatorIds = selectedOperatorString
						.split(",");
				for (int i = 0; i < splittedOperatorIds.length; i++) {
					operatorIDs.add(splittedOperatorIds[i]);
				}
				configuration.setSelectedOperators(operatorIDs);
			}

			// buffersize
			configuration.setIntraOperatorBuffersize(Integer
					.parseInt(intraOperatorBufferSizeText.getText()));
		}
	}

	/**
	 * sets the values of the configuration based on the selected values from
	 * buffertype combo
	 * 
	 * @param configuration
	 */
	private void setBufferTypeFromCombo(BenchmarkerConfiguration configuration) {
		String bufferSelection = buffertypeCombo.getText();
		if (bufferSelection.equals(BOTH)) {
			configuration.setUseThreadedBuffer(true);
			configuration.setUseNonThreadedBuffer(true);
		} else if (bufferSelection.equals(THREADED)) {
			configuration.setUseThreadedBuffer(true);
		} else if (bufferSelection.equals(NON_THREADED)) {
			configuration.setUseNonThreadedBuffer(true);
		}
	}
}
