package player;

import ast.HighElement;

public class TokenElementPair {
	public HighElement h;
	public Token t;
	
	public TokenElementPair(Token t, HighElement h) {
		this.t = t;
		this.h = h;
	}
}
