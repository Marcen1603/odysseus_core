/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.wrapper.nmea.nmea;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GGAMessage implements NMEAMessage {
	private Date time;
	private Double latitude;
	private Double longitude;
	private BigDecimal quality;
	private Integer satellites;
	private Double dilution;
	private Double altitude;
	private Double height;
	private String checksum;

	/**
	 * 
	 * @return The time
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
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(Double latitude) {
		Integer dd = ((Double) (latitude / 100)).intValue();
		this.latitude = dd + (latitude - dd * 100) / 60;
	}

	/**
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(Double longitude) {
		Integer dd = ((Double) (longitude / 100)).intValue();
		this.longitude = dd + (longitude - dd * 100) / 60;
	}

	/**
	 * @return the quality
	 */
	public BigDecimal getQuality() {
		return quality;
	}

	/**
	 * @param quality
	 *            the quality to set
	 */
	public void setQuality(BigDecimal quality) {
		this.quality = quality;
	}

	/**
	 * @return the satellites
	 */
	public Integer getSatellites() {
		return satellites;
	}

	/**
	 * @param satellites
	 *            the satellites to set
	 */
	public void setSatellites(Integer satellites) {
		this.satellites = satellites;
	}

	/**
	 * @return the dilution
	 */
	public Double getDilution() {
		return dilution;
	}

	/**
	 * @param dilution
	 *            the dilution to set
	 */
	public void setDilution(Double dilution) {
		this.dilution = dilution;
	}

	/**
	 * @return the altitude
	 */
	public Double getAltitude() {
		return altitude;
	}

	/**
	 * @param altitude
	 *            the altitude to set
	 */
	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}

	/**
	 * @return the height
	 */
	public Double getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(Double height) {
		this.height = height;
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
		data.put("latitude", latitude);
		data.put("longitude", longitude);
		data.put("altitude", altitude);
		data.put("satellites", satellites);
		data.put("dilution", dilution);
		data.put("height", height);
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
		sb.append("Time: " + time + "\n");
		sb.append("Latitude: " + latitude + "\n");
		sb.append("Longitude: " + longitude + "\n");
		sb.append("Quality: " + quality + "\n");
		sb.append("Satellites: " + satellites + "\n");
		sb.append("Dilution: " + dilution + "\n");
		sb.append("Altitude: " + altitude + "\n");
		sb.append("Height: " + height + "\n");
		sb.append("Checksum: " + checksum + "\n");
		return sb.toString();
	}
}
