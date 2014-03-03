package de.uniol.inf.is.odysseus.rcp.evaluation.command;

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
import java.util.regex.Pattern;
import java.util.TreeMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.evaluation.Activator;
import de.uniol.inf.is.odysseus.rcp.queries.ParserClientUtil;

public class EvaluationJob extends Job implements IPlanModificationListener {

	private int number;
	private String allLines;
	private IFile file;
	private Map<String, List<String>> values;

	public EvaluationJob(String name, Map<String, List<String>> values, int number, String allLines, IFile file) {
		super(name);
		this.values = values;
		this.number = number;
		this.allLines = allLines;
		this.file = file;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		IServerExecutor executor = (IServerExecutor) Activator.getExecutor();
		executor.addPlanModificationListener(this);

		try {
			synchronized (this) {

				// String[] algorithms = {"J48", "DECISIONTABLE", "NaiveBayes"};
				// String[] algorithms = {"SIMPLEKMEANS", "EM", "DENSITY_KMEANS"};
				// String[] algorithms = {"5", "10", "15", "20"};

				List<String> variableNames = new ArrayList<>();

				int totalEvaluations = number;
				ArrayList<Integer> ranges = new ArrayList<>();
				for (String variable : values.keySet()) {
					variableNames.add(variable);
					List<String> thevals = values.get(variable);
					totalEvaluations = totalEvaluations * thevals.size();
					ranges.add(thevals.size());
				}
				Collections.reverse(variableNames);
				Collections.reverse(ranges);				
				monitor.beginTask("Running evaluations...", totalEvaluations);
				int counter = recursiveFor(new ArrayDeque<Integer>(), variableNames, ranges, ranges.size(), 0, totalEvaluations, values, monitor);
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

	private int recursiveFor(Deque<Integer> indices, List<String> names, List<Integer> ranges, int n, int counter, int totalEvals, Map<String, List<String>> values, IProgressMonitor monitor) throws Exception {
		if (n != 0) {
			for (int i = 0; i < ranges.get(n - 1); i++) {
				indices.push(i);
				counter = recursiveFor(indices, names, ranges, n - 1, counter, totalEvals, values, monitor);
				indices.pop();
			}
		} else {			
			counter = runEvalStep(indices, names, values, counter, totalEvals, monitor);
		}
		return counter;
	}

	private int runEvalStep(Deque<Integer> index, List<String> names, Map<String, List<String>> values, int counter, int totalEvals, IProgressMonitor monitor) throws Exception {
		IServerExecutor executor = (IServerExecutor) Activator.getExecutor();
		NumberFormat nf = NumberFormat.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("ddMMyy-HHmmss");
		Calendar cal = Calendar.getInstance();
		String date = dateFormat.format(cal.getTime());
		ISession caller = OdysseusRCPPlugIn.getActiveSession();
		Map<String, String> currentValues = new TreeMap<>();
		Integer[] pointers = index.toArray(new Integer[0]);
		for (int i = pointers.length - 1; i >= 0; i--) {
			String name = names.get(i);
			String value = values.get(name).get(pointers[i]);
			currentValues.put(name, value);

		}
		for (int i = 1; i <= number; i++) {
			counter++;
			String prefix = "Performing Evaluation number " + i + " / " + number + " for total " + counter + "/" + totalEvals + "\n";
			if (monitor.isCanceled()) {
				throw new InterruptedException();
			}
			String thislines = allLines.replaceAll("SPECIAL_STAMP", date + "-" + i);
			for (Entry<String, String> currentValue : currentValues.entrySet()) {
				prefix = prefix + " - " + currentValue.getKey() + ": " + currentValue.getValue() + "\n";
				thislines = thislines.replaceAll(Pattern.quote("${"+currentValue.getKey()+"}"), currentValue.getValue());
			}
			monitor.subTask(prefix + "Executing Script \"" + file.getName() + "\"... ");
			Context context = ParserClientUtil.createRCPContext(file);
			long timeStarted = System.currentTimeMillis();
			Collection<Integer> ids = executor.addQuery(thislines, "OdysseusScript", caller, "Standard", context);
			monitor.subTask(prefix + "Running query and waiting for stop...");
			for(int id : ids){
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

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		if (eventArgs.getEventType().equals(PlanModificationEventType.QUERY_STOP)) {
			System.out.println("query stopped!");
			synchronized (this) {
				this.notifyAll();
			}
		}

	}

}
