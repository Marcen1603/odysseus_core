/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.sourcedescription.sdf.quality;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFIntervall;

/**
 * Diese Klasse dient dazu fuer ein Qualitaetskriterium Einordnungen von
 * Auspraegungen bezueglich der Einteilung "sehr gut" bis "sehr schlecht" zu
 * spezifizieren
 */

public class SDFQualityNormalization {

    /**
	 * @uml.property  name="qualityElement"
	 * @uml.associationEnd  
	 */
    private SDFQuality qualityElement;

    /**
	 * @uml.property  name="veryGood"
	 * @uml.associationEnd  
	 */
    private SDFIntervall veryGood = null;

    /**
	 * @uml.property  name="good"
	 * @uml.associationEnd  
	 */
    private SDFIntervall good = null;

    /**
	 * @uml.property  name="medium"
	 * @uml.associationEnd  
	 */
    private SDFIntervall medium = null;

    /**
	 * @uml.property  name="bad"
	 * @uml.associationEnd  
	 */
    private SDFIntervall bad = null;

    /**
	 * @uml.property  name="veryBad"
	 * @uml.associationEnd  
	 */
    private SDFIntervall veryBad = null;

    /**
     * 
     * @uml.property name="qualityElement"
     */
    public void setQualityElement(SDFQuality qualityElement) {
        this.qualityElement = qualityElement;
    }

    /**
     * 
     * @uml.property name="qualityElement"
     */
    public SDFQuality getQualityElement() {
        return qualityElement;
    }

    /**
     * 
     * @uml.property name="veryGood"
     */
    public void setVeryGood(SDFIntervall veryGood) {
        this.veryGood = veryGood;
    }

    /**
     * 
     * @uml.property name="veryGood"
     */
    public SDFIntervall getVeryGood() {
        return veryGood;
    }

    /**
     * 
     * @uml.property name="good"
     */
    public void setGood(SDFIntervall good) {
        this.good = good;
    }

    /**
     * 
     * @uml.property name="good"
     */
    public SDFIntervall getGood() {
        return good;
    }

    /**
     * 
     * @uml.property name="medium"
     */
    public void setMedium(SDFIntervall medium) {
        this.medium = medium;
    }

    /**
     * 
     * @uml.property name="medium"
     */
    public SDFIntervall getMedium() {
        return medium;
    }

    /**
     * 
     * @uml.property name="bad"
     */
    public void setBad(SDFIntervall bad) {
        this.bad = bad;
    }

    /**
     * 
     * @uml.property name="bad"
     */
    public SDFIntervall getBad() {
        return bad;
    }

    /**
     * 
     * @uml.property name="veryBad"
     */
    public void setVeryBad(SDFIntervall veryBad) {
        this.veryBad = veryBad;
    }

    /**
     * 
     * @uml.property name="veryBad"
     */
    public SDFIntervall getVeryBad() {
        return veryBad;
    }

	@Override
	public String toString() {
		return "(" + qualityElement + ", " + veryGood.toString() + ","
				+ good.toString() + "," + medium.toString() + ", "
				+ bad.toString() + ", " + veryBad.toString() + ")";
	}

}