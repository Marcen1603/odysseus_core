package de.uniol.inf.is.odysseus.neuralnetworks.networks;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class NeuralNetworkFactory<T extends ITimeInterval>
{

    private static NeuralNetworkFactory<? extends ITimeInterval> instance = null;
    
    private List<INeuralNetwork<T>> networks = new ArrayList<>();
    
    public static synchronized <T extends ITimeInterval> NeuralNetworkFactory<T> getInstance()
    {
        if(instance == null)
        {
            instance = new NeuralNetworkFactory<T>();
        }
        @SuppressWarnings("unchecked")
        NeuralNetworkFactory<T> cur = (NeuralNetworkFactory<T>) instance;
        return cur;
    }
 
    public INeuralNetwork<T> create(String name)
    {
        for(INeuralNetwork<T> net : this.networks){
            if(net.getName().equalsIgnoreCase(name)) {
                return net.createInstance();
            }
        }
        return null;
    }

    public void addNeuralNetwork(INeuralNetwork<T> network) 
    { 
        this.networks.add(network); 
    }

    public void removeNeuralNetwork(INeuralNetwork<ITimeInterval> neuralnetwork)
    {
        this.networks.remove(neuralnetwork);
    }
    
}
