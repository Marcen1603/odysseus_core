package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.util.ByteBufferBackedInputStream;

/**
 * Protocol handler for binary protocols (e.g., eth, ip, tcp) with that it is
 * possible to transform it into a schema of attributes, each defined by the
 * position and length (in bits) in the protocol.<br />
 * <br />
 * The protocol handler can NOT transform the binary data in a data structure
 * without knowledge of the user about the protocol. He needs to define which
 * bits to interpret how.<br />
 * <br />
 * Example for eth, ip and tcp:<br />
 * <br />
 * options = [<br />
 * ['eth_mac_destination', 48],<br />
 * ['eth_mac_source', 48],<br />
 * ['eth_ether_type', 16],<br />
 * ['ip_version', 4],<br />
 * ['ip_ihl', 4],<br />
 * ['ip_tos', 8],<br />
 * ['ip_total_length', 16],<br />
 * ['ip_identification', 16],<br />
 * ['ip_flags', 3],<br />
 * ['ip_fragment_offset', 13],<br />
 * ['ip_ttl', 8],<br />
 * ['ip_protocol', 8],<br />
 * ['ip_header_checksum', 16],<br />
 * ['ip_source_ip_address', 32],<br />
 * ['ip_destination_ip_address', 32],<br />
 * ['tcp_source_port', 16],<br />
 * ['tcp_destination_port', 16],<br />
 * ['tcp_sequence_number', 32],<br />
 * ['tcp_ack_number', 32],<br />
 * ['tcp_data_offset', 4],<br />
 * ['tcp_reserved', 6],<br />
 * ['tcp_flags', 6],<br />
 * ['tcp_window_size', 16],<br />
 * ['tcp_checksum', 16],<br />
 * ['tcp_urgentPointer', 16],<br />
 * ['tcp_payload', -1]<br />
 * ],<br />
 * schema = [<br />
 * ['eth_mac_destination', 'List_Byte'],<br />
 * ['eth_mac_source', 'List_Byte'],<br />
 * ['eth_ether_type', 'Short'],<br />
 * ['ip_version', 'Byte'],<br />
 * ['ip_ihl', 'Byte'],<br />
 * ['ip_tos', 'Byte'],<br />
 * ['ip_total_length', 'Short'],<br />
 * ['ip_identification', 'Short'],<br />
 * ['ip_flags', 'Byte'],<br />
 * ['ip_fragment_offset', 'List_Byte'],<br />
 * ['ip_ttl', 'Byte'],<br />
 * ['ip_protocol', 'Byte'],<br />
 * ['ip_header_checksum', 'Short'],<br />
 * ['ip_source_ip_address', 'List_Byte'],<br />
 * ['ip_destination_ip_address', 'List_Byte'],<br />
 * ['tcp_source_port', 'Short'],<br />
 * ['tcp_destination_port', 'Short'],<br />
 * ['tcp_sequence_number', 'Integer'],<br />
 * ['tcp_ack_number', 'Integer'],<br />
 * ['tcp_data_offset', 'Byte'],<br />
 * ['tcp_reserved', 'Byte'],<br />
 * ['tcp_flags', 'Byte'],<br />
 * ['tcp_window_size', 'Short'],<br />
 * ['tcp_checksum', 'Short'],<br />
 * ['tcp_urgentPointer', 'Short'],<br />
 * ['tcp_payload', 'List_Byte']<br />
 * ]<br />
 * <br />
 * Options and schema must contain all attributes of the schema in the correct order
 * defined by the protocol. The options values are the amount of bits. The last number of bits may be
 * set to -1 to indicate that all remaining bits shall be used. <br/>
 * <br />
 * Restrictions for the datahandlers:<br />
 * <br />
 * Does only work for primitive types and list_byte; not, e.g., for
 * tuple, string, object, other lists. This is because those datahandlers need
 * the amount of elements (e.g., in a list).
 * 
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class GenericByteProtocolHandler extends AbstractProtocolHandler<Tuple<IMetaAttribute>> {

	private static final String name = "GenericByteProtocol";

	private Map<String, Integer> byteSchema = new HashMap<>();

	protected ByteOrder byteOrder;

	// attributes for pull (hasNext etc.)
	private DataInputStream inputStream;
	private boolean isDone = false;

	public GenericByteProtocolHandler() {
		super();
	}

	public GenericByteProtocolHandler(ITransportDirection direction, IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<Tuple<IMetaAttribute>> dataHandler) {
		super(direction, access, dataHandler, options);
		init_internal(dataHandler.getSchema(), options);
	}

	/*
	 * Options: attribute name and number of bits. The last number of bits may be
	 * set to -1 to indicate that all remaining bits shall be used. Schema:
	 * attribute name and datatype. Order must be the same as in the byte-encrypted
	 * protocol!
	 */
	private void init_internal(SDFSchema schema, OptionMap options) {
		if (schema == null) {
			throw new IllegalArgumentException("Schema must be set if " + name + " is used as protocol handler!");
		}

		for (int i = 0; i < schema.size(); i++) {
			String attributeName = schema.getAttribute(i).getAttributeName();
			if (!options.containsKey(attributeName)) {
				throw new IllegalArgumentException("Options must contain an entry '[" + attributeName + ",<numBits>]'");
			}
			int numBits = options.getInt(attributeName, -1);
			if (i < schema.size() - 1 && numBits <= 0) {
				throw new IllegalArgumentException(
						"Value for option '" + attributeName + "' must be an integer greater zero.");
			} else if (i == schema.size() - 1 && (numBits == 0 || numBits < -1)) {
				throw new IllegalArgumentException("Value for option '" + attributeName
						+ "' must be an integer greater zero or -1. The latter to use all remaining bits.");
			}
			byteSchema.put(attributeName, numBits);
		}

		String byteOrderString = optionsMap.get("byteorder", "big_endian");
		if ("LITTLE_ENDIAN".equalsIgnoreCase(byteOrderString)) {
			byteOrder = ByteOrder.LITTLE_ENDIAN;
		} else {
			byteOrder = ByteOrder.BIG_ENDIAN;
		}
	}

	@Override
	public IProtocolHandler<Tuple<IMetaAttribute>> createInstance(ITransportDirection direction, IAccessPattern access,
			OptionMap options, IStreamObjectDataHandler<Tuple<IMetaAttribute>> dataHandler) {
		return new GenericByteProtocolHandler(direction, access, options, dataHandler);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ITransportExchangePattern getExchangePattern() {
		return ITransportExchangePattern.InOut;
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		return false;
	}

	/*
	 * For all attributes: read as many bits as defined in options and call
	 * datahandler. Does only work for primitive types and list_byte; not, e.g., for
	 * tuple, string, object, other lists. This is because those datahandler need
	 * the amount of elements (e.g., in a list).
	 */
	private Tuple<IMetaAttribute> getNext(InputStream inputStream) throws IOException {
		try (BitInputStream bitStream = new BitInputStream(inputStream, byteOrder)) {
			SDFSchema schema = getDataHandler().getSchema();
			Tuple<IMetaAttribute> tuple = new Tuple<>(schema.size(), false);
			@SuppressWarnings("deprecation")
			IDataHandlerRegistry dataHandlerReg = DataHandlerRegistry.instance;
			for (int i = 0; i < schema.size(); i++) {
				SDFAttribute attribute = schema.get(i);
				Object data = readBitsAndTransformToSDFDatatype(attribute, byteSchema.get(attribute.getAttributeName()),
						bitStream, dataHandlerReg, schema.getConstraints());
				tuple.setAttribute(i, data);
			}
			return tuple;
		} catch (IOException e) {
			throw e;
		}
	}

	private Object readBitsAndTransformToSDFDatatype(SDFAttribute attribute, int numBits, BitInputStream bitStream,
			IDataHandlerRegistry dataHandlerReg, Collection<SDFConstraint> schemaConstraints) throws IOException {
		if (bitStream.bitsAvailable() < numBits) {
			throw new IOException("Try to read " + numBits + " bits but bitstream contains only "
					+ bitStream.bitsAvailable() + " bits!");
		} else if (numBits == -1) {
			// special case of -1 for the last attribute (can be set to indicate that all
			// remaining bits shall be used)
			numBits = (int) bitStream.bitsAvailable();
		}

		byte[] bytes = readBitsAndTransformToBytes(numBits, bitStream);
		SDFDatatype datatype = determineDataType(attribute);
		SDFSchema subschema = createSubschema(attribute, schemaConstraints);
		ByteBuffer buffer = transformBytesToByteBuffer(bytes, datatype);
		return dataHandlerReg.getDataHandler(datatype, subschema).readData(buffer);
	}

	private ByteBuffer transformBytesToByteBuffer(byte[] bytes, SDFDatatype datatype) {
		if (datatype.equals(SDFDatatype.LIST_BYTE)) {
			// the number of list elements must be puts as an integer to the beginning of
			// bytebuffer when calling the datahandler.
			ByteBuffer buffer = ByteBuffer.allocate(bytes.length + Integer.BYTES);
			buffer.putInt(bytes.length);
			buffer.put(bytes);
			buffer.flip();
			return buffer;
		}

		return ByteBuffer.wrap(bytes);
	}

	private SDFSchema createSubschema(SDFAttribute attribute, Collection<SDFConstraint> schemaConstraints) {
		SDFSchema subSchema = SDFSchemaFactory.createNewTupleSchema("", attribute);
		if (schemaConstraints != null) {
			subSchema = SDFSchemaFactory.createNewWithContraints(schemaConstraints, subSchema);
		}
		return subSchema;
	}

	private SDFDatatype determineDataType(SDFAttribute attribute) {
		SDFDatatype datatype = attribute.getDatatype();
		if (datatype.isTuple()) {
			throw new IllegalArgumentException(
					datatype.getQualName() + " is not supported by GenericByteProtocolHandler");
		} else if (datatype.isListValue() && !datatype.equals(SDFDatatype.LIST_BYTE)) {
			throw new IllegalArgumentException(
					datatype.getQualName() + " is not supported by GenericByteProtocolHandler");
		}
		return datatype;
	}

	// readBits can only be called with a max length of 63 bits to read.
	// That caused problems, whenever more is tried to read.
	// The original idea was to read then min(numBitsToRead, 63) bits as long as
	// there are bits available and to store the results in a long (datatype) array
	// Afterwards, BitSet.valueOf(long[]) was called. That lead to wrong results.
	// So now, bits are read bytewise (as long as there are more than 7 bits
	// available) and a bitset is created for each byte.
	// Bitset is used to convert from long to byte array.
	private byte[] readBitsAndTransformToBytes(int numBits, BitInputStream bitStream) throws IOException {
		byte[] bytes = new byte[(int) Math.ceil((double) numBits / 8)];
		for (int i = 0; i < bytes.length; i++, numBits -= (int) Math.min(numBits, 8)) {
			BitSet bitSet = BitSet.valueOf(new long[] { bitStream.readBits((int) Math.min(numBits, 8)) });
			if (bitSet.isEmpty()) {
				// special case: for the value 0, the bitset is empty and bitset.toByteArray()
				// would create an empty array.
				bytes[i] = 0;
			} else {
				// we put only 8 bits into the bitset
				bytes[i] = bitSet.toByteArray()[0];
			}
		}
		return bytes;
	}

	// Pull

	@Override
	public void open() throws UnknownHostException, IOException {
		super.open();

		if (getDirection().equals(ITransportDirection.IN)) {
			if ((this.getAccessPattern().equals(IAccessPattern.PULL))
					|| (this.getAccessPattern().equals(IAccessPattern.ROBUST_PULL))) {
				inputStream = new DataInputStream(getTransportHandler().getInputStream());
			}
		}
		isDone = false;
	}

	@Override
	public void close() throws IOException {
		if (getDirection().equals(ITransportDirection.IN)) {
			if (inputStream != null) {
				inputStream.close();
			}
		}

		super.close();
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	public boolean hasNext() throws IOException {
		if (inputStream.available() <= 0) {
			isDone = true;
			return false;
		}
		return true;
	}

	@Override
	public Tuple<IMetaAttribute> getNext() throws IOException {
		return getNext(inputStream);
	}

	// Push
	@Override
	public void process(InputStream message) {
		try {
			Tuple<IMetaAttribute> tuple = getNext(message);
			getTransfer().transfer(tuple);
		} catch (IOException e) {
			LoggerFactory.getLogger(GenericByteProtocolHandler.class).error("Could not process message!", e);
		}
	}

	@Override
	public void process(long callerId, ByteBuffer message) {
		ByteBufferBackedInputStream inputStream = new ByteBufferBackedInputStream(message);
		process(inputStream);
	}

	@Override
	public void process(String message) {
		InputStream inputStream = IOUtils.toInputStream(message);
		process(inputStream);
	}

	@Override
	public void process(String[] message) {
		for (String m : message) {
			process(m);
		}
	}

}