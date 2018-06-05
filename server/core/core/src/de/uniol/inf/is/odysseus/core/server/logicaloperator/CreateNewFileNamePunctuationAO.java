package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionParameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;

@LogicalOperator(name="CreateNewFilenamePunctuation", maxInputPorts=1, minInputPorts=1,doc="Depending on a predicate and a name: Create NewFilenamePunctuations", category = { LogicalOperatorCategory.PROCESSING})
public class CreateNewFileNamePunctuationAO extends UnaryLogicalOp implements
		IHasPredicate {

	private static final long serialVersionUID = 1226925433467106010L;

	IPredicate<?> createNewPuncPredicate;
	SDFExpression filenameExpression;
	
	public CreateNewFileNamePunctuationAO(){		
	}
	
	public CreateNewFileNamePunctuationAO(CreateNewFileNamePunctuationAO ao){
		this.createNewPuncPredicate = ao.createNewPuncPredicate.clone();
		this.filenameExpression = ao.filenameExpression.clone();
	}
	
	@Override
	public IPredicate<?> getPredicate() {
		return createNewPuncPredicate;
	}

	public IPredicate<?> getCreateNewPuncPredicate() {
		return createNewPuncPredicate;
	}

	@Parameter(name="predicate", type = PredicateParameter.class, doc="If expression evaluates to true, a NewFileNamePunctuation is created from the filename attribute value")
	public void setCreateNewPuncPredicate(IPredicate<?> createNewPuncPredicate) {
		this.createNewPuncPredicate = createNewPuncPredicate;
	}

	public SDFExpression getFilenameExpression() {
		return filenameExpression;
	}

	@Parameter(name="filename", type = NamedExpressionParameter.class, doc = "The expression to create the output filename.")
	public void setFilenameAttribute(NamedExpression filenameExpression) {
		this.filenameExpression = filenameExpression.expression;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new CreateNewFileNamePunctuationAO(this);
	}

	@Override
	public void setPredicate(IPredicate<?> predicate) {
		this.setPredicate(predicate);
	}

}
