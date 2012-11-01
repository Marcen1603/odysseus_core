package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
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
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.LayerUpdater;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.PQuery;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.StreamMapEditorPart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.BasicLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.LayerTypeRegistry;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.RasterLayer;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.script.parser.PreParserStatement;

public class MapEditorModel extends ModelObject {

	private static final Logger LOG = LoggerFactory.getLogger(MapEditorModel.class);

	public static final String MAP = "map";

	private LinkedList<ILayer> layers = new LinkedList<ILayer>();
	private TreeMap<String, LayerUpdater> connections = new TreeMap<String, LayerUpdater>();
	private LinkedList<IFile> qryFileList = new LinkedList<IFile>();
	
	private IProject project = null;
	
	private ScreenManager screenManager = null;
	//private StreamMapEditorPart editor = null;

	public void init(StreamMapEditorPart editor) {
	//	this.editor = editor;
		this.screenManager = editor.getScreenManager();
		for (ILayer layer : layers) {
			if (layer != null)
				layer.init(screenManager, null, null);
		}
		// editor.updateViews();
		screenManager.getCanvas().redraw();
	}

	public static MapEditorModel open(IFile file, StreamMapEditorPart editor) {
		MapEditorModel newModel = new MapEditorModel();
		String output = "";
		InputStream is = null;
		try {
			
			is = file.getContents();
			// InputStreamReader isr = new InputStreamReader(is);
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				output += inputLine;
			}

			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		LOG.debug(output);
		newModel.setProject(file.getProject());
	
		
		if (output.isEmpty()) {
			return newModel;
		}

		PersistentMapEditorModel persistentModel = new Gson().fromJson(output, PersistentMapEditorModel.class);

		if (persistentModel.getFiles() != null && !persistentModel.getFiles().isEmpty())
			for (String fileName : persistentModel.getFiles()) {
				IFile nativeFile = newModel.getProject().getFile(fileName);
				newModel.addFile(nativeFile);
				run(nativeFile);
			}

		IServerExecutor executor = (IServerExecutor) OdysseusRCPPlugIn.getExecutor();
		if (persistentModel.getQueries() != null)
			for (PQuery query : persistentModel.getQueries()) {
				LOG.debug("Reload Query: " + query);

				String[] qLines = new String[3];
				qLines[0] = "#TRANSCFG " + query.transformation;
				qLines[1] = "#PARSER " + query.parser;
				qLines[2] = "#QUERY " + query.text;

				boolean exists = false;
				// No multiple definition of a Stream.
				for (IPhysicalQuery phyQuery : executor.getExecutionPlan().getQueries()) {
					if (phyQuery.getLogicalQuery().getQueryText().equals(query.text)) {
						exists = true;
					}
				}
				if (!exists) {
					execute(qLines);
				}

				for (IPhysicalQuery phyQuery : executor.getExecutionPlan().getQueries()) {
					if (phyQuery.getLogicalQuery().getQueryText().equals(query.text)) {
						List<IPhysicalOperator> ops = phyQuery.getRoots();
						final List<ISubscription<ISource<?>>> subs = new LinkedList<ISubscription<ISource<?>>>();
						for (IPhysicalOperator operator : ops) {

							if (operator instanceof ISource<?>) {
								subs.add(new PhysicalSubscription<ISource<?>>((ISource<?>) operator, 0, 0, operator.getOutputSchema()));
							} else if (operator instanceof ISink<?>) {
								Collection<?> list = ((ISink<?>) operator).getSubscribedToSource();

								for (Object obj : list) {
									@SuppressWarnings("unchecked")
									PhysicalSubscription<ISource<?>> sub = (PhysicalSubscription<ISource<?>>) obj;
									subs.add(new PhysicalSubscription<ISource<?>>(sub.getTarget(), sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema()));
								}
							} else {
								throw new IllegalArgumentException("could not identify type of content of node " + operator);
							}
						}
						DefaultStreamConnection connection = new DefaultStreamConnection(subs);
						newModel.addConnection(connection, phyQuery, editor);
					}
				}

			}

		if (persistentModel.getLayers() != null)
			for (LayerConfiguration layerConf : persistentModel.getLayers()) {
				LOG.debug("Reload Layer: " + layerConf.getName());
				newModel.addLayer(layerConf);
			}

		return newModel;
	}

