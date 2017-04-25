package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.AbstractSecurityPunctuation;

public interface ISAOperatorPO<T extends IStreamObject<?>> {

	void override(AbstractSecurityPunctuation sp);

	ArrayList<AbstractSecurityPunctuation> match(T object);

}
