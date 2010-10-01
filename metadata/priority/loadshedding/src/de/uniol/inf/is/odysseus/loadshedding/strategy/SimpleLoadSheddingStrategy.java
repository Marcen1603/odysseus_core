package de.uniol.inf.is.odysseus.loadshedding.strategy;

import java.util.List;

import de.uniol.inf.is.odysseus.loadshedding.DirectLoadSheddingBuffer;
import de.uniol.inf.is.odysseus.loadshedding.ILoadSheddingStrategy;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;

public class SimpleLoadSheddingStrategy implements ILoadSheddingStrategy{
	
	public SimpleLoadSheddingStrategy() {
	}
	
	@Override
	public double calcCapacity(IPartialPlan plan) {
		// TODO Selektivitaet momentan immer als 1 angesetzt. Muss noch (wie Anzahl gelesener Elemente)
		// ueberwacht werden
		
		double processingSteps = 0;
		
		for(int i=0; i < plan.getIterableSource().size(); i++) {
			processingSteps += 1;
		}
		
		double result = 1/processingSteps;
		
		return 1/result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void activateLoadShedding(double percentToRemove, List<DirectLoadSheddingBuffer<?>> shedders) {
		// Moeglichst einfach: ignoriere prozentuale Anzahl+Gewichtung und aktiviere einfach erst einmal
		// fuer ein Element (d.h. einen Aufruf).
		for(DirectLoadSheddingBuffer each : shedders) {
			each.setRate(1);
		}

	}

	@Override
	public void deactivateLoadShedding(List<DirectLoadSheddingBuffer<?>> shedders) {
		for(DirectLoadSheddingBuffer<?> each : shedders) {
			each.setRate(DirectLoadSheddingBuffer.NO_LOAD_SHEDDING);
		}
	}	

}
