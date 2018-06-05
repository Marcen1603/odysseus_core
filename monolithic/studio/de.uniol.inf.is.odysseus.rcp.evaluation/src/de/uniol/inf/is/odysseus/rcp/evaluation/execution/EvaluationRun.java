package de.uniol.inf.is.odysseus.rcp.evaluation.execution;

import java.io.Serializable;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public class EvaluationRun implements Serializable {

    private static final long serialVersionUID = -155264023451987992L;
    private final int run;
    private final Map<String, String> values;
    private final ArrayList<String> sortedNames;
    private EvaluationRunContext context;

    public EvaluationRun(EvaluationRunContext context, int run, Map<String, String> currentValues) {
        this.context = context;
        this.run = run;
        this.values = currentValues;
        this.sortedNames = new ArrayList<>(values.keySet());
        Collections.sort(sortedNames);
    }

    public int getRun() {
        return run;
    }

    public String getVariableString() {
        String var = "";
        String sep = "";
        for (String key : sortedNames) {
            String fileSafeName = values.get(key).replaceAll("\\W+", "");
            var = var + sep + fileSafeName;
            sep = "_";
        }
        return var;
    }

    public EvaluationRunContext getContext() {
        return context;
    }

    public String createThroughputResultPath(ILogicalOperator op) {
        String name = op.getName();
        String base = context.getThroughputResultsPath();

        String var = "";
        String sep = "";
        for (String key : sortedNames) {
            String fileSafeName = values.get(key).replaceAll("\\W+", "");
            var = var + sep + fileSafeName;
            sep = "_";
        }
        name = name + ".csv";

        return FileSystems.getDefault().getPath(base + var, "" + run, name).toAbsolutePath().toString();
    }

    public String createLatencyResultPath(ILogicalOperator op) {
        String name = op.getName();
        String base = context.getLatencyResultsPath();

        String var = getVariableString();
        name = name + ".csv";

        return FileSystems.getDefault().getPath(base + var, "" + run, name).toAbsolutePath().toString();
    }

    public String createCPUResultPath(ILogicalOperator op) {
        String name = op.getName();
        String base = context.getCPUResultsPath();

        String var = getVariableString();
        name = name + ".csv";

        return FileSystems.getDefault().getPath(base + var, "" + run, name).toAbsolutePath().toString();
    }

    public String createMemoryResultPath(ILogicalOperator op) {
        String name = op.getName();
        String base = context.getMemoryResultsPath();

        String var = getVariableString();
        name = name + ".csv";

        return FileSystems.getDefault().getPath(base + var, "" + run, name).toAbsolutePath().toString();
    }
}
