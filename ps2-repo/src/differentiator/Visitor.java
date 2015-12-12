package differentiator;

import nodes.*;

public interface Visitor {
	public String visit(VarNode node, String var);
	public String visit(NumberNode node, String var);
	public String visit(AddNode node, String var);
	public String visit(MultNode node, String var);
}
