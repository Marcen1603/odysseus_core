package de.uniol.inf.is.odysseus.scheduler.strategy.factory.chainfactory.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.base.FESortedPair;
import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.scheduler.strategy.AbstractExecListSchedulingStrategy;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;

public class ChainStrategy extends AbstractExecListSchedulingStrategy {

	public ChainStrategy(IPartialPlan plan) {
		super(plan);
	}
	
	@Override
	protected List<IIterableSource<?>> calculateExecutionList(
			IPartialPlan plan) {

		// Calc for every leaf (!) operator the path to the root (inkl. virtual operators)
		Map<IIterableSource<?>, List<ISource<?>>> virtualOps = new HashMap<IIterableSource<?>, List<ISource<?>>>();
		List<List<IIterableSource<?>>> pathes = new ArrayList<List<IIterableSource<?>>>();
		calcForLeafsPathsToRoots(plan.getRoots(), virtualOps, pathes);
		
		Map<List<IIterableSource<?>>, OperatorPoint[]> progressCharts = new HashMap<List<IIterableSource<?>>, OperatorPoint[]>();

		// Für alle Operatorpfade im System Berechnung durchführen
		calculateProgressCharts(pathes, virtualOps, progressCharts);
		// Lower Envelopes für alle Operatorpfade im System berechnen
		calculateLowerEnvelopes(pathes, progressCharts);
		
		// Jetzt alle Pfade nach der Steigung sortieren
		PriorityQueue<FESortedPair<OperatorPoint, IIterableSource<?>>> opPointList = new PriorityQueue<FESortedPair<OperatorPoint, IIterableSource<?>>>();
		// Dazu zunächst sammeln
		for (Entry<List<IIterableSource<?>>, OperatorPoint[]> e : progressCharts.entrySet()) {
			List<IIterableSource<?>> sources = e.getKey();
			Iterator<IIterableSource<?>> sIter = sources.iterator();
			OperatorPoint[] points = e.getValue();
			// Es gibt mehr Punkte als Operatoren! Der letzte Punkt kann immer ignoriert werden
			for (int i=0;i<points.length-1; i++){
				if (sIter.hasNext()){
					IIterableSource<?> v = sIter.next();
					opPointList.add(new FESortedPair<OperatorPoint, IIterableSource<?>>(
							points[i], v));
				}
			}
		}
				
		List<IIterableSource<?>> execList = new LinkedList<IIterableSource<?>>();
		for (FESortedPair<OperatorPoint, IIterableSource<?>> p : opPointList) {
			execList.add(p.getE2());
		}
		return execList;
	}

	private OperatorPoint calcVirtOpCosts(OperatorPoint lastPoint, List<ISource<?>> p) {
		double t = lastPoint.t;
		double s = lastPoint.s;
		if (p != null) {
			// Die Summe der Kosten des virtuellen Operators berechnen!!
			for (int i = 0; i < p.size(); i++) {
				IMonitoringData<Double> c = p.get(i).getMonitoringData(
						MonitoringDataTypes.ESTIMATED_PROCESSING_COST.name);
				t = t + ((Number) c.getValue()).doubleValue();
				IMonitoringData<Double> sel = p.get(i).getMonitoringData(
						MonitoringDataTypes.ESTIMATED_PROCESSING_COST.name);
				s = s * ((Number) sel.getValue()).doubleValue();
			}
		}
		OperatorPoint ret = new OperatorPoint(t, s);
		return ret;
	}
	
