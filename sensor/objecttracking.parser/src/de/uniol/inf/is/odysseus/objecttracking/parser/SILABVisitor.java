/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.objecttracking.parser;

import de.uniol.inf.is.odysseus.datadictionary.DataDictionaryException;
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
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.usermanagement.ISession;

public class SILABVisitor implements IVisitor{

	static boolean registerd = VisitorFactory.getInstance().setVisitor(new SILABVisitor(), "Silab");
	
	private ISession user;
	private IDataDictionary dd;
	
	@Override
	public void setUser(ISession user) {
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
			String name = ((CreateStreamVisitor)baseObject).getName();
			source = new AccessAO(new SDFSource(name,
					"RelationalSILABInputStreamAccessPO"));
			source.setPort(port);
			source.setHost(host);
			source.setOutputSchema(new SDFSchema(name, ((CreateStreamVisitor)baseObject).getAttributes()));
			
			
//		}
		ILogicalOperator op = addTimestampAO(source, source.getOutputSchema());
		try {
			dd.setStream(((CreateStreamVisitor)baseObject).getName(), op, user);
		} catch (DataDictionaryException e) {
			throw new QueryParseException(e.getMessage());
		}
		return data;
		
	}
	
	private ILogicalOperator addTimestampAO(ILogicalOperator operator, SDFSchema attributes) {
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
