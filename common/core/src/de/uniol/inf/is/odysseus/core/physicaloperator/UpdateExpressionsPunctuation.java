package de.uniol.inf.is.odysseus.core.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

/**
 * This punctuation can be used to update expressions, e.g., from a
 * MAP-operator. It does not contain expressions directly, but the strings that
 * can be parsed to expressions. The strings are templates: they can contain
 * attribute-names in <>-brackets (e.g., <attributeName>). These parts are
 * replaced by the actual content of that attribute of the incoming tuples in
 * the respective operator that created the punctuations
 * {@see CreateUpdateExpressionsPunctuationPO}.
 * 
 * @author Tobias Brandt
 *
 */
public class UpdateExpressionsPunctuation extends AbstractPunctuation {

	public static final SDFSchema schema;

	static {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute("UpdateExpressionsPunctuation", "expressions", SDFDatatype.STRING));
		schema = SDFSchemaFactory.createNewSchema("UpdateExpressionsPunctuation", Tuple.class, attributes);
	}

	private RelationalExpression<?>[] expressions;

	public UpdateExpressionsPunctuation(PointInTime p, RelationalExpression<?>[] expressions) {
		super(p);
		this.expressions = expressions;
	}

	private static final long serialVersionUID = -5806607266505236307L;

	@Override
	public SDFSchema getSchema() {
		return schema;
	}

	@Override
	public Tuple<?> getValue() {
		@SuppressWarnings("rawtypes")
		Tuple<?> ret = new Tuple(1, false);
		ret.setAttribute(0, expressions);
		return ret;
	}

	@Override
	public AbstractPunctuation clone() {
		return new UpdateExpressionsPunctuation(getTime(), this.expressions);
	}

	@Override
	public AbstractPunctuation clone(PointInTime newTime) {
		return new UpdateExpressionsPunctuation(newTime, this.expressions);
	}

	@SuppressWarnings("rawtypes")
	public RelationalExpression[] getExpressions() {
		return expressions;
	}

}
