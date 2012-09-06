package de.uniol.inf.is.odysseus.context.store.types;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 * @param <T>
 */
public class SingleElementStore<T extends Tuple<? extends ITimeInterval>> extends AbstractContextStore<T> {
    private T tuple = null;

    public SingleElementStore(String name, SDFSchema schema) {
        super(name, schema, 1);
    }

    @Override
    public void insertValue(T value) {
        if (validateSchemaSizeOfValue(value)) {
            if (this.tuple == null) {
                this.tuple = value;
            }
            else {
                if (value.getMetadata().getStart().afterOrEquals(this.tuple.getMetadata().getStart())) {
                    this.tuple = value;
                }
            }
            notifyListener();
        }
        else {
            logger.warn("Context store failure: size of value and schema do not match");
        }
    }

    @Override
    public List<T> getValues(ITimeInterval timeinterval) {
        List<T> list = new ArrayList<T>();
        if (this.tuple != null) {
            list.add(this.tuple);
        }
        return list;
    }

    @Override
    public List<T> getLastValues() {
        List<T> list = new ArrayList<T>();
        if (this.tuple == null) {
            list.add(this.tuple);
        }
        return list;
    }

    @Override
    public List<T> getAllValues() {
        List<T> list = new ArrayList<T>();
        if (this.tuple == null) {
            list.add(this.tuple);
        }
        return list;
    }

    @Override
    public void processTime(PointInTime time) {
    }

    @Override
    protected void internalClear() {
        this.tuple = null;
    }

}
