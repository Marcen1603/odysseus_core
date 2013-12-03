package de.uniol.inf.is.odysseus.wrapper.inertiacube.physicaloperator;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.wrapper.inertiacube.datatype.YawPitchRoll;

public class StoreInertiaPO extends AbstractSink<Tuple<? extends ITimeInterval>> {
    /** Stores the schema for the sink. */
    private final SDFSchema schema;
    
    /** Position of the yawPitchRoll in the data tuple. */
    private int yawPitchRollPos;
    
    /** Path to save files. */
    private String path;
    
    /** Write stream. */
    private BufferedWriter writer = null;
    
    /** New line character. */
    private static String newLine = System.getProperty("line.separator");

    /**
     * Constructs the operator using the given schema.
     * @param s
     * Schema of the data will be passed to this operator.
     */
    public StoreInertiaPO(final SDFSchema s) {
        this.schema = s;
        
        this.yawPitchRollPos = -1;
        final SDFAttribute yawPitchRollAttribute = schema.findAttribute("yawPitchRoll");
        if (yawPitchRollAttribute != null) {
            this.yawPitchRollPos = schema.indexOf(yawPitchRollAttribute);
        }
    }

    /**
     * Copy constructor.
     * @param po
     * Instance to copy from.
     */
    public StoreInertiaPO(final StoreInertiaPO po) {
        this.schema = po.schema;
        this.yawPitchRollPos = po.yawPitchRollPos;
        this.path = po.path;
    }
    
    @Override
    protected void process_open() throws OpenFailedException {
        super.process_open();
        
        if (path.equals("") || path == null)
            throw new RuntimeException("Path may not be null.");
        
        File file = new File(path);
        if (!file.exists()) {
            if (!file.mkdirs())
                throw new RuntimeException("Path '" + path + "' could not be created.");
        }
        
        if (!file.isDirectory())
            throw new RuntimeException("Path should be a directory.");
        
        FileWriter fstream;
		try {
			fstream = new FileWriter(path + "/inertiaCube_" + System.currentTimeMillis() + ".csv");
			writer = new BufferedWriter(fstream);
			writer.write(String.format("%s;%s;%s;%s" + newLine, "timestamp", "yaw", "pitch", "roll"));
		} catch (IOException e) {
			throw new RuntimeException("Output file could not be created.");
		}
    }
    
    @Override
    protected void process_close() {
    	super.process_close();
    	try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Override
    protected void process_next(Tuple<? extends ITimeInterval> object, int port) {
        YawPitchRoll data = (YawPitchRoll) object.getAttribute(yawPitchRollPos);
        try {
			writer.write(String.format("%d;%f;%f;%f" + newLine, System.currentTimeMillis(), data.getYaw(), data.getPitch(), data.getRoll()));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Override
    public void processPunctuation(IPunctuation punctuation, int port) {
    }

    @Override
    public AbstractSink<Tuple<? extends ITimeInterval>> clone() {
        return new StoreInertiaPO(this);
    }
    
    public void setPath(String path) {
        this.path = path;
    }

}
