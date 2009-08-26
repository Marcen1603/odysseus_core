package de.uniol.inf.is.odysseus.iec61970.dataaccessserver.communication;
import java.nio.channels.SocketChannel;

/**
 * Hilfsklasse um eine Änderung der Operationen des entsprechenden Channels im Provider anzuzeigen
 * @author Mart Köhler
 *
 */
public class ChangeRequest {
	public static final int REGISTER = 1;
	public static final int CHANGEOPS = 2;
	
	public SocketChannel socket;
	public int type;
	public int ops;
	
	public ChangeRequest(SocketChannel socket, int type, int ops) {
		this.socket = socket;
		this.type = type;
		this.ops = ops;
	}
}
