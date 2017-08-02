package de.uniol.inf.is.odysseus.wrapper.pmml.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;

import javax.xml.bind.JAXBException;

import org.dmg.pmml.PMML;
import org.jpmml.model.PMMLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class PMMLProtocolHandler extends AbstractProtocolHandler<IStreamObject<? extends IMetaAttribute>>{
	
	private static final Logger logger = LoggerFactory.getLogger(PMMLProtocolHandler.class.getSimpleName());
	
	public PMMLProtocolHandler() {
		super();
	}
	
	public PMMLProtocolHandler(ITransportDirection direction, IAccessPattern access, OptionMap options,
            IStreamObjectDataHandler<IStreamObject<? extends IMetaAttribute>> dataHandler) {
		super(direction, access, dataHandler, options);
		logger.info("Started Instance of PMML Parser");
	}

	@Override
	public IProtocolHandler<IStreamObject<? extends IMetaAttribute>> createInstance(ITransportDirection direction,
			IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<IStreamObject<? extends IMetaAttribute>> dataHandler) {
		return new PMMLProtocolHandler(direction, access, options, dataHandler);
	}

	@Override
	public String getName() {
		return "PMML";
	}
	
	@Override
	public void open() throws UnknownHostException, IOException {
		super.open();
		getTransportHandler().open();
		if(getDirection().equals(ITransportDirection.IN)) {
			try {
				InputStream initialStream = getTransportHandler().getInputStream();
				PMML pmml = PMMLUtil.unmarshal(initialStream);
				
				String app = pmml.getHeader().getApplication().getName();
			} catch (IllegalArgumentException e) {
				logger.error("Given transport handler has no input stream");
			} catch (SAXException e) {
				logger.error("SAX Exception on parsing PMML", e);
			} catch (JAXBException e) {
				logger.error("JAXB Exception on parsing PMML", e);
			}
		}
		logger.info("connection opened");
	}
	

	@Override
	public void close() throws IOException {
		super.close();
		getTransportHandler().close();
		
		logger.info("connection closed");
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean hasNext() throws IOException {
		return true;
	}
	
	@Override
	public IStreamObject<? extends IMetaAttribute> getNext() throws IOException {
		return getDataHandler().readData("[123|Freezy|Meezy]");
	}
	
	@Override
	public void onConnect(ITransportHandler caller) {
		super.onConnect(caller);
	}

	@Override
	public void onDisonnect(ITransportHandler caller) {
		super.onDisonnect(caller);
	}
	

	@Override
	public void process(String[] message) {
		logger.debug("Process has been called");
	}
}
