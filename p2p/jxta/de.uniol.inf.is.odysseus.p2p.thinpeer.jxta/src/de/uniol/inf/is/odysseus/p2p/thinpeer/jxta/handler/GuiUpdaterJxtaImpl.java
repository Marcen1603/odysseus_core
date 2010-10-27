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

	public GuiUpdaterJxtaImpl() {
	}

	@Override
	public void run() {

		while (true) {

			try {
				Thread.sleep(UPDATE_TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			this.updateQueryList();
			this.updateAdminList();
			this.updateSourcesList();

		}
	}

	public void updateQueryList() {
		synchronized (ThinPeerJxtaImpl.getInstance().getQueries()) {
			DefaultListModel model = new DefaultListModel();
			for (Query q: ThinPeerJxtaImpl.getInstance().getQueries()
					.keySet()) {
				model
						.addElement(q.getId()
								+ "("
								+ getAllBiddings(q) + ") - " + q.getStatus());
			}
			ThinPeerJxtaImpl.getInstance().getGui().getQuerys().setModel(model);
		}

	}

	public void updateAdminList() {
		synchronized (ThinPeerJxtaImpl.getInstance().getAdminPeers()) {
			DefaultListModel model = new DefaultListModel();
			for (String s : ThinPeerJxtaImpl.getInstance().getAdminPeers()
					.keySet()) {
				model.addElement(((ExtendedPeerAdvertisement)ThinPeerJxtaImpl.getInstance().getAdminPeers()
						.get(s)).getPeerName().substring(19));
			}
			ThinPeerJxtaImpl.getInstance().getGui().getAdminPeers().setModel(
					model);
		}
	}

	public void updateSourcesList() {
		synchronized (ThinPeerJxtaImpl.getInstance().getAdminPeers()) {
			DefaultListModel model = new DefaultListModel();
			for (String s : ThinPeerJxtaImpl.getInstance().getSources()
					.keySet()) {
				model.addElement(s);
			}
			ThinPeerJxtaImpl.getInstance().getGui().getSources()
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
