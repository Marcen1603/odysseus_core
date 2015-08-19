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
package cc.kuka.odysseus.ontology.rcp.l10n;

import org.eclipse.osgi.util.NLS;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class OdysseusNLS extends NLS {
    private static final String BUNDLE_NAME = "OSGI-INF.l10n.bundle"; //$NON-NLS-1$

    public static String OK;
    public static String Cancel;
    public static String Error;

    public static String Name;
    public static String URL;
    public static String New;

    public static String Add;
    public static String Remove;

    public static String Attribute;
    public static String Attributes;

    public static String Property;
    public static String Properties;

    public static String Datatype;
    public static String MeasurementCapability;
    public static String MeasurementCapabilities;

    public static String Condition;
    public static String Conditions;

    public static String MeasurementProperty;
    public static String MeasurementProperties;

    public static String SensingDevice;
    public static String SensingDevices;

    public static String NewSensingDevice;
    public static String DefineSensingDevices;
    public static String DefineMeasurementCapabilities;

    public static String Interval;
    public static String Function;

    public static String URI;

    public static String Expression;
    public static String Expressions;

    static {
        NLS.initializeMessages(OdysseusNLS.BUNDLE_NAME, OdysseusNLS.class);
    }

    private OdysseusNLS() {

    }
}
