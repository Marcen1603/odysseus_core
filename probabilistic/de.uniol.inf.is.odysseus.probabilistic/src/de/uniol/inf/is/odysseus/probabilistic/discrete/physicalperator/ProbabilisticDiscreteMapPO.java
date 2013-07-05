package de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMapPO;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <T>
 */
public class ProbabilisticDiscreteMapPO<T extends IMetaAttribute> extends AbstractPipe<Tuple<T>, Tuple<T>> {

	static private Logger logger = LoggerFactory.getLogger(RelationalMapPO.class);

	private VarHelper[][] variables; // Expression.Index
	private SDFExpression[] expressions;
	private final SDFSchema inputSchema;
	final private LinkedList<Tuple<T>> lastObjects = new LinkedList<>();
	private int maxHistoryElements = 0;
	final private boolean statebased;
	final private boolean allowNull;
	private int[] probabilisticAttributePos;

	public ProbabilisticDiscreteMapPO(SDFSchema inputSchema, SDFExpression[] expressions, boolean statebased, boolean allowNullInOutput) {
		this.inputSchema = inputSchema;
		this.statebased = statebased;
		this.allowNull = allowNullInOutput;
		init(inputSchema, expressions);
	}

	public ProbabilisticDiscreteMapPO(ProbabilisticDiscreteMapPO<T> probabilisticDiscreteMapPO) {
		this.inputSchema = probabilisticDiscreteMapPO.inputSchema.clone();
		this.statebased = probabilisticDiscreteMapPO.statebased;
		this.allowNull = probabilisticDiscreteMapPO.allowNull;
		init(probabilisticDiscreteMapPO.inputSchema, probabilisticDiscreteMapPO.expressions);
	}

	private int[] determineProbabilisticAttributePos(SDFExpression[] expressions) {
		int[] attributePos = new int[expressions.length];
		int j = 0;
		for (int i = 0; i < expressions.length; i++) {
			SDFExpression expr = expressions[i];
			if (expr.getType().getClass() == SDFProbabilisticDatatype.class) {
				SDFProbabilisticDatatype datatype = (SDFProbabilisticDatatype) expr.getType();
				if (datatype.isDiscrete()) {
					attributePos[j] = i;
					j++;
				}
			}
		}
		int[] probabilisticAttributePos = new int[j];
		System.arraycopy(attributePos, 0, probabilisticAttributePos, 0, j);
		return probabilisticAttributePos;
	}

