package com.catsoftware.engine.prism.io;

import com.catsoftware.engine.prism.draw.DrawableObject;
import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a complete Prism document containing both raster and vector data.
 * This is the main file format for saving and loading Prism projects (.prism files).
 * Includes version tracking for backward compatibility.
 *
 * @author lynxjr
 * @version 1.0.00
 * @since Prism B1
 */
public class PrismDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    private int version = 1;

    public int canvasWidth;
    public int canvasHeight;

    private String creatorVersion;
    private int creatorVersionCode;
    private long timestamp;

    public Color backgroundColor = Color.WHITE;

    public boolean infinite;
    public boolean gridEnabled;

    public List<DrawableObject> objects = 
        Collections.synchronizedList(new ArrayList<>());

    private int rasterWidth;
    private int rasterHeight;
    private int[] rasterPixels;

    /**
     * Creates a new empty Prism document.
     * Automatically sets the creator version and timestamp.
     */
    public PrismDocument() {
        this.creatorVersion = PrismVersion.VERSION;
        this.creatorVersionCode = PrismVersion.VERSION_CODE;
        this.timestamp = System.currentTimeMillis();
    }

    // ------------------ Objects ------------------

    /**
     * Returns the list of vector objects in this document.
     * The list is thread-safe (synchronized).
     *
     * @return The list of drawable objects
     */
    public List<DrawableObject> getObjects() {
        return objects;
    }

    /**
     * Sets the list of vector objects in this document.
     *
     * @param objects The new list of objects (null becomes empty list)
     */
    public void setObjects(List<DrawableObject> objects) {
        this.objects = objects != null ? objects : new ArrayList<>();
    }

    // ------------------ Raster ------------------

    /** @return The width of the raster image in pixels */
    public int getRasterWidth() { return rasterWidth; }
    
    /** @param w The new raster width */
    public void setRasterWidth(int w) { rasterWidth = w; }

    /** @return The height of the raster image in pixels */
    public int getRasterHeight() { return rasterHeight; }
    
    /** @param h The new raster height */
    public void setRasterHeight(int h) { rasterHeight = h; }

    /**
     * Returns the raw pixel data of the raster image.
     * Each int represents an ARGB pixel.
     *
     * @return Array of pixel values
     */
    public int[] getRasterPixels() { return rasterPixels; }
    
    /**
     * Sets the raw pixel data of the raster image.
     *
     * @param p Array of ARGB pixel values
     */
    public void setRasterPixels(int[] p) { rasterPixels = p; }

    // ------------------ Grid ------------------

    /** @return true if the grid is enabled */
    public boolean isGridEnabled() { return gridEnabled; }
    
    /** @param gridEnabled true to enable grid */
    public void setGridEnabled(boolean gridEnabled) {
        this.gridEnabled = gridEnabled;
    }

    // ------------------ Metadata ------------------

    /** @return The version string of the Prism app that created this file */
    public String getCreatorVersion() { return creatorVersion; }
    
    /** @return The numeric version code for compatibility checking */
    public int getCreatorVersionCode() { return creatorVersionCode; }
    
    /** @return The timestamp when this document was created */
    public long getTimestamp() { return timestamp; }

    // ------------------ Canvas ------------------

    /** @return The canvas width in pixels */
    public int getCanvasWidth() { return canvasWidth; }
    
    /** @param canvasWidth The new canvas width */
    public void setCanvasWidth(int canvasWidth) {
        this.canvasWidth = canvasWidth;
    }

    /** @return The canvas height in pixels */
    public int getCanvasHeight() { return canvasHeight; }
    
    /** @param canvasHeight The new canvas height */
    public void setCanvasHeight(int canvasHeight) {
        this.canvasHeight = canvasHeight;
    }
}