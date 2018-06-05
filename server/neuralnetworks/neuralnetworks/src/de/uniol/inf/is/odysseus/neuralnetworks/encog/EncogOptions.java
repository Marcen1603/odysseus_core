package de.uniol.inf.is.odysseus.neuralnetworks.encog;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class EncogOptions
{

    public static EncogOptions getInstance()
    {
        if(instance == null)
        {
            instance = new EncogOptions();
        }
        return instance;
    }
    
    private static EncogOptions instance = null;
    private static Map<String, String>   defaults = createDefaultMap(); 
    private static Map<String, String[]> pars     = createOptionsMap();
    
    private static Map<String, String> createDefaultMap()
    {
        HashMap<String, String> defaults = new HashMap<>();
        
        defaults.put("activation", "sig");
        defaults.put("neighborhood", "rbf");
        defaults.put("training", "backprop for regression / classification, som cluster training for clustering");
        defaults.put("multithreaded", "true");
        defaults.put("learnrate", "0.1");
        defaults.put("bias", "1.0");
        defaults.put("delta", "0.1");
        defaults.put("k-folds", "3");
        defaults.put("crossvalidation", "true");
        defaults.put("low", "0.0");
        defaults.put("high", "1.0");
        defaults.put("iterations", "1000");
        defaults.put("error", "0.01");
        defaults.put("x_dimension", "10");
        defaults.put("y_dimension", "10");
        defaults.put("startRate", "0.8");
        defaults.put("endRate", "0.003");
        defaults.put("startRadius", "30");
        defaults.put("endRadius", "5");
        
        return defaults;
    }

    /*
     * A key corresponds to an option. The value is a String[] 
     * that contains the possible values separated by \n. For a
     * value the are three characteristics:
     * the name, the default value and a description.
     */
    private static Map<String, String[]> createOptionsMap()
    {
        HashMap<String, String[]> var = new HashMap<>();
        
        var.put("activation", new String[]{ "step",
                                            "low=0.0, center=0.0, high=1.0",
                                            "A step function with controllable low, center and high.",
                                            "\n",
                                            "tanh",
                                            "low=-1.0, high=1.0",
                                            "A tangens function.",
                                            "\n",
                                            "log",
                                            "-",
                                            "A logarithmic function.",
                                            "\n",
                                            "bipolar",
                                            "low=-1.0, high=1.0",
                                            "A bipolar function.",
                                            "\n",
                                            "comp",
                                            "low=-1.0, high=1.0",
                                            "A competitive function.",
                                            "\n",
                                            "gauss",
                                            "low=0.0, high=1.0",
                                            "A guassion function.",
                                            "\n",
                                            "ramp",
                                            "low=2.0, high=3.0",
                                            "A ramp function that can be controlled by low, high, lowThreshold, "
                                            + "highTrehsold, lowOutput and highOutput.",
                                            "\n",
                                            "sin",
                                            "low=-1.0, high=1.0",
                                            "A sin function.",
                                            "\n",
                                            "softmax",
                                            "low=0.0, high=1.0",
                                            "A softmax function.",
                                            "\n"
        });
        var.put("neighborhood", new String[]{"bubble",
                                             "-",
                                             "A bubble neighborhood function that will return 1.0 (full update) "
                                             + "for any neuron that is plus or minus the width distance from the winning "
                                             + "neuron",
                                             "\n",
                                             "rbf1d",
                                             "-",
                                             "Construct a 1-dimensonal neigborhood function.",
                                             "\n",
                                             "single",
                                             "-",
                                             "A very simple neighborhood function that will return 1.0 (full effect) for "
                                             + "the winning neuron, and 0.0 (no change) for everything else.",
                                             "\n",
                                             "rbf",
                                             "-",
                                             "Construct a multi-dimensional neighborhood function with size as dimension "
                                             + "space.",
                                             "\n",
                                             
        });
        var.put("multithreaded", new String[]{"", getDefaultValue("multithreaded"), "Enable/disable multithreading for "
                + "training phase.", "\n"});
        var.put("learnrate", new String[]{"", getDefaultValue("learnrate"), "", "\n"});
        var.put("bias", new String[]{"", getDefaultValue("bias"), "", "\n"});
        var.put("delta", new String[]{"", getDefaultValue("delta"), "", "\n"});
        var.put("k-folds", new String[]{"", getDefaultValue("k-folds"), "", "\n"});
        var.put("crossvalidation", new String[]{"", getDefaultValue("crossvalidation"), "", "\n"});
        var.put("training", new String[]{"backprop", "-", "Standard backpropagation algorithm for feedforward network.", "\n",
                                         "manhatten","-","Manhatten algorithm for feedforward network.","\n",
                                         "rprop","-","Manhatten algorithm for feedforward network.","\n"
        });
        var.put("error", new String[]{"", getDefaultValue("error"), "Supposed error for the network.", "\n"});
        var.put("iterations", new String[]{"", getDefaultValue("iterations"), "Maximal training iterations.", "\n"});
        var.put("y_dimension", new String[]{"", getDefaultValue("x_dimension"),"","\n"});
        var.put("x_dimension", new String[]{"", getDefaultValue("x_dimension"),"","\n"});
        var.put("startRate", new String[]{"", getDefaultValue("startRate"),"", "\n"});
        var.put("endRate", new String[]{"", getDefaultValue("endRate"),"", "\n"});
        var.put("startRadius", new String[]{"", getDefaultValue("startRadius"),"", "\n"});
        var.put("endRadius", new String[]{"", getDefaultValue("endRadius"),"", "\n"});
        return var;
    }
    
    public static String getDefaultValue(String key)
    {
        if(defaults.containsKey(key))
            return defaults.get(key);
        return "";
    }
    
    public String toString(String key)
    {
        StringBuilder builder = new StringBuilder();
        String[] values       = pars.get(key);
        builder.append(key+":");
        if(values != null)
        {
            builder.append(toString(values));
        }
        return builder.toString();
    }

    public String toString(String[] array)
    {
        StringBuilder builder = new StringBuilder();
        String[] v = {"name:\t\t", "default:\t", "description:\t"};
        int ctr = 0;
        if(array.length > 4)
        {
            builder.append("\nvalues:\n");
        }
        for(String s : array)
        {
            if(ctr == 4) ctr = 0;
            if(s != "\n" )
                if(s != "")
                    builder.append("\t"+v[ctr]+s+"\n");
                else
                    builder.append("\t\n");
            else
                builder.append("\t"+s);
            ctr++;
        }
        return builder.toString();
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        for(Entry<String, String[]> s : pars.entrySet())
        {
            builder.append("\n");
            builder.append("parameter:\t " +s.getKey() + toString(s.getValue()));
        }
        return builder.toString();
    }
    
    public String toString(Map<String, String> values)
    {
        StringBuilder builder = new StringBuilder();
        for(Entry<String, String> s : values.entrySet())
        {
            builder.append("\n");
            builder.append(s.getKey() + ": \n" + s.getValue());
        }
        return builder.toString();
    }
    
    public Map<String, String[]> getOptions() { return pars; }

    public static void main(String[] args)
    {
        EncogOptions obj = new EncogOptions();
        System.out.println(obj.toString());
    }

    public boolean contains(String name) { return pars.containsKey(name.toLowerCase()); }
    
}
