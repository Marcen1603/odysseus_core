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
package de.uniol.inf.is.odysseus.image.util;

import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_core;

import de.uniol.inf.is.odysseus.image.common.datatype.Image;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class OpenCVUtil {
    private OpenCVUtil() {
    }

    public static IplImage imageToIplImage(final Image image) {
        final IplImage iplImage = IplImage.create(opencv_core.cvSize(image.getWidth(), image.getHeight()), opencv_core.IPL_DEPTH_64F, 1);
        final int widthStep = iplImage.widthStep() / 8;
        if (widthStep > iplImage.width()) {
            for (int h = 0; h < iplImage.height(); h++) {
                iplImage.getDoubleBuffer(h * widthStep).put(image.getBuffer().array(), h * image.getWidth(), image.getWidth());
            }
        }
        else {
            iplImage.getDoubleBuffer().put(image.getBuffer());
        }
        iplImage.origin(1);
        return iplImage;
    }

    public static Image iplImageToImage(IplImage iplImage, final Image image) {
        final int widthStep = iplImage.widthStep() / 8;
        if (widthStep > iplImage.width()) {
            for (int h = 0; h < iplImage.height(); h++) {
                iplImage.getDoubleBuffer(h * widthStep).get(image.getBuffer().array(), h * image.getWidth(), image.getWidth());
            }
        }
        else {
            image.setBuffer(iplImage.getDoubleBuffer());
        }
        iplImage.release();
        iplImage = null;
        return image;
    }
}
