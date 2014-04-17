package de.uniol.inf.is.odysseus.p2p_new.physicaloperator;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.datahandler.NullAwareTupleDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionManager;
import de.uniol.inf.is.odysseus.p2p_new.data.ITransmissionReceiver;
import de.uniol.inf.is.odysseus.p2p_new.data.ITransmissionReceiverListener;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;

@SuppressWarnings("rawtypes")
public class JxtaReceiverPO<T extends IStreamObject> extends AbstractSource<T> implements ITransmissionReceiverListener {

	private static final Logger LOG = LoggerFactory.getLogger(JxtaReceiverPO.class);

	private NullAwareTupleDataHandler dataHandler;
	private ITransmissionReceiver transmission;
	
	public JxtaReceiverPO(JxtaReceiverAO ao) {
		SDFSchema schema = ao.getOutputSchema().clone();
		setOutputSchema(schema);
		dataHandler = (NullAwareTupleDataHandler) new NullAwareTupleDataHandler().createInstance(schema);
		
		transmission = DataTransmissionManager.getInstance().registerTransmissionReceiver(ao.getPeerID(), ao.getPipeID());
		transmission.addListener(this);
		transmission.open();
	}
	
	public JxtaReceiverPO(JxtaReceiverPO<T> po) {
		super(po);
		setOutputSchema(po.getOutputSchema().clone());
		
		this.dataHandler = po.dataHandler;
		this.transmission = po.transmission;
	}

	@Override
	public AbstractSource<T> clone() {
		return new JxtaReceiverPO<T>(this);
	}

	@Override
	protected void process_close() {
		try {
			transmission.sendClose();
		} catch (DataTransmissionException e) {
			LOG.error("Could not send close message", e);
		}
	}

	@Override
	protected void process_open() throws OpenFailedException {
		try {
			transmission.sendOpen();
		} catch (DataTransmissionException e) {
			throw new OpenFailedException(e);
		}
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if( ipo == this ) {
			return true;
		}
		return false;
	}

	@Override
	public void onReceiveData(ITransmissionReceiver receiver, byte[] data) {
		T streamObject = JxtaPOUtil.createStreamObject(ByteBuffer.wrap(data), dataHandler);
		if( streamObject != null ) {
			transfer(streamObject);
		}
	}
	
	@Override
	public void onReceivePunctuation(ITransmissionReceiver receiver, IPunctuation punc) {
		sendPunctuation(punc);
	}
	
	@Override
	public void onReceiveDone(ITransmissionReceiver receiver) {
		process_done();
	}
	
	public final ITransmissionReceiver getTransmission() {
		return transmission;
	}
}
