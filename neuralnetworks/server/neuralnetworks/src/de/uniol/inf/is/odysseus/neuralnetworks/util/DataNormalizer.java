package de.uniol.inf.is.odysseus.neuralnetworks.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.linear.MatrixUtils;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

public final class DataNormalizer
{
    
    private static DataNormalizer instance = null;
    private static final String UNKNOWN_CLAZZ = "UNKNOWN_CLASS";
    
    public class NormalizationData 
    {
        final double        N_HIGH;
        final double        N_LOW;
        final double        ACTUAL_HIGH;
        final double        ACTUAL_LOW;
        final SDFAttribute  CLAZZ;
        
        public NormalizationData(final double nHigh,
                                 final double nLow,
                                 final double actualHigh,
                                 final double actualLow,
                                 final SDFAttribute clazz)
        {
             N_HIGH      = nHigh;
             N_LOW       = nLow;
             ACTUAL_HIGH = actualHigh;
             ACTUAL_LOW  = actualLow;
             CLAZZ       = clazz;
        }
    }
    
    public static DataNormalizer getInstance() 
    {
        if(instance == null) return (instance = new DataNormalizer());
        else                 return instance;
    }
    
    public double normalise(final double val, 
                            final double actualHigh, 
                            final double actualLow, 
                            final double normalizedHigh, 
                            final double normalizedLow)
    {
        if( val>actualHigh )    return normalizedHigh;
        else if( val<actualLow )return normalizedLow;
        else                    
            return ((val - actualLow) / (actualHigh - actualLow)) * 
                   (normalizedHigh - normalizedLow) + normalizedLow;
    }

    public double normalise(final double val, final NormalizationData nData)
    {
        double actualHigh     = nData.ACTUAL_HIGH;
        double actualLow      = nData.ACTUAL_LOW;
        double normalizedHigh = nData.N_HIGH;
        double normalizedLow  = nData.N_LOW;
        if( val>actualHigh )    return normalizedHigh;
        else if( val<actualLow )return normalizedLow;
        else                    
        return ((val - actualLow) / (actualHigh - actualLow)) * 
           (normalizedHigh - normalizedLow) + normalizedLow;
    }
    
    public double[] normalise(final double[] val, final NormalizationData nData)
    {
        double actualHigh     = nData.ACTUAL_HIGH;
        double actualLow      = nData.ACTUAL_LOW;
        double normalizedHigh = nData.N_HIGH;
        double normalizedLow  = nData.N_LOW;
        double[] result = new double[val.length];
        for(int i  = 0; i < val.length; i++)
        {
            result[i] = normalise(val[i], 
                            actualHigh, 
                            actualLow, 
                            normalizedHigh, 
                            normalizedLow);
        }
        return result;
    }
    
    public double[] normalise(final double[] val, 
                              final double actualHigh, 
                              final double actualLow, 
                              final double normalizedHigh, 
                              final double normalizedLow)
    {
        double[] result = new double[val.length];
        for(int i  = 0; i < val.length; i++)
        {
            result[i] = normalise(val[i], 
                                  actualHigh, 
                                  actualLow, 
                                  normalizedHigh, 
                                  normalizedLow);
        }
        return result;
    }
    
    public double[][] normalise(final double[][] val, final NormalizationData nData)
    {
        double actualHigh     = nData.ACTUAL_HIGH;
        double actualLow      = nData.ACTUAL_LOW;
        double normalizedHigh = nData.N_HIGH;
        double normalizedLow  = nData.N_LOW;
        double[][] transpose  = MatrixUtils.createRealMatrix(val).transpose().getData();
        double[][] result = new double[transpose.length][transpose[0].length];
        for(int i = 0; i < transpose.length; i++)
        {
            result[i] = Arrays.copyOf(normalise(transpose[i], 
                              actualHigh, 
                              actualLow, 
                              normalizedHigh, 
                              normalizedLow), 
                              transpose[i].length);
        }
        return MatrixUtils.createRealMatrix(result).transpose().getData();
    }
    
    public double[][] normalise(final double[][] val, 
                                final double actualHigh, 
                                final double actualLow, 
                                final double normalizedHigh, 
                                final double normalizedLow)
    {
        double[][] transpose = MatrixUtils.createRealMatrix(val).transpose().getData();
        double[][] result = new double[transpose.length][transpose[0].length];
        for(int i = 0; i < transpose.length; i++)
        {
            result[i] = Arrays.copyOf(normalise(transpose[i], 
                                      actualHigh, 
                                      actualLow, 
                                      normalizedHigh, 
                                      normalizedLow), 
                                      transpose[i].length);
        }
        return MatrixUtils.createRealMatrix(result).transpose().getData();
    }
    
    public double denormalise(final double val, 
                                    final double actualHigh, 
                                    final double actualLow, 
                                    final double normalizedHigh, 
                                    final double normalizedLow)
    {      
        final double result = ((actualLow - actualHigh) * val
                - normalizedHigh * actualLow + actualHigh
                * normalizedLow)
                / (normalizedLow - normalizedHigh);
        return result;
    }

