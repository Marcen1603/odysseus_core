/**
 * Copyright 2013 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.ontology.rcp.l10n;

import org.eclipse.osgi.util.NLS;

/**
 * @author Christian Kuka <christian@kuka.cc>
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
