/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pse.beast.toolbox;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 *
 * @author Niels
 */
public final class FileLoader {

    private FileLoader() {

    }

    /**
     *
     * @param file the file that will be read
     * @return A LinkedList of String elements which are in the same order as in
     * the file
     * @throws FileNotFoundException if the file is not found it throws an
     * exception
     * @throws IOException throws Exception
     */
    public static LinkedList<String> loadFileAsString(File file) throws FileNotFoundException, IOException {

        LinkedList<String> stringlist;
        InputStream inputStream = new FileInputStream(file);
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            stringlist = new LinkedList<>();
            String line;

            line = br.readLine();
            while (line != null) {
                stringlist.add(line);
                line = br.readLine();
            }
            br.close();
        
        return stringlist;
    }

    /**
     * @param toRead the File you want to read
     * @return the image, if it was possible to read it. In case it couldn't be
     * read, the methode returns null
     */
    public static BufferedImage loadFileAsImage(File toRead) {
        BufferedImage toReturn = null;
        try {
            toReturn = ImageIO.read(toRead);
        } catch (IOException e) {
            ErrorLogger.log("The specified file: " + toRead.getAbsolutePath() + " couldn't be loaded");
        }

        return toReturn;
    }

    /**
     * creates a new Name inside a directory
     * @param pathToDir the path of the directory you want the new unique String to be created in 
     * @return the unique String
     */
    public static synchronized  String getNewUniqueName(String pathToDir) {
        ArrayList<String> usedNames = new ArrayList<>();

        File folder = new File(pathToDir.replace("\"", ""));
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    usedNames.add(file.getName());
                }
            }
        }

        String newName = getRandomName(100);
        while (usedNames.contains(newName)) {
            newName = getRandomName(100);
        }

        return newName;
    }

    private static String getRandomName(int wordSize) {
        SecureRandom random = new SecureRandom();
        return new java.math.BigInteger(wordSize, random).toString(32);
    }
    
    /**
     * returns all files that end with the specified String that are in this folder
     * @param pathToDir the path to the folder
     * @param endsWith the String 
     */
    public static List<String> listAllFilesFromFolder(String pathToDir, String endsWith) {
        ArrayList<String> foundFiles = new ArrayList<>();
        
        File folder = new File(pathToDir.replace("\"", ""));
        
        File[] listOfFiles = folder.listFiles();
        
        if (listOfFiles != null) {
            for (File file : listOfFiles) {    
                if (file.isFile() && file.getName().endsWith(endsWith)) {
                    //surround it with quotes in case there are spaces in there
                    foundFiles.add("\"" + file.getAbsolutePath() + "\"");
                }
            }
        }
        return foundFiles;
    }
}
