package query;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IUpdateEventListener;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;

public class MonitoringCPU implements IUpdateEventListener {

	private IServerExecutor executor;

	public MonitoringCPU(){
		
	}
	public void bindExecutor(IExecutor ex) throws PlanManagementException {
		executor = (IServerExecutor) ex;
		String name = executor.getName();
		if (name == null) {
			name = "";
		}
		executor.addUpdateEventListener(this, IUpdateEventListener.QUERY, null);
	}

	public void unbindExecutor(IExecutor ex) {
		if (executor == (IServerExecutor) ex) {
			executor.removeUpdateEventListener(this, IUpdateEventListener.QUERY, null);
			executor = null;
		}
	}
	@Override
	public void eventOccured(String type) {
		// TODO Auto-generated method stub
		
	}
}
