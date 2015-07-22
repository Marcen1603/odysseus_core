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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.parallelization.interoperator.constants.InterOperatorParallelizationConstants;
import de.uniol.inf.is.odysseus.parallelization.rcp.data.BenchmarkDataHandler;
import de.uniol.inf.is.odysseus.parallelization.rcp.data.BenchmarkerConfiguration;
import de.uniol.inf.is.odysseus.parallelization.rcp.windows.ParallelizationBenchmarkerWindow;
import de.uniol.inf.is.odysseus.parallelization.rcp.windows.table.StrategySelectionRow;
import de.uniol.inf.is.odysseus.parallelization.rcp.windows.table.StrategySelectionHelper;

public class BenchmarkConfigureComposite extends AbstractBenchmarkComposite {

	private Text interOperatorDegreeText;
	private Text buffersizeText;
	private Text numberOfElementsText;
	private Button allowPostOptimizationButton;
	private StrategySelectionHelper strategySelectionHelper;
	private Text maxExecutionTimeText;
	private BenchmarkDataHandler data;
	private ParallelizationBenchmarkerWindow window;
	private Combo buffertypeCombo;

	private final String BOTH = "Threaded & Non-Threaded Buffer";
	private final String THREADED = "Only Threaded Buffer";
	private final String NON_THREADED = "Only Non-Threaded Buffer";
	private String[] BUFFER_COMBO = { BOTH, THREADED, NON_THREADED };
	private Text numberOfExecutionsText;
	private Button useIntraOperatorParallelization;
	private Button useInterOperatorParallelization;
	private Text intraOperatorDegreeText;

	public BenchmarkConfigureComposite(Composite parent, int style,
			int windowWidth, UUID benchmarkProcessId,
			ParallelizationBenchmarkerWindow window) {
		super(parent, style);
		this.window = window;

		this.data = BenchmarkDataHandler
				.getExistingInstance(benchmarkProcessId);
		if (data.getBenchmarkInitializationResult().getStrategiesForOperator()
				.isEmpty()) {
			window.createErrorMessage(new Exception(
					"Running Benchmark not possible. No operator for parallelization found. \n Maybe you need to add unique ids to operators you want to parallelize."));
			return;
		}

		GridData contentGridData = new GridData(GridData.FILL_BOTH);
		contentGridData.widthHint = windowWidth;
		this.setLayoutData(contentGridData);
		GridLayout gridLayout = new GridLayout();
		this.setLayout(gridLayout);

		createContent();

		parent.pack();
		parent.setVisible(true);
	}

	private void createContent() {
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.widthHint = 220;

		createLabelWithSeperator(this, "Global configuration");

		Composite globalConfigComposite = new Composite(this, SWT.NONE);
		GridLayout globalConfigGridLayout = new GridLayout(2, false);
		globalConfigComposite.setLayout(globalConfigGridLayout);

		Label numberOfElementsLabel = new Label(globalConfigComposite, SWT.NULL);
		numberOfElementsLabel.setText("Number of elements for analyse: ");
		numberOfElementsText = new Text(globalConfigComposite, SWT.SINGLE
				| SWT.BORDER);
		numberOfElementsText.setText(String
				.valueOf(BenchmarkerConfiguration.DEFAULT_NUMBER_OF_ELEMENTS));
		numberOfElementsText.setLayoutData(gridData);

		Label maxExecutionTimeLabel = new Label(globalConfigComposite, SWT.NULL);
		maxExecutionTimeLabel.setText("Maximum time for each analyse in ms: ");
		maxExecutionTimeText = new Text(globalConfigComposite, SWT.SINGLE
				| SWT.BORDER);
		maxExecutionTimeText.setText(String
				.valueOf(BenchmarkerConfiguration.DEFAULT_MAX_EXECUTION_TIME));
		maxExecutionTimeText.setLayoutData(gridData);

		Label numberOfExecutionsLabel = new Label(globalConfigComposite,
				SWT.NULL);
		numberOfExecutionsLabel
				.setText("Number of executions for each configuration: ");
		numberOfExecutionsText = new Text(globalConfigComposite, SWT.SINGLE
				| SWT.BORDER);
		numberOfExecutionsText
				.setText(String
						.valueOf(BenchmarkerConfiguration.DEFAULT_NUMBER_OF_EXECUTIONS));
		numberOfExecutionsText.setLayoutData(gridData);

		createLabelWithSeperator(this,
				"Configure Inter-operator parallelization");

		useInterOperatorParallelization = new Button(this, SWT.CHECK);
		useInterOperatorParallelization
				.setText("Use Inter-Operator Parallelization");
		useInterOperatorParallelization.setSelection(true);

		Composite interOperatorconfigComposite = new Composite(this, SWT.NONE);
		GridLayout interOperatorGridLayout = new GridLayout(2, false);
		interOperatorconfigComposite.setLayout(interOperatorGridLayout);

		Label degreeLabel = new Label(interOperatorconfigComposite, SWT.NULL);
		degreeLabel.setText("Degrees (comma-seperated): ");
		interOperatorDegreeText = new Text(interOperatorconfigComposite,
				SWT.SINGLE | SWT.BORDER);
		interOperatorDegreeText.setText("2,4,8");
		interOperatorDegreeText.setLayoutData(gridData);

		Label buffersizeLabel = new Label(interOperatorconfigComposite,
				SWT.NULL);
		buffersizeLabel.setText("Buffersize: ");
		buffersizeText = new Text(interOperatorconfigComposite, SWT.SINGLE
				| SWT.BORDER);
		buffersizeText.setText(String
				.valueOf(InterOperatorParallelizationConstants.AUTO_BUFFER_SIZE));
		buffersizeText.setLayoutData(gridData);

		Label selectBufferType = new Label(interOperatorconfigComposite,
				SWT.NULL);
		selectBufferType.setText("Select buffer type: ");
		buffertypeCombo = new Combo(interOperatorconfigComposite, SWT.READ_ONLY);
		buffertypeCombo.setItems(BUFFER_COMBO);
		buffertypeCombo.select(0);
		buffertypeCombo.setLayoutData(gridData);

		allowPostOptimizationButton = new Button(this, SWT.CHECK);
		allowPostOptimizationButton.setText("Allow post optimization");
		allowPostOptimizationButton.setSelection(true);

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

		final CheckboxTableViewer tableViewer = getStrategySelectionTableViewer()
				.getTableViewer();

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

		createLabelWithSeperator(this,
				"Configure Intra-operator parallelization");

		useIntraOperatorParallelization = new Button(this, SWT.CHECK);
		useIntraOperatorParallelization
				.setText("Use Intra-Operator Parallelization");
		useIntraOperatorParallelization.setSelection(true);

		Composite intraOperatorconfigComposite = new Composite(this, SWT.NONE);
		GridLayout intraOperatorGridLayout = new GridLayout(2, false);
		intraOperatorconfigComposite.setLayout(intraOperatorGridLayout);

		Label intraOperatorDegreeLabel = new Label(
				intraOperatorconfigComposite, SWT.NULL);
		intraOperatorDegreeLabel.setText("Degrees (comma-seperated): ");

		intraOperatorDegreeText = new Text(intraOperatorconfigComposite,
				SWT.SINGLE | SWT.BORDER);
		intraOperatorDegreeText.setText("2,4,8");
		intraOperatorDegreeText.setLayoutData(gridData);

	}

