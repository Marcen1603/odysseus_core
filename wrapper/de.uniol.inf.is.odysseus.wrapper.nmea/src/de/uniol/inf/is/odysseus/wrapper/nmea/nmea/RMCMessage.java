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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RMCMessage implements NMEAMessage {
	private Date time;
	private String status;
	private Double latitude;
	private Double longitude;
	private Double speed;
	private Double course;
	private Date date;
	private Double variation;
	private String mode;
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
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
	 * @return the speed
	 */
	public Double getSpeed() {
		return speed;
	}

	/**
	 * @param speed
	 *            the speed to set
	 */
	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	/**
	 * @return the course
	 */
	public Double getCourse() {
		return course;
	}

	/**
	 * @param course
	 *            the course to set
	 */
	public void setCourse(Double course) {
		this.course = course;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the variation
	 */
	public Double getVariation() {
		return variation;
	}

	/**
	 * @param variation
	 *            the variation to set
	 */
	public void setVariation(Double variation) {
		this.variation = variation;
	}

	/**
	 * @return the mode
	 */
	public String getMode() {
		return mode;
	}

	/**
	 * @param mode
	 *            the mode to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
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
		data.put("speed", speed);
		data.put("course", course);
		data.put("status", status);
		data.put("variation", variation);
		data.put("mode", mode);

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
		sb.append("Status: " + status + "\n");
		sb.append("Latitude: " + latitude + "\n");
		sb.append("Longitude: " + longitude + "\n");
		sb.append("Speed: " + speed + "\n");
		sb.append("Course: " + course + "\n");
		sb.append("Date: " + date + "\n");
		sb.append("Variation: " + variation + "\n");
		sb.append("Mode: " + mode + "\n");
		sb.append("Checksum: " + checksum + "\n");
		return sb.toString();
	}
}
