/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.catsoftware.engine.prism.draw;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

/**
 * A resize handle for manipulating objects on the canvas.
 * Provides visual handles for resizing and rotating selected objects.
 *
 * @author lynxjr
 * @version 1.0.00
 * @since Prism B1
 */
public class ResizeHandle implements Serializable {
    
    /**
     * Possible positions for resize handles.
     */
    public enum Position {
        /** Top-left corner handle */
        TOP_LEFT, 
        /** Top-right corner handle */
        TOP_RIGHT, 
        /** Bottom-left corner handle */
        BOTTOM_LEFT, 
        /** Bottom-right corner handle */
        BOTTOM_RIGHT,
        /** Top edge center handle */
        TOP, 
        /** Bottom edge center handle */
        BOTTOM, 
        /** Left edge center handle */
        LEFT, 
        /** Right edge center handle */
        RIGHT,
        /** Rotation handle (above object) */
        ROTATE
    }
    
    private Position position;
    private Rectangle bounds;
    private static final int HANDLE_SIZE = 8;
    
    /**
     * Creates a new resize handle.
     *
     * @param position The handle position
     */
    public ResizeHandle(Position position) {
        this.position = position;
        this.bounds = new Rectangle(0, 0, HANDLE_SIZE, HANDLE_SIZE);
    }
    
    /**
     * Updates the handle position based on object bounds.
     *
     * @param objectBounds The bounding rectangle of the object
     */
    public void updatePosition(Rectangle objectBounds) {
        switch (position) {
            case TOP_LEFT:
                bounds.setLocation(objectBounds.x, objectBounds.y);
                break;
            case TOP_RIGHT:
                bounds.setLocation(objectBounds.x + objectBounds.width - HANDLE_SIZE, 
                                  objectBounds.y);
                break;
            case BOTTOM_LEFT:
                bounds.setLocation(objectBounds.x, 
                                  objectBounds.y + objectBounds.height - HANDLE_SIZE);
                break;
            case BOTTOM_RIGHT:
                bounds.setLocation(objectBounds.x + objectBounds.width - HANDLE_SIZE,
                                  objectBounds.y + objectBounds.height - HANDLE_SIZE);
                break;
            case TOP:
                bounds.setLocation(objectBounds.x + objectBounds.width/2 - HANDLE_SIZE/2,
                                  objectBounds.y);
                break;
            case BOTTOM:
                bounds.setLocation(objectBounds.x + objectBounds.width/2 - HANDLE_SIZE/2,
                                  objectBounds.y + objectBounds.height - HANDLE_SIZE);
                break;
            case LEFT:
                bounds.setLocation(objectBounds.x,
                                  objectBounds.y + objectBounds.height/2 - HANDLE_SIZE/2);
                break;
            case RIGHT:
                bounds.setLocation(objectBounds.x + objectBounds.width - HANDLE_SIZE,
                                  objectBounds.y + objectBounds.height/2 - HANDLE_SIZE/2);
                break;
            case ROTATE:
                // Position above the object
                bounds.setLocation(
                    objectBounds.x + objectBounds.width/2 - HANDLE_SIZE/2,
                    objectBounds.y - HANDLE_SIZE - 5
                );
                break;
        }
        bounds.width = HANDLE_SIZE;
        bounds.height = HANDLE_SIZE;
    }
    
    /**
     * Draws the resize handle on the canvas.
     *
     * @param g The graphics context to draw on
     */
    public void draw(Graphics2D g) {
        // Fill with blue
        g.setColor(new Color(0, 120, 255));
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        
        // Draw white border
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(1.5f));
        g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
        
        // Draw inner highlight for better visibility
        g.setColor(new Color(200, 230, 255));
        g.drawRect(bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2);
    }
    
    /**
     * Checks if a point is inside this handle.
     *
     * @param p The point to check
     * @return true if point is inside
     */
    public boolean contains(Point p) {
        return bounds.contains(p);
    }
    
    /**
     * Returns the appropriate cursor for this handle.
     *
     * @return The cursor to display
     */
    public Cursor getCursor() {
        switch (position) {
            case TOP_LEFT: return Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);
            case TOP_RIGHT: return Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);
            case BOTTOM_LEFT: return Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR);
            case BOTTOM_RIGHT: return Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
            case TOP: return Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
            case BOTTOM: return Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
            case LEFT: return Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR);
            case RIGHT: return Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
            case ROTATE:
                return Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
            default: return Cursor.getDefaultCursor();
        }
    }
    
    /**
     * Returns the position of this handle.
     *
     * @return The handle position
     */
    public Position getPosition() {
        return position;
    }
}
