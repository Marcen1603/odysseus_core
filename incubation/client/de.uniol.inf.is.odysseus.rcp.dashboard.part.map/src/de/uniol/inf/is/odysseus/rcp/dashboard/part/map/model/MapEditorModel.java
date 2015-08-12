package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.streamconnection.DefaultStreamConnection;
import de.uniol.inf.is.odysseus.core.streamconnection.IStreamConnection;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;
import de.uniol.inf.is.odysseus.rcp.exception.ExceptionWindow;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.LayerUpdater;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.OwnProperties;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dashboard.MapDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.LayerTypeRegistry;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.RasterLayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.RasterLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.VectorLayerConfiguration;

//import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.HeatmapLayerConfiguration;
//import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.TracemapLayerConfiguration;
//import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.heatmap.Heatmap;
//import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.tracemap.TraceLayer;

public class MapEditorModel extends ModelObject {

	private static final Logger LOG = LoggerFactory.getLogger(MapEditorModel.class);

	public static final String MAP = "map";

	private LinkedList<ILayer> layers = new LinkedList<ILayer>();
	private TreeMap<String, LayerUpdater> connections = new TreeMap<String, LayerUpdater>();
	private LinkedList<IFile> qryFileList = new LinkedList<IFile>();

	private IProject project = null;
	private ScreenManager screenManager = null;
	private OwnProperties ownProperties;

	private int layercount;
	private static IFile iFile;

	private int srid;

