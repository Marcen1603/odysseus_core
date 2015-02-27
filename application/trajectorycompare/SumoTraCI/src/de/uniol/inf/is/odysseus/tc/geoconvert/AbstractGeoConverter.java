/*
 * com.maddyhome.idea.copyright.pattern.ProjectInfo@7c76668f
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

import java.util.List;

import org.javatuples.Pair;
import org.osgeo.proj4j.BasicCoordinateTransform;
import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.UnknownAuthorityCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.tc.ISumoParams;
import de.uniol.inf.is.odysseus.tc.service.IXMLService;
import de.uniol.inf.is.odysseus.tc.vehicle.VehicleInfo;

/**
 * Created by marcus on 03.12.14.
 */
public abstract class AbstractGeoConverter implements IGeoConverter {

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractGeoConverter.class);

    private final CRSFactory cf = new CRSFactory();
    private final GeometryFactory gf = new GeometryFactory();
    private CoordinateReferenceSystem crsSrc;
    private CoordinateReferenceSystem crsDest;
    private BasicCoordinateTransform basicTransform;

    private Pair<Double, Double> netOffset = null;
    protected String projParameter;

    protected abstract String getToReference();

    public AbstractGeoConverter(IXMLService xmlService, ISumoParams sumoParams) {
        try {

            //first get the path to the netfile
            String pat = sumoParams.getSumoFilePath(
                    xmlService.getValue(
                        sumoParams.getSumoFilePath(
                                sumoParams.getConfigFilename()),
                                "//configuration/input/net-file",
                                "value"
            ));
            final String[] v = xmlService.getValue(pat, "//net/location" ,"netOffset").split(",");
            this.netOffset = Pair.with(
                    Double.parseDouble(v[0]),
                    Double.parseDouble(v[1])
            );
            this.projParameter = xmlService.getValue(pat, "//net/location" ,"projParameter");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        this.crsSrc = this.cf.createFromParameters("Sumo", this.projParameter);
        try {
        	this.crsDest = this.cf.createFromName(this.getToReference());
        } catch(UnknownAuthorityCodeException e) {
        	this.crsDest = this.cf.createFromParameters("Sumo", this.getToReference());
        }
        this.basicTransform = new BasicCoordinateTransform(this.crsSrc, this.crsDest);


    }

    @Override
    public List<VehicleInfo> convert(List<VehicleInfo> vList) {
        for(VehicleInfo vi : vList) {
            vi.setPosition(this.transform(vi.getPosition()));
        }
        return vList;
    }

    private Point transform(Point point) {
        final ProjCoordinate pcSrc = 
        		new ProjCoordinate(point.getX() - this.netOffset.getValue0(), point.getY() - this.netOffset.getValue1());
        final ProjCoordinate pcDest = this.basicTransform.transform(
                pcSrc, new ProjCoordinate()
        );

        return gf.createPoint(new Coordinate(pcDest.y, pcDest.x));
    }
}
