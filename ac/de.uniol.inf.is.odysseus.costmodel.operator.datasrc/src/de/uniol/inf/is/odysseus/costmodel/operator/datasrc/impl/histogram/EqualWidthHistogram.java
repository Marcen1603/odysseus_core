package de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram;

import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public class EqualWidthHistogram implements IHistogram {

	private double[] counts;
	private double min;
	private double max;
	private double intervalLength;
	private SDFAttribute attribute;
	private double countSum;
	private boolean isRelative;
	
	public EqualWidthHistogram( SDFAttribute attribute, double min, double max, double intervalLength, double[] counts) {
		this.counts = counts;
		this.min = min;
		this.max = max;
		this.intervalLength = intervalLength;
		this.attribute = attribute;
		
		for( double count : counts ) {
			countSum += count;
		}
		isRelative = false;
	}
	
	@Override
	public double getMinimum() {
		return min;
	}

	@Override
	public double getMaximum() {
		return max;
	}

	@Override
	public String toString() {
		return printNumbers();
	}
	
	@Override
	public EqualWidthHistogram clone(){
		double[] counts = new double[this.counts.length];
		System.arraycopy(this.counts, 0, counts, 0, this.counts.length);
		
		EqualWidthHistogram hist = new EqualWidthHistogram(attribute, min, max, intervalLength, counts);
		hist.isRelative = this.isRelative;
		return hist;
	}

	@Override
	public SDFAttribute getAttribute() {
		return attribute;
	}

	@Override
	public void setAttribute(SDFAttribute attribute) {
		this.attribute = attribute;
	}
	
	public String printNumbers() {
		
		StringBuilder sb = new StringBuilder();
		for( int i = 0; i < counts.length; i++ ) {
			sb.append("[");
			sb.append(String.format("%-10.3f", (i * intervalLength)+min).trim() );
			sb.append(", ");
			sb.append(String.format("%-10.3f", ((i+1) * intervalLength) + min ).trim() );
			sb.append("): ");
			sb.append(String.format("%-10.3f", counts[i] ).trim());
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
			sb.append(String.format("%-10.3f", (i * intervalLength)+min) );
			sb.append(" ,");
			sb.append(String.format("%-10.3f", ((i+1) * intervalLength) + min ) );
			sb.append(") ");
			
			int barLength = (int) (( counts[i] / maxCount ) * 80.0 );
			for( int j = 0; j < barLength; j++)
				sb.append("#");
			
			sb.append(" ");
			sb.append( String.format("%-6.3f%%", (counts[i] / countSum ) * 100.0 ) );
			
			sb.append("\n");
		}
		
		return sb.toString();
	}

	@Override
	public double getOccurences(double value) {
		if( value < min ) 
			return 0;
		if( value > max ) 
			return 0;
		
		// find count-index
		int index = (int)(( value - min ) / intervalLength);
		if( index >= counts.length )
			index = counts.length - 1;
		
		return counts[index] / intervalLength;
	}

	@Override
	public double getOccurenceRange(double from, double to) {
		if( from > to ) {
			double temp = from;
			from = to;
			to = temp;
		}
		
		if( from < min && to < min ) 
			return 0.0;
		if( from > max && to > max ) 
			return 0.0;
		
		from = trunc(from);
		to = trunc(to);
		
		int fromCountIndex = (int)((from - min ) / intervalLength );
		if( fromCountIndex >= counts.length )
			fromCountIndex = counts.length - 1;
		
		int toCountIndex = (int)((to - min) / intervalLength );
		if( toCountIndex >= counts.length )
			toCountIndex = counts.length - 1;
		
		if( fromCountIndex == toCountIndex ) {
			return counts[fromCountIndex] * 
					( to - from ) / intervalLength;
		}
		
		double startPart = counts[fromCountIndex] *
				( ( ( fromCountIndex + 1 ) * intervalLength ) - ( from - min ) ) / intervalLength;
		
		double endPart = counts[toCountIndex] * 
				( ( to - min ) - (toCountIndex * intervalLength) ) / intervalLength;
		
		double sumPart = 0.0;
		for( int i = fromCountIndex + 1; i < toCountIndex; i++ ) {
			sumPart += counts[i];
		}
		
		return startPart + sumPart + endPart; 
	}
	
	private double trunc( double value ) {
		if( value < min )
			return min;
		if( value > max )
			return max;
		return value;
	}

	@Override
	public double getValueCount() {
		return countSum;
	}

	@Override
	public IHistogram cutLower(double value) {
		
		// change only copies
		EqualWidthHistogram clone = clone();
		if( value < clone.min ) {
			return clone;
		}
		
		if( value > clone.max ) {
			for( int i = 0; i < clone.counts.length; i++ )
				clone.counts[i] = 0;
			clone.countSum = 0;
			
			return clone;
		}
		
		int countIndex = (int)((value - clone.min) / clone.intervalLength);
		for( int i = 0; i < countIndex; i++ ) {
			clone.countSum -= clone.counts[i];
			clone.counts[i] = 0;
		}
		
		// partially cut 
		double border = countIndex * clone.intervalLength + clone.min;
		double dist = value - border;
		double diff = (clone.counts[countIndex] * ( dist / clone.intervalLength ));
		clone.counts[countIndex] = clone.counts[countIndex] - diff;
		clone.countSum -= diff;
		
		return clone;
	}

	@Override
	public IHistogram cutHigher(double value) {
		// change only copies
		EqualWidthHistogram clone = clone();
		if( value > clone.max ) {
			return clone;
		}
		
		if( value < clone.min ) {
			for( int i = 0; i < clone.counts.length; i++ )
				clone.counts[i] = 0;
			clone.countSum = 0;
			
			return clone;
		}
		
		int countIndex = (int)((value - clone.min) / clone.intervalLength);
		for( int i = countIndex+1; i < clone.counts.length; i++ ) {
			clone.countSum -= clone.counts[i];
			clone.counts[i] = 0;
		}
		
		// partially cut 
		double border = ( countIndex + 1 ) * clone.intervalLength + clone.min;
		double dist = border - value;
		double diff = (clone.counts[countIndex] * ( dist / clone.intervalLength ));
		clone.counts[countIndex] = clone.counts[countIndex] - diff;
		clone.countSum -= diff;

		return clone;
	}

	@Override
	public double[] getIntervalBorders() {
		double[] borders = new double[(int)((max - min) / intervalLength) + 1];
		borders[0] = min;
		borders[borders.length - 1] = max;
		
		for( int i = 1; i < borders.length - 1; i++ ) 
			borders[i] = min + ( intervalLength * i );
		return borders;
	}

	@Override
	public IHistogram toRelative() {
		EqualWidthHistogram clone = clone();
		
		for( int i = 0; i < clone.counts.length; i++ )
			clone.counts[i] /= clone.countSum;
		clone.countSum = 1;
		clone.isRelative = true;
		return clone;
	}

	@Override
	public IHistogram toAbsolute(double countNum) {
		EqualWidthHistogram clone = clone();
		
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
			throw new IndexOutOfBoundsException("" + intervalIndex);
		
		countSum -= counts[intervalIndex];
		counts[intervalIndex] = occs;
		countSum += occs;
	}
	
	public IHistogram normalize() {
		EqualWidthHistogram clone = clone();
		if( !isRelative() )
			return clone;
		
		double factor = 1.0 / clone.countSum;
		
		for( int i = 0; i < clone.counts.length; i++ )
			clone.counts[i] *= factor;
		
		clone.countSum = 1.0;
		
		return clone;
	}
}
