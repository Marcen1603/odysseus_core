package de.uniol.inf.is.odysseus.core.server.physicaloperator.sink;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;

public class SenderPO extends AbstractSink<IStreamObject<?>> {
    static Logger               LOG = LoggerFactory.getLogger(SenderPO.class);
    private IDataHandler<?>     dataHandler;
    private IProtocolHandler<?> protocolHandler;

    public SenderPO() {
    }

    public SenderPO(IProtocolHandler<?> protocolHandler, IDataHandler<?> dataHandler) {
        this.protocolHandler = protocolHandler;
        this.dataHandler = dataHandler;
    }

    public SenderPO(SenderPO senderPO) {
        super();
        // protocolHandler = (IProtocolHandler<W>)
        // senderPO.protocolHandler.clone();
    }

    @Override
    protected void process_next(IStreamObject<?> object, int port) {

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        dataHandler.writeData(buffer, object);
        try {
            protocolHandler.write(buffer.array());
        }
        catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

    }

    @Override
    public void processPunctuation(PointInTime timestamp, int port) {
        // TODO Auto-generated method stub

    }

    public void setProtocolHandler(IProtocolHandler<?> ph) {
        this.protocolHandler = ph;
    }

    @Override
    protected void process_open() throws OpenFailedException {
        if (!this.isOpen()) {
            super.process_open();
            try {
                protocolHandler.open();
            }
            catch (Exception e) {
                LOG.error(e.getMessage(), e);
                throw new OpenFailedException(e);
            }
        }
    }

    @Override
    protected void process_close() {
        if (this.isOpen()) {
            try {
                protocolHandler.close();
            }
            catch (Exception e) {
                LOG.error(e.getMessage(), e);
                throw new OpenFailedException(e);
            }
            super.process_close();
        }
    }

    @Override
    public AbstractSink<IStreamObject<?>> clone() {
        return new SenderPO(this);
    }

}
