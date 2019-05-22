package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.FileNameParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name="SubQuery", minInputPorts=0, maxInputPorts=Integer.MAX_VALUE, doc="This operator hides a complete subplan. Remark: Each input must be connected to a (SubQuery)Connector operator.", category = { LogicalOperatorCategory.PLAN})
public class SubQueryAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -568778238569070376L;
	
	String queryText;
	String queryParser = "OdysseusScript";
	String queryFileName;
	
	private Map<Integer, List<SDFAttribute>> outputSchema = new HashMap<>();
	
	public SubQueryAO() {
		
	}
	
	public SubQueryAO(SubQueryAO subQueryAO) {
		super(subQueryAO);
		this.queryParser = subQueryAO.queryParser;
		this.queryText = subQueryAO.queryText;
		this.queryFileName = subQueryAO.queryFileName;
		this.outputSchema = new HashMap<>(subQueryAO.outputSchema);
	}
	
	@Parameter(type = CreateSDFAttributeParameter.class, name = "Schema", isList = true, optional = true, doc = "The output schema for output port 0.")
	public void setAttributes(List<SDFAttribute> attributes) {
		this.outputSchema.put(0, attributes);
	}
	
	// TODO: allow further output schemata ...
	
	@Parameter(name="QueryFile", optional=true, type = FileNameParameter.class, doc="A file containing the query. Could e.g. be http://...")
	public void setQueryFileName(String queryFileName) {
		this.queryFileName = queryFileName;
	}
	
	public String getQueryFileName() {
		return queryFileName;
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
		if (Strings.isNullOrEmpty(queryText) && Strings.isNullOrEmpty(queryFileName)) {
			addError("One of query or queryFile must be set");
			valid = false;
		}

		if (!Strings.isNullOrEmpty(queryText) && !Strings.isNullOrEmpty(queryFileName)) {
			addError("Only one of query or queryFile can be set");
			valid = false;
		}

		return valid;
	}
	
	
	@Override
	public AbstractLogicalOperator clone() {
		return new SubQueryAO(this);
	}

}
