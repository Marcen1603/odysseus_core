/**
 * 
 */
package de.uniol.inf.is.odysseus.planmigration.simpleplanmigrationstrategy;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Merlin Wasmann
 * 
 */
@Deprecated
public class MigrationMarkerPunctuation extends AbstractPunctuation {

	private static final long serialVersionUID = 1L;
	private ISource<?> source;
	private String sourceName = null;

	public MigrationMarkerPunctuation(long time, ISource<?> source) {
		super(time);
		this.source = source;
	}

	public MigrationMarkerPunctuation(PointInTime point, ISource<?> source) {
		super(point);
		this.source = source;
	}

	public MigrationMarkerPunctuation(AbstractPunctuation punct,
			ISource<?> source) {
		super(punct);
		this.source = source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPunctuation#clone
	 * ()
	 */
	@Override
	public AbstractPunctuation clone() {
		return new MigrationMarkerPunctuation(getTime(), getSource());
	}

	@Override
	public AbstractPunctuation clone(PointInTime newTime) {
		return new MigrationMarkerPunctuation(newTime, getSource());
	}

	public ISource<?> getSource() {
		return this.source;
	}

	public void setSourceName(String name) {
		this.sourceName = name;
	}
	
	public String getSourceName() {
		return (this.sourceName != null ? this.sourceName : this.source.getName());
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " (" + hashCode() + ") | "
				+ getTime() + " | from " + getSourceName();
	}

	@Override
	public SDFSchema getSchema() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tuple<?> getValue() {
		// TODO Auto-generated method stub
		return null;
	}

}
