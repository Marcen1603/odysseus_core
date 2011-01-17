package windperformancercp.model.sources;

import java.util.ArrayList;

import windperformancercp.event.IEventHandler;


/**
 * Diese Schnittstelle kapselt moegliche Sourcen in diesem Kontext (Windenergieanlagen, Messmaste...)
 * @author Diana von Gallera
 *
 */

public interface ISource extends IDialogResult, IEventHandler {
	
	
	public void setName(String newName);
	public String getName();
	public String getHost();
	public String getStreamIdentifier();
	public void setStreamIdentifier(String strId);
	public void setHost(String newHost);
	public int getPort();
	public void setPort(int newPort);
	public int getId();
	public ArrayList<Attribute> getAttributeList();
	public ArrayList<String> getAttributeNameList();
	public void setAttributeList(ArrayList<Attribute> newAttl);
	public Attribute getIthAtt(int i);
	public void setIthAtt(int i, Attribute att);
	public int getAttIndex(Attribute att);
	public int getNumberOfAtts();
	public int getType();
	public boolean isWindTurbine();
	public boolean isMetMast();
	public boolean isConnected();
	public int getConnectState();
	public void setConnectState(int i);
	
}
