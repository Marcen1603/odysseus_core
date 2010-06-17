package de.uniol.inf.is.odysseus.scars.base;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class JDVEAccessMVPOAsList<M extends IProbability> extends JDVEAccessMVPO<M> {

	private SDFAttributeList outputSchema;
	
	public JDVEAccessMVPOAsList(int pPort) {
		super(pPort);
//		this.outputSchema = outputSchema;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return this.outputSchema;
	}

	@Override
	public void transferNext() {
		//Object[] transferData = buffer.toArray();
		Object[] complete = new Object[buffer.size()];
		//Alle Autos
		for (int j=0; j<buffer.size(); j++) {
			Object[] carObject = new Object[7];
			carObject[0] = buffer.get(j).getCarType();
			carObject[1] = buffer.get(j).getCarTrafficID();
			carObject[2] = buffer.get(j).getLaneID();
			Object[] positionsObject = new Object[6];
			//Alle Positions
			for (int i=0; i<6; i++) {
				positionsObject[i] = buffer.get(j).getPositionUTM()[i];
			}
			MVRelationalTuple<M> positionsTupel = new MVRelationalTuple<M>(positionsObject);
			carObject[3] = positionsTupel;
			carObject[4] = buffer.get(j).getVelocity();
			carObject[5] = buffer.get(j).getLength();
			carObject[6] = buffer.get(j).getWidth();
			MVRelationalTuple<M> carTupel = new MVRelationalTuple<M>(carObject);
			complete[j] = carTupel;
			System.out.println(buffer.get(j).getCarTrafficID() + ": " + buffer.get(j).getVelocity());
		}
		
		if (!buffer.isEmpty()) {
			transfer(new MVRelationalTuple<M>(complete));
		}
		
		buffer = null;
	}
}
