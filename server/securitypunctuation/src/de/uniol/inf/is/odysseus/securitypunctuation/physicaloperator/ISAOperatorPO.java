package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.AbstractSecurityPunctuation;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.SecurityPunctuation;

public interface ISAOperatorPO {


	boolean match(Tuple t);

	void override(AbstractSecurityPunctuation sp);


}
