package Lib;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;

public class XFile {
    public static Object readObject(String path){
        try {
            ObjectInputStream objInput = new ObjectInputStream(new FileInputStream(path));
            Object obj = objInput.readObject();
            objInput.close();
            return obj;
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }
    public static void writeObject(String path, Object o) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(o);
            oos.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    public static BufferedImage readImage(File f){
        BufferedImage img = null;

        if (f != null) {
            try {
                img = ImageIO.read(new File(f.getAbsolutePath()));
            } catch (IOException e) {
                System.err.println(e);
            }
        }
        return img;

    }

    public static void copyFile(String originalName, String newName) {
        File originalFile = new File(originalName);
        File newFile = new File(newName);

        try {
            Files.copy(originalFile.toPath(), newFile.toPath());
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