	public void init(MapDashboardPart mapDashboardPart) {
		this.screenManager = mapDashboardPart.getScreenManager();
		for (LayerUpdater layerUpdater : connections.values()) {
			screenManager.addPropertyChangeListener(layerUpdater);
			screenManager.addConnection(layerUpdater);
		}

		// Cause if we load it from a file, the
		// screenManger was null
		for (ILayer layer : layers) {
			if (layer != null)
				layer.init(screenManager, null, null);
		}
		screenManager.redraw();
		
		//added on my own
		ownProperties = new OwnProperties();
	}

//	/**
//	 * If a saved file is used
//	 * 
//	 * @param file
//	 *            The used file
//	 * @param editor
//	 * @return Loaded MapEditorModel
//	 */
//	@SuppressWarnings("unchecked")
//	public static MapEditorModel open(IFile file, MapDashboardPart mapDashboardPart) {
//		iFile = file;
//		MapEditorModel newModel = new MapEditorModel();
//		String output = "";
//		InputStream is = null;
//		try {
//			is = file.getContents();
//			BufferedReader in = new BufferedReader(new InputStreamReader(is));
//			String inputLine;
//
//			while ((inputLine = in.readLine()) != null) {
//				output += inputLine;
//			}
//
//			in.close();
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (CoreException e) {
//			e.printStackTrace();
//		} finally {
//			if (is != null) {
//				try {
//					is.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//
//		LOG.debug(output);
//		newModel.setProject(file.getProject());
//
//		if (output.isEmpty()) {
//			return newModel;
//		}
//		PersistentMapEditorModel persistentModel = getGson().fromJson(output, PersistentMapEditorModel.class);
//
//		if (persistentModel.getSrid() != null)
//			newModel.srid = persistentModel.getSrid();
//		IServerExecutor executor = (IServerExecutor) OdysseusRCPPlugIn.getExecutor();
//		if (persistentModel.getQueries() != null)
//
//			for (PersistentQuery pquery : persistentModel.getQueries()) {
//				if (pquery instanceof QueryFile) {
//					QueryFile query = (QueryFile) pquery;
//					IFile nativeFile = newModel.getProject().getFile(query.filename);
//					newModel.addFile(nativeFile);
//					run(nativeFile);
//				} else if (pquery instanceof QueryString) {
//
//					QueryString query = (QueryString) pquery;
//
//					LOG.debug("Reload Query: " + query);
//
//					String[] qLines = new String[3];
//					qLines[0] = "#TRANSCFG " + query.transformation;
//					qLines[1] = "#PARSER " + query.parser;
//					qLines[2] = "#QUERY " + query.text;
//
//					boolean exists = false;
//					// No multiple definition of a Stream.
//					for (IPhysicalQuery phyQuery : executor.getExecutionPlan().getQueries()) {
//						if (phyQuery.getLogicalQuery().getQueryText().equals(query.text)) {
//							exists = true;
//						}
//					}
//					if (!exists) {
//						execute(qLines);
//					}
//
//					for (IPhysicalQuery phyQuery : executor.getExecutionPlan().getQueries()) {
//						if (phyQuery.getLogicalQuery().getQueryText().equals(query.text)) {
//							List<IPhysicalOperator> ops = phyQuery.getRoots();
//
//							@SuppressWarnings({ "rawtypes" })
//							DefaultStreamConnection connection = new DefaultStreamConnection(ops);
//							newModel.addConnection(connection, phyQuery, mapDashboardPart);
//						}
//					}
//				}
//			}
//
//		if (persistentModel.getLayers() != null) {
//			for (LayerConfiguration layerConf : persistentModel.getLayers()) {
//				LOG.debug("Reload Layer: " + layerConf.getName());
//				newModel.addLayer(layerConf);
//			}
//		}
//		return newModel;
//	}

//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	private static Gson getGson() {
//		// Layers
//		GsonBuilder gsonbuilder = new GsonBuilder().setPrettyPrinting();
//		RuntimeTypeAdapterFactory layerConfigurationFactory = RuntimeTypeAdapterFactory.of(LayerConfiguration.class);
//		layerConfigurationFactory.registerSubtype(VectorLayerConfiguration.class);
//		layerConfigurationFactory.registerSubtype(RasterLayerConfiguration.class);
//
//		// // Thematic layers
//		// layerConfigurationFactory
//		// .registerSubtype(HeatmapLayerConfiguration.class);
//		// layerConfigurationFactory
//		// .registerSubtype(TracemapLayerConfiguration.class);
//
//		gsonbuilder.registerTypeAdapterFactory(layerConfigurationFactory);
//
//		// Queries
//		RuntimeTypeAdapterFactory persistentQueryFactory = RuntimeTypeAdapterFactory.of(PersistentQuery.class);
//		persistentQueryFactory.registerSubtype(QueryFile.class);
//		persistentQueryFactory.registerSubtype(QueryString.class);
//		gsonbuilder.registerTypeAdapterFactory(persistentQueryFactory);
//		return gsonbuilder.create();
//	}
//
//	/**
//	 * Saves the map as JSON in the file. All parts of the LayerConfigurations
//	 * have to be serializable
//	 * 
//	 * @param file
//	 */
//	public void save(IFile file) {
//		StringBuilder configuration = new StringBuilder();
//
//		try {
//			BufferedWriter bw = new BufferedWriter(new FileWriter(file.getLocation().toOSString()));
//			PersistentMapEditorModel persistentModel = new PersistentMapEditorModel();
//			persistentModel.setSrid(this.srid);
//			for (IFile qryfile : qryFileList) {
//				QueryFile queryfile = new QueryFile();
//				queryfile.filename = qryfile.getName();
//				persistentModel.addQuery(queryfile);
//			}
//
//			for (LayerUpdater connection : connections.values()) {
//				String queryText = connection.getQuery().getQueryText();
//				String queryParser = connection.getQuery().getParserId();
//				QueryString pquery = new QueryString();
//
//				pquery.transformation = "Standard";
//				pquery.parser = queryParser;
//				pquery.text = queryText;
//
//				persistentModel.addQuery(pquery);
//			}
//
//			persistentModel.addLayers(layers);
//
//			Gson gson = getGson();
//			String jsonString = gson.toJson(persistentModel);
//			configuration.append(jsonString);
//
//			LOG.debug("Save " + configuration.toString());
//			bw.write(configuration.toString());
//			bw.flush();
//			bw.close();
//			file.refreshLocal(IResource.DEPTH_INFINITE, null);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Checks the type of the layerConfiguration and calls the right method to
	 * add
	 * 
	 * @param layerConfiguration
	 */
	public void addLayer(LayerConfiguration layerConfiguration) {
		layercount++;
		ILayer layer = null;

		if (layerConfiguration instanceof RasterLayerConfiguration)
			layer = addLayer((RasterLayerConfiguration) layerConfiguration);
		else if (layerConfiguration instanceof VectorLayerConfiguration)
			layer = addLayer((VectorLayerConfiguration) layerConfiguration);
		// else if (layerConfiguration instanceof TracemapLayerConfiguration)
		// layer = addLayer((TracemapLayerConfiguration) layerConfiguration);
		// else if (layerConfiguration instanceof HeatmapLayerConfiguration)
		// layer = addLayer((HeatmapLayerConfiguration) layerConfiguration);
		
		firePropertyChange(MAP, null, this);
		layers.addLast(layer);
	}

	private ILayer addLayer(RasterLayerConfiguration layerConfiguration) {
		ILayer layer = new RasterLayer(layerConfiguration);

		if (screenManager != null) {
			layer.init(screenManager, null, null);
		}
		return layer;
	}

	private ILayer addLayer(VectorLayerConfiguration layerConfiguration) {
		ILayer layer = null;
		SDFAttribute attribute = null;
		SDFSchema schema = null;

		// Find the Query
		for (LayerUpdater connection : connections.values()) {
			if (connection.getQuery().getQueryText().equals(layerConfiguration.getQuery())) {

				// Connect the Stream to the iStreamListner
				schema = connection.getConnection().getOutputSchema();
				for (SDFAttribute tmpAttribute : schema) {
					if (tmpAttribute.getAttributeName().equals(layerConfiguration.getAttribute())) {
						attribute = tmpAttribute;
						layer = LayerTypeRegistry.getLayer(attribute.getDatatype());
						layer.setConfiguration(layerConfiguration);

						// PreInit
						layer.init(null, schema, attribute);
						connection.add(layer);
					}
				}
			}
		}
		if (screenManager != null && layer != null) {
			layer.init(screenManager, schema, attribute);
		}
		return layer;
	}

	// /**
	// * Adds an HeatmapLayer to the map
	// *
	// * @param layerConfiguration
	// * @return The layer
	// */
	// private ILayer addLayer(HeatmapLayerConfiguration layerConfiguration) {
	// ILayer layer = new Heatmap(layerConfiguration);
	// if (screenManager != null) {
	// layer.init(screenManager, null, null);
	// }
	//
	// // Add to the selected connection (LayerUpdater)
	// for (LayerUpdater connection : connections.values()) {
	// if (connection.getQuery().getQueryText()
	// .equals(layerConfiguration.getQuery())) {
	// connection.add(layer);
	// }
	// }
	//
	// // We don't want to set it active manually, too much clicks ...
	// layer.setActive(true);
	//
	// return layer;
	// }

	// /**
	// * Adds a TracemapLayer to the map
	// *
	// * @param layerConfiguration
	// * @return The layer
	// */
	// private ILayer addLayer(TracemapLayerConfiguration layerConfiguration) {
	// ILayer layer = new TraceLayer(layerConfiguration);
	// if (screenManager != null) {
	// layer.init(screenManager, null, null);
	// }
	//
	// // Add to the selected connection (LayerUpdater)
	// for (LayerUpdater connection : connections.values()) {
	// if (connection.getQuery().getQueryText()
	// .equals(layerConfiguration.getQuery())) {
	// connection.add(layer);
	// }
	// }
	//
	// // We don't want to set it active manually, too much clicks ...
	// layer.setActive(true);
	//
	// return layer;
	// }
	
	public void editLayer(LayerConfiguration layerConfiguration){
		ILayer layer = null;

		if (layerConfiguration instanceof RasterLayerConfiguration)
			layer = editLayer((RasterLayerConfiguration) layerConfiguration);
//		else if (layerConfiguration instanceof VectorLayerConfiguration)
//			layer = editLayer((VectorLayerConfiguration) layerConfiguration);
		// else if (layerConfiguration instanceof TracemapLayerConfiguration)
		// layer = editLayer((TracemapLayerConfiguration) layerConfiguration);
		// else if (layerConfiguration instanceof HeatmapLayerConfiguration)
		// layer = editLayer((HeatmapLayerConfiguration) layerConfiguration);
		
		firePropertyChange(MAP, null, this);
	}
	
	private ILayer editLayer(RasterLayerConfiguration layerConfiguration){
		ILayer layer = new RasterLayer(layerConfiguration);
		return layer;
	}
	
//	private ILayer editLayer(VectorLayerConfiguration layerConfiguration){
//		ILayer layer = new VectorLayer(layerConfiguration);
//		return layer;
//	}
	
	public void removeLayer(ILayer layer) {
		layers.remove(layer);
		firePropertyChange(MAP, null, this);
	}

	public void layerUp(ILayer layer) {
		LinkedList<ILayer> group = layers;
		if (group.contains(layer)) {
			int positionOrign = group.lastIndexOf(layer);
			int positionNext = positionOrign + 1;
			if (positionNext < group.size()) {
				ILayer tmp = group.get(positionOrign);
				ILayer tmp1 = group.get(positionNext);

				group.set(positionNext, tmp);
				group.set(positionOrign, tmp1);
			}
		}
	}
	
	public void layerTop(ILayer layer){
		LinkedList<ILayer> group = layers;
		if (group.contains(layer)) {
			int positionOrign = group.lastIndexOf(layer);
			int positionTop = group.size()-1;
			if (positionTop != positionOrign) {
				ILayer tmp = group.get(positionOrign);
				ILayer tmp1 = group.get(positionTop);

				group.set(positionTop, tmp);
				group.set(positionOrign, tmp1);
			}
		}
	}

	public void layerDown(ILayer layer) {
		LinkedList<ILayer> group = layers;
		if (group.contains(layer)) {
			int positionOrign = group.lastIndexOf(layer);
			int positionNext = positionOrign - 1;
			if (positionNext >= 0) {
				ILayer tmp = group.get(positionOrign);
				ILayer tmp1 = group.get(positionNext);

				group.set(positionNext, tmp);
				group.set(positionOrign, tmp1);
			}
		}
	}
	
	public void layerBottom(ILayer layer){
		LinkedList<ILayer> group = layers;
		if (group.contains(layer)) {
			int positionOrign = group.lastIndexOf(layer);
			int positionBottom= 0;
			if (positionBottom != positionOrign) {
				ILayer tmp = group.get(positionOrign);
				ILayer tmp1 = group.get(positionBottom);

				group.set(positionBottom, tmp);
				group.set(positionOrign, tmp1);
			}
		}
	}

	//TODO This should be handled by the Edit-Button later
	public void rename(ILayer layer, String name) {
		layer.setName(name);
	}

	// TODO THIS SHOULD BE DONE BY THE DASHBOARDPART
//	public void addConnection(IStreamConnection<Object> connection, IPhysicalQuery query, MapDashboardPart mapDashboardPart) {
//		LOG.debug("Add Connection: " + query.getLogicalQuery().getQueryText());
//
//		if (!connections.containsKey(String.valueOf(query.hashCode()))) {
//
//			LayerUpdater updater = new LayerUpdater(mapDashboardPart, query, connection);
//			connections.put(String.valueOf(query.hashCode()), updater);
//			if (screenManager != null) {
//				// If this Model is not opened by a file
//				screenManager.addPropertyChangeListener(updater);
//				screenManager.addConnection(updater);
//			}
//
//			LOG.debug("Bind Query: " + query.getID());
//			LOG.debug("Bind Query: " + query.getLogicalQuery().getQueryText());
//		}
//		firePropertyChange(MAP, null, this);
//	}

//	private static void run(final IFile queryFile) {
//		try {
//			// Datei oeffnen
//			if (!queryFile.isSynchronized(IResource.DEPTH_ZERO))
//				queryFile.refreshLocal(IResource.DEPTH_ZERO, null);
//
//			// Datei einlesen
//			ArrayList<String> lines = new ArrayList<String>();
//			BufferedReader br = new BufferedReader(new InputStreamReader(queryFile.getContents()));
//			String line = br.readLine();
//			while (line != null) {
//				lines.add(line);
//				line = br.readLine();
//			}
//			br.close();
//
//			execute(lines.toArray(new String[lines.size()]));
//
//		} catch (Exception ex) {
//			LOG.error("Exception during running query file", ex);
//
//			new ExceptionWindow(ex);
//		}
//	}
//
//	public static void execute(final String[] text) {
//		try {
//			ISession user = OdysseusRCPPlugIn.getActiveSession();
//			OdysseusRCPEditorTextPlugIn.getExecutor().addQuery(concat(text), "OdysseusScript", user, Context.empty());
//		} catch (PlanManagementException ex) {
//			LOG.error("Exception during executing script", ex);
//			if (!ex.getRootMessage().contains("multiple")) {
//				MessageDialog.openError(getCurrentShell(), ex.getMessage(), ex.getRootMessage());
//			}
//		}
//	}

//	//TODO CHange this to Dashboard Composite later
//	private static Shell getCurrentShell() {
//		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
//	}
//
//	private static String concat(String[] text) {
//		StringBuilder sb = new StringBuilder();
//		for (String line : text) {
//			sb.append(line).append("\n");
//		}
//		return sb.toString();
//	}

//	//TODO This shouldn't be needed after the Dashboardpart is handling the queries
//	public void removeConnection(LayerUpdater connection) {
//		String key = String.valueOf(connection.getQuery().hashCode());
//		for (ILayer layer : connections.get(key)) {
//			removeLayer(layer);
//		}
//		connections.remove(key);
//		screenManager.removeConnection(connection);
//		firePropertyChange(MAP, null, this);
//	}

	public void addFile(IFile file) {
		this.qryFileList.add(file);
		firePropertyChange(MAP, null, this);
	}

	public LinkedList<IFile> getFiles() {
		return qryFileList;
	}

	public void removeFile(IFile file) {
		this.qryFileList.remove(file);
		firePropertyChange(MAP, null, this);
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

	public String[] getLayerNameList() {
		ArrayList<String> names = new ArrayList<String>();
		for (ILayer layer : layers) {
			names.add(layer.getComplexName());
		}
		return names.toArray(new String[names.size()]);
	}

	public String getNextLayerName() {
		return "Layer" + layercount++;
	}

	public int getSRID() {
		return this.srid;
	}

	public void setSrid(int srid) {
		this.srid = srid;
	}

	public LinkedList<IFile> getQryFileList() {
		return qryFileList;
	}

	public void setQryFileList(LinkedList<IFile> qryFileList) {
		this.qryFileList = qryFileList;
	}

	public IFile getFile() {
		return iFile;
	}

	public LinkedList<ILayer> getLayers() {
		return layers;
	}

	public Collection<LayerUpdater> getConnectionCollection() {
		return connections.values();
	}
	
	public OwnProperties getOwnProperties(){
		return ownProperties;
	}
}
