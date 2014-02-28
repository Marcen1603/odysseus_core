/**
 * 
 */
package de.uniol.inf.is.odysseus.generator.generic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.generator.AbstractDataGenerator;
import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.error.NoError;
import de.uniol.inf.is.odysseus.generator.valuegenerator.IValueGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class GenericProvider extends AbstractDataGenerator {
    private final Map<String, IValueGenerator> generators = new HashMap<String, IValueGenerator>();
    private String schemaFile;
    private long frequency;

    /**
     * 
     */
    public GenericProvider(String schemaFile, long frequency) {
        this.schemaFile = schemaFile;
        this.frequency = frequency;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized List<DataTuple> next() {
        DataTuple tuple = new DataTuple();
        if (generators.size() > 0) {
            for (String attribute : generators.keySet()) {
                tuple.addDouble(this.generators.get(attribute).nextValue());
            }
            try {
                if (this.frequency < 1000l) {
                    Thread.sleep(1000 / this.frequency);
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<DataTuple> list = new ArrayList<DataTuple>();
            list.add(tuple);
            return list;
        }
        else {
            throw new IllegalArgumentException("Empty generator list");
        }
    }

    @Override
    public void process_init() {
        // String entry = "Time;WeibullDistribution;1;1";
        URL file;
        if (Activator.getContext() != null) {
            file = Activator.getContext().getBundle().getEntry(schemaFile);
        }
        else {
            file = GenericProvider.class.getResource(schemaFile);
        }
        if (file == null) {
            try {
                File schemaPath = new File(schemaFile);
                if (!schemaPath.exists()) {
                    schemaPath = new File(System.getProperty("user.home") + File.separator + schemaFile);
                }
                file = schemaPath.toURI().toURL();
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        if (file != null) {
            try {
                StringBuilder config = new StringBuilder();
                final BufferedReader reader = new BufferedReader(new InputStreamReader(file.openConnection().getInputStream()));
                String entry;
                while ((entry = reader.readLine()) != null) {
                    String[] attributeParameter = entry.split(" ");

                    if (attributeParameter.length >= 2) {
                        try {
                            IValueGenerator generator = null;
                            // Class<?> generatorClass =
                            // Class.forName(attributeParameter[1]
                            // + "Generator");
                            Class<?> generatorClass = Activator.getGeneratorClass((attributeParameter[1] + "Generator").toUpperCase());

                            Constructor<?>[] constructors = generatorClass.getDeclaredConstructors();
                            for (Constructor<?> constructor : constructors) {
                                Class<?>[] params = constructor.getParameterTypes();
                                // First parameter of constructor is the error
                                // model
                                if (params.length == attributeParameter.length - 2 + 1) {
                                    Object[] args = new Object[params.length];
                                    args[0] = new NoError();
                                    for (int i = 2; i < attributeParameter.length; i++) {
                                        if (params[i - 1] == boolean.class) {
                                            args[i - 1] = Boolean.parseBoolean(attributeParameter[i]);
                                        }
                                        else if (params[i - 1] == byte.class) {
                                            args[i - 1] = Byte.parseByte(attributeParameter[i]);
                                        }
                                        else if (params[i - 1] == short.class) {
                                            args[i - 1] = Short.parseShort(attributeParameter[i]);
                                        }
                                        else if (params[i - 1] == int.class) {
                                            args[i - 1] = Integer.parseInt(attributeParameter[i]);
                                        }
                                        else if (params[i - 1] == float.class) {
                                            args[i - 1] = Float.parseFloat(attributeParameter[i]);
                                        }
                                        else if (params[i - 1] == double.class) {
                                            args[i - 1] = Double.parseDouble(attributeParameter[i]);
                                        }
                                    }
                                    generator = (IValueGenerator) constructor.newInstance(args);
                                    config.append(String.format("Attribute %s initialized with %s(%s)\n", attributeParameter[0], generatorClass.getSimpleName(), Arrays.toString(args)));
                                    break;
                                }
                            }
                            if (generator != null) {
                                generator.init();
                                this.generators.put(attributeParameter[0], generator);
                            }
                            else {
                                throw new IllegalArgumentException("No value generator found");
                            }
                        }
                        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                StringBuilder schema = new StringBuilder();
                for (String attribute : this.generators.keySet()) {
                    if (schema.length() != 0) {
                        schema.append(",");
                    }
                    schema.append(attribute);
                    schema.append(" DOUBLE");
                }
                schema.insert(0, "Use the following statement to access the stream\nATTACH STREAM generator (");
                schema.append(")\nWRAPPER 'GenericPush'\nPROTOCOL 'SizeByteBuffer'\nTRANSPORT 'NonBlockingTcp'\nDATAHANDLER 'Tuple'\nOPTIONS ( 'port' '54325', 'host' 'localhost', 'ByteOrder' 'Little_Endian')");
                System.out.println(config);
                System.out.println(schema.toString());
            }
            catch (SecurityException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void close() {

    }

    @Override
    public GenericProvider newCleanInstance() {
        return new GenericProvider("schema.txt", 10l);
    }

    public static void main(final String[] args) {
        Activator activator = new Activator();
        try {
            activator.start(null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
