/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.dsm.generators;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;

/**
 * 
 * @author Daniel Weinberg Created at: 07.09.2011
 */
public class Appliance extends StreamClientHandler {

    private Calendar calendar = Calendar.getInstance();
    private long timestamp; // Zeitstempel
    private int randomRuntimes; // Starthäufigkeiten am aktuellen Tag
    private long currentDay; // aktueller Tag
    private int interval = 0; // aktuelles Intervall der Gerätestarts
    private long[][] randomTimes; // Startzeitpunkte am aktuellen Tag

    private double watt; // Leistungsaufnahme des Geräts
    private int rMin; // min Starts pro Tag
    private int rMax; // max Starts pro Tag
    private int startMin; // früheste Startuhrzeit in Stunden an einem Tag
    private int startMax; // späteste Startuhrzeit in Stunden an einem Tag
    private int startUpTime; // Anlaufzeit eines Geräts in ms
    private String name; // Identifikationsname des Geräts
    private int runtimeMin; // minimale Laufzeit in Minuten
    private int runtimeMax; // maximale Laufzeit in Minuten
    private double iRange; // Schwankung
    private double iLength; // Schwankungslänge
    private double measuredWatt = 0; // Initialer Messwert
    private long measureTimestamp; // Zeitstempel der letzten Messung
    private int frequency; // Sendefrequenz
    private double probability; // Startwahrscheinlichkeit pro Tag

    /*
     * Übergabe der initialen Werte aus der Konfigurationsdatei
     */
    public Appliance(String name, double watt, int runtimeMin, int runtimeMax, double iRange, double iLength, int rMin, int rMax, int frequency, int startMin, int startMax, int startUpTime,
            double probability) {
        this.name = name;
        this.watt = watt;
        this.runtimeMin = runtimeMin;
        this.runtimeMax = runtimeMax;
        this.iRange = iRange;
        this.iLength = iLength;
        this.rMin = rMin;
        this.rMax = rMax;
        this.startMin = startMin;
        this.startMax = startMax;
        this.startUpTime = startUpTime;
        this.frequency = frequency;
        this.probability = probability;
    }