	private void init(SDFSchema schema, SDFExpression[] expressions) {
		this.probabilisticAttributePos = determineProbabilisticAttributePos(expressions);
		this.expressions = new SDFExpression[expressions.length];
		for (int i = 0; i < expressions.length; ++i) {
			this.expressions[i] = expressions[i].clone();
		}
		this.variables = new VarHelper[expressions.length][];
		int i = 0;
		for (SDFExpression expression : expressions) {
			List<SDFAttribute> neededAttributes = expression.getAllAttributes();
			VarHelper[] newArray = new VarHelper[neededAttributes.size()];

			this.variables[i++] = newArray;
			int j = 0;
			for (SDFAttribute curAttribute : neededAttributes) {
				if (curAttribute.getSourceName() != null && curAttribute.getSourceName().startsWith("__last_")) {
					if (!statebased) {
						throw new RuntimeException("Map cannot be used with __last_! Used StateMap instead!");
					}
					int pos = Integer.parseInt(curAttribute.getSourceName().substring("__last_".length(), curAttribute.getSourceName().indexOf('.')));
					if (pos > maxHistoryElements) {
						maxHistoryElements = pos + 1;
					}
					String realAttrStr = curAttribute.getURI().substring(curAttribute.getURI().indexOf('.') + 1);
					String newSource = realAttrStr.substring(0, realAttrStr.indexOf('.'));
					String newName = realAttrStr.substring(realAttrStr.indexOf('.') + 1);
					if ("null".equals(newSource)) {
						newSource = null;
					}
					SDFAttribute newAttribute = new SDFAttribute(newSource, newName, curAttribute);
					int index = schema.indexOf(newAttribute);
					newArray[j++] = new VarHelper(index, pos);
				} else {
					newArray[j++] = new VarHelper(schema.indexOf(curAttribute), 0);
				}
			}
		}
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@SuppressWarnings("unchecked")
	@Override
	final protected void process_next(Tuple<T> object, int port) {
		boolean nullValueOccured = false;
		ProbabilisticTuple<T> outputVal = new ProbabilisticTuple<T>(this.expressions.length, false);
		outputVal.setMetadata((T) object.getMetadata().clone());
		int lastObjectSize = this.lastObjects.size();
		if (lastObjectSize > maxHistoryElements) {
			lastObjects.removeLast();
			lastObjectSize--;
		}
		lastObjects.addFirst(object);
		lastObjectSize++;
		synchronized (this.expressions) {
			for (int i = 0; i < this.expressions.length; ++i) {
				Object[] values = new Object[this.variables[i].length];
				for (int j = 0; j < this.variables[i].length; ++j) {
					Tuple<T> obj = null;
					if (lastObjectSize > this.variables[i][j].objectPosToUse) {
						obj = lastObjects.get(this.variables[i][j].objectPosToUse);
					}
					if (obj != null) {
						values[j] = obj.getAttribute(this.variables[i][j].pos);
					}
				}

				try {
					this.expressions[i].bindMetaAttribute(object.getMetadata());
					this.expressions[i].bindAdditionalContent(object.getAdditionalContent());
					this.expressions[i].bindVariables(values);
					Object expr = this.expressions[i].getValue();
					outputVal.setAttribute(i, expr);
					if (expr == null) {
						nullValueOccured = true;
					}
				} catch (Exception e) {
					nullValueOccured = true;
					if (!(e instanceof NullPointerException)) {
						logger.error("Cannot calc result ", e);
						// Not needed. Value is null, if not set!
						// outputVal.setAttribute(i, null);
					}
				}
				if (this.expressions[i].getType().requiresDeepClone()) {
					outputVal.setRequiresDeepClone(true);
				}
			}
		}
		if (!nullValueOccured || (nullValueOccured && allowNull)) {
			double jointProbability = 1.0;
			for (int pos : this.probabilisticAttributePos) {
				AbstractProbabilisticValue<?> value = outputVal.getAttribute(pos);
				double sum = 0.0;
				for (Double probability : value.getValues().values()) {
					sum += probability;
				}
				jointProbability *= sum;
			}
			if (jointProbability > 0.0) {
				((IProbabilistic) outputVal.getMetadata()).setExistence(jointProbability);
				transfer(outputVal);
			}
		}
	}

	@Override
	public ProbabilisticDiscreteMapPO<T> clone() {
		return new ProbabilisticDiscreteMapPO<T>(this);
	}

	@Override
	@SuppressWarnings({ "rawtypes" })
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof RelationalMapPO)) {
			return false;
		}
		ProbabilisticDiscreteMapPO rmpo = (ProbabilisticDiscreteMapPO) ipo;

		if (!this.getOutputSchema().equals(rmpo.getOutputSchema())) {
			return false;
		}

		if (this.hasSameSources(rmpo) && this.inputSchema.compareTo(rmpo.inputSchema) == 0) {
			if (this.expressions.length == rmpo.expressions.length) {
				for (int i = 0; i < this.expressions.length; i++) {
					if (!this.expressions[i].equals(rmpo.expressions[i])) {
						return false;
					}
				}
			} else {
				return false;
			}
			return true;
		}
		return false;
	}

	public boolean isStatebased() {
		return statebased;
	}

}

class VarHelper {
	int pos;
	int objectPosToUse;

	public VarHelper(int pos, int objectPosToUse) {
		super();
		this.pos = pos;
		this.objectPosToUse = objectPosToUse;
	}

}
