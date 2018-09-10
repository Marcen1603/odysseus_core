package de.uniol.inf.is.odysseus.neuralnetworks;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datatype.IDatatypeProvider;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class NeuralNetworkDatatype implements IDatatypeProvider
{
    public static final SDFDatatype NEURALNETWORK = new SDFDatatype("NeuralNetwork");

    @Override
    public List<SDFDatatype> getDatatypes()
    {   
        List<SDFDatatype> l = new ArrayList<>();
        l.add(NEURALNETWORK);
        return l;
    }
}
