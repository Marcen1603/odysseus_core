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

package de.uniol.inf.is.odysseus.webservice;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.2.5
 * Tue Mar 30 15:25:18 CEST 2010
 * Generated source version: 2.2.5
 * 
 */

@WebFault(name = "fault", targetNamespace = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/")
public class StatementQueryFault extends Exception {
    public static final long serialVersionUID = 20100330152518L;
    
    private de.uniol.inf.is.odysseus.webservice.Fault fault;

    public StatementQueryFault() {
        super();
    }
    
    public StatementQueryFault(String message) {
        super(message);
    }
    
    public StatementQueryFault(String message, Throwable cause) {
        super(message, cause);
    }

    public StatementQueryFault(String message, de.uniol.inf.is.odysseus.webservice.Fault fault) {
        super(message);
        this.fault = fault;
    }

    public StatementQueryFault(String message, de.uniol.inf.is.odysseus.webservice.Fault fault, Throwable cause) {
        super(message, cause);
        this.fault = fault;
    }

    public de.uniol.inf.is.odysseus.webservice.Fault getFaultInfo() {
        return this.fault;
    }
}
