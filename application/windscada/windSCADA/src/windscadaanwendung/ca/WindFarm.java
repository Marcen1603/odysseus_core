package windscadaanwendung.ca;

import java.util.ArrayList;
import java.util.List;

import windscadaanwendung.hd.HitWindFarmData;

public class WindFarm {
	
	private HitWindFarmData hitWindFarmData;
	private int id;
	private List<WKA> wkas;
	//TODO: add scripts;
	
	public List<WKA> getWkas() {
		return wkas;
	}

	public void setWkas(List<WKA> wkas) {
		this.wkas = wkas;
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public WindFarm() {
		this.wkas = new ArrayList<WKA>();
	}
	
	public boolean addWKA(WKA wka) {
		return this.wkas.add(wka);
	}
	
	public boolean removeWKA(WKA wka) {
		return this.wkas.remove(wka);
	}
	
	@Override
	public String toString() {
		return String.valueOf(this.id);
	}

	/**
	 * @return the hitWindFarmData
	 */
	public HitWindFarmData getHitWindFarmData() {
		return hitWindFarmData;
	}

	/**
	 * @param hitWindFarmData the hitWindFarmData to set
	 */
	public void setHitWindFarmData(HitWindFarmData hitWindFarmData) {
		this.hitWindFarmData = hitWindFarmData;
	}

}
