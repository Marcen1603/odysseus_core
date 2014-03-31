package de.uniol.inf.is.odysseus.rcp.evaluation.command;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.GetQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.logicaloperator.latency.CalcLatencyAO;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.evaluation.Activator;
import de.uniol.inf.is.odysseus.rcp.evaluation.model.EvaluationModel;
import de.uniol.inf.is.odysseus.rcp.evaluation.model.EvaluationVariable;
import de.uniol.inf.is.odysseus.rcp.queries.ParserClientUtil;

public class EvaluationJob extends Job implements IPlanModificationListener {

	private EvaluationModel model;

	public EvaluationJob(EvaluationModel model) {
		super("Running Evaluation...");
		this.model = model;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		IServerExecutor executor = (IServerExecutor) Activator.getExecutor();
		executor.addPlanModificationListener(this);

		try {
			synchronized (this) {

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
				Collections.reverse(ranges);

				String lines = fileToLines(model.getQueryFile());

				monitor.beginTask("Running evaluations...", totalEvaluations);
				int counter = recursiveFor(new ArrayDeque<Integer>(), ranges, ranges.size(), 0, totalEvaluations, variables, monitor, lines);
				if (counter < totalEvaluations) {
					return Status.CANCEL_STATUS;
				}
			}
		} catch (InterruptedException ex) {
			return Status.CANCEL_STATUS;
		} catch (Throwable ex) {
			ex.printStackTrace();
			return Status.CANCEL_STATUS;
		}
		return Status.OK_STATUS;
	}

	private int recursiveFor(Deque<Integer> indices, List<Integer> ranges, int n, int counter, int totalEvals, List<EvaluationVariable> values, IProgressMonitor monitor, String lines) throws Exception {
		if (n != 0) {
			for (int i = 0; i < ranges.get(n - 1); i++) {
				indices.push(i);
				counter = recursiveFor(indices, ranges, n - 1, counter, totalEvals, values, monitor, lines);
				indices.pop();
			}
		} else {
			counter = runEvalStep(indices, values, counter, totalEvals, monitor, lines);
		}
		return counter;
	}

	private int runEvalStep(Deque<Integer> index, List<EvaluationVariable> values, int counter, int totalEvals, IProgressMonitor monitor, String allLines) throws Exception {
		IServerExecutor executor = (IServerExecutor) Activator.getExecutor();
		NumberFormat nf = NumberFormat.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("ddMMyy-HHmmss");
		Calendar cal = Calendar.getInstance();
		String date = dateFormat.format(cal.getTime());
		ISession caller = OdysseusRCPPlugIn.getActiveSession();

		Map<String, String> currentValues = new TreeMap<>();
		Integer[] pointers = index.toArray(new Integer[0]);
		for (int i = pointers.length - 1; i >= 0; i--) {
			EvaluationVariable var = values.get(i);
			String value = var.getValues().get(pointers[i]);
			currentValues.put(var.getName(), value);
		}
		for (int i = 1; i <= model.getNumberOfRuns(); i++) {
			counter++;
			String prefix = "Performing Evaluation number " + i + " / " + model.getNumberOfRuns() + " for total " + counter + "/" + totalEvals + "\n";
			if (monitor.isCanceled()) {
				throw new InterruptedException();
			}
			String thislines = allLines.replaceAll("SPECIAL_STAMP", date + "-" + i);
			for (Entry<String, String> currentValue : currentValues.entrySet()) {
				prefix = prefix + " - " + currentValue.getKey() + ": " + currentValue.getValue() + "\n";
				thislines = thislines.replaceAll(Pattern.quote("${" + currentValue.getKey() + "}"), currentValue.getValue());
			}
			monitor.subTask(prefix + "Executing Script \"" + model.getQueryFile().getName() + "\"... ");
			Context context = ParserClientUtil.createRCPContext((IFile) model.getQueryFile());
			long timeStarted = System.currentTimeMillis();

			List<IExecutorCommand> commands = executor.translateQuery(thislines, "OdysseusScript", caller, context);
			List<ILogicalQuery> queries = new ArrayList<>();
			for (IExecutorCommand command : commands) {
				if (command instanceof CreateQueryCommand) {
					queries.add(((CreateQueryCommand) command).getQuery());
				}
				if (command instanceof GetQueryCommand) {
					int id = ((GetQueryCommand) command).getQueryID();
					ILogicalQuery query = executor.getLogicalQueryById(id, caller);
					queries.add(query);
					executor.stopQuery(id, caller);
				}
			}
			// Collection<Integer> ids = executor.addQuery(thislines,
			// "OdysseusScript", caller, "Standard", context);
			if (model.isWithLatency()) {
				monitor.subTask(prefix + "Preparing queries for latency calculation");
				addLatencyCalculations(queries, model);
			}

			if (model.isWithThroughput()) {
				monitor.subTask(prefix + "Preparing queries for throughput calculation");
				addThroughputCalculations(queries, model);
			}

			Collection<Integer> ids = new ArrayList<>();

			for (ILogicalQuery query : queries) {
				QueryBuildConfiguration buildConfig = executor.getBuildConfigForQuery(query);
				ids.add(executor.addQuery(query.getLogicalPlan(), caller, buildConfig.getName()));
			}

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
			monitor.subTask(prefix + "Run done. Starting next...");
		}
		return counter;

	}

