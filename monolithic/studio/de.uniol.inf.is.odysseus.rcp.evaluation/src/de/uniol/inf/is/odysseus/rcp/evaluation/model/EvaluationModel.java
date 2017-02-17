package de.uniol.inf.is.odysseus.rcp.evaluation.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.XMLMemento;

public class EvaluationModel implements Serializable {

	private static final long serialVersionUID = 112449710812448224L;

	private static final String ACTIVE = "ACTIVE";
	private static final String VALUE = "value";
	private static final String VARIABLE = "variable";
	private static final String VARIABLES = "variables";
	private static final String PLOT_FILES_PATH = "PLOT_FILES_PATH";
	private static final String PROCESSING_RESULTS_PATH = "PROCESSINGRESULTSPATH";
	private static final String WITH_THROUGHPUT = "WITH_THROUGHPUT";
	private static final String WITH_LATENCY = "WITH_LATENCY";
	private static final String USE_MAX_LATENCY = "USE_MAX_LATENCY";
	private static final String WITH_RESOURCE = "WITH_RESOURCE";

	private static final String NUMBER_OF_RUNS = "NUMBER_OF_RUNS";
	private static final String QUERY_FILE = "QUERY_FILE";
	private static final String SETUP_QUERY_FILE = "SETUP_QUERY_FILE";
	private static final String TEARDOWN_QUERY_FILE = "TEARDOWN_QUERY_FILE";
	private static final String CREATE_LATENCY_PLOTS = "CREATE_LATENCY_PLOTS";
	private static final String CREATE_THROUGHPUT_PLOTS = "CREATE_THROUGHPUT_PLOTS";
	private static final String MEASURE_THROUGHPUT_EACH = "MEASURE_THROUGHPUT_EACH";
	private static final String CREATE_CPU_PLOTS = "CREATE_CPU_PLOTS";
	private static final String CREATE_MEMORY_PLOTS = "CREATE_MEMORY_PLOTS";

	private static final String OUTPUT_TYPE = "OUTPUT_TYPE";
	private static final String OUTPUT_HEIGHT = "OUTPUT_HEIGHT";
	private static final String OUTPUT_WIDTH = "OUTPUT_WIDTH";

	private static final String RUN_SETUP_TEARDOWN_IN_EVERY_RUN = "RUN_SETUP_TEARDOWN_IN_EVERY_RUN";

	private IResource queryFile;
	private IResource setupQueryFile;
	private IResource tearDownQueryFile;
	private boolean runSetupTearDownEveryRun;

	private String processingResultsPath = "";
	private String plotFilesPath = "";

	private boolean withLatency = true;
	private boolean useMaxLatency = false;
	private boolean withThroughput = true;
	private boolean withResource = true;

	private int measureThrougputEach = 100;

	private boolean createThroughputPlots = true;
	private boolean createLatencyPlots = true;
	private boolean createMemoryPlots = true;
	private boolean createCPUPlots = true;

	private int numberOfRuns = 10;
	private List<EvaluationVariable> variables = new ArrayList<>();

	private int outputHeight = 300;
	private int outputWidth = 1000;

	private String outputType = "PNG";

	private EvaluationModel() {
		this.variables.add(new EvaluationVariable("var_1", "a", "b", "c"));
		this.variables.add(new EvaluationVariable("var_2", "x", "y", "z"));
	}

	public static EvaluationModel createEmpty(IResource iResource) {
		EvaluationModel model = new EvaluationModel();
		model.setQueryFile(iResource);
		return model;
	}

	public static EvaluationModel load(IFile file) {
		EvaluationModel model = new EvaluationModel();
		model.loadFromFile(file);
		return model;
	}

