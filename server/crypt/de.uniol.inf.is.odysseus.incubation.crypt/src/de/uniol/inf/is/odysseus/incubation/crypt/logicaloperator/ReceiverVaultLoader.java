/**
 * 
 */
package de.uniol.inf.is.odysseus.incubation.crypt.logicaloperator;

/**
 * @author MarkMilster
 *
 */
public interface ReceiverVaultLoader extends VaultLoader {

	public int getReceiverID();
	public void setReceiverID(int receiverID);
	
	public int getStreamID();
	public void setStreamID(int streamID);
	
}
