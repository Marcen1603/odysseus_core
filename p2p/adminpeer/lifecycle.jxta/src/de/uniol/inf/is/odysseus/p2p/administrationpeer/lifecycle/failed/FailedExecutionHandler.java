package de.uniol.inf.is.odysseus.p2p.administrationpeer.lifecycle.failed;

import java.util.Arrays;
import java.util.List;

import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public class FailedExecutionHandler<F> extends AbstractExecutionHandler<F> {

	public FailedExecutionHandler() {
		super();
		setProvidedLifecycle(Lifecycle.FAILED);
	}

	public FailedExecutionHandler(
			FailedExecutionHandler<F> failedExecutionHandler) {
		super(failedExecutionHandler);
	}

	@Override
	public IExecutionHandler<F> clone() {
		return new FailedExecutionHandler<F>(this);
	}

	@Override
	public String getName() {
		return "FailedExecutionHandler";
	}

	@Override
	public void run() {
		IExecutionListenerCallback cb = getExecutionListenerCallback();
		List<Lifecycle> history = cb.getQuery()
				.getHistory();
		// if(true){
		// getExecutionListenerCallback().changeState(Lifecycle.CLOSED);
		// return;
		// }

		// Jeder Schritt darf nur einmailg wiederholt werden
		Lifecycle priorLifecycle = cb
				.getQuery()
				.getHistory()
				.get(cb.getQuery().getHistory()
						.size() - 2);
		// Falls Failed direkt nach Failed kommt, dann exisitert der geforderte
		// Handler nicht und wir brechen hier komplett ab
		if (occurence(history,
				Arrays.asList(Lifecycle.FAILED, Lifecycle.FAILED)) > 0) {
			cb.changeState(Lifecycle.TERMINATED);
			return;
		} else {
			if (priorLifecycle == Lifecycle.NEW) {
				if (occurence(history,
						Arrays.asList(priorLifecycle, Lifecycle.FAILED)) > 0) {
					cb.changeState(
							Lifecycle.TERMINATED);
				}
			} else if (priorLifecycle == Lifecycle.SPLIT) {
				if (occurence(history,
						Arrays.asList(priorLifecycle, Lifecycle.FAILED)) < 2) {
					cb.changeState(Lifecycle.SPLIT);
				} else {
					cb.changeState(
							Lifecycle.TERMINATED);
				}

			} else if (priorLifecycle == Lifecycle.DISTRIBUTION) {
				if (occurence(history,
						Arrays.asList(priorLifecycle, Lifecycle.FAILED)) < 2) {
					cb.changeState(
							Lifecycle.DISTRIBUTION);
				} else {
					cb.changeState(
							Lifecycle.TERMINATED);
				}

			} else if (priorLifecycle == Lifecycle.GRANTED) {
				if (occurence(history,
						Arrays.asList(priorLifecycle, Lifecycle.FAILED)) < 2) {
					cb.changeState(
							Lifecycle.DISTRIBUTION);
				} else {
					cb.changeState(
							Lifecycle.TERMINATED);
				}
			} else if (priorLifecycle == Lifecycle.RUNNING) {
				if (occurence(history,
						Arrays.asList(priorLifecycle, Lifecycle.FAILED)) < 2) {
					cb.changeState(
							Lifecycle.DISTRIBUTION);
				} else {
					cb.changeState(
							Lifecycle.TERMINATED);
				}
			}
		}

	}

	private int occurence(List<Lifecycle> list, List<Lifecycle> occurence) {
		int occ = 0;
		if (occurence.size() > 0) {
			for (int counter = 0; counter + occurence.size() < list.size(); counter++) {
				boolean iterate = true;
				List<Lifecycle> tempList = list.subList(counter, counter
						+ occurence.size());
				for (int i = 0; i < tempList.size(); i++) {
					if (!(tempList.get(i) == occurence.get(i))) {
						iterate = false;
						break;
					}
				}
				if (iterate) {
					occ++;
				}
				// }
			}
		}
		return occ;
	}

}
