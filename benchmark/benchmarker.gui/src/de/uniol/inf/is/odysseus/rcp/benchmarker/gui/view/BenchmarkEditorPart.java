package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.view;

import static de.uniol.inf.is.odysseus.rcp.benchmarker.utils.StringUtils.isNotBlank;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.EditorPart;

import de.uniol.inf.is.odysseus.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.Activator;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller.TextboxVerifier;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller.commands.OpenBenchmarkHandler;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.Benchmark;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkHolder;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkParam;
import de.uniol.inf.is.odysseus.rcp.benchmarker.utils.BenchmarkStoreUtil;
import de.uniol.inf.is.odysseus.rcp.benchmarker.utils.ObservativeMapEntryValue;
import de.uniol.inf.is.odysseus.rcp.benchmarker.utils.OdysseusBenchmarkUtil;

/**
 * Diese Klasse zeichnet den Editor, mit dem man Benchmarkparameter einstellen
 * kann.
 * 
 * @author Stefanie Witzke
 * 
 */
public class BenchmarkEditorPart extends EditorPart implements ISaveablePart, PropertyChangeListener, SelectionListener {

	public static final String ID = "de.uniol.inf.is.odysseus.rcp.benchmarker.gui.editorBenchmark";

	private BenchmarkParam benchmarkParam;
	private Text textNameBenchmark;
	private Combo comboScheduler;
	private Combo comboSchedulingstrategy;
	private Combo comboBufferPlacement;
	private Text textDataType;
	// private Combo comboMetadataTypes;
	private Combo comboQueryLanguage;
	private Text textQuery;
	private Text textMaxResults;
	private Button checkButtonPriority;
	private Button checkButtonPunctuations;
	private Button checkButtonExtendedPostpriorisation;
	private Button checkButtonMemoryUsage;
	private Button checkButtonNoMetadata;
	private Text textWaitConfig;
	private Button checkButtonResultPerQuery;
	private Text textInputFile;// browser
	private Text textNumberOfRuns;
	private Label labelPageName;
	// private Button buttonAdd;
	private Button buttonStart;
	private Button buttonCopy;
	private Button buttonBrowser;
	private boolean isDirty;

