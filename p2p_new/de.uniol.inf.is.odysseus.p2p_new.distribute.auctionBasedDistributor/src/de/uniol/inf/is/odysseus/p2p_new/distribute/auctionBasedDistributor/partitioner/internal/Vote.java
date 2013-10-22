package de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.partitioner.internal;

public class Vote implements Comparable<Vote> {
	private final double bid;
	private final double percentageOfBearableCosts;
	private final int count;
	
	public Vote(double bid, double percentageOfBearableCosts, int count) {
		this.bid = bid;
		this.percentageOfBearableCosts = percentageOfBearableCosts;
		this.count = count;
	}
	
	public double getBid() {
		return bid;
	}
	
	public double getPercentageOfBearableCosts() {
		return percentageOfBearableCosts;
	}
	
	public double calcAverageBid() {
		return bid/count;
	}
	
	public int getCount() {
		return count;
	}

	@Override
	public int compareTo(Vote o) {
		double thisValue = this.getPercentageOfBearableCosts()*this.calcAverageBid();
		double otherValue = o.getPercentageOfBearableCosts()*o.calcAverageBid();
		if(thisValue>otherValue) {
			return 1;
		}
		else if(thisValue<otherValue) {
			return -1;
		}
		return 0;
	}
}
