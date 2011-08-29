package de.uniol.inf.is.odysseus.costmodel.operator.datasrc;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public interface IHistogram extends Cloneable {

	public SDFAttribute getAttribute();
	public void setAttribute( SDFAttribute attribute );
	
	public double getMinimum();
	public double getMaximum();
	public double getOccurences(double value);
	public double getOccurenceRange( double from, double to );
	
	public double getValueCount();
	public int getIntervalCount();
	public double[] getIntervalBorders();
	public double[] getAllOccurences();
	
	public IHistogram clone();
	
	public IHistogram cutLower( double value );
	public IHistogram cutHigher( double value );
	public IHistogram toRelative();
	public IHistogram toAbsolute( double countNum );
	public boolean isRelative();
	public IHistogram normalize();
	
	public void setOccurences( int intervalIndex, double occs );
}
