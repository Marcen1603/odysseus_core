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
package de.uniol.inf.is.odysseus.broker.parser.cql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.broker.dictionary.BrokerDictionary;
import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAO;
import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAOFactory;
import de.uniol.inf.is.odysseus.broker.metric.MetricMeasureAO;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatypeConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.unit.SDFUnit;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryException;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFSchemaExtended;
import de.uniol.inf.is.odysseus.parser.cql.CQLParser;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttrDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttributeDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttributeDefinitions;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttributeType;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerAsSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerQueue;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerSelectInto;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerSimpleSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTComplexSelectStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateBroker;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTListDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTMetric;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTORSchemaDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTRecordDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTRecordEntryDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSelectStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSimpleSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.Node;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.AbstractDefaultVisitor;

/**
 * The BrokerVisitor provides the implementation of the broker related parts
 * from the translation. This encapsulate the general translation from the
 * broker specific translation to avoid dependencies.
 * 
 * @author Dennis Geesen
 */
public class BrokerVisitor extends AbstractDefaultVisitor {

    private ISession caller;
    private IDataDictionary dataDictionary;

    public BrokerVisitor() {
    }

    public void setUser(ISession user) {
        this.caller = user;
    }

    public void setDataDictionary(IDataDictionary dd) {
        this.dataDictionary = dd;
    }

    /*
     * (non-Javadoc)
     * 
     * @seede.uniol.inf.is.odysseus.parser.cql.parser.transformation.
     * AbstractDefaultVisitor
     * #visit(de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerSource,
     * java.lang.Object)
     */
    @Override
    public Object visit(ASTBrokerSource node, Object data) {
        BrokerAO readFromBroker = null;
        if (node.jjtGetChild(0) instanceof ASTBrokerSimpleSource) {
            readFromBroker = (BrokerAO) visit((ASTBrokerSimpleSource) node.jjtGetChild(0), data);
        } else if (node.jjtGetChild(0) instanceof ASTBrokerAsSource) {
            readFromBroker = (BrokerAO) visit((ASTBrokerAsSource) node.jjtGetChild(0), data);
        }
        if (node.jjtGetNumChildren() > 1) {
            if (node.jjtGetChild(1) != null) {
                if (node.jjtGetChild(1) instanceof ASTBrokerQueue) {
                    ASTComplexSelectStatement statement = (ASTComplexSelectStatement) node.jjtGetChild(1).jjtGetChild(0);
                    CQLParser parser = new CQLParser();
                    parser.setUser(caller);
                    AbstractLogicalOperator topOfQueue = (AbstractLogicalOperator) parser.visit(statement, null);
                    if (readFromBroker != null) {
                        // queue - writing is always on port 1
                        readFromBroker.subscribeToSource(topOfQueue, 1, 0, topOfQueue.getOutputSchema());
                    }
                }
            }
        }
        return readFromBroker;

    }

    /*
     * (non-Javadoc)
     * 
     * @seede.uniol.inf.is.odysseus.parser.cql.parser.transformation.
     * AbstractDefaultVisitor
     * #visit(de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerSimpleSource,
     * java.lang.Object)
     */
    @Override
    public Object visit(ASTBrokerSimpleSource node, Object data) {
        return getSimpleSource(node, data);
    }

    /*
     * (non-Javadoc)
     * 
     * @seede.uniol.inf.is.odysseus.parser.cql.parser.transformation.
     * AbstractDefaultVisitor
     * #visit(de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerAsSource,
     * java.lang.Object)
     */
    @Override
    public Object visit(ASTBrokerAsSource node, Object data) {
        ASTIdentifier ident = (ASTIdentifier) node.jjtGetChild(1);
        String name = ident.getName();

        // parse the nested substatement
        ASTComplexSelectStatement childNode = (ASTComplexSelectStatement) node.jjtGetChild(0);
        CQLParser v = new CQLParser();
        v.setUser(caller);
        AbstractLogicalOperator result = (AbstractLogicalOperator) v.visit(childNode, null);

        BrokerAO broker = BrokerAOFactory.getFactory().createBrokerAO(name);
        broker.setSchema(result.getOutputSchema());
        if (!BrokerDictionary.getInstance().brokerExists(name)) {
            BrokerDictionary.getInstance().addBroker(name, (SDFSchemaExtended)broker.getOutputSchema(), broker.getQueueSchema());
        }

        // connect the source to broker
        broker.subscribeToSource(result, 0, 0, result.getOutputSchema());
        // make it accessible like a normal source
        dataDictionary.addSourceType(name, "brokerStreaming");
        dataDictionary.addEntitySchema(name, broker.getOutputSchema(), caller);
        return broker;

    }

