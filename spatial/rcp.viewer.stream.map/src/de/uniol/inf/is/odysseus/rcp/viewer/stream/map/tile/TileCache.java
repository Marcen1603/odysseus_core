package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.tile;

import java.util.LinkedHashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenManager;

public class TileCache {

	public static final int CACHE_SIZE = 256;
	private ScreenManager manager = null;
	
	private LinkedHashMap<Tile, AsyncImage> map = new LinkedHashMap<Tile, AsyncImage>(
			CACHE_SIZE, 0.75f, true) {

		private static final long serialVersionUID = 3355437879185799011L;

		protected boolean removeEldestEntry(Map.Entry<Tile, AsyncImage> eldest) {
			boolean remove = size() > CACHE_SIZE;
			if (remove)
				eldest.getValue().dispose(manager.getCanvas().getDisplay());
			return remove;
		}
	};

	public TileCache(ScreenManager manager){
		this.manager = manager;
	}
	
	public void put(TileServer tileServer, int x, int y, int z, AsyncImage image) {
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
