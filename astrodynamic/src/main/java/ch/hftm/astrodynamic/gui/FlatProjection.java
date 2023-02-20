package ch.hftm.astrodynamic.gui;

import ch.hftm.astrodynamic.model.Simulation;
import ch.hftm.astrodynamic.physics.AstronomicalObject;
import ch.hftm.astrodynamic.physics.Planetoid;
import ch.hftm.astrodynamic.scalar.LengthScalar;
import ch.hftm.astrodynamic.scalar.UnitlessScalar;
import ch.hftm.astrodynamic.utils.Scalar;
import ch.hftm.astrodynamic.utils.UnitConversionError;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// Projects 3d space to 2d space ortographic
public class FlatProjection {
    static final double DEFAULT_ZOOM_FACTOR = 0.0000001;
    static final double DEFAULT_ENLARGE_FACTOR = 10.0;

    private static final int POSITION_WIDTH = 2;
    private static final Color POSITION_COLOR = Color.GREEN;

    private Simulation simulation;

    private Canvas canvas;
    private GraphicsContext gc;

    private Scalar zoomFactor;
    private Scalar enlargeObjectsFactor;

    private AstronomicalObject focus;
    private AstronomicalObject reference;

    private Scalar canvasX;
    private Scalar canvasY;

    public FlatProjection(Canvas canvas, Simulation simulation) {
        this.canvas = canvas;
        this.simulation = simulation;

        gc = canvas.getGraphicsContext2D();

        zoomFactor = new UnitlessScalar(DEFAULT_ZOOM_FACTOR);
        enlargeObjectsFactor = new UnitlessScalar(DEFAULT_ENLARGE_FACTOR);

        // get center of canvas for focus calculation
        canvasX = new LengthScalar(canvas.getWidth()/2.0);
        canvasY = new LengthScalar(canvas.getHeight()/2.0);

        System.out.println("canvas middle " + canvasX.toString() + ", " + canvasY.toString());

        focus = simulation.getAstronomicalObjectByName("Earth");
    }

    public void draw() throws UnitConversionError {

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        Scalar focusX = new LengthScalar(0);
        Scalar focusY = new LengthScalar(0);
        if (focus != null) {
            focusX = focus.getPosition().getX().multiply(zoomFactor);
            focusY = focus.getPosition().getY().multiply(zoomFactor);
        }

        System.out.println("Focus " + focusX.toString() + ", " + focusY.toString());

        gc.setStroke(POSITION_COLOR);
        gc.setLineWidth(POSITION_WIDTH);

        gc.strokeOval(canvasX.getValue().doubleValue() - 1, canvasY.getValue().doubleValue() - 1, 2, 2);
        gc.stroke();

        for (Planetoid p: simulation.getPlanetoids()) {

            Scalar posX = p.getPosition().getX().multiply(zoomFactor).subtract(focusX).add(canvasX);
            Scalar posY = p.getPosition().getY().multiply(zoomFactor).subtract(focusY).add(canvasY);
            Scalar radius = p.getZeroElevation().multiply(enlargeObjectsFactor).multiply(zoomFactor);
            Scalar diameter = radius.multiply(new UnitlessScalar(2.0)); 

            System.out.println(p.getName() + " pos " + posX.toString() + ", " + posY.toString());
            System.out.println(p.getName() + " radius " + radius.toString());

            gc.strokeOval(posX.subtract(radius).getValue().doubleValue(), posY.subtract(radius).getValue().doubleValue(), diameter.getValue().doubleValue(), diameter.getValue().doubleValue());

            gc.stroke();
        }
    }

    public void setFocus(AstronomicalObject focus) {
        this.focus = focus;
    }

    public void setZoomFactor(Scalar zoomFactor) {
        this.zoomFactor = zoomFactor;
    }

    public void setEnlargeFactor(Scalar enlargeFactor) {
        this.enlargeObjectsFactor = enlargeFactor;
    }

    // return as double for ease of use in gui
    public double getZoomFactor() {
        return zoomFactor.getValue().doubleValue();
    }

    // return as double for ease of use in gui
    public double getEnlargeFactor() {
        return enlargeObjectsFactor.getValue().doubleValue();
    }
}