    private Appliance(Appliance appliance) {
        this.name = appliance.name;
        this.watt = appliance.watt;
        this.runtimeMin = appliance.runtimeMin;
        this.runtimeMax = appliance.runtimeMax;
        this.iRange = appliance.iRange;
        this.iLength = appliance.iLength;
        this.rMin = appliance.rMin;
        this.rMax = appliance.rMax;
        this.startMin = appliance.startMin;
        this.startMax = appliance.startMax;
        this.startUpTime = appliance.startUpTime;
        this.frequency = appliance.frequency;
        this.probability = appliance.probability;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uniol.inf.is.odysseus.generator.StreamClientHandler#init()
     * 
     * Bestimmung der initialen Werte
     */
    @Override
    public void init() {
        randomTimes = new long[2][rMax];
        calendar.setTimeInMillis(SimulationClock.getInstance().getTime());
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        randomRuntimes = getRandomNumber(rMin, rMax);
        if (getProbability(probability) == false) {
            randomRuntimes = 0;
        }
        if (randomRuntimes > 0) {
            randomTimes[1][0] = getRandomNumber(runtimeMin, runtimeMax);
            randomTimes[0][0] = getRandomStart(startMin, (((startMax - startMin) / randomRuntimes) + startMin));
            for (int i = 1; i < randomRuntimes; i++) {
                calendar.setTimeInMillis((randomTimes[0][i - 1] + (randomTimes[1][i - 1] * 60000)));
                int tmpStartMin = calendar.get(Calendar.HOUR_OF_DAY);
                randomTimes[0][i] = getRandomStart(tmpStartMin, (((startMax - tmpStartMin) / (randomRuntimes - i)) + tmpStartMin));
                randomTimes[1][i] = getRandomNumber(runtimeMin, runtimeMax);
            }
        }
    }

    @Override
    public void close() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uniol.inf.is.odysseus.generator.StreamClientHandler#next()
     * 
     * Erstellung eines Datentupels
     */
    @Override
    public List<DataTuple> next() {
        DataTuple tuple = new DataTuple();
        timestamp = SimulationClock.getInstance().getTime();
        newDay(timestamp);

        if (randomRuntimes > 0) {
            if (timestamp >= randomTimes[0][interval] && timestamp <= randomTimes[0][interval] + (randomTimes[1][interval] * 60000)) {
                tuple.addLong(timestamp); // Zeitstempel
                tuple.addString(name); // Gerätename (ID)
                tuple.addDouble(getMeteredValue(timestamp)); // Watt-Wert
                // /tuple.addDouble(getMeteredConsumption(timestamp));
                // //Wattmillisekunden-Wert
            } else {
                if (timestamp >= randomTimes[0][interval] + (randomTimes[1][interval] * 60000) && interval < randomRuntimes - 1) {
                    interval++;
                }
                tuple.addLong(timestamp); // Zeitstempel
                tuple.addString(name); // Gerätename (ID)
                tuple.addDouble(0); // Watt-Wert
                // /tuple.addDouble(0); //Wattmillisekunden-Wert
            }
        } else {
            tuple.addLong(timestamp); // Zeitstempel
            tuple.addString(name); // Gerätename (ID)
            tuple.addDouble(0); // Watt-Wert
            // /tuple.addDouble(0); //Wattmillisekunden-Wert
        }

        try {
            Thread.sleep(frequency / SimulationClock.getInstance().getSpeed());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<DataTuple> list = new ArrayList<DataTuple>();
        list.add(tuple);
        return list;
    }

    /*
     * Testabfrage, ob ein neuer Tag begonnen hat
     */
    private boolean newDay(long ts) {
        calendar.setTimeInMillis(ts);
        if (currentDay == calendar.get(Calendar.DAY_OF_MONTH)) {
            return false;
        }
        
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        randomRuntimes = getRandomNumber(rMin, rMax);
        if (getProbability(probability) == false) {
            randomRuntimes = 0;
        }
        if (randomRuntimes > 0) {
            randomTimes[1][0] = getRandomNumber(runtimeMin, runtimeMax);
            randomTimes[0][0] = getRandomStart(startMin, (((startMax - startMin) / randomRuntimes) + startMin));
            for (int i = 1; i < randomRuntimes; i++) {
                calendar.setTimeInMillis((randomTimes[0][i - 1] + (randomTimes[1][i - 1] * 60000)));
                int tmpStartMin = calendar.get(Calendar.HOUR_OF_DAY);
                randomTimes[0][i] = getRandomStart(tmpStartMin, (((startMax - tmpStartMin) / (randomRuntimes - i)) + tmpStartMin));
                randomTimes[1][i] = getRandomNumber(runtimeMin, runtimeMax);
            }
        }
        interval = 0;
        return true;

    }

    /*
     * Ermittlung des Watt-Wertes mit Hilfe einer Sinusfunktion
     */
    private double getMeteredValue(long ts) {
        double mTime = ts - randomTimes[0][interval];
        double a;
        double g;

        if (mTime >= 0 && mTime < startUpTime) {
            a = ((1 / startUpTime) * mTime);
            g = Math.sin(Math.PI / 2 * a);
            return g * watt;
        }
        
        g = iRange * Math.sin(Math.PI / (iLength) * (mTime / 1000)) + 1;
        return g * watt;
    }

    /*
     * Ermittlung des kWh-Wertes mit Hilfe einer Sinusfunktion
     */
    @SuppressWarnings("unused")
    private double getMeteredConsumption(long ts) {
        double mTime = ts - randomTimes[0][interval];
        double a;
        double g;
        double returnValue;
        long mIntervall;
        if (measureTimestamp > 0) {
            mIntervall = ts - measureTimestamp;
        } else {
            mIntervall = 0;
        }
        measureTimestamp = ts;

        if (mTime >= 0 && mTime < startUpTime) {
            a = ((1 / startUpTime) * mTime);
            g = Math.sin(Math.PI / 2 * a);
            returnValue = measuredWatt * mIntervall;
            measuredWatt = g * watt;
            return returnValue;
        } 
        g = iRange * Math.sin(Math.PI / (iLength) * (mTime / 1000)) + 1;
        returnValue = measuredWatt * mIntervall;
        measuredWatt = g * watt;
        return returnValue;
    }

    /*
     * Liefert eine Zufallszahl zwischen min, max
     */
    private static int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    /*
     * Ermittelt einen Startzeitstempel im übergebenen Stundenintervall min, max
     */
    private Long getRandomStart(int min, int max) {
        calendar.set(Calendar.HOUR_OF_DAY, min);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long startMin = calendar.getTimeInMillis();

        calendar.set(Calendar.HOUR_OF_DAY, max);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long startMax = calendar.getTimeInMillis();

        Random random = new Random();
        long start = (long) (random.nextDouble() * (startMax - startMin)) + startMin;

        return start;
    }

    /*
     * Bestimmt Start mit Hilfe einer übergebenen Wahrscheinlichekeit d
     */
    private static boolean getProbability(double d) {
        return Math.random() < d;
    }

    @Override
    public StreamClientHandler clone() {
        return new Appliance(this);
    }

}
