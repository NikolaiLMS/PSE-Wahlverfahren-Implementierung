/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pse.beast.datatypes.propertydescription;

/**
 *
 * @author Holger
 */
public class FormalPropertiesDescription {

    private String code;
   
    /**
     * @param code the code of the FormalPropertyDescription 
     */
    public FormalPropertiesDescription(String code) {
        this.code = code;
    }
    
    /**
     * 
     * @return the code of the FormalPropertyDescription
     */
    public String getCode() {
        return code;
    }

    /**
     * /**
     * @param code the code of the FormalPropertyDescription 
     */
    public void setCode(String code) {
        this.code = code;
    }
//    public BooleanExpListNode getAST(){
//        return astGenerator.generateFromSyntaxTree(code);
//    }
}
