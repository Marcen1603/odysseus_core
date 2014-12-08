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

	private EvaluationModel model;
	private Collection<Integer> ids = new ArrayList<>();	
	private static final String PRE_TRANSFORM_TOKEN = "#PRETRANSFORM EvaluationPreTransformation";
	private static final CharSequence METADATA_LATENCY = "#METADATA Latency";

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
				String lines = fileToLines(model.getQueryFile());
				
				// save model file for logging purposes
				String modelFileBackup = FilenameUtils.concat(evaluationRunContext.getResultsPathIdentified(), "model.eval");
				model.save(new File(modelFileBackup));
				// and the current query file
				String queryFileBackup = FilenameUtils.concat(evaluationRunContext.getResultsPathIdentified(), model.getQueryFile().getName());
				FileUtils.write(new File(queryFileBackup), lines);
				
				// then, prepare lines for executing
				lines = prepareQueryFileForEvaluation(lines);
				monitor.beginTask("Running evaluations...", totalEvaluations);				
				
				EvaluationRunContainer evaluationRunContainer = new EvaluationRunContainer(evaluationRunContext); 
				int counter = recursiveFor(new ArrayDeque<Integer>(), ranges, ranges.size(), 0, totalEvaluations, evaluationRunContainer, variables, monitor, lines);
				if (counter < totalEvaluations) {
					return Status.CANCEL_STATUS;
				}
				
				monitor.beginTask("Creating diagrams...", IProgressMonitor.UNKNOWN);
				if(model.isCreateLatencyPlots()){
					monitor.subTask("Creating latency plots...");					
					PlotBuilder.createLatencyPlots(evaluationRunContainer, model, monitor);					
				}
				if(model.isCreateThroughputPlots()){
					monitor.subTask("Creating throughput plots...");
					PlotBuilder.createThroughputPlots(evaluationRunContainer, model, monitor);
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

	private String prepareQueryFileForEvaluation(String lines) {
		if(!lines.contains(METADATA_LATENCY)){
			lines = METADATA_LATENCY + System.lineSeparator()+lines; 
		}
		if(!lines.contains(PRE_TRANSFORM_TOKEN)){
			lines = PRE_TRANSFORM_TOKEN+System.lineSeparator()+lines;
		}
		
		return lines;
	}

	private int recursiveFor(Deque<Integer> indices, List<Integer> ranges, int n, int counter, int totalEvals, EvaluationRunContainer evaluationRunContainer, List<EvaluationVariable> values, IProgressMonitor monitor, String lines) throws Exception {
		if (n != 0) {
			for (int i = 0; i < ranges.get(n - 1); i++) {
				indices.push(i);
				counter = recursiveFor(indices, ranges, n - 1, counter, totalEvals, evaluationRunContainer, values, monitor, lines);
				indices.pop();
			}
		} else {
			counter = runEvalStep(indices, values, counter, totalEvals, evaluationRunContainer, monitor, lines);
		}
		return counter;
	}

	private int runEvalStep(Deque<Integer> index, List<EvaluationVariable> values, int counter, int totalEvals, EvaluationRunContainer evaluationRunContainer, IProgressMonitor monitor, String querytext) throws Exception {
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
			EvaluationRun evaluationRun = new EvaluationRun(evaluationRunContainer.getContext(), i, currentValues);
			evaluationRunContainer.getRuns().add(evaluationRun);
			context.put(EvaluationRun.class.getName(), evaluationRun);
			
			ids  = executor.addQuery(querytext, "OdysseusScript", caller, context);
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
