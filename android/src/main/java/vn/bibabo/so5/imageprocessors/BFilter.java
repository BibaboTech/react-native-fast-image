package vn.bibabo.so5.imageprocessors;


import android.support.annotation.NonNull;

import com.zomato.photofilters.imageprocessors.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * This Class represents a ImageFilter and includes many subfilters within, we add different subfilters to this class's
 * object and they are then processed in that particular order
 */
public class BFilter extends Filter {
    protected List<BSubFilter> mSubFilters = new ArrayList<>();

    public BFilter(BFilter filter) {
        super(filter);
        this.mSubFilters = filter.mSubFilters;
    }

    public BFilter() {
    }

    /**
     * Adds a Subfilter to the Main BFilter
     *
     * @param subFilter Subfilter like contrast, brightness, tone Curve etc. subfilter
     * @see BBrightnessSubFilter
     * @see BColorOverlaySubFilter
     * @see BContrastSubFilter
     * @see BToneCurveSubFilter
     * @see BVignetteSubFilter
     * @see BSaturationSubFilter
     */
    public void addBSubFilter(BSubFilter subFilter) {
        mSubFilters.add(subFilter);
    }

    /**
     * Adds all {@link BSubFilter}s from the List to the Main BFilter.
     * @param subFilterList a list of {@link BSubFilter}s; must not be null
     */
    public void addBSubFilters(@NonNull List<BSubFilter> subFilterList) {
        mSubFilters.addAll(subFilterList);
    }

    /**
     * Get a new list of currently applied subfilters.
     *
     * @return a {@link List} of {@link BSubFilter}.
     *         Empty if no filters are added to {@link #subFilters};
     *         never null
     *
     * @see #addBSubFilter(BSubFilter)
     * @see #addBSubFilters(List)
     */
    @NonNull
    public List<BSubFilter> getSubFilters() {
        if (mSubFilters == null || mSubFilters.isEmpty())
            return new ArrayList<>(0);
        return new ArrayList<>(mSubFilters);
    }

    /**
     * Clears all the subfilters from the Parent BFilter
     */
    public void clearSubFilters() {
        mSubFilters.clear();
    }


    /**
     * Give the output pixel array by applying the defined filter
     *
     * @param inputPixels Input pixel array on which filter is to be applied
     * @param width width of image
     * @param height height of image
     * @return filtered pixel array
     */

    public int[] processFilter(int[] inputPixels, int width, int height) {
        try {
            for (BSubFilter subFilter : mSubFilters) {
                try {
                    inputPixels = subFilter.apply(inputPixels, width, height);
                } catch (OutOfMemoryError oe) {
                    System.gc();
                    try {
                        inputPixels = subFilter.apply(inputPixels, width, height);
                    } catch (OutOfMemoryError ignored) {
                    }
                }
            }
        } catch (Throwable t) {
        }
        return inputPixels;
    }

}
