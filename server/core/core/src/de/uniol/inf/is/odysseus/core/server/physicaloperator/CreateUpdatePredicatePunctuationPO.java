package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import java.util.List;

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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IExpressionBuilder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OperatorBuilderFactory;

public class CreateUpdatePredicatePunctuationPO<T extends Tuple<? extends ITimeInterval>> extends AbstractPipe<T, T> {

	private String predicateTemplate;
	private SDFSchema inputSchema;
	private List<String> targetOperatorNames;
	private IAttributeResolver attributeResolver;

	public CreateUpdatePredicatePunctuationPO(CreateUpdatePredicatePunctuationAO ao) {
		this.predicateTemplate = ao.getPredicateTemplate();
		this.inputSchema = ao.getInputSchema();
		this.attributeResolver = new DirectAttributeResolver(this.inputSchema);
		this.targetOperatorNames = ao.getTargetOperatorNames();
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
					replacement = object.getAttribute(attributePosition).toString();
				}
				newPredicate = newPredicate.replace(templateStyle, replacement);
			}
			attributePosition++;
		}

		String predicateType = getInputSchema(port).getType().getName();
		IPredicate<?> predicate = createPredicate(newPredicate, predicateType);
		UpdatePredicatePunctuation punctuation = new UpdatePredicatePunctuation(object.getMetadata().getStart(),
				predicate, this.targetOperatorNames);
		this.sendPunctuation(punctuation);
	}

	/**
	 * Takes a string and converts it to a predicate
	 *
	 * @param rawPredicate
	 *            The predicate as a string
	 * @param predicateType
	 *            The identifier for the type of the predicate
	 * @return The predicate as a Predicate object
	 */
	private IPredicate<?> createPredicate(String rawPredicate, String predicateType) {
		IExpressionBuilder<?,?> pBuilder = OperatorBuilderFactory.getExpressionBuilder(predicateType);
		IPredicate<?> predicate = pBuilder.createPredicate(attributeResolver, rawPredicate);
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