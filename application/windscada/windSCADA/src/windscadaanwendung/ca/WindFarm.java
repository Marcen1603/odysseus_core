package windscadaanwendung.ca;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import windscadaanwendung.hd.HitWindFarmData;

/**
 * This class holds the data for a windFarm which contains several WKAs. This
 * class extends the Observable to inform the Observers about changes on the
 * data, e.g. the historical data.
 * 
 * @author MarkMilster
 * 
 */
public class WindFarm extends Observable {

	private HitWindFarmData hitWindFarmData;
	private int id;
	private List<WKA> wkas;

	/**
	 * Returns a List of the windTurbines in this windFarm
	 * 
	 * @return The List of the windTurbines
	 */
	public List<WKA> getWkas() {
		return wkas;
	}

	/**
	 * Sets a List of windTurbines
	 * They will be called WKA
	 * 
	 * @param wkas
	 *            The List of windTurbines
	 */
	public void setWkas(List<WKA> wkas) {
		this.wkas = wkas;
	}

	/**
	 * Returns the id of this windFarm
	 * 
	 * @return The id of this windFarm
	 */
	public int getID() {
		return id;
	}

	/**
	 * Sets the id of this windFarm
	 * 
	 * @param id
	 */
	public void setID(int id) {
		this.id = id;
	}

	/**
	 * Default Contructor creates a windFarm without data and with a empty List
	 * of WKAs
	 */
	public WindFarm() {
		this.wkas = new ArrayList<WKA>();
	}

	/**
	 * adds a wka to this windFarm if it was'nt added earlier. Also this method
	 * sets this windFarm in the specified WKA
	 * 
	 * @param wka
	 *            to insert
	 * @return true if the insert of this wka was successfull otherwise false,
	 *         if the wka was added earlier this method returns false
	 */
	public boolean addWKA(WKA wka) {
		if (this.wkas.contains(wka)) {
			return false;
		} else {
			if (this.wkas.add(wka)) {
				wka.setFarm(this);
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * removes a wka from this windFarm if it exists in this windFarm
	 * 
	 * @param wka
	 *            to insert
	 * @return true if the wka was removed otherwise false
	 */
	public boolean removeWKA(WKA wka) {
		return this.wkas.remove(wka);
	}

	@Override
	public String toString() {
		return String.valueOf(this.id);
	}

	/**
	 * Returns the historical windFarmData stored for this windFarm
	 * 
	 * @return the hitWindFarmData
	 */
	public HitWindFarmData getHitWindFarmData() {
		return hitWindFarmData;
	}

	/**
	 * Stores the historical windFarmData for this windFarm
	 * 
	 * @param hitWindFarmData
	 *            the hitWindFarmData to set
	 */
	public void setHitWindFarmData(HitWindFarmData hitWindFarmData) {
		this.hitWindFarmData = hitWindFarmData;
		setChanged();
		notifyObservers(hitWindFarmData);
	}

}
