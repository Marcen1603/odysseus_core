package de.uniol.inf.is.odysseus.neuralnetworks.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(category = { LogicalOperatorCategory.CLASSIFIKATION, LogicalOperatorCategory.CLUSTERING },
doc = "Performs a classification or clustering task for a given input. The left port defines the input data "
    + "and the right port defines the input stream for neural networks.", 
maxInputPorts = 2, 
minInputPorts = 2, 
name = "NEURALNETWORK")
public class NeuralNetworkAO extends AbstractLogicalOperator
{
    
    private SDFAttribute networkAttribute;
    private String       className = "clazz";
    private boolean      block = true;

    
    private static final long serialVersionUID = 1771912671900840524L;

    public NeuralNetworkAO() {}
    
    public NeuralNetworkAO(NeuralNetworkAO op)//TODO networkAttribute set default-value
    {
        this.networkAttribute = op.networkAttribute;
        this.className        = op.className;
    }

    @Override
    public AbstractLogicalOperator clone()
    {
        return new NeuralNetworkAO(this);
    }
    
    @Parameter(name = "block", type = BooleanParameter.class, optional = true)
    public void setBlock(Boolean block)
    {
        this.block = block;
    }
    
    @Parameter(name="network", type = ResolvedSDFAttributeParameter.class, optional = false)
    public void setNetworkAttribute(SDFAttribute network)
    {
        this.networkAttribute = network;
    }
    
    @Parameter(name="classname", type=StringParameter.class, doc="The name of the classification result", optional = true)
    public void setClassName(String className) 
    {
        this.className = className;
    }
    
    @Override
    protected SDFSchema getOutputSchemaIntern(int pos) 
    {
        SDFAttribute attributeId = new SDFAttribute(null, className, SDFDatatype.STRING, null, null, null);
        SDFSchema outSchema = SDFSchemaFactory.createNewAddAttribute(attributeId, getInputSchema(0));
        return outSchema;

    }
    
    public SDFAttribute getNetworkAttribute() { return this.networkAttribute; }
    public String getClassName()              { return this.className; }

    public boolean isBlock()
    {
        return block;
    }


}
