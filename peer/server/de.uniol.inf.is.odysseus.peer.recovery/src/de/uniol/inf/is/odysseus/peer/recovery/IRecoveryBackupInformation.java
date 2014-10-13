package de.uniol.inf.is.odysseus.peer.recovery;

import java.util.Collection;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import com.google.common.collect.ImmutableCollection;

import de.uniol.inf.is.odysseus.core.collection.IPair;

/**
 * A backup information is for query parts, which are executed on another peer. <br />
 * An information consists of the following:<br />
 * - id of the distributed query <br />
 * - the PQL code of the query part <br />
 * - the id of the peer, where the query part is executed <br />
 * - information about subsequent parts (relative to the query part): PQL code
 * and peer, where it is executed.
 * 
 * @author Michael Brand
 *
 */
public interface IRecoveryBackupInformation {

	/**
	 * Sets the id of the distributed query.
	 * 
	 * @param sharedQuery
	 *            The id. <br />
	 *            Must be not null.
	 */
	public void setSharedQuery(ID sharedQuery);

	/**
	 * Gets the id of the distributed query.
	 * 
	 * @return sharedQuery The id.
	 */
	public ID getSharedQuery();

	/**
	 * Sets the PQL code of the query part.
	 * 
	 * @param pql
	 *            The PQL code. <br />
	 *            Must be not null.
	 */
	public void setPQL(String pql);

	/**
	 * Gets the PQL code of the query part.
	 * 
	 * @return The PQL code.
	 */
	public String getPQL();

	/**
	 * Sets the id of the peer, where the query part is executed.
	 * 
	 * @param peer
	 *            The id of the peer, where the query part is executed. <br />
	 *            Must be not null.
	 */
	public void setPeer(PeerID peer);

	/**
	 * Gets the id of the peer, where the query part is executed.
	 * 
	 * @return The id of the peer, where the query part is executed.
	 */
	public PeerID getPeer();

	/**
	 * Gets the information about subsequent parts.
	 * 
	 * @return The information about subsequent parts (relative to the query
	 *         part): PQL code and peer, where it is executed.
	 */
	public ImmutableCollection<IPair<String, PeerID>> getSubsequentPartsInformation();

	/**
	 * Sets the information about subsequent parts.
	 * 
	 * @param info
	 *            The information about subsequent parts (relative to the query
	 *            part): PQL code and peer, where it is executed. <br />
	 *            Must be not null.
	 */
	public void setSubsequentPartsInformation(
			Collection<IPair<String, PeerID>> info);

	/**
	 * Removes the information about a subsequent part.
	 * 
	 * @param info
	 *            The information about a subsequent part (relative to the query
	 *            part): PQL code and peer, where it is executed. <br />
	 *            Must be not null.
	 */
	public void removeSubsequentPartsInformation(IPair<String, PeerID> info);

	/**
	 * Adds the information about a subsequent part.
	 * 
	 * @param info
	 *            The information about a subsequent part (relative to the query
	 *            part): PQL code and peer, where it is executed. <br />
	 *            Must be not null.
	 */
	public void addSubsequentPartsInformation(IPair<String, PeerID> info);

}