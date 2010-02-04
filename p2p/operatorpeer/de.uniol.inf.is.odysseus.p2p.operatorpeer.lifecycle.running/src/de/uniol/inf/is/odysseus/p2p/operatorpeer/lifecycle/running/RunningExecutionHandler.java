package de.uniol.inf.is.odysseus.p2p.operatorpeer.lifecycle.running;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.uniol.inf.is.odysseus.logicaloperator.base.AlgebraPlanToStringVisitor;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.util.AbstractTreeWalker;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterPriority;

public class RunningExecutionHandler extends AbstractExecutionHandler<AbstractPeer, IAdvancedExecutor> {

	public RunningExecutionHandler() {
		super();
		setProvidedLifecycle(Lifecycle.RUNNING);
	}
	
	@Override
	public IExecutionHandler<AbstractPeer, IAdvancedExecutor> clone() throws CloneNotSupportedException {
		IExecutionHandler<AbstractPeer, IAdvancedExecutor> handler = new RunningExecutionHandler();
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
				getFunction().addQuery(s.getAo(), new ParameterPriority(2));		
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
			if(m.getReturnType().toString().equals("interface de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor")) {
				try {
					setFunction((IAdvancedExecutor) m.invoke(peer,(Object[])null));
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
