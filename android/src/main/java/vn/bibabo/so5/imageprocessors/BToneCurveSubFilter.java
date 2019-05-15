package vn.bibabo.so5.imageprocessors;

import com.zomato.photofilters.geometry.BezierSpline;
import com.zomato.photofilters.geometry.Point;
import com.zomato.photofilters.imageprocessors.subfilters.ToneCurveSubFilter;


/**
 * @author varun
 * Subfilter to tweak rgb channels of an image
 */
public class BToneCurveSubFilter extends ToneCurveSubFilter implements BSubFilter {
    // These are knots which contains the plot points
    protected Point[] rgbKnots;
    protected Point[] greenKnots;
    protected Point[] redKnots;
    protected Point[] blueKnots;
    protected int[] rgb;
    protected int[] r;
    protected int[] g;
    protected int[] b;

    /**
     * Initialise ToneCurveSubfilter (NOTE : Don't pass null knots, pass straight line instead)
     * Knots are the points in 2D taken by tweaking photoshop channels(plane ranging from 0-255)
     *
     * @param rgbKnots   rgb Knots
     * @param redKnots   Knots in Red Channel
     * @param greenKnots Knots in green Channel
     * @param blueKnots  Knots in Blue channel
     */
    public BToneCurveSubFilter(Point[] rgbKnots, Point[] redKnots, Point[] greenKnots, Point[] blueKnots) {
        super(rgbKnots, redKnots, greenKnots, blueKnots);

        Point[] straightKnots = new Point[2];
        straightKnots[0] = new Point(0, 0);
        straightKnots[1] = new Point(255, 255);
        if (rgbKnots == null) {
            this.rgbKnots = straightKnots;
        } else {
            this.rgbKnots = rgbKnots;
        }
        if (redKnots == null) {
            this.redKnots = straightKnots;
        } else {
            this.redKnots = redKnots;
        }
        if (greenKnots == null) {
            this.greenKnots = straightKnots;
        } else {
            this.greenKnots = greenKnots;
        }
        if (blueKnots == null) {
            this.blueKnots = straightKnots;
        } else {
            this.blueKnots = blueKnots;
        }
    }

    @Override
    public int[] apply(int[] inputPixels, int width, int height) {
        rgbKnots = sortPointsOnXAxis(rgbKnots);
        redKnots = sortPointsOnXAxis(redKnots);
        greenKnots = sortPointsOnXAxis(greenKnots);
        blueKnots = sortPointsOnXAxis(blueKnots);
        if (rgb == null) {
            rgb = BezierSpline.curveGenerator(rgbKnots);
        }

        if (r == null) {
            r = BezierSpline.curveGenerator(redKnots);
        }

        if (g == null) {
            g = BezierSpline.curveGenerator(greenKnots);
        }

        if (b == null) {
            b = BezierSpline.curveGenerator(blueKnots);
        }

        BImageProcessor.applyCurves(rgb, r, g, b, inputPixels, width, height);
        return inputPixels;
    }
}
