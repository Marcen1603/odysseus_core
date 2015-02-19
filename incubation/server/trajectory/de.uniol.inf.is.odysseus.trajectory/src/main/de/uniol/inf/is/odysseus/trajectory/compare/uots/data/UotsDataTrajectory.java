package de.uniol.inf.is.odysseus.trajectory.compare.uots.data;

import de.uniol.inf.is.odysseus.trajectory.compare.data.AbstractConvertedDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawDataTrajectory;

/**
 * An Uots data trajectory which utilize <tt>UotsData</tt> for an appropriate representation.
 * @author marcus
 *
 */
public class UotsDataTrajectory extends AbstractConvertedDataTrajectory<UotsData> {

	/**
	 * Creates an instance of <tt>UotsDataTrajectory</tt>.
	 * 
	 * @param rawTrajectory the <tt>RawDataTrajectory</tt> to encapsulate
	 * @param convertedData the converted <tt>UotsData</tt>
	 * 
	 * @throws IllegalArgumentException if <tt>convertedData == null</tt>
	 */
	public UotsDataTrajectory(final RawDataTrajectory rawTrajectory, final UotsData convertedData) {
		super(rawTrajectory, convertedData);
	}
}
