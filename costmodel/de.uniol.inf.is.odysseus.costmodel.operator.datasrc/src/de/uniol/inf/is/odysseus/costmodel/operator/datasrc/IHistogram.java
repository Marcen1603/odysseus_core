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
package de.uniol.inf.is.odysseus.costmodel.operator.datasrc;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

/**
 * Repräsentiert ein Histogramm.
 * 
 * @author Timo Michelsen
 * 
 */
public interface IHistogram extends Cloneable {

	/**
	 * Liefert das Attribut zum Histogramm
	 * 
	 * @return Attribut zum Histogramm
	 */
	public SDFAttribute getAttribute();

	/**
	 * Setzt das Attribut, welches zum Histogramm gehören soll.
	 * 
	 * @param attribute
	 *            Attribut
	 */
	public void setAttribute(SDFAttribute attribute);

	/**
	 * Liefert den minimalen Attributwert
	 * 
	 * @return Minimaler Attributwert
	 */
	public double getMinimum();

	/**
	 * Liefert den maximalen Attributwert
	 * 
	 * @return Maximaler Attributwert
	 */
	public double getMaximum();

	/**
	 * Liefert die Häufigkeit zu einem gegebenen Attributwert.
	 * 
	 * @param value
	 *            Attributwert
	 * @return Häufigkeit
	 */
	public double getOccurences(double value);

	/**
	 * Liefert die Häufigkeit zu einem Attributwertebereich
	 * 
	 * @param from
	 *            Minimaler Wert des Attributwertebereichs
	 * @param to
	 *            Maximaler Wert des Attributwertebereichs
	 * @return Summe der Häufigkeiten im Attributwertebereich
	 */
	public double getOccurenceRange(double from, double to);

	/**
	 * Liefert die Anzahl aller Attributwerte (d.h. die Summe aller
	 * Häufigkeiten)
	 * 
	 * @return Anzahl der Attributwerte (Summe der Häufigkeiten)
	 */
	public double getValueCount();

	/**
	 * Liefert die Zahl der Intervalle
	 * 
	 * @return Zahl der Intervalle
	 */
	public int getIntervalCount();

	/**
	 * Liefert die Intervallgrenzen
	 * 
	 * @return Intervallgrenzen
	 */
	public double[] getIntervalBorders();

	/**
	 * Liefert eine Liste aller Häufigkeiten
	 * 
	 * @return Liste aller Häufigkeiten
	 */
	public double[] getAllOccurences();

	/**
	 * Erstellt eine Kopie des Histogramms als neue Instanz
	 * 
	 * @return Neue Instanz des gleichen Histogramms
	 */
	public IHistogram clone();

	/**
	 * Setzt alle Häufigkeiten unterhalb des gegebenen Attributwertes auf 0.
	 * Evtl. werden anteilig Häufigkeiten verringert.
	 * 
	 * @param value
	 *            Attributwert als obere Grenze der Kürzung
	 * @return Verändertes Histogramm
	 */
	public IHistogram cutLower(double value);

	/**
	 * Setzt alle Häufigkeiten oberhalb des gegebenen Attributwertes auf 0.
	 * Evtl. werden anteilig Häufigkeiten verringert.
	 * 
	 * @param value
	 *            Attributwert als untere Grenze des Kürzung
	 * @return Verändertes Histogramm
	 */
	public IHistogram cutHigher(double value);

	/**
	 * Erstellt ein relatives Histogramm.
	 * 
	 * @return Relatives Histogramm
	 */
	public IHistogram toRelative();

	/**
	 * Erstellt ein absolutes Histogramm mit der gegebenen Summe aller
	 * Häufigkeiten
	 * 
	 * @param countNum
	 *            Neue Summe aller Häufigkeiten
	 * @return Absolutes Histogramm
	 */
	public IHistogram toAbsolute(double countNum);

	/**
	 * Gibt zurück, ob das Histogramm relativ ist.
	 * 
	 * @return <code>true</code>, falls das Histogramm relativ ist, sonst
	 *         <code>false</code>
	 */
	public boolean isRelative();

	/**
	 * Normalisiert das Histogramm. Nur für relative Histogramme verfügbar. Die
	 * relativen Häufigkeiten werden so angepasst, dass ihre Summe 1 ergibt.
	 * 
	 * @return Angepasstes relatives Histogramm, dessen Summe 1 ergibt.
	 */
	public IHistogram normalize();

	/**
	 * Setzt die Häufigkeit eines Intervalls auf den gegebenen Wert
	 * 
	 * @param intervalIndex
	 *            Index des Intervalls, dessen Häufigkeit gesetzt werden soll
	 * @param occs
	 *            Neue Häufigkeit des Intervalls
	 */
	public void setOccurences(int intervalIndex, double occs);
}
