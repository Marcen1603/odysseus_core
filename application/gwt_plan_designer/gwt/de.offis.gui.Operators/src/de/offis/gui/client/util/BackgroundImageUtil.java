package de.offis.gui.client.util;

import java.io.Serializable;
import java.util.ArrayList;

import de.offis.gui.client.rpc.OperatorsServiceProxy;

/**
 * This class helps at selecting the correct url for the different types of modules.
 *
 * @author Alexander Funk
 * 
 */
public class BackgroundImageUtil {
	public static class BackgroundImages implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3438056971004802721L;
		private ArrayList<String> metaTypeImages = null;
		private ArrayList<String> outputImages = null;
		private ArrayList<String> sensorTypeImages = null;
		
		public BackgroundImages(ArrayList<String> metaTypeImages, ArrayList<String> outputImages, ArrayList<String> sensorTypeImages) {
			this.metaTypeImages = metaTypeImages;
			this.outputImages = outputImages;
			this.sensorTypeImages = sensorTypeImages;
		}
		
		protected BackgroundImages() {
			// serializable gwt
		}
		
		public ArrayList<String> getMetaTypeImages() {
			return metaTypeImages;
		}
		
		public ArrayList<String> getOutputImages() {
			return outputImages;
		}
		
		public ArrayList<String> getSensorTypeImages() {
			return sensorTypeImages;
		}
	}
	
	private BackgroundImages backgrounds = null;

	private static final String STANDARD_SENSORTYPE_IMAGE = "images/inputs/standard.svg";
	private static final String STANDARD_METATYPE_IMAGE = "images/operators/standard.svg";
	private static final String STANDARD_OUTPUT_IMAGE = "images/outputs/standard.svg";

	public BackgroundImageUtil() {
		OperatorsServiceProxy.get().getBackgroundImages(
				new SuccessCallback<BackgroundImages>() {

					@Override
					public void onSuccess(BackgroundImages result) {
						backgrounds = result;
					}
				});
	}

	public String getUrlForMetaTypeImage(String metatype) {
		if (backgrounds == null) {
			return STANDARD_METATYPE_IMAGE;
		}

		if (!backgrounds.getMetaTypeImages().contains(metatype)) {
			return STANDARD_METATYPE_IMAGE;
		}

		return "images/operators/" + metatype + ".svg";
	}

	public String getUrlForOutputImage(String output) {
		if (backgrounds == null) {
			return STANDARD_OUTPUT_IMAGE;
		}

		if (!backgrounds.getOutputImages().contains(output)) {
			return STANDARD_OUTPUT_IMAGE;
		}

		return "images/outputs/" + output + ".png";
	}

	public String getUrlForSensorTypeImage(String sensortype) {
		if (backgrounds == null) {
			return STANDARD_SENSORTYPE_IMAGE;
		}

		if (!backgrounds.getSensorTypeImages().contains(sensortype)) {
			return STANDARD_SENSORTYPE_IMAGE;
		}

		return "images/inputs/" + sensortype + ".png";
	}
}