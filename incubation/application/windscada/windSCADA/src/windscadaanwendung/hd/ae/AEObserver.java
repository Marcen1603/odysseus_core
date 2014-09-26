package windscadaanwendung.hd.ae;

public interface AEObserver {
	
	/**
	 * This Method is called if a AEEntry is created or deleted.
	 * @param aeEntry
	 */
	public void onChangedData(AEEntry aeEntry);

}
