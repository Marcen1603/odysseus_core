package de.uniol.inf.is.odysseus.probabilistic.metadata;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public interface IProbabilistic extends IMetaAttribute, IClone {

	double getProbability(int pos);

	void setProbability(int pos, double value);
}
