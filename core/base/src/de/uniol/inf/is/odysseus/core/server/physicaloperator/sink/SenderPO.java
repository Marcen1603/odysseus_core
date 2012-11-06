package de.uniol.inf.is.odysseus.core.server.physicaloperator.sink;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;

public class SenderPO<T extends IStreamObject<?>> extends AbstractSink<T> {
    static Logger               LOG = LoggerFactory.getLogger(SenderPO.class);
    private IProtocolHandler<T> protocolHandler;

    public SenderPO(IProtocolHandler<T> protocolHandler) {
        this.protocolHandler = protocolHandler;
    }

    public SenderPO(SenderPO<T> senderPO) {
        super();
        this.protocolHandler = senderPO.protocolHandler;
    }

    @Override
    protected void process_next(T object, int port) {
        try {
            protocolHandler.write(object);
        }
        catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

    }

    @Override
    public void processPunctuation(PointInTime timestamp, int port) {
        // TODO Auto-generated method stub

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
    public AbstractSink<T> clone() {
        return new SenderPO<T>(this);
    }

}
