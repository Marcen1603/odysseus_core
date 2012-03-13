package de.uniol.inf.is.odysseus.aalso.output;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.aalso.exceptions.PlaceTypeUndefinedException;
import de.uniol.inf.is.odysseus.aalso.model.Place;
import de.uniol.inf.is.odysseus.aalso.model.World;
import de.uniol.inf.is.odysseus.aalso.types.BooleanType;
import de.uniol.inf.is.odysseus.aalso.types.FloatNumber;
import de.uniol.inf.is.odysseus.aalso.types.IntegerNumber;
import de.uniol.inf.is.odysseus.aalso.types.Publishable;

public class AalsoDataProvider extends StreamClientHandler {

    /**
     * The <code>World</code> we have to print about.
     */
    private World world;
    /**
     * The name of the referenced place object
     */
    private String sensorObjectReferenceName = "";
    /**
     * The frequency of the SensorObject
     */
    private int frequency;
    /**
     * The port on which the server will send the datastream
     */
    private int startingPort;
    /**
     * host mashine
     */
    private String host;
    /**
     * Stub for the "create-query"
     */
    private String cQLQuery = "";

    /**
     * Sets the global variables and generates the "create-query" by calling the
     * setObjectFrequencyAndQuery- and writeCQLQuery method
     * 
     * @see #setObjectFrequencyAndQuery()
     * @see #writeCQLQuery()
     * 
     * 
     * @param world
     * @see {@link #world}
     * @param sensorObjectReferenceName
     * @see {@link #sensorObjectReferenceName}
     * @param startingPort
     * @see {@link #startingPort}
     * @param host
     * @see {@link #host}
     * @throws Exception
     */
    public AalsoDataProvider(final World world, String sensorObjectReferenceName, int startingPort, String host) throws Exception {
        this.setWorld(world);
        this.setStartingPort(startingPort);
        this.setHost(host);
        this.setSensorObjectReferenceName(sensorObjectReferenceName);
        this.setObjectFrequencyAndQuery();
        this.writeCQLQuery();
    }

