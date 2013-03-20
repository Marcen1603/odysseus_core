package de.uniol.inf.is.odysseus.p2p_new.distribute.model;

import java.util.List;

import de.uniol.inf.is.odysseus.core.distribution.ILogicalQueryDistributor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;

public class ModelBasedDistributor implements ILogicalQueryDistributor {

	private static final String DISTRIBUTION_TYPE = "model";

	// called by OSGi-DS
	public final void activate() {
		
	}

	// called by OSGi-DS
	public final void deactivate() {
		
	}

	@Override
	public List<ILogicalQuery> distributeLogicalQueries(IExecutor sender, List<ILogicalQuery> queriesToDistribute) {
		
		// Anfragen ausschreiben
			// Jede Anfrage eigene Ausschreibung (Advertisement)
		
		// Gebote abwarten
			// asynchroner Vorgang
			// was passiert dann hier?
			// werden Anfragen lokal ausgeführt?
		
		// Gebote sammeln
		
		// Bestimmte Zeit verstreichen lassen
			// Während dieser Zeit können Gebote abgegeben werden
		
		// Gebotsphase beendet
			// Danach eintreffende Gebote werden abgelehnt/ignoriert
		
		// Sichtung der Gebote
			// Können mit allen Geboten alle Operatoren einer Anfrage platziert werden?
			// Was passiert bei Überlappungen?
				// Überlappungen im Falle der Replikation erwünscht (hier zunächst nicht weiter betrachtet)
			// Welche Gebote sind "gut"?
			// NP-Problem?
		
		// Zuordnung, welches Gebot zu welchem Operator "stattgegeben" wird
			// Gebote wenn möglich nicht verändern
				// Gebotswert basiert auf nicht bekannten Kostenfunktionen der Anbieter
				// I. d. R. werden die Operatoren mit einbezogen
			// Operatoren nachträglich hinzufügen nicht erwünscht (Autonomie)
				// Kann u. U. die Bedingungen für den Anbieter drastisch ändern
			// Operatoren aus Geboten entfernen
				// Auflösung von Überlappungen
				// Auf den ersten Blick ungefährlicher als das Hinzufügen von Anfragen
					// Autonomiefrage stellt sich trotzdem
			// Welche Bedingungen müssen alle Gebote zusammen erfüllen, um eine Erfolgreiche Zuordnung zu gewährleisten?
		
		// Nach Zuordnung
			// Anbieter, welche den Zuschlag erhalten haben, informieren
			// Anfragen werden zerlegt und verteilt (über PQL)
				// Spezielle Konfiguration der Sender- und Access-Operatoren
				// Wie bei manueller Zuordnung
		
		// Ausführung 
			// ...
		
		return queriesToDistribute;
	}

	@Override
	public String getName() {
		return DISTRIBUTION_TYPE;
	}

}
