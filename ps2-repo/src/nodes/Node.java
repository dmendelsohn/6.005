package nodes;

import differentiator.Token;
import differentiator.Visitor;

public interface Node {
	public Node getLeftChild();
	public void setLeftChild(Node node);
	public Node getRightChild();
	public void setRightChild(Node node);
	public Token getToken();
	public void setToken(Token token);
	public NodeType getNodeType();
	
	public String convertTreeToString();
	public String accept(Visitor visitor, String var);
	
	public static enum NodeType {
		NUM,
		VAR,
		ADD,
		MULT;
	}
}
