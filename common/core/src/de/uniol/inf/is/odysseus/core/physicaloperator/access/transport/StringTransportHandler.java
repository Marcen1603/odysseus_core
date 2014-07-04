package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

/**
 * Currently, this class is only used for ConvertPO
 * @author Marco Grawunder
 *
 */

public class StringTransportHandler extends AbstractTransportHandler {

	final InputStream inputStream;
	private Charset charset = Charset.forName("UTF-8");
	
	public StringTransportHandler(List<String> output,  Map<String,String> options) {
		StringBuffer s = new StringBuffer();
		for (int i=0;i<output.size();i++){
			s.append(output.get(i));
			if (i<s.length()-1){
				s.append("\n");
			}
		}
		
		this.inputStream = new ByteArrayInputStream(s.toString().getBytes(charset));
		
	}

	@Override
	public void send(byte[] message) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, Map<String, String> options) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getInputStream() {
		return inputStream;
	}

	@Override
	public OutputStream getOutputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processInOpen() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void processOutOpen() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void processInClose() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void processOutClose() throws IOException {
		// TODO Auto-generated method stub

	}

	public static StringTransportHandler getInstance(List<String> output,  Map<String,String> options ) {
		StringTransportHandler instance = new StringTransportHandler(output, options);
		return instance;
	}
	
    @Override
    public boolean isSemanticallyEqualImpl(ITransportHandler o) {
    	if(!(o instanceof StringTransportHandler)) {
    		return false;
    	}
    	StringTransportHandler other = (StringTransportHandler)o;
    	if(!this.charset.name().equals(other.charset.name())) {
    		return false;
    	}
    	
    	return true;
    }
}
