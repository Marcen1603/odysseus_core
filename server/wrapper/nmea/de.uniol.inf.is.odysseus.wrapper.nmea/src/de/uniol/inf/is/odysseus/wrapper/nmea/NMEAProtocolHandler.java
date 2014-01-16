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
package de.uniol.inf.is.odysseus.wrapper.nmea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.wrapper.nmea.message.GGAMessage;
import de.uniol.inf.is.odysseus.wrapper.nmea.message.GSVMessage;
import de.uniol.inf.is.odysseus.wrapper.nmea.message.RMCMessage;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class NMEAProtocolHandler extends AbstractProtocolHandler<KeyValueObject<? extends IMetaAttribute>> {
    private static final Logger LOG = LoggerFactory.getLogger(NMEAProtocolHandler.class);
    public static final Pattern GGA = Pattern.compile("^\\$GPGGA,([0-9.]+),([0-9.]+),(N|S),([0-9.]+),(E|W),([0-8]?),([0-9]+),([0-9.]*),([0-9.]+),M,([0-9.]+),M,,\\*([0-9A-Za-z]+)$");
    public static final Pattern RMC = Pattern.compile("^\\$GPRMC,([0-9.]+),(A|V),([0-9.]+),(N|S),([0-9.]+),(E|W),([0-9.]+),([0-9.]+),([0-9]+),([0-9.]*),(E|W)?,(N|A|D|E)\\*([0-9A-Za-z]+)$");
    public static final Pattern GSV = Pattern.compile("^\\$GPGSV,([1-3]+),([1-3]+),([0-9]+),([0-9,]*)\\*([0-9A-Za-z]+)$");
    public static final Pattern GSVSatellite = Pattern.compile("([0-9]{1,2}),([0-9]+),([0-9]+),([0-9]*)");
    private Date time;
    private Date date;
    protected BufferedReader reader;

    public NMEAProtocolHandler() {
    }

    public NMEAProtocolHandler(ITransportDirection direction, IAccessPattern access, IDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler) {
        super(direction, access, dataHandler);
    }

    @Override
    public void open() throws UnknownHostException, IOException {
        getTransportHandler().open();
        reader = new BufferedReader(new InputStreamReader(getTransportHandler().getInputStream()));
    }

    @Override
    public void close() throws IOException {
        reader.close();
        getTransportHandler().close();
    }

    @Override
    public boolean hasNext() throws IOException {
        return true;
    }

    @Override
    public KeyValueObject<? extends IMetaAttribute> getNext() throws IOException {
        String data = reader.readLine();
        Map<String, Object> event = new HashMap<String, Object>();
        Matcher ggaMatcher = GGA.matcher(data);
        if (ggaMatcher.find()) {
            GGAMessage message = new GGAMessage();
            SimpleDateFormat sdf = new SimpleDateFormat("HHmmss.SSS");
            try {
                message.setTime(sdf.parse((!"".equals(ggaMatcher.group(1)) ? ggaMatcher.group(1) : "0")));
                time = message.getTime();
            }
            catch (ParseException e) {
                LOG.error(e.getMessage(), e);
            }
            message.setLatitude(new Double((!"".equals(ggaMatcher.group(2)) ? ggaMatcher.group(2) : "0.0")));
            message.setLongitude(new Double((!"".equals(ggaMatcher.group(4)) ? ggaMatcher.group(4) : "0.0")));
            message.setQuality(new BigDecimal((!"".equals(ggaMatcher.group(6)) ? ggaMatcher.group(6) : "0")));
            message.setSatellites(new Integer((!"".equals(ggaMatcher.group(7)) ? ggaMatcher.group(7) : "0")));
            message.setDilution(new Double((!"".equals(ggaMatcher.group(8)) ? ggaMatcher.group(8) : "0.0")));
            message.setAltitude(new Double((!"".equals(ggaMatcher.group(9)) ? ggaMatcher.group(9) : "0.0")));
            message.setHeight(new Double((!"".equals(ggaMatcher.group(10)) ? ggaMatcher.group(10) : "0.0")));
            message.setChecksum(ggaMatcher.group(11));
            event = message.toMap();
        }
        Matcher rmcMatcher = RMC.matcher(data);
        if (rmcMatcher.find()) {
            RMCMessage message = new RMCMessage();
            SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss.SSS");
            SimpleDateFormat sdfDate = new SimpleDateFormat("ddMMyy");
            try {
                message.setTime(sdfTime.parse((!"".equals(rmcMatcher.group(1)) ? rmcMatcher.group(1) : "0.0")));
                time = message.getTime();
            }
            catch (ParseException e) {
                LOG.error(e.getMessage(), e);
            }
            message.setStatus(rmcMatcher.group(2));
            message.setLatitude(new Double((!"".equals(rmcMatcher.group(3)) ? rmcMatcher.group(3) : "0.0")));
            message.setLongitude(new Double((!"".equals(rmcMatcher.group(5)) ? rmcMatcher.group(5) : "0.0")));
            message.setSpeed(new Double((!"".equals(rmcMatcher.group(7)) ? rmcMatcher.group(7) : "0.0")));
            message.setCourse(new Double((!"".equals(rmcMatcher.group(8)) ? rmcMatcher.group(8) : "0.0")));
            try {
                message.setDate(sdfDate.parse((!"".equals(rmcMatcher.group(9)) ? rmcMatcher.group(9) : "0")));
                date = message.getDate();
            }
            catch (ParseException e) {
                LOG.error(e.getMessage(), e);
            }
            message.setVariation(new Double((!"".equals(rmcMatcher.group(10)) ? rmcMatcher.group(10) : "0.0")));
            message.setMode(rmcMatcher.group(12));
            message.setChecksum(rmcMatcher.group(13));
            event = message.toMap();
        }
        Matcher gsvMatcher = GSV.matcher(data);
        if (gsvMatcher.find()) {
            GSVMessage message = new GSVMessage();
            message.setTotal(new Integer((!"".equals(gsvMatcher.group(1)) ? gsvMatcher.group(1) : "0")));
            message.setCount(new Integer((!"".equals(gsvMatcher.group(2)) ? gsvMatcher.group(2) : "0")));
            Integer satellites = new Integer((!"".equals(gsvMatcher.group(3)) ? gsvMatcher.group(3) : "0"));

            String satelliteList = gsvMatcher.group(4);
            Matcher gsvSatelliteMatcher = GSVSatellite.matcher(satelliteList);
            if (gsvSatelliteMatcher.find()) {
                Integer satelliteId = new Integer((!"".equals(gsvSatelliteMatcher.group(1)) ? gsvSatelliteMatcher.group(1) : "0"));
                Integer elevation = new Integer((!"".equals(gsvSatelliteMatcher.group(2)) ? gsvSatelliteMatcher.group(2) : "0"));
                Integer azimuth = new Integer((!"".equals(gsvSatelliteMatcher.group(3)) ? gsvSatelliteMatcher.group(3) : "0"));
                Integer snr = new Integer((!"".equals(gsvSatelliteMatcher.group(4)) ? gsvSatelliteMatcher.group(4) : "0"));
                message.addSatellite(satelliteId, elevation, azimuth, snr);
            }
            message.setSatellitesNum(satellites);
            message.setChecksum(gsvMatcher.group(5));
            event = message.toMap();
        }
        event.put("timestamp", date.getTime() + time.getTime());
        return new KeyValueObject<>(event);
    }

    @Override
    public void write(KeyValueObject<? extends IMetaAttribute> object) throws IOException {
        throw new IllegalArgumentException("Currently not implemented");
    }

    @Override
    public IProtocolHandler<KeyValueObject<? extends IMetaAttribute>> createInstance(ITransportDirection direction, IAccessPattern access, Map<String, String> options,
            IDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler) {
        NMEAProtocolHandler instance = new NMEAProtocolHandler(direction, access, dataHandler);
        instance.setOptionsMap(options);
        return instance;
    }

    @Override
    public String getName() {
        return "NMEA";
    }

    @Override
    public void onConnect(ITransportHandler caller) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisonnect(ITransportHandler caller) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) {
        if (!(o instanceof NMEAProtocolHandler)) {
            return false;
        }
        else {
            // the datahandler was already checked in the
            // isSemanticallyEqual-Method of AbstracProtocolHandler
            return true;
        }
    }

}
