package de.uniol.inf.is.odysseus.p2p_new.distribute.model;

import java.util.List;

import de.uniol.inf.is.odysseus.core.distribution.ILogicalQueryDistributor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;

public class ModelBasedDistributor implements ILogicalQueryDistributor {

	private static final String DISTRIBUTION_TYPE = "auto";

	// called by OSGi-DS
	public final void activate() {

	}

	// called by OSGi-DS
	public final void deactivate() {

	}

	@Override
	public List<ILogicalQuery> distributeLogicalQueries(IExecutor sender, List<ILogicalQuery> queriesToDistribute, String transCfgName) {

		// Anfragen ausschreiben
		// Jede Anfrage eigene Ausschreibung (Advertisement)

		// Gebote abwarten
		// asynchroner Vorgang
		// was passiert dann hier?
		// werden Anfragen lokal ausgef�hrt?

		// Gebote sammeln

		// Bestimmte Zeit verstreichen lassen
		// W�hrend dieser Zeit k�nnen Gebote abgegeben werden

		// Gebotsphase beendet
		// Danach eintreffende Gebote werden abgelehnt/ignoriert

		// Sichtung der Gebote
		// K�nnen mit allen Geboten alle Operatoren einer Anfrage platziert
		// werden?
		// Was passiert bei �berlappungen?
		// �berlappungen im Falle der Replikation erw�nscht (hier zun�chst nicht
		// weiter betrachtet)
		// Welche Gebote sind "gut"?
		// NP-Problem?

		// Zuordnung, welches Gebot zu welchem Operator "stattgegeben" wird
		// Gebote wenn m�glich nicht ver�ndern
		// Gebotswert basiert auf nicht bekannten Kostenfunktionen der Anbieter
		// I. d. R. werden die Operatoren mit einbezogen
		// Operatoren nachtr�glich hinzuf�gen nicht erw�nscht (Autonomie)
		// Kann u. U. die Bedingungen f�r den Anbieter drastisch �ndern
		// Operatoren aus Geboten entfernen
		// Aufl�sung von �berlappungen
		// Auf den ersten Blick ungef�hrlicher als das Hinzuf�gen von Anfragen
		// Autonomiefrage stellt sich trotzdem
		// Welche Bedingungen m�ssen alle Gebote zusammen erf�llen, um eine
		// Erfolgreiche Zuordnung zu gew�hrleisten?

		// Nach Zuordnung
		// Anbieter, welche den Zuschlag erhalten haben, informieren
		// Anfragen werden zerlegt und verteilt (�ber PQL)
		// Spezielle Konfiguration der Sender- und Access-Operatoren
		// Wie bei manueller Zuordnung

		// Ausf�hrung
		// ...

		return queriesToDistribute;
	}

	@Override
	public String getName() {
		return DISTRIBUTION_TYPE;
	}

}
