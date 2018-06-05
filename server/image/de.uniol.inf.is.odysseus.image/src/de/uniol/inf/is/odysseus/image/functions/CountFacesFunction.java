/********************************************************************************** 
 * Copyright 2014 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.image.functions;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.objdetect.CascadeClassifier;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.image.common.datatype.Image;
import de.uniol.inf.is.odysseus.image.common.sdf.schema.SDFImageDatatype;
import de.uniol.inf.is.odysseus.image.util.OpenCVUtil;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class CountFacesFunction extends AbstractFunction<Integer> {

	/**
     * 
     */
	private static final long serialVersionUID = -4351350244354050164L;
	/**
     * 
     */
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFImageDatatype.IMAGE } };
	private  CascadeClassifier faceDetector;

	/**
	 * 
	 */
	public CountFacesFunction() {
		super("faces", 1, CountFacesFunction.ACC_TYPES, SDFDatatype.INTEGER,
				true);
//		faceDetector = new CascadeClassifier(getClass().getResource(
//				"/haarcascade_frontalface_default.xml").getPath());
	}

	@Override
	public Integer getValue() {
		// if (classifierCascade != null) {
		final Image image = (Image) this.getInputValue(0);
		final Mat iplImage = OpenCVUtil.imageToIplImage(image);
		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(iplImage, faceDetections);
		return faceDetections.toArray().length;

	}

}
