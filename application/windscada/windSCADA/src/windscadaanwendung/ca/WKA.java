package windscadaanwendung.ca;

import java.util.Observable;

import windscadaanwendung.hd.HitWKAData;

/**
 * This class holds the data for a WKA (wind turbine). This class extends the
 * Observable to inform the Observers about changes on the data, e.g. the
 * historical data.
 * 
 * @author MarkMilster
 * 
 */
public class WKA extends Observable {

	private HitWKAData hitWKAData;
	private WindFarm farm;

	/**
	 * Returns the windFarm of this WKA
	 * 
	 * @return the windFarm
	 */
	public WindFarm getFarm() {
		return farm;
	}

	/**
	 * Stores a windFarm for this WKA. Also if this wkas was'nt added to the
	 * list of WKAs in the specified windFarm it will be added there.
	 * 
	 * @param farm
	 *            the windFarm of this WKA
	 */
	public void setFarm(WindFarm farm) {
		this.farm = farm;
		this.farm.addWKA(this);
	}

	/**
	 * returns the unique id of this wka
	 * 
	 * @return the id
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * sets the id of this wka
	 * 
	 * @param id
	 *            has to be unique overall wkas to identify a wka independent of
	 *            the windFarm
	 */
	public void setID(int id) {
		this.id = id;
	}

	/**
	 * returns the geographical latitude of this wka
	 * 
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * sets the geographical latitude of this wka
	 * 
	 * @param latitude
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * returns the geographical longtude of this wka
	 * 
	 * @return
	 */
	public double getLongtude() {
		return longtude;
	}

	/**
	 * sets the geographical longtude of this wka
	 * 
	 * @param longtude
	 */
	public void setLongtude(double longtude) {
		this.longtude = longtude;
	}

	private int id;
	private String host;

	/**
	 * returns the host which will deliver the datastream with the current
	 * measurements of this wka
	 * 
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * sets the host which has to deliver the datastream with the current
	 * measurements of this wka
	 * 
	 * @param host
	 */
	public void setHost(String host) {
		this.host = host;
	}

	private int port;

	/**
	 * returns the port on which the also stored host will deliver the
	 * datastream with the current measurements of this wka
	 * 
	 * @return the port of the host
	 */
	public int getPort() {
		return port;
	}

	/**
	 * sets the port on which the also stored host will deliver the datastream
	 * with the current measurements of this wka
	 * 
	 * @return the port of the host
	 */
	public void setPort(int port) {
		this.port = port;
	}

	private double latitude, longtude;

	/**
	 * default constructor
	 */
	public WKA() {
		super();
	}

	/**
	 * returns the historical data of this wka
	 * 
	 * @return the hitWKAData
	 */
	public HitWKAData getHitWKAData() {
		return hitWKAData;
	}

	/**
	 * sets the historical data of this wka. This mehtod also calls all the
	 * observers for this wka and delivers the new historical data to them to
	 * show it
	 * 
	 * @param hitWKAData
	 *            the hitWKAData to set
	 */
	public void setHitWKAData(HitWKAData hitWKAData) {
		this.hitWKAData = hitWKAData;
		setChanged();
		notifyObservers(hitWKAData);
	}

}
