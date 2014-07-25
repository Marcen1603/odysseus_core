package de.uniol.inf.is.odysseus.modbus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.io.ModbusTCPTransaction;
import com.ghgande.j2mod.modbus.msg.ReadInputDiscretesRequest;
import com.ghgande.j2mod.modbus.msg.ReadInputDiscretesResponse;
import com.ghgande.j2mod.modbus.net.TCPMasterConnection;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IIteratable;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class ModbusTCPTransportHandler extends AbstractTransportHandler
		implements IIteratable<Tuple<IMetaAttribute>> {

	private static final int DEFAULT_PORT = 0;
	public static final String NAME = "ModbusTCP";
	private int port;
	private InetAddress slave;
	private int ref;
	private int count;

	private TCPMasterConnection con;
	private ModbusTCPTransaction trans;
	private ReadInputDiscretesRequest req;

	public ModbusTCPTransportHandler(IProtocolHandler<?> protocolHandler,
			Map<String, String> options) {
		super(protocolHandler, options);
		init(options);
	}

	private void init(Map<String, String> options) {
		port = DEFAULT_PORT;
		try {
			slave = InetAddress.getByName("");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		ref = 0;
		count = 0;
	}

	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, Map<String, String> options) {
		return new ModbusTCPTransportHandler(protocolHandler, options);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void processInOpen() throws IOException {
		// 2. Open the connection
		con = new TCPMasterConnection(slave);
		con.setPort(port);
		try {
			con.connect();
		} catch (Exception e) {
			throw new IOException(e);
		}
		// TODO: This seems to read one value --> List of ref and count!
		
		// 3. Prepare the request
		req = new ReadInputDiscretesRequest(ref, count);

		trans = new ModbusTCPTransaction(con);
		trans.setRequest(req);
	}

	@Override
	public void processOutOpen() throws IOException {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void processInClose() throws IOException {
		con.close();
	}

	@Override
	public void processOutClose() throws IOException {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void send(byte[] message) throws IOException {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public InputStream getInputStream() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public OutputStream getOutputStream() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasNext() {
		return true; // ?
	}

	@Override
	public Tuple<IMetaAttribute> getNext() {
		try {
			trans.execute();
		} catch (ModbusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ReadInputDiscretesResponse res = (ReadInputDiscretesResponse) trans
				.getResponse();
		System.out.println("Digital Inputs Status="
				+ res.getDiscretes().toString());

		return null;
	}

}
