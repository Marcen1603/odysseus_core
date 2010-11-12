package de.uniol.inf.is.odysseus.usermanagement;

public class PercentileContraint implements
		IPercentileConstraint {
	
	private static final long serialVersionUID = 3932940457600499058L;
	final double highSlaConformanceLevel;
	final double lowSlaConformanceLevel;
	final double width;
	final double penalty;
	final boolean highInside;

	public PercentileContraint(double highSlaConformanceLevel,
			double lowSlaConformanceLevel, double penalty, boolean highInside) {
		if (highSlaConformanceLevel > lowSlaConformanceLevel){
			this.highSlaConformanceLevel = highSlaConformanceLevel;
			this.lowSlaConformanceLevel = lowSlaConformanceLevel;
		}else{
			this.highSlaConformanceLevel = lowSlaConformanceLevel;
			this.lowSlaConformanceLevel = highSlaConformanceLevel;			
		}
		this.width = highSlaConformanceLevel-lowSlaConformanceLevel;
		this.penalty = penalty;
		this.highInside = highInside;
	}

	public PercentileContraint(double high, double low, double penalty) {
		this(high,low,penalty,(high==1.0 || low==1.0));
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
	
	@Override
	public boolean highInside() {
		return highInside;
	}
}
