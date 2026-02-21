/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.catsoftware.engine.prism.draw;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A freehand stroke object that can be drawn on the canvas.
 * Supports variable brush size, opacity, rotation, and closed shape filling.
 *
 * @author lynxjr
 * @version 1.0.00
 * @since Prism B1
 */
public class StrokeObject implements DrawableObject, Serializable {
    private List<Point> points;
    private Color color;
    private int size;
    private boolean highlight;
    private List<Point> originalPoints;
    private int originalSize;
    private static final boolean DEBUG = true;
    private static int debugId = 0;
    private int id;
    private float alpha = 1.0f;
    private double rotation = 0;
    private Point fixedAnchor;
    
    // Store the original bounds min values
    private int originalMinX;
    private int originalMinY;
    private int originalMaxX;
    private int originalMaxY;

    /**
     * Returns the list of points in this stroke.
     *
     * @return The list of points
     */
    public List<Point> getPoints() { return points; }
    
    /**
     * Returns the stroke color.
     *
     * @return The color
     */
    public Color getColor() { return color; }
    
    /**
     * Returns the brush size.
     *
     * @return The brush size in pixels
     */
    public int getBrushSize() { return size; }
    
    /**
     * Checks if this is a highlight stroke.
     *
     * @return true if highlight mode is enabled
     */
    public boolean isHighlight() { return highlight; }

    /**
     * Creates a new stroke object.
     *
     * @param pts The list of points that make up the stroke
     * @param c The stroke color
     * @param s The brush size
     * @param h true for highlight mode (semi-transparent)
     * @param a The opacity level (0.0 - 1.0)
     */
    public StrokeObject(List<Point> pts, Color c, int s, boolean h, float a) {
        id = debugId++;
        points = new ArrayList<>(pts);
        originalPoints = new ArrayList<>();
        for (Point p : pts) {
            originalPoints.add(new Point(p));
        }
        color = c;
        size = s;
        originalSize = s;
        highlight = h;
        alpha = a;
        calculateOriginalBounds();
        
        if (DEBUG) {
            System.out.println("[StrokeObject " + id + "] Created at bounds: " + getBounds());
        }
    }

