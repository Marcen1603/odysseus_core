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

import java.io.File;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacv.JavaCvErrorCallback;
import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_objdetect;
import com.googlecode.javacv.cpp.opencv_objdetect.CvHaarClassifierCascade;

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
	private final CvHaarClassifierCascade classifierCascade;
	@SuppressWarnings("unused")
	private final JavaCvErrorCallback callback;

	/**
 * 
 */
	public CountFacesFunction() {
		super("faces", 1, CountFacesFunction.ACC_TYPES, SDFDatatype.INTEGER,
				true);
		callback = new JavaCvErrorCallback();
		Loader.load(opencv_objdetect.class);

		File haarcascadeFile = new File("haarcascade_frontalface_default.xml");
		if (haarcascadeFile.canRead()) {
			Pointer haarcascadePointer = opencv_core.cvLoad(haarcascadeFile
					.getAbsolutePath());
			classifierCascade = new CvHaarClassifierCascade(haarcascadePointer);
		} else {
			classifierCascade = null;
		}

	}

	public Integer getValue() {
		if (classifierCascade != null) {
			final Image image = (Image) this.getInputValue(0);
			final IplImage iplImage = OpenCVUtil.imageToIplImage(image);
			IplImage iplGrayImage = IplImage.create(iplImage.width(),
					iplImage.height(), opencv_core.IPL_DEPTH_8U, 1);
			opencv_core.cvConvertScale(iplGrayImage, iplImage, 1, 0);
			CvMemStorage storage = CvMemStorage.create();
			CvSeq sign = opencv_objdetect.cvHaarDetectObjects(iplGrayImage,
					classifierCascade, storage, 1.1, 3,
					opencv_objdetect.CV_HAAR_DO_CANNY_PRUNING);
			opencv_core.cvClearMemStorage(storage);
			iplGrayImage.release();
			iplImage.release();
			return sign.total();
		}
		return 0;
	}

}
