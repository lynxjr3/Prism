/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.catsoftware.engine.prism.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Handles saving and loading of complete Prism documents (.prism files).
 * Uses Java serialization with buffered streams for better performance.
 *
 * @author lynxjr
 * @version 1.0.00
 * @since Prism B1
 */
public class PrismIO {
    
    /**
     * Saves a Prism document to a file.
     * The document is serialized using Java's object serialization.
     *
     * @param file The destination file
     * @param doc The PrismDocument to save
     * @throws IOException If an I/O error occurs
     */
    public static void save(File file, PrismDocument doc) throws IOException {
        try (ObjectOutputStream out =
                 new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {

            out.writeObject(doc);
        }
    }

    /**
     * Loads a Prism document from a file.
     * The file must have been created by {@link #save}.
     *
     * @param file The source file
     * @return The loaded PrismDocument
     * @throws IOException If an I/O error occurs
     * @throws ClassNotFoundException If the serialized class cannot be found
     */
    public static PrismDocument load(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in =
                 new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {

            return (PrismDocument) in.readObject();
        }
    }
}

