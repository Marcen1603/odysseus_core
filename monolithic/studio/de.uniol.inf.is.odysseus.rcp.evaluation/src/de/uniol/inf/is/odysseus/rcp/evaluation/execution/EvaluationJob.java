package de.uniol.inf.is.odysseus.rcp.evaluation.execution;

import java.io.BufferedReader;
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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
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
import de.uniol.inf.is.odysseus.rcp.evaluation.model.EvaluationModel;
import de.uniol.inf.is.odysseus.rcp.evaluation.model.EvaluationVariable;
import de.uniol.inf.is.odysseus.rcp.queries.ParserClientUtil;

public class EvaluationJob extends Job implements IPlanModificationListener {

	private EvaluationModel model;
	private Collection<Integer> ids = new ArrayList<>();

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
				String lines = fileToLines(model.getQueryFile());

				monitor.beginTask("Running evaluations...", totalEvaluations);				
				DateFormat dateFormat = new SimpleDateFormat("ddMMyy-HHmmss");
				Calendar cal = Calendar.getInstance();
				String identifier = dateFormat.format(cal.getTime());
				int counter = recursiveFor(new ArrayDeque<Integer>(), ranges, ranges.size(), 0, totalEvaluations, identifier, variables, monitor, lines);
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

	private int recursiveFor(Deque<Integer> indices, List<Integer> ranges, int n, int counter, int totalEvals, String identifier, List<EvaluationVariable> values, IProgressMonitor monitor, String lines) throws Exception {
		if (n != 0) {
			for (int i = 0; i < ranges.get(n - 1); i++) {
				indices.push(i);
				counter = recursiveFor(indices, ranges, n - 1, counter, totalEvals, identifier, values, monitor, lines);
				indices.pop();
			}
		} else {
			counter = runEvalStep(indices, values, counter, totalEvals, identifier, monitor, lines);
		}
		return counter;
	}

	private int runEvalStep(Deque<Integer> index, List<EvaluationVariable> values, int counter, int totalEvals, String identifier, IProgressMonitor monitor, String querytext) throws Exception {
		IServerExecutor executor = (IServerExecutor) Activator.getExecutor();
		NumberFormat nf = NumberFormat.getInstance();

		ISession caller = OdysseusRCPPlugIn.getActiveSession();

		Map<String, String> currentValues = new TreeMap<>();
		Integer[] pointers = index.toArray(new Integer[0]);
		for (int i = 0; i<pointers.length; i++) {
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
			for (Entry<String, String> currentValue : currentValues.entrySet()) {
				prefix = prefix + " - " + currentValue.getKey() + ": " + currentValue.getValue() + "\n";
				querytext = querytext.replaceAll(Pattern.quote("${" + currentValue.getKey() + "}"), currentValue.getValue());
			}
			monitor.subTask(prefix + "Executing Script \"" + model.getQueryFile().getName() + "\"... ");			
			Context context = ParserClientUtil.createRCPContext((IFile) model.getQueryFile());
			long timeStarted = System.currentTimeMillis();

			monitor.subTask(prefix + "Adding query...");
			EvaluationRun evaluationRun = new EvaluationRun(model, i, currentValues, identifier);
			
			context.put(EvaluationRun.class.getName(), evaluationRun);
			
			ids  = executor.addQuery(querytext, "OdysseusScript", caller, "Standard", context);
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
	

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		if (eventArgs.getEventType().equals(PlanModificationEventType.QUERY_STOP)) {
//			System.out.println("query stopped!");
			synchronized (this) {
				this.notifyAll();
			}
		}
	}
	
	@Override
	protected void canceling() {	
		IServerExecutor executor = (IServerExecutor) Activator.getExecutor();
		ISession caller = OdysseusRCPPlugIn.getActiveSession();
		for(Integer id : ids){
			executor.stopQuery(id, caller);
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
