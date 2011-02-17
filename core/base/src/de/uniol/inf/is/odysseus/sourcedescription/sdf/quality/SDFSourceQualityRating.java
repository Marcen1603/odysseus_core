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

import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;

public class SDFSourceQualityRating {

    /**
	 * @uml.property  name="source"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    SDFSource source = null;

    /**
	 * @uml.property  name="qualityAspect"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    SDFQuality qualityAspect = null;

    /**
	 * @uml.property  name="qualityLevel"
	 */
    double qualityLevel = -1;


	public SDFSourceQualityRating(SDFSource source, SDFQuality qualityAspect,
			double qualityLevel) {

		this.source = source;
		this.qualityAspect = qualityAspect;
		this.qualityLevel = qualityLevel;
	}

    /**
     * 
     * @uml.property name="source"
     */
    public void setSource(SDFSource source) {
        this.source = source;
    }

    /**
     * 
     * @uml.property name="source"
     */
    public SDFSource getSource() {
        return source;
    }

    /**
     * 
     * @uml.property name="qualityAspect"
     */
    public void setQualityAspect(SDFQuality qualityAspect) {
        this.qualityAspect = qualityAspect;
    }

    /**
     * 
     * @uml.property name="qualityAspect"
     */
    public SDFQuality getQualityAspect() {
        return qualityAspect;
    }

    /**
     * 
     * @uml.property name="qualityLevel"
     */
    public void setQualityLevel(double qualityLevel) {
        this.qualityLevel = qualityLevel;
    }

    /**
     * 
     * @uml.property name="qualityLevel"
     */
    public double getQualityLevel() {
        return qualityLevel;
    }

	@Override
	public String toString() {
		return "(" + source.toString() + ", " + qualityAspect.toString() + ", "
				+ qualityLevel + ")";
	}

}