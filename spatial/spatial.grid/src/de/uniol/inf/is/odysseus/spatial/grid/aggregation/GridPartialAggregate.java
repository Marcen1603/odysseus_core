/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.spatial.grid.aggregation;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.spatial.grid.common.OpenCVUtil;
import de.uniol.inf.is.odysseus.spatial.grid.model.Grid;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class GridPartialAggregate<T> implements IPartialAggregate<T> {
    private double         count;
    private final Grid     grid;
    private final IplImage image;

    public GridPartialAggregate(final Grid grid) {
        this.count = 1.0;
        this.grid = grid.clone();
        this.image = IplImage.create(opencv_core.cvSize(this.grid.width, this.grid.height), opencv_core.IPL_DEPTH_16U,
                1);
        IplImage tmp = IplImage.create(opencv_core.cvSize(this.image.width(), this.image.height()),
                opencv_core.IPL_DEPTH_8U, 1);

        IplImage tmp64f = OpenCVUtil.gridToImage(this.grid);
        opencv_core.cvConvertScale(tmp64f, tmp, 255, 0);
        opencv_core.cvConvertScale(tmp, this.image, 1, 0);
        tmp.release();
        tmp64f.release();
    }

    public GridPartialAggregate(final GridPartialAggregate<T> gridPartialAggregate) {
        this.grid = gridPartialAggregate.grid.clone();
        this.count = gridPartialAggregate.count;
        this.image = IplImage.create(opencv_core.cvSize(this.grid.width, this.grid.height), opencv_core.IPL_DEPTH_16U,
                1);
        opencv_core.cvCopy(gridPartialAggregate.image, this.image);
    }

    @Override
    public GridPartialAggregate<T> clone() {
        return new GridPartialAggregate<T>(this);
    }

    public void evaluate() {
        IplImage image = IplImage.create(opencv_core.cvSize(this.image.width(), this.image.height()),
                opencv_core.IPL_DEPTH_8U, 1);
        IplImage image64f = OpenCVUtil.gridToImage(grid);
        opencv_core.cvConvertScale(this.image, image, 1.0 / this.count, 0);
        opencv_core.cvConvertScale(image, image64f, 1.0 / 255.0, 0);
        OpenCVUtil.imageToGrid(image64f, this.grid);

        image64f.release();
        image.release();
        this.image.release();
    }

    public Grid getGrid() {
        return this.grid;
    }

    public void merge(final Grid grid, long time) {
        this.count++;

        IplImage merge = IplImage.create(opencv_core.cvSize(this.image.width(), this.image.height()),
                opencv_core.IPL_DEPTH_16U, 1);

        IplImage tmp = IplImage.create(opencv_core.cvSize(this.image.width(), this.image.height()),
                opencv_core.IPL_DEPTH_8U, 1);
        IplImage tmp64f = OpenCVUtil.gridToImage(grid);
        opencv_core.cvConvertScale(tmp64f, tmp, 255, 0);

        opencv_core.cvConvert(tmp, merge);

        opencv_core.cvAdd(this.image, merge, this.image, null);

        merge.release();
        tmp.release();
        tmp64f.release();
    }

    @Override
    public String toString() {
        final StringBuffer ret = new StringBuffer("GridPartialAggregate (").append(this.hashCode()).append(")")
                .append(this.grid);
        return ret.toString();
    }

}
