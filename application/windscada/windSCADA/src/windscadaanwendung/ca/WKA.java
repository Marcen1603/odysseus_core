package windscadaanwendung.ca;

import windscadaanwendung.AktWKAData;
import windscadaanwendung.hd.HitWKAData;

public class WKA {
	
	private HitWKAData hitWKAData;
	private AktWKAData aktWKAData;
	
	private WindFarm farm;
	public WindFarm getFarm() {
		return farm;
	}

	public void setFarm(WindFarm farm) {
		this.farm = farm;
	}

	public int getID() {
		return this.id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongtude() {
		return longtude;
	}

	public void setLongtude(double longtude) {
		this.longtude = longtude;
	}

	private int id;
	private double latitude, longtude;
	//TODO: add Scripts and comment
	
	public WKA() {
		
	}

	/**
	 * @return the hitWKAData
	 */
	public HitWKAData getHitWKAData() {
		return hitWKAData;
	}

	/**
	 * @param hitWKAData the hitWKAData to set
	 */
	public void setHitWKAData(HitWKAData hitWKAData) {
		
		this.hitWKAData = hitWKAData;
	}

	/**
	 * @return the aktWKAData
	 */
	public AktWKAData getAktWKAData() {
		return aktWKAData;
	}

	/**
	 * @param aktWKAData the aktWKAData to set
	 */
	public void setAktWKAData(AktWKAData aktWKAData) {
		this.aktWKAData = aktWKAData;
	}

}
