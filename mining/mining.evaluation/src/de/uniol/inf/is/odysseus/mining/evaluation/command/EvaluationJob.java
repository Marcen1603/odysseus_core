package de.uniol.inf.is.odysseus.mining.evaluation.command;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;

public class EvaluationJob extends Job implements IPlanModificationListener {

	private int number;
	private String allLines;
	private IFile file;

	public EvaluationJob(String name, int number, String allLines, IFile file) {
		super(name);
		this.number = number;
		this.allLines = allLines;
		this.file = file;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		IServerExecutor executor = (IServerExecutor) OdysseusRCPEditorTextPlugIn.getExecutor();
		executor.addPlanModificationListener(this);
		ISession caller = OdysseusRCPPlugIn.getActiveSession();
		try {
			synchronized (this) {
				DateFormat dateFormat = new SimpleDateFormat("ddMMyy-HHmmss");
				Calendar cal = Calendar.getInstance();
				String date = dateFormat.format(cal.getTime());
				for (int window = 100; window <= 1000000; window=window*10) {
					for (int i = 1; i <= number; i++) {
						String prefix = "Performing Evaluation Number " + i + " / "+number+" for window size "+window+"\n";
						if (monitor.isCanceled()) {
							return Status.CANCEL_STATUS;
						}
						monitor.subTask(prefix + "Executing Script \"" + file.getName() + "\"... ");
						String thislines = allLines.replaceAll("SPECIAL_STAMP", date + "-" + i);
						thislines = thislines.replaceAll("SET_WINDOW_SIZE", window+"");
						Collection<Integer> ids = executor.addQuery(thislines, "OdysseusScript", caller, "Standard");
						monitor.subTask(prefix + "Running query and waiting for stop...");
						this.wait();
						monitor.subTask(prefix + "Process done. Removing query...");
						for (Integer id : ids) {
							executor.removeQuery(id, caller);
						}
						monitor.subTask(prefix + "Run done. Starting next...");
					}
				}
			}

		} catch (Throwable ex) {
			ex.printStackTrace();
			return Status.CANCEL_STATUS;
		}
		return Status.OK_STATUS;
	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		if (eventArgs.getEventType().equals(PlanModificationEventType.QUERY_STOP)) {
			synchronized (this) {
				this.notify();
			}
		}

	}

}
