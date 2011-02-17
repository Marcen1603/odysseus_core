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
package de.uniol.inf.is.odysseus.jmxexampleoperator;

/**
 * Example of an operator configuration
 */
import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

/**
 *
 * @author Christian Kuka
 * @version 1.0
 */
public class Configuration extends StandardMBean implements ConfigurationMBean {

    /**
     * Processed tuples
     */
    private int processTuple;
    /**
     * State of the operator
     */
    private boolean state;

    public Configuration() throws NotCompliantMBeanException {
        super(ConfigurationMBean.class);
    }

    @Override
    public boolean stopProcessing() {
        this.setState(false);
        return this.getState();
    }

    @Override
    public boolean startProcessing() {
        this.setState(true);
        return this.getState();
    }

    @Override
    public int getProcessedTuple() {
        return processTuple;
    }

    /**
     * Set total number of processed tuples
     * @param processTuple Processed tuples
     */
    public void setProcessTuple(int processTuple) {
        this.processTuple = processTuple;
    }

    /**
     * Increment the number of process tuple
     */
    public void incProcessTuple() {
        this.processTuple++;
    }

    /**
     * Set the state of an operator
     * @param state The state (on/off)
     */
    public void setState(boolean state) {
        this.state = state;
    }

    /**
     * Get the state of an operator
     * @return The state of the operator
     */
    public boolean getState() {
        return state;
    }
}
