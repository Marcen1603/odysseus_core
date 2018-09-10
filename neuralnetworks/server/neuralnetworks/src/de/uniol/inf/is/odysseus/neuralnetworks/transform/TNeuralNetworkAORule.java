package de.uniol.inf.is.odysseus.neuralnetworks.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.neuralnetworks.logicaloperator.NeuralNetworkAO;
import de.uniol.inf.is.odysseus.neuralnetworks.physicaloperator.NeuralNetworkPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TNeuralNetworkAORule extends AbstractTransformationRule<NeuralNetworkAO>
{

    @SuppressWarnings("unchecked")
    @Override
    public void execute(NeuralNetworkAO operator, TransformationConfiguration config) throws RuleException
    {
        IMetadataMergeFunction<?> metaDataMerge = MetadataRegistry.getMergeFunction(
                operator.getInputSchema(0).getMetaAttributeNames(), operator.getInputSchema(1).getMetaAttributeNames());

        TITransferArea<Tuple<ITimeInterval>, Tuple<ITimeInterval>> transferFunction = new TITransferArea<>();
        int positionOfNetwork = -1;
        positionOfNetwork = operator.getInputSchema(0).indexOf(operator.getNetworkAttribute());
        if (positionOfNetwork == -1) {
            positionOfNetwork = operator.getInputSchema(1).indexOf(operator.getNetworkAttribute());
        }
        if (positionOfNetwork == -1) {
            throw new IllegalArgumentException("the classifier attribute must be either one of port 0 or port 1!");
        }
        NeuralNetworkPO<ITimeInterval> po = new NeuralNetworkPO<ITimeInterval>(positionOfNetwork, 
                                                                               (IMetadataMergeFunction<ITimeInterval>) 
                                                                               metaDataMerge, 
                                                                               transferFunction);
        defaultExecute(operator, po, config, true, true);
    }

    @Override
    public boolean isExecutable(NeuralNetworkAO operator, TransformationConfiguration config) 
    { return operator.isAllPhysicalInputSet(); }
    @Override
    public IRuleFlowGroup getRuleFlowGroup() { return TransformRuleFlowGroup.TRANSFORMATION; }
    @Override
    public String getName()                  { return "NeuralNetworkAO -> NeuralNetworkPO";  }
    
}
