package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionParameter;

@LogicalOperator(name = "TOPK", maxInputPorts = 1, minInputPorts = 1, category = {
		LogicalOperatorCategory.ADVANCED }, doc = "Calculate the top k elements of the input")
public class TopKAO extends AbstractLogicalOperator {
	
	private static final long serialVersionUID = 8852471127806000337L;

	private NamedExpression setupFunction;
	private NamedExpression preScoreFunction;
	private NamedExpression scoringFunction;
	private NamedExpression tearDownFunction;
	// TODO: This expression is typically defined over the input schema --> must be defined over the output schema ...
	private NamedExpression cleanupPredicate;
	
	private int k;
	private boolean descending = true;
	private boolean suppressDuplicates = true;
	private List<SDFAttribute> groupingAttributes;
	private List<SDFAttribute> uniqueAttributes;
	private boolean fastGrouping = false;
	private boolean tieWithTimestamp = false;
	private boolean triggerOnlyByPunctuation = false;
	private boolean recalcScore = false;
	private boolean addScore = false;
	
	public TopKAO() {
	}

	public TopKAO(TopKAO other) {
		super(other);
		this.scoringFunction = other.scoringFunction;
		this.preScoreFunction = other.preScoreFunction;
		this.setupFunction = other.setupFunction;
		this.tearDownFunction = other.tearDownFunction;
		this.cleanupPredicate = other.cleanupPredicate;
		this.k = other.k;
		this.descending = other.descending;
		this.setSuppressDuplicates(other.isSuppressDuplicates());
		if (other.groupingAttributes != null) {
			this.groupingAttributes = new ArrayList<SDFAttribute>(other.groupingAttributes);
		}
		if (other.uniqueAttributes != null){
			this.uniqueAttributes = new ArrayList<>(other.uniqueAttributes);
		}
		this.fastGrouping = other.fastGrouping;
		this.tieWithTimestamp = other.tieWithTimestamp;
		this.recalcScore = other.recalcScore;
		this.addScore = other.addScore;
		this.triggerOnlyByPunctuation = other.triggerOnlyByPunctuation;
	}
	

	
	public NamedExpression getSetupFunction() {
		return setupFunction;
	}

	@Parameter(name = "setupFunction", optional = true, type = NamedExpressionParameter.class, doc = "This function is called for every input element before calculating the score.")
	public void setSetupFunction(NamedExpression setupFunction) {
		this.setupFunction = setupFunction;
	}

	public NamedExpression getTearDownFunction() {
		return tearDownFunction;
	}

	@Parameter(name = "tearDownFunction", optional = true, type = NamedExpressionParameter.class, doc = "This function is called for every input element after calculating the score.")
	public void setTearDownFunction(NamedExpression tearDownFunction) {
		this.tearDownFunction = tearDownFunction;
	}

	public NamedExpression getCleanupPredicate() {
		return cleanupPredicate;
	}

	@Parameter(name = "cleanUpPredicate", optional = true, type = NamedExpressionParameter.class, doc = "This (optional) predicate is used to clean up the state after processing the input.")
	public void setCleanupPredicate(NamedExpression cleanupPredicate) {
		this.cleanupPredicate = cleanupPredicate;
	}

	public NamedExpression getScoringFunction() {
		return scoringFunction;
	}

	@Parameter(name = "scoringFunction", optional = false, type = NamedExpressionParameter.class, doc = "The scoring function for ordering")
	public void setScoringFunction(NamedExpression scoringFunction) {
		this.scoringFunction = scoringFunction;
	}

	public NamedExpression getPreScoreFunction() {
		return preScoreFunction;
	}
	
	@Parameter(name = "preScoreFunction", optional = true, type = NamedExpressionParameter.class, doc = "This function be will called on the input before each element is scored. Typically used in case where recalcScore is set to true.")
	public void setPreScoreFunction(NamedExpression preScoreFunction) {
		this.preScoreFunction = preScoreFunction;
	}
	
	public int getK() {
		return k;
	}

	@Parameter(name = "k", optional = false, type = IntegerParameter.class, doc = "The number of elements to sort")
	public void setK(int k) {
		this.k = k;
	}

	public boolean isDescending() {
		return descending;
	}

	@Parameter(name = "descending", optional = true, type = BooleanParameter.class, doc = "Sort descending (default is true)")
	public void setDescending(boolean ascending) {
		this.descending = ascending;
	}

	public boolean isAddScore() {
		return addScore;
	}

	@Parameter(optional = true, type = BooleanParameter.class, doc = "If set to true, the score value will be added to each output element in the top k list. Default is false.")
	public void setAddScore(boolean addScore) {
		this.addScore = addScore;
	}

	public boolean isTiWithTimestamp() {
		return tieWithTimestamp;
	}

	@Parameter(name = "tieWithTimestamp", optional = true, type = BooleanParameter.class, doc = "If two elements have the same score, this value can be used to define an order by time stamps. (Default is false)")
	public void setTiWithTimestamp(boolean tieWithTimestamp) {
		this.tieWithTimestamp = tieWithTimestamp;
	}
	
