package de.uniol.inf.is.odysseus.base.usermanagement;

public class PercentileContraint implements
		IPercentileConstraint {
	final double highSlaConformanceLevel;
	final double lowSlaConformanceLevel;
	final double width;
	final double penalty;
	final boolean highInside;

	public PercentileContraint(double highSlaConformanceLevel,
			double lowSlaConformanceLevel, double penalty, boolean highInside) {
		this.highSlaConformanceLevel = highSlaConformanceLevel;
		this.lowSlaConformanceLevel = lowSlaConformanceLevel;
		this.width = highSlaConformanceLevel-lowSlaConformanceLevel;
		this.penalty = penalty;
		this.highInside = highInside;
	}

	public PercentileContraint(double d, double e, int i) {
		this(d,e,i,false);
	}

	@Override
	public int compareTo(IPercentileConstraint o) {
		return -1*Double.compare(this.getLowSlaConformanceLevel(), o.getLowSlaConformanceLevel());
	}
	
	@Override
	public double getHighSlaConformanceLevel() {
		return highSlaConformanceLevel;
	}
	
	@Override
	public double getLowSlaConformanceLevel() {
		return lowSlaConformanceLevel;
	}
	
	@Override
	public double getPenalty() {
		return penalty;
	}
	
	@Override
	public String toString() {
		return "["+lowSlaConformanceLevel+", "+highSlaConformanceLevel+(highInside?"]":")")+" p="+penalty;
	}

	@Override
	public boolean contains(double currentSLAConformance) {
		if (highInside){
			return lowSlaConformanceLevel <= currentSLAConformance && currentSLAConformance <= highSlaConformanceLevel; 
		}else{
			return lowSlaConformanceLevel <= currentSLAConformance && currentSLAConformance < highSlaConformanceLevel;
		}
	}
	
	@Override
	public boolean overlaps(IPercentileConstraint other){
		return !(this.getHighSlaConformanceLevel() <= other.getLowSlaConformanceLevel() ||
				this.getLowSlaConformanceLevel() >= other.getHighSlaConformanceLevel());
	}

	@Override
	public double getWidth() {
		return width;
	}
}