	public BenchmarkEditorPart() {
		isDirty = false;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		boolean newBench = !BenchmarkHolder.INSTANCE.contains(benchmarkParam.getId());
		Benchmark benchmark = BenchmarkStoreUtil.storeBenchmarkParam(benchmarkParam);

		setDirtyState(false);

		// Den ProjectView refreshen
		ProjectView projectView = (ProjectView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(ProjectView.ID);
		if (!newBench) {
			projectView.refresh(benchmark);
		} else {
			projectView.refresh();
		}
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if (!(input instanceof BenchmarkEditorInput))
			throw new PartInitException("Invalid Input: Must be BenchmarkEditorInput");
		setSite(site);
		setInput(input);
		this.benchmarkParam = ((BenchmarkEditorInput) input).getBenchmarkParam();
		IActionBars actionBars = ((IEditorSite) getSite()).getActionBars();

		// Speicher-Aktionen registrieren
		actionBars.setGlobalActionHandler("org.eclipse.ui.file.save",
				ActionFactory.SAVE.create(getSite().getWorkbenchWindow()));
		actionBars.setGlobalActionHandler("org.eclipse.ui.file.saveAll",
				ActionFactory.SAVE_ALL.create(getSite().getWorkbenchWindow()));

		Set<Set<String>> allTypeCombination = MetadataRegistry.getAvailableMetadataCombinations();
		for (Set<String> typeCombinations : allTypeCombination) {
			for (String type : typeCombinations) {
				if (!benchmarkParam.getAllSingleTypes().containsKey(type)) {
					benchmarkParam.getAllSingleTypes().put(type, false);
				}
			}
		}
	}

	@Override
	public boolean isDirty() {
		return isDirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void setFocus() {
	}

	@Override
	protected void setPartName(String partName) {
	}

	@Override
	public String getTitleToolTip() {
		return getPartName();
	}

	@Override
	public String getPartName() {
		return "Editor: " + textNameBenchmark.getText();
	}

	@Override
	public void dispose() {
		benchmarkParam.removePropertyChangeListener(this);
		super.dispose();
	}

	@Override
	public void createPartControl(Composite parent) {
		DataBindingContext bindingContext = new DataBindingContext();
		GridLayout gridLayout = new GridLayout(3, false);
		parent.setLayout(gridLayout);
		GridData gridData = new GridData();

		/*
		 * // MetadataCheckboxListener SelectionListener
		 * metadataSelectionListener = new SelectionAdapter() { public void
		 * widgetSelected(SelectionEvent e) { String element = (String)
		 * ((Button) e.getSource()).getData(); if
		 * (selectedTypes.contains(element)) { selectedTypes.remove(element); }
		 * else { selectedTypes.add(element); } } };
		 */

		{ // SeitenLabel
			Label labelPage = new Label(parent, SWT.NULL);
			labelPage.setText("    ");

			labelPageName = new Label(parent, SWT.None);
			labelPageName.setText("Settings " + benchmarkParam.getId());
			new Label(parent, SWT.NULL);
		}

		{ // Name_Of_The_Benchmark
			Label labelNameBenchmarkrun = new Label(parent, SWT.NULL);
			labelNameBenchmarkrun.setText("Name of Benchmarkrun: ");

			gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.grabExcessHorizontalSpace = true;
			textNameBenchmark = new Text(parent, SWT.SINGLE | SWT.BORDER);
			textNameBenchmark.setLayoutData(gridData);
			new Label(parent, SWT.NULL);
			bindingContext.bindValue(SWTObservables.observeText(textNameBenchmark, SWT.Modify),
					BeansObservables.observeValue(benchmarkParam, "name"));
		}

		{ // Scheduler
			Label labelScheduler = new Label(parent, SWT.NULL);
			labelScheduler.setText("Scheduler: ");

			gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.grabExcessHorizontalSpace = true;
			comboScheduler = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.BORDER);
			comboScheduler.setLayoutData(gridData);
			String[] schedulerName = Activator.getDefault().getSchedulerServices();
			if (schedulerName != null) {
				for (int i = 0; i < schedulerName.length; i++) {
					comboScheduler.add(schedulerName[i]);
				}
			} else {
				comboScheduler.add("No one available");
			}
			new Label(parent, SWT.NULL);
			bindingContext.bindValue(SWTObservables.observeSelection(comboScheduler),
					BeansObservables.observeValue(benchmarkParam, "scheduler"));
		}

		{ // Schedulingstrategy
			Label labelSchedulingStrategy = new Label(parent, SWT.NULL);
			labelSchedulingStrategy.setText("Schedulingstrategy: ");

			gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.grabExcessHorizontalSpace = true;
			comboSchedulingstrategy = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.BORDER);
			comboSchedulingstrategy.setLayoutData(gridData);
			String[] schedulingstrategyName = Activator.getDefault().getSchedulingstrategyServices();
			if (schedulingstrategyName != null) {
				for (int i = 0; i < schedulingstrategyName.length; i++) {
					comboSchedulingstrategy.add(schedulingstrategyName[i]);
				}
			} else {
				comboSchedulingstrategy.add("No one available");
			}
			new Label(parent, SWT.NULL);
			bindingContext.bindValue(SWTObservables.observeSelection(comboSchedulingstrategy),
					BeansObservables.observeValue(benchmarkParam, "schedulingstrategy"));
		}

		{ // Buffer_Placement
			Label labelBufferPlacement = new Label(parent, SWT.NULL);
			labelBufferPlacement.setText("Bufferplacement: ");

			gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.grabExcessHorizontalSpace = true;
			comboBufferPlacement = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.BORDER);
			comboBufferPlacement.setLayoutData(gridData);
			String[] bufferplacementstrategyName = Activator.getDefault().getBufferplacementstrategyServices();
			if (bufferplacementstrategyName != null) {
				for (int i = 0; i < bufferplacementstrategyName.length; i++) {
					comboBufferPlacement.add(bufferplacementstrategyName[i]);
				}
			} else {
				comboBufferPlacement.add("No one available");
			}
			new Label(parent, SWT.NULL);
			bindingContext.bindValue(SWTObservables.observeSelection(comboBufferPlacement),
					BeansObservables.observeValue(benchmarkParam, "bufferplacement"));
		}

