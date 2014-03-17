package de.uniol.inf.is.odysseus.rcp.evaluation.model;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.XMLMemento;

public class EvaluationModel {

	private static final String ACTIVE = "ACTIVE";
	private static final String VALUE = "value";
	private static final String VARIABLE = "variable";
	private static final String VARIABLES = "variables";
	private static final String PLOT_FILES_PATH = "PLOT_FILES_PATH";
	private static final String PROCESSING_RESULTS_PATH = "PROCESSINGRESULTSPATH";
	private static final String WITH_THROUGHPUT = "WITH_THROUGHPUT";
	private static final String WITH_LATENCY = "WITH_LATENCY";
	private static final String NUMBER_OF_RUNS = "NUMBER_OF_RUNS";
	private IFile file;
	

	private String processingResultsPath = "";
	private String plotFilesPath = "";

	private boolean withLatency = true;
	private boolean withThroughput = true;

	private int numberOfRuns = 10;
	private List<EvaluationVariable> variables = new ArrayList<>();

	public EvaluationModel(IFile file) {
		this.file = file;		
		this.variables.add(new EvaluationVariable("var_1", "a", "b", "c"));
		this.variables.add(new EvaluationVariable("var_2","x", "y", "z"));
	}

	public void save() {
		try {
			XMLMemento memento = XMLMemento.createWriteRoot("evaluation");
			memento.putString(PROCESSING_RESULTS_PATH, processingResultsPath);
			memento.putString(PLOT_FILES_PATH, plotFilesPath);
			memento.putBoolean(WITH_LATENCY, withLatency);
			memento.putBoolean(WITH_THROUGHPUT, withThroughput);
			memento.putInteger(NUMBER_OF_RUNS, numberOfRuns);

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
			InputStream is = IOUtils.toInputStream(sw.toString());
			file.setContents(is, true, true, new NullProgressMonitor());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void load() {
		try {
			InputStream is = file.getContents();
			String content = IOUtils.toString(is);
			is.close();
			if (!content.isEmpty()) {
				StringReader sr = new StringReader(content);
				XMLMemento memento = XMLMemento.createReadRoot(sr);
				this.processingResultsPath = memento.getString(PROCESSING_RESULTS_PATH);
				this.plotFilesPath = memento.getString(PLOT_FILES_PATH);
				this.withLatency = memento.getBoolean(WITH_LATENCY);
				this.withThroughput = memento.getBoolean(WITH_THROUGHPUT);
				this.numberOfRuns = memento.getInteger(NUMBER_OF_RUNS);

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

	public String getProcessingResultsPath() {
		return processingResultsPath;
	}

	public void setProcessingResultsPath(String processingResultsPath) {
		this.processingResultsPath = processingResultsPath;
	}

	public String getPlotFilesPath() {
		return plotFilesPath;
	}

	public void setPlotFilesPath(String plotFilesPath) {
		this.plotFilesPath = plotFilesPath;
	}

	public boolean isWithLatency() {
		return withLatency;
	}

	public void setWithLatency(boolean withLatency) {
		this.withLatency = withLatency;
	}

	public boolean isWithThroughput() {
		return withThroughput;
	}

	public void setWithThroughput(boolean withThroughput) {
		this.withThroughput = withThroughput;
	}

	public int getNumberOfRuns() {
		return numberOfRuns;
	}

	public void setNumberOfRuns(int numberOfRuns) {
		this.numberOfRuns = numberOfRuns;
	}

	public List<EvaluationVariable> getVariables() {
		return variables;
	}

	public boolean parameterNameExists(String newName) {
		for(EvaluationVariable var : this.variables){
			if(var.getName().equalsIgnoreCase(newName)){
				return true;
			}
		}
		return false;
	}

}
