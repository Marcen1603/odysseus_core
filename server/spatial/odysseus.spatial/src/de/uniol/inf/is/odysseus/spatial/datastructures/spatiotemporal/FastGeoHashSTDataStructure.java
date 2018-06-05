package de.uniol.inf.is.odysseus.spatial.datastructures.spatiotemporal;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.hsr.geohash.GeoHash;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.spatial.index.GeoHashHelper;

@Deprecated
public class FastGeoHashSTDataStructure extends GeoHashSTDataStructure {

	public static final String TYPE = "fastgeohash";

	private static Logger _logger = LoggerFactory.getLogger(FastGeoHashSTDataStructure.class);

	// How much bigger the number of deleted elements over the remaining
	// elements need to be to do a re-creation of the hashes
	private static final double biggerFactor = 3;

	private int cleanUpCounter;
	private static final int CLEAN_SKIPS = 10;

	public FastGeoHashSTDataStructure(String name, int geometryPosition) {
		super(name, geometryPosition);
	}

	@Override
	public void cleanUp(PointInTime timestamp) {

		// Test: As every query itself considers the time, cleaning up is not
		// that important. We could skip a lot of cleanup.
		if (cleanUpCounter < CLEAN_SKIPS) {
			cleanUpCounter++;
			return;
		}

		cleanUpCounter = 0;

		// Remove old elements from sweepArea
		List<Tuple<ITimeInterval>> removed = this.sweepArea.extractElementsBeforeAsList(timestamp);

		// Do we remove more than we leave?
		if (removed.size() >= biggerFactor * this.sweepArea.size()) {
			// We remove more than we leave -> we can better delete
			// everything and re-insert
			// This is especially useful for tumbling windows

			// TODO Is this really better??

			_logger.debug("------------------");
			_logger.debug("Creating new GeoHashIndex in order to do fast clean.");
			_logger.debug("Elements to clean: " + removed.size());
			_logger.debug("Elements in GeoHashIndex: " + geoHashes.size());
			_logger.debug("Elements left in SweepArea: " + sweepArea.size());

			this.geoHashes = new TreeMap<>();

			// Add tuples that are still in sweepArea back to new
			// GeoHash
			this.sweepArea.queryAllElementsAsList().stream().forEach(tuple -> {
				// Insert into map
				GeoHash geoHash = GeoHashHelper.fromGeometry(GeoHashHelper.getGeometry(tuple, getGeometryPosition()),
						BIT_PRECISION);
				List<Tuple<ITimeInterval>> geoHashList = this.geoHashes.get(geoHash);
				if (geoHashList == null) {
					/*
					 * Probably we will only have one element in here as two
					 * objects on the same location are unlikely (but depends on
					 * the scenario)
					 */
					geoHashList = new ArrayList<>(1);
				}

				// Add the new tuple to the list
				geoHashList.add(tuple);
				this.geoHashes.put(geoHash, geoHashList);
				// Count
				elementCounter++;
			});
		} else if (removed.size() > 0) {
			// Warn
			if (removed.size() > 50000) {
				_logger.warn("Remove " + removed.size() + " elements from GeoHashIndex. This can take a while!");
			}

			// Remove the extracted elements from the quadTree
			removed.stream().forEach(e -> this.geoHashes.remove(
					GeoHashHelper.fromGeometry(GeoHashHelper.getGeometry(e, getGeometryPosition()), BIT_PRECISION)));
		}

	}

}
