/*
 * com.maddyhome.idea.copyright.pattern.ProjectInfo@329306d1
 * Copyright (c) ${year}, ${owner}, All rights reserved.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package de.uniol.inf.is.odysseus.tc.geoconvert;

import com.google.inject.Inject;

import de.uniol.inf.is.odysseus.tc.ISumoParams;
import de.uniol.inf.is.odysseus.tc.service.IXMLService;

/**
 * Created by marcus on 30.11.14.
 */
public class GpsGeoConverter extends AbstractGeoConverter {

    @Inject
    public GpsGeoConverter(IXMLService xmlService, ISumoParams sumoParams) {
        super(xmlService, sumoParams);
    }

    @Override
    protected String getToReference() {
        return "EPSG:4326";
    }
}
