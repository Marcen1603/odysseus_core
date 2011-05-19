/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
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
		if (getLowSlaConformanceLevel() < o.getLowSlaConformanceLevel()){
			return -1;
		}else if (getLowSlaConformanceLevel() > o.getLowSlaConformanceLevel()){
			return 1;
		}
		return 0;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (highInside ? 1231 : 1237);
		long temp;
		temp = Double.doubleToLongBits(highSlaConformanceLevel);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(lowSlaConformanceLevel);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(penalty);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(width);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PercentileContraint other = (PercentileContraint) obj;
		if (highInside != other.highInside)
			return false;
		if (Double.doubleToLongBits(highSlaConformanceLevel) != Double
				.doubleToLongBits(other.highSlaConformanceLevel))
			return false;
		if (Double.doubleToLongBits(lowSlaConformanceLevel) != Double
				.doubleToLongBits(other.lowSlaConformanceLevel))
			return false;
		if (Double.doubleToLongBits(penalty) != Double
				.doubleToLongBits(other.penalty))
			return false;
		if (Double.doubleToLongBits(width) != Double
				.doubleToLongBits(other.width))
			return false;
		return true;
	}
	
	
}
