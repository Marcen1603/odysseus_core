package de.uniol.inf.is.odysseus.modbus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ghgande.j2mod.modbus.Modbus;
import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.io.ModbusTCPTransaction;
import com.ghgande.j2mod.modbus.msg.ModbusRequest;
import com.ghgande.j2mod.modbus.msg.ModbusResponse;
import com.ghgande.j2mod.modbus.msg.ReadCoilsRequest;
import com.ghgande.j2mod.modbus.msg.ReadCoilsResponse;
import com.ghgande.j2mod.modbus.msg.ReadInputDiscretesRequest;
import com.ghgande.j2mod.modbus.msg.ReadInputDiscretesResponse;
import com.ghgande.j2mod.modbus.msg.ReadInputRegistersRequest;
import com.ghgande.j2mod.modbus.msg.ReadInputRegistersResponse;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersRequest;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersResponse;
import com.ghgande.j2mod.modbus.msg.WriteCoilRequest;
import com.ghgande.j2mod.modbus.msg.WriteMultipleCoilsRequest;
import com.ghgande.j2mod.modbus.msg.WriteMultipleRegistersRequest;
import com.ghgande.j2mod.modbus.net.TCPMasterConnection;
import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.ghgande.j2mod.modbus.procimg.Register;
import com.ghgande.j2mod.modbus.util.BitVector;

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
	public static final String FUNCTION_CODE = "FUNCTION_CODE".toLowerCase();
	public static final String WRITE_FUNCTION_CODE = "WRITE_FUNCTION_CODE".toLowerCase();	
	public static final String UNIT_ID = "unitid";
	

	private int port;
	private InetAddress slave;
	private int ref;
	private int count;

	private TCPMasterConnection con;
	private ModbusTCPTransaction trans;

	private int readFunctionCode;
	private int writeFunctionCode;

	private int unitID;

	public ModbusTCPTransportHandler() {
	}

	public ModbusTCPTransportHandler(IProtocolHandler<?> protocolHandler,
			OptionMap options) {
		super(protocolHandler, options);
		init(options);
	}

	private void init(OptionMap options) {
		options.checkRequiredException(SLAVE, REF, COUNT);

		port = options.getInt(PORT, DEFAULT_PORT);
		String slaveStr;

		slaveStr = options.get(SLAVE);

		try {
			slave = InetAddress.getByName(slaveStr);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		ref = Integer.parseInt(options.get(REF));
		count = Integer.parseInt(options.get(COUNT));
		readFunctionCode = options.getInt(FUNCTION_CODE, 2);
		
		writeFunctionCode = options.getInt(WRITE_FUNCTION_CODE, -1);
		
		logger.debug("initialized with port=" + port + " slave=" + slave
				+ " ref=" + ref + " count=" + count + " for function "
				+ FUNCTION_CODE);
		unitID = options.getInt(UNIT_ID, -1);
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
		ModbusRequest req = null;

		logger.debug("Opening connection to slave " + slave);
		// 2. Open the connection
		con = new TCPMasterConnection(slave);
		con.setPort(port);
		try {
			con.connect();
		} catch (Exception e) {
			throw new IOException(e);
		}
		// Optional: Configure Modbus Source...
		initSource();
		
		// 3. Prepare the request

		// req = new ReadInputDiscretesRequest(ref, count);
		switch (readFunctionCode) {
		case Modbus.READ_INPUT_DISCRETES:
			req = new ReadInputDiscretesRequest(ref, count);
			break;
		case Modbus.READ_INPUT_REGISTERS:
			req = new ReadInputRegistersRequest(ref, count);
			break;
		case Modbus.READ_MULTIPLE_REGISTERS:
			req = new ReadMultipleRegistersRequest(ref, count);
			break;
		case Modbus.READ_COILS:
			req = new ReadCoilsRequest(ref, count);
			break;
		default:
			throw new IllegalArgumentException("FUNCTION_CODE " + readFunctionCode
					+ " not know");
		}

		if (unitID >= 0) {
			req.setUnitID(unitID);
		}

		logger.debug("Creating new Transaction");
		trans = new ModbusTCPTransaction(con);
		trans.setRequest(req);
	}

	private void initSource() {
		ModbusRequest req = null;
		
		// TODO: Move parameter to options

		// 5 (WriteCoilRequest),
		// 15 (WriteMultipleCoilsRequest) und
		// 16 (WriteMultipleRegistersRequest),
		
		switch (writeFunctionCode) {
		case Modbus.WRITE_COIL:
			boolean b = true;
			req = new WriteCoilRequest(ref, b);
			break;
		case Modbus.WRITE_MULTIPLE_COILS:
			com.ghgande.j2mod.modbus.util.BitVector bv = null;
			new WriteMultipleCoilsRequest(ref, bv);
			new WriteMultipleCoilsRequest(ref, count);
			break;
		case Modbus.WRITE_MULTIPLE_REGISTERS:
			int first = 0;
			Register[] registers = null;
			new WriteMultipleRegistersRequest(first, registers);
			break;
		}

		if (req != null){
			trans = new ModbusTCPTransaction(con);
			trans.setRequest(req);
			try {
				trans.execute();
				@SuppressWarnings("unused")
				ModbusResponse response = trans.getResponse();
				// What to do with the response?
			} catch (ModbusException e) {
				e.printStackTrace();
			}
		}
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
		logger.debug("Retieving values from slave " + slave);
		try {
			trans.execute();
		} catch (NullPointerException np) {
			logger.error("got nullpointer from modbus. Is connections established?");
		} catch (ModbusException e) {
			e.printStackTrace();
		}
		logger.debug("Read response");
		Tuple<IMetaAttribute> t = new Tuple<>(1, false);

		ModbusResponse response = trans.getResponse();
		if (response == null) {
			logger.error("Slave did not deliver any values ");
		} else {
			int fCode = response.getFunctionCode();
			switch (fCode) {
			case Modbus.READ_INPUT_REGISTERS:
				ReadInputRegistersResponse res = (ReadInputRegistersResponse) response;
				List<Integer> resList = new ArrayList<>();
				for (InputRegister r : res.getRegisters()) {
					resList.add(r.getValue());
				}
				t.setAttribute(0, resList);
				break;
			case Modbus.READ_MULTIPLE_REGISTERS:
				ReadMultipleRegistersResponse resp = (ReadMultipleRegistersResponse) response;
				List<Integer> resList2 = new ArrayList<>();
				for (Register r : resp.getRegisters()) {
					resList2.add(r.getValue());
				}
				t.setAttribute(0, resList2);
				break;
			case Modbus.READ_INPUT_DISCRETES:
				com.ghgande.j2mod.modbus.util.BitVector discretes = ((ReadInputDiscretesResponse) response)
						.getDiscretes();
				t.setAttribute(0, discretes.createOdysseusBitVector());
				break;
			case Modbus.READ_COILS:
				com.ghgande.j2mod.modbus.util.BitVector coils = ((ReadCoilsResponse) response)
						.getCoils();
				t.setAttribute(0, coils.createOdysseusBitVector());
				break;
			default:
				t.setAttribute(0, response.getHexMessage());
			}
		}

		return t;
	}
}
