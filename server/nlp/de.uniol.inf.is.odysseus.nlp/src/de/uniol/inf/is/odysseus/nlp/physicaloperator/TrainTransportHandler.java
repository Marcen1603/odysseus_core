package de.uniol.inf.is.odysseus.nlp.physicaloperator;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.server.event.error.ParameterException;
import de.uniol.inf.is.odysseus.nlp.datastructure.ToolkitFactory;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model.AnnotationModel;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model.ITrainableModel;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model.TrainableFileAnnotationModel;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelNotFoundException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPToolkitNotFoundException;
import de.uniol.inf.is.odysseus.nlp.datastructure.toolkit.NLPToolkit;

public class TrainTransportHandler extends AbstractPushTransportHandler {
	public static final String NAME = "TRAIN";

	public static final String URIKEY = "uri";
	public static final String CHARSETKEY = "charset";
	public static final String MODELKEY = "model";
	public static final String LANGUAGECODEKEY = "language";
	
	private AnnotationModel<?> model;
	private NLPToolkit toolkit;
	private String modelName;
	private String charset;

	private URI fileURI;
	

	private File file;

	private String language;
	
	public TrainTransportHandler(){
		
	}
	

	public TrainTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
		init(options);
	}


	private void init(OptionMap options) {
		if(!(options.containsKey(CHARSETKEY) && options.containsKey(MODELKEY) && options.containsKey(URIKEY)) && options.containsKey(LANGUAGECODEKEY)){
			throw new ParameterException("You have to define all parameters: "+String.join(",", MODELKEY, URIKEY, CHARSETKEY, LANGUAGECODEKEY));
		}
		this.language = options.get(LANGUAGECODEKEY);
		this.charset = options.get(CHARSETKEY);
		initModel(options.get(MODELKEY));
		try {
			initFile(options.get(URIKEY), options.get(CHARSETKEY));
		} catch (URISyntaxException e) {
			throw new ParameterException("Wrong URI syntax in option "+URIKEY);
		}
	}

	private void initFile(String uri, String charset) throws URISyntaxException {
		fileURI = new URI(uri);
		file = new File(fileURI);
	}


	private void initModel(String model) {
		String[] data = model.split("\\.");
		if(data.length < 2)
			throw new ParameterException(model + " was not found.");
		try {
			toolkit = ToolkitFactory.getEmptyToolkit(data[0]);
			AnnotationModel<?> m = toolkit.getPipeline().identifierToModel(data[1]).newInstance();
			if(m == null){
				throw new ParameterException("Model "+ data[1]+" in existing Toolkit "+ data[0] + " was not found.");
			}else if(m instanceof ITrainableModel){
				this.model = m;
			}else{
				throw new ParameterException("Model " + data[1] + " in Toolkit "+ data[0] + " is not trainable.");
			}
		}catch (NLPModelNotFoundException e) {
			throw new ParameterException("Model "+ data[1]+" in existing Toolkit "+ data[0] + " was not found.");
		} catch (NLPToolkitNotFoundException e) {
			throw new ParameterException("Toolkit "+data[0]+" was not found.");
		} catch (InstantiationException | IllegalAccessException ignored) {
		}
	}


	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) {
		final TrainTransportHandler handler = new TrainTransportHandler(
				protocolHandler, options);
		return handler;
	}


	@Override
	public String getName() {
		return NAME;
	}
	
	protected AnnotationModel<?> getModel(){
		return model;
	}


	@Override
	public void processInOpen() throws IOException{
		Thread trainThread = new Thread(){
			@Override
			public void run(){
				((ITrainableModel) getModel()).train(language, file, charset);
				String[] output = {toolkit.serialize(getModel())};
	            fireProcess(output);
			}
		};
		trainThread.start();
	}


	@Override
	public void processOutOpen() throws IOException {
		
	}


	@Override
	public void processInClose() throws IOException {
	}


	@Override
	public void processOutClose() throws IOException {
	}


	@Override
	public void send(byte[] message) throws IOException {
	}


	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		if(!(other instanceof TrainTransportHandler))
			return false;
		TrainTransportHandler th = (TrainTransportHandler) other;
		return th.charset.equals(th.charset)
				&& th.fileURI.equals(th.fileURI)
				&& th.modelName.equals(th.modelName);
	}
}
