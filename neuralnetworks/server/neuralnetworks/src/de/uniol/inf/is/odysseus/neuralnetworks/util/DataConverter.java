package de.uniol.inf.is.odysseus.neuralnetworks.util;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

@SuppressWarnings("rawtypes")
public final class DataConverter<M extends ITimeInterval>
{
   
    private static DataConverter instance = null;
    
    public static DataConverter getInstance() 
    {
        if(instance == null) return (instance = new DataConverter());
        else                 return  instance;
    }
    
    /**
     * Converts given tuples to input data for a neural network. It returns
     * an object array that contains double[][] input data, double[][] ideal data,
     * double actualHigh and double actualLow in this order.
     * 
     * @param data
     * @param schema
     * @param clazz
     * @param clazzCount
     * @param nominals
     * @return
     */
    public Object[] convert(List<Tuple<M>> data, 
                            SDFSchema schema, 
                            SDFAttribute clazz, 
                            int clazzCount,
                            Map<String, Double[]> nominals)
    {

        Object[] result  = new Object[5];
        int indexOfClassAttribute =  -1;
        if(schema.contains(clazz)) indexOfClassAttribute = schema.indexOf(clazz);
        double[][] tData = new double[data.size()][data.get(0).size() - 1];
        double[][] iData = new double[data.size()][clazzCount];
        boolean b = false;
        boolean c = false;
        double actualHigh = 0.0;
        double actualLow  = 0.0;
        if (data.get(0).size() - 1 == indexOfClassAttribute) { b = true; }
        for (int i = 0; i < data.size(); i++)
        {
            c = false;
            Tuple<M> tuple = data.get(i);
            int j = 0;
            int l = 0;
            while (j != tuple.size() - 1)
            {
                Object attr = tuple.getAttribute(l);
                if (j != indexOfClassAttribute || (j == indexOfClassAttribute && c))
                {
                    if (attr instanceof Double)
                    {
                        double val  = ((Number) attr).doubleValue();
                        tData[i][j] = val;
                        actualHigh  = Math.max(actualHigh, val);
                        actualLow   = Math.min(actualLow, val);
                    }
                }
                else if (j == indexOfClassAttribute && !c)
                {
                    if (attr instanceof Double)
                    {
                        double val  = ((Number) attr).doubleValue();
                        iData[i][0] = val;
                        actualHigh  = Math.max(actualHigh, val);
                        actualLow   = Math.min(actualLow, val);
                    }
                    else if (attr instanceof String)
                    {
                        Double[] e = nominals.get(attr.toString());
                        if (e != null)
                        {
                            for (int k = 0; k < e.length; k++) 
                            { 
                                iData[i][k] = e[k]; 
                                actualHigh  = Math.max(actualHigh, e[k]);
                                actualLow   = Math.min(actualLow,  e[k]);
                             }
                        }
                    }
                    c = true;
                    l = j;
                    j--;
                }
                l++;
                j++;
                if (b && j == tuple.size() - 2)
                {
                    Object attr2 = tuple.getAttribute(data.get(0).size() - 1);
                    if (attr2 instanceof Double)
                    {
                        double val  = ((Number) attr2).doubleValue();
                        iData[i][0] = val;
                        actualHigh  = Math.max(actualHigh, val);
                        actualLow   = Math.min(actualLow, val);
                    }
                    else if (attr2 instanceof String)
                    {
                        Double[] e = nominals.get(attr2.toString());
                        if (e != null)
                        {
                            for (int k = 0; k < e.length; k++)
                            {
                                iData[i][k] = e[k];
                                actualHigh  = Math.max(actualHigh, e[k]);
                                actualLow   = Math.min(actualLow,  e[k]);
                            }
                        }
                    }
                }
            }
        }
        result[0] = tData;
        result[1] = iData;
        result[2] = actualHigh;
        result[3] = actualLow;
        return result;
    }
    
    public Object[] convertWithoutIdealData(List<Tuple<M>> data, 
                                                   SDFSchema schema, 
                                                   SDFAttribute clazz, 
                                                   int clazzCount,
                                                   Map<String, Double[]> nominals)
    {
        Object[] result  = new Object[3];
        double[][] tData = new double[data.size()][data.get(0).size()];
        double actualHigh = 0.0;
        double actualLow  = 0.0;
//        if (data.get(0).size() - 1 == indexOfClassAttribute) { b = true; }
        for (int i = 0; i < data.size(); i++)
        {
            Tuple<M> tuple = data.get(i);
            int j = 0;
            int l = 0;
            while (j != tuple.size() - 1)
            {
                Object attr = tuple.getAttribute(l);
                if (attr instanceof Double)
                {
                    double val  = ((Number) attr).doubleValue();
                    tData[i][j] = val;
                    actualHigh  = Math.max(actualHigh, val);
                    actualLow   = Math.min(actualLow, val);
                }
                l++;
                j++;
            }
        }
        result[0] = tData;
        result[1] = actualHigh;
        result[2] = actualLow;
        return result;
    }
    
}