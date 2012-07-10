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
package de.uniol.inf.is.odysseus.costmodel.operator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;

/**
 * Singleton-Klasse zur Konfiguration des Kostenmodells nach Operatoreigenschaften.
 * Verwendet {@link OdysseusConfiguration}.
 * 
 * @author Timo Michelsen
 *
 */
public class OperatorCostModelCfg {

	private static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(OperatorCostModelCfg.class);
		}
		return _logger;
	}

	private static OperatorCostModelCfg instance;

	/**
	 * Liefert die einzige Instanz dieser Klasse.
	 * 
	 * @return Instanz der Klasse
	 */
	public static OperatorCostModelCfg getInstance() {
		if (instance == null)
			instance = new OperatorCostModelCfg();
		return instance;
	}

	private static final double STD_MEM_COST = 4;
	private static final double STD_CPU_COST = 0.00005;
	private static final double MEM_HEADROOM = 0.4;
	private static final double CPU_HEADROOM = 0.4;
	
	private double stdMemCost;
	private double stdCpuCost;
	private double memHeadroom;
	private double cpuHeadroom;
	
	// Privater Konstruktor
	// Ermittelt alle Einstellungen. Falls nicht vorhanden,
	// werden Standardwerte gesetzt.
	private OperatorCostModelCfg() {
		stdMemCost = toDouble( OdysseusConfiguration.get("ac_standardMemCost"), STD_MEM_COST);
		stdCpuCost = toDouble( OdysseusConfiguration.get("ac_standardCpuCost"), STD_CPU_COST);
		memHeadroom = toDouble( OdysseusConfiguration.get("ac_memHeadroom"), MEM_HEADROOM);
		cpuHeadroom = toDouble( OdysseusConfiguration.get("ac_cpuHeadroom"), CPU_HEADROOM);
		
		// check cfg
		if( stdMemCost <= 0.0 ) {
			getLogger().error("Standard Memory Cost is invalid: " + stdMemCost + ", it must be positive. Setting to default of " + STD_MEM_COST);
			stdMemCost = STD_MEM_COST;
		}
		// check cfg
		if( stdCpuCost <= 0.0 ) {
			getLogger().error("Standard Processor Cost is invalid: " + stdCpuCost + ", it must be positive. Setting to default of " + STD_CPU_COST);
			stdMemCost = STD_CPU_COST;
		}
		if( memHeadroom <= 0.0 || memHeadroom > 1.0 ) {
			getLogger().error("Headroom of Memory Cost is invalid: " + memHeadroom + ", it must be between 0 and 1. Setting to default of " + MEM_HEADROOM);
			stdMemCost = MEM_HEADROOM;
		}
		if( cpuHeadroom <= 0.0 || cpuHeadroom > 1.0 ) {
			getLogger().error("Headroom of Processor Cost is invalid: " + cpuHeadroom + ", it must be between 0 and 1. Setting to default of " + CPU_HEADROOM);
			stdMemCost = CPU_HEADROOM;
		}		
	}
	
	/**
	 * Liefert den Standardwert für Speicherkosten
	 * 
	 * @return Standardwert für Speicherkosten
	 */
	public double getStandardMemCost() {
		return stdMemCost;
	}
	
	/**
	 * Liefert den Standardwert für Prozessorkosten
	 * 
	 * @return Standardwert für Prozessorkosten
	 */
	public double getStandardCpuCost() {
		return stdCpuCost;
	}
	
	/**
	 * Liefert den Headroom bzgl. Speicherkosten
	 * 
	 * @return Headroom bzgl. Speicherkosten
	 */
	public double getMemHeadroom() {
		return memHeadroom;
	}
	
	/**
	 * Liefert den Headroom bzgl. Prozessorkosten
	 * 
	 * @return Headroom bzgl Prozessorkosten
	 */
	public double getCpuHeadroom() {
		return cpuHeadroom;
	}

	// wandelt einen String in ein Double um, falls möglich
	private static Double toDouble(String value, double std) {
		try {
			if (value == null)
				return std;
			return Double.valueOf(value);
		} catch (Exception ex) {
			return std;
		}
	}
}