	public boolean isRecalcScore() {
		return recalcScore;
	}
	
	@Parameter(name = "recalcScore", optional = true, type = BooleanParameter.class, doc = "Sometime the score for an elements depends on state information. Set recalcScore to true to update for each (!) stored element every time a new output is triggered.")
	public void setRecalcScore(boolean recalcScore) {
		this.recalcScore = recalcScore;
	}

	/**
	 * @return the suppressDuplicates
	 */
	public boolean isSuppressDuplicates() {
		return suppressDuplicates;
	}

	/**
	 * @param suppressDuplicates
	 *            the suppressDuplicates to set
	 */
	@Parameter(name = "suppressDuplicates", optional = true, type = BooleanParameter.class, doc = "If set to true (default), output is only generated when a new top k set is available")
	public void setSuppressDuplicates(boolean suppressDuplicates) {
		this.suppressDuplicates = suppressDuplicates;
	}

	@Parameter(name = "GROUP_BY", optional = true, type = ResolvedSDFAttributeParameter.class, isList = true)
	public void setGroupingAttributes(List<SDFAttribute> attributes) {
		this.groupingAttributes = attributes;
	}

	public List<SDFAttribute> getGroupingAttributes() {
		return groupingAttributes;
	}

	public List<SDFAttribute> getUniqueAttributes() {
		return uniqueAttributes;
	}

	@Parameter(name = "uniqueAttributes", optional = true, type = ResolvedSDFAttributeParameter.class, isList = true)
	public void setUniqueAttributes(List<SDFAttribute> uniqueAttributes) {
		this.uniqueAttributes = uniqueAttributes;
	}

	/**
	 * @return the fastGrouping
	 */
	public boolean isFastGrouping() {
		return fastGrouping;
	}

	/**
	 * @param fastGrouping
	 *            the fastGrouping to set
	 */
	@Parameter(name = "fastGrouping", type = BooleanParameter.class, optional = true, doc = "Use hash code instead of tuple compare to create group. Potentially unsafe!")
	public void setFastGrouping(boolean fastGrouping) {
		this.fastGrouping = fastGrouping;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		SDFSchema subSchema = getInputSchema(0);
		// TODO addScore flag
		if (addScore){

			SDFAttribute score = new SDFAttribute(subSchema.getAttribute(0).getSourceName(), "score", SDFDatatype.DOUBLE);
			subSchema = SDFSchemaFactory.createNewAddAttribute(score, subSchema);
		}
		
		List<SDFAttribute> attributes = new LinkedList<SDFAttribute>();
		attributes.add(new SDFAttribute(subSchema.getURI(), "topk",
				SDFDatatype.createTypeWithSubSchema(SDFDatatype.LIST_TUPLE, subSchema)));
		
		attributes.add(new SDFAttribute(subSchema.getURI(), "trigger",
				SDFDatatype.createTypeWithSubSchema(SDFDatatype.TUPLE, subSchema)));
		if (groupingAttributes != null) {
			attributes.addAll(groupingAttributes);
		}
		SDFSchema outputSchema = SDFSchemaFactory.createNewWithAttributes(attributes, subSchema);
		return outputSchema;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new TopKAO(this);
	}

	/**
	 * @return triggerByPunctuation
	 */
	@Deprecated
	public boolean isTriggerByPunctuation() {
		return triggerOnlyByPunctuation;
	}

	/**
	 * If triggerByPunctuation is set, the operator waits for a punctuation of
	 * type TuplePunctuation that holds the grouping attributes as tuple. If
	 * such a punctuation arrives, the top-K of this group is output. If this
	 * attribute is unset, the top-K set is output by every tuple that arrives.
	 */
	@Parameter(name = "triggerByPunctuation", optional = true, type = BooleanParameter.class, doc = "If set to true, output is only generated when punctuation arrives.", deprecated=true)
	@Deprecated
	public void setTriggerByPunctuation(boolean triggerByPunctuation) {
		this.triggerOnlyByPunctuation = triggerByPunctuation;
	}

	/**
	 * @return triggerByPunctuation
	 */
	public boolean isTriggerOnlyByPunctuation() {
		return triggerOnlyByPunctuation;
	}

	/**
	 * If triggerByPunctuation is set, the operator waits for a punctuation of
	 * type TuplePunctuation that holds the grouping attributes as tuple. If
	 * such a punctuation arrives, the top-K of this group is output. If this
	 * attribute is unset, the top-K set is output by every tuple that arrives.
	 */
	@Parameter(name = "triggerOnlyByPunctuation", optional = true, type = BooleanParameter.class, doc = "If set to true, output is only generated when punctuation arrives.")
	public void setTriggerOnlyByPunctuation(boolean triggerByPunctuation) {
		this.triggerOnlyByPunctuation = triggerByPunctuation;
	}

	
	
}
