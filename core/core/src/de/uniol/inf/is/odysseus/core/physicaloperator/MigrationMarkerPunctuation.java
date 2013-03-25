/**
 * 
 */
package de.uniol.inf.is.odysseus.core.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

/**
 * @author Merlin Wasmann
 *
 */
public class MigrationMarkerPunctuation extends AbstractPunctuation {
	
	private ISource<?> source;

	public MigrationMarkerPunctuation(long time, ISource<?> source) {
		super(time);
		this.source = source;
	}

	public MigrationMarkerPunctuation(PointInTime point, ISource<?> source) {
		super(point);
		this.source = source;
	}
	
	public MigrationMarkerPunctuation(AbstractPunctuation punct, ISource<?> source) {
		super(punct);
		this.source = source;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPunctuation#clone()
	 */
	@Override
	public AbstractPunctuation clone() {
		return new MigrationMarkerPunctuation(getTime(), getSource());
	}
	
	public ISource<?> getSource() {
		return this.source;
	}

}
