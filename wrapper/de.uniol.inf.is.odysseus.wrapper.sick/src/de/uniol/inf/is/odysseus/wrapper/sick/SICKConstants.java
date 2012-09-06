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
package de.uniol.inf.is.odysseus.wrapper.sick;

/**
 * Constants for the SICK protocol
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public final class SICKConstants {
    public static final String START_SCAN_COMMAND                 = "sEN LMDscandata 1";
    public static final String STOP_SCAN_COMMAND                  = "sEN LMDscandata 0";
    public static final String SET_DATETIME_COMMAND               = "sMN LSPsetdatetime";
    public static final String SET_ACCESS_MODE_MAINTAINER_COMMAND = "sMN SetAccessMode 02 B21ACE26";
    public static final String SET_ACCESS_MODE_CLIENT_COMMAND     = "sMN SetAccessMode 03 F4724744";
    public static final String SET_ACCESS_MODE_SERVICE_COMMAND    = "sMN SetAccessMode 04 81BE23AA";

    public static final byte   START                              = (byte) 0x02;
    public static final byte   END                                = (byte) 0x03;
    public static final String SRA                                = "sRA";
    public static final String SEA                                = "sEA";
    public static final String SSN                                = "sSN";
    public static final String SAN                                = "sAN";
    public static final String SFA                                = "sFA";
    public static final String SMN                                = "sMN";
    public static final String SEN                                = "sEN";
    public static final String SET_ACCESS_MODE                    = "SetAccessMode";
    public static final String SET_DATETIME                       = "LSPsetdatetime";
    public static final String LCM_STATE                          = "LCMstate";
    public static final String LMD_SCANDATA                       = "LMDscandata";
    public static final String DIST1                              = "DIST1";
    public static final String DIST2                              = "DIST2";
    public static final String RSSI1                              = "RSSI1";
    public static final String RSSI2                              = "RSSI2";

    private SICKConstants() {
    }
}
