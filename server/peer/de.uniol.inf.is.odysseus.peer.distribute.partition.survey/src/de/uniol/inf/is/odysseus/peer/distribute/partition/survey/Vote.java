package de.uniol.inf.is.odysseus.peer.distribute.partition.survey;

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
		double thisValue = this.getPercentageOfBearableCosts();
		double otherValue =  o.getPercentageOfBearableCosts();
		
		// workaround, falls die peers ein gebot von 0 abgeben, weil sie die quellen nicht besitzen
		// das werden sieimmer tun, wenn sie den gesamten plan bewerten sollen
		if(this.calcAverageBid()>0)
			thisValue *= this.calcAverageBid();
		
		if(o.calcAverageBid()>0)
			otherValue *= o.calcAverageBid();
		
		if(thisValue>otherValue) {
			return 1;
		}
		else if(thisValue<otherValue) {
			return -1;
		}
		return 0;
	}
}
