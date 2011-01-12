package de.uniol.inf.is.odysseus.p2p.operatorpeer.lifecycle.running;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.AlgebraPlanToStringVisitor;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterPriority;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;
import de.uniol.inf.is.odysseus.util.AbstractTreeWalker;

public class RunningExecutionHandler extends AbstractExecutionHandler<AbstractPeer, IExecutor> {

	public RunningExecutionHandler() {
		super();
		setProvidedLifecycle(Lifecycle.RUNNING);
	}
	
	@Override
	public IExecutionHandler<AbstractPeer, IExecutor> clone()  {
		IExecutionHandler<AbstractPeer, IExecutor> handler = new RunningExecutionHandler();
		handler.setFunction(getFunction());
		handler.setPeer(getPeer());
		handler.setExecutionListenerCallback(getExecutionListenerCallback());
		return handler;
	}

	@Override
	public String getName() {
		return "RunningExecutionHandler";
	}

	@Override
	public void run() {
		System.out.println("running wird ausgeführt");
		try {

		for(Subplan s :getExecutionListenerCallback().getQuery().getSubPlans().values()) {
			if(s.getStatus() == Lifecycle.GRANTED) {
				System.out.println("Füge hinzu: "+AbstractTreeWalker.prefixWalk(s.getAo(),
						new AlgebraPlanToStringVisitor()));
				User user = GlobalState.getActiveUser();
				IDataDictionary dd = GlobalState.getActiveDatadictionary();
				getFunction().addQuery(s.getAo(), user, dd, new ParameterPriority(2));		
			}
		}
		
		
		if(!getFunction().isRunning()) {
			getFunction().startExecution();
		}
	} catch (PlanManagementException e2) {
		e2.printStackTrace();
	}
		//Lasse eine bestimmte Zeit laufen, bis Anfrage beendet wird
//		try {
//			Thread.sleep(100000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		getExecutionListenerCallback().changeState(Lifecycle.SUCCESS);
	}
	
	@Override
	public void setPeer(AbstractPeer peer) {
		super.setPeer(peer);
		Method[] methods = peer.getClass().getMethods();
		for(Method m : methods) {
			if(m.getReturnType().toString().equals("interface de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor")) {
				try {
					setFunction((IExecutor) m.invoke(peer,(Object[])null));
					break;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
