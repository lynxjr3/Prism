/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.catsoftware.engine.prism.history;

import com.catsoftware.engine.prism.draw.DrawableObject;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a snapshot of the canvas state for undo/redo operations.
 * Stores both the raster image and all vector objects at a point in time.
 * Large images are automatically scaled down to save memory.
 *
 * @author lynxjr
 * @version 1.0.00
 * @since Prism B1
 */
public class CanvasState {
    private BufferedImage raster;
    private List<DrawableObject> objects;
    
    /**
     * Creates a new canvas state snapshot.
     * If the image is larger than 2000x2000, it will be scaled down by 50%
     * to conserve memory in the undo stack.
     *
     * @param raster The raster image at this state
     * @param objects The vector objects at this state
     */
    public CanvasState(BufferedImage raster, List<DrawableObject> objects) {
        // If image is too large, compress it
        if (raster.getWidth() > 2000 || raster.getHeight() > 2000) {
            // Scale down large images for undo storage
            this.raster = createScaledCopy(raster, 0.5);
        } else {
            this.raster = copyImage(raster);
        }
        this.objects = new ArrayList<>(objects);
    }

    /**
     * Creates an exact copy of a BufferedImage.
     *
     * @param src The source image to copy
     * @return A new BufferedImage with the same content
     */
    private BufferedImage copyImage(BufferedImage src) {
        BufferedImage copy = new BufferedImage(
            src.getWidth(),
            src.getHeight(),
            src.getType()
        );
        Graphics g = copy.getGraphics();
        g.drawImage(src, 0, 0, null);
        g.dispose();
        return copy;
    }
    
    /**
     * Returns the raster image at this state.
     *
     * @return The stored BufferedImage
     */
    public BufferedImage getRaster() {
        return raster;
    }
    
    /**
     * Returns the list of vector objects at this state.
     *
     * @return A list of DrawableObjects
     */
    public List<DrawableObject> getObjects() {
        return objects;
    }
    
    /**
     * Creates a scaled-down copy of an image.
     * Used to reduce memory usage for large images in the undo stack.
     *
     * @param src The source image to scale
     * @param scale The scaling factor (e.g., 0.5 for half size)
     * @return A new scaled BufferedImage
     */
    private BufferedImage createScaledCopy(BufferedImage src, double scale) {
        int newWidth = (int)(src.getWidth() * scale);
        int newHeight = (int)(src.getHeight() * scale);
        
        BufferedImage scaled = new BufferedImage(newWidth, newHeight, src.getType());
        Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
                           RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(src, 0, 0, newWidth, newHeight, null);
        g.dispose();
        
        return scaled;
    }
}