	private void addThroughputCalculations(Collection<ILogicalQuery> queries, EvaluationModel model2) {
		// TODO Auto-generated method stub

	}

	private void addLatencyCalculations(List<ILogicalQuery> queries, EvaluationModel evaluationModel) {
		try {
			// IServerExecutor executor = (IServerExecutor)
			// Activator.getExecutor();
			// ISession caller = OdysseusRCPPlugIn.getActiveSession();
			for (ILogicalQuery query : queries) {

				// // add latency calculation
				// LatencyCalculationPipe<Tuple<IntervalLatency>> latency =
				// new LatencyCalculationPipe<>();
				// @SuppressWarnings("unchecked")
				// ISource<Tuple<IntervalLatency>> oldSink =
				// (ISource<Tuple<IntervalLatency>>) operator;
				// latency.setOutputSchema(oldSink.getOutputSchema());
				// oldSink.subscribeSink(latency, 0, 0,
				// operator.getOutputSchema());
				// // add latency to payload using an ao to create correct
				// // output schema
				// LatencyToPayloadAO ao = new LatencyToPayloadAO();
				// SDFSchema outputSchema =
				// ao.buildOutputSchema(latency.getOutputSchema());
				// LatencyToPayloadPO<IntervalLatency,
				// Tuple<IntervalLatency>> ltp = new LatencyToPayloadPO<>();
				// ltp.setOutputSchema(outputSchema);
				// latency.subscribeSink(ltp, 0, 0,
				// latency.getOutputSchema());
				ILogicalOperator root = query.getLogicalPlan();
				CalcLatencyAO latency = new CalcLatencyAO();
				latency.subscribeToSource(root, 0, 0, root.getOutputSchema());
				query.setLogicalPlan(latency, true);
			}

		} catch (PlanManagementException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		if (eventArgs.getEventType().equals(PlanModificationEventType.QUERY_STOP)) {
			System.out.println("query stopped!");
			synchronized (this) {
				this.notifyAll();
			}
		}
	}

	private static String fileToLines(IResource res) throws Exception {
		if (!res.isSynchronized(IResource.DEPTH_ZERO)) {
			res.refreshLocal(IResource.DEPTH_ZERO, null);
		}
		IFile file = (IFile) res;
		String lines = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(file.getContents()));
		String line = br.readLine();
		while (line != null) {
			lines = lines + line + "\n";
			line = br.readLine();
		}
		br.close();
		return lines;
	}

}
