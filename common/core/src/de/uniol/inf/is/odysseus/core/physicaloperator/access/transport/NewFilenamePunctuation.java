package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPunctuation;

public class NewFilenamePunctuation extends AbstractPunctuation {

	private static final long serialVersionUID = 6424816957991015360L;
	final private String filename;

	public NewFilenamePunctuation(PointInTime p, String filename) {
		super(p);
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}
	
	@Override
	public AbstractPunctuation clone() {
		return new NewFilenamePunctuation(getTime(),filename);
	}

	@Override
	public AbstractPunctuation clone(PointInTime newTime) {
		return new NewFilenamePunctuation(newTime, filename);
	}
	
	@Override
	public String toString() {
		return super.toString()+" "+filename;
	}

}
