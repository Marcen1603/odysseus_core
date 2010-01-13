package de.uniol.inf.is.odysseus.p2p.thinpeer.handler;

public interface IQueryPublisher {
	
	void publishQuerySpezification(String queryId, String query, String language);
	
	//Anfrage wird nicht ausgeschrieben sondern direkt an einen AdminPeer gesendet.
	void sendQuerySpezificationToAdminPeer(String queryId, String query, String language, String adminPeer);

}
