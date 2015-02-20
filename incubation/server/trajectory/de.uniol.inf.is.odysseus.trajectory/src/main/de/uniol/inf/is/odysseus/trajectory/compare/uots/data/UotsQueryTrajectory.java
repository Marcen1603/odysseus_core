package de.uniol.inf.is.odysseus.trajectory.compare.uots.data;

import java.util.Map;

import de.uniol.inf.is.odysseus.trajectory.compare.data.AbstractConvertedQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawQueryTrajectory;

/**
 * An Owd query trajectory which utilize <tt>UotsData</tt> for an appropriate representation.
 * @author marcus
 *
 */
public class UotsQueryTrajectory extends AbstractConvertedQueryTrajectory<UotsData> {

	/**
	 * Creates an instance of <tt>UotsQueryTrajectory</tt>.
	 *  
	 * @param rawTrajectory the <tt>RawQueryTrajectory</tt> to encapsulate
	 * @param convertedData the converted <tt>UotsData</tt>
	 * @param textualAttributes the textual attributes
	 * 
	 * @throws IllegalArgumentException if <tt>convertedData == null</tt> or <tt>rawTrajectory == null</tt> 
	 */
	public UotsQueryTrajectory(final RawQueryTrajectory rawTrajectory,
			final UotsData convertedData, final Map<String, String> textualAttributes) {
		super(rawTrajectory, convertedData, textualAttributes);
	}

}
