package de.uniol.inf.is.odysseus.storing.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class InsertDatabaseAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -7457858813451201858L;

	private boolean loopthrough = false;
	private String statement;

	public InsertDatabaseAO(String statement) {
		this.loopthrough = true;
		this.statement = statement;
	}

	public InsertDatabaseAO(boolean loopthrough, String statement) {
		this.loopthrough = loopthrough;
		this.statement = statement;
	}

	public InsertDatabaseAO(InsertDatabaseAO original) {
		this.loopthrough = original.loopthrough;
		this.statement = original.statement;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		if (loopthrough) {
			return getInputSchema();
		} else {
			return new SDFAttributeList();
		}
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new InsertDatabaseAO(this);
	}

}
