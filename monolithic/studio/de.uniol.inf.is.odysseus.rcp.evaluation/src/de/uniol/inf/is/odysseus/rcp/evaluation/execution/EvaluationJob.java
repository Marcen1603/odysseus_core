package de.uniol.inf.is.odysseus.rcp.evaluation.execution;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.evaluation.Activator;
import de.uniol.inf.is.odysseus.rcp.evaluation.model.EvaluationModel;
import de.uniol.inf.is.odysseus.rcp.evaluation.model.EvaluationVariable;
import de.uniol.inf.is.odysseus.rcp.evaluation.plot.PlotBuilder;
import de.uniol.inf.is.odysseus.rcp.queries.ParserClientUtil;

public class EvaluationJob extends Job implements IPlanModificationListener {

	InfoService INFO = InfoServiceFactory.getInfoService(EvaluationJob.class);
	
	private EvaluationModel model;
	private Collection<Integer> ids = new ArrayList<>();
	private static final String PRE_TRANSFORM_TOKEN = "#PRETRANSFORM EvaluationPreTransformation";

	public EvaluationJob(EvaluationModel model) {
		super("Running Evaluation...");
		this.model = model;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		ids.clear();
		IServerExecutor executor = (IServerExecutor) Activator.getExecutor();
		executor.addPlanModificationListener(this);

		try {
			synchronized (this) {

				DateFormat dateFormat = new SimpleDateFormat("ddMMyy-HHmmss");
				Calendar cal = Calendar.getInstance();
				String identifier = dateFormat.format(cal.getTime());
				EvaluationRunContext evaluationRunContext = new EvaluationRunContext(model, identifier);

				int totalEvaluations = model.getNumberOfRuns();
				List<EvaluationVariable> variables = new ArrayList<>();
				ArrayList<Integer> ranges = new ArrayList<>();
				for (EvaluationVariable variable : model.getVariables()) {
					if (variable.isActive()) {
						variables.add(variable);
						totalEvaluations = totalEvaluations * variable.getValues().size();
						ranges.add(variable.getValues().size());
					}
				}
				String queryLines = fileToLines(model.getQueryFile());
				String setupLines = fileToLines(model.getSetupQueryFile());
				String tearDownLines = fileToLines(model.getTearDownQueryFile());

				// save model file for logging purposes
				String modelFileBackup = FilenameUtils.concat(evaluationRunContext.getResultsPathIdentified(),
						"model.eval");
				model.save(new File(modelFileBackup));
				// and the current query file
				String queryFileBackup = FilenameUtils.concat(evaluationRunContext.getResultsPathIdentified(),
						model.getQueryFile().getName());
				FileUtils.write(new File(queryFileBackup), queryLines);

				// then, prepare lines for executing
				queryLines = prepareQueryFileForEvaluation(queryLines);

				monitor.beginTask("Running evaluations...", totalEvaluations);

				EvaluationRunContainer evaluationRunContainer = new EvaluationRunContainer(evaluationRunContext);
				int counter = recursiveFor(new ArrayDeque<Integer>(), ranges, ranges.size(), 0, totalEvaluations,
						evaluationRunContainer, variables, monitor, queryLines, setupLines, tearDownLines,
						model.isRunSetupTearDownEveryRun());
				if (counter < totalEvaluations) {
					return Status.CANCEL_STATUS;
				}

				monitor.beginTask("Creating diagrams...", IProgressMonitor.UNKNOWN);
				if (model.isCreateLatencyPlots()) {
					monitor.subTask("Creating latency plots...");
					PlotBuilder.createLatencyPlots(evaluationRunContainer, model, monitor);
				}
				if (model.isCreateThroughputPlots()) {
					monitor.subTask("Creating throughput plots...");
					PlotBuilder.createThroughputPlots(evaluationRunContainer, model, monitor);
				}
				if (model.isCreateCPUPlots()) {
					monitor.subTask("Creating CPU plots...");
					PlotBuilder.createCPUPlots(evaluationRunContainer, model, monitor);
				}
				if (model.isCreateMemoryPlots()) {
					monitor.subTask("Creating memory plots...");
					PlotBuilder.createMemoryPlots(evaluationRunContainer, model, monitor);
				}
			}
		} catch (InterruptedException ex) {
			return Status.CANCEL_STATUS;
		} catch (Throwable ex) {
			INFO.error("Error in Evaluation", ex);
			ex.printStackTrace();
			return Status.CANCEL_STATUS;
		}
		return Status.OK_STATUS;
	}

	private String prepareQueryFileForEvaluation(String lines) {
		if (!lines.contains(PRE_TRANSFORM_TOKEN)) {
			lines = PRE_TRANSFORM_TOKEN + System.lineSeparator() + lines;
		}

		return lines;
	}

	private int recursiveFor(Deque<Integer> indices, List<Integer> ranges, int n, int counter, int totalEvals,
			EvaluationRunContainer evaluationRunContainer, List<EvaluationVariable> values, IProgressMonitor monitor,
			String queryLines, String setupLines, String teardownLine, boolean setupTeardownEveryRun) throws Exception {
		if (n != 0) {
			for (int i = 0; i < ranges.get(n - 1); i++) {
				indices.push(i);
				counter = recursiveFor(indices, ranges, n - 1, counter, totalEvals, evaluationRunContainer, values,
						monitor, queryLines, setupLines, teardownLine, setupTeardownEveryRun);
				indices.pop();
			}
		} else {
			counter = runEvalStep(indices, values, counter, totalEvals, evaluationRunContainer, monitor, queryLines,
					setupLines, teardownLine, setupTeardownEveryRun);
		}
		return counter;
	}

