package de.uniol.inf.is.odysseus.p2p.distribution.bidding.client;

public class MaxQueryBiddingStrategyJxtaImpl implements IBiddingStrategy {

	private static int MAXQUERIES = 10;

	public boolean doBidding(String query) {
//		int i = 0;
//		for (String s : OperatorPeerJxtaImpl.getInstance().getQueries()
//				.keySet()) {
//			Query q = OperatorPeerJxtaImpl.getInstance().getQueries().get(s);
//			if (q.getStatus() == Query.Status.RUN) {
//				i++;
//			}
//
//		}
//		if (i >= MAXQUERIES) {
//			System.out.println("nein biete nicht");
//			return false;
//		}
//		else {
//			System.out.println("ja biete");
//			return true;
//		}
		return true;
	}

}
