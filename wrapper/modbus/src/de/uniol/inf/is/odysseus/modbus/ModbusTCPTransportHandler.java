package de.uniol.inf.is.odysseus.modbus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.io.ModbusTCPTransaction;
import com.ghgande.j2mod.modbus.msg.ReadInputDiscretesRequest;
import com.ghgande.j2mod.modbus.msg.ReadInputDiscretesResponse;
import com.ghgande.j2mod.modbus.net.TCPMasterConnection;

import de.uniol.inf.is.odysseus.core.collection.BitVector;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractSimplePullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class ModbusTCPTransportHandler extends
		AbstractSimplePullTransportHandler<Tuple<IMetaAttribute>> {

	Logger logger = LoggerFactory.getLogger(ModbusTCPTransportHandler.class);
	
	private static final int DEFAULT_PORT = 0;
	public static final String NAME = "ModbusTCP";

	public static final String PORT = "port";
	public static final String SLAVE = "slave";
	public static final String REF = "ref";
	public static final String COUNT = "count";

	private int port;
	private InetAddress slave;
	private int ref;
	private int count;

	private TCPMasterConnection con;
	private ModbusTCPTransaction trans;
	private ReadInputDiscretesRequest req;

	public ModbusTCPTransportHandler() {
	}

	public ModbusTCPTransportHandler(IProtocolHandler<?> protocolHandler,
			OptionMap options) {
		super(protocolHandler, options);
		init(options);
	}

	private void init(OptionMap options) {
		port = DEFAULT_PORT;
		String slaveStr;
		if (options.containsKey(PORT)) {
			port = Integer.parseInt(options.get(PORT));
		}
		if (options.containsKey(SLAVE)) {
			slaveStr = options.get(SLAVE);
		} else {
			throw new IllegalArgumentException(SLAVE + " option must be set");
		}
		try {
			slave = InetAddress.getByName(slaveStr);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		if (options.containsKey(REF)) {
			ref = Integer.parseInt(options.get(REF));
		} else {
			throw new IllegalArgumentException(REF + " option must be set");
		}
		if (options.containsKey(COUNT)) {
			count = Integer.parseInt(options.get(COUNT));
		} else {
			throw new IllegalArgumentException(COUNT + " option must be set");
		}
		logger.debug("initialized with port="+port+" slave="+slave+" ref="+ref+" count="+count);
	}

	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, OptionMap options) {
		return new ModbusTCPTransportHandler(protocolHandler, options);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void processInOpen() throws IOException {
		logger.debug("Opening connection to slave "+slave);
		// 2. Open the connection
		con = new TCPMasterConnection(slave);
		con.setPort(port);
		try {
			con.connect();
		} catch (Exception e) {
			throw new IOException(e);
		}
		// TODO: This seems to read one value --> List of ref and count!
		logger.debug("Creating new read request");
		// 3. Prepare the request
		req = new ReadInputDiscretesRequest(ref, count);

		logger.debug("Creating new Transaction");
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
		return false;
	}

	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public Tuple<IMetaAttribute> getNext() {
		logger.debug("Retieving values from slave "+slave);
		try {
			trans.execute();
		} catch (NullPointerException np) {
			logger.error("got nullpointer from modbus. Is connections established?");
		} catch (ModbusException e) {
			e.printStackTrace();
		}
		logger.debug("Read response");
		ReadInputDiscretesResponse res = (ReadInputDiscretesResponse) trans
				.getResponse();
		if (res == null) {
			logger.error("Slave did not deliver any values ");
		}
		Tuple<IMetaAttribute> t = new Tuple<>(1, false);
		// System.out.println("Digital Inputs Status="
		// + res.getDiscretes().toString());

		if (res != null) {
			logger.debug("Creating output from result "+ res.getDiscretes());
			BitVector out = res.getDiscretes().createOdysseusBitVector();

			t.setAttribute(0, out);
		}
		return t;
	}

}
