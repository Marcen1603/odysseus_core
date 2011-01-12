package de.uniol.inf.is.odysseus.p2p.administrationpeer.lifecycle.open;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

public class OpenExecutionHandler extends AbstractExecutionHandler<AbstractPeer, ICompiler> {

	public OpenExecutionHandler() {
		super();
		setProvidedLifecycle(Lifecycle.NEW);
	}
	
	@Override
	public IExecutionHandler<AbstractPeer, ICompiler> clone()  {
		IExecutionHandler<AbstractPeer, ICompiler> handler = new OpenExecutionHandler();
		handler.setFunction(getFunction());
		handler.setPeer(getPeer());
		handler.setExecutionListenerCallback(getExecutionListenerCallback());
		return handler;
	}

	@Override
	public String getName() {
		return "OpenExecutionHandler";
	}

	@Override
	public void run() {
		if(getFunction()!=null && getPeer()!=null && getExecutionListenerCallback()!=null) {
			List<IQuery> plan = null;
			try {
				//Anfrage Übersetzen und in Query kapseln
				plan = getFunction().translateQuery(getExecutionListenerCallback().getQuery().getDeclarativeQuery(), getExecutionListenerCallback().getQuery().getLanguage(), getExecutionListenerCallback().getQuery().getUser(), getExecutionListenerCallback().getQuery().getDataDictionary());
			} catch (QueryParseException e3) {
				e3.printStackTrace();
				Log.logAction(getExecutionListenerCallback().getQuery().getId(), "Fehler bei der Übersetzung der Anfrage");
				getExecutionListenerCallback().changeState(Lifecycle.FAILED);
//				sendSourceFailure(getExecutionListenerCallback().getQuery().getId());
				return;
			} catch (Throwable e2) {
				e2.printStackTrace();
//				sendSourceFailure(getExecutionListenerCallback().getQuery().getId());
				getExecutionListenerCallback().changeState(Lifecycle.FAILED);
				return;
			}			
			try {
			//Restrukturierung des Operatorplans
			ILogicalOperator restructPlan = getFunction().rewritePlan(plan.get(0).getLogicalPlan(), null);
			getExecutionListenerCallback().getQuery().setLogicalOperatorplan(restructPlan);
			} catch (Exception e) {
				e.printStackTrace();
				getExecutionListenerCallback().changeState(Lifecycle.FAILED);
				return;
			}
		} else {
			getExecutionListenerCallback().changeState(Lifecycle.FAILED);
			return;
		}
		getExecutionListenerCallback().changeState(Lifecycle.SUCCESS);
	}
	
	@Override
	public void setPeer(AbstractPeer peer) {
		super.setPeer(peer);
		Method[] methods = peer.getClass().getMethods();
		for(Method m : methods) {
			if(m.getReturnType().toString().equals("interface de.uniol.inf.is.odysseus.planmanagement.ICompiler")) {
				try {
					setFunction((ICompiler) m.invoke(peer,(Object[])null));
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
