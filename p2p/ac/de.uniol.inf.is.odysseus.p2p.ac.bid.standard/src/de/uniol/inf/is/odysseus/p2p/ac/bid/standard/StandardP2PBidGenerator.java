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
package de.uniol.inf.is.odysseus.p2p.ac.bid.standard;

import de.uniol.inf.is.odysseus.core.server.ac.IAdmissionControl;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCost;
import de.uniol.inf.is.odysseus.p2p.ac.bid.IP2PBidGenerator;

/**
 * Standardimplementierung der Gebotsgenerieren. Das Gebot wird aus der
 * Kombination aus Prozessorkosten und Speicherkosten gebildet. Dabei werden die
 * potenziellen Kosten des neuen Teilplans mit einbezogen. Je höher also das
 * Gebot, desto schlechter. Dementsprechend muss die Implementierung von
 * IP2PBidSelector aufgebaut sein. Dieser Generator kann nur mit dem
 * Kostenmodell nach Operatoreigenschaften eingesetzt werden. Absonsten wird als
 * Gebot immer 1 zurückgegeben.
 * 
 * @author Timo Michelsen
 * 
 */
public class StandardP2PBidGenerator implements IP2PBidGenerator {

    /**
     * Liefert ein Gebot zu einem Teilplan. Dabei werden die aktuellen Kosten
     * des Ausführungsplans mit den neuen Kosten des Teilplans addiert.
     * Anschließend werden die Komponenten (Speicherkosten und Prozessorkosten)
     * addiert. Das Ergebnis ist das Gebot.
     */
    @Override
    public double generateBid(IAdmissionControl sender, ICost actSystemLoad, ICost queryCost, ICost maxCost) {
        if (!(actSystemLoad instanceof OperatorCost)) {
            return 1;
        }

        Runtime runtime = Runtime.getRuntime();

        // calculate potencial system load
        OperatorCost actSystemLoad2 = (OperatorCost) actSystemLoad;
        OperatorCost queryCost2 = (OperatorCost) queryCost;
        double cpu = actSystemLoad2.getCpuCost() + queryCost2.getCpuCost();
        double memFactor = (actSystemLoad2.getMemCost() + queryCost2.getMemCost()) / runtime.totalMemory();

        // higher cost --> higher bid
        return cpu + memFactor;
    }

}
