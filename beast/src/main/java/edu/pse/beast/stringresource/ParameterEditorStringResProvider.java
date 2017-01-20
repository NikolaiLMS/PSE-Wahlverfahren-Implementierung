/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pse.beast.stringresource;

import edu.pse.beast.toolbox.FileLoader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

/**
 *
 * @author Niels
 */
public class ParameterEditorStringResProvider extends StringResourceProvider{
    private StringResourceLoader menuStringRes;
    private StringResourceLoader toolbarTipStringRes;
    private StringResourceLoader otherStringRes;
    
    public ParameterEditorStringResProvider(String languageId, String relativePath) {
        super(languageId, relativePath);
        File toolbarFile;
        toolbarFile = new File(getFileLocationString("ParameterEditorToolbar"));
        try {
            LinkedList<String> toolbarList;
            toolbarList = FileLoader.loadFileAsString(toolbarFile);
            toolbarTipStringRes = new StringResourceLoader(toolbarList);
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
        File menuFile;
        menuFile = new File(getFileLocationString("ParameterEditorMenu"));
        try {
            LinkedList<String> menuList;
            menuList = FileLoader.loadFileAsString(menuFile);
            menuStringRes = new StringResourceLoader(menuList);
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
        File otherParam;
        otherParam = new File(getFileLocationString("ParameterEditorOther"));
        try {
            LinkedList<String> otherList;
            otherList = FileLoader.loadFileAsString(otherParam);
            otherStringRes = new StringResourceLoader(otherList);
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    public StringResourceLoader getMenuStringRes() {
        return menuStringRes;
    }

    public StringResourceLoader getToolbarTipStringRes() {
        return toolbarTipStringRes;
    }

    public StringResourceLoader getOtherStringRes() {
        return otherStringRes;
    }
    
}
