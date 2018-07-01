package edu.pse.beast.propertychecker;

import edu.pse.beast.datatypes.booleanExpAST.BooleanExpNodeVisitor;
import edu.pse.beast.datatypes.booleanExpAST.BooleanValuedNodes.BooleanExpressionNode;
import edu.pse.beast.toolbox.antlr.booleanexp.FormalPropertyDescriptionParser.IntersectContentContext;
import edu.pse.beast.toolbox.antlr.booleanexp.FormalPropertyDescriptionParser.IntersectExpContext;

public class IntersectContentNode extends BooleanExpressionNode {

	public final IntersectContentContext intersectContentContext;
	public final String voteOutput;

	public IntersectContentNode(IntersectContentContext intersectContentContext, String voteOutput) {
		this.intersectContentContext = intersectContentContext;
		this.voteOutput = voteOutput;
	}

	@Override
	public void getVisited(BooleanExpNodeVisitor visitor) {
		visitor.visitIntersectContentNode(this);
	}

	@Override
	public String getTreeString(int depth) {
		System.out.println("might add treestring");
		return "";
	}
}
