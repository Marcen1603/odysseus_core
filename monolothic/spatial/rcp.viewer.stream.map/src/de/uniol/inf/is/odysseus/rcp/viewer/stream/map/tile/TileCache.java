package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.tile;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class TileCache {

	public static final int CACHE_SIZE = 128;
	
	private Map<Tile, AsyncImage> map = Collections.synchronizedMap(new LinkedHashMap<Tile, AsyncImage>(
			CACHE_SIZE, 0.75f, true) {

		private static final long serialVersionUID = 3355437879185799011L;

		@Override
		protected boolean removeEldestEntry(Map.Entry<Tile, AsyncImage> eldest) {
			@SuppressWarnings("unused")
			long time = System.currentTimeMillis();
			boolean remove = size() > CACHE_SIZE;
			if (remove){
				eldest.getValue().dispose();
			}
			return remove;
		}
	});
	
	public void put(TileServer tileServer, int x, int y, int z, AsyncImage image) {
		AsyncImage parent = get(tileServer, x/2, y/2, z-1);
		if (parent != null)
			image.setParent(parent);
		map.put(new Tile(tileServer.getURL(), x, y, z), image);
	}

	public AsyncImage get(TileServer tileServer, int x, int y, int z) {
		return map.get(new Tile(tileServer.getURL(), x, y, z));
	}

	public void remove(TileServer tileServer, int x, int y, int z) {
		map.remove(new Tile(tileServer.getURL(), x, y, z));
	}
	
	public int getSize() {
		return map.size();
	}
}
