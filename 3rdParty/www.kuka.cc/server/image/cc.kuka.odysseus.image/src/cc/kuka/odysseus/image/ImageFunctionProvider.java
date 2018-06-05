/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package cc.kuka.odysseus.image;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.kuka.odysseus.image.function.image.CIELCHToRGBFunction;
import cc.kuka.odysseus.image.function.image.CIELabToRGBFunction;
import cc.kuka.odysseus.image.function.image.CIELuvToRGBFunction;
import cc.kuka.odysseus.image.function.image.CMYKToRGBFunction;
import cc.kuka.odysseus.image.function.image.CMYToRGBFunction;
import cc.kuka.odysseus.image.function.image.FaceDectionFunction;
import cc.kuka.odysseus.image.function.image.FacesFunction;
import cc.kuka.odysseus.image.function.image.FillImageFunction;
import cc.kuka.odysseus.image.function.image.GetImageFunction;
import cc.kuka.odysseus.image.function.image.HSLToRGBFunction;
import cc.kuka.odysseus.image.function.image.HSVToRGBFunction;
import cc.kuka.odysseus.image.function.image.HunterLabToRGBFunction;
import cc.kuka.odysseus.image.function.image.InverseImageFunction;
import cc.kuka.odysseus.image.function.image.MaxFunction;
import cc.kuka.odysseus.image.function.image.MaxLocationFunction;
import cc.kuka.odysseus.image.function.image.MinFunction;
import cc.kuka.odysseus.image.function.image.MinLocationFunction;
import cc.kuka.odysseus.image.function.image.RGBToCIELCHFunction;
import cc.kuka.odysseus.image.function.image.RGBToCIELabFunction;
import cc.kuka.odysseus.image.function.image.RGBToCIELuvFunction;
import cc.kuka.odysseus.image.function.image.RGBToCMYFunction;
import cc.kuka.odysseus.image.function.image.RGBToCMYKFunction;
import cc.kuka.odysseus.image.function.image.RGBToHSLFunction;
import cc.kuka.odysseus.image.function.image.RGBToHSVFunction;
import cc.kuka.odysseus.image.function.image.RGBToHexFunction;
import cc.kuka.odysseus.image.function.image.RGBToHunterLabFunction;
import cc.kuka.odysseus.image.function.image.RGBToXYZFunction;
import cc.kuka.odysseus.image.function.image.RGBToYxyFunction;
import cc.kuka.odysseus.image.function.image.ResizeImageFunction;
import cc.kuka.odysseus.image.function.image.RotateImageFunction;
import cc.kuka.odysseus.image.function.image.SetImageFunction;
import cc.kuka.odysseus.image.function.image.SharpeningFunction;
import cc.kuka.odysseus.image.function.image.SmileDectionFunction;
import cc.kuka.odysseus.image.function.image.SubImageFunction;
import cc.kuka.odysseus.image.function.image.ToImageFunction;
import cc.kuka.odysseus.image.function.image.ToImageMatrixFunction;
import cc.kuka.odysseus.image.function.image.ToMatrixFunction;
import cc.kuka.odysseus.image.function.image.XYZToRGBFunction;
import cc.kuka.odysseus.image.function.image.YxyToRGBFunction;
import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class ImageFunctionProvider implements IFunctionProvider {
    private static final Logger LOG = LoggerFactory.getLogger(ImageFunctionProvider.class);

    public ImageFunctionProvider() {
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public List<IMepFunction<?>> getFunctions() {

        final List<IMepFunction<?>> functions = new ArrayList<>();
        functions.add(new RotateImageFunction());
        functions.add(new SubImageFunction());
        functions.add(new ResizeImageFunction());
        functions.add(new FillImageFunction());
        functions.add(new SharpeningFunction());

        functions.add(new InverseImageFunction());
        functions.add(new ToImageFunction());
        functions.add(new ToImageMatrixFunction());
        functions.add(new ToMatrixFunction());
        functions.add(new SetImageFunction());
        functions.add(new GetImageFunction());

        functions.add(new MinFunction());
        functions.add(new MinLocationFunction());
        functions.add(new MaxFunction());
        functions.add(new MaxLocationFunction());

        // RGB to *
        functions.add(new RGBToHexFunction());
        functions.add(new RGBToCIELabFunction());
        functions.add(new RGBToCIELCHFunction());
        functions.add(new RGBToCIELuvFunction());
        functions.add(new RGBToCMYFunction());
        functions.add(new RGBToCMYKFunction());
        functions.add(new RGBToHSLFunction());
        functions.add(new RGBToHSVFunction());
        functions.add(new RGBToHunterLabFunction());
        functions.add(new RGBToXYZFunction());
        functions.add(new RGBToYxyFunction());

        // * to RGB
        functions.add(new CIELabToRGBFunction());
        functions.add(new CIELCHToRGBFunction());
        functions.add(new CIELuvToRGBFunction());
        functions.add(new CMYToRGBFunction());
        functions.add(new CMYKToRGBFunction());
        functions.add(new HSLToRGBFunction());
        functions.add(new HSVToRGBFunction());
        functions.add(new HunterLabToRGBFunction());
        functions.add(new XYZToRGBFunction());
        functions.add(new YxyToRGBFunction());

        functions.add(new FaceDectionFunction());
        functions.add(new SmileDectionFunction());

        functions.add(new FacesFunction());

        ImageFunctionProvider.LOG.info(String.format("Register functions: %s", functions));
        return functions;
    }

}
