package de.uniol.inf.is.odysseus.usermanagement;

import java.io.Serializable;

public interface IPercentileConstraint extends Comparable<IPercentileConstraint>, Serializable{

	public double getLowSlaConformanceLevel();
	public double getHighSlaConformanceLevel();
	public double getWidth();
	public double getPenalty();
	public boolean contains(double currentSLAConformance);
	public boolean overlaps(IPercentileConstraint pc);
	public boolean highInside();

}
