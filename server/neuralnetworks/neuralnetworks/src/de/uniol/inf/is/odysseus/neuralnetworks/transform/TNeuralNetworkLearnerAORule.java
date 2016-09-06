package de.uniol.inf.is.odysseus.neuralnetworks.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.neuralnetworks.logicaloperator.NeuralNetworkLearnerAO;
import de.uniol.inf.is.odysseus.neuralnetworks.networks.INeuralNetwork;
import de.uniol.inf.is.odysseus.neuralnetworks.networks.NeuralNetworkFactory;
import de.uniol.inf.is.odysseus.neuralnetworks.physicaloperator.NeuralNetworkLearnerPO;
import de.uniol.inf.is.odysseus.neuralnetworks.strategy.IStrategy;
import de.uniol.inf.is.odysseus.neuralnetworks.strategy.StrategyFactory;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Jens Plümer
 * 
 * Created at 17.05.16
 */
public class TNeuralNetworkLearnerAORule extends AbstractTransformationRule<NeuralNetworkLearnerAO>
{

    @Override
    public void execute(NeuralNetworkLearnerAO op, TransformationConfiguration config) throws RuleException
    {
        INeuralNetwork<ITimeInterval> network = 
                NeuralNetworkFactory.getInstance().create(op.getFramework());
        IStrategy<ITimeInterval> strategy =
                StrategyFactory.getInstance().create(op.getStrategy(), op.getStrategyConfiguration());
        try
        {
            network.init(op.getInputNeurons(), 
                         op.getOutputNeurons(), 
                         op.getType(), 
                         op.getInputSchema(0), 
                         op.getClassAttribute(), 
                         op.getNominals(),
                         op.getMLTask());
            network.setOptions(op.getNetworkConfiguration(), op.getHiddenLayers());
        } catch(Exception e) { throw new RuleException(e); }
        
        AbstractPipe<Tuple<ITimeInterval>, Tuple<ITimeInterval>> po = 
                new NeuralNetworkLearnerPO<ITimeInterval>(network, strategy);
        po.setLogicalOperator(op);
        defaultExecute(op, po, config, true, true);
    }

    @Override
    public boolean isExecutable(NeuralNetworkLearnerAO operator, TransformationConfiguration config)
    { return operator.isAllPhysicalInputSet(); }
    @Override
    public IRuleFlowGroup getRuleFlowGroup() 
    { return TransformRuleFlowGroup.TRANSFORMATION; }
    @Override
    public String getName() 
    { return "NeuralNetworkLearnAO -> NeuralNetworkLearnPO"; }
}
