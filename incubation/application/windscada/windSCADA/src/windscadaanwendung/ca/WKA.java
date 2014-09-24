package windscadaanwendung.ca;

import java.util.Observable;

import windscadaanwendung.hd.HitWKAData;

public class WKA extends Observable {
	
	private HitWKAData hitWKAData;
	
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
	private String host;
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	private int port;
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	private double latitude, longtude;
	
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
		setChanged(); 
        notifyObservers(hitWKAData); 
	}

}
