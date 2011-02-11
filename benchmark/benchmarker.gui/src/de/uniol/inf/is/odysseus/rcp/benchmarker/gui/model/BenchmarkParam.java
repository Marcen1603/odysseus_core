package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BenchmarkParam implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	private String scheduler;
	private String schedulingstrategy;
	private String bufferplacement;
	private String dataType;
	private String metadataTypes;
	private String queryLanguage;
	private String query;
	private String maxResult;
	private boolean priority;
	private boolean punctuations;
	private boolean extendedPostpriorisation;
	private boolean memoryUsage;
	private boolean noMetada;
	private String waitConfig;
	private boolean resultPerQuery;
	private String inputFile;
	private String numberOfRuns;
	private transient PropertyChangeSupport propertyChangeSupport;
	private boolean readOnly;
	private Map<String, Boolean> allSingleTypes;

	public BenchmarkParam() {
		propertyChangeSupport = new PropertyChangeSupport(this);
		dataType = "relational"; // Default: DataType = "relational"
		name = "run";
		waitConfig = "3";
		numberOfRuns = "1";
		allSingleTypes = new HashMap<String, Boolean>();
	}

	public BenchmarkParam(int id) {
		this();
		this.id = id;
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getScheduler() {
		return scheduler;
	}

	public String getSchedulingstrategy() {
		return schedulingstrategy;
	}

	public String getBufferplacement() {
		return bufferplacement;
	}

	public String getDataType() {
		return dataType;
	}

	public String getMetadataTypes() {
		return metadataTypes;
	}

	public String getQueryLanguage() {
		return queryLanguage;
	}

	public String getQuery() {
		return query;
	}

	public String getMaxResult() {
		return maxResult;
	}

	public boolean isPriority() {
		return priority;
	}

	public boolean isPunctuations() {
		return punctuations;
	}

	public boolean isExtendesPostpriorisation() {
		return extendedPostpriorisation;
	}

	public boolean isMemoryUsage() {
		return memoryUsage;
	}

	public boolean isNoMetada() {
		return noMetada;
	}

	public String getWaitConfig() {
		return waitConfig;
	}

	public boolean isResultPerQuery() {
		return resultPerQuery;
	}

	public String getInputFile() {
		return inputFile;
	}

	public String getNumberOfRuns() {
		return numberOfRuns;
	}

	public void setId(int id) {
		propertyChangeSupport.firePropertyChange("id", this.id, this.id = id);
	}

	public void setName(String name) {
		propertyChangeSupport.firePropertyChange("nameOfBenchmarkRun", this.name, this.name = name);
	}

	public void setScheduler(String scheduler) {
		propertyChangeSupport.firePropertyChange("scheduler", this.scheduler, this.scheduler = scheduler);
	}

	public void setSchedulingstrategy(String schedulingstrategy) {
		propertyChangeSupport.firePropertyChange("schedulingstrategy", this.schedulingstrategy,
				this.schedulingstrategy = schedulingstrategy);
	}

	public void setBufferplacement(String bufferplacement) {
		propertyChangeSupport.firePropertyChange("bufferplacement", this.bufferplacement,
				this.bufferplacement = bufferplacement);
	}

	public void setDataType(String dataType) {
		propertyChangeSupport.firePropertyChange("dataType", this.dataType, this.dataType = dataType);
	}

	public void setMetadataTypes(String metadataTypes) {
		propertyChangeSupport.firePropertyChange("metadataTypes", this.metadataTypes,
				this.metadataTypes = metadataTypes);
	}

	public void setQueryLanguage(String queryLanguage) {
		propertyChangeSupport.firePropertyChange("queryLanguage", this.queryLanguage,
				this.queryLanguage = queryLanguage);
	}

	public void setQuery(String query) {
		propertyChangeSupport.firePropertyChange("query", this.query, this.query = query);
	}

	public void setMaxResult(String maxResult) {
		propertyChangeSupport.firePropertyChange("maxResult", this.maxResult, this.maxResult = maxResult);
	}

	public void setPriority(boolean priority) {
		propertyChangeSupport.firePropertyChange("priority", this.priority, this.priority = priority);
	}

	public void setPunctuations(boolean punctuations) {
		propertyChangeSupport.firePropertyChange("punctuations", this.punctuations, this.punctuations = punctuations);
	}

	public void setExtendesPostpriorisation(boolean extendesPostpriorisation) {
		propertyChangeSupport.firePropertyChange("extendesPostpriorisation", this.extendedPostpriorisation,
				this.extendedPostpriorisation = extendesPostpriorisation);
	}

	public void setMemoryUsage(boolean memoryUsage) {
		propertyChangeSupport.firePropertyChange("memoryUsage", this.memoryUsage, this.memoryUsage = memoryUsage);
	}

	public void setNoMetada(boolean noMetada) {
		propertyChangeSupport.firePropertyChange("noMetada", this.noMetada, this.noMetada = noMetada);
	}

	public void setWaitConfig(String waitConfig) {
		propertyChangeSupport.firePropertyChange("waitConfig", this.waitConfig, this.waitConfig = waitConfig);
	}

	public void setResultPerQuery(boolean resultPerQuery) {
		propertyChangeSupport.firePropertyChange("resultPerQuery", this.resultPerQuery,
				this.resultPerQuery = resultPerQuery);
	}

	public void setInputFile(String inputFile) {
		propertyChangeSupport.firePropertyChange("inputFile", this.inputFile, this.inputFile = inputFile);
	}

	public void setNumberOfRuns(String numberOfRuns) {
		propertyChangeSupport.firePropertyChange("numberOfRuns", this.numberOfRuns, this.numberOfRuns = numberOfRuns);
	}

	/**
	 * @return true wenn nicht überschrieben werden darf
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	public Map<String, Boolean> getAllSingleTypes() {
		return allSingleTypes;
	}
	
	public void setAllSingleTypes(Map<String, Boolean> allSingleTypes) {
		this.allSingleTypes = allSingleTypes;
	}

	protected void setPropertyChangeSupport(PropertyChangeSupport changeSupport) {
		this.propertyChangeSupport = changeSupport;
	}

	@Override
	public String toString() {
		return "BenchmarkRun [nameOfBenchmarkRun=" + name + ", scheduler=" + scheduler + ", schedulingstrategy="
				+ schedulingstrategy + ", bufferplacement=" + bufferplacement + ", dataType=" + dataType
				+ ", metadataTypes=" + metadataTypes + ", queryLanguage=" + queryLanguage + ", query=" + query
				+ ", maxResult=" + maxResult + ", priority=" + priority + ", punctuations=" + punctuations
				+ ", extendesPostpriorisation=" + extendedPostpriorisation + ", memoryUsage=" + memoryUsage
				+ ", noMetada=" + noMetada + ", waitConfig=" + waitConfig + ", resultPerQuery=" + resultPerQuery
				+ ", inputFile=" + inputFile + ", numberOfRuns=" + numberOfRuns + "]";
	}

	@Override
	public BenchmarkParam clone() {
		BenchmarkParam newParam;
		try {
			newParam =  (BenchmarkParam) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
		newParam.allSingleTypes = new HashMap<String, Boolean>(allSingleTypes);
		newParam.propertyChangeSupport = new PropertyChangeSupport(newParam);
		return newParam;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BenchmarkParam other = (BenchmarkParam) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
