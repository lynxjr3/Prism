/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.catsoftware.engine.prism.draw;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

/**
 * Base interface for all drawable objects in Prism.
 * All vector objects (shapes, strokes, text, images) implement this interface.
 *
 * @author lynxjr
 * @version 1.0.00
 * @since Prism B1
 */
public interface DrawableObject extends Serializable {
    
    /**
     * Renders this object onto a graphics context.
     * 
     * @param g The graphics context to draw on
     */
    public abstract void draw(Graphics2D g);

    /**
     * Checks if a point is inside/on this object.
     * Used for selection and hit testing.
     * 
     * @param p The point to check
     * @return true if point is inside/on this object
     */
    public abstract boolean contains(Point p);

    /**
     * Moves this object by the specified delta.
     * 
     * @param dx Horizontal offset
     * @param dy Vertical offset
     */
    public abstract void moveBy(int dx, int dy);
    
    /**
     * Returns the bounding rectangle of this object.
     * 
     * @return The bounding box
     */
    Rectangle getBounds();
    
    /**
     * Scales this object by the given factors.
     * 
     * @param scaleX Horizontal scale factor
     * @param scaleY Vertical scale factor
     */
    public abstract void resize(double scaleX, double scaleY);
    
    /**
     * Resizes this object to absolute dimensions.
     * 
     * @param newWidth New width in pixels
     * @param newHeight New height in pixels
     */
    public abstract void resizeAbsolute(int newWidth, int newHeight);
    
    /**
     * Fills this object with the specified color.
     * 
     * @param g Graphics context (may be null for vector fills)
     * @param fillColor The color to fill with
     */
    public abstract void fill(Graphics2D g, Color fillColor);

    /**
     * Sets the rotation angle of this object.
     * 
     * @param degrees Rotation angle in degrees
     */
    public abstract void setRotation(double degrees);
    
    /**
     * Returns the current rotation angle.
     * 
     * @return Rotation angle in degrees
     */
    public abstract double getRotation();
    
    /**
     * Rotates this object by the specified delta.
     * 
     * @param delta Degrees to add to current rotation
     */
    public abstract void rotate(double delta);
    
    /**
     * Applies a gradient fill to this object.
     * 
     * @param color1 Start color
     * @param color2 End color
     * @param horizontal true for horizontal gradient, false for vertical
     */
    public abstract void setGradient(Color color1, Color color2, boolean horizontal);
    
    /**
     * Checks if this object has a gradient fill.
     * 
     * @return true if gradient is enabled
     */
    public abstract boolean hasGradient();
    
    /**
     * Removes gradient fill from this object.
     */
    public abstract void removeGradient();
}
