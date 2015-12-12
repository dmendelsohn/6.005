package nodes;

import differentiator.Token;
import differentiator.Visitor;

public class AddNode implements Node {
	private Node mRightChild;
	private Node mLeftChild;
	private Token mToken = new Token(Token.Type.ADD, "+");
	private NodeType mNodeType = NodeType.ADD;
	

	@Override
	public Node getLeftChild() {
		return mLeftChild;
	}
	
	@Override
	public void setLeftChild(Node node) {
		mLeftChild = node;
	}

	@Override
	public Node getRightChild() {
		return mRightChild;
	}
	
	@Override
	public void setRightChild(Node node) {
		mRightChild = node;
	}

	@Override
	public Token getToken() {
		return mToken;
	}
	
	@Override
	public void setToken(Token token) {
		
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
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(getLeftChild().convertTreeToString());
		sb.append("+");
		sb.append(getRightChild().convertTreeToString());
		sb.append(")");
		return sb.toString();
	}
}