    public double denormalise(final double val, final NormalizationData nData)
    {
        double actualHigh     = nData.ACTUAL_HIGH;
        double actualLow      = nData.ACTUAL_LOW;
        double normalizedHigh = nData.N_HIGH;
        double normalizedLow  = nData.N_LOW;
        final double result   = ((actualLow - actualHigh) * val
        - normalizedHigh * actualLow + actualHigh
        * normalizedLow)
        / (normalizedLow - normalizedHigh);
        return result;
    }
    
    public double[] denormalise(final double val[], 
            final double actualHigh, 
            final double actualLow, 
            final double normalizedHigh, 
            final double normalizedLow)
    {
        
        double[] result = new double[val.length];
        for(int i  = 0; i < val.length; i++)
        {
            result[i] = denormalise(val[i], 
                                    actualHigh, 
                                    actualLow, 
                                    normalizedHigh, 
                                    normalizedLow);
        }
        return result;
    }
    
    public double[] denormalise(final double val[], final NormalizationData nData)
    {
        double actualHigh     = nData.ACTUAL_HIGH;
        double actualLow      = nData.ACTUAL_LOW;
        double normalizedHigh = nData.N_HIGH;
        double normalizedLow  = nData.N_LOW;
        double[] result = new double[val.length];
        for(int i  = 0; i < val.length; i++)
        {
            result[i] = denormalise(val[i], 
                                  actualHigh, 
                                  actualLow, 
                                  normalizedHigh, 
                                  normalizedLow);
        }
        return result;
    }
     
    public double[][] denormalise(final double[][] val, 
            final double actualHigh, 
            final double actualLow, 
            final double normalizedHigh, 
            final double normalizedLow)
    {
        
        double[][] transpose = MatrixUtils.createRealMatrix(val).transpose().getData();
        double[][] result = new double[transpose.length][transpose[0].length];
        for(int i = 0; i < transpose.length; i++)
        {
            result[i] = Arrays.copyOf(denormalise(transpose[i], 
                                      actualHigh, 
                                      actualLow, 
                                      normalizedHigh, 
                                      normalizedLow), 
                                      transpose[i].length);
        }
        return MatrixUtils.createRealMatrix(result).transpose().getData();
    }
    
    public double[][] denormalise(final double[][] val, final NormalizationData nData)
    {
        double actualHigh     = nData.ACTUAL_HIGH;
        double actualLow      = nData.ACTUAL_LOW;
        double normalizedHigh = nData.N_HIGH;
        double normalizedLow  = nData.N_LOW;
        double[][] transpose = MatrixUtils.createRealMatrix(val).transpose().getData();
        double[][] result = new double[transpose.length][transpose[0].length];
        for(int i = 0; i < transpose.length; i++)
        {
            result[i] = Arrays.copyOf(denormalise(transpose[i], 
                                      actualHigh, 
                                      actualLow, 
                                      normalizedHigh, 
                                      normalizedLow), 
                                      transpose[i].length);
        }
        return MatrixUtils.createRealMatrix(result).transpose().getData();
    }
    
    public Map<String, Double[]> encodeNominals(Map<SDFAttribute, List<String>> nominals, 
                                               SDFAttribute clazz, 
                                               double low, 
                                               double high)
    {
        Map<String, Double[]> eNominals = new HashMap<>();
        
        List<String> lNominals = nominals.get(clazz);
        int cNominal           = lNominals.size();
        int i                  = 0;
        for(String s : lNominals)
        {
            if(s != null && !s.equals(""))
            {
                Double[] encoding = new Double[cNominal];
                for(int j = 0; j < cNominal; j++)
                {
                    if(j == i) encoding[j] = high;
                    else       encoding[j] = low;
                }
                eNominals.put(s, encoding);
                i++;
            }
        }
        Double[] d = new Double[cNominal];
        for(int j = 0; j < cNominal; j++)
        {
            d[j] = 0.0;
        }
        eNominals.put(UNKNOWN_CLAZZ, d);
        return eNominals;
    }
  
    public Map<String, Double[]> encodeNominals(Map<SDFAttribute, List<String>> nominals, final NormalizationData nData)
    {
        SDFAttribute clazz = nData.CLAZZ;
        double nHigh       = nData.N_HIGH;
        double nLow        = nData.N_LOW;
        Map<String, Double[]> eNominals = new HashMap<>();
        
        List<String> lNominals = nominals.get(clazz);
        int cNominal           = lNominals.size();
        int i                  = 0;
        for(String s : lNominals)
        {
            if(s != null && !s.equals(""))
            {
                Double[] encoding = new Double[cNominal];
                for(int j = 0; j < cNominal; j++)
                {
                    if(j == i) encoding[j] = nHigh;
                    else       encoding[j] = nLow;
                }
                eNominals.put(s, encoding);
                i++;
            }
        }
        Double[] d = new Double[cNominal];
        for(int j = 0; j < cNominal; j++)
        {
            d[j] = 0.0;
        }
        eNominals.put(UNKNOWN_CLAZZ, d);
        return eNominals;
    }   
    
