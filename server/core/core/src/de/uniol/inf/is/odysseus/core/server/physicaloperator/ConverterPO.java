package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.StringTransportHandler;

public class ConverterPO<R extends IStreamObject<IMetaAttribute>, W extends IStreamObject<IMetaAttribute>> extends AbstractPipe<R, W> implements ITransferHandler<W> {

	IDataHandler<R> inputDataHandler;
	IProtocolHandler<W> protocolHandler;
	OptionMap options;

	public ConverterPO(IDataHandler<R> inputDataHandler,
			IDataHandler<W> outputDataHandler,
			IProtocolHandler<W> protocolHandler, OptionMap options) {
		super();
		this.inputDataHandler = inputDataHandler;
		this.protocolHandler = protocolHandler;
		this.options = options;
	}



	public ConverterPO(ConverterPO<R,W> other) {
		throw new IllegalArgumentException("Currently not implemented.!");
	}

	
	
	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(R object, int port) {
		// Take input and generate InputStream/Handler for it
		List<String> output = new LinkedList<>();
		inputDataHandler.writeData(output, object, WriteOptions.defaultOptions);
		StringTransportHandler handler = StringTransportHandler.getInstance(output, options);
		protocolHandler.setTransportHandler(handler);
		try {
			protocolHandler.open();
			while(protocolHandler.hasNext()){
				W out = protocolHandler.getNext();
				if (out != null){
					out.setMetadata(object.getMetadata().clone());
					transfer(out);
				}
			}
			protocolHandler.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
					
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}


}
