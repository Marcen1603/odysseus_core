/*******************************************************************************
 * LMS1xx protocol handler for the Odysseus data stream management system
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
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
package de.uniol.inf.is.odysseus.wrapper.lms1xx;

/**
 * Constants for the SICK protocol
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public final class LMS1xxConstants {
    public static final String START_SCAN_COMMAND = "sEN LMDscandata 1";
    public static final String STOP_SCAN_COMMAND = "sEN LMDscandata 0";
    public static final String SET_DATETIME_COMMAND = "sMN LSPsetdatetime";
    public static final String SET_ACCESS_MODE_MAINTAINER_COMMAND = "sMN SetAccessMode 02 B21ACE26";
    public static final String SET_ACCESS_MODE_CLIENT_COMMAND = "sMN SetAccessMode 03 F4724744";
    public static final String SET_ACCESS_MODE_SERVICE_COMMAND = "sMN SetAccessMode 04 81BE23AA";
    public static final String RUN_COMMAND = "sMN Run";

    public static final byte STX = (byte) 0x02;
    public static final byte ETX = (byte) 0x03;
    public static final String SRA = "sRA";
    public static final String SEA = "sEA";
    public static final String SSN = "sSN";
    public static final String SAN = "sAN";
    public static final String SFA = "sFA";
    public static final String SMN = "sMN";
    public static final String SEN = "sEN";
    public static final String SET_ACCESS_MODE = "SetAccessMode";
    public static final String SET_DATETIME = "LSPsetdatetime";
    public static final String LCM_STATE = "LCMstate";
    public static final String LMD_SCANDATA = "LMDscandata";
    public static final String DIST1 = "DIST1";
    public static final String DIST2 = "DIST2";
    public static final String RSSI1 = "RSSI1";
    public static final String RSSI2 = "RSSI2";

    private LMS1xxConstants() {
    }
}
