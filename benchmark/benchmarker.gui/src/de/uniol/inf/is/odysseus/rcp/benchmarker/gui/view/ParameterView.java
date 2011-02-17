/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

/**
 * Diese Klasse zeichnet die View für die Parameter eines Benchmarkdurchlaufs.
 * 
 * @author Steffi
 * 
 */
public class ParameterView extends ViewPart {

	// private BenchmarkParam benchmarkRun;

	private Text textQuery;
	private Button checkButtonPriority;
	private Button checkButtonPunctuations;
	private Button checkButtonExtendedPostpriorisation;
	private Button checkButtonMemoryUsage;
	private Button checkButtonNoMetadata;
	private Button checkButtonResultPerQuery;
	private Label labelPageName;
	private int counter = 1;

	public ParameterView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {

		GridLayout gridLayout = new GridLayout(2, false);
		parent.setLayout(gridLayout);
		GridData gridData = new GridData();

		{ // SeitenLabel
			Label labelPage = new Label(parent, SWT.NULL);
			labelPage.setText("    ");

			labelPageName = new Label(parent, SWT.None);
			labelPageName.setText("Settings " + counter);
		}
		{ // Name_Of_The_Benchmarkrun
			Label labelNameBenchmarkrun = new Label(parent, SWT.NULL);
			labelNameBenchmarkrun.setText("Name of Benchmarkrun: ");

			Label labelNameBenchmarkrunName = new Label(parent, SWT.NULL);
			labelNameBenchmarkrunName.setText("Name of Benchmarkrun: ");
		}

		{ // Scheduler
			Label labelScheduler = new Label(parent, SWT.NULL);
			labelScheduler.setText("Scheduler: ");

			Label labelSchedulerName = new Label(parent, SWT.NULL);
			labelSchedulerName.setText("Scheduler: ");
		}

		{ // Schedulingstrategy
			Label labelSchedulingStrategy = new Label(parent, SWT.NULL);
			labelSchedulingStrategy.setText("Schedulingstrategy: ");

			Label labelSchedulingStrategyName = new Label(parent, SWT.NULL);
			labelSchedulingStrategyName.setText("Schedulingstrategy: ");
		}

		{ // Buffer_Placement
			Label labelBufferPlacement = new Label(parent, SWT.NULL);
			labelBufferPlacement.setText("Bufferplacement: ");

			Label labelBufferPlacementName = new Label(parent, SWT.NULL);
			labelBufferPlacementName.setText("Bufferplacement: ");
		}

		{ // Data_Type
			Label labelDataType = new Label(parent, SWT.NULL);
			labelDataType.setText("Data Type: ");

			Label labelDataTypeName = new Label(parent, SWT.NULL);
			labelDataTypeName.setText("Data Type: ");
		}

		{ // Metadata_Types
			Label labelMetadata_Types = new Label(parent, SWT.NULL);
			labelMetadata_Types.setText("Metadata Types: ");

			Label labelMetadata_TypesName = new Label(parent, SWT.NULL);
			labelMetadata_TypesName.setText("Metadata Types: ");
		}

		{ // Query_Language
			Label labelQueryLanguage = new Label(parent, SWT.NULL);
			labelQueryLanguage.setText("Query Language: ");

			Label labelQueryLanguageName = new Label(parent, SWT.NULL);
			labelQueryLanguageName.setText("Query Language: ");
		}

		{ // Query
			Label labelQuery = new Label(parent, SWT.NULL);
			labelQuery.setText("Query ");

			gridData = new GridData(GridData.FILL_BOTH);
			textQuery = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
			textQuery.setLayoutData(gridData);
			textQuery.setEnabled(false);
		}

		{ // Max_Results
			Label labelMaxResults = new Label(parent, SWT.NULL);
			labelMaxResults.setText("Stop after results: ");

			Label labelMaxResultsName = new Label(parent, SWT.NULL);
			labelMaxResultsName.setText("Stop after results: ");
		}

		{ // Priority
			Label labelPriority = new Label(parent, SWT.NULL);
			labelPriority.setText("Priority ");

			checkButtonPriority = new Button(parent, SWT.CHECK);
			checkButtonPriority.setEnabled(false);
		}

		{ // Punctuation
			Label labelPunctuations = new Label(parent, SWT.NULL);
			labelPunctuations.setText("Punctuations ");

			checkButtonPunctuations = new Button(parent, SWT.CHECK);
			checkButtonPunctuations.setEnabled(false);
		}

		{ // Extended_Postpriorisation
			Label labelExtendedPostpriorisation = new Label(parent, SWT.NULL);
			labelExtendedPostpriorisation.setText("Extended Postpriorisation ");

			checkButtonExtendedPostpriorisation = new Button(parent, SWT.CHECK);
			checkButtonExtendedPostpriorisation.setEnabled(false);
		}

		{ // Memory_Usage
			Label labelMemoryUsage = new Label(parent, SWT.NULL);
			labelMemoryUsage.setText("Memory Usage ");

			checkButtonMemoryUsage = new Button(parent, SWT.CHECK);
			checkButtonMemoryUsage.setEnabled(false);
		}

		{ // No_Metadata
			Label labelNoMetadata = new Label(parent, SWT.NULL);
			labelNoMetadata.setText("No Metadata ");

			checkButtonNoMetadata = new Button(parent, SWT.CHECK);
			checkButtonNoMetadata.setEnabled(false);
		}

		{ // Wait_Config
			Label labelWaitConfig = new Label(parent, SWT.NULL);
			labelWaitConfig.setText("Wait Configuration: ");

			Label labelWaitConfigName = new Label(parent, SWT.NULL);
			labelWaitConfigName.setText("Wait Configuration: ");
		}

		{ // Result_Per_Query
			Label labelResultPerQuery = new Label(parent, SWT.NULL);
			labelResultPerQuery.setText("Result per Query ");

			checkButtonResultPerQuery = new Button(parent, SWT.CHECK);
			checkButtonResultPerQuery.setEnabled(false);
		}

		{ // Input_File
			Label labelInputFile = new Label(parent, SWT.NULL);
			labelInputFile.setText("Input File: ");

			Label labelInputFileName = new Label(parent, SWT.NULL);
			labelInputFileName.setText("Input File: ");
		}

		{ // Number_Of_Runs
			Label labelNumberOfRuns = new Label(parent, SWT.NULL);
			labelNumberOfRuns.setText("Number of runs: ");

			Label labelNumberOfRunsName = new Label(parent, SWT.NULL);
			labelNumberOfRunsName.setText("Number of runs: ");
		}
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}
}
