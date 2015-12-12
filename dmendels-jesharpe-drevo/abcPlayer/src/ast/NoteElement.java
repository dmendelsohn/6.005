package ast;

import java.util.ArrayList;
import java.util.List;

import player.Fraction;
import player.Token;

public class NoteElement implements SyncElement {

	//values for optional fields are empty strings by default
	private String basenote;
	private String accidental = "";
	private String octave = "";
	private Fraction duration = new Fraction("1/1"); //default value
	
	public NoteElement(List<Token> tokens) {
		for (Token t : tokens) {
			switch (t.getType()) {
				case BASENOTE:
					basenote = t.getValue();
					break;
				case ACCIDENTAL:
					accidental = t.getValue();
					break;
				case OCTAVE:
					octave = t.getValue();
					break;
				case DURATION:
					String multiplier = "1";
					multiplier = t.getValue();
					duration = new Fraction(multiplier);
					break;
				default:
					throw new RuntimeException("Note constructor found unacceptable token");
			}
		}
	}
	
	public String getBasenote() {
		return basenote;
	}
	
	public String getAccidental() {
		return accidental;
	}
	
	public String getOctave() {
		return octave;
	}
	
	@Override
	public Fraction getDuration() {
		return duration;
	}

	@Override
	public List<NoteElement> expand() {
		List<NoteElement> l =  new ArrayList<NoteElement>();
		l.add(this);
		return l;
	}

	@Override
	public int getLcmOfDenoms() {
		return getDuration().getDenom();
	}

	@Override
	public void scaleDuration(Fraction scale) {
		duration = duration.multiply(scale);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof NoteElement))
			return false;
		NoteElement that = (NoteElement)obj;
		if (!this.getBasenote().equals(that.getBasenote()) ||
				!this.getAccidental().equals(that.getAccidental()) ||
				!this.getOctave().equals(that.getOctave()) ||
				!this.getDuration().equals(that.getDuration()))
			return false;
		return true;
	}
	
}
