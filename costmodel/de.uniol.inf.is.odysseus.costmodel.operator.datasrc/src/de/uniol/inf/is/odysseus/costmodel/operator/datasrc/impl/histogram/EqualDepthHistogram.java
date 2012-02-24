package de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;

public class EqualDepthHistogram implements IHistogram {

	private static Logger _logger = null;
	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(EqualDepthHistogram.class);
		}
		return _logger;
	}

	private SDFAttribute attribute;
	private double[] borders;
	private double[] counts;
	private double countSum;
	private boolean isRelative;
	
	public EqualDepthHistogram(SDFAttribute attribute, double[] borders, double[] counts) {
		this.attribute = attribute;
		this.borders = borders;
		this.counts = counts;
		this.isRelative = false;
		
		for( double count : counts ) 
			countSum += count;
	}
	
	@Override
	public SDFAttribute getAttribute() {
		return attribute;
	}

	@Override
	public void setAttribute(SDFAttribute attribute) {
		this.attribute = attribute;
	}

	@Override
	public double getMinimum() {
		return borders[0];
	}

	@Override
	public double getMaximum() {
		return borders[borders.length - 1];
	}

	@Override
	public EqualDepthHistogram clone(){
		double[] bordersClone = new double[borders.length];
		System.arraycopy(borders, 0, bordersClone, 0, borders.length);
		
		double[] countsClone = new double[counts.length];
		System.arraycopy(counts, 0, countsClone, 0, counts.length);
		
		EqualDepthHistogram hist = new EqualDepthHistogram(attribute, bordersClone, countsClone);
		hist.isRelative = this.isRelative;
		return hist;
	}
	
	@Override
	public String toString() {
		return printNumbers();
	}
	
	public String printNumbers() {
		StringBuilder sb = new StringBuilder();
		
		
		for( int i = 0; i < counts.length; i++ ) {
			sb.append(String.format("%-10.3f", borders[i] ) );
			sb.append("\t");
			sb.append(String.format("%-10.3f", borders[i+1]) );
			sb.append("\t");
			sb.append(String.format("%-10.3f", counts[i] ));
			sb.append("\n");
		}
		sb.append("Summary,");
		sb.append(String.format("%-10.3f", countSum) );
		
		return sb.toString();
	}
	
	public String printBars() {
		StringBuilder sb = new StringBuilder();
		double maxCount = Integer.MIN_VALUE;
		for( int i = 0; i < counts.length; i++ ) {
			if( counts[i] > maxCount )
				maxCount = counts[i];
		}
		
		for( int i = 0; i < counts.length; i++ ) {
			sb.append("[");
			sb.append(String.format("%-10.3f", borders[i] ) );
			sb.append(" ,");
			sb.append(String.format("%-10.3f", borders[i+1]) );
			sb.append(") ");
			
			int barLength = (int) ((counts[i] / maxCount ) * 80.0 );
			for( int j = 0; j < barLength; j++)
				sb.append("#");
			
			sb.append(" ");
			sb.append( String.format("%-6.3f", (counts[i] / countSum ) * 100.0 ) );
			
			sb.append("\n");
		}
		return sb.toString();
	}

	@Override
	public double getOccurences(double value) {
		if( value < borders[0])
			return 0.0;
		if( value > borders[borders.length - 1])
			return 0.0;
		
		int borderIndex = getLowerBorderIndex(value);
		if( borderIndex >= 0 )
			return counts[borderIndex];
		
		getLogger().warn("Invalid value in equal-depth:" + value);
		return 0.0;
	}

	@Override
	public double getOccurenceRange(double from, double to) {
		if( from > to ) {
			double temp = from;
			from = to;
			to = temp;
		}
		
		if( from < borders[0] && to < borders[0])
			return 0.0;
		if( from > borders[borders.length - 1] && to > borders[borders.length - 1])
			return 0.0;
		
		from = trunc(from);
		to = trunc(to);
		
		int startBorderIndex = getLowerBorderIndex(from);
		int endBorderIndex = getLowerBorderIndex(to);
		
		if( startBorderIndex == endBorderIndex ) {
			return counts[startBorderIndex] * 
					( to - from  ) /
					(borders[startBorderIndex + 1] - borders[startBorderIndex]);
		}
		
		// parts of intervals
		double startPart = counts[startBorderIndex] * 
				(borders[startBorderIndex + 1] - from) /
				(borders[startBorderIndex + 1] - borders[startBorderIndex]);
		
		double endPart = counts[endBorderIndex] *
				(to - borders[endBorderIndex]) / 
				(borders[endBorderIndex + 1] - borders[endBorderIndex]);
		
		// full intervals
		double sumFullPart = 0.0;
		for( int i = startBorderIndex + 1; i < endBorderIndex; i++ ) {
			sumFullPart += counts[i];
		}
		
		return startPart + sumFullPart + endPart;
	}
	
	private double trunc( double value ) {
		if( value > borders[borders.length-1])
			return borders[borders.length-1];
		if( value < borders[0] )
			return borders[0];
		return value;
	}
	
	private int getLowerBorderIndex( double value ) {
		for( int i = 1; i < borders.length; i++ ) {
			if( value < borders[i])
				return i-1;
		}
		
		getLogger().warn("LowerBorderIndex invalid:" + value);
		return borders.length - 1;
	}

	@Override
	public double getValueCount() {
		return countSum;
	}

	@Override
	public IHistogram cutLower(double value) {
		EqualDepthHistogram clone = clone();
		
		if( value <= borders[0] ) 
			return clone();
		if( value >= borders[borders.length - 1]) {
			for( int i = 0; i < counts.length; i++ ) 
				clone.counts[i] = 0;
			clone.countSum = 0;
			return clone;
		}
		
		int borderIndex = getLowerBorderIndex(value);
		for( int i = 0; i < borderIndex; i++ ) {
			clone.countSum -= clone.counts[i];
			clone.counts[i] = 0;
		}
		
		// partially cut 
		double border = borders[borderIndex];
		double dist = value - border;
		double diff = (clone.counts[borderIndex] * ( dist / (borders[borderIndex+1] - borders[borderIndex]) ));
		clone.counts[borderIndex] = clone.counts[borderIndex] - diff;
		clone.countSum -= diff;
				
		return clone;
	}

	@Override
	public IHistogram cutHigher(double value) {
		EqualDepthHistogram clone = clone();
		
		if( value >= borders[borders.length - 1] ) 
			return clone();
		if( value <= borders[0]) {
			for( int i = 0; i < counts.length; i++ ) 
				clone.counts[i] = 0;
			clone.countSum = 0;
			return clone;
		}
		
		int borderIndex = getLowerBorderIndex(value);
		for( int i = borderIndex + 1; i < borders.length - 1; i++ ) {
			clone.countSum -= clone.counts[i];
			clone.counts[i] = 0;
		}
		
		// partially cut 
		double border = borders[borderIndex + 1];
		double dist = border - value;
		double diff = (clone.counts[borderIndex] * ( dist / (borders[borderIndex+1] - borders[borderIndex]) ));
		clone.counts[borderIndex] = clone.counts[borderIndex] - diff;
		clone.countSum -= diff;
		
		return clone;
	}

	@Override
	public double[] getIntervalBorders() {
		return borders;
	}

	@Override
	public IHistogram toRelative() {
		EqualDepthHistogram clone = clone();
		
		for( int i = 0; i < clone.counts.length; i++ )
			clone.counts[i] /= clone.countSum;
		clone.countSum = 1;
		clone.isRelative = true;
		return clone;
	}

	@Override
	public IHistogram toAbsolute(double countNum) {
		EqualDepthHistogram clone = clone();
		
		clone.countSum = 0;
		for( int i = 0; i < clone.counts.length; i++ ) {
			clone.counts[i] *= countNum;
			clone.countSum += clone.counts[i];
		}
		clone.isRelative = false;
		return clone;
	}

	@Override
	public boolean isRelative() {
		return isRelative;
	}

	@Override
	public int getIntervalCount() {
		return counts.length;
	}

	@Override
	public double[] getAllOccurences() {
		double[] copy = new double[counts.length];
		System.arraycopy(counts, 0, copy, 0, counts.length);
		return copy;
	}

	@Override
	public void setOccurences(int intervalIndex, double occs) {
		if( occs < 0.0)
			throw new IllegalArgumentException("number of occurences must be zero or positive, not " + occs);
		if( intervalIndex < 0 || intervalIndex >= counts.length )
			throw new IndexOutOfBoundsException("" + occs);
		
		countSum -= counts[intervalIndex];
		counts[intervalIndex] = occs;
		countSum += occs;
	}

	@Override
	public IHistogram normalize() {
		EqualDepthHistogram clone = clone();
		if( !isRelative() )
			return clone;
		
		double factor = 1.0 / clone.countSum;
		
		for( int i = 0; i < clone.counts.length; i++ )
			clone.counts[i] *= factor;
		
		clone.countSum = 1.0;
		
		return clone;
	}
}
