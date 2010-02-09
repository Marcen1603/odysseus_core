package de.uniol.inf.is.odysseus.broker.parser.cql;

import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.RenameAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.parser.cql.CQLParser;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTComplexSelectStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSimpleSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.AbstractDefaultVisitor;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;

public class CreateBrokerSourceVisitor extends AbstractDefaultVisitor {

	@Override
	public Object visit(ASTBrokerSource node, Object data) {
		ASTIdentifier ident = (ASTIdentifier) node.jjtGetChild(0);
		String name = ident.getName();

		// parse the nested substatement
		ASTComplexSelectStatement childNode = (ASTComplexSelectStatement) node
				.jjtGetChild(1);
		CQLParser v = new CQLParser();
		AbstractLogicalOperator result = (AbstractLogicalOperator) v.visit(
				childNode, null);

		// get existing broker or generate a new one
		BrokerAO broker = new BrokerAO(name);
		broker.setOutputSchema(result.getOutputSchema());
		if (!BrokerDictionary.getInstance().brokerExists(name)) {
			BrokerDictionary.getInstance().addBroker(name, broker);
		} else {
			broker = BrokerDictionary.getInstance().getBroker(name);
		}

		// connect the source to broker
		broker.subscribeTo(result, result.getOutputSchema());
		// make it accessible like a normal source
		DataDictionary.getInstance().sourceTypeMap.put(name, "brokerStreaming");
		SDFEntity entity = new SDFEntity(name);
		SDFAttributeList attributes = new SDFAttributeList();
		entity.setAttributes(attributes);
		DataDictionary.getInstance().entityMap.put(name, entity);
		return broker;
	}

	public Object visit(ASTSimpleSource node,Object data) {
		String brokerName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		if (BrokerDictionary.getInstance().brokerExists(brokerName)) {					
			//BrokerAO broker = BrokerDictionary.getInstance().getBroker(brokerName);			
			//return broker;
			BrokerAO broker = new BrokerAO(brokerName);
			broker.setOutputSchema(BrokerDictionary.getInstance().getBroker(brokerName).getOutputSchema());
			return broker;
		}else{
			throw new RuntimeException("Broker "+brokerName+" not exists");
		}
		
	}

}
