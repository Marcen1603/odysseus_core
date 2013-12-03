/**
 * 
 */
package de.uniol.inf.is.odysseus.wrapper.nmea.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ckuka
 * 
 */
public class GSVMessage implements NMEAMessage {
	private Date time;
	private Integer total;
	private Integer count;
	private Map<Integer, Satellite> satellites;
	private Integer satellitesNum;
	private String checksum;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.wrapper.nmea.nmea.NMEAMessage#getTime()
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(Date time) {
		this.time = time;
	}

	/**
	 * @return the total
	 */
	public Integer getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(Integer total) {
		this.total = total;
	}

	/**
	 * @return the count
	 */
	public Integer getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(Integer count) {
		this.count = count;
	}

	/**
	 * @return the satellitesNum
	 */
	public Integer getSatellitesNum() {
		return satellitesNum;
	}

	/**
	 * @param satellitesNum
	 *            the satellitesNum to set
	 */
	public void setSatellitesNum(Integer satellitesNum) {
		this.satellitesNum = satellitesNum;
	}

	/**
	 * @return the satellites
	 */
	public Map<Integer, Satellite> getSatellites() {
		return satellites;
	}

	/**
	 * @param satellites
	 *            the satellites to set
	 */
	public void setSatellites(Map<Integer, Satellite> satellites) {
		this.satellites = satellites;
	}

	/**
	 * 
	 * @param id
	 * @param elevation
	 * @param azimuth
	 * @param snr
	 */
	public void addSatellite(Integer id, Integer elevation, Integer azimuth,
			Integer snr) {
		if (satellites == null) {
			satellites = new HashMap<Integer, Satellite>();
		}
		Satellite satellite = new Satellite();
		satellite.setId(id);
		satellite.setElevation(elevation);
		satellite.setAzimuth(azimuth);
		satellite.setSnr(snr);
		satellites.put(id, satellite);
	}

	/**
	 * 
	 * @param id
	 */
	public void removeSatellite(Integer id) {
		satellites.remove(id);
	}

	/**
	 * @return the checksum
	 */
	public String getChecksum() {
		return checksum;
	}

	/**
	 * @param checksum
	 *            the checksum to set
	 */
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.wrapper.nmea.nmea.NMEAMessage#toMap()
	 */
	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("total", total);
		data.put("count", count);
		data.put("satellitesNum", satellitesNum);
		List<Integer> ids = new ArrayList<Integer>();
		List<Integer> elevations = new ArrayList<Integer>();
		List<Integer> azimuths = new ArrayList<Integer>();
		List<Integer> snrs = new ArrayList<Integer>();
		if (satellites != null) {
			for (Satellite sat : satellites.values()) {
				ids.add(sat.getId());
				elevations.add(sat.getElevation());
				azimuths.add(sat.getAzimuth());
				snrs.add(sat.getSnr());
			}
		}
		data.put("id", ids);
		data.put("elevation", elevations);
		data.put("azimuth", azimuths);
		data.put("snr", snrs);
		return data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Total: " + total + "\n");
		sb.append("Count: " + count + "\n");
		sb.append("Satellites: " + satellitesNum + "\n");
		if (satellites != null) {
			for (Satellite sat : satellites.values()) {
				sb.append("Id: " + sat.getId() + "\n");
				sb.append("Elevation: " + sat.getElevation() + "\n");
				sb.append("Azimuth: " + sat.getAzimuth() + "\n");
				sb.append("SNR: " + sat.getSnr() + "\n");
			}
		}
		sb.append("Checksum: " + checksum + "\n");
		return sb.toString();
	}

	class Satellite {
		private Integer id;
		private Integer elevation;
		private Integer azimuth;
		private Integer snr;

		/**
		 * @return the id
		 */
		public Integer getId() {
			return id;
		}

		/**
		 * @param id
		 *            the id to set
		 */
		public void setId(Integer id) {
			this.id = id;
		}

		/**
		 * @return the elevation
		 */
		public Integer getElevation() {
			return elevation;
		}

		/**
		 * @param elevation
		 *            the elevation to set
		 */
		public void setElevation(Integer elevation) {
			this.elevation = elevation;
		}

		/**
		 * @return the azimuth
		 */
		public Integer getAzimuth() {
			return azimuth;
		}

		/**
		 * @param azimuth
		 *            the azimuth to set
		 */
		public void setAzimuth(Integer azimuth) {
			this.azimuth = azimuth;
		}

		/**
		 * @return the snr
		 */
		public Integer getSnr() {
			return snr;
		}

		/**
		 * @param snr
		 *            the snr to set
		 */
		public void setSnr(Integer snr) {
			this.snr = snr;
		}

	}
}
