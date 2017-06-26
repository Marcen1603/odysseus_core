package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.UpdateExpressionsPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CreateUpdateExpressionsPunctuationAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedString;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.mep.MEP;

public class CreateUpdateExpressionsPunctuationPO<T extends Tuple<? extends ITimeInterval>> extends AbstractPipe<T, T> {

	private SDFSchema inputSchema;
	private List<NamedString> namedStrings;
	private IAttributeResolver attributeResolver;

	public CreateUpdateExpressionsPunctuationPO(CreateUpdateExpressionsPunctuationAO ao) {
		namedStrings = ao.getNamedStrings();
		this.inputSchema = ao.getInputSchema();
		this.attributeResolver = new DirectAttributeResolver(this.inputSchema);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
	}

	@Override
	protected void process_next(T object, int port) {

		RelationalExpression<?>[] expressions = new RelationalExpression[this.namedStrings.size()];

		// Convert strings to expressions after replacing <...>
		int counter = 0;
		for (NamedString namedString : this.namedStrings) {
			String expressionString = this.replaceTemplatePlaceholders(namedString.getContent(), object);
			SDFExpression sdfExpression = new SDFExpression("", expressionString, this.attributeResolver,
					MEP.getInstance(), AggregateFunctionBuilderRegistry.getAggregatePattern());
			expressions[counter] = new RelationalExpression<>(sdfExpression);
			counter++;
		}

		// Send the punctuation with the new expressions
		UpdateExpressionsPunctuation punctuation = new UpdateExpressionsPunctuation(object.getMetadata().getStart(),
				expressions);
		this.sendPunctuation(punctuation);
	}

	/**
	 * Replaces the placeholders "<attributeName>" with the content of the
	 * attribute
	 * 
	 * @param template
	 *            The template of the expression: an expression as a string with
	 *            placeholders
	 * @param object
	 *            The streaming object with the content of the current
	 *            attributes
	 * @return The expression
	 */
	private String replaceTemplatePlaceholders(String template, T object) {
		String expressionString = template;

		int attributePosition = 0;
		for (SDFAttribute attribute : this.inputSchema.getAttributes()) {
			String templateStyle = "<" + attribute.getAttributeName() + ">";
			if (expressionString.contains(templateStyle)) {
				String replacement = "";
				if (object.getAttribute(attributePosition) instanceof Double) {
					replacement = String.valueOf((Double) object.getAttribute(attributePosition));
				} else {
					replacement = String.valueOf(object.getAttribute(attributePosition));
				}
				expressionString = expressionString.replace(templateStyle, replacement);
			}
			attributePosition++;
		}

		return expressionString;

	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}
}
