package de.uniol.inf.is.odysseus.probabilistic.datahandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticTupleDataHandler extends AbstractDataHandler<ProbabilisticTuple<?>> {
    static protected List<String> types        = new ArrayList<String>();
    static {
        ProbabilisticDoubleHandler.types.add("ProbabilisticTuple");
    }

    IDataHandler<?>[]             dataHandlers = null;

    // Default Constructor for declarative Service needed
    public ProbabilisticTupleDataHandler() {
    }

    private ProbabilisticTupleDataHandler(SDFSchema schema) {
        this.createDataHandler(schema);
    }

    public void init(SDFSchema schema) {
        if (dataHandlers == null) {
            createDataHandler(schema);
        }
        else {
            throw new RuntimeException("TupleDataHandler is immutable. Values already set");
        }
    }

    public void init(List<String> schema) {
        if (dataHandlers == null) {
            createDataHandler(schema);
        }
        else {
            throw new RuntimeException("TupleDataHandler is immutable. Values already set");
        }
    }

    @Override
    public ProbabilisticTuple<?> readData(ByteBuffer buffer) {
        ProbabilisticTuple<?> r = null;
        synchronized (buffer) {
            Object[] attributes = new Object[dataHandlers.length];
            for (int i = 0; i < dataHandlers.length; i++) {
                attributes[i] = dataHandlers[i].readData(buffer);
            }
            r = new ProbabilisticTuple<IMetaAttribute>(attributes, false);
        }
        return r;
    }

    @Override
    public ProbabilisticTuple<?> readData(ObjectInputStream inputStream) throws IOException {
        Object[] attributes = new Object[dataHandlers.length];
        for (int i = 0; i < this.dataHandlers.length; i++) {
            attributes[i] = dataHandlers[i].readData(inputStream);
        }
        return new ProbabilisticTuple<IMetaAttribute>(attributes, false);
    }

    @Override
    public ProbabilisticTuple<?> readData(String string) {
        throw new RuntimeException("Sorry. Currently not implemented");
    }

    @Override
    public void writeData(ByteBuffer buffer, Object data) {
        ProbabilisticTuple<?> r = (ProbabilisticTuple<?>) data;

        int size = memSize(r);

        if (size > buffer.capacity()) {
            buffer = ByteBuffer.allocate(size * 2);
        }

        synchronized (buffer) {
            for (int i = 0; i < dataHandlers.length; i++) {
                dataHandlers[i].writeData(buffer, r.getAttribute(i));
            }
        }
    }

    @Override
    public int memSize(Object attribute) {
        ProbabilisticTuple<?> r = (ProbabilisticTuple<?>) attribute;
        int size = 0;
        for (int i = 0; i < dataHandlers.length; i++) {
            size += dataHandlers[i].memSize(r.getAttribute(i));
        }
        return size;
    }

    @Override
    public List<String> getSupportedDataTypes() {
        return Collections.unmodifiableList(ProbabilisticTupleDataHandler.types);
    }

    @Override
    protected IDataHandler<ProbabilisticTuple<?>> getInstance(SDFSchema schema) {
        return new ProbabilisticTupleDataHandler(schema);
    }

    private void createDataHandler(SDFSchema schema) {
        this.dataHandlers = new IDataHandler<?>[schema.size()];
        int i = 0;
        for (SDFAttribute attribute : schema) {

            SDFDatatype type = attribute.getDatatype();
            String uri = attribute.getDatatype().getURI(false);
            ;

            // is this really needed??
            if (type.isTuple()) {
                uri = "TUPLE";
            }
            else if (type.isMultiValue()) {
                uri = "MULTI_VALUE";
            }

            if (!DataHandlerRegistry.containsDataHandler(uri)) {
                throw new IllegalArgumentException("Unregistered datatype " + uri);
            }

            dataHandlers[i++] = DataHandlerRegistry.getDataHandler(uri, new SDFSchema("", attribute));

        }
    }

    private void createDataHandler(List<String> schema) {
        this.dataHandlers = new IDataHandler<?>[schema.size()];
        int i = 0;
        for (String attribute : schema) {

            IDataHandler<?> handler = DataHandlerRegistry.getDataHandler(attribute, (SDFSchema) null);

            if (handler == null) {
                throw new IllegalArgumentException("Unregistered datatype " + attribute);
            }

            this.dataHandlers[i++] = handler;
        }
    }

}
