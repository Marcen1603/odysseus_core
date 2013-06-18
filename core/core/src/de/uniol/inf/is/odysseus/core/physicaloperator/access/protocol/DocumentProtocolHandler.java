package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * Protocol handler to read the complete content of an input stream
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <T>
 */
public class DocumentProtocolHandler<T> extends AbstractProtocolHandler<T> {
    Logger LOG = LoggerFactory.getLogger(DocumentProtocolHandler.class);
    protected BufferedReader reader;
    protected BufferedWriter writer;
    private long delay;
    private int nanodelay;
    private int delayeach = 0;
    private long delayCounter = 0L;

    private long dumpEachDocument = -1;
    private long lastDocument = -1;
    protected long documentCounter = 0L;
    private boolean debug = false;
    private boolean isDone = false;
    private StringBuffer measurements = new StringBuffer("");
    private long measureEachDocument = -1;
    private long lastDumpTime = 0;
    private long basetime;

    public DocumentProtocolHandler() {
        super();
    }

    public DocumentProtocolHandler(ITransportDirection direction, IAccessPattern access) {
        super(direction, access);
    }

    @Override
    public void open() throws UnknownHostException, IOException {
        getTransportHandler().open();
        if (getDirection().equals(ITransportDirection.IN)) {
            if ((this.getAccess().equals(IAccessPattern.PULL)) || (this.getAccess().equals(IAccessPattern.ROBUST_PULL))) {
                reader = new BufferedReader(new InputStreamReader(getTransportHandler().getInputStream()));
            }
        } else {
            writer = new BufferedWriter(new OutputStreamWriter(getTransportHandler().getOutputStream()));
        }
        delayCounter = 0;
        documentCounter = 0;
        isDone = false;
    }

    @Override
    public void close() throws IOException {
        try {
            if (getDirection().equals(ITransportDirection.IN)) {
                if (reader != null) {
                    reader.close();
                }
            } else {
                if (writer != null) {
                    writer.close();
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new IOException(e);
        } finally {
            getTransportHandler().close();
        }
    }

    @Override
    public boolean hasNext() throws IOException {
//        if (reader.ready() == false) {
//            isDone = true;
//            return false;
//        }
        return true;
    }

    @Override
    public T getNext() throws IOException {
        delay();
        if (reader.ready()) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            // remove last "\n"
            String result = builder.subSequence(0, builder.length()-1).toString();

            if (debug) {
                if (dumpEachDocument > 0) {
                    if (documentCounter % dumpEachDocument == 0) {
                        long time = System.currentTimeMillis();
                        LOG.debug(documentCounter + " " + time + " " + (time - lastDumpTime) + " " + line);
                        lastDumpTime = time;
                    }
                }
                if (measureEachDocument > 0) {
                    if (documentCounter % measureEachDocument == 0) {
                        long time = System.currentTimeMillis();
                        measurements.append(documentCounter).append(";").append(time - basetime).append("\n");
                    }
                }

                if (lastDocument == documentCounter || documentCounter == 0) {
                    long time = System.currentTimeMillis();
                    if (documentCounter == 0) {
                        basetime = time;
                    }
                    LOG.debug(documentCounter + " " + time);
                    measurements.append(documentCounter).append(";").append(time - basetime).append("\n");
                    if (lastDocument == documentCounter) {
                        System.out.println(measurements);
                        isDone = true;
                    }
                }
                documentCounter++;
            }
            return getDataHandler().readData(result);
        } else {
            return null;
        }
    }

    @Override
    public void write(T object) throws IOException {
        writer.write(object.toString());
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public IProtocolHandler<T> createInstance(ITransportDirection direction, IAccessPattern access, Map<String, String> options, IDataHandler<T> dataHandler, ITransferHandler<T> transfer) {
        DocumentProtocolHandler<T> instance = new DocumentProtocolHandler<T>(direction, access);
        instance.setDataHandler(dataHandler);
        instance.setTransfer(transfer);
        instance.init(options);

        return instance;
    }

    @Override
    public String getName() {
        return "Document";
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public void setNanodelay(int nanodelay) {
        this.nanodelay = nanodelay;
    }

    public int getNanodelay() {
        return nanodelay;
    }

    public int getDelayeach() {
        return delayeach;
    }

    public void setDelayeach(int delayeach) {
        this.delayeach = delayeach;
    }

    @Override
    public ITransportExchangePattern getExchangePattern() {
        if (this.getDirection().equals(ITransportDirection.IN)) {
            return ITransportExchangePattern.InOnly;
        } else {
            return ITransportExchangePattern.OutOnly;
        }
    }

    @Override
    public void onConnect(ITransportHandler caller) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisonnect(ITransportHandler caller) {
        // TODO Auto-generated method stub

    }

    @Override
    public void process(ByteBuffer message) {
        getTransfer().transfer(getDataHandler().readData(message));
    }

    protected void init(Map<String, String> options) {
        if (options.get("delay") != null) {
            setDelay(Long.parseLong(options.get("delay")));
        }
        if (options.get("nanodelay") != null) {
            setNanodelay(Integer.parseInt(options.get("nanodelay")));
        }
        if (options.get("delayeach") != null) {
            setDelayeach(Integer.parseInt(options.get("delayeach")));
        }

        if (options.get("dumpeachdocument") != null) {
            dumpEachDocument = Integer.parseInt(options.get("dumpeachdocument"));
        }

        if (options.get("measureeachdocument") != null) {
            measureEachDocument = Integer.parseInt(options.get("measureeachdocument"));
            measurements.setLength(0);
        }

        if (options.get("lastdocument") != null) {
            lastDocument = Integer.parseInt(options.get("lastdocument"));
        }
        if (options.get("maxdocuments") != null) {
            lastDocument = Integer.parseInt(options.get("maxdocuments"));
        }
        if (options.get("debug") != null) {
            debug = Boolean.parseBoolean(options.get("debug"));
        }
        lastDumpTime = System.currentTimeMillis();
    }

    protected void delay() {
        if (delayeach > 0) {
            delayCounter++;
            if (delayCounter < delayeach) {
                return;
            }
            delayCounter = 0;
        }
        if (delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                LOG.debug(e.getMessage(), e);
            }
        } else {
            if (nanodelay > 0) {
                try {
                    Thread.sleep(0L, nanodelay);
                } catch (InterruptedException e) {
                    LOG.debug(e.getMessage(), e);
                }
            }
        }
    }
}
