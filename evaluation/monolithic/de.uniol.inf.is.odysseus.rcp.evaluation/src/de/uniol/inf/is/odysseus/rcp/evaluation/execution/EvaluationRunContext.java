package de.uniol.inf.is.odysseus.rcp.evaluation.execution;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import de.uniol.inf.is.odysseus.rcp.evaluation.model.EvaluationModel;

public class EvaluationRunContext {

    private String identifier;
    private EvaluationModel model;

    public EvaluationRunContext(EvaluationModel model, String identifier) {
        this.identifier = identifier;
        this.model = model;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public EvaluationModel getModel() {
        return model;
    }

    public void setModel(EvaluationModel model) {
        this.model = model;
    }

    public String getResultsPathIdentified() {
        return FilenameUtils.concat(this.model.getProcessingResultsPath(), identifier);
    }

    public String getPlotsPathIdentified() {
        return FilenameUtils.concat(this.model.getPlotFilesPath(), identifier);
    }

    public String getLatencyResultsPath() {
        return FilenameUtils.concat(getResultsPathIdentified(), "latencies") + File.separator;
    }

    public String getThroughputResultsPath() {
        return FilenameUtils.concat(getResultsPathIdentified(), "throughput") + File.separator;
    }

    public String getCPUResultsPath() {
        return FilenameUtils.concat(getResultsPathIdentified(), "cpu") + File.separator;
    }

    public String getMemoryResultsPath() {
        return FilenameUtils.concat(getResultsPathIdentified(), "memory") + File.separator;
    }

    public String getLatencyPlotsPath() {
        return FilenameUtils.concat(getPlotsPathIdentified(), "latencies") + File.separator;
    }

    public String getThroughputPlotsPath() {
        return FilenameUtils.concat(getPlotsPathIdentified(), "throughput") + File.separator;
    }

    public String getCPUPlotsPath() {
        return FilenameUtils.concat(getPlotsPathIdentified(), "cpu") + File.separator;
    }

    public String getMemoryPlotsPath() {
        return FilenameUtils.concat(getPlotsPathIdentified(), "memory") + File.separator;
    }
}
