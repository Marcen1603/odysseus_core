package de.uniol.inf.is.odysseus.wrapper.urg.datahandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.wrapper.urg.datatype.UrgScann;
import de.uniol.inf.is.odysseus.wrapper.urg.utils.MessageParser;

public class UrgScannDataHandler extends AbstractDataHandler<UrgScann> {
	/** Static field containing the supported types. */
    private static List<String> types = new ArrayList<String>();
    static {
        types.add("UrgScann");
    }

    /**
     * Standard constructor.
     */
    public UrgScannDataHandler() {
    }

    /**
     * Constructor with schema.
     * @param schema
     * Passed schema.
     */
    public UrgScannDataHandler(SDFSchema schema) {
    }
    
	@Override
	public UrgScann readData(ByteBuffer buffer) {
		return MessageParser.parseMessage(buffer);
	}

	@Override
	public UrgScann readData(ObjectInputStream inputStream) throws IOException {
		throw new RuntimeException("Method is not implemented.");
	}

	@Override
	public UrgScann readData(String string) {
		throw new RuntimeException("Method is not implemented.");
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		throw new RuntimeException("Method is not implemented.");
	}

	@Override
	public int memSize(Object attribute) {
		return 0;
	}

	@Override
	protected IDataHandler<UrgScann> getInstance(SDFSchema schema) {
		return new UrgScannDataHandler(schema);
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return Collections.unmodifiableList(types);
	}

}
