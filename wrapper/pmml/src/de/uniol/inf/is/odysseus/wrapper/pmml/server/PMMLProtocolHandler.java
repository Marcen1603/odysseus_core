package de.uniol.inf.is.odysseus.wrapper.pmml.server;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import org.dmg.pmml.PMML;
import org.jpmml.model.PMMLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * Defines protocol handler for reading PMML model and returning all available models
 * 
 * 
 * @author Viktor Spadi
 *
 */
@SuppressWarnings("rawtypes")
public class PMMLProtocolHandler extends AbstractProtocolHandler<IStreamObject<? extends IMetaAttribute>>{
	
	private static final Logger logger = LoggerFactory.getLogger(PMMLProtocolHandler.class.getSimpleName());
	
	private boolean parsed = false;
	private boolean fireOnce = false;
	private InputStream inputStream;
	private PMML pmml;
	public PMMLProtocolHandler() {
		super();
	}
	
	public PMMLProtocolHandler(ITransportDirection direction, IAccessPattern access, OptionMap options,
            IStreamObjectDataHandler<IStreamObject<? extends IMetaAttribute>> dataHandler) {
		super(direction, access, dataHandler, options);
		logger.info("Started Instance of PMML Parser");
		// get options
		this.fireOnce = "true".equals(options.get("fireOnce", "false"));
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
		
		logger.info("connection opened");
	}
	
	

	@Override
	public void close() throws IOException {
		super.close();
		getTransportHandler().close();
		this.parsed = false;
		this.inputStream.close();
		this.pmml = null;
		logger.info("connection closed");
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean hasNext() throws IOException {
		boolean wasParsed = this.parsed;
		boolean wasRead = readPMMLFromStream();
		if(pmml == null)
			logger.warn("pmml is null -> check stream");
		else
			logger.debug("parser next: fireOnce="+(fireOnce?"true":"false")+" parsed="+(parsed?"true":"false"));
		return (!fireOnce || !wasParsed) && wasRead;
	}
	
	
	
	private boolean readPMMLFromStream() {
		if(getDirection().equals(ITransportDirection.IN)) {
			try {
				this.inputStream = getTransportHandler().getInputStream();
				
				try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
						BufferedReader reader = new BufferedReader(inputStreamReader)) {

					String pmmlString = reader.lines().collect(Collectors.joining("\n"));
					//Pattern pattern = Pattern.compile("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$"); 
					//Matcher matcher = pattern.matcher(pmmlString);
					
					Decoder decoder = Base64.getDecoder();
					try {
						this.inputStream = new ByteArrayInputStream(decoder.decode(pmmlString));
					} catch(IllegalArgumentException e) {
						// if pmml is not base 64 encoded
					}
				}
		          
				
				if(this.inputStream.available() > 0) {
					PMML pmml = PMMLUtil.unmarshal(this.inputStream);
					
					if(pmml != null) {
						this.pmml = pmml;
						this.parsed = true;
						logger.info("parse pmml successfull");
						return true;
					}
				}
				// catch some exceptions! :) 
			} catch (IllegalArgumentException e) {
				logger.error("Given transport handler has no input stream", e);
			} catch (SAXException e) {
				logger.error("SAX Exception on parsing PMML", e);
			} catch (JAXBException e) {
				if(e.getLinkedException() instanceof SAXParseException) {
					// nothing special, just no more input 
				} else {
					logger.error("JAXB Exception on parsing PMML", e);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	
	@Override
	public boolean isDone() {
		return fireOnce && parsed;
	}
	
	@Override
	public IStreamObject<? extends IMetaAttribute> getNext() throws IOException {
		//logger.info("get next");//Jens: no logging for every processed tuple!
		Tuple<?> t = new Tuple(1, false);
		t.setAttribute(0, this.pmml);
		return t;
	}
	
	@Override
	public void onConnect(ITransportHandler caller) {
		super.onConnect(caller);
		logger.info("connect");
	}

	@Override
	public void onDisonnect(ITransportHandler caller) {
		super.onDisonnect(caller);
		if(this.inputStream != null) {
			try {
				this.inputStream.close();
			} catch (IOException e) {
				logger.error("error closing inputStream", e);
			}
		}
		logger.info("disconnect");
	}
	

	@Override
	public void process(String[] message) {
		logger.debug("Process has been called");
	}
}
