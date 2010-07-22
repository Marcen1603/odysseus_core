package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.base.IMetaAttribute;

public interface IRating extends IMetaAttribute, IClone {

	public void setRating(int rating);
	public int getRating();

	public void incRating();
	public void decRating();

	public boolean isZero();

	public IRating getRatingObject();

}
