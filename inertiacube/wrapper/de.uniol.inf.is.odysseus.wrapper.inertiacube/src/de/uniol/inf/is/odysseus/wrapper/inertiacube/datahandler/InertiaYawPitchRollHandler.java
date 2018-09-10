package de.uniol.inf.is.odysseus.wrapper.inertiacube.datahandler;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.wrapper.inertiacube.datatype.YawPitchRoll;

public class InertiaYawPitchRollHandler extends AbstractDataHandler<YawPitchRoll> {
	/** Static field containing the supported types. */
    private static List<String> types = new ArrayList<String>();
    static {
        types.add("YawPitchRoll");
    }

    /**
     * Standard constructor.
     */
    public InertiaYawPitchRollHandler() {
    	super(null);
    }

    /**
     * Constructor with schema.
     * @param schema
     * Passed schema.
     */
    public InertiaYawPitchRollHandler(SDFSchema schema) {
    	super(schema);
    }

	@Override
	public YawPitchRoll readData(ByteBuffer buffer) {
		float yaw = buffer.getFloat();
		float pitch = buffer.getFloat();
		float roll = buffer.getFloat();
		return new YawPitchRoll(yaw, pitch, roll);
	}

	@Override
	public YawPitchRoll readData(String string) {
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
	protected IDataHandler<YawPitchRoll> getInstance(SDFSchema schema) {
		return new InertiaYawPitchRollHandler(schema);
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return Collections.unmodifiableList(types);
	}

	@Override
	public Class<?> createsType() {
		return YawPitchRoll.class;
	}
}
