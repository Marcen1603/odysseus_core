package windperformancercp.model;

/**
 * Diese Schnittstelle kapselt moegliche Sourcen in diesem Kontext (Windenergieanlagen, Messmaste...)
 * @author Diana von Gallera
 *
 */

public interface ISource extends IDialogResult {
	
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
	
}
