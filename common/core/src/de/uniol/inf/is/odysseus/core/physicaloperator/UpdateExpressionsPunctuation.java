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

public class UpdateExpressionsPunctuation extends AbstractPunctuation {

	public static final SDFSchema schema;

	static {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute("UpdatePredicatePunctuation", "predicate", SDFDatatype.STRING));
		schema = SDFSchemaFactory.createNewSchema("UpdatePredicatePunctuation", Tuple.class, attributes);
	}

	private RelationalExpression[] expressions;

	public UpdateExpressionsPunctuation(PointInTime p, RelationalExpression[] expressions) {
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

	public RelationalExpression[] getExpressions() {
		return expressions;
	}

}
