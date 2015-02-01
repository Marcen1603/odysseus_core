package de.uniol.inf.is.odysseus.sports.sportsql.physicaloperator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.fasterxml.jackson.dataformat.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ddcaccess.AbstractSportsDDCAccess;

@SuppressWarnings("rawtypes")
public class SportsHeatMapPO<T extends Tuple<?>> extends AbstractPipe<T, Tuple> {

	// Heatmap
	private SportsHeatMap sportsHeatmap = new SportsHeatMap();
	private int numTilesX = 15;
	private int numTilesY = 15;

	private int minX;
	private int minY;

	int pixelsPerSquareHorizontal = 100;
	int pixelsPerSquareVertical = 100;

	int reduceSendLoadCounter = 0;

	public SportsHeatMapPO() {
		try {
			minX = (int) AbstractSportsDDCAccess.getFieldXMin();
			minY = (int) AbstractSportsDDCAccess.getFieldYMin();
			pixelsPerSquareHorizontal = (int) (Math.abs(AbstractSportsDDCAccess.getFieldXMax()
					- AbstractSportsDDCAccess.getFieldXMin()) / numTilesX);
			pixelsPerSquareVertical = (int) (Math.abs(AbstractSportsDDCAccess.getFieldYMax()
					- AbstractSportsDDCAccess.getFieldYMin()) / numTilesY);
		} catch (NumberFormatException | MissingDDCEntryException e) {
			e.printStackTrace();
		}
	}

	public SportsHeatMapPO(SportsHeatMapPO<Tuple<?>> heatmap) {
		this.pixelsPerSquareHorizontal = heatmap.pixelsPerSquareHorizontal;
		this.pixelsPerSquareVertical = heatmap.pixelsPerSquareVertical;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@SuppressWarnings({ "unchecked"})
	@Override
	protected void process_next(T object, int port) {
		int entityId = object.getAttribute(0);
		double xValue = object.getAttribute(1);
		double yValue = object.getAttribute(2);

		int newX = (int) (xValue + Math.abs(minX));
		int newY = (int) (yValue + Math.abs(minY));

		int xArray = (int) (newX / pixelsPerSquareHorizontal);
		int yArray = (int) (newY / pixelsPerSquareVertical);
		
		// If the players leave the field (I guess)
		if (xArray >= numTilesX)
			xArray = numTilesX - 1;
		if (yArray >= numTilesY)
			yArray = numTilesY - 1;
		
		if (xArray < 0)
			xArray = 0;
		if (yArray < 0)
			yArray = 0;
			

		if (!sportsHeatmap.heatmaps.containsKey(entityId)) {
			// We don't have values for this entity
			int[][] heatmap = new int[numTilesX][numTilesY];
			sportsHeatmap.heatmaps.put(entityId, heatmap);
		}

		int[][] heatmapArray = sportsHeatmap.heatmaps.get(entityId);

		// Add, that the player was in this tile
		heatmapArray[xArray][yArray]++;

		Integer maxHeatObj = sportsHeatmap.maxHeatValues.get(entityId);
		int maxHeatValue = 0;
		if (maxHeatObj != null) {
			maxHeatValue = maxHeatObj;
		}

		if (heatmapArray[xArray][yArray] > maxHeatValue) {
			// We need the highest value for color calculation
			maxHeatValue = heatmapArray[xArray][yArray];
			sportsHeatmap.maxHeatValues.put(entityId, maxHeatValue);
		}

		reduceSendLoadCounter++;
		if (reduceSendLoadCounter >= 1000) {
			try {
				String serializedHeatMap = toString(sportsHeatmap.heatmaps);
				String serializedMaxHeatMap = toString(sportsHeatmap.maxHeatValues);
				Tuple out = new Tuple(object.size() + 2, false);
				out.setAttribute(object.size(), serializedHeatMap);
				out.setAttribute(object.size() + 1, serializedMaxHeatMap);
				out.setMetadata(object.getMetadata());
				out.setRequiresDeepClone(object.requiresDeepClone());
				transfer(out);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			reduceSendLoadCounter = 0;
		}

	}

	@SuppressWarnings({"unchecked" })
	@Override
	public AbstractPipe<T, T> clone() {
		return new SportsHeatMapPO(this);
	}

	/** Write the object to a Base64 string. */
	private static String toString(Serializable o) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		oos.close();
		return new String(Base64Coder.encode(baos.toByteArray()));
	}

}
