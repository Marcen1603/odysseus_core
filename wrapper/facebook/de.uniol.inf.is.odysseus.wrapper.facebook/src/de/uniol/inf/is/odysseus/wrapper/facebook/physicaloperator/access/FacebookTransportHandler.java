package de.uniol.inf.is.odysseus.wrapper.facebook.physicaloperator.access;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;



public class FacebookTransportHandler extends AbstractPullTransportHandler {

	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(FacebookTransportHandler.class);

	private String accessToken;
	private String page;
	private int limit = 50;

	private InputStream input;

	private String url;

	public FacebookTransportHandler() {
		super();
	}

	/**
	 * @param protocolHandler
	 */
	public FacebookTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
		init(options);
	}


	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, OptionMap options) {
		final FacebookTransportHandler handler = new FacebookTransportHandler(protocolHandler, options);
		return handler;
	}

	private void init(OptionMap options) {
		if (options.containsKey("accesstoken")) {
			setAccessToken(options.get("accesstoken"));
		}
		if (options.containsKey("page")) {
			setPage(options.get("page"));
		}

		if (options.containsKey("limit")) {
			setLimit(Integer.parseInt(options.get("limit")));
		}
	}


	@Override
	public String getName() {
		return "Facebook";
	}

	@Override
	public void processInOpen() throws IOException {
			this.url = "https://graph.facebook.com/"+this.page+"/feed?fields=message&limit="+this.limit+"&access_token="+this.accessToken;
			this.input = new URL(url).openStream();
	}

	@Override
	public void processInStart() {

	}

	@Override
	public void processOutOpen() throws IOException {

	}

	@Override
	public void processInClose() throws IOException {
		this.input = null;
        this.fireOnDisconnect();
	}


	public String getAccessToken() {
		return this.accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getPage() {
		return this.page;
	}

	public void setPage(String page){
		this.page = page;
	}

	public int getLimit() {
		return this.limit;
	}

	public void setLimit(int limit){
		this.limit = limit;
	}

	@Override
	public InputStream getInputStream() {
		return this.input;
	}

	@Override
	public OutputStream getOutputStream() {
		return null;
	}

	@Override
	public void send(byte[] message) throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public void processOutClose() throws IOException {
		// TODO Auto-generated method stub
	}

    @Override
    public boolean isSemanticallyEqualImpl(ITransportHandler o) {
    	if(!(o instanceof FacebookTransportHandler)) {
    		return false;
    	}
    	FacebookTransportHandler other = (FacebookTransportHandler)o;
    	if(!this.accessToken.equals(other.accessToken)) {
    		return false;
    	} else if(this.limit != other.limit) {
    		return false;
    	} else if(!this.page.equals(other.page)) {
    		return false;
    	}

    	return true;
    }
}
