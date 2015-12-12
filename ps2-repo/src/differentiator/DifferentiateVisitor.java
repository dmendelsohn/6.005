package differentiator;

import nodes.*;

public class DifferentiateVisitor implements Visitor {
	
	public String visit(VarNode node, String var) {
			if (node.getToken().getVal().equals(var))
				return "1";
			else
				return "0";
	}
	
	public String visit(NumberNode node, String var) {
		return "0";
	}
	
	public String visit(AddNode node, String var) {
		String s1 = Differentiator.evaluateNode(node.getLeftChild(), var);
		String s2 = Differentiator.evaluateNode(node.getRightChild(), var);
		return Utils.smartAdd(s1,s2);
	}
	
	public String visit(MultNode node, String var) {
		String s1 = Differentiator.evaluateNode(node.getLeftChild(), var);
		String s2 = node.getLeftChild().convertTreeToString();
		String s3 = Differentiator.evaluateNode(node.getRightChild(), var);
		String s4 = node.getRightChild().convertTreeToString();
		String leftSide = Utils.smartMul(s1,s4);
		String rightSide = Utils.smartMul(s2, s3);
		return Utils.smartAdd(leftSide, rightSide);
	}
	
}
