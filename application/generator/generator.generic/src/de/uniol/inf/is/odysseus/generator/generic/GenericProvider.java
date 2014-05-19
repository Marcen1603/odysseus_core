/**
 * 
 */
package de.uniol.inf.is.odysseus.generator.generic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.generator.AbstractDataGenerator;
import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamServer;
import de.uniol.inf.is.odysseus.generator.error.NoError;
import de.uniol.inf.is.odysseus.generator.valuegenerator.IMultiValueGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.ISingleValueGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.IValueGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class GenericProvider extends AbstractDataGenerator {
    private final Map<String, IValueGenerator> generators = new LinkedHashMap<String, IValueGenerator>();
    private final String schemaFile;
    private final String out;
    private final long frequency;
    private final BufferedWriter writer;

    /**
     * 
     */
    public GenericProvider(final String schemaFile, final long frequency) {
        this.schemaFile = schemaFile;
        this.frequency = frequency;
        this.out = null;
        this.writer = null;
    }

    /**
     * @param schemaFile2
     * @param frequency2
     * @param outFile
     * @throws IOException
     */
    public GenericProvider(final String schemaFile, final long frequency, final String out) throws IOException {
        this.schemaFile = schemaFile;
        this.frequency = frequency;
        this.out = out;
        if (out != null) {
            this.writer = new BufferedWriter(new FileWriter(out));
        }
        else {
            writer = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized List<DataTuple> next() {
        final DataTuple tuple = new DataTuple();
        if (this.generators.size() > 0) {
            for (final String attribute : this.generators.keySet()) {
                if (this.generators.get(attribute) instanceof IMultiValueGenerator) {
                    final double[] value = ((IMultiValueGenerator) this.generators.get(attribute)).nextValue();
                    for (final double element : value) {
                        tuple.addDouble(element);
                    }
                }
                else if (this.generators.get(attribute) instanceof ISingleValueGenerator) {
                    tuple.addDouble(((ISingleValueGenerator) this.generators.get(attribute)).nextValue());
                }
            }
            if (this.writer != null) {
                final StringBuilder sb = new StringBuilder();
                for (final Object attribute : tuple.getAttributes()) {
                    if (sb.length() > 0) {
                        sb.append(",");
                    }
                    sb.append(attribute);
                }
                try {
                    this.writer.write(sb.toString());
                    this.writer.newLine();
                    this.writer.flush();
                }
                catch (final IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                if (this.frequency < 1000l) {
                    Thread.sleep(1000 / this.frequency);
                }
            }
            catch (final InterruptedException e) {
                e.printStackTrace();
            }
            final List<DataTuple> list = new ArrayList<DataTuple>();
            list.add(tuple);
            return list;
        }
        throw new IllegalArgumentException("Empty generator list");
    }

    @Override
    public void process_init() {
        // String entry = "Time;WeibullDistribution;1;1";
        URL file;
        if (Activator.getContext() != null) {
            file = Activator.getContext().getBundle().getEntry(this.schemaFile);
        }
        else {
            file = GenericProvider.class.getResource(this.schemaFile);
        }
        if (file == null) {
            try {
                File schemaPath = new File(this.schemaFile);
                if (!schemaPath.exists()) {
                    schemaPath = new File(System.getProperty("user.home") + File.separator + this.schemaFile);
                }
                file = schemaPath.toURI().toURL();
            }
            catch (final MalformedURLException e) {
                e.printStackTrace();
            }
        }
        if (file != null) {
            try {
                final StringBuilder config = new StringBuilder();
                final BufferedReader reader = new BufferedReader(new InputStreamReader(file.openConnection().getInputStream()));
                String entry;
                while ((entry = reader.readLine()) != null) {
                    final String[] attributeParameter = entry.split(" ");

                    if (attributeParameter.length >= 2) {
                        try {
                            IValueGenerator generator = null;
                            // Class<?> generatorClass =
                            // Class.forName(attributeParameter[1]
                            // + "Generator");
                            final Class<?> generatorClass = Activator.getGeneratorClass((attributeParameter[1] + "Generator").toUpperCase());
                            final Constructor<?>[] constructors = generatorClass.getDeclaredConstructors();
                            for (final Constructor<?> constructor : constructors) {
                                final Class<?>[] params = constructor.getParameterTypes();
                                // First parameter of constructor is the error
                                // model
                                if (params.length == ((attributeParameter.length - 2) + 1)) {
                                    final Object[] args = new Object[params.length];
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
                                        else if (params[i - 1] == Double[].class) {
                                            final String[] column = attributeParameter[i].split(",");
                                            final Double[] value = new Double[column.length];
                                            for (int c = 0; c < column.length; c++) {
                                                value[c] = Double.parseDouble(column[c]);
                                            }
                                            args[i - 1] = value;
                                        }
                                        else if (params[i - 1] == Double[][].class) {
                                            final String[] row = attributeParameter[i].split(";");
                                            if (row.length > 0) {
                                                String[] column = row[0].split(",");
                                                final Double[][] value = new Double[row.length][column.length];
                                                for (int r = 0; r < row.length; r++) {
                                                    column = row[r].split(",");
                                                    for (int c = 0; c < column.length; c++) {
                                                        value[r][c] = Double.parseDouble(column[c]);
                                                    }
                                                }
                                                args[i - 1] = value;
                                            }
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
                final StringBuilder schema = new StringBuilder();
                for (final String attribute : this.generators.keySet()) {
                    if (schema.length() != 0) {
                        schema.append(",");
                    }
                    if (this.generators.get(attribute) instanceof IMultiValueGenerator) {
                        final IMultiValueGenerator generator = (IMultiValueGenerator) this.generators.get(attribute);
                        for (int i = 0; i < generator.dimension(); i++) {
                            if (i != 0) {
                                schema.append(",");
                            }
                            schema.append(attribute + i);
                            schema.append(" DOUBLE");
                        }
                    }
                    else if (this.generators.get(attribute) instanceof ISingleValueGenerator) {
                        schema.append(attribute);
                        schema.append(" DOUBLE");
                    }
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
        if (this.writer != null) {
            try {
                this.writer.flush();
                this.writer.close();
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public GenericProvider newCleanInstance() {
        try {
            return new GenericProvider(this.schemaFile, this.frequency, this.out);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return new GenericProvider(this.schemaFile, this.frequency);
    }

    public static void main(final String[] args) {
        final Map<String, String> options = new HashMap<>();
        options.put("p", "port");
        options.put("f", "freq");
        options.put("s", "schema");
        options.put("o", "out");
        int port = 54325;
        long frequency = 1000l;
        String schemaFile = "schema.txt";
        String out = null;
        for (int i = 0; i < args.length; i++) {
            switch (args[i].charAt(0)) {
                case '-':
                    String argstring = null;
                    if (args[i].charAt(1) == '-') {
                        argstring = args[i].toString();
                        argstring = argstring.substring(2, argstring.length());
                    }
                    else {
                        argstring = args[i].toString();
                        argstring = argstring.substring(1, argstring.length());
                        if (options.containsKey(argstring)) {
                            argstring = options.get(argstring);
                        }
                    }
                    if (argstring != null) {
                        if (argstring.equalsIgnoreCase("port")) {
                            port = Integer.parseInt(args[i + 1]);
                        }
                        else if (argstring.equalsIgnoreCase("freq")) {
                            frequency = Long.parseLong(args[i + 1]);
                        }
                        else if (argstring.equalsIgnoreCase("schema")) {
                            schemaFile = args[i + 1];
                        }
                        else if (argstring.equalsIgnoreCase("out")) {
                            out = args[i + 1];
                        }
                        else {
                            System.err.println("Unknown parameter " + argstring);
                            GenericProvider.help();
                            System.exit(1);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        if (out != null) {
            System.out.println("Generator started with -p " + port + " -f " + frequency + " -s " + schemaFile + " -o " + out);
        }
        else {
            System.out.println("Generator started with -p " + port + " -f " + frequency + " -s " + schemaFile);
        }
        try {
            StreamServer genericServer;
            if (out != null) {
                genericServer = new StreamServer(port, new GenericProvider(schemaFile, frequency, out));
            }
            else {
                genericServer = new StreamServer(port, new GenericProvider(schemaFile, frequency));
            }
            genericServer.start();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }

    }

    public static void help() {
        System.out.println("Usage: generator OPTION... \n" + "Runs the generator server.\n" + "\n" + "Possible arguments.\n" + "  -p, --port\t\tthe port to listen on\n"
                + "  -f, --freq\t\tthe data frequency in milliseconds\n" + "  -s, --schema\t\tthe path to the schema file\n" + "  -o, --out\t\tthe path to the output file\n" + " ");
    }
}
