package de.uniol.inf.is.odysseus.objecttracking.parser;

import de.uniol.inf.is.odysseus.datadictionary.DataDictionary;
import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.parser.cql.IVisitor;
import de.uniol.inf.is.odysseus.parser.cql.VisitorFactory;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTHost;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTInteger;
import de.uniol.inf.is.odysseus.parser.cql.parser.SimpleNode;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CreateStreamVisitor;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.usermanagement.User;

public class SILABVisitor implements IVisitor{

	static boolean registerd = VisitorFactory.getInstance().setVisitor(new SILABVisitor(), "Silab");
	
	private User user;
	private IDataDictionary dd;
	
	@Override
	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public void setDataDictionary(IDataDictionary dd){
		this.dd = dd;
	}
	
	
	@Override
	public Object visit(SimpleNode node, Object data, Object baseObject) {
			
		String host = ((ASTHost) node.jjtGetChild(0)).getValue();
		int port = ((ASTInteger) node.jjtGetChild(1)).getValue().intValue();
		AccessAO source = null;
		
		// TODO Tuple mode and MV mode are not supported with SILAB yet.
//		if (node.useTupleMode()) {
//			source = new AccessAO(new SDFSource(name,
//					"RelationalInputStreamAccessPO"));
//			source.setPort(port);
//			source.setHost(host);
//		} else if (node.useMVMode()) {
//			source = new AccessAO(new SDFSource(name,
//					"RelationalAtomicDataInputStreamAccessMVPO"));
//			source.setPort(port);
//			source.setHost(host);
//			source.setOutputSchema(this.attributes);
//		} else {
			source = new AccessAO(new SDFSource(((CreateStreamVisitor)baseObject).getName(),
					"RelationalSILABInputStreamAccessPO"));
			source.setPort(port);
			source.setHost(host);
			source.setOutputSchema(((CreateStreamVisitor)baseObject).getAttributes());
			
			
//		}
		ILogicalOperator op = addTimestampAO(source, source.getOutputSchema());
		dd.setStream(((CreateStreamVisitor)baseObject).getName(), op, user);
		return data;
		
	}
	
	private ILogicalOperator addTimestampAO(ILogicalOperator operator, SDFAttributeList attributes) {
		TimestampAO timestampAO = new TimestampAO();
		for(SDFAttribute attr : attributes) {
			if (attr.getDatatype().equals("StartTimestamp")){
				timestampAO.setStartTimestamp(attr);
			}
			
			if (attr.getDatatype().equals("EndTimestamp")){
				timestampAO.setEndTimestamp(attr);
			}
		}
		
		timestampAO.subscribeTo(operator, operator.getOutputSchema());
		return timestampAO;
	}

}
