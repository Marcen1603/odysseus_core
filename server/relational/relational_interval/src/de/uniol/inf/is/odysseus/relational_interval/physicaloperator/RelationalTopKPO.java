package de.uniol.inf.is.odysseus.relational_interval.physicaloperator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import de.uniol.inf.is.odysseus.core.collection.SerializablePair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.relational.VarHelper;

public class RelationalTopKPO<T extends Tuple<M>, M extends ITimeInterval>
		extends AbstractPipe<T, T> {

	private class TopKComparatorAsc implements
			Comparator<SerializablePair<Double, T>> {

		@Override
		public int compare(SerializablePair<Double, T> left,
				SerializablePair<Double, T> right) {
			return left.getE1().compareTo(right.getE1());
		}
	}

	private class TopKComparatorDesc implements
			Comparator<SerializablePair<Double, T>> {

		@Override
		public int compare(SerializablePair<Double, T> left,
				SerializablePair<Double, T> right) {
			return right.getE1().compareTo(left.getE1());
		}
	}

	final private SDFExpression scoringFunction;
	final private int k;
	final private TreeSet<SerializablePair<Double, T>> topK;
	private LinkedList<T> lastResult;

	private VarHelper[] variables;

	public RelationalTopKPO(SDFSchema inputSchema,
			SDFExpression scoringFunction, int k, boolean descending) {
		super();
		this.scoringFunction = scoringFunction;
		initScoringFunction(inputSchema);
		this.k = k;
		topK = new TreeSet<SerializablePair<Double, T>>(
				descending ? new TopKComparatorDesc() : new TopKComparatorAsc());
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {

		cleanUp(object.getMetadata().getStart());

		addObject(calcScore(object));

		produceResult(object);
	}

	private void cleanUp(PointInTime start) {
		Iterator<SerializablePair<Double, T>> iter = topK.iterator();
		while (iter.hasNext()) {
			if (iter.next().getE2().getMetadata().getEnd()
					.beforeOrEquals(start)) {
				iter.remove();
			}
		}
	}

	private void addObject(SerializablePair<Double, T> scoredObject) {
		topK.add(scoredObject);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void produceResult(T object) {
		// Produce result
		T result = (T) new Tuple(2, false);
		Iterator<SerializablePair<Double, T>> iter = topK.descendingIterator();
		List<T> resultList = new LinkedList<T>();
		for (int i = 0; i < k && iter.hasNext(); i++) {
			T out = (T) iter.next().getE2().clone();
			out.setMetadata(null);
			resultList.add(out);
		}
		boolean sameAsLastResult = compareWithLastResult(resultList);

		if (!sameAsLastResult) {
			lastResult = new LinkedList<T>(resultList);
			result.setAttribute(0, resultList);
			result.setAttribute(1, object);
			M meta = (M) object.getMetadata().clone();
			result.setMetadata(meta);
			transfer(result);
		}
	}

	private boolean compareWithLastResult(List<T> resultList) {
		if (lastResult == null) {
			return false;
		}

		if (lastResult.size() != resultList.size()) {
			return false;
		}

		Iterator<T> iter = resultList.iterator();
		for (T e : lastResult) {
			if ((!e.equals(iter.next()))) {
				return false;
			}
		}

		return true;

	}

	private SerializablePair<Double, T> calcScore(T object) {
		SerializablePair<Double, T> scoredObject = new SerializablePair<>();
		Double score = null;
		scoredObject.setE2(object);

		Object[] values = new Object[this.variables.length];
		IMetaAttribute[] meta = new IMetaAttribute[this.variables.length];
		for (int j = 0; j < this.variables.length; ++j) {

			if (object != null) {
				values[j] = object.getAttribute(this.variables[j].pos);
				meta[j] = object.getMetadata();
			}
		}

		try {
			this.scoringFunction.bindMetaAttribute(object.getMetadata());
			this.scoringFunction.bindAdditionalContent(object
					.getAdditionalContent());
			this.scoringFunction.bindVariables(meta, values);
			score = ((Number) this.scoringFunction.getValue()).doubleValue();

		} catch (Exception e) {
			if (!(e instanceof NullPointerException)) {
				sendWarning("Cannot calc result for " + object
						+ " with expression " + scoringFunction, e);
			}
		}

		scoredObject.setE1(score);

		return scoredObject;
	}

	protected void initScoringFunction(SDFSchema schema) {
		List<SDFAttribute> neededAttributes = scoringFunction
				.getAllAttributes();
		this.variables = new VarHelper[neededAttributes.size()];
		int j = 0;
		for (SDFAttribute curAttribute : neededAttributes) {
			variables[j++] = initAttribute(schema, curAttribute);
		}
	}

	public VarHelper initAttribute(SDFSchema schema, SDFAttribute curAttribute) {
		return new VarHelper(schema.indexOf(curAttribute), 0);
	}

}