	protected void validateConfiguration(
			List<StrategySelectionRow> selectedStratgies) {
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

		if (!useInterOperatorParallelization.getSelection() && !useIntraOperatorParallelization.getSelection()){
			throw new IllegalArgumentException(
					"You need to enable inter- or intra-operator-parallelization or both.");
		}
		
		if (useInterOperatorParallelization.getSelection()) {
			// degree values
			List<Integer> degrees = new ArrayList<Integer>();
			String degreeString = this.interOperatorDegreeText.getText();
			if (degreeString.isEmpty()) {
				throw new IllegalArgumentException("No degree definied");
			} else {
				String[] degreeValues = degreeString.trim().split(",");
				for (int i = 0; i < degreeValues.length; i++) {
					degrees.add(Integer.parseInt(degreeValues[i]));
				}
			}

			// buffersize
			String buffersizeString = this.buffersizeText.getText();
			if (buffersizeString.isEmpty()) {
				throw new IllegalArgumentException("No buffersize definied");
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

		if (useIntraOperatorParallelization.getSelection()) {
			// degree values
			List<Integer> degrees = new ArrayList<Integer>();
			String degreeString = this.intraOperatorDegreeText.getText();
			if (degreeString.isEmpty()) {
				throw new IllegalArgumentException("No degree definied");
			} else {
				String[] degreeValues = degreeString.trim().split(",");
				for (int i = 0; i < degreeValues.length; i++) {
					degrees.add(Integer.parseInt(degreeValues[i]));
				}
			}
		}
	}

	protected void createConfiguration(
			List<StrategySelectionRow> selectedStratgies) {
		BenchmarkerConfiguration configuration = new BenchmarkerConfiguration();

		String numberOfElementsString = this.numberOfElementsText.getText();
		configuration.setNumberOfElements(Integer
				.parseInt(numberOfElementsString));

		String maxExecutionTimeString = this.maxExecutionTimeText.getText();
		configuration.setMaximumExecutionTime(Long
				.parseLong(maxExecutionTimeString));

		String numberOfExecutionString = numberOfExecutionsText.getText();
		configuration.setNumberOfExecutions(Integer
				.parseInt(numberOfExecutionString));

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
			String buffersizeString = this.buffersizeText.getText();
			configuration.setBuffersize(Integer.parseInt(buffersizeString));

			// buffertype
			setBufferTypeFromCombo(configuration);

			// allow post optimization
			boolean allowPostOptimization = allowPostOptimizationButton
					.getSelection();
			configuration.setAllowPostOptimization(allowPostOptimization);

			// set selected strategies
			configuration.setSelectedStratgies(selectedStratgies);
		}

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
		}

		data.setConfiguration(configuration);
	}

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

	public boolean prepareParallelizationAnalysis() {
		List<StrategySelectionRow> selectedStratgies = null;
		try {
			selectedStratgies = getStrategySelectionTableViewer()
					.getSelectedStratgies();
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
}
