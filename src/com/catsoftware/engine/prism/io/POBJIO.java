/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.catsoftware.engine.prism.io;

import com.catsoftware.engine.prism.draw.DrawableObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Handles saving and loading of Prism objects (.pobj files).
 * Uses Java serialization with GZIP compression for smaller file sizes.
 *
 * @author lynxjr
 * @version 1.0.00
 * @since Prism B1
 */
public class POBJIO {
    
    /**
     * Saves a list of drawable objects to a file.
     * The objects are serialized and compressed using GZIP.
     *
     * @param objects The list of objects to save
     * @param file The destination file
     * @throws IOException If an I/O error occurs
     */
    public static void saveObjects(List<DrawableObject> objects, File file) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new GZIPOutputStream(new FileOutputStream(file)))) {
            // Create a new ArrayList to ensure serialization works correctly
            oos.writeObject(new ArrayList<>(objects));
        }
    }
    
    /**
     * Loads a list of drawable objects from a file.
     * The file must have been created by {@link #saveObjects}.
     *
     * @param file The source file
     * @return The loaded list of objects
     * @throws IOException If an I/O error occurs
     * @throws ClassNotFoundException If the serialized class cannot be found
     */
    @SuppressWarnings("unchecked")
    public static List<DrawableObject> loadObjects(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new GZIPInputStream(new FileInputStream(file)))) {
            return (List<DrawableObject>) ois.readObject();
        }
    }
}