	private void saveToLocalFile(File file) {
		try {
			String fileContents = createInputStreamFile();
			FileUtils.write(file, fileContents);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void saveToIFile(IFile file) {
		try {
			String fileContents = createInputStreamFile();
			InputStream is = IOUtils.toInputStream(fileContents);
			file.setContents(is, true, true, new NullProgressMonitor());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String createInputStreamFile() throws IOException {

		XMLMemento memento = XMLMemento.createWriteRoot("evaluation");
		memento.putString(PROCESSING_RESULTS_PATH, this.processingResultsPath);
		memento.putString(PLOT_FILES_PATH, this.plotFilesPath);
		memento.putBoolean(WITH_LATENCY, this.withLatency);
		memento.putBoolean(USE_MAX_LATENCY, this.useMaxLatency);
		memento.putBoolean(WITH_THROUGHPUT, this.withThroughput);
		memento.putBoolean(WITH_RESOURCE, this.withResource);
		memento.putBoolean(CREATE_LATENCY_PLOTS, this.createLatencyPlots);
		memento.putBoolean(CREATE_THROUGHPUT_PLOTS, this.createThroughputPlots);
		memento.putInteger(MEASURE_THROUGHPUT_EACH, this.measureThrougputEach);
		memento.putBoolean(CREATE_CPU_PLOTS, this.createCPUPlots);
		memento.putBoolean(CREATE_MEMORY_PLOTS, this.createMemoryPlots);

		memento.putInteger(NUMBER_OF_RUNS, this.numberOfRuns);
		memento.putString(QUERY_FILE, this.queryFile.getProjectRelativePath().toPortableString());
		if (setupQueryFile != null) {
			memento.putString(SETUP_QUERY_FILE, this.setupQueryFile.getProjectRelativePath().toPortableString());
		}
		if (tearDownQueryFile != null) {
			memento.putString(TEARDOWN_QUERY_FILE, this.tearDownQueryFile.getProjectRelativePath().toPortableString());
		}
		memento.putBoolean(RUN_SETUP_TEARDOWN_IN_EVERY_RUN,this.runSetupTearDownEveryRun);
		memento.putInteger(OUTPUT_HEIGHT, this.outputHeight);
		memento.putInteger(OUTPUT_WIDTH, this.outputWidth);
		memento.putString(OUTPUT_TYPE, this.outputType);

		IMemento varMem = memento.createChild(VARIABLES);
		for (EvaluationVariable var : this.variables) {
			IMemento varChild = varMem.createChild(VARIABLE, var.getName());
			varChild.putBoolean(ACTIVE, var.isActive());
			for (String value : var.getValues()) {
				varChild.createChild(VALUE).putTextData(value);
			}
		}

		StringWriter sw = new StringWriter();
		memento.save(sw);

		return sw.toString();

	}

	private void loadFromFile(IFile file) {
		try (InputStream is = file.getContents()) {
			String content = IOUtils.toString(is);
			is.close();
			if (!content.isEmpty()) {
				StringReader sr = new StringReader(content);
				XMLMemento memento = XMLMemento.createReadRoot(sr);
				this.processingResultsPath = memento.getString(PROCESSING_RESULTS_PATH);
				this.plotFilesPath = memento.getString(PLOT_FILES_PATH);
				this.withLatency = checkNullAndSet(memento.getBoolean(WITH_LATENCY), this.withLatency);
				this.useMaxLatency = checkNullAndSet(memento.getBoolean(USE_MAX_LATENCY), this.useMaxLatency);
				this.withThroughput = memento.getBoolean(WITH_THROUGHPUT);
				this.withResource = memento.getBoolean(WITH_RESOURCE);

				this.numberOfRuns = memento.getInteger(NUMBER_OF_RUNS);

				this.measureThrougputEach = checkNullAndSet(memento.getInteger(MEASURE_THROUGHPUT_EACH),
						this.measureThrougputEach);
				this.createLatencyPlots = checkNullAndSet(memento.getBoolean(CREATE_LATENCY_PLOTS),
						this.createLatencyPlots);
				this.createThroughputPlots = checkNullAndSet(memento.getBoolean(CREATE_THROUGHPUT_PLOTS),
						this.createThroughputPlots);
				this.createCPUPlots = checkNullAndSet(memento.getBoolean(CREATE_CPU_PLOTS), this.createCPUPlots);
				this.createMemoryPlots = checkNullAndSet(memento.getBoolean(CREATE_MEMORY_PLOTS),
						this.createMemoryPlots);
				if (!this.withLatency) {
					this.createLatencyPlots = false;
				}
				if (!this.withThroughput) {
					this.createThroughputPlots = false;
				}
				if (!this.withResource) {
					this.createCPUPlots = false;
					this.createMemoryPlots = false;
				}
				this.outputHeight = checkNullAndSet(memento.getInteger(OUTPUT_HEIGHT), this.outputHeight);
				this.outputWidth = checkNullAndSet(memento.getInteger(OUTPUT_WIDTH), this.outputWidth);
				this.outputType = checkNullAndSet(memento.getString(OUTPUT_TYPE), this.outputType);

				String path = memento.getString(QUERY_FILE);
				this.queryFile = file.getProject().findMember(Path.fromPortableString(path));
				path = memento.getString(SETUP_QUERY_FILE);
				if (path != null) {
					this.setupQueryFile = file.getProject().findMember(Path.fromPortableString(path));
				}
				path = memento.getString(TEARDOWN_QUERY_FILE);
				if (path != null) {
					this.tearDownQueryFile = file.getProject().findMember(Path.fromPortableString(path));
				}

				this.runSetupTearDownEveryRun = checkNullAndSet(memento.getBoolean(RUN_SETUP_TEARDOWN_IN_EVERY_RUN), this.runSetupTearDownEveryRun);



				this.variables.clear();
				IMemento varMem = memento.getChild(VARIABLES);
				for (IMemento mem : varMem.getChildren(VARIABLE)) {
					String name = mem.getID();
					List<String> values = new ArrayList<>();
					for (IMemento child : mem.getChildren(VALUE)) {
						values.add(child.getTextData());
					}
					boolean active = mem.getBoolean(ACTIVE);
					this.variables.add(new EvaluationVariable(name, values, active));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private <T> T checkNullAndSet(T value, T defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		return value;

	}

	public String getProcessingResultsPath() {
		return this.processingResultsPath;
	}

	public void setProcessingResultsPath(String processingResultsPath) {
		this.processingResultsPath = processingResultsPath;

	}

	public String getPlotFilesPath() {
		return this.plotFilesPath;
	}

	public void setPlotFilesPath(String plotFilesPath) {
		this.plotFilesPath = plotFilesPath;

	}

	public boolean isWithLatency() {
		return this.withLatency;
	}

	public void setWithLatency(boolean withLatency) {
		this.withLatency = withLatency;
		if (!this.withLatency) {
			this.createLatencyPlots = false;
		}
	}

	public boolean isUseMaxLatency() {
		return useMaxLatency;
	}

	public void setUseMaxLatency(boolean useMaxLatency) {
		this.useMaxLatency = useMaxLatency;
	}

	public boolean isWithThroughput() {
		return this.withThroughput;
	}

	public void setWithThroughput(boolean withThroughput) {
		this.withThroughput = withThroughput;
		if (!this.withThroughput) {
			this.createThroughputPlots = false;
		}
	}

	public boolean isWithResource() {
		return this.withResource;
	}

	public void setWithResource(boolean withResource) {
		this.withResource = withResource;
		if (!this.withResource) {
			this.createCPUPlots = false;
			this.createMemoryPlots = false;
		}
	}

	public int getNumberOfRuns() {
		return this.numberOfRuns;
	}

	public void setNumberOfRuns(int numberOfRuns) {
		this.numberOfRuns = numberOfRuns;
	}

	public List<EvaluationVariable> getVariables() {
		return this.variables;
	}

	public boolean parameterNameExists(String newName) {
		for (EvaluationVariable var : this.variables) {
			if (var.getName().equalsIgnoreCase(newName)) {
				return true;
			}
		}
		return false;
	}

	public IResource getQueryFile() {
		return this.queryFile;
	}

	public void setQueryFile(IResource queryFile) {
		this.queryFile = queryFile;
	}

	public IResource getSetupQueryFile() {
		return this.setupQueryFile;
	}

	public void setSetupQueryFile(IResource queryFile) {
		this.setupQueryFile = queryFile;
	}

	public IResource getTearDownQueryFile() {
		return this.tearDownQueryFile;
	}

	public void setTearDownQueryFile(IResource queryFile) {
		this.tearDownQueryFile = queryFile;
	}

	public boolean isRunSetupTearDownEveryRun() {
		return runSetupTearDownEveryRun;
	}

	public void setRunSetupTearDownEveryRun(boolean runSetupTearDownEveryRun) {
		this.runSetupTearDownEveryRun = runSetupTearDownEveryRun;
	}

	public void save(IFile file) {
		saveToIFile(file);
	}

	public void save(File file) {
		saveToLocalFile(file);
	}

	public int getMeasureThrougputEach() {
		return this.measureThrougputEach;
	}

	public void setMeasureThrougputEach(int measureThrougputEach) {
		this.measureThrougputEach = measureThrougputEach;
	}

	public boolean isCreateThroughputPlots() {
		return this.createThroughputPlots;
	}

	public void setCreateThroughputPlots(boolean createThroughputPlots) {
		this.createThroughputPlots = createThroughputPlots;
	}

	public boolean isCreateLatencyPlots() {
		return this.createLatencyPlots;
	}

	public void setCreateLatencyPlots(boolean createLatencyPlots) {
		this.createLatencyPlots = createLatencyPlots;
	}

	public boolean isCreateCPUPlots() {
		return this.createCPUPlots;
	}

	public void setCreateCPUPlots(boolean createCPUPlots) {
		this.createCPUPlots = createCPUPlots;
	}

	public boolean isCreateMemoryPlots() {
		return this.createMemoryPlots;
	}

	public void setCreateMemoryPlots(boolean createMemoryPlots) {
		this.createMemoryPlots = createMemoryPlots;
	}

	public int getOutputHeight() {
		return this.outputHeight;
	}

	public void setOutputHeight(int outputHeight) {
		this.outputHeight = outputHeight;
	}

	public int getOutputWidth() {
		return this.outputWidth;
	}

	public void setOutputWidth(int outputWidth) {
		this.outputWidth = outputWidth;
	}

	public String getOutputType() {
		return this.outputType;
	}

	public void setOutputType(String outputType) {
		this.outputType = outputType;
	}

}