	private void calculateProgressCharts(List<List<IIterableSource<?>>> pathes,
			Map<IIterableSource<?>, List<ISource<?>>> virtualOps,
			Map<List<IIterableSource<?>>, OperatorPoint[]> progressCharts) {

		// Berechnung für jeden Pfad durchführen
		for (List<IIterableSource<?>> schePath : pathes) {

			// Das Ablaufdiagramm eines Operatorpfades mit m Operatoren
			// hat m+1 Operatorpunkte.
			OperatorPoint[] points = new OperatorPoint[schePath.size() + 1];

			// Der erste Operatorpunkt ist (t,s) = (0,1)
			points[0] = new OperatorPoint(0, 1);

			// Die weiteren Operatorpunkte (bis auf den letzten) haben folgende
			// Koordinaten:
			// t = t des Vorgängers + Kosten des Operators
			// s = s des Vorgängers * Selektivität des Operators
			for (int j = 1; j < schePath.size(); j++) {
				List<ISource<?>> p = virtualOps.get(schePath.get(j - 1));
				points[j] = calcVirtOpCosts(points[j - 1], p);

			}

			// Der letzte Operatorpunkt hat folgende Koordinaten:
			// t = t des Vorgängers + Kosten des Operators
			// s = 0 (da Ergebnisse der Wurzel aus System ausgegeben werden)

			points[schePath.size()] = calcVirtOpCosts(
					points[schePath.size() - 1], virtualOps
							.get(schePath.size() - 1));
			points[schePath.size()].s = 0;

			// Ablaufdiagramm abspeichern
			progressCharts.put(schePath, points);
		}
	}

	private void calculateLowerEnvelopes(List<List<IIterableSource<?>>> pathes,
			Map<List<IIterableSource<?>>, OperatorPoint[]> progressCharts) {
		
		// Anm. MG: Wo hier die Hülle berechnet wird, ist mir noch nicht ganz klar ...

		// Berechnung für jeden Pfad durchführen
		for (List<IIterableSource<?>> schePath : pathes) {

			// Ablaufdiagramm holen
			OperatorPoint[] progressChart = progressCharts.get(schePath);

			// Problem sind Puffer ganz oben --> t==0;
			if (progressChart.length == 2 && progressChart[1].t == 0.0) {
				// Ein Operator der nichts kostet sollte auf jeden Fall die
				// höchste Prio bekommen ;-)
				progressChart[0].d = Double.MAX_VALUE;

			} else {

				// Für jeden Operatorpunkt durchführen
				for (int j = 0; j < progressChart.length;) {

					// Operatorpunkt holen
					OperatorPoint point = progressChart[j];
					// Steilste Steigung
					double maxDerivation = Double.MIN_VALUE;
					// Operatorpunkt für den die steilste Steigung erreicht
					// wurde (SDOP)
					int maxDerivationPoint = -1;

					// Steilste Steigung zu einem nachfolgenden Operatorpunkt
					// berechnen
					for (int k = j + 1; k < progressChart.length; k++) {

						// Ziel-Operatorpunkt holen
						OperatorPoint nextPoint = progressChart[k];

						// Steigung
						double derivation = (-(nextPoint.s - point.s))
								/ (nextPoint.t - point.t);

						// Wenn Steigung steiler, dann zwischenspeichern
						if (derivation > maxDerivation) {
							maxDerivation = derivation;
							maxDerivationPoint = k;
						}

					}

					// Alle Punkt bis zu dem SDOP liegen auf einem Segment.
					// Daher die errechnete Steigung (die später die Priorität
					// wiederspiegelt) für alle Teilstrecken des Segments setzen.
					// Die Berechnung weiterer Segmente startet dann wieder bei SDOP.
					while (j < maxDerivationPoint) {
						progressChart[j].d = maxDerivation;
						j++;
					}

					// Wenn letzten Punkt erreicht, dann dort auch noch die Steigung
					// des Segments setzen zu dem er gehört und noch einmal Zählvariable
					// inkrementieren, damit die for-Schleife beendet wird.
					if (j == progressChart.length - 1) {
						progressChart[j].d = maxDerivation;
						j++;
					}
				}
			}
		}
	}
}

/**
 * Die Klasse repräsentiert einen Operatorpunkt in einem Ablaufdiagramm.
 * 
 * @author Andreas Ziesenitz
 * 
 */
class OperatorPoint implements Comparable<OperatorPoint> {

	public double t;
	public double s;
	public double d;

	public OperatorPoint(double t, double s) {
		this.t = t;
		this.s = s;
	}

	@Override
	/*
	 * Ein element ist besser (also weiter vorne), wenn es ein höhreres d hat!
	 */
	public int compareTo(OperatorPoint o) {
		if (this.d == o.d)
			return 0;
		if (this.d > o.d)
			return -1;
		return 1;
	}

	@Override
	public String toString() {
		return "t=" + t + " s=" + s + " d=" + d;
	}

}
