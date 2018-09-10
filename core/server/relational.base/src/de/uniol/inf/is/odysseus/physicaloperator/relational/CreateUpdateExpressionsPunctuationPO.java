package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Option;
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
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.mep.MEP;

/**
 * Creates punctuations with expressions based on templates. These can be used
 * to update the expressions of MAP operators.
 * 
 * @author Tobias Bandt
 *
 * @param <T>
 */
public class CreateUpdateExpressionsPunctuationPO<T extends Tuple<? extends ITimeInterval>> extends AbstractPipe<T, T> {

	private SDFSchema inputSchema;
	private List<Option> expressionTemplates;
	private List<String> targetOperatorNames;
	private IAttributeResolver attributeResolver;

	public CreateUpdateExpressionsPunctuationPO(CreateUpdateExpressionsPunctuationAO ao) {
		this.expressionTemplates = ao.getNamedStrings();
		this.targetOperatorNames = ao.getTargetOperatorNames();
		this.inputSchema = ao.getInputSchema();
		this.attributeResolver = new DirectAttributeResolver(this.inputSchema);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
	}

	@Override
	protected void process_next(T object, int port) {

		Map<String, RelationalExpression<ITimeInterval>> expressionsMap = new HashMap<>(
				this.expressionTemplates.size());

		// Convert strings to expressions after replacing <...>
		for (Option namedString : this.expressionTemplates) {
			String expressionString = this.replaceTemplatePlaceholders(namedString.getValue(), object);
			SDFExpression sdfExpression = new SDFExpression("", expressionString, this.attributeResolver,
					MEP.getInstance(), AggregateFunctionBuilderRegistry.getAggregatePattern());
			RelationalExpression<ITimeInterval> expression = new RelationalExpression<>(sdfExpression);
			expressionsMap.put(namedString.getName(), expression);
		}

		// Send the punctuation with the new expressions
		UpdateExpressionsPunctuation<ITimeInterval> punctuation = new UpdateExpressionsPunctuation<ITimeInterval>(
				object.getMetadata().getStart(), expressionsMap, this.targetOperatorNames);
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
					replacement = object.getAttribute(attributePosition).toString();
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