		{ // Data_Type
			Label labelDataType = new Label(parent, SWT.NULL);
			labelDataType.setText("Data Type: ");

			gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.grabExcessHorizontalSpace = true;
			textDataType = new Text(parent, SWT.SINGLE | SWT.BORDER);
			textDataType.setLayoutData(gridData);
			new Label(parent, SWT.NULL);
			bindingContext.bindValue(SWTObservables.observeText(textDataType, SWT.Modify),
					BeansObservables.observeValue(benchmarkParam, "dataType"));
		}

		{ // Metadata_Types
			Label labelMetadata_Types = new Label(parent, SWT.NULL);
			labelMetadata_Types.setText("Metadata Types: ");

			gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.grabExcessHorizontalSpace = true;
			// comboMetadataTypes = new Combo(parent, SWT.DROP_DOWN |
			// SWT.READ_ONLY | SWT.BORDER);
			// comboMetadataTypes.setLayoutData(gridData);
			// for (int i = 0; i < 3; i++) {
			// comboMetadataTypes.add("Metadata Type " + i);
			// }
			Label labelmetadata = new Label(parent, SWT.NULL);
			labelmetadata.setText("Select Combination: ");
			new Label(parent, SWT.NULL);
			// bindingContext.bindValue(SWTObservables.observeSelection(comboMetadataTypes),
			// BeansObservables.observeValue(benchmarkParam, "metadataTypes"));

			// TODO
			// /echte metadaten:
			Map<String, Boolean> allSingleTypes = benchmarkParam.getAllSingleTypes();
			for (Map.Entry<String, Boolean> typeEntry : allSingleTypes.entrySet()) {
				new Label(parent, SWT.NULL);
				Button checkboxMetadataType = new Button(parent, SWT.CHECK);
				// checkboxMetadataType.addSelectionListener(metadataSelectionListener);
				checkboxMetadataType.setText(splitString(typeEntry.getKey()));
				checkboxMetadataType.addSelectionListener(this);
				bindingContext.bindValue(SWTObservables.observeSelection(checkboxMetadataType),
						new ObservativeMapEntryValue<String, Boolean>(typeEntry));

				new Label(parent, SWT.NULL);
			}
		}

		{ // Query_Language
			Label labelQueryLanguage = new Label(parent, SWT.NULL);
			labelQueryLanguage.setText("Query Language: ");

			gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.grabExcessHorizontalSpace = true;
			comboQueryLanguage = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.BORDER);
			comboQueryLanguage.setLayoutData(gridData);

