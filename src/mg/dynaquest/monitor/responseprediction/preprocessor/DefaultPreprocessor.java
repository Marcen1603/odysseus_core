package mg.dynaquest.monitor.responseprediction.preprocessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The standard implementation of an {@link IMetadataPreprocessor}.
 * 
 * You can register preprocessor objects, which calculate the characteristics
 * of the metadata in {@link #processMetadata(List)}.
 * @author Jonas Jacobi
 */
public class DefaultPreprocessor extends AbstractMetadataPreprocessor {
	private static final long serialVersionUID = 1865028320823181294L;
	
	List<IPreprocessorObject<?>> preprocessorObjects;

	public DefaultPreprocessor(IPOMetadatarepository metadatarepository) {
		super(metadatarepository);
		this.preprocessorObjects = new ArrayList<IPreprocessorObject<?>>();
	}

	/**
	 * Add a preprocessor object for the calculation of metadata characteristics
	 * @param object the preprocessor object to add
	 */
	public void addPreprocessorObject(IPreprocessorObject<?> object) {
		if (!preprocessorObjects.contains(object)) {
			this.preprocessorObjects.add(object);
		}
	}

	@Override
	protected PreprocessedMetadata processMetadata(List<POEventData> events) {
		if (events == null) {
			System.out.println("process metadata null");
			return null;
		}
		Iterator<POEventData> it = events.iterator();
		if (!it.hasNext()) {
			return null;
		}
		POEventData first = it.next();
		PreprocessedMetadata data = new PreprocessedMetadata(first.getPoGuid());
		data.setStartTime(first.getTime());
		data.setEndTime(events.get(events.size() - 1).getTime());
		for (IPreprocessorObject<?> object : this.preprocessorObjects) {
			object.processData(events, data);
		}

		return data;
	}
}
