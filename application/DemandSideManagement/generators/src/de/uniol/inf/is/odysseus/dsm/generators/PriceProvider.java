package de.uniol.inf.is.odysseus.dsm.generators;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;

public class PriceProvider extends StreamClientHandler{
	
	private Calendar calendar = Calendar.getInstance();
	private int end;
	private int[][] priceModel;
	
	/*
	 * Übergabe der initialen Werte aus der Konfigurationsdatei
	 */
	public PriceProvider(int[][] priceModel){
		this.priceModel = priceModel;
	}
	
	private PriceProvider(PriceProvider priceProvider){
		this.priceModel = priceProvider.priceModel;
	}
	
	@Override
	public void init() {
		
	}
	
	@Override
    public void close() {
    	
	}
    
	@Override
    public List<DataTuple> next() {
		DataTuple tuple = new DataTuple();

		long timestamp = SimulationClock.getInstance().getTime(); //Abfrage der Simulationsuhrzeit
		
		int[] prices = getPrices(getHour(timestamp)); //Abfrage des Preismodells
		
		tuple.addLong(timestamp); //StartTimestamp
		tuple.addLong(getTimestamp(end, timestamp)); //EndTimestamp
		tuple.addInteger(prices[0]); //aktueller Preis
		tuple.addInteger(prices[1]); // nächster Preis
		

		try {
			Thread.sleep(1000/SimulationClock.getInstance().getSpeed());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<DataTuple> list = new ArrayList<DataTuple>();
		list.add(tuple);
		return list;
	}

	/*
	 * Bestimmt den Endzeitstempel des aktuellen Preisintervalls
	 */
	private Long getTimestamp(int hour, long timestamp) {
		calendar.setTimeInMillis(timestamp);
		
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		return calendar.getTimeInMillis();
	}

	/*
	 * Ermittlung des aktuellen und zukünftigen Preises
	 */
	private int[] getPrices(int hour) {
		int priceNow = 0;
		int priceNext = 0;
    	int[] prices = new int[2];
    	

    	for(int j=0; j<4; j++){
    		if(hour >= priceModel[j][0] && hour < priceModel[j][1]){
    			priceNow = priceModel[j][2];
    			end = priceModel[j][1];
    			if(j == 3){
    				priceNext = priceModel[0][2];
    			} else {
    				priceNext = priceModel[j+1][2];
    			}
    		}
    	}
    	
		prices[0] = priceNow;
    	prices[1] = priceNext;
    	
		return prices;
	}
	
	/*
	 * Ermittlung der aktuellen Stunde der Simulationsuhr
	 */

	private int getHour(long timestamp) {
		calendar.setTimeInMillis(timestamp);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	@Override
	public StreamClientHandler clone() {
		return new PriceProvider(this);
	}
	
}

