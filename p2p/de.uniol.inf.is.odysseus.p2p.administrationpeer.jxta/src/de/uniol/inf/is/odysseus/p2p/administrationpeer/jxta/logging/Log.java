package de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.logging;

import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.AdministrationPeerJxtaImpl;

public class Log {

	public static void addQuery(String queryId) {
		if (AdministrationPeerJxtaImpl.getInstance().isGuiEnabled()) {
			AdministrationPeerJxtaImpl.getInstance().getGui().addTab(queryId);
		}
	}

	public static void logAction(String queryId, String action) {
		if (AdministrationPeerJxtaImpl.getInstance().isGuiEnabled()) {
			AdministrationPeerJxtaImpl.getInstance().getGui().addAction(
					queryId, action);
		}
	}

	public static void setSubplans(String queryId, int subplans) {
		if (AdministrationPeerJxtaImpl.getInstance().isGuiEnabled()) {
			AdministrationPeerJxtaImpl.getInstance().getGui().addSubplans(
					queryId, subplans);
		}

	}

	public static void setSplittingStrategy(String queryId,
			String splittingStrategy) {
		if (AdministrationPeerJxtaImpl.getInstance().isGuiEnabled()) {
			AdministrationPeerJxtaImpl.getInstance().getGui().addSplitting(
					queryId, splittingStrategy);
		}
	}

	public static void setStatus(String queryId, String status) {
		if (AdministrationPeerJxtaImpl.getInstance().isGuiEnabled()) {
			AdministrationPeerJxtaImpl.getInstance().getGui().addStatus(
					queryId, status);
		}
	}

	public static void removeQuery(String queryId) {
		if (AdministrationPeerJxtaImpl.getInstance().isGuiEnabled()) {
			AdministrationPeerJxtaImpl.getInstance().getGui().removeQuery(
					queryId);
		}
	}

	public static void addBid(String queryId,Integer bids) {
		if (AdministrationPeerJxtaImpl.getInstance().isGuiEnabled()) {
			AdministrationPeerJxtaImpl.getInstance().getGui().addBids(
					queryId, bids.toString());
		}
	}

}
