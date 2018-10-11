/**
 * 
 */
package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.SeriesChangeEvent;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.xy.AbstractIntervalXYDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;

import com.google.common.base.Preconditions;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@SuppressWarnings("rawtypes")
public class DynamicHistogramDataset extends AbstractIntervalXYDataset implements XYDataset, IntervalXYDataset {
    /**
     * 
     */
    private static final long serialVersionUID = -2693163609859678458L;
    private HistogramType type;
    private final List<TimeSeries> data;
    private Map<Comparable, DynamicHistogramContainer> bins;
    private int binCount;

    public DynamicHistogramDataset() {
        this.type = HistogramType.FREQUENCY;
        this.data = new ArrayList<TimeSeries>();
        this.bins = new HashMap<Comparable, DynamicHistogramContainer>();
    }

    public List<TimeSeries> getSeries() {
        return Collections.unmodifiableList(this.data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Number getEndX(final int series, final int item) {
        final TimeSeries ts = this.data.get(series);
        return this.bins.get(ts.getKey()).getEndX(ts.getValue(item).doubleValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Number getEndY(final int series, final int item) {
        return this.getY(series, item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Number getStartX(final int series, final int item) {
        final TimeSeries ts = this.data.get(series);
        return this.bins.get(ts.getKey()).getStartX(ts.getValue(item).doubleValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Number getStartY(final int series, final int item) {
        return this.getY(series, item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount(final int series) {
        return this.getSeries(series).getItemCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Number getX(final int series, final int item) {
        final TimeSeries ts = this.data.get(series);
        return this.bins.get(ts.getKey()).getCenterX(ts.getValue(item).doubleValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Number getY(final int series, final int item) {
        final TimeSeries ts = this.data.get(series);
        DynamicHistogramContainer histogram = this.bins.get(ts.getKey());
        double value = histogram.getY(ts.getValue(item).doubleValue());
        if (this.type == HistogramType.SCALE_AREA_TO_1) {
            double sum = 0;
            for (int i = 0; i < histogram.bins; i++) {
                sum += histogram.getY(i) * histogram.width(i);
            }
            return value / sum;
        }
        else if (this.type == HistogramType.RELATIVE_FREQUENCY) {
            return new Double(value / this.bins.get(ts.getKey()).sum);
        }
        return new Double(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSeriesCount() {
        return this.data.size();
    }

    public int indexOf(final TimeSeries series) {
        Objects.requireNonNull(series);
        return this.data.indexOf(series);
    }

    public TimeSeries getSeries(final int series) {
        return this.data.get(series);
    }

    public TimeSeries getSeries(final Comparable<?> key) {
        TimeSeries result = null;
        final Iterator<TimeSeries> iterator = this.data.iterator();
        while (iterator.hasNext()) {
            final TimeSeries series = iterator.next();
            final Comparable<?> k = series.getKey();
            if ((k != null) && k.equals(key)) {
                result = series;
            }
        }
        return result;
    }

    public void addSeries(final TimeSeries series) {
        Objects.requireNonNull(series);
        this.data.add(series);
        this.bins.put(series.getKey(), new DynamicHistogramContainer(-10, 10, getBinCount()));
        series.addChangeListener(this);
        this.fireDatasetChanged();
    }

    public void removeSeries(final TimeSeries series) {
        Objects.requireNonNull(series);
        this.bins.remove(series);
        this.data.remove(series);
        series.removeChangeListener(this);
        this.fireDatasetChanged();
    }

    public void removeSeries(final int index) {
        final TimeSeries series = this.getSeries(index);
        if (series != null) {
            this.removeSeries(series);
        }
    }

    public void removeAllSeries() {
        for (int i = 0; i < this.data.size(); i++) {
            final TimeSeries series = this.data.get(i);
            series.removeChangeListener(this);
        }
        this.bins.clear();
        this.data.clear();
        this.fireDatasetChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Comparable<?> getSeriesKey(final int series) {
        return this.getSeries(series).getKey();
    }

    public HistogramType getType() {
        return this.type;
    }

    public void setType(final HistogramType type) {
        Objects.requireNonNull(type);
        this.type = type;
        updateHistogramSettings();
        this.notifyListeners(new DatasetChangeEvent(this, this));
    }

    /**
     * @param binCount
     *            the binCount to set
     */
    public void setBinCount(int binCount) {
        // Preconditions.checkArgument(binCount > 0);
        this.binCount = binCount;
        updateHistogramSettings();
        this.notifyListeners(new DatasetChangeEvent(this, this));
    }

    /**
     * @return the binCount
     */
    public int getBinCount() {
        return this.binCount;
    }

    @SuppressWarnings("unchecked")
    private void updateHistogramSettings() {
        for (TimeSeries serie : getSeries()) {
            DynamicHistogramContainer histogram = new DynamicHistogramContainer(serie.getMinY(), serie.getMaxY(), getBinCount());
            this.bins.put(serie.getKey(), histogram);
            for (TimeSeriesDataItem item : (List<TimeSeriesDataItem>) serie.getItems()) {
                histogram.add(item.getValue().doubleValue());
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public void seriesChanged(final SeriesChangeEvent event) {
        TimeSeries source = (TimeSeries) event.getSource();
        DynamicHistogramContainer histogram = this.bins.get(source.getKey());
        histogram.clear();
        for (TimeSeriesDataItem item : (List<TimeSeriesDataItem>) source.getItems()) {
            histogram.add(item.getValue().doubleValue());
        }
        super.seriesChanged(event);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @SuppressWarnings("unused")
    private class DynamicHistogramContainer {
        private final double[] intervals;
        private final int[] count;
        private final int bins;
        private int sum;

        public DynamicHistogramContainer(final double min, final double max, final int bins) {
            this.bins = bins;
            this.intervals = new double[bins + 1];
            this.count = new int[bins + 2];
            double width = (max - min) / bins;
            double value = min;
            for (int i = 0; i < intervals.length; i++) {
                intervals[i] = value;
                value += width;
            }
        }

        public void add(double value) {
            int bin = valueToBin(value);
            this.count[bin]++;
            this.sum++;
        }

        public void remove(double value) {
            int bin = valueToBin(value);
            this.count[bin]--;
            this.sum--;
        }

        public void clear() {
            Arrays.fill(this.count, 0);
            this.sum = 0;
        }

        public int sum() {
            return sum;
        }

        public double min() {
            return intervals[0];
        }

        public double max() {
            return intervals[intervals.length - 1];
        }

        public double getY(final double value) {
            final int bin = this.valueToBin(value);
            return this.count[bin];
        }

        public double getEndX(final double value) {
            final int bin = this.valueToBin(value);
            return this.getEndX(bin);
        }

        public double getStartX(final double value) {
            final int bin = this.valueToBin(value);
            return this.getStartX(bin);
        }

        public double getCenterX(final double value) {
            final int bin = this.valueToBin(value);
            return this.getCenterX(bin);
        }

        private double getY(final int bin) {
            return this.count[bin];
        }

        private double getEndX(final int bin) {
            int index = bin - 1;
            if (index < 0) {
                return min();
            }
            if ((index + 1) >= this.intervals.length) {
                return Double.POSITIVE_INFINITY;
            }
            return this.intervals[index + 1];
        }

        private double getStartX(final int bin) {
            int index = bin - 1;
            if (index < 0) {
                return Double.NEGATIVE_INFINITY;
            }
            if (index >= this.intervals.length) {
                return max();
            }
            return this.intervals[index];
        }

        private double getCenterX(final int bin) {
            return (this.getEndX(bin) + this.getStartX(bin)) / 2.0;
        }

        private double width(int bin) {
            return this.getEndX(bin) - this.getStartX(bin);
        }

        private int valueToBin(final double value) {
            if (value < min()) {
                return 0;
            }
            if (value > max()) {
                return this.intervals.length;
            }
            int index = Arrays.binarySearch(this.intervals, value);
            if (index < 0) {
                index = -index - 2;
            }
            if (index >= this.bins) {
                return this.intervals.length;
            }
            return index + 1;
        }
    }

}
