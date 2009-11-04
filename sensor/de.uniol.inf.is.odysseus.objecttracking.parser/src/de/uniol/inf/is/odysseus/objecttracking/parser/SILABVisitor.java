package de.uniol.inf.is.odysseus.objecttracking.parser;

import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.parser.cql.IVisitor;
import de.uniol.inf.is.odysseus.parser.cql.VisitorFactory;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTHost;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTInteger;
import de.uniol.inf.is.odysseus.parser.cql.parser.SimpleNode;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CreateStreamVisitor;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;

public class SILABVisitor implements IVisitor{

	static boolean registerd = VisitorFactory.getInstance().setVisitor(new SILABVisitor(), "Silab");
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
		DataDictionary.getInstance().setView(((CreateStreamVisitor)baseObject).getName(), source);
		return data;
		
	}

}
