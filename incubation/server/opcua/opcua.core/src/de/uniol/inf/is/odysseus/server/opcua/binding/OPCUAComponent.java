package de.uniol.inf.is.odysseus.server.opcua.binding;

import java.io.Closeable;
import java.io.IOException;

import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.xafero.turjumaan.core.sdk.enums.AttributeIds;

import de.uniol.inf.is.odysseus.server.opcua.access.OPCUATransportHandler;
import de.uniol.inf.is.odysseus.server.opcua.core.OPCValue;

/* 
 * Sometime, somewhere
 */
public class OPCUAComponent implements Closeable {

	public static final OPCUAComponent Instance = new OPCUAComponent();

	@Override
	public void close() throws IOException {
	}

	public void send(OPCUATransportHandler<?> opcuaTransportHandler, NodeId node, AttributeIds attr,
			OPCValue<Double> ov) {
	}
}