    /**
     * Calculates the original bounding box of the stroke.
     */
    private void calculateOriginalBounds() {
        originalMinX = Integer.MAX_VALUE;
        originalMinY = Integer.MAX_VALUE;
        originalMaxX = Integer.MIN_VALUE;
        originalMaxY = Integer.MIN_VALUE;
        
        for (Point p : originalPoints) {
            originalMinX = Math.min(originalMinX, p.x);
            originalMinY = Math.min(originalMinY, p.y);
            originalMaxX = Math.max(originalMaxX, p.x);
            originalMaxY = Math.max(originalMaxY, p.y);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        // Save original composite
        Composite originalComposite = g.getComposite();
        
        // Apply alpha
        if (alpha < 1.0f) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        }
        
        // Save transform for rotation
        AffineTransform original = g.getTransform();
        Rectangle bounds = getBounds();
        g.rotate(Math.toRadians(rotation), bounds.getCenterX(), bounds.getCenterY());
        
        // Handle highlight mode
        if (highlight) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f * alpha));
        }
        
        g.setColor(color);
        g.setStroke(new BasicStroke(size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        for (int i = 1; i < points.size(); i++) {
            Point a = points.get(i - 1);
            Point b = points.get(i);
            g.drawLine(a.x, a.y, b.x, b.y);
        }
        
        // Restore transform and composite
        g.setTransform(original);
        g.setComposite(originalComposite);
    }

    @Override
    public boolean contains(Point p) {
        if (points.size() < 3) {
            // A line or dot can't have an "inside" - check if click is on the stroke
            for (Point pt : points) {
                if (pt.distance(p) < size + 2) {
                    return true;
                }
            }
            return false;
        }
        
        // Check if the stroke is approximately closed
        Point first = points.get(0);
        Point last = points.get(points.size() - 1);
        boolean isClosed = first.distance(last) < size * 2;
        
        if (!isClosed) {
            // Open stroke - just check if click is on the stroke line
            for (Point pt : points) {
                if (pt.distance(p) < size + 2) {
                    return true;
                }
            }
            return false;
        }
        
        // Closed stroke - use point-in-polygon test to check if click is INSIDE
        return isPointInPolygon(p.x, p.y);
    }

    /**
     * Finds the closest distance from a point to any point in the stroke.
     *
     * @param p The point to check
     * @return The minimum distance
     */
    private double getClosestDistance(Point p) {
        double minDist = Double.MAX_VALUE;
        for (Point pt : points) {
            minDist = Math.min(minDist, pt.distance(p));
        }
        return minDist;
    }

    @Override
    public void moveBy(int dx, int dy) {
        // Move current points
        for (Point pt : points) {
            pt.translate(dx, dy);
        }
        
        // Move original points
        for (Point pt : originalPoints) {
            pt.translate(dx, dy);
        }
        
        // Update original bounds
        originalMinX += dx;
        originalMinY += dy;
        originalMaxX += dx;
        originalMaxY += dy;
    }
    
    @Override
    public Rectangle getBounds() {
        if (points.isEmpty()) return new Rectangle();
        
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        
        for (Point p : points) {
            minX = Math.min(minX, p.x);
            minY = Math.min(minY, p.y);
            maxX = Math.max(maxX, p.x);
            maxY = Math.max(maxY, p.y);
        }
        
        Rectangle unrotated = new Rectangle(
            minX - size,
            minY - size,
            (maxX - minX) + size * 2,
            (maxY - minY) + size * 2
        );
        
        if (rotation == 0) {
            return unrotated;
        }
        
        // Calculate rotated bounds
        double rad = Math.toRadians(rotation);
        double cos = Math.abs(Math.cos(rad));
        double sin = Math.abs(Math.sin(rad));
        
        int newWidth = (int)(unrotated.width * cos + unrotated.height * sin);
        int newHeight = (int)(unrotated.width * sin + unrotated.height * cos);
        
        int centerX = unrotated.x + unrotated.width/2;
        int centerY = unrotated.y + unrotated.height/2;
        
        int newX = centerX - newWidth/2;
        int newY = centerY - newHeight/2;
        
        return new Rectangle(newX, newY, newWidth, newHeight);
    }

    @Override
    public void resize(double scaleX, double scaleY) {
        double scale = Math.min(scaleX, scaleY);
        Rectangle bounds = getBounds();
        int newWidth = (int)(bounds.width * scale);
        int newHeight = (int)(bounds.height * scale);
        resizeAbsolute(newWidth, newHeight);
    }

    /**
     * Sets a fixed anchor point for resizing operations.
     *
     * @param anchor The anchor point
     */
    public void setFixedAnchor(Point anchor) {
        this.fixedAnchor = new Point(anchor);
    }

    @Override
    public void resizeAbsolute(int newWidth, int newHeight) {
        if (originalPoints.isEmpty()) return;
        
        // Use the FIXED anchor that was set at the start of drag
        if (fixedAnchor == null) {
            // Fallback - should not happen
            Rectangle bounds = getBounds();
            fixedAnchor = new Point(bounds.x + size/2, bounds.y + size/2);
        }
        
        if (DEBUG) {
            System.out.println("\n=== StrokeObject " + id + " Resize ===");
            System.out.println("Using FIXED anchor: " + fixedAnchor);
            System.out.println("Current bounds: " + getBounds());
        }
        
        int origWidth = originalMaxX - originalMinX;
        int origHeight = originalMaxY - originalMinY;
        
        if (origWidth == 0 && origHeight == 0) {
            double scale = Math.min(
                (double)newWidth / (size * 2),
                (double)newHeight / (size * 2)
            );
            size = Math.max(1, (int)(originalSize * scale));
            return;
        }
        
        // Calculate scale factors
        double scaleX = (double)(newWidth - size * 2) / origWidth;
        double scaleY = (double)(newHeight - size * 2) / origHeight;
        double scale = Math.min(scaleX, scaleY);
        
        // Clear and rebuild points
        points.clear();
        
        // Scale points from original, anchored at the FIXED position
        for (Point orig : originalPoints) {
            int relX = orig.x - originalMinX;
            int relY = orig.y - originalMinY;
            
            int scaledRelX = (int)(relX * scale);
            int scaledRelY = (int)(relY * scale);
            
            points.add(new Point(
                fixedAnchor.x + scaledRelX,
                fixedAnchor.y + scaledRelY
            ));
        }
        
        // Scale brush size
        size = Math.max(1, (int)(originalSize * scale));
    }

    /**
     * Clears the fixed anchor point after resizing is complete.
     */
    public void clearFixedAnchor() {
        fixedAnchor = null;
    }

    @Override
    public void fill(Graphics2D g, Color fillColor) {
        System.out.println("  StrokeObject.fill() called");
        System.out.println("  Points size: " + points.size());
        
        if (points.size() < 3) {
            System.out.println("  Less than 3 points - can't fill, just recolor");
            this.color = fillColor;
            draw(g);
            return;
        }
        
        // Check if stroke is approximately closed
        Point first = points.get(0);
        Point last = points.get(points.size() - 1);
        double distance = first.distance(last);
        System.out.println("  Distance between first and last point: " + distance);
        System.out.println("  Brush size: " + size);
        
        if (distance > size * 2) {
            System.out.println("  Stroke is open - can't fill, just recolor");
            this.color = fillColor;
            draw(g);
            return;
        }
        
        System.out.println("  Stroke is closed - filling polygon");
        
        // Create a closed polygon from the points
        int[] xPoints = new int[points.size()];
        int[] yPoints = new int[points.size()];
        
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            xPoints[i] = p.x;
            yPoints[i] = p.y;
        }
        
        // Save original color
        Color originalColor = color;
        
        // Fill the polygon
        g.setColor(fillColor);
        g.fillPolygon(xPoints, yPoints, points.size());
        
        // Restore original color for the outline
        g.setColor(originalColor);
        g.setStroke(new BasicStroke(size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawPolygon(xPoints, yPoints, points.size());
        
        System.out.println("  Polygon fill complete");
    }

    /**
     * Checks if a point is inside the closed stroke polygon.
     *
     * @param x The X coordinate
     * @param y The Y coordinate
     * @return true if the point is inside
     */
    private boolean isPointInPolygon(int x, int y) {
        if (points.size() < 3) return false;
        
        // Ray casting algorithm - count intersections with polygon edges
        int intersections = 0;
        int n = points.size();
        
        for (int i = 0; i < n; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get((i + 1) % n);
            
            // Check if the ray from (x,y) to infinity intersects this edge
            if (((p1.y > y) != (p2.y > y)) &&
                (x < (p2.x - p1.x) * (y - p1.y) / (p2.y - p1.y) + p1.x)) {
                intersections++;
            }
        }
        
        return (intersections % 2) == 1;
    }

    /**
     * Sets the stroke color.
     *
     * @param color The new color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Sets the brush size.
     *
     * @param size The new size in pixels
     */
    public void setBrushSize(int size) {
        this.size = size;
    }

    /**
     * Sets the opacity level.
     *
     * @param alpha Value between 0.1 and 1.0
     */
    public void setAlpha(float alpha) {
        this.alpha = Math.max(0.1f, Math.min(1.0f, alpha));
    }

    /**
     * Returns the opacity level.
     *
     * @return The current alpha value
     */
    public float getAlpha() {
        return alpha;
    }

    @Override
    public void setRotation(double degrees) {
        this.rotation = degrees;
    }

    @Override
    public double getRotation() {
        return rotation;
    }

    @Override
    public void rotate(double delta) {
        rotation += delta;
        rotation = rotation % 360;
    }

    /**
     * Sets a gradient (not supported for strokes - falls back to solid color).
     *
     * @param color1 Start color (ignored)
     * @param color2 End color (ignored)
     * @param horizontal Direction (ignored)
     */
    public void setGradient(Color color1, Color color2, boolean horizontal) {
        // Strokes don't support gradients - just set color
        this.color = color1;
    }

    @Override
    public boolean hasGradient() {
        return false;
    }

    @Override
    public void removeGradient() {
        // Nothing to remove
    }
}
