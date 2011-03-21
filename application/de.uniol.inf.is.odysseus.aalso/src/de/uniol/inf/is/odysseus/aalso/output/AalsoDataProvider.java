package de.uniol.inf.is.odysseus.aalso.output;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.aalso.exceptions.PlaceTypeUndefinedException;
import de.uniol.inf.is.odysseus.aalso.model.Place;
import de.uniol.inf.is.odysseus.aalso.model.World;
import de.uniol.inf.is.odysseus.aalso.types.BooleanType;
import de.uniol.inf.is.odysseus.aalso.types.FloatNumber;
import de.uniol.inf.is.odysseus.aalso.types.IntegerNumber;
import de.uniol.inf.is.odysseus.aalso.types.Publishable;

public class AalsoDataProvider extends StreamClientHandler {

	/**
	 * The <code>World</code> we have to print about.
	 */
	private World world;
	private String sensorObjectReferenceName = "";
	private int frequency;

	public AalsoDataProvider(final World world, String sensorObjectReferenceName) throws Exception {
		this.setWorld(world);
		this.setSensorObjectReferenceName(sensorObjectReferenceName);
		this.setFrequencyFromSensorObject();
	}

	/**
	 * Updates Tuple and provides sensordata
	 */
	@Override
	public List<DataTuple> next() {
		DataTuple sensorData = new DataTuple();
		sensorData.addAttribute(world.getTime().getTime().getTime());
		Place place;
		try {
			place = world.getPlacesOfType(this.getSensorObjectReferenceName()).iterator().next();
			Collection<Publishable> infofields = place.getInfoValues();
			if (!infofields.isEmpty()) {
				for (String infoKey : place.getInfoKeys()) {
					Publishable info = place.get(infoKey);
					if (info == null) {
						throw new RuntimeException("You can't have null values in the Place's info if you are using a CSVPrinter");
					} else {
						String infoFlattened = info.flatten().toString();
						String[] infoSplitted = infoFlattened.split(":");
						if(infoSplitted[0].equals("FloatNumber")){
							sensorData.addAttribute(new Double(((FloatNumber) info).getNumber()));
						} else if(infoSplitted[0].equals("IntegerNumber")){
							sensorData.addAttribute(new Integer(((IntegerNumber) info).getNumber()));
						} else if(infoSplitted[0].equals("BooleanType")){
							sensorData.addAttribute(new Boolean(((BooleanType) info).getValue()));
						} else if(infoSplitted[0].equals("Text")){
							if(!infoKey.equals("frequency")){
								sensorData.addAttribute(new String(info.flatten().getData()));
							}
						} else {
							sensorData.addAttribute(new String(info.flatten().getData()));
						}
					}
				}
			}
		} catch (PlaceTypeUndefinedException e1) {
			e1.printStackTrace();
		}
		// sensorData.addAttribute("test " + new Random().nextInt());
		try {
			Thread.sleep(this.getFrequency());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<DataTuple> list = new ArrayList<DataTuple>();
		list.add(sensorData);
		return list;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	public void setFrequencyFromSensorObject() {
		Place place;
		try {
			place = world.getPlacesOfType(this.getSensorObjectReferenceName()).iterator().next();
			Collection<Publishable> infofields = place.getInfoValues();
			if (!infofields.isEmpty()) {
				for (String infoKey : place.getInfoKeys()) {
					Publishable info = place.get(infoKey);
					if (info == null) {
						throw new RuntimeException("You can't have null values in the Place's info if you are using a CSVPrinter");
					} else {
						String infoFlattened = info.flatten().toString();
						String[] infoSplitted = infoFlattened.split(":");
						if(infoSplitted[0].equals("IntegerNumber")){
							if(infoKey.equals("frequency")){
								this.setFrequency(((IntegerNumber) info).getNumber());
							}
						}
					}
				}
			}
		} catch (PlaceTypeUndefinedException e1) {
			e1.printStackTrace();
		}
	}
	
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public int getFrequency() {
		return frequency;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}

	public World getWorld() {
		return world;
	}

	public void setSensorObjectReferenceName(String sensorObjectReferenceName) {
		this.sensorObjectReferenceName = sensorObjectReferenceName;
	}

	public String getSensorObjectReferenceName() {
		return sensorObjectReferenceName;
	}
}