    /*
     * (non-Javadoc)
     * 
     * @seede.uniol.inf.is.odysseus.parser.cql.parser.transformation.
     * AbstractDefaultVisitor
     * #visit(de.uniol.inf.is.odysseus.parser.cql.parser.ASTSimpleSource,
     * java.lang.Object)
     */
    @Override
    public Object visit(ASTSimpleSource node, Object data) {
        return getSimpleSource(node, data);

    }

    /**
     * Gets a Broker as a simple source.
     * 
     * @param node
     *            the node
     * @param data
     *            the data
     * @return the simple source
     */
    private static Object getSimpleSource(Node node, Object data) {
        String brokerName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
        if (BrokerDictionary.getInstance().brokerExists(brokerName)) {
            BrokerAO broker = BrokerAOFactory.getFactory().createBrokerAO(brokerName);
            return broker;
        }

        throw new RuntimeException("Broker " + brokerName + " not exists");
    }

    /*
     * (non-Javadoc)
     * 
     * @seede.uniol.inf.is.odysseus.parser.cql.parser.transformation.
     * AbstractDefaultVisitor
     * #visit(de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerSelectInto,
     * java.lang.Object)
     */
    @Override
    public Object visit(ASTBrokerSelectInto node, Object data) {
        ASTSelectStatement statement = new ASTSelectStatement(0);
        int number = 0;

        // add selectClause
        statement.jjtAddChild(node.jjtGetChild(0), number);
        number++;
        // get broker name
        String brokerName = ((ASTIdentifier) node.jjtGetChild(1)).getName();

        // add rest from select except of the last one if it's a complex
        // statement
        int numChilds = node.jjtGetNumChildren();
        for (int i = 2; i < numChilds; i++) {
            statement.jjtAddChild(node.jjtGetChild(i), number);
            number++;
        }
        // parse first nested statement
        CQLParser v = new CQLParser();
        v.setUser(caller);
        AbstractLogicalOperator topOfSelectStatementOperator = (AbstractLogicalOperator) v.visit(statement, null);

        BrokerAO broker = BrokerAOFactory.getFactory().createBrokerAO(brokerName);
        // add the schemas from existing one
        if (!BrokerDictionary.getInstance().brokerExists(brokerName)) {
            throw new RuntimeException("Broker with name \"" + brokerName + "\" not found. You have to create one first.");
        }
        broker.setSchema(BrokerDictionary.getInstance().getSchema(brokerName));
        broker.setQueueSchema(BrokerDictionary.getInstance().getQueueSchema(brokerName));

        // check schemas
        if (!schemaEquals(topOfSelectStatementOperator.getOutputSchema(), broker.getOutputSchema())) {
            String message = "Schema to insert: " + topOfSelectStatementOperator.getOutputSchema().toString() + "\n";
            message = message + "Schema of Broker: " + broker.getOutputSchema().toString();
            throw new RuntimeException("Statement and broker do not have the same schema.\n" + message);
        }
        // connect the nested statement into the broker
        broker.subscribeToSource(topOfSelectStatementOperator, 0, 0, topOfSelectStatementOperator.getOutputSchema());
        return broker;

    }

