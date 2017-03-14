package edu.pse.beast.toolbox.antlr.booleanexp.GenerateAST;

import edu.pse.beast.datatypes.booleanExpAST.BooleanExpListNode;
import edu.pse.beast.datatypes.internal.InternalTypeContainer;
import edu.pse.beast.datatypes.internal.InternalTypeRep;
import edu.pse.beast.datatypes.propertydescription.SymbolicVariable;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by holger on 14.03.17.
 */
public class UserAstTestHelper {
    public static void main(String[] args) {

    }

    @Test
    public void testBinaryOtherExpAST() {
        List<String> comparisonSymbols = Arrays.asList("==", "<", "<=", ">", ">=");
        String expTemplate ="COMPARE\n" +
                "\tlhs: SymbVar: {id c, type: CANDIDATE}\n" +
                "\trhs: SymbVar: {id c, type: CANDIDATE}\n";
        String formula = "c COMPARE c;";
        comparisonSymbols.forEach(
                symb -> {
                    BooleanExpListNode ast = FormalPropertySyntaxTreeToAstTranslatorTest.translate(
                            formula.replace("COMPARE", symb),
                            new SymbolicVariable("c", new InternalTypeContainer(InternalTypeRep.CANDIDATE)));
                    String astRep = ast.getTreeString();
                    Assert.assertEquals(expTemplate.replace("COMPARE", symb), astRep);
                }
        );
    }

    @Test
    public void quantorAstTest() {
        String temp = "QUANT(var) : var == var;";
        List<String> quantors = Arrays.asList("FOR_ALL_", "EXISTS_ONE_");
        List<List<String>> types = Arrays.asList(
                Arrays.asList("VOTERS", "CANDIDATES", "SEATS"),
                Arrays.asList("VOTER", "CANDIDATE", "SEAT"));
        String[] expQuant = {"ForAll", "ExistsOne"};
        String exp = "QUANT: Declared var: var\n" +
                "\tfollowing: \t==\n" +
                "\t\tlhs: SymbVar: {id var, type: VARTYPE}\n" +
                "\t\trhs: SymbVar: {id var, type: VARTYPE}\n";
        for (int i = 0; i < quantors.size(); i++) {
            int pos = i;
            int loop = 0;
            for (int j = 0; j < types.get(i).size(); j++) {
                String quantor = quantors.get(pos) + types.get(i).get(j);
                String code = temp.replace("QUANT", quantor);
                BooleanExpListNode ast = FormalPropertySyntaxTreeToAstTranslatorTest.translate(
                        code,
                        new SymbolicVariable("c", new InternalTypeContainer(InternalTypeRep.CANDIDATE)));
                String expStr = exp.replace("QUANT", expQuant[pos]);
                expStr = expStr.replaceAll("VARTYPE", types.get(1).get(j));
                Assert.assertEquals(expStr, ast.getTreeString());
            }
        }

    }

}