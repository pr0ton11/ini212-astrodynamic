package ch.hftm.astrodynamic.gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// provides ground track of spaceship above map of planet
public class GroundTrack {

    private static String EMPTY_MAP = "empty";

    private static int TRACK_WIDTH = 2;
    private static Color TRACK_COLOR = Color.GREEN;

    private static int POSITION_WIDTH = 2;
    private static Color POSITION_COLOR = Color.GREEN;
    private static int POSITION_RADIUS = 5;

    private class Point {
        double x;
        double y;
        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    private Canvas canvas;
    private GraphicsContext gc;
    private Map<String, Image> planetMapImages;
    private Image currentMapImage;
    private String currentMapPlanetName;

    private List<Point> trackPoints;
    private Point currentPosition; 

    public GroundTrack(Canvas canvas) {
        this.canvas = canvas;
        gc = this.canvas.getGraphicsContext2D();

        trackPoints = new ArrayList<>();

        planetMapImages = new HashMap<>();
        initImages();
    }

    // here we preload all map images for faster laodtimes in simulation
    private void initImages() {
        planetMapImages.put(EMPTY_MAP, new Image("img/empty_map.png"));
        planetMapImages.put("Earth", new Image("img/World_map_geographical.jpg"));
        planetMapImages.put("Moon", new Image("img/lunar_surface_DEM_gray.png"));

        // init current map image to prevent null
        currentMapPlanetName = EMPTY_MAP;
        currentMapImage = planetMapImages.get(currentMapPlanetName);
    }

    public void setPlanet(String planetName){
        currentMapPlanetName = planetName;
        if (!planetMapImages.containsKey(planetName)) // display empty map if we have no specific map for this planet name
        {
            currentMapImage = planetMapImages.get(EMPTY_MAP);
            return;
        }
        currentMapImage = planetMapImages.get(currentMapPlanetName);
    }

    public String getPlanet() {
        return currentMapPlanetName;
    }

    public void addTrackPoint(double x, double y) {
        trackPoints.add(new Point(x, y));
    }

    public void clearTrack() {
        trackPoints.clear();
    }

    public void setCurrentPosition(double x, double y) {
        currentPosition = new Point(x, y);
    }

    public void draw() {
        drawMap();

        drawGroundTrack();

        drawCurrentPosition();
    }

    private void drawMap() {
        gc.drawImage(currentMapImage, 0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void drawGroundTrack() {
        Point previousPoint = null;

        gc.setStroke(TRACK_COLOR);
        gc.setLineWidth(TRACK_WIDTH);

        for (Point p: trackPoints) {
            if (previousPoint != null) {
                gc.strokeLine(previousPoint.x, previousPoint.y, p.x, p.y);
            }
            previousPoint = p;
        }

        gc.stroke();
    }

    private void drawCurrentPosition() {
        if (currentPosition == null) {
            return;
        }

        gc.setStroke(POSITION_COLOR);
        gc.setLineWidth(POSITION_WIDTH);

        gc.strokeOval(currentPosition.x-POSITION_RADIUS, currentPosition.y-POSITION_RADIUS, currentPosition.x+POSITION_RADIUS, currentPosition.y+POSITION_RADIUS);

        gc.stroke();
    }
}
