package de.uniol.inf.is.odysseus.wrapper.facebook.physicaloperator.access;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;


public class FacebookTransportHandler extends AbstractPullTransportHandler {

	private final Logger logger = LoggerFactory.getLogger(FacebookTransportHandler.class);

	private String accessToken;
	
	private InputStream input;
	private OutputStream output;
	
	public FacebookTransportHandler() {
		super();
	}
	
	/**
	 * @param protocolHandler
	 */
	public FacebookTransportHandler(final IProtocolHandler<?> protocolHandler) {
		super(protocolHandler);
	}
	
	@Override
	public void send(byte[] message) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, Map<String, String> options) {
		final FacebookTransportHandler handler = new FacebookTransportHandler(protocolHandler);
		handler.init(options);
		return handler;
	}

	private void init(final Map<String, String> options) {
		if (options.containsKey("accesstoken")) {
			setAccessToken(options.get("accesstoken"));
		}
	}


	@Override
	public String getName() {
		return "Facebook";
	}
	/*
	Für den test unten...
	private String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }

	*/

	@Override
	public void processInOpen() throws IOException {
	
		/*facebookClient = new DefaultFacebookClient("CAACEdEose0cBAKCtMmkHFZByB9dhcab2NCZCIzpe4bFhgn53qkxkuxQHWbRKyPI2RHPu3Yz80kSYClZBHjkYGT3TrpbicRIjB5r6tNChtIsZCtG0DRAFUZAihZBfOGgQPqXR89S106jZBR4U4MkWp8sZB5wOZAUidHJrPYCo8uFg8KgZDZD");
		Page page = facebookClient.fetchObject("cocacola", Page.class);

		System.out.println((page.getDescription()));
		System.out.println("Page likes: " + page.getLikes());
		*/
	
			String url = "https://graph.facebook.com/cocacola/feed?limit=50&access_token="+this.accessToken;
			this.input = new URL(url).openStream();
			
			/* Test was im Stream drin steht....
			 * 
		  try {
			JSONObject json;
			
			    try {
			      BufferedReader rd = new BufferedReader(new InputStreamReader(this.input, Charset.forName("UTF-8")));
			
			      String jsonText = readAll(rd);
			      json = new JSONObject(jsonText);
			     
			       // loop array
				  JSONArray requestarray = (JSONArray) json.get("data");
				  for (int i = 0, size = requestarray.length(); i < size; i++) {
						JSONObject singlePost = requestarray.getJSONObject(i);
						if(singlePost.has("message")){
							String message = singlePost.getString("message");
							System.out.println(message);		
						}
					}
			     
			    } finally{
			    	
			    }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		*/
	}


	@Override
	public void processOutOpen() throws IOException {
		
	}

	@Override
	public void processInClose() throws IOException {
		this.input = null;
        this.fireOnDisconnect();
	}

	@Override
	public void processOutClose() throws IOException {
	    this.output = null;
        this.fireOnDisconnect();
	
	}
	
	public String getAccessToken() {
		return this.accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Override
	public InputStream getInputStream() {
		return this.input;
	}

	@Override
	public OutputStream getOutputStream() {
		return this.output;
	}

}
