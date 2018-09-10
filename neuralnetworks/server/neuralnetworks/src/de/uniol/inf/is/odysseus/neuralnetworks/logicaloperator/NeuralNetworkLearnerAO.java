package de.uniol.inf.is.odysseus.neuralnetworks.logicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.neuralnetworks.NeuralNetworkDatatype;
import de.uniol.inf.is.odysseus.neuralnetworks.networks.INeuralNetwork.Task;

/**
 * 
 * @author Jens Plümer
 * 
 * Created at 17.05.16
 */
@LogicalOperator(category = { LogicalOperatorCategory.CLASSIFIKATION, LogicalOperatorCategory.CLUSTERING },
doc = "Trains a given neural network for the given input data and outputs trained instances of this network.", 
maxInputPorts = 1, 
minInputPorts = 1, 
name = "NEURALNETWORK_LEARNER")
public class NeuralNetworkLearnerAO extends AbstractLogicalOperator
{

    private static final long serialVersionUID = 1771912671900840524L;

    private Map<String, List<String>> nominals;
    private List<Integer> hiddenLayers;   
    private List<Option>  networkOptions;
    private List<Option>  strategyOptions;
    private Task          mlTask;
    private SDFAttribute  classAttribute;
    private Integer       inputNeurons;
    private Integer       outputNeurons;
    private String        networkType;
    private String        framework;
    private String        strategy;
    
    public NeuralNetworkLearnerAO() {}
    
    public NeuralNetworkLearnerAO(NeuralNetworkLearnerAO op)
    {
        super(op);
        inputNeurons   = op.inputNeurons;
        outputNeurons  = op.outputNeurons;
        framework      = op.framework;
        networkType    = op.networkType;
        classAttribute = op.classAttribute;
        mlTask         = op.mlTask;
        nominals       = op.nominals       != null ? new HashMap<>(op.nominals) : null;
        hiddenLayers   = op.hiddenLayers   != null ? new ArrayList<>(op.hiddenLayers) : null;
        networkOptions       = op.networkOptions        != null ? new ArrayList<>(op.networkOptions) : null;
        strategyOptions       = op.strategyOptions        != null ? new ArrayList<>(op.strategyOptions) : null;
        strategy       = op.strategy;
    }
    
    @Override
    public AbstractLogicalOperator clone()
    {
        return new NeuralNetworkLearnerAO(this);
    }
    
    @Parameter(name = "class", type = ResolvedSDFAttributeParameter.class, optional = true)
    public void setClassAttribute(SDFAttribute var) 
    {
        classAttribute = var;
    }

    
    @Parameter(name = "problem", type = StringParameter.class, optional = true)
    public void setProblem(String problem) 
    {
        switch(problem.toLowerCase())
        {
        case "regression" :     mlTask = Task.REGRESSION;    break;
        case "clustering" :     mlTask = Task.CLUSTERING;    break;
        case "classification" : mlTask = Task.CLASSIFICATION;break;
        }
    }
    
    @Parameter(name = "nominals", type = StringParameter.class, isList = true, optional = true, isMap = true)
    public void setNominals(Map<String, List<String>> var) 
    {
        nominals = var;
    }

    
    @Parameter(name = "strategyConf", type = OptionParameter.class, optional = true, isList = true)
    public void setOptions2(List<Option> options)
    {
        strategyOptions = options;
    }
    
    @Parameter(name = "networkConf", type = OptionParameter.class, optional = true, isList = true)
    public void setOptions1(List<Option> options)
    {
        networkOptions = options;
    }
    
    @Parameter(name = "inputNeurons", type = IntegerParameter.class)
    public void setInputNeurons(Integer var) 
    {
        inputNeurons = var;
    }
    
    @Parameter(name = "outputNeurons", type = IntegerParameter.class)
    public void setOutputNeurons(Integer outputs)
    {
        outputNeurons = outputs;
    }
    
    @Parameter(name = "hiddenLayers", type = IntegerParameter.class, optional = true, isList = true)
    public void setHiddenLayers(List<Integer> options) 
    {      
            hiddenLayers = options; 
    }
    
    @Parameter(name = "type", type = StringParameter.class, optional = true)
    public void setType(String type) 
    {
        networkType = type;
    }
    
    @Parameter(name = "framework", type = StringParameter.class, optional = true)
    public void setFramework(String name) 
    {
        framework= name;
    }

    @Parameter(name = "strategy", type = StringParameter.class, optional = true)
    public void setStrategy(String name) 
    {
        strategy= name;
    }
    
    public SDFAttribute getClassAttribute(){ return classAttribute; }
    public Integer getInputNeurons()       { return inputNeurons;   }
    public Integer getOutputNeurons()      { return outputNeurons;  }
    public List<Integer> getHiddenLayers() { return hiddenLayers;   }
    public List<Option> getOptions1()      { return networkOptions; }
    public List<Option> getOptions2()      { return strategyOptions;}
    public String getType()                { return networkType == null ? "feedforward" : networkType; }
    public String getFramework()           { return framework == null ? "Encog" : framework;           }
    public Task getMLTask()                { return mlTask == null ? Task.CLASSIFICATION : mlTask;     }
    
    @Override
    protected SDFSchema getOutputSchemaIntern(int pos) {
        List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
        SDFAttribute support = new SDFAttribute(null, "NeuralNetwork", NeuralNetworkDatatype.NEURALNETWORK, null, null, null);
        SDFAttribute support2 = new SDFAttribute(null, "error", SDFDatatype.DOUBLE, null, null, null);
        SDFAttribute support3 = new SDFAttribute(null, "iterations", SDFDatatype.INTEGER, null, null, null);
        SDFAttribute support4 = new SDFAttribute(null, "duration", SDFDatatype.DOUBLE, null, null, null);
        SDFAttribute support5 = new SDFAttribute(null, "concept drift", SDFDatatype.BOOLEAN, null, null, null);
        attributes.add(support);
        attributes.add(support2);
        attributes.add(support3);
        attributes.add(support4);
        attributes.add(support5);
        SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(attributes, getInputSchema(0));
        return outSchema;
    }

    public Map<SDFAttribute, List<String>> getNominals() 
    {
        if(nominals == null) return null;
        DirectAttributeResolver dar = new DirectAttributeResolver(getInputSchema(0));
        Map<SDFAttribute, List<String>> values = new HashMap<SDFAttribute, List<String>>();
        for (Entry<String, List<String>> e : nominals.entrySet())
        {
            SDFAttribute a = dar.getAttribute(e.getKey());
            values.put(a, e.getValue());
        }
        return values;
    }

    public Map<String, String> getNetworkConfiguration() 
    {
        if(networkOptions != null)
        {
            Map<String, String> optionsMap = new HashMap<>();
            for (Option o : networkOptions)
            {
                optionsMap.put(o.getName(), o.getValue());
            }
            return optionsMap;
        }
        return null;
    }

    public Map<String, String> getStrategyConfiguration()
    {
        if(strategyOptions != null)
        {
            Map<String, String> optionsMap = new HashMap<>();
            for (Option o : strategyOptions)
            {
                optionsMap.put(o.getName(), o.getValue());
            }
            return optionsMap;
        }
        return null;
    }
        
    public String getStrategy() { return strategy != null ? strategy : "default"; };
    
}
