package de.uniol.inf.is.odysseus.trajectory.compare.owd.data;

import de.uniol.inf.is.odysseus.trajectory.compare.data.AbstractConvertedDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawDataTrajectory;

/**
 * An Owd data trajectory which utilize <tt>OwdData</tt> for an appropriate representation.
 * @author marcus
 *
 */
public class OwdDataTrajectory extends AbstractConvertedDataTrajectory<OwdData> {

	/**
	 * Creates an instance of <tt>OwdDataTrajectory</tt>.
	 * 
	 * @param rawTrajectory the <tt>RawDataTrajectory</tt> to encapsulate
	 * @param convertedData the converted <tt>OwdData/tt>
	 * 
	 * @throws IllegalArgumentException if <tt>convertedData == null</tt>
	 */
	public OwdDataTrajectory(final RawDataTrajectory rawTrajectory, final OwdData convertedData) {
		super(rawTrajectory, convertedData);
	}
}
