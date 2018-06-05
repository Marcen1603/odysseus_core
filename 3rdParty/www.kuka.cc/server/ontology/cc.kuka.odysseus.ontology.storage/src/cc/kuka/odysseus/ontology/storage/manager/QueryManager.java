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
package cc.kuka.odysseus.ontology.storage.manager;

import java.net.URI;
import java.util.List;

import cc.kuka.odysseus.ontology.common.model.FeatureOfInterest;
import cc.kuka.odysseus.ontology.common.model.Property;
import cc.kuka.odysseus.ontology.common.model.SensingDevice;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public interface QueryManager {
    /**
     * Gets all sensing devices from the ontology.
     *
     * @return A list of URIs
     */

    List<SensingDevice> getAllSensingDevices();

    List<FeatureOfInterest> getAllFeaturesOfInterest();

    List<Property> getAllProperties();

    SensingDevice getSensingDevice(URI uri);

    FeatureOfInterest getFeatureOfInterest(URI uri);

    Property getProperty(URI uri);

    void clearCache();

}
