package player;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import ast.Song;

/**
 * Main entry point of your application.
 */
public class Main {
	public static void main(String [] args) throws SyntaxErrorException, MidiUnavailableException, InvalidMidiDataException {
		if (args.length != 1) {
			System.err.println("Must have only one String argument with name of ABC file!");
	        System.exit(1);
		} else {
			String filename = "sample_abc/" + args[0];
			System.out.println("Playing: " + filename);
			Main.play(filename);
		} 
	}
	/**
	 * Plays the input file using Java MIDI API and displays
	 * header information to the standard output stream.
	 * 
	 * <p>Your code <b>should not</b> exit the application abnormally using
	 * System.exit()</p>
	 * 
	 * @param file the name of input abc file
	 * @throws SyntaxErrorException 
	 * @throws MidiUnavailableException 
	 * @throws InvalidMidiDataException 
	 */
	public static void play(String file) throws SyntaxErrorException, MidiUnavailableException, InvalidMidiDataException {
		Lexer l = new Lexer(file);
		Parser p = new Parser(l);
		Evaluator e = new Evaluator(p.getSong());
	}
	
	//NOTE: On the fur_elise.abc input, we got a problem where the music would temporarily pause.
	//The location at which it did this was non-deterministic.  Since we did not use threads at all,
	//we suspect the problem might lie within the code for SequencePlayer or the method in which we
	//used that class
}