			String[] queryLanguageName = Activator.getDefault().getQueryLanguageServices();
			if (queryLanguageName != null) {
				for (int i = 0; i < queryLanguageName.length; i++) {
					comboQueryLanguage.add(queryLanguageName[i]);
				}
			} else {
				comboQueryLanguage.add("No one available");
			}
			new Label(parent, SWT.NULL);
			bindingContext.bindValue(SWTObservables.observeSelection(comboQueryLanguage),
					BeansObservables.observeValue(benchmarkParam, "queryLanguage"));
		}

		{ // Query
			Label labelQuery = new Label(parent, SWT.NULL);
			labelQuery.setText("Query ");

			gridData = new GridData(GridData.FILL_BOTH);
			textQuery = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
			textQuery.setLayoutData(gridData);
			new Label(parent, SWT.NULL);
			bindingContext.bindValue(SWTObservables.observeText(textQuery, SWT.Modify),
					BeansObservables.observeValue(benchmarkParam, "query"));
		}

		{ // Max_Results
			Label labelMaxResults = new Label(parent, SWT.NULL);
			labelMaxResults.setText("Stop after results: ");

			gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.grabExcessHorizontalSpace = true;
			textMaxResults = new Text(parent, SWT.SINGLE | SWT.BORDER);
			textMaxResults.setLayoutData(gridData);
			new Label(parent, SWT.NULL);
			bindingContext.bindValue(SWTObservables.observeText(textMaxResults, SWT.Modify),
					BeansObservables.observeValue(benchmarkParam, "maxResult"));
		}

		{ // Priority
			Label labelPriority = new Label(parent, SWT.NULL);
			labelPriority.setText("Priority ");

			checkButtonPriority = new Button(parent, SWT.CHECK);
			new Label(parent, SWT.NULL);
			bindingContext.bindValue(SWTObservables.observeSelection(checkButtonPriority),
					BeansObservables.observeValue(benchmarkParam, "priority"));
		}

		{ // Punctuation
			Label labelPunctuations = new Label(parent, SWT.NULL);
			labelPunctuations.setText("Punctuations ");

			checkButtonPunctuations = new Button(parent, SWT.CHECK);
			new Label(parent, SWT.NULL);
			bindingContext.bindValue(SWTObservables.observeSelection(checkButtonPunctuations),
					BeansObservables.observeValue(benchmarkParam, "punctuations"));
		}

		{ // Extended_Postpriorisation
			Label labelExtendedPostpriorisation = new Label(parent, SWT.NULL);
			labelExtendedPostpriorisation.setText("Extended Postpriorisation ");

			checkButtonExtendedPostpriorisation = new Button(parent, SWT.CHECK);
			if (checkButtonPriority.getSelection() == false) {
				checkButtonExtendedPostpriorisation.setEnabled(false);
			}
			new Label(parent, SWT.NULL);
			bindingContext.bindValue(SWTObservables.observeSelection(checkButtonExtendedPostpriorisation),
					BeansObservables.observeValue(benchmarkParam, "extendesPostpriorisation"));
		}

		{ // Memory_Usage
			Label labelMemoryUsage = new Label(parent, SWT.NULL);
			labelMemoryUsage.setText("Memory Usage ");

			checkButtonMemoryUsage = new Button(parent, SWT.CHECK);
			new Label(parent, SWT.NULL);
			bindingContext.bindValue(SWTObservables.observeSelection(checkButtonMemoryUsage),
					BeansObservables.observeValue(benchmarkParam, "memoryUsage"));
		}

		{ // No_Metadata
			Label labelNoMetadata = new Label(parent, SWT.NULL);
			labelNoMetadata.setText("No Metadata ");

			checkButtonNoMetadata = new Button(parent, SWT.CHECK);
			new Label(parent, SWT.NULL);
			bindingContext.bindValue(SWTObservables.observeSelection(checkButtonNoMetadata),
					BeansObservables.observeValue(benchmarkParam, "noMetada"));
		}

		{ // Wait_Config
			Label labelDataType = new Label(parent, SWT.NULL);
			labelDataType.setText("Wait Configuration: ");

			gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.grabExcessHorizontalSpace = true;
			textWaitConfig = new Text(parent, SWT.SINGLE | SWT.BORDER);
			textWaitConfig.setLayoutData(gridData);
			new Label(parent, SWT.NULL);
			bindingContext.bindValue(SWTObservables.observeText(textWaitConfig, SWT.Modify),
					BeansObservables.observeValue(benchmarkParam, "waitConfig"));
		}

		{ // Result_Per_Query
			Label labelResultPerQuery = new Label(parent, SWT.NULL);
			labelResultPerQuery.setText("Result per Query ");

			checkButtonResultPerQuery = new Button(parent, SWT.CHECK);
			checkButtonResultPerQuery.setEnabled(false);
			new Label(parent, SWT.NULL);
			bindingContext.bindValue(SWTObservables.observeSelection(checkButtonResultPerQuery),
					BeansObservables.observeValue(benchmarkParam, "resultPerQuery"));

		}

		{ // Input_File
			Label labelInputFile = new Label(parent, SWT.NULL);
			labelInputFile.setText("Input File: ");

			gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.grabExcessHorizontalSpace = true;
			gridData.horizontalAlignment = GridData.FILL;
			textInputFile = new Text(parent, SWT.SINGLE | SWT.BORDER);
			textInputFile.setLayoutData(gridData);

			buttonBrowser = new Button(parent, SWT.NONE);
			buttonBrowser.setText("Browse");
			buttonBrowser.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
				}

				// Browserfenster
				public void widgetSelected(SelectionEvent e) {
					FileDialog dlg = new FileDialog(buttonBrowser.getShell(), SWT.OPEN);
					dlg.setText("Open");
					String path = dlg.open();
					if (path == null)
						return;
					textInputFile.setText(path);
				}
			});
			bindingContext.bindValue(SWTObservables.observeText(textInputFile, SWT.Modify),
					BeansObservables.observeValue(benchmarkParam, "inputFile"));
		}

		{ // Number_Of_Runs
			Label labelNumberOfRuns = new Label(parent, SWT.NULL);
			labelNumberOfRuns.setText("Number of runs: ");

			gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.grabExcessHorizontalSpace = true;
			textNumberOfRuns = new Text(parent, SWT.SINGLE | SWT.BORDER);
			textNumberOfRuns.setLayoutData(gridData);
			new Label(parent, SWT.NULL);
			bindingContext.bindValue(SWTObservables.observeText(textNumberOfRuns, SWT.Modify),
					BeansObservables.observeValue(benchmarkParam, "numberOfRuns"));
		}

		{ // Next_Save_-Buttons
			gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.grabExcessHorizontalSpace = true;
			/*
			 * buttonAdd = new Button(parent, SWT.PUSH);
			 * buttonAdd.setText("Add Settings"); buttonAdd.setEnabled(false);
			 */
			buttonCopy = new Button(parent, SWT.PUSH);
			buttonCopy.setText("Copy Settings as New");
			buttonStart = new Button(parent, SWT.PUSH);
			buttonStart.setText("Start Benchmarker");
			buttonStart.setEnabled(false);
		}

		/*
		 * // Buttonlistener - NEXT buttonAdd.addSelectionListener(new
		 * SelectionAdapter() {
		 * 
		 * @Override public void widgetSelected(SelectionEvent e) { } });
		 */

		buttonCopy.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				OpenBenchmarkHandler.copyAndOpenBenchmark(benchmarkParam);
			}
		});

		// Buttonlistener - SAVE
		buttonStart.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (!checkMetadataCombination()) {
				} else {
					OdysseusBenchmarkUtil util = new OdysseusBenchmarkUtil(BenchmarkHolder.INSTANCE);
					try {
						util.startrun();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		// Abhängigkeit von Extenden_Postpriorisation zu Priority
		checkButtonPriority.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (checkButtonPriority.getSelection() == false) {
					checkButtonExtendedPostpriorisation.setEnabled(false);
					checkButtonExtendedPostpriorisation.setSelection(false);
				} else {
					checkButtonExtendedPostpriorisation.setEnabled(true);
				}
			}
		});

		// Abhängigkeit von Result_Per_Query zu Max_Result
		textMaxResults.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent event) {
				String text = ((Text) event.widget).getText();

				if (text.isEmpty()) {
					checkButtonResultPerQuery.setSelection(false);
					checkButtonResultPerQuery.setEnabled(false);
				} else {
					checkButtonResultPerQuery.setEnabled(true);
				}

				// Testen
				System.out.println(benchmarkParam.getDataType());
				System.out.println(benchmarkParam.getScheduler());
				System.out.println(benchmarkParam.getSchedulingstrategy());
			}
		});

		// In Max_Result, Wait_Config und Number_Of_Runs dürfen nur Zahlen
		// stehen
		final TextboxVerifier textboxVerifyListener = new TextboxVerifier();
		textMaxResults.addVerifyListener(textboxVerifyListener);
		textWaitConfig.addVerifyListener(textboxVerifyListener);
		textNumberOfRuns.addVerifyListener(textboxVerifyListener);

		benchmarkParam.addPropertyChangeListener(this);

		checkEditorMode();
		// ueberpruefe ob die Buttons aktiviert/deaktiviert sein muessen.
		propertyChange(new PropertyChangeEvent(benchmarkParam, null, null, null));
		setDirtyState(false);
	}

	/**
	 * Diese Methode überprüft, ob die gespeicherten Parametereinstellungen
	 * geändert werden dürfen und setzt sie ggf. auf Enabled(false)
	 */
	public void checkEditorMode() {
		boolean readOnly = benchmarkParam.isReadOnly();
		textNameBenchmark.setEnabled(!readOnly);
		comboScheduler.setEnabled(!readOnly);
		comboSchedulingstrategy.setEnabled(!readOnly);
		comboBufferPlacement.setEnabled(!readOnly);
		textDataType.setEnabled(!readOnly);
		// comboMetadataTypes.setEnabled(!readOnly);
		comboQueryLanguage.setEnabled(!readOnly);
		textQuery.setEnabled(!readOnly);
		textMaxResults.setEnabled(!readOnly);
		checkButtonPriority.setEnabled(!readOnly);
		checkButtonPunctuations.setEnabled(!readOnly);
		checkButtonExtendedPostpriorisation.setEnabled(!readOnly);
		checkButtonMemoryUsage.setEnabled(!readOnly);
		checkButtonNoMetadata.setEnabled(!readOnly);
		textWaitConfig.setEnabled(!readOnly);
		checkButtonResultPerQuery.setEnabled(!readOnly);
		textInputFile.setEnabled(!readOnly);
		textNumberOfRuns.setEnabled(!readOnly);
		labelPageName.setEnabled(!readOnly);
		// buttonAdd.setVisible(!readOnly);
		buttonStart.setVisible(!readOnly);
		buttonBrowser.setVisible(!readOnly);
	}

	/**
	 * Wenn eine Checkbox der Metadaten angeklickt wird
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		if (!isDirty) {
			setDirtyState(true);
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	/**
	 * Wenn sich die Eingaben verändern
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (!isDirty) {
			setDirtyState(true);
		}

		BenchmarkParam benchmarkRun = ((BenchmarkParam) evt.getSource());
		if (!BenchmarkHolder.INSTANCE.contains(benchmarkRun.getId())
				&& isNotBlank(benchmarkRun.getName(), benchmarkRun.getScheduler(),
						benchmarkRun.getSchedulingstrategy(), benchmarkRun.getBufferplacement(),
						benchmarkRun.getDataType(), benchmarkRun.getQueryLanguage(), benchmarkRun.getWaitConfig(),
						benchmarkRun.getInputFile(), benchmarkRun.getNumberOfRuns())
				&& (isNotBlank(benchmarkRun.getInputFile()) || isNotBlank(benchmarkRun.getQuery()))
				&& trueMap(benchmarkRun.getAllSingleTypes())) {
			// buttonAdd.setEnabled(true);
			buttonStart.setEnabled(true);
		} else {
			// buttonAdd.setSelection(false);
			// buttonAdd.setEnabled(false);
			buttonStart.setSelection(false);
			buttonStart.setEnabled(false);
		}
	}

	private void setDirtyState(boolean dirty) {
		if (isDirty != dirty) {
			isDirty = dirty;
			firePropertyChange(IEditorPart.PROP_DIRTY);
		}
	}

	private String splitString(String string) {
		String[] stringArray = string.split("\\.");
		int i = stringArray.length;
		return stringArray[i - 1];
	}

	private boolean trueMap(Map<String, Boolean> map) {
		boolean result = false;
		Set<Entry<String, Boolean>> sets = map.entrySet();
		for (Entry<String, Boolean> entry : sets) {
			if (entry.getValue() == true) {
				result = true;
			}
		}
		return result;
	}

	private boolean checkMetadataCombination() {
		Set<Set<String>> allTypeCombination = MetadataRegistry.getAvailableMetadataCombinations();
		@SuppressWarnings("unchecked")
		Set<String> set = new HashSet(Arrays.asList(benchmarkParam.getMetadataCombination()));
		if (allTypeCombination.contains(set)) {
			return true;
		} else {
			System.out.println("MEtadatenkombination falsch!"); // NUR
																// TESTAUSGABE!!!!!!!!!!!!!!!!!!!!!!!!!!
			Shell window = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
			org.eclipse.jface.dialogs.MessageDialog.openInformation(window, "Error in Metadata-Combination",
					"The benchmark couldn't start, metadata-combination doesn't exist!");
			return false;

		}
	}
}