    /*
     * (non-Javadoc)
     * 
     * @seede.uniol.inf.is.odysseus.parser.cql.parser.transformation.
     * AbstractDefaultVisitor
     * #visit(de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateBroker,
     * java.lang.Object)
     */
    @Override
    public Object visit(ASTCreateBroker node, Object data) {
        String brokerName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
        // check first if name already exists
        if (BrokerDictionary.getInstance().brokerExists(brokerName)) {
            throw new RuntimeException("There is already a broker named \"" + brokerName + "\".");
        }

        // parse attributes
        List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
        if (node.jjtGetChild(1) instanceof ASTORSchemaDefinition) {
            SDFAttribute rootAttribute = (SDFAttribute) node.jjtGetChild(1).jjtAccept(this, brokerName);
            attributes.add(rootAttribute);
        } else {
            ASTAttributeDefinitions attributeDe = (ASTAttributeDefinitions) node.jjtGetChild(1);
            try {

                for (int i = 0; i < attributeDe.jjtGetNumChildren(); i++) {
                    ASTAttributeDefinition attrNode = (ASTAttributeDefinition) attributeDe.jjtGetChild(i);
                    String attrName = ((ASTIdentifier) attrNode.jjtGetChild(0)).getName();
                    SDFUnit unit = null;
                    List<?> cov = null;
                    Map<String, SDFDatatypeConstraint> dtContraints = new HashMap<String, SDFDatatypeConstraint>();

                    ASTAttributeType astAttrType = (ASTAttributeType) attrNode.jjtGetChild(1);
                    SDFDatatype datatype;
                    datatype = dataDictionary.getDatatype(astAttrType.getType());
                    if (datatype.isDate()) {
                        dtContraints.put("format", astAttrType.getDateFormat());
                    }
                    if (datatype.isMeasurementValue() && astAttrType.jjtGetNumChildren() > 0) {
                        cov = (List<?>) astAttrType.jjtGetChild(0).jjtAccept(this, data);

                    }
                    SDFAttribute attribute = new SDFAttribute(brokerName, attrName, datatype, unit, dtContraints, cov);

                    attributes.add(attribute);
                }
            } catch (DataDictionaryException e) {
                throw new QueryParseException(e.getMessage());
            }

        }
        SDFSchemaExtended schema = new SDFSchemaExtended(attributes);

        List<SDFAttribute> metaAttributes = new ArrayList<SDFAttribute>();
        // parse meta attributes
        if (node.jjtGetNumChildren() > 2) {
            if (node.jjtGetChild(2) != null) {

                if (node.jjtGetChild(2) instanceof ASTAttributeDefinitions) {
                    ASTAttributeDefinitions metaAttributeDe = (ASTAttributeDefinitions) node.jjtGetChild(2);
                    for (int i = 0; i < metaAttributeDe.jjtGetNumChildren(); i++) {
                        ASTAttributeDefinition attrNode = (ASTAttributeDefinition) metaAttributeDe.jjtGetChild(i);
                        String attrName = ((ASTIdentifier) attrNode.jjtGetChild(0)).getName();

                        ASTAttributeType astAttrType = (ASTAttributeType) attrNode.jjtGetChild(1);
                        SDFUnit unit = null;
                        List<?> cov = null;
                        Map<String, SDFDatatypeConstraint> dtContraints = new HashMap<String, SDFDatatypeConstraint>();

                        SDFDatatype datatype;
                        try {
                            datatype = dataDictionary.getDatatype(astAttrType.getType());
                        } catch (Exception e) {
                            throw new QueryParseException(e.getMessage());
                        }
                        if (datatype.isDate()) {
                            dtContraints.put("format", astAttrType.getDateFormat());
                        }
                        if (datatype.isMeasurementValue() && astAttrType.jjtGetNumChildren() > 0) {
                            cov = ((List<?>) astAttrType.jjtGetChild(0).jjtAccept(this, data));

                        }
                        SDFAttribute attribute = new SDFAttribute(brokerName, attrName, datatype, unit, dtContraints, cov);
                        metaAttributes.add(attribute);
                    }
                } else if (node.jjtGetChild(2) instanceof ASTORSchemaDefinition) {
                    SDFAttribute rootAttribute = (SDFAttribute) node.jjtGetChild(2).jjtAccept(this, brokerName);
                    metaAttributes.add(rootAttribute);
                }
            }
        }
        SDFSchema metaAttributSchema = new SDFSchema("", metaAttributes);

        // make it accessible like a normal source
        dataDictionary.addSourceType(brokerName, "brokerStreaming");
        dataDictionary.addEntitySchema(brokerName, schema, caller);
        // create the broker
        BrokerAO broker = BrokerAOFactory.getFactory().createBrokerAO(brokerName);
        broker.setSchema(schema);
        broker.setQueueSchema(metaAttributSchema);
        BrokerDictionary.getInstance().addBroker(brokerName, (SDFSchemaExtended)broker.getOutputSchema(), broker.getQueueSchema());

        // set the broker view in the data dictionary
        // used for procedural parser
        BrokerDictionary.getInstance().setLogicalPlan(brokerName, broker);

        // Is this necessary any more?
        try {
            dataDictionary.setView(brokerName, broker, caller);
        } catch (DataDictionaryException e) {
            throw new QueryParseException(e.getMessage());
        }

        return broker;
    }

