package de.uniol.inf.is.odysseus.wrapper.iec60870_5_104;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.openmuc.j60870.APdu;
import org.openmuc.j60870.ASdu;
import org.openmuc.j60870.internal.ConnectionSettings;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.wrapper.iec60870_5_104.util.ASDUConverter;

/**
 * The IEC60870-5-104 protocol handler implements the IEC60870-5-104
 * communication standard without TCP layer, so also without handshake. The
 * purpose of this protocol handler is to convert binary data (payload of tcp)
 * to ASdus. <br />
 * <br />
 * ATTENTION: Use only the tuple data handler with this protocol handler!<br />
 * <br />
 * IEC 60870 part 5 is one of the IEC 60870 set of standards which define
 * systems used for telecontrol (supervisory control and data acquisition) in
 * electrical engineering and power system automation applications. Part 5
 * provides a communication profile for sending basic telecontrol messages
 * between two systems, which uses permanent directly connected data circuits
 * between the systems. The IEC Technical Committee 57 (Working Group 03) have
 * developed a protocol standard for telecontrol, teleprotection, and associated
 * telecommunications for electric power systems. <br />
 * <br />
 * Amongst other documents and standards, the IEC Technical Committee 57 has
 * also generated the following companion standard: <br />
 * <br />
 * IEC 60870-5-104 Transmission Protocols - Network access for IEC 60870-5-101
 * using standard transport profiles <br />
 * <br />
 * The IEC 60870-5-104 (IEC 104) protocol is an extension of IEC 101 protocol
 * with the changes in transport, network, link & physical layer services to
 * suit the complete network access. The standard uses an open TCP/IP interface
 * to network to have connectivity to the LAN (Local Area Network) and routers
 * with different facility (ISDN, X.25, Frame relay etc.) can be used to connect
 * to the WAN (Wide Area Network). Application layer of IEC 104 is preserved
 * same as that of IEC 101 with some of the data types and facilities not used.
 * There are two separate link layers defined in the standard, which is suitable
 * for data transfer over Ethernet & serial line (PPP - Point-to-Point
 * Protocol). The control field data of IEC104 contains various types of
 * mechanisms for effective handling of network data synchronization. <br />
 * <br />
 * The security of IEC 104, by design has been proven to be problematic,[2] as
 * many of the other SCADA protocols developed around the same time. Though the
 * IEC technical committee (TC) 57 have published a security standard IEC 62351,
 * which implements end-to-end encryption which would prevent such attacks as
 * replay, man-in-the-middle and packet injection. Unfortunately due to the
 * increase in complexity vendors are reluctant to roll this out on their
 * networks. <br />
 * <br />
 * IEC 60870-5-101 is a standard for power system monitoring, control &
 * associated communications for telecontrol, teleprotection, and associated
 * telecommunications for electric power systems. This is completely compatible
 * with IEC 60870-5-1 to IEC 60870-5-5 standards and uses standard asynchronous
 * serial tele-control channel interface between DTE and DCE. The standard is
 * suitable for multiple configurations like point-to-point, star, mutidropped
 * etc. <br />
 * <br />
 * Features:
 * <ul>
 * <li>Supports unbalanced (only master initiated message) & balanced (can be
 * master/slave initiated) modes of data transfer.</li>
 * <li>Link address and ASDU (Application Service Data Unit) addresses are
 * provided for classifying the end station and different segments under the
 * same.</li>
 * <li>Data is classified into different information objects and each
 * information object is provided with a specific address.</li>
 * <li>Facility to classify the data into high priority (class-1) and low
 * priority (class-2) and transfer the same using separate mechanisms.</li>
 * <li>Possibility of classifying the data into different groups (1-16) to get
 * the data according to the group by issuing specific group interrogation
 * commands from the master & obtaining data under all the groups by issuing a
 * general interrogation.</li>
 * <li>Cyclic & Spontaneous data updating schemes are provided.</li>
 * <li>Facility for time synchronization.</li>
 * <li>Schemes for transfer of files-Example:IED's will store disturbance
 * recorder file in the memory, When electrical disturbance is occurred in the
 * field. This file can be retrieved through IEC103 protocol for fault
 * analysis.</li>
 * </ul>
 * Frame format: <br />
 * Character format of IEC 101 uses 1 start bit, 1 stop bit, 1 parity bit & 8
 * data bits. FT1.2 (defined in IEC 60870-5-1) is used for frame format of IEC
 * 101 which is suitable for asynchronous communication with hamming distance of
 * 4. This uses 3 types of frame formats - Frame with variable length ASDU,
 * Frame with fixed length & single character. Single character is used for
 * acknowledgments, fixed length frames are used for commands & variable lengths
 * are used for sending data. The details of variable length frame is given
 * below <br />
 * <br />
 * <table>
 * <tr>
 * <td>Data unit</td>
 * <td>Name</td>
 * <td>Function</td>
 * </tr>
 * <tr>
 * <td>Start Frame</td>
 * <td>Start Character</td>
 * <td>Indicates start of Frame</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Length Field (*2)</td>
 * <td>Total length of Frame</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Start Character (repeat)</td>
 * <td>Repeat provided for reliability</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Control Field</td>
 * <td>Indicates control functions like message direction</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Link Address (0,1 or 2)</td>
 * <td>Normally used as the device / station address</td>
 * </tr>
 * <tr>
 * <td>Data Unit Identifier</td>
 * <td>Type Identifier</td>
 * <td>Defines the data type which contains specific format of information
 * objects</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Variable Structure Qualifier</td>
 * <td>Indicates whether type contains multiple information objects or not</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>COT (1 or 2)</td>
 * <td>Indicates causes of data transmissions like spontaneous or cyclic</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>ASDU Address (1 or 2)</td>
 * <td>Denotes separate segments and its address inside a device</td>
 * </tr>
 * <tr>
 * <td>Information Object</td>
 * <td>Information Object Address (1 or 2 or 3)</td>
 * <td>Provides address of the information object element</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Information Elements (n)</td>
 * <td>Contains details of the information element depending on the type</td>
 * </tr>
 * <tr>
 * <td>Information Object-2</td>
 * <td>-----</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>-----</td>
 * <td>-----</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>Information Object-m</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>Stop Frame</td>
 * <td>Checksum</td>
 * <td>Used for Error checks</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Stop Char</td>
 * <td>Indicates end of a frame</td>
 * </tr>
 * </table>
 * <br />
 * Types supported:
 * <ul>
 * <li>Single indication without / with 24 / with 56 bit timestamps.</li>
 * <li>Double indication without / with 24 / with 56 bit timestamps.</li>
 * <li>Step position information without / with 24 / with 56 bit
 * timestamps.</li>
 * <li>Measured value – normalized, scaled, short floating point without / with
 * timestamps.</li>
 * <li>Bitstring of 32 bit without / with timestamps.</li>
 * <li>Integrated totals (counters) without / with timestamps.</li>
 * <li>Packed events (start & tripping ) of protection equipments.</li>
 * <li>Single commands.</li>
 * <li>Double commands.</li>
 * <li>Regulating step command.</li>
 * <li>Set point commands of various data formats.</li>
 * <li>Bitstring commands.</li>
 * <li>Interrogation commands.</li>
 * <li>Clock synchronization & delay acquisition commands.</li>
 * <li>Test & reset commands.</li>
 * </ul>
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class IEC104ProtocolHandler extends AbstractProtocolHandler<Tuple<IMetaAttribute>> {

	/**
	 * The name of the protocol handler for query languages.
	 */
	private static final String name = "IEC60870-5-104";

	private final ConnectionSettings connSettings = new ConnectionSettings();

	public IEC104ProtocolHandler() {
		super();
	}

	public IEC104ProtocolHandler(ITransportDirection direction, IAccessPattern access,
			IStreamObjectDataHandler<Tuple<IMetaAttribute>> datahandler, OptionMap optionsMap) {
		super(direction, access, datahandler, optionsMap);
	}

	@Override
	public IProtocolHandler<Tuple<IMetaAttribute>> createInstance(ITransportDirection direction, IAccessPattern access,
			OptionMap options, IStreamObjectDataHandler<Tuple<IMetaAttribute>> dataHandler) {
		return new IEC104ProtocolHandler(direction, access, dataHandler, options);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> obj) {
		return obj != null && obj instanceof IEC104ProtocolHandler;
	}

	/**
	 * Transforms an ASdu to a tuple (FIX data type) and transfers it.
	 */
	private void transferASdu(ASdu asdu) {
		getTransfer().transfer(getDataHandler().readData(ASDUConverter.asduToTuple(asdu)));
	}

	@Override
	public void process(InputStream message) {
		try {
			transferASdu(new APdu(new DataInputStream(message), connSettings).getASdu());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	@Override
	public void process(long callerId, ByteBuffer message) {
		try {
			transferASdu(
					new APdu(new DataInputStream(new ByteArrayInputStream(message.array())), connSettings).getASdu());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	@Override
	public void process(String[] message) {
		StringBuilder builder = new StringBuilder();
		Arrays.asList(message).stream().forEach(string -> builder.append(string + "\n"));
		try {
			transferASdu(
					new APdu(new DataInputStream(new ByteArrayInputStream(builder.toString().getBytes())), connSettings)
							.getASdu());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	@Override
	public void process(Tuple<IMetaAttribute> message) {
		getTransfer().transfer(message);
	}

	@Override
	public void process(Tuple<IMetaAttribute> message, int port) {
		getTransfer().transfer(message, port);
	}

}