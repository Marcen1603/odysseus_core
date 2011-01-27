package de.uniol.inf.is.odysseus.p2p.gui;


import de.uniol.inf.is.odysseus.p2p.peer.ILogListener;


public class Log implements ILogListener{
	
	static private Log instance = new Log();
	
	public static Log getInstance() {
		return instance;
	}
	
	private AbstractMainWindow window;

	public void setWindow(AbstractMainWindow w) {
		window = w;
	}
	
	public AbstractMainWindow getWindow() {
		return window;
	}

	@Override
	public void addQuery(String queryId) {
		getWindow().addTab(queryId);
	}

	@Override
	public void removeQueryOrSubplan(String id) {
		getWindow().removeTab(id);
	}
	
	
	@Override
	public void logAction(String queryId, String action) {
			getWindow().addAction(
					queryId, action);
	}

	@Override
	public void setSubplans(String queryId, int subplans) {
			getWindow().addSubplans(
					queryId, subplans);

	}

	@Override
	public void setSplittingStrategy(String queryId,
			String splittingStrategy) {
			getWindow().addSplitting(
					queryId, splittingStrategy);
	}

	@Override
	public void setStatus(String queryId, String status) {
			getWindow().addStatus(
					queryId, status);
	}

	@Override
	public void addBid(String queryId,Integer bids) {
			getWindow().addBids(
					queryId, bids.toString());
	}
	
	@Override
	public void logEvent(String queryId, String event) {
			getWindow().addAction(queryId,
					event);

	}
	
	@Override
	public void setScheduler(String queryId, String scheduler) {
			getWindow().addScheduler(queryId,
					scheduler);

	}
	
	@Override
	public void setSchedulerStrategy(String queryId,
			String schedulerStrategy) {
			getWindow().addSchedulerStrategy(
					queryId, schedulerStrategy);

	}

	public void addAdminPeer(String queryId, String adminPeerName) {
		getWindow().addAdminPeer(queryId, adminPeerName);
		
	}

	@Override
	public void addStatus(String queryId, String string) {
		getWindow().addStatus(queryId, string);
		
	}

	@Override
	public void addTab(String id, String query) {
		getWindow().addTab(id, query);
		
	}

	@Override
	public void addResult(String queryId, Object o) {
		getWindow().addResult(queryId, o);
		
	}
	
	public void addEvent(String queryId, String event) {
		getWindow().addEvent(queryId, event);
	}



}