    @Override
    public Object visit(ASTMetric node, Object data) {
        String attribute = ((ASTIdentifier) node.jjtGetChild(0)).getName();
        AbstractLogicalOperator topOp = (AbstractLogicalOperator) data;
        MetricMeasureAO metricOp = new MetricMeasureAO(attribute);
        metricOp.setOutputSchema(topOp.getOutputSchema());
        topOp.subscribeSink(metricOp, 0, 0, topOp.getOutputSchema());
        return metricOp;
    }

    /**
     * Checks whether two schema are equal. This is only based on the attribute
     * names and not on the assigned source names as well.
     * 
     * @param left
     *            the left
     * @param right
     *            the right
     * @return true, if successful
     */
    private static boolean schemaEquals(SDFSchema left, SDFSchema right) {
        if (left.size() != right.size()) {
            return false;
        }
        
        for (int i = 0; i < left.size(); i++) {
            if (!left.get(i).getAttributeName().equals(right.get(i).getAttributeName())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object visit(ASTORSchemaDefinition node, Object data) {
        return node.jjtGetChild(0).jjtAccept(this, data);
    }

    @Override
    public Object visit(ASTRecordDefinition node, Object data) {
        String attrName = ((ASTIdentifier) node.jjtGetChild(0)).getName();

        // create a new datatype from this record attribute

        List<SDFAttribute> complexAttrList = new ArrayList<SDFAttribute>();
        for (int i = 1; i < node.jjtGetNumChildren(); i++) {
            SDFAttribute attr = (SDFAttribute) node.jjtGetChild(i).jjtAccept(this, data);
            complexAttrList.add(attr);
        }

        SDFSchema complexAttrSchema = new SDFSchema("", complexAttrList);
        SDFDatatype recordType = new SDFDatatype(data.toString() + "." + attrName, SDFDatatype.KindOfDatatype.TUPLE, complexAttrSchema);
        try {
            dataDictionary.addDatatype(recordType.getURI(), recordType);
        } catch (DataDictionaryException e) {
            throw new QueryParseException(e.getMessage());
        }

        SDFAttribute recordAttribute = new SDFAttribute(data.toString(), attrName, recordType);

        return recordAttribute;
    }

    @Override
    public Object visit(ASTRecordEntryDefinition node, Object data) {
        return node.jjtGetChild(0).jjtAccept(this, data);
    }

    @Override
    public Object visit(ASTListDefinition node, Object data) {
        String attrName = ((ASTIdentifier) node.jjtGetChild(0)).getName();

        List<SDFAttribute> complexAttrList = new ArrayList<SDFAttribute>();
        for (int i = 1; i < node.jjtGetNumChildren(); i++) {
            SDFAttribute listedAttribute = (SDFAttribute) node.jjtGetChild(i).jjtAccept(this, data);
            complexAttrList.add(listedAttribute);
        }

        SDFSchema complexAttrSchema = new SDFSchema("", complexAttrList);

        SDFDatatype listType = new SDFDatatype(data.toString() + "." + attrName, SDFDatatype.KindOfDatatype.MULTI_VALUE, complexAttrSchema);
        try {
            dataDictionary.addDatatype(listType.getURI(), listType);
        } catch (DataDictionaryException e) {
            throw new QueryParseException(e.getMessage());
        }

        SDFAttribute attribute = new SDFAttribute(data.toString(), attrName, listType);

        return attribute;
    }

    @Override
    public Object visit(ASTAttrDefinition node, Object data) {
        String attrName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
        ASTAttributeType astAttrType = (ASTAttributeType) node.jjtGetChild(1);

        SDFDatatype datatype;
        try {
            datatype = dataDictionary.getDatatype(astAttrType.getType());
        } catch (DataDictionaryException e) {
            throw new QueryParseException(e.getMessage());
        }
        List<?> cov = null;
        Map<String, SDFDatatypeConstraint> dtconstr = new HashMap<String, SDFDatatypeConstraint>();
        SDFUnit unit = null;

        if (datatype.isMeasurementValue() && astAttrType.jjtGetNumChildren() > 0) {
            cov = (List<?>) astAttrType.jjtGetChild(0).jjtAccept(this, data);

        }
        if (datatype.isDate())
            dtconstr.put("format", astAttrType.getDateFormat());

        SDFAttribute attribute = new SDFAttribute(data.toString(), attrName, datatype, unit, dtconstr, cov);
        return attribute;
    }
}
