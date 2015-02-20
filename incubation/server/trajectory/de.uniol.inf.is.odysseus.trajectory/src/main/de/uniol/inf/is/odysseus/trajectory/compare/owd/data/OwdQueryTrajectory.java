package de.uniol.inf.is.odysseus.trajectory.compare.owd.data;

import java.util.Map;

import de.uniol.inf.is.odysseus.trajectory.compare.data.AbstractConvertedQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawQueryTrajectory;

/**
 * An Owd query trajectory which utilize <tt>OwdData</tt> for an appropriate representation.
 * @author marcus
 *
 */
public class OwdQueryTrajectory extends AbstractConvertedQueryTrajectory<OwdData> {

	/**
	 * Creates an instance of <tt>OwdQueryTrajectory</tt>.
	 *  
	 * @param rawTrajectory the <tt>RawQueryTrajectory</tt> to encapsulate
	 * @param convertedData the converted <tt>OwdData</tt>
	 * @param textualAttributes the textual attributes
	 * 
	 * @throws IllegalArgumentException if <tt>convertedData == null</tt> or <tt>rawTrajectory == null</tt> 
	 */
	public OwdQueryTrajectory(final RawQueryTrajectory rawTrajectory,
			final OwdData convertedData, final Map<String, String> textualAttributes) {
		super(rawTrajectory, convertedData, textualAttributes);
	}

}
