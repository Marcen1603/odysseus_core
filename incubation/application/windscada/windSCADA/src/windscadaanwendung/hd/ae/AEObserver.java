package windscadaanwendung.hd.ae;

/**
 * This interface is a special type of Observer for the instances that want to
 * listen to historical AEData
 * 
 * @author MarkMilster
 * 
 */
public interface AEObserver {

	/**
	 * This Method is called if a AEEntry is created or deleted.
	 * 
	 * @param aeEntry
	 */
	public void onChangedData(AEEntry aeEntry);

}