    /**
     * Writes the generated CQL-Create-Query in the "OdysCreates.aalso" file
     */
    private void writeCQLQuery() {
        try {
            // Get the path to the file
            ClassLoader loader = this.getClass().getClassLoader();
            if (loader == null) {
                loader = ClassLoader.getSystemClassLoader();
            }
            String url = loader.getResource(this.getClass().getName().replace('.', '/') + ".class").toString();
            String basePath = url.split("Siafu.jar")[0];
            basePath = basePath.substring(10);// 10 if running in jar file! 6
                                              // else
            basePath = basePath.replace('/', '\\');
            // Open the file and lock it so other processes are queued
            RandomAccessFile file = new RandomAccessFile(basePath + "OdysCreates.aalso", "rw");
            FileChannel out = file.getChannel();
            FileLock lock = out.lock();
            file.skipBytes(Integer.MAX_VALUE);
            ByteBuffer textBuffer = ByteBuffer.wrap((this.cQLQuery + "\n").getBytes("ISO-8859-1"));
            out.write(textBuffer);
            lock.release();
            out.close();
            file.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Updates Tuple and provides sensordata by iterating through the context
     * variables of the Place object. Realizes the frequency of the SensorObject
     * through a thread.sleep call.
     */
    @Override
    public List<DataTuple> next() {
        DataTuple sensorData = new DataTuple();
        sensorData.addAttribute(world.getTime().getTime().getTime());
        Place place;
        try {
            place = world.getPlacesOfType(this.getSensorObjectReferenceName()).iterator().next();
            Collection<Publishable> infofields = place.getInfoValues();
            if (!infofields.isEmpty()) {
                for (String infoKey : place.getInfoKeys()) {
                    Publishable info = place.get(infoKey);
                    if (info == null) {
                        throw new RuntimeException("You can't have null values in the Place's info if you are using the TCP-Socket Data Provider");
                    }

                    String infoFlattened = info.flatten().toString();
                    String[] infoSplitted = infoFlattened.split(":");
                    if (infoSplitted[0].equals("FloatNumber")) {
                        sensorData.addAttribute(new Double(((FloatNumber) info).getNumber()));
                    } else if (infoSplitted[0].equals("IntegerNumber")) {
                        sensorData.addAttribute(new Integer(((IntegerNumber) info).getNumber()));
                    } else if (infoSplitted[0].equals("BooleanType")) {
                        sensorData.addAttribute(new Boolean(((BooleanType) info).getValue()));
                    } else if (infoSplitted[0].equals("Text")) {
                        if (!infoKey.equals("frequency")) {
                            sensorData.addAttribute(new String(info.flatten().getData()));
                        }
                    } else {
                        sensorData.addAttribute(new String(info.flatten().getData()));
                    }

                }
            }
        } catch (PlaceTypeUndefinedException e1) {
            e1.printStackTrace();
        }
        // sensorData.addAttribute("test " + new Random().nextInt());
        try {
            // Delay the call for the next DataTuple
            Thread.sleep(1000 / this.getFrequency());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<DataTuple> list = new ArrayList<DataTuple>();
        list.add(sensorData);
        return list;
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }

    @Override
    public void close() {
        // TODO Auto-generated method stub

    }

    /**
     * Read out the places infofields (context variables) and create the
     * "CREATE STREAM" query by identifying the correct StreamSQL DataTypes.
     */
    public void setObjectFrequencyAndQuery() {
        Place place;
        String query = "";
        try {
            place = world.getPlacesOfType(this.getSensorObjectReferenceName()).iterator().next();
            Collection<Publishable> infofields = place.getInfoValues();
            if (!infofields.isEmpty()) {
                query = "CREATE STREAM " + this.host + this.startingPort + this.sensorObjectReferenceName + "(timestamp STARTTIMESTAMP, ";
                // Iterate through info fileds
                for (String infoKey : place.getInfoKeys()) {
                    Publishable info = place.get(infoKey);
                    if (info == null) {
                        throw new RuntimeException("You can't have null values in the Place's info if you are using a CSVPrinter");
                    }
                    
                    String infoFlattened = info.flatten().toString();
                    String[] infoSplitted = infoFlattened.split(":");
                    // Identify the correct STREAM-SQL Datatype and write
                    // the concerning substring in the cQLQuery variable
                    if (infoSplitted[0].equals("FloatNumber")) {
                        query += infoKey.replaceAll("-", "") + " DOUBLE, ";
                    } else if (infoSplitted[0].equals("IntegerNumber")) {
                        if (infoKey.equals("frequency")) {
                            this.setFrequency(((IntegerNumber) info).getNumber());
                        } else {
                            query += infoKey.replaceAll("-", "") + " INTEGER, ";
                        }
                    } else if (infoSplitted[0].equals("BooleanType")) {
                        query += infoKey.replaceAll("-", "") + " BOOLEAN, ";
                    } else if (infoSplitted[0].equals("Text")) {
                        if (!infoKey.equals("frequency")) {
                            query += infoKey.replaceAll("-", "") + " STRING, ";
                        }
                    }

                }
            }
            // Replace last "," with ")" and set host and port
            this.cQLQuery = query.substring(0, query.length() - 2) + ") CHANNEL " + this.host + " : " + this.startingPort + ";";
        } catch (PlaceTypeUndefinedException e1) {
            e1.printStackTrace();
        }
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    public void setSensorObjectReferenceName(String sensorObjectReferenceName) {
        this.sensorObjectReferenceName = sensorObjectReferenceName;
    }

    public String getSensorObjectReferenceName() {
        return sensorObjectReferenceName;
    }

    public void setQuery(String query) {
        this.cQLQuery = query;
    }

    public String getQuery() {
        return cQLQuery;
    }

    public void setStartingPort(int startingPort) {
        this.startingPort = startingPort;
    }

    public int getStartingPort() {
        return startingPort;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }
}
