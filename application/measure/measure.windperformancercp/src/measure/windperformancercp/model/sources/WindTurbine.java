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
	
	private double hubHeight;
	private int powerControl;
	private double cutinWS;
	private double eightyFiveWS;
	
	/**
	 * Modelling of a wind turbine in data stream and power control context.
	 * Note: a new wt is always offline, t.m. not connected to the data stream system.
	 * @param name String for identification by application user.
	 * @param identifier The stream identifier.
	 * @param hostName The host name, e.g. localhost.
	 * @param portId The hosts port (between 1025 and 65532. oder so).
	 * @param attList The stream schema.
	 * @param hubHeight The hub height.
	 * @param powerControl The power control type (active/passive)
	 * @param cutin The cut in wind speed (m/s)
	 * @param etyFive The average wind speed to get 85 percent of the rated power.
	 * @param freq The frequency of the data stream in Hertz.
	 */
	public WindTurbine(String name, String identifier, String hostName, int portId, ArrayList<Attribute> attList, double hubHeight, int powerControl, double cutin, double etyFive, int freq){
		super(WTId, name, identifier, hostName, portId, attList, freq);
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
	
	/**
	 * Copy of an existing wind turbine.
	 * Note: a new wt is always offline, t.m. not connected to the data stream system.
	 * @param copy
	 */
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
	
	/**
	 * Sets the average windspeed, that is necessary to get 85 percent of the rated power of the wt.
	 * @param v windspeed in m/s
	 */
	public void setEightyFiveWS(double v){
		this.eightyFiveWS = v;
	}
	
	/**
	 * Returns the average windspeed, that is necessary to get 85 percent of the rated power of the wt.
	 * @return windspeed in m/s
	 */
	public double getEightyFiveWS(){
		return this.eightyFiveWS;
	}
	
	/**
	 * Sets the power control. 
	 * @param pc 0 = active (pitch), 1 = passive (stall)
	 */
	public void setPowerControl(int pc){
		this.powerControl = pc;
	}
	
	/**
	 * Returns the power control. 
	 * @param pc 0 = active (pitch), 1 = passive (stall)
	 */
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
	
	/**
	 * Returns whether two turbines are equal.
	 * NOTE: the name or connect state is not relevant in this case. 
	 * if a turbine has exact the same values as another, but the one is called Paul and the other Peter, then Paul equals Peter.
	 */
	@Override
	public boolean equals(Object obj){
		if(super.equals(obj)){	//in super it is checked whether the type is the same. So it must be a wind turbine.
			WindTurbine other = (WindTurbine) obj;
			if(Double.compare(this.cutinWS, other.getCutInWS())== 0){ //take care, they are doubles
				if(Double.compare(this.eightyFiveWS, other.getEightyFiveWS())== 0){	
					if(Double.compare(this.hubHeight, other.getHubHeight())== 0){
						if(this.powerControl == other.getPowerControl()){
							return true;
						}
					}
					
				}
			}
		}  
		return false;
	}
	
	@Override
	public int hashCode(){
		
		final int prime = 31;
		int result = 1;
		result = prime * result + super.hashCode();
		result = prime * result + (int) cutinWS; 
		result = prime * result + (int) eightyFiveWS;
		result = prime * result + (int) hubHeight;
		result = prime * result + powerControl;
		return result;
	}
}