	private int runEvalStep(Deque<Integer> index, List<EvaluationVariable> values, int counter, int totalEvals,
			EvaluationRunContainer evaluationRunContainer, IProgressMonitor monitor, String queryLines,
			String setupQueryLines, String teardownQueryLines, boolean setupTeardownEveryRun) throws Exception {
		IServerExecutor executor = (IServerExecutor) Activator.getExecutor();
		NumberFormat nf = NumberFormat.getInstance();

		ISession caller = OdysseusRCPPlugIn.getActiveSession();
		IResource qeryFile = model.getQueryFile();
		Context context = ParserClientUtil.createRCPContext((IFile) qeryFile);

		Map<String, String> currentValues = new TreeMap<>();
		Integer[] pointers = index.toArray(new Integer[0]);
		for (int i = 0; i < pointers.length; i++) {
			EvaluationVariable var = values.get(i);
			String value = var.getValues().get(pointers[i]);
			currentValues.put(var.getName(), value);
		}

		String querytext = queryLines;

		String prefix = "";

		if (!setupTeardownEveryRun) {
			runSetup(monitor, setupQueryLines, executor, caller, context, prefix);
		}

		for (int i = 1; i <= model.getNumberOfRuns(); i++) {
			counter++;
			String prefixHeader = "Performing Evaluation number " + i + " / " + model.getNumberOfRuns() + " for total "
					+ counter + "/" + totalEvals + "\n";
			if (monitor.isCanceled()) {
				runTearDown(monitor, teardownQueryLines, executor, caller, context, prefix);
				throw new InterruptedException();
			}

			prefix = prefixHeader;

			for (Entry<String, String> currentValue : currentValues.entrySet()) {
				prefix = prefix + " - " + currentValue.getKey() + ": " + currentValue.getValue() + "\n";
				querytext = querytext.replaceAll(Pattern.quote("${" + currentValue.getKey() + "}"),
						currentValue.getValue());
			}
			monitor.subTask(prefix + "Executing Script \"" + qeryFile.getName() + "\"... ");
			long timeStarted = System.currentTimeMillis();

			monitor.subTask(prefix + "Adding query...");
			EvaluationRun evaluationRun = new EvaluationRun(evaluationRunContainer.getContext(), i, currentValues);
			evaluationRunContainer.getRuns().add(evaluationRun);
			if (context.containsKey(EvaluationRun.class.getName())) {
				context.remove(EvaluationRun.class.getName());
			}
			context.put(EvaluationRun.class.getName(), evaluationRun);

			if (setupTeardownEveryRun) {
				runSetup(monitor, setupQueryLines, executor, caller, context, prefix);
			}

			ids = executor.addQuery(querytext, "OdysseusScript", caller, context);
			monitor.subTask(prefix + "Running query and waiting for stop...");
			for (int id : ids) {
				executor.startQuery(id, caller);
			}

			this.wait();

			monitor.worked(1);
			System.out.println("Evaluation job takes " + nf.format(System.currentTimeMillis() - timeStarted) + " ms");
			monitor.subTask(prefix + "Process done. Removing query...");
			for (Integer id : ids) {
				executor.removeQuery(id, caller);
			}

			if (setupTeardownEveryRun) {
				runTearDown(monitor, teardownQueryLines, executor, caller, context, prefix);
			}

			monitor.subTask(prefix + "Run done. Starting next...");
		}
		prefix = "";
		if (!setupTeardownEveryRun) {
			runTearDown(monitor, teardownQueryLines, executor, caller, context, prefix);
		}
		return counter;

	}

	private void runSetup(IProgressMonitor monitor, String setupQueryLines, IServerExecutor executor, ISession caller,
			Context context, String prefix) throws InterruptedException {
		String message = prefix + "Setting up query ...";
		runAndWait(monitor, executor, caller, context, message, setupQueryLines);
	}

	private void runTearDown(IProgressMonitor monitor, String teardownQueryLines, IServerExecutor executor,
			ISession caller, Context context, String prefix) throws InterruptedException {
		String message = prefix + "Tearing down query ...";
		runAndWait(monitor, executor, caller, context, message, teardownQueryLines);
	}

	private void runAndWait(IProgressMonitor monitor, IServerExecutor executor, ISession caller, Context context,
			String message, String query) throws InterruptedException {
		if (!com.google.common.base.Strings.isNullOrEmpty(query)) {
			ids = executor.addQuery(query, "OdysseusScript", caller, context);
			monitor.subTask(message);
			for (int id : ids) {
				executor.startQuery(id, caller);
			}
			// Simple queries need to time and send to QUERY_STOP
			Thread.sleep(1000);
			for (int i : ids) {
				// If one query is still running ... wait
				if (executor.getQueryState(i, caller) == QueryState.RUNNING) {
					this.wait(1000);
				}
			}
		}
	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		if (eventArgs.getEventType().equals(PlanModificationEventType.QUERY_STOP)) {
			// System.out.println("query stopped!");
			synchronized (this) {
				this.notifyAll();
			}
		}
	}

	@Override
	protected void canceling() {
		IServerExecutor executor = (IServerExecutor) Activator.getExecutor();
		ISession caller = OdysseusRCPPlugIn.getActiveSession();
		for (Integer id : ids) {
			executor.stopQuery(id, caller);
		}
	}

	private static String fileToLines(IResource res) throws Exception {
		if (res == null && !(res instanceof IFile)) {
			return "";
		}
		String lines = "";
		if (!res.isSynchronized(IResource.DEPTH_ZERO)) {
			res.refreshLocal(IResource.DEPTH_ZERO, null);
		}
		if (!(res instanceof IFile)){
			return "";
		}
		try {
			IFile file = (IFile) res;

			try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getContents()))) {
				String line = br.readLine();
				while (line != null) {
					lines = lines + line + "\n";
					line = br.readLine();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lines;
	}

}
