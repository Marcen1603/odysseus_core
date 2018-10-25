package de.uniol.inf.is.odysseus.rcp.viewer.stream.table;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.mep.ParseException;

public class TupleFilter {

	private static final Logger LOG = LoggerFactory.getLogger(TupleFilter.class);

	private final String expressionString;
	private final SDFExpression filterExpression;
	private final List<Integer> filterAttributeIndices;
	private final List<Object> filterAttributeValues;
	
	public TupleFilter(String expressionString, SDFSchema schema) throws ParseException {
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(expressionString), "Expression string must not be null or empty!");
		Objects.requireNonNull(schema, "Schema must not be null!");

		this.expressionString = expressionString;

		try {
			filterExpression = new SDFExpression(expressionString, null, MEP.getInstance());
			filterAttributeIndices = determineAttributeIndices(filterExpression.getAllAttributes(), schema);
			filterAttributeValues = Lists.newArrayListWithExpectedSize(filterAttributeIndices.size());
			for (int i = 0; i < filterAttributeIndices.size(); i++) {
				filterAttributeValues.add(null); // dummy-values
			}

		} catch (Throwable t) {
			LOG.error("Could not apply filter", t);
			throw new ParseException("Could not apply filter", t);
		}
	}

	private static List<Integer> determineAttributeIndices(List<SDFAttribute> attributes, SDFSchema schema) throws ParseException {
		List<Integer> indices = Lists.newArrayList();
		for (int i = 0; i < attributes.size(); i++) {
			SDFAttribute attribute = attributes.get(i);

			boolean found = false;
			for (int j = 0; j < schema.size(); j++) {
				if (schema.getAttribute(j).getAttributeName().equals(attribute.getAttributeName())) {
					indices.add(j);
					found = true;
					break;
				}
			}

			if (!found) {
				throw new ParseException("Could not find attribute '" + attribute.getAttributeName() + "'");
			}
		}

		return indices;
	}

	public boolean isFiltered(Tuple<?> tuple) {
		for (int i = 0; i < filterAttributeIndices.size(); i++) {
			filterAttributeValues.set(i, tuple.getAttribute(filterAttributeIndices.get(i)));
		}

		try {
			filterExpression.bindVariables(filterAttributeValues.toArray());
			Boolean result = filterExpression.getValue();
			if (result != null && result.equals(Boolean.FALSE)) {
				return true; // tuple is filtered out
			}
		} catch (Throwable t) {
			LOG.error("Could not apply filter to tuple {}", tuple, t);
		}
		return false;
	}

	public String getExpressionString() {
		return expressionString;
	}
}
