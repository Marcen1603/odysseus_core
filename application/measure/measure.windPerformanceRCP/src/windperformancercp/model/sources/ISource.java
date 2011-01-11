package windperformancercp.model.sources;

import java.util.ArrayList;

import windperformancercp.event.IEventHandler;


/**
 * Diese Schnittstelle kapselt moegliche Sourcen in diesem Kontext (Windenergieanlagen, Messmaste...)
 * @author Diana von Gallera
 *
 */

public interface ISource extends IDialogResult, IEventHandler {
	
	public int getType();
	public boolean isWindTurbine();
	public boolean isMetMast();
	public void setName(String newName);
	public String getName();
	public void setPort(int newPort);
	public int getPort();
	public void setHost(String newHost);
	public String getHost();
	public int getId();
	public String getStreamIdentifier();
	public void setStreamIdentifier(String strId);
	public ArrayList<Attribute> getAttList();
	public void setAttList(ArrayList<Attribute> newAttl);
	public Attribute getIthAtt(int i);
	public void setIthAtt(int i, Attribute att);
	public int getNumberOfAtts();
	public boolean isConnected();
	public int getConnectState();
	public void setConnectState(int i);
	
}
