package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.parser.pql.relational.RelationalPredicateBuilder;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.UpdateableSelectAO;
import de.uniol.inf.is.odysseus.spatial.punctuation.UpdatePredicatePunctuation;

public class UpdateableSelectPO extends SelectPO {

	private IAttributeResolver attributeResolver;
		
	@SuppressWarnings("unchecked")
	public UpdateableSelectPO(UpdateableSelectAO ao, IPredicate predicate) {
		super(predicate);
		this.attributeResolver = new DirectAttributeResolver(ao.getInputSchema());
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {

		if (punctuation instanceof UpdatePredicatePunctuation) {
			UpdatePredicatePunctuation updatePredicatePunctuation = (UpdatePredicatePunctuation) punctuation;
			String newPredicate = updatePredicatePunctuation.getNewPredicate();		
			IPredicate predicate = createPredicate(newPredicate);
			this.setPredicate(predicate);
		} else {
			super.processPunctuation(punctuation, port);			
		}
	}
	
	private IPredicate createPredicate(String rawPredicate) {
		RelationalPredicateBuilder builder = new RelationalPredicateBuilder();
		IPredicate predicate = builder.createPredicate(attributeResolver, rawPredicate);
		return predicate;
	}

}
