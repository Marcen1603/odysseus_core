package de.uniol.inf.is.odysseus.p2p.peer;

public interface ILogListener {

	void addEvent(String queryId, String event);

	void addAdminPeer(String queryId, String adminPeerName);

	void addQuery(String queryId);

	void logAction(String queryId, String action);

	void setSubplans(String queryId, int subplans);

	void setSplittingStrategy(String queryId, String splittingStrategy);

	void removeQueryOrSubplan(String id);

	void addBid(String queryId, Integer bids);

	void logEvent(String queryId, String event);

	void setScheduler(String queryId, String scheduler);

	void setSchedulerStrategy(String queryId, String schedulerStrategy);

	void addStatus(String queryId, String string);

	void addTab(String id, String query);

	void addResult(String queryId, Object o);

	void setStatus(String queryId, String status);

}
