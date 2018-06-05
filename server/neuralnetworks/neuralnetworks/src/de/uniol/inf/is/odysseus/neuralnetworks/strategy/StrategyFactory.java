package de.uniol.inf.is.odysseus.neuralnetworks.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class StrategyFactory<T extends ITimeInterval>
{

    private static StrategyFactory<? extends ITimeInterval> instance = null;
    
    @SuppressWarnings("rawtypes")
    private List<IStrategy> strategies = new ArrayList<>();
    
    public static synchronized <T extends ITimeInterval> StrategyFactory<T> getInstance()
    {
        if(instance == null)
        {
            instance = new StrategyFactory<T>();
        }
        @SuppressWarnings("unchecked")
        StrategyFactory<T> cur = (StrategyFactory<T>) instance;
        return cur;
    }
    
    @SuppressWarnings("unchecked")
    public IStrategy<T> create(String name, Map<String, String> options)
    {
        for(IStrategy<T> net : strategies)
        {
            if(net.getName().equalsIgnoreCase(name)) 
            {
                return net.createInstance(options);
            }
        }
        return null;
    }

    public void addStrategy(IStrategy<ITimeInterval> strategy)
    {
        strategies.add(strategy);
    }
    
    public void removeStrategy(IStrategy<ITimeInterval> defaultForgetting)
    {
        strategies.remove(defaultForgetting);
    }
    
}
