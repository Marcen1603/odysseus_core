package de.uniol.inf.is.odysseus.recovery.installedqueries.internal;

import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferUtil;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;

/**
 * Utility class that serializes and deserializes {@link IExecutorCommand}s.
 * 
 * @author Michael Brand
 *
 */
public class ExecutorCommandSerializer {

	private static final Logger LOG = LoggerFactory.getLogger(ExecutorCommandSerializer.class);

	/**
	 * Encode an {@code IExecutorCommand} to a Base64Binary.
	 * 
	 * @param command
	 *            The command to encode.
	 * @return A string representing the binary.
	 */
	public static String serialize(IExecutorCommand cmd) {
		try {
			return new String(DatatypeConverter.printBase64Binary(ByteBufferUtil.toByteArray(cmd)));
		} catch (IOException e) {
			LOG.error("Executor command is not serializable!", e);
			return null;
		}
	}

	/**
	 * Decode an {@code IExecutorCommand} from a Base64Binary.
	 * 
	 * @param str
	 *            A string representing the binary.
	 * @return The decoded command.
	 */
	public static IExecutorCommand deserialize(String str) {
		try {
			return (IExecutorCommand) ByteBufferUtil.fromByteArray(DatatypeConverter.parseBase64Binary(str));
		} catch (IOException | ClassNotFoundException e) {
			LOG.error("String is not a serialized executor command!", e);
			return null;
		}
	}

}