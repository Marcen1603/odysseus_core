package de.uniol.inf.is.odysseus.p2p.operatorpeer.logging;

import de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.OperatorPeerJxtaImpl;

public class Log {

	public static void logAction(String queryId, String action) {
		if (OperatorPeerJxtaImpl.getInstance().isGuiEnabled()) {
			OperatorPeerJxtaImpl.getInstance().getGui().addAction(queryId,
					action);
		} else {

		}
	}

	public static void logEvent(String queryId, String event) {
		if (OperatorPeerJxtaImpl.getInstance().isGuiEnabled()) {
			OperatorPeerJxtaImpl.getInstance().getGui().addAction(queryId,
					event);
		} else {

		}

	}

	public static void addQuery(String queryId) {
		if (OperatorPeerJxtaImpl.getInstance().isGuiEnabled()) {
			if (!OperatorPeerJxtaImpl.getInstance().getGui().isQuery(queryId)) {
				OperatorPeerJxtaImpl.getInstance().getGui().addTab(queryId);
			} else {
				int count = OperatorPeerJxtaImpl.getInstance().getQueries()
						.get(queryId).getSubPlans().size();
				OperatorPeerJxtaImpl.getInstance().getGui().addSubplans(
						queryId, count);
			}
		}

	}

	public static void setScheduler(String queryId, String scheduler) {
		if (OperatorPeerJxtaImpl.getInstance().isGuiEnabled()) {
			OperatorPeerJxtaImpl.getInstance().getGui().addScheduler(queryId,
					scheduler);
		} else {

		}

	}

	public static void setSchedulerStrategy(String queryId,
			String schedulerStrategy) {
		if (OperatorPeerJxtaImpl.getInstance().isGuiEnabled()) {
			OperatorPeerJxtaImpl.getInstance().getGui().addSchedulerStrategy(
					queryId, schedulerStrategy);
		} else {

		}

	}

	public static void setStatus(String queryId, String status) {
		if (OperatorPeerJxtaImpl.getInstance().isGuiEnabled()) {
			OperatorPeerJxtaImpl.getInstance().getGui().addStatus(queryId,
					status);
		} else {

		}

	}

	public static void setSubplan(String queryid, int subplan) {
		if (OperatorPeerJxtaImpl.getInstance().isGuiEnabled()) {

		} else {

		}

	}

}
