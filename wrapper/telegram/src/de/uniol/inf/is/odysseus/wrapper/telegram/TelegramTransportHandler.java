package de.uniol.inf.is.odysseus.wrapper.telegram;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;

public class TelegramTransportHandler extends AbstractPushTransportHandler {


	final static private ObjectMapper mapper = new ObjectMapper();

	static final String NAME = "telegram";
	static final String TOKEN = "telegram.api.token";
	private String token;
	private boolean isRunning;

	public TelegramTransportHandler() {
	}

	public TelegramTransportHandler(IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
		init(options);
	}

	private void init(OptionMap options) {
		options.checkRequiredException(TOKEN);
		this.token = options.get(TOKEN);
	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) {
		return new TelegramTransportHandler(protocolHandler, options);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void processInOpen() throws IOException {
		isRunning = true;
		// TODO: Error handling
		Thread runner = new Thread() {

			@Override
			public void run() {
				TelegramBotAPI api = TelegramBotAPI.getInstance(token);
				while (isRunning) {
					try {
						// call blocks
						List<JsonNode> res = api.getUpdates();
						if (isRunning) {
							// res can be empty
							for (JsonNode n : res) {
//								@SuppressWarnings("unchecked")
//								KeyValueObject<IMetaAttribute> kv = (KeyValueObject<IMetaAttribute>) KeyValueObjectDataHandler
//										.jsonToKVOWrapper(n);
								// TODO: Find a solution to add JsonNode directly to KeyValueObject ...
								KeyValueObject<IMetaAttribute> kv = KeyValueObject.createInstance(mapper.writeValueAsString(n));
								TelegramTransportHandler.this.fireProcess(kv);
							}
						} else {
							api.resetLastId();
						}
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}
		};
		runner.start();
	}

	protected void fireProcess(KeyValueObject<IMetaAttribute> kv) {
		super.fireProcess(kv);
	}

	@Override
	public void processInClose() throws IOException {
		isRunning = false;
	}

	@Override
	public void processOutOpen() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void processOutClose() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void send(Object message) throws IOException {
		if (message instanceof KeyValueObject) {
			@SuppressWarnings("unchecked")
			KeyValueObject<IMetaAttribute> kvMessage = (KeyValueObject<IMetaAttribute>)message;
			// TODO: Why the cast?
			Integer chatID=  kvMessage.getAttribute("chat.id");
			if (chatID == null){
				throw new RuntimeException("Cannot find attribute with name chat.id");
			}

			String text = (String) kvMessage.getAttribute("text");
			if (text == null){
				throw new RuntimeException("Cannot find attribute with name text");
			}
			TelegramBotAPI.getInstance(token).sendMessage(chatID, text);
		} else {
			throw new RuntimeException("This format is currently not supported. Only Key Value");
		}
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void send(byte[] message) throws IOException {
		throw new RuntimeException("Cannot send bytes to Telegram");
	}

}
