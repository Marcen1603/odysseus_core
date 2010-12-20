package windperformancercp.model;

/**
 * Diese Schnittstelle kapselt moegliche Sourcen in diesem Kontext (Windenergieanlagen, Messmaste...)
 * @author Diana von Gallera
 *
 */

public interface ISource {
	
	public int getType();
	public boolean isWindTurbine();
	public boolean isMetMast();
	public int getPort();
	public int getId();
	public String getStreamIdentifier();
	
}
