package de.uniol.inf.is.odysseus.base.usermanagement;

public interface IPercentileConstraint extends Comparable<IPercentileConstraint>{

	public double getLowSlaConformanceLevel();
	public double getHighSlaConformanceLevel();
	public double getWidth();
	public double getPenalty();
	public boolean contains(double currentSLAConformance);
	public boolean overlaps(IPercentileConstraint pc);

}
