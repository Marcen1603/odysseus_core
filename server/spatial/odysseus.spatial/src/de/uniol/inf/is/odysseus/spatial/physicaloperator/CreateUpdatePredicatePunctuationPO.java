package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.UpdatePredicatePunctuation;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CreateUpdatePredicatePunctuationAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.parser.pql.relational.RelationalPredicateBuilder;

public class CreateUpdatePredicatePunctuationPO<T extends Tuple<? extends ITimeInterval>> extends AbstractPipe<T, T> {

	private String predicateTemplate;
	private SDFSchema inputSchema;
	private IAttributeResolver attributeResolver;

	public CreateUpdatePredicatePunctuationPO(CreateUpdatePredicatePunctuationAO ao) {
		this.predicateTemplate = ao.getPredicateTemplate();
		this.inputSchema = ao.getInputSchema();
		this.attributeResolver = new DirectAttributeResolver(this.inputSchema);
	}

	@Override
	protected void process_next(T object, int port) {

		// TODO build replacement map

		String newPredicate = this.predicateTemplate;

		int attributePosition = 0;
		for (SDFAttribute attribute : this.inputSchema.getAttributes()) {
			String templateStyle = "<" + attribute.getAttributeName() + ">";
			if (newPredicate.contains(templateStyle)) {
				String replacement = "";
				if (object.getAttribute(attributePosition) instanceof Double) {
					replacement = String.valueOf((Double) object.getAttribute(attributePosition));
				} else {
					replacement = String.valueOf(object.getAttribute(attributePosition));
				}
				newPredicate = newPredicate.replace(templateStyle, replacement);
			}
			attributePosition++;
		}

		IPredicate<?> predicate = createPredicate(newPredicate);
		UpdatePredicatePunctuation punctuation = new UpdatePredicatePunctuation(object.getMetadata().getStart(),
				predicate);
		this.sendPunctuation(punctuation);
	}

	/**
	 * Takes a string and converts it to a predicate
	 * 
	 * @param rawPredicate
	 *            The predicate as a string
	 * @return The predicate as a Predicate object
	 */
	private IPredicate<?> createPredicate(String rawPredicate) {
		RelationalPredicateBuilder builder = new RelationalPredicateBuilder();
		IPredicate<?> predicate = builder.createPredicate(attributeResolver, rawPredicate);
		return predicate;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

}