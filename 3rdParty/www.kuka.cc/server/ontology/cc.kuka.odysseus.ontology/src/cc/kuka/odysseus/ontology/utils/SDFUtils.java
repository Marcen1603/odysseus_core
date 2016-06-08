/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package cc.kuka.odysseus.ontology.utils;

import java.util.Objects;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public final class SDFUtils {
	/**
	 * 
	 * @param attribute
	 * @return
	 */
	public static String getFeatureOfInterestLabel(final SDFAttribute attribute) {
		Objects.requireNonNull(attribute);
		final String featureOfInterestName = attribute.getSourceName();
		if (featureOfInterestName != null) {
			final String[] featureOfInterestArray = featureOfInterestName.split(":");
			if (featureOfInterestArray.length >= 2) {
				final int pos = featureOfInterestArray[0].indexOf(".");
				if (pos > 0) {
					return featureOfInterestArray[0].substring(pos + 1, featureOfInterestArray[0].length());
				}
				return featureOfInterestArray[0];
			}
		}
		return null;
	}

	/**
	 * 
	 * @param attribute
	 * @return
	 */
	public static String getSensingDeviceLabel(final SDFAttribute attribute) {
		Objects.requireNonNull(attribute);
		final String sourceName = attribute.getSourceName();
		if (sourceName != null) {
			final String[] sourceArray = sourceName.split(":");
			if (sourceArray.length >= 2) {
				return sourceArray[1];
			} else if (sourceArray.length == 1) {
				return sourceArray[0];
			}
		}
		return null;
	}

	private SDFUtils() {
		throw new UnsupportedOperationException();
	}
}
