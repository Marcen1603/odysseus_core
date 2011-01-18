package de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.handler;


import javax.swing.DefaultListModel;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.ExtendedPeerAdvertisement;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;
import de.uniol.inf.is.odysseus.p2p.thinpeer.handler.IGuiUpdater;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.ThinPeerJxtaImpl;

public class GuiUpdaterJxtaImpl implements IGuiUpdater {

	// Wie oft wird die GUI aktualisiert
	private int UPDATE_TIME = 6000;
	private ThinPeerJxtaImpl thinPeerJxtaImpl;

	public GuiUpdaterJxtaImpl(ThinPeerJxtaImpl thinPeerJxtaImpl) {
		this.thinPeerJxtaImpl = thinPeerJxtaImpl;
	}

	@Override
	public void run() {

		while (true) {

			try {
				Thread.sleep(UPDATE_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			this.updateQueryList();
			this.updateAdminList();
			this.updateSourcesList();

		}
	}

	public void updateQueryList() {
		synchronized (thinPeerJxtaImpl.getQueries()) {
			DefaultListModel model = new DefaultListModel();
			for (Query q: thinPeerJxtaImpl.getQueries()
					.keySet()) {
				model
						.addElement(q.getId()
								+ "("
								+ getAllBiddings(q) + ") - " + q.getStatus());
			}
			thinPeerJxtaImpl.getGui().getQuerys().setModel(model);
		}

	}

	public void updateAdminList() {
		synchronized (thinPeerJxtaImpl.getAdminPeers()) {
			DefaultListModel model = new DefaultListModel();
			for (String s : thinPeerJxtaImpl.getAdminPeers()
					.keySet()) {
				model.addElement(((ExtendedPeerAdvertisement)thinPeerJxtaImpl.getAdminPeers()
						.get(s)).getPeerName().substring(19));
			}
			thinPeerJxtaImpl.getGui().getAdminPeers().setModel(
					model);
		}
	}

	public void updateSourcesList() {
		synchronized (thinPeerJxtaImpl.getAdminPeers()) {
			DefaultListModel model = new DefaultListModel();
			for (String s : thinPeerJxtaImpl.getSources()
					.keySet()) {
				model.addElement(s);
			}
			thinPeerJxtaImpl.getGui().getSources()
					.setModel(model);
		}
	}
	
	public int getAllBiddings(Query query) {
		int biddingCounter = 0;
		for(Subplan sub : query.getSubPlans().values()) {
			biddingCounter += sub.getBiddings().size();
		}
		return biddingCounter;
	}

}
