package de.uniol.inf.is.odysseus.systemload;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.latency.Latency;

public class LatencySystemLoad extends SystemLoad implements ILatencySystemLoad {

    /**
     * 
     */
    private static final long serialVersionUID = 6178098153031328094L;

    @SuppressWarnings("unchecked")
    public final static Class<? extends IMetaAttribute>[] CLASSES = new Class[] { ILatency.class, ISystemLoad.class };

    private ILatency latency = new Latency();

    public LatencySystemLoad() {
        super();

    }

    public LatencySystemLoad(LatencySystemLoad copy) {
        super(copy);

        latency = copy.latency.clone();
    }

    @Override
    public ILatencySystemLoad clone() {
        return new LatencySystemLoad(this);
    }

    @Override
    public void setMinLatencyStart(long timestamp) {
        latency.setMinLatencyStart(timestamp);
    }

    @Override
    public void setMaxLatencyStart(long timestamp) {
        latency.setMaxLatencyStart(timestamp);
    }

    @Override
    public void setLatencyEnd(long timestamp) {
        latency.setLatencyEnd(timestamp);
    }

    @Override
    public long getLatencyStart() {
        return latency.getLatencyStart();
    }

    @Override
    public long getMaxLatency() {
        return latency.getMaxLatency();
    }

    @Override
    public long getLatencyEnd() {
        return latency.getLatencyEnd();
    }

    @Override
    public long getLatency() {
        return latency.getLatency();
    }

    @Override
    public long getMaxLatencyStart() {
        return latency.getMaxLatencyStart();
    }

    @Override
    public String toString() {
        return "( (i,sysload)= " + super.toString() + " | l= " + latency.toString() + " )";
    }

    @Override
    public String getCSVHeader(char delimiter) {
        return super.getCSVHeader(delimiter) + delimiter + latency.getCSVHeader(delimiter);
    }

    @Override
    public String csvToString(WriteOptions options) {
        return super.csvToString(options) + options.getDelimiter() + latency.csvToString(options);
    }

    @Override
    public Class<? extends IMetaAttribute>[] getClasses() {
        return CLASSES;
    }

    @Override
    public String getName() {
        return "LatencySystemLoad";
    }
}