	public void save(IFile file) {
		StringBuilder configuration = new StringBuilder();

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file.getLocation().toOSString()));
			PersistentMapEditorModel persistentModel = new PersistentMapEditorModel();

			for(IFile qryfile : qryFileList){
				persistentModel.addFile(qryfile.getName());
			}

			for (LayerUpdater connection : connections.values()) {
				String queryText = connection.getQuery().getLogicalQuery().getQueryText();
				String queryParser = connection.getQuery().getLogicalQuery().getParserId();
				PQuery pquery = new PQuery();

				pquery.transformation = "Standard";
				pquery.parser = queryParser;
				pquery.text = queryText;

				persistentModel.addQuery(pquery);
			}

			for (ILayer layer : layers) {
				if (!(layer instanceof BasicLayer)){
				if(layer.getName().length() > 1)
					persistentModel.addLayer(layer);
				else
					LOG.error("Null Layer");
				}
			}

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			configuration.append(gson.toJson(persistentModel));

			LOG.debug("Save " + configuration.toString());
			bw.write(configuration.toString());
			bw.flush();
			bw.close();
		    file.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// public void addLayer(ILayer layer) {
	// if(layer == null){
	// return;
	// //throw new RuntimeException("Can add an empty layer.");
	// }
	//
	// if(layer instanceof VectorLayer){
	// String query = ((VectorLayer)layer).getConfiguration().getQuery();
	// // Find the Query
	// for(LayerUpdater connection : connections.values()){
	// if(connection.getQuery().getLogicalQuery().getQueryText().equals(query)){
	// //Connect the Stream to the iStreamListner
	// connection.add(layer);
	// //Add the Layer to the draw list.
	// layers.addLast(layer);
	// }
	// }
	// }
	//
	//
	// //Initializes the layers after the MapModel was initialized.
	// if(screenManager != null){
	// layer.init(screenManager, null, null);
	// }
	//
	// editor.UpdateViews();
	// changeSupport.firePropertyChange(MAP, null, this);
	// }

	public void addLayer(LayerConfiguration layerConfiguration) {
		ILayer layer = null;
		SDFAttribute attribute = null;
		SDFSchema schema = null;

		if (layerConfiguration.getType() == 0) {
			layer = new RasterLayer(layerConfiguration);
		}

		if (layerConfiguration.getType() == 1) {

			// Find the Query
			for (LayerUpdater connection : connections.values()) {
				// LOG.debug(connection.getQuery().getLogicalQuery().getQueryText());
				// LOG.debug(layerConfiguration.getQuery());
				if (connection.getQuery().getLogicalQuery().getQueryText().equals(layerConfiguration.getQuery())) {

					// Connect the Stream to the iStreamListner
					schema = connection.getConnection().getSubscriptions().get(0).getSchema();
					// AttributeResolver resolver = new AttributeResolver();
					// resolver.addSource(connection.getConnection().getSubscriptions().get(0).getSchema().getURI(),
					// (ILogicalOperator)
					// connection.getQuery().getLogicalQuery().getLogicalPlan().getSubscriptions().toArray()[0]);
					// resolver.addAttributes(schema);
					// attribute =
					// resolver.getAttribute(layerConfiguration.getAttribute());
					for (SDFAttribute tmpAttribute : schema) {
						if (tmpAttribute.getAttributeName().equals(layerConfiguration.getAttribute())) {
							attribute = tmpAttribute;
							// LOG.debug("Stream: " + schema.getURI());
							// LOG.debug("Stream: " + attribute.toString());
							layer = LayerTypeRegistry.getLayer(attribute.getDatatype());
							layer.setConfiguration(layerConfiguration);

							// PreInit
							layer.init(null, schema, attribute);

							connection.add(layer);
						}
					}
				}
			}
		}

		if (screenManager != null) {
			layer.init(screenManager, schema, attribute);
		}

		firePropertyChange(MAP, null, this);
		// Add the Layer to the draw list.
		layers.addLast(layer);
	}

	public void addConnection(IStreamConnection<Object> connection, IPhysicalQuery query, StreamMapEditorPart editor) {
		LOG.debug("Add Connection: " + query.getLogicalQuery().getQueryText());

		if (!connections.containsKey(String.valueOf(query.hashCode()))) {

			LayerUpdater updater = new LayerUpdater(editor, query, connection);
			connections.put(String.valueOf(query.hashCode()), updater);

			LOG.debug("Bind Query: " + query.getID());
			LOG.debug("Bind Query: " + query.getLogicalQuery().getQueryText());
		}
		firePropertyChange(MAP, null, this);
	}

	public LinkedList<ILayer> getLayers() {
		return layers;
	}

	public Collection<LayerUpdater> getConnectionCollection() {
		return connections.values();
	}

	private static void run(final IFile queryFile) {
		try {
			// Datei öffnen
			if (!queryFile.isSynchronized(IResource.DEPTH_ZERO))
				queryFile.refreshLocal(IResource.DEPTH_ZERO, null);

			// Datei einlesen
			ArrayList<String> lines = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new InputStreamReader(queryFile.getContents()));
			String line = br.readLine();
			while (line != null) {
				lines.add(line);
				line = br.readLine();
			}
			br.close();

			execute(lines.toArray(new String[lines.size()]));

		} catch (Exception ex) {
			LOG.error("Exception during running query file", ex);

			new ExceptionWindow(ex);
		}
	}

	public static void execute(final String[] text) {
		// Dieser Teil geschieht asynchron zum UIThread und wird als Job
		// ausgeführt
		// Job-Mechanismus wird von RCP gestellt.
		// Job job = new Job("Parsing and Executing Query") {
		// @Override
		// protected IStatus run(IProgressMonitor monitor) {
		// IStatus status = Status.OK_STATUS;
		try {
			ISession user = OdysseusRCPPlugIn.getActiveSession();
			// Befehle holen
			final List<PreParserStatement> statements = OdysseusRCPEditorTextPlugIn.getScriptParser().parseScript(text, user);

			// Erst Text testen
			// monitor.beginTask("Executing Commands", statements.size() * 2);
			// monitor.subTask("Validating");

			Map<String, Object> variables = new HashMap<String, Object>();
			for (PreParserStatement stmt : statements) {
				stmt.validate(variables, user);
				// monitor.worked(1);

				// Wollte der Nutzer abbrechen?
				// if (monitor.isCanceled())
				// return Status.CANCEL_STATUS;
			}

			// Dann ausführen
			variables = new HashMap<String, Object>();
			int counter = 1;
			for (PreParserStatement stmt : statements) {
				// monitor.subTask("Executing (" + counter + " / " +
				// statements.size() + ")");
				stmt.execute(variables, user, OdysseusRCPEditorTextPlugIn.getScriptParser());
				// monitor.worked(1);
				counter++;
			}
		} catch (OdysseusScriptException ex) {
			
			//Evil Workaround
			
			if(!ex.getRootMessage().contains("multiple")){
				MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), ex.getMessage(), ex.getRootMessage() );	
			}
			
			
			
			LOG.error("Exception during executing script", ex);
			//
			// //status = new Status(Status.ERROR,
			// IEditorTextParserConstants.PLUGIN_ID, "Script Execution Error: "
			// + ex.getRootMessage(), ex);
		}
		// monitor.done();

		// return status;
		// }
		// };
		// job.setUser(true); // gibt an, dass der Nutzer dieses Job ausgelöst
		// hat
		// job.schedule(); // dieser Job soll nun ausgeführt werden
	}

	public void removeLayer(ILayer layer) {
		layers.remove(layer);
		firePropertyChange(MAP, null, this);
	}

	public void removeConnection(LayerUpdater connection) {
		String key = String.valueOf(connection.getQuery().hashCode());
		for (ILayer layer : connections.get(key)) {
			removeLayer(layer);
		}
		connections.remove(key);
		firePropertyChange(MAP, null, this);
	}

	
	public void addFile(IFile file){
		this.qryFileList.add(file);
		firePropertyChange(MAP, null, this);
	}
	
	public LinkedList<IFile> getFiles(){
		return qryFileList;
	}

	public void removeFile(IFile file){
		this.qryFileList.remove(file);
		firePropertyChange(MAP, null, this);
	}

	public IProject getProject() {
    	return project;
    }

	public void setProject(IProject project) {
    	this.project = project;
    }

	public void layerUp(ILayer layer) {
		if(layers.contains(layer)){
			int positionOrign = layers.lastIndexOf(layer);
			int positionNext = positionOrign + 1;
			if(positionNext < layers.size()){
				ILayer tmp = layers.get(positionOrign);
				ILayer tmp1 = layers.get(positionNext);

				layers.set(positionNext, tmp);
				layers.set(positionOrign, tmp1);
			}
		}
    }

	public void layerDown(ILayer layer) {
		if(layers.contains(layer)){
			int positionOrign = layers.lastIndexOf(layer);
			int positionNext = positionOrign - 1;
			if(positionNext >= 0){
				ILayer tmp = layers.get(positionOrign);
				ILayer tmp1 = layers.get(positionNext);

				layers.set(positionNext, tmp);
				layers.set(positionOrign, tmp1);
			}
		}  
    }
	
	
}
