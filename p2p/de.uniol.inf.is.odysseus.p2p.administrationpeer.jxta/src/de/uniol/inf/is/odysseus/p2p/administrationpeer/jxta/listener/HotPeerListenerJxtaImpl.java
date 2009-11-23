package de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import net.jxta.endpoint.Message;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.p2p.logicaloperator.P2PSinkAO;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.listener.IHotPeerListener;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.AdministrationPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan.SubplanStatus;
import de.uniol.inf.is.odysseus.p2p.jxta.QueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.SubplanJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.AdvertisementTools;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;

public class HotPeerListenerJxtaImpl implements IHotPeerListener {

	private int MINHOTPEERS = 1;
	
	private int RETRY=10;
	
	private int WAIT_TIME=5000;
	
	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(WAIT_TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (AdministrationPeerJxtaImpl.getInstance().getOperatorPeers().size()==0)
				continue;
			for (String s : AdministrationPeerJxtaImpl.getInstance().getDistributionProvider().getManagedQueries().keySet()){
				QueryJxtaImpl q = (QueryJxtaImpl)AdministrationPeerJxtaImpl.getInstance().getDistributionProvider().getManagedQueries().get(s);
				for (Subplan subPlan : q.getSubPlans().values()){
					if (subPlan.getPeerId().equals("") || subPlan.getStatus()!=SubplanStatus.CLOSED){
						continue;
					}
					//Wenn für einen Subplan zu wenig HotPeers vorhanden sind dann müssen neue gesucht werden.
					if(((SubplanJxtaImpl) subPlan).getHotPeers().size() < MINHOTPEERS){
						//Anfrage zusammen bauen
						//Message hotPeerQuery = MessageTool.createSimpleMessage("HotQuery", "queryId", AdministrationPeerJxtaImpl.getInstance().getQueries().get(s).getId(),((SocketServerListenerJxtaImpl) AdministrationPeerJxtaImpl.getInstance().getSocketServerListener()).getServerPipeAdvertisement()); 
						
						ArrayList<String> list = setToList(AdministrationPeerJxtaImpl.getInstance().getOperatorPeers().keySet());
						int peer=0;
						int y = 0;
						if (list.size()<RETRY){
							y = list.size();
						}
						else{
							y=RETRY;
						}
						
						for (int x=0;x<y;x++){
							boolean help = false;
							//Auswahl eines Peers für HotPeer mittels Zufallsstrategie
							peer = (Integer) AdministrationPeerJxtaImpl.getInstance().getHotPeerStrategy().getHotPeer(list);
							String peerId = list.get(peer);
							//Führt der Peer den Plan vielleicht gerade schon aus ?
							if (!peerId.equals(((SubplanJxtaImpl) subPlan).getPeerId())){
								//Ist der Peer vielleicht schon ein HotPeer ?
								for (String a : ((SubplanJxtaImpl) subPlan).getHotPeers().keySet()){
									if (a.equals(peerId)){
										help = true;
									}
								}
								if (help)
									continue;
								//Sende HotPeerAufforderung
		
								//Ein neues Advertisement setzen
								AbstractLogicalOperator ao = subPlan.getAo().clone();
								((P2PSinkAO) ao).setAdv(AdvertisementTools.getServerPipeAdvertisement(AdministrationPeerJxtaImpl.getInstance().getNetPeerGroup()).toString());
								//Ende neues Advertisement setzen
													
								HashMap<String, Object> messageElements = new HashMap<String, Object>();
								messageElements.put("queryId", AdministrationPeerJxtaImpl.getInstance().getDistributionProvider().getManagedQueries().get(s).getId());
								messageElements.put("result", "granted");
								messageElements.put("operatorplan", ao);
								
								Message response = MessageTool.createSimpleMessage(
										"HotQuery", messageElements);
								MessageTool.sendMessage(AdministrationPeerJxtaImpl.getInstance().getNetPeerGroup(), MessageTool.createPipeAdvertisementFromXml(AdministrationPeerJxtaImpl.getInstance().getOperatorPeers().get(peerId).getPipe()), response);
								((SubplanJxtaImpl) subPlan).getHotPeers().put(peerId, MessageTool.createPipeAdvertisementFromXml(((P2PSinkAO) ao).getAdv()));
								break;
							}
						}
					}
				}
			}
			
		}

	}
	
	private ArrayList<String> setToList(Set<String> set){
		
		Iterator<String> it = set.iterator();
		ArrayList<String> temp = new ArrayList<String>();
		while (it.hasNext()){
			temp.add(it.next());
		}
		return temp;
		
	}
	
}
