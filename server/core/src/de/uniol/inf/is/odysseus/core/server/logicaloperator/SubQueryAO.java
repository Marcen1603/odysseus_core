package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.FileNameParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name="SubQuery", minInputPorts=0, maxInputPorts=Integer.MAX_VALUE, doc="This operator hides a complete subplan. Remark: Each input must be connected to a (SubQuery)Connector operator.", category = { LogicalOperatorCategory.PLAN})
public class SubQueryAO extends AbstractSchemaBasedAO {

	private static final long serialVersionUID = -568778238569070376L;
	
	String queryText;
	String queryParser = "OdysseusScript";
	String queryFileName;
	String queryName;
	Integer queryID;
		
	public SubQueryAO() {
	}
	
	public SubQueryAO(SubQueryAO subQueryAO) {
		super(subQueryAO);
		this.queryParser = subQueryAO.queryParser;
		this.queryText = subQueryAO.queryText;
		this.queryFileName = subQueryAO.queryFileName;
		this.queryName = subQueryAO.queryName;
		this.queryID = subQueryAO.queryID;
	}
	
	@Parameter(name="QueryFile", optional=true, type = FileNameParameter.class, doc="A file containing the query. Could e.g. be http://...")
	public void setQueryFileName(String queryFileName) {
		this.queryFileName = queryFileName;
	}
	
	public String getQueryFileName() {
		return queryFileName;
	}
	
	@Parameter(name="QueryID", optional=true, type = IntegerParameter.class, doc="An already existing subquery with given ID.")
	public void setQueryID(Integer queryID) {
		this.queryID = queryID;
	}
	
	public Integer getQueryID() {
		return queryID;
	}

	@Parameter(name="QueryName", optional=true, type = StringParameter.class, doc="An already existing subquery with given name.")
	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}
	
	
	public String getQueryName() {
		return queryName;
	}
	
	@Parameter(name = "Query", optional = true, type = StringParameter.class, doc="The query string to use.")
	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}
	
	public String getQueryText() {
		return queryText;
	}
	
	@Parameter(name = "Parser", optional = true, type = StringParameter.class)
	public void setQueryParser(String queryParser) {
		this.queryParser = queryParser;
	}
	
	public String getQueryParser() {
		return queryParser;
	}
	
	@Override
	public boolean isValid() {
		boolean valid = true;
		if (Strings.isNullOrEmpty(queryText) && Strings.isNullOrEmpty(queryFileName) && Strings.isNullOrEmpty(queryName) && queryID==null) {
			addError("One of query, queryFile, queryId or queryName must be set");
			valid = false;
		}

//		if (!Strings.isNullOrEmpty(queryText) && !Strings.isNullOrEmpty(queryFileName)) {
//			addError("Only one of query or queryFile can be set");
//			valid = false;
//		}

		return valid;
	}
	
	
	@Override
	public AbstractLogicalOperator clone() {
		return new SubQueryAO(this);
	}

}
