package de.uniol.inf.is.odysseus.p2p.thinpeer.peerImpl.jxta.handler;

import javax.swing.DefaultListModel;

import de.uniol.inf.is.odysseus.p2p.thinpeer.handler.IGuiUpdater;
import de.uniol.inf.is.odysseus.p2p.thinpeer.peerImpl.jxta.ThinPeerJxtaImpl;

public class GuiUpdaterJxtaImpl implements IGuiUpdater {

	// Wie oft wird die GUI aktualisiert
	private int UPDATE_TIME = 6000;

	public GuiUpdaterJxtaImpl() {
	}

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
			for (String s : ThinPeerJxtaImpl.getInstance().getQueries()
					.keySet()) {
				model
						.addElement((ThinPeerJxtaImpl.getInstance()
								.getQueries().get(s).getId()
								+ "("
								+ ThinPeerJxtaImpl.getInstance().getQueries()
										.get(s).getBiddings().size() + ") - " + ThinPeerJxtaImpl
								.getInstance().getQueries().get(s).getStatus()));
			}
			ThinPeerJxtaImpl.getInstance().getGui().getQuerys().setModel(model);
		}

	}

	public void updateAdminList() {
		synchronized (ThinPeerJxtaImpl.getInstance().getAdminPeers()) {
			DefaultListModel model = new DefaultListModel();
			for (String s : ThinPeerJxtaImpl.getInstance().getAdminPeers()
					.keySet()) {
				model.addElement(ThinPeerJxtaImpl.getInstance().getAdminPeers()
						.get(s).getPeerName().substring(19));
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

}
