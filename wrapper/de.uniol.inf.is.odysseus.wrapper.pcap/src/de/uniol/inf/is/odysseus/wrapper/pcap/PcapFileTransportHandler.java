package de.uniol.inf.is.odysseus.wrapper.pcap;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.jnetpcap.Pcap;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.JPacketHandler;
import org.jnetpcap.protocol.tcpip.Tcp;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

// TODO javaDoc
public class PcapFileTransportHandler extends AbstractPushTransportHandler {

	/**
	 * The name of the transport handler for query languages.
	 */
	private static final String name = "PcapFile";

	/**
	 * The options name for the file.
	 */
	private static final String fileKey = "file";

	/**
	 * The name of the file.
	 */
	private String filename;

	/**
	 * The Pcap object.
	 */
	private Pcap pcapObj;

	/**
	 * Empty default constructor. Needed to run as an OSGi service.
	 */
	public PcapFileTransportHandler() {
	}

	/**
	 * Creates a new transport handler.
	 *
	 * @param protocolHandler
	 *            The protocol handler to use.
	 * @param options
	 *            The options map.
	 */
	public PcapFileTransportHandler(IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
		init(options);
	}

	/**
	 * Initializes the transport handler.
	 *
	 * @param options
	 *            The options map.
	 */
	private void init(OptionMap options) {
		options.checkRequiredException(fileKey);
		filename = options.get(fileKey);
	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) {
		return new PcapFileTransportHandler(protocolHandler, options);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler obj) {
		if (obj == null || !(obj instanceof PcapFileTransportHandler)) {
			return false;
		}

		PcapFileTransportHandler other = (PcapFileTransportHandler) obj;
		return (this.filename == null && other.filename == null)
				|| (this.filename != null && this.filename.equals(other.filename));
	}

	////////////////////
	// Read Pcap file //
	////////////////////

	@Override
	public void processInOpen() throws IOException {
		final StringBuilder errBuilder = new StringBuilder();
		pcapObj = Pcap.openOffline(filename, errBuilder);
		if (pcapObj == null) {
			throw new IOException("Could not open Pcap file!\n" + errBuilder.toString());
		}

		pcapObj.loop(Pcap.LOOP_INFINITE, new JPacketHandler<PcapFileTransportHandler>() {

			// The object holding all tcp information (including payload)
			final Tcp tcp = new Tcp();

			@Override
			public void nextPacket(JPacket packet, PcapFileTransportHandler pcapHandler) {
				if (packet.hasHeader(tcp)) {
					pcapHandler.fireProcess(ByteBuffer.wrap(tcp.getPayload()));
				}
			}

		}, this);

	}

	@Override
	public void processInClose() throws IOException {
		if (pcapObj != null) {
			pcapObj.close();
		}
	}

	/////////////////////
	// Write Pcap file //
	/////////////////////

	@Override
	public void processOutOpen() throws IOException {
		throw new RuntimeException("PcapFileHanlder is currently only implemented to READ files!");
	}

	@Override
	public void processOutClose() throws IOException {
		throw new RuntimeException("PcapFileHanlder is currently only implemented to READ files!");
	}

	@Override
	public void send(byte[] message) throws IOException {
		throw new RuntimeException("PcapFileHanlder is currently only implemented to READ files!");

	}
}