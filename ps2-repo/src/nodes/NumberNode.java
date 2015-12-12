package nodes;

import differentiator.Token;
import differentiator.Visitor;

public class NumberNode implements Node {
	private Token mToken;
	private NodeType mNodeType = NodeType.NUM;
	

	@Override
	public Node getLeftChild() {
		return null;
	}
	
	@Override
	public void setLeftChild(Node node) {
	}

	@Override
	public Node getRightChild() {
		return null;
	}
	
	@Override
	public void setRightChild(Node node) {
	}

	@Override
	public Token getToken() {
		return mToken;
	}
	
	@Override
	public void setToken(Token token) {
		mToken = token;
	}

	@Override
	public NodeType getNodeType() {
		return mNodeType;
	}
	
	@Override
	public String accept(Visitor visitor, String var) {
		return visitor.visit(this, var);
	}

	@Override
	public String convertTreeToString() {
		return mToken.getVal();
	}
}