    // corrected:
    public String decodeNominals(final Map<String, Double[]> nominals,
                                 final Double[] val, 
                                 final double normalizedHigh, 
                                 final double normalizedLow)
    {
        String nominal = UNKNOWN_CLAZZ;
        for(Entry<String, Double[]> entry : nominals.entrySet())
        {
            Double[] a = entry.getValue();
            boolean b  = true;
            double[] dVal = new double[val.length];
            for(int i = 0; i < val.length; i++) { dVal[i] = val[i]; }
            if(a.length == val.length)
            {
//                double[] denormalised = denormalise(dVal, 
//                                                    actualHigh, 
//                                                    actualLow, 
//                                                    normalizedHigh, 
//                                                    normalizedLow); 
                int maxIndex = 0;
                for (int i = 1; i < val.length; i++)
                {
                   double newnumber = val[i];
                   if ((newnumber > val[maxIndex])){ maxIndex = i; }
                }
                for(int i = 0; i < val.length; i++)
                {
                    if(i != maxIndex) val[i] = normalizedLow;
                    else              val[i] = normalizedHigh;
                }
                for(int i = 0; i < a.length; i++)
                {
                    if(a[i] != val[i]) b = false;
                }
                if(b) nominal = entry.getKey();
            }
            else { return null; }
        }
        return nominal;
    }
  
    // corrected:
    public String decodeNominals(final Map<String, Double[]> nominals, final Double[] val, final NormalizationData nData)
    {
        final double normalizedHigh = nData.N_HIGH;
        final double normalizedLow  = nData.N_LOW;
        String nominal              = UNKNOWN_CLAZZ;
        for(Entry<String, Double[]> entry : nominals.entrySet())
        {
            Double[] a = entry.getValue();
            boolean b  = true;
            double[] dVal = new double[val.length];
            for(int i = 0; i < val.length; i++) { dVal[i] = val[i]; }
            if(a.length == val.length)
            {
//                double[] denormalised = denormalise(dVal, 
//                                               actualHigh, 
//                                               actualLow, 
//                                               normalizedHigh, 
//                                               normalizedLow); 
                int maxIndex = 0;
                for (int i = 1; i < val.length; i++)
                {
                   double newnumber = val[i];
                   if ((newnumber > val[maxIndex])){ maxIndex = i; }
                }
                for(int i = 0; i < val.length; i++)
                {
                    if(i != maxIndex) val[i] = normalizedLow;
                    else              val[i] = normalizedHigh;
                }
                for(int i = 0; i < a.length; i++)
                {
                    if(a[i] != val[i]) b = false;
                }
                if(b) nominal = entry.getKey();
            }
            else { return null; }
        }
        return nominal;
    }

    // corrected:
    public String decodeNominals(final Map<String, Double[]> nominals,
                                 final double[] val, 
                                 final double normalizedHigh, 
                                 final double normalizedLow)
    {
        String nominal = UNKNOWN_CLAZZ;
        for(Entry<String, Double[]> entry : nominals.entrySet())
        {
            Double[] a = entry.getValue();
            boolean b  = true;
            if(a.length == val.length)
            {
//                double[] denormalised = denormalise(val, 
//                                                    actualHigh, 
//                                                    actualLow, 
//                                                    normalizedHigh, 
//                                                    normalizedLow); 
                int maxIndex = 0;
                for (int i = 1; i < val.length; i++)
                {
                   double newnumber = val[i];
                   if ((newnumber > val[maxIndex])){ maxIndex = i; }
                }
                for(int i = 0; i < val.length; i++)
                {
                    if(i != maxIndex) val[i] = normalizedLow;
                    else              val[i] = normalizedHigh;
                }
                for(int i = 0; i < a.length; i++)
                {
                    if(a[i] != val[i]) b = false;
                }
                if(b) nominal = entry.getKey();
            }
            else { return null; }
        }
        return nominal;
    }
    
    // corrected:
    public String decodeNominals(final Map<String, Double[]> nominals, double[] val, final NormalizationData nData)
    {
        final double normalizedHigh = nData.N_HIGH;
        final double normalizedLow  = nData.N_LOW;
        String nominal              = UNKNOWN_CLAZZ;
        for(Entry<String, Double[]> entry : nominals.entrySet())
        {
            Double[] a = entry.getValue();
            boolean b  = true;
            if(a.length == val.length)
            {
//            double[] denormalised = denormalise(val, 
//                                           actualHigh, 
//                                           actualLow, 
//                                           normalizedHigh, 
//                                           normalizedLow); 
                int maxIndex = 0;
                for (int i = 1; i < val.length; i++)
                {
                   double newnumber = val[i];
                   if ((newnumber > val[maxIndex])){ maxIndex = i; }
                }
                for(int i = 0; i < val.length; i++)
                {
                    if(i != maxIndex) val[i] = normalizedLow;
                    else              val[i] = normalizedHigh;
                }
                for(int i = 0; i < a.length; i++)
                {
                    if(a[i] != val[i]) b = false;
                }
            if(b) nominal = entry.getKey();
            }
            else { return null; }
        }
        return nominal;
    }
}    