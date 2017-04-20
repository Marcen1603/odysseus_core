package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.AbstractSecurityPunctuation;

public class SASelectionPO<T extends IStreamObject<?>> extends AbstractSAOperatorPO implements IHasPredicate{
	private IPredicate<? super Tuple> predicate;
	public SASelectionPO(IPredicate predicate) {
		
	}

	@Override
	public IPredicate getPredicate() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void processPunctuation(IPunctuation punctuation, int port){
		if(punctuation instanceof AbstractSecurityPunctuation){
		override((AbstractSecurityPunctuation)punctuation);
	}
		//while(((AbstractSecurityPunctuation) punctuation).getDDP().match()==false){
			
		//};
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}
	

}
