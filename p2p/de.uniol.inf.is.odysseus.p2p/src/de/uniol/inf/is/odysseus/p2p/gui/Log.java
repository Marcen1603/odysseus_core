package de.uniol.inf.is.odysseus.p2p.gui;


import de.uniol.inf.is.odysseus.p2p.gui.AbstractMainWindow;


public class Log {
	
	private static AbstractMainWindow window;



	public static void setWindow(AbstractMainWindow w) {
		window = w;
	}
	
	public static AbstractMainWindow getWindow() {
		return window;
	}

	public static void addQuery(String queryId) {
		getWindow().addTab(queryId);
	}
//	public static void addQuery(String queryId) {
//			if (!getWindow().isQuery(queryId)) {
////				getWindow().addTab(queryId);
//			} else {
////				int count = OperatorPeerJxtaImpl.getInstance().getQueries()
////						.get(queryId).getSubPlans().size();
////				OperatorPeerJxtaImpl.getInstance().getGui().addSubplans(
////						queryId, count);
//				System.out.println("komm hier rein");
//			}
//
//	}

	public static void logAction(String queryId, String action) {
			getWindow().addAction(
					queryId, action);
	}

	public static void setSubplans(String queryId, int subplans) {
			getWindow().addSubplans(
					queryId, subplans);

	}

	public static void setSplittingStrategy(String queryId,
			String splittingStrategy) {
			getWindow().addSplitting(
					queryId, splittingStrategy);
	}

	public static void setStatus(String queryId, String status) {
			getWindow().addStatus(
					queryId, status);
	}

	public static void removeQuery(String queryId) {
			getWindow().removeQuery(
					queryId);
	}

	public static void addBid(String queryId,Integer bids) {
			getWindow().addBids(
					queryId, bids.toString());
	}
	
	public static void logEvent(String queryId, String event) {
			getWindow().addAction(queryId,
					event);

	}
	
	public static void setScheduler(String queryId, String scheduler) {
			getWindow().addScheduler(queryId,
					scheduler);

	}

	public static void setSchedulerStrategy(String queryId,
			String schedulerStrategy) {
			getWindow().addSchedulerStrategy(
					queryId, schedulerStrategy);

	}

	public static void addAdminPeer(String queryId, String adminPeerName) {
		getWindow().addAdminPeer(queryId, adminPeerName);
		
	}

	public static void addStatus(String queryId, String string) {
		getWindow().addStatus(queryId, string);
		
	}

	public static void addTab(String id, String query) {
		getWindow().addTab(id, query);
		
	}

	public static void addResult(String queryId, Object o) {
		getWindow().addResult(queryId, o);
		
	}
	
	public static void addEvent(String queryId, String event) {
		getWindow().addEvent(queryId, event);
	}



}
