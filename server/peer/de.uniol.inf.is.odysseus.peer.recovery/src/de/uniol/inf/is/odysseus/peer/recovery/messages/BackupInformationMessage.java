package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.List;

import net.jxta.id.ID;
import net.jxta.id.IDFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * The message for backup information for recovery. <br />
 * A shared query id and a collection of pql statements will be sent
 * (representing the backup information).
 * 
 * @author Michael Brand
 *
 */
public class BackupInformationMessage implements IMessage {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(BackupInformationMessage.class);

	/**
	 * The separator for different pql statements.
	 */
	public static final String SEPARATOR = "##";

	/**
	 * The byte size for integers.
	 */
	private static final int INT_BUFFER_SIZE = 4;

	/**
	 * The list of pql statements to send.
	 */
	private ImmutableList<String> mPqlStatements;

	/**
	 * The shared query id to send.
	 */
	private ID mSharedQueryID;

	/**
	 * Empty default constructor.
	 */
	public BackupInformationMessage() {

		// Empty construvtor

	}

	/**
	 * Creates a new message with a given shared query id and given pql
	 * statements.
	 * 
	 * @param sharedQueryID
	 *            The given shared query id to send.
	 * @param pqlStatements
	 *            The given pql statements to send.
	 */
	public BackupInformationMessage(ID sharedQueryID,
			Collection<String> pqlStatements) {

		Preconditions.checkNotNull(sharedQueryID,
				"The shared query ID must be not null!");
		this.mSharedQueryID = sharedQueryID;

		Preconditions.checkNotNull(pqlStatements,
				"The collection of pql statements must be not null!");
		Preconditions.checkArgument(!pqlStatements.isEmpty(),
				"The collection of pql statement must be not empty!");
		this.mPqlStatements = ImmutableList.copyOf(pqlStatements);

	}

	@Override
	public byte[] toBytes() {

		int numPQLStatements = this.mPqlStatements.size();
		byte[] sharedQueryIDBytes = mSharedQueryID.toString().getBytes();
		byte[] separatorBytes = SEPARATOR.getBytes();
		byte[][] pqlStatementBytes = new byte[numPQLStatements][];
		for (int index = 0; index < numPQLStatements; index++) {

			pqlStatementBytes[index] = this.mPqlStatements.get(index)
					.getBytes();

		}

		int bufferSize = 0;
		for (int index = 0; index < numPQLStatements; index++) {

			bufferSize += INT_BUFFER_SIZE + pqlStatementBytes[index].length;
			if (index < numPQLStatements - 1) {

				bufferSize += INT_BUFFER_SIZE + separatorBytes.length;

			}

		}
		bufferSize += INT_BUFFER_SIZE + sharedQueryIDBytes.length;

		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
		for (int index = 0; index < numPQLStatements; index++) {

			buffer.putInt(pqlStatementBytes[index].length);
			buffer.put(pqlStatementBytes[index]);
			if (index < numPQLStatements - 1) {

				buffer.putInt(separatorBytes.length);
				buffer.put(separatorBytes);

			}

		}
		buffer.putInt(sharedQueryIDBytes.length);
		buffer.put(sharedQueryIDBytes);

		buffer.flip();
		return buffer.array();

	}

	@Override
	public void fromBytes(byte[] data) {

		ByteBuffer buffer = ByteBuffer.wrap(data);

		int numPQLStatements = this.mPqlStatements.size();
		List<String> pqlStatements = Lists.newArrayList();
		for (int index = 0; index < numPQLStatements; index++) {

			int pqlStatementLength = buffer.getInt();
			byte[] pqlStatementBytes = new byte[pqlStatementLength];
			buffer.get(pqlStatementBytes);
			pqlStatements.add(new String(pqlStatementBytes));
			if (index < numPQLStatements - 1) {

				int separatorLength = buffer.getInt();
				byte[] separatorBytes = new byte[separatorLength];
				buffer.get(separatorBytes);

			}

		}
		this.mPqlStatements = ImmutableList.copyOf(pqlStatements);

		int sharedQueryIDLength = buffer.getInt();
		byte[] sharedQueryIDBytes = new byte[sharedQueryIDLength];
		buffer.get(sharedQueryIDBytes);
		this.mSharedQueryID = toID(new String(sharedQueryIDBytes));

	}

	/**
	 * Creates a shared query id from String.
	 * 
	 * @param text
	 *            The given String.
	 * @return The ID represented by <code>text</code> or null, if an error
	 *         occurs.
	 */
	private static ID toID(String text) {

		try {

			final URI id = new URI(text);
			return IDFactory.fromURI(id);

		} catch (URISyntaxException | ClassCastException ex) {

			LOG.error("Could not get id from text {}", text, ex);
			return null;

		}

	}

	/**
	 * Gets the sent pql statements.
	 * 
	 * @return
	 */
	public ImmutableCollection<String> getPqlStatements() {

		return this.mPqlStatements;

	}

	/**
	 * Gets the sent shared query id.
	 * 
	 * @return
	 */
	public ID getSharedQueryID() {

		return this.mSharedQueryID;

	}

}