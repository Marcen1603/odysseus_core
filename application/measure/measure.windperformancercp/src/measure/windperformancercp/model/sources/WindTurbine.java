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
package measure.windperformancercp.model.sources;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "windTurbine", propOrder = {
    "hubHeight",
    "powerControl",
    "cutinWS",
    "eightyFiveWS"
})
@XmlRootElement
public class WindTurbine extends AbstractSource {
	//Power Control
	public static final int PC_ACTIVE = 0;
	public static final int PC_PASSIVE = 1;
	
	double hubHeight;
	int powerControl;
	double cutinWS;
	double eightyFiveWS;
	
	public WindTurbine(String name, String identifier, String hostName, int portId, ArrayList<Attribute> attList, double hubHeight, int powerControl, double cutin, double etyFive, int freq){
		super(WTId, name, identifier, hostName, portId, attList,0, freq);
		this.powerControl = powerControl;
		this.hubHeight = hubHeight;
		this.cutinWS = cutin;
		this.eightyFiveWS = etyFive;
			//System.out.println("WT Konstruktor: created new wind turbine: '"+this.toString());		
	}
	
	public WindTurbine(){
		super();
		this.powerControl = -1;
		this.hubHeight = 0.0;
		this.cutinWS = -1;
		this.eightyFiveWS = -1;
	}
	
	public WindTurbine(WindTurbine copy){
		this(copy.getName(),
			copy.getStreamIdentifier(),
			copy.getHost(),
			copy.getPort(),
			copy.getAttributeList(),
			copy.getHubHeight(),
			copy.getPowerControl(),
			copy.getCutInWS(),
			copy.getEightyFiveWS(),
			copy.getFrequency());
	}
	
	public void setHubHeight(double hh){
		this.hubHeight = hh;
	}
	
	public double getHubHeight(){
		return this.hubHeight;
	}
	
	public void setCutInWS(double v){
		this.cutinWS = v;
	}
	
	public double getCutInWS(){
		return this.cutinWS;
	}
	
	public void setEightyFiveWS(double v){
		this.eightyFiveWS = v;
	}
	
	public double getEightyFiveWS(){
		return this.eightyFiveWS;
	}
	
	public void setPowerControl(int pc){
		this.powerControl = pc;
	}
	
	public int getPowerControl(){
		return this.powerControl;
	}
	
	@Override
	public boolean isWindTurbine() {
		return true;
	}

	@Override
	public boolean isMetMast() {
		return false;
	}
}
