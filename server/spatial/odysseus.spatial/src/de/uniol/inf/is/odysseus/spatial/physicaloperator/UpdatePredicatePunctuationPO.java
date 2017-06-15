package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.UpdatePredicatePunctuationAO;
import de.uniol.inf.is.odysseus.spatial.punctuation.UpdatePredicatePunctuation;

public class UpdatePredicatePunctuationPO<T extends Tuple<? extends ITimeInterval>> extends AbstractPipe<T, T> {

	private String predicateTemplate;
	private SDFSchema inputSchema;

	public UpdatePredicatePunctuationPO(UpdatePredicatePunctuationAO ao) {
		this.predicateTemplate = ao.getPredicateTemplate();
		this.inputSchema = ao.getInputSchema();
	}

	@Override
	protected void process_next(T object, int port) {

		String newPredicate = this.predicateTemplate;

		int attributePosition = 0;
		for (SDFAttribute attribute : this.inputSchema.getAttributes()) {
			String templateStyle = "<" + attribute.getAttributeName() + ">";
			if (newPredicate.contains(templateStyle)) {
				String replacement = String.valueOf(object.getAttribute(attributePosition));
				newPredicate.replace(templateStyle, replacement);
			}
			attributePosition++;
		}

		UpdatePredicatePunctuation punctuation = new UpdatePredicatePunctuation(object.getMetadata().getStart(),
				newPredicate);
		this.sendPunctuation(punctuation);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

}
