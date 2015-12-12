package sound;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import static org.junit.Assert.*;
import org.junit.Test;

import org.junit.Test;

public class SequencePlayerTest {
	@Test
	public void piece1Test() throws MidiUnavailableException, InvalidMidiDataException {
	    SequencePlayer player = new SequencePlayer(140, 12);
        
        player.addNote(new Pitch('C').toMidiNote(), 0, 12);
        player.addNote(new Pitch('C').toMidiNote(), 12, 12);
        
        player.addNote(new Pitch('C').toMidiNote(), 24, 9);
        player.addNote(new Pitch('D').toMidiNote(), 33, 3);
        player.addNote(new Pitch('E').toMidiNote(), 36, 12);
        // Bar
        player.addNote(new Pitch('E').toMidiNote(), 48, 9);
        player.addNote(new Pitch('D').toMidiNote(), 57, 3);
        player.addNote(new Pitch('E').toMidiNote(), 60, 9);
        player.addNote(new Pitch('F').toMidiNote(), 69, 3);
        player.addNote(new Pitch('G').toMidiNote(), 72, 24);
        // Bar
        player.addNote(new Pitch('C').transpose(Pitch.OCTAVE).toMidiNote(), 96, 4);
        player.addNote(new Pitch('C').transpose(Pitch.OCTAVE).toMidiNote(), 100, 4);
        player.addNote(new Pitch('C').transpose(Pitch.OCTAVE).toMidiNote(), 104, 4);
        
        player.addNote(new Pitch('G').toMidiNote(), 108, 4);
        player.addNote(new Pitch('G').toMidiNote(), 112, 4);
        player.addNote(new Pitch('G').toMidiNote(), 116, 4);
        
        player.addNote(new Pitch('E').toMidiNote(), 120, 4);
        player.addNote(new Pitch('E').toMidiNote(), 124, 4);
        player.addNote(new Pitch('E').toMidiNote(), 128, 4);
        
        player.addNote(new Pitch('C').toMidiNote(), 132, 4);
        player.addNote(new Pitch('C').toMidiNote(), 136, 4);
        player.addNote(new Pitch('C').toMidiNote(), 140, 4);
        // Bar
        player.addNote(new Pitch('G').toMidiNote(), 144, 9);
        player.addNote(new Pitch('F').toMidiNote(), 153, 3);
        player.addNote(new Pitch('E').toMidiNote(), 156, 9);
        player.addNote(new Pitch('D').toMidiNote(), 165, 3);
        player.addNote(new Pitch('C').toMidiNote(), 168, 24); 
        // Bar
        
        //player.play();
        
        String [] output = player.toString().split("\n");
        String[] correct = {
                "Event: NOTE_ON  Pitch: 60  Tick: 0",
                "Event: NOTE_OFF Pitch: 60  Tick: 12",
                "Event: NOTE_ON  Pitch: 60  Tick: 12",
                "Event: NOTE_OFF Pitch: 60  Tick: 24",
                "Event: NOTE_ON  Pitch: 60  Tick: 24",
                "Event: NOTE_OFF Pitch: 60  Tick: 33",
                "Event: NOTE_ON  Pitch: 62  Tick: 33",
                "Event: NOTE_OFF Pitch: 62  Tick: 36",
                "Event: NOTE_ON  Pitch: 64  Tick: 36",
                "Event: NOTE_OFF Pitch: 64  Tick: 48",
                "Event: NOTE_ON  Pitch: 64  Tick: 48",
                "Event: NOTE_OFF Pitch: 64  Tick: 57",
                "Event: NOTE_ON  Pitch: 62  Tick: 57",
                "Event: NOTE_OFF Pitch: 62  Tick: 60",
                "Event: NOTE_ON  Pitch: 64  Tick: 60",
                "Event: NOTE_OFF Pitch: 64  Tick: 69",
                "Event: NOTE_ON  Pitch: 65  Tick: 69",
                "Event: NOTE_OFF Pitch: 65  Tick: 72",
                "Event: NOTE_ON  Pitch: 67  Tick: 72",
                "Event: NOTE_OFF Pitch: 67  Tick: 96",
                "Event: NOTE_ON  Pitch: 72  Tick: 96",
                "Event: NOTE_OFF Pitch: 72  Tick: 100",
                "Event: NOTE_ON  Pitch: 72  Tick: 100",
                "Event: NOTE_OFF Pitch: 72  Tick: 104",
                "Event: NOTE_ON  Pitch: 72  Tick: 104",
                "Event: NOTE_OFF Pitch: 72  Tick: 108",
                "Event: NOTE_ON  Pitch: 67  Tick: 108",
                "Event: NOTE_OFF Pitch: 67  Tick: 112",
                "Event: NOTE_ON  Pitch: 67  Tick: 112",
                "Event: NOTE_OFF Pitch: 67  Tick: 116",
                "Event: NOTE_ON  Pitch: 67  Tick: 116",
                "Event: NOTE_OFF Pitch: 67  Tick: 120",
                "Event: NOTE_ON  Pitch: 64  Tick: 120",
                "Event: NOTE_OFF Pitch: 64  Tick: 124",
                "Event: NOTE_ON  Pitch: 64  Tick: 124",
                "Event: NOTE_OFF Pitch: 64  Tick: 128",
                "Event: NOTE_ON  Pitch: 64  Tick: 128",
                "Event: NOTE_OFF Pitch: 64  Tick: 132",
                "Event: NOTE_ON  Pitch: 60  Tick: 132",
                "Event: NOTE_OFF Pitch: 60  Tick: 136",
                "Event: NOTE_ON  Pitch: 60  Tick: 136",
                "Event: NOTE_OFF Pitch: 60  Tick: 140",
                "Event: NOTE_ON  Pitch: 60  Tick: 140",
                "Event: NOTE_OFF Pitch: 60  Tick: 144",
                "Event: NOTE_ON  Pitch: 67  Tick: 144",
                "Event: NOTE_OFF Pitch: 67  Tick: 153",
                "Event: NOTE_ON  Pitch: 65  Tick: 153",
                "Event: NOTE_OFF Pitch: 65  Tick: 156",
                "Event: NOTE_ON  Pitch: 64  Tick: 156",
                "Event: NOTE_OFF Pitch: 64  Tick: 165",
                "Event: NOTE_ON  Pitch: 62  Tick: 165",
                "Event: NOTE_OFF Pitch: 62  Tick: 168",
                "Event: NOTE_ON  Pitch: 60  Tick: 168",
                "Event: NOTE_OFF Pitch: 60  Tick: 192",
                "***** End of track *****   Tick: 192"
        };
        
        assertArrayEquals(output, correct);
        
	}
	
	@Test
	public void piece2Test() throws MidiUnavailableException, InvalidMidiDataException {
		// 4 ticks per quarter note
		SequencePlayer player = new SequencePlayer(200, 48);
		
		// pitch, starttick, numticks
		player.addNote(new Pitch('E').transpose(12).toMidiNote(), 0, 24);
		player.addNote(new Pitch('F').transpose(1).toMidiNote(), 0, 24);
		
		player.addNote(new Pitch('E').transpose(12).toMidiNote(), 24, 24);
		player.addNote(new Pitch('F').transpose(1).toMidiNote(), 24, 24);
		
		// eighth rest
		
		player.addNote(new Pitch('E').transpose(12).toMidiNote(), 72, 24);
		player.addNote(new Pitch('F').transpose(1).toMidiNote(), 72, 24);
		
		// eighth rest
		
		player.addNote(new Pitch('A').toMidiNote(), 120, 24);
		player.addNote(new Pitch('F').transpose(1).toMidiNote(), 120, 24);
		
		player.addNote(new Pitch('E').transpose(12).toMidiNote(), 144, 48);
		player.addNote(new Pitch('F').transpose(1).toMidiNote(), 144, 48);
		
		// bar
		
		player.addNote(new Pitch('G').toMidiNote(), 196, 48);
		player.addNote(new Pitch('B').toMidiNote(), 196, 48);
		player.addNote(new Pitch('G').transpose(12).toMidiNote(), 196, 48);
		
		// quarter rest
		
		player.addNote(new Pitch('G').toMidiNote(), 292, 48);
		
		// quarter rest
		
		player.addNote(new Pitch('C').transpose(12).toMidiNote(), 384, 72);
		player.addNote(new Pitch('G').toMidiNote(), 456, 24);
		
		// quarter rest
		
		player.addNote(new Pitch('E').toMidiNote(), 528, 48);
		
		// bar
		
		player.addNote(new Pitch('E').toMidiNote(), 576, 24);
		player.addNote(new Pitch('A').toMidiNote(), 600, 48);
		player.addNote(new Pitch('B').toMidiNote(), 648, 48);
		player.addNote(new Pitch('B').transpose(-1).toMidiNote(), 696, 24);
		player.addNote(new Pitch('A').toMidiNote(), 720, 48);
		
		// bar
		
		player.addNote(new Pitch('G').toMidiNote(), 768, 32);
		player.addNote(new Pitch('E').transpose(12).toMidiNote(), 800, 32);
		player.addNote(new Pitch('G').transpose(12).toMidiNote(), 832, 32);
		player.addNote(new Pitch('A').transpose(12).toMidiNote(), 864, 24);
		player.addNote(new Pitch('F').transpose(12).toMidiNote(), 888, 48);
		player.addNote(new Pitch('G').transpose(12).toMidiNote(), 936, 48);
		
		// bar
		
		player.addNote(new Pitch('E').transpose(12).toMidiNote(), 984, 48);
		player.addNote(new Pitch('C').transpose(12).toMidiNote(), 1032, 24);
		player.addNote(new Pitch('D').transpose(12).toMidiNote(), 1056, 24);
		player.addNote(new Pitch('B').toMidiNote(), 1080, 36);

		//player.play();
		
		String [] OUTPUT = player.toString().split("\n");
		String [] CORRECT = {
				"Event: NOTE_ON  Pitch: 76  Tick: 0",		
			"Event: NOTE_ON  Pitch: 66  Tick: 0",
			"Event: NOTE_OFF Pitch: 76  Tick: 24",
			"Event: NOTE_OFF Pitch: 66  Tick: 24",
			"Event: NOTE_ON  Pitch: 76  Tick: 24",
			"Event: NOTE_ON  Pitch: 66  Tick: 24",
			"Event: NOTE_OFF Pitch: 76  Tick: 48",
			"Event: NOTE_OFF Pitch: 66  Tick: 48",
			"Event: NOTE_ON  Pitch: 76  Tick: 72",
			"Event: NOTE_ON  Pitch: 66  Tick: 72",
			"Event: NOTE_OFF Pitch: 76  Tick: 96",
			"Event: NOTE_OFF Pitch: 66  Tick: 96",
			"Event: NOTE_ON  Pitch: 69  Tick: 120",
			"Event: NOTE_ON  Pitch: 66  Tick: 120",
			"Event: NOTE_OFF Pitch: 69  Tick: 144",
			"Event: NOTE_OFF Pitch: 66  Tick: 144",
			"Event: NOTE_ON  Pitch: 76  Tick: 144",
			"Event: NOTE_ON  Pitch: 66  Tick: 144",
			"Event: NOTE_OFF Pitch: 76  Tick: 192",
			"Event: NOTE_OFF Pitch: 66  Tick: 192",
			"Event: NOTE_ON  Pitch: 67  Tick: 196",
			"Event: NOTE_ON  Pitch: 71  Tick: 196",
			"Event: NOTE_ON  Pitch: 79  Tick: 196",
			"Event: NOTE_OFF Pitch: 67  Tick: 244",
			"Event: NOTE_OFF Pitch: 71  Tick: 244",
			"Event: NOTE_OFF Pitch: 79  Tick: 244",
			"Event: NOTE_ON  Pitch: 67  Tick: 292",
			"Event: NOTE_OFF Pitch: 67  Tick: 340",
			"Event: NOTE_ON  Pitch: 72  Tick: 384",
			"Event: NOTE_OFF Pitch: 72  Tick: 456",
			"Event: NOTE_ON  Pitch: 67  Tick: 456",
			"Event: NOTE_OFF Pitch: 67  Tick: 480",
			"Event: NOTE_ON  Pitch: 64  Tick: 528",
			"Event: NOTE_OFF Pitch: 64  Tick: 576",
			"Event: NOTE_ON  Pitch: 64  Tick: 576",
			"Event: NOTE_OFF Pitch: 64  Tick: 600",
			"Event: NOTE_ON  Pitch: 69  Tick: 600",
			"Event: NOTE_OFF Pitch: 69  Tick: 648",
			"Event: NOTE_ON  Pitch: 71  Tick: 648",
			"Event: NOTE_OFF Pitch: 71  Tick: 696",
			"Event: NOTE_ON  Pitch: 70  Tick: 696",
			"Event: NOTE_OFF Pitch: 70  Tick: 720",
			"Event: NOTE_ON  Pitch: 69  Tick: 720",
			"Event: NOTE_OFF Pitch: 69  Tick: 768",
			"Event: NOTE_ON  Pitch: 67  Tick: 768",
			"Event: NOTE_OFF Pitch: 67  Tick: 800",
			"Event: NOTE_ON  Pitch: 76  Tick: 800",
			"Event: NOTE_OFF Pitch: 76  Tick: 832",
			"Event: NOTE_ON  Pitch: 79  Tick: 832",
			"Event: NOTE_OFF Pitch: 79  Tick: 864",
			"Event: NOTE_ON  Pitch: 81  Tick: 864",
			"Event: NOTE_OFF Pitch: 81  Tick: 888",
			"Event: NOTE_ON  Pitch: 77  Tick: 888",
			"Event: NOTE_OFF Pitch: 77  Tick: 936",
			"Event: NOTE_ON  Pitch: 79  Tick: 936",
			"Event: NOTE_OFF Pitch: 79  Tick: 984",
			"Event: NOTE_ON  Pitch: 76  Tick: 984",
			"Event: NOTE_OFF Pitch: 76  Tick: 1032",
			"Event: NOTE_ON  Pitch: 72  Tick: 1032",
			"Event: NOTE_OFF Pitch: 72  Tick: 1056",
			"Event: NOTE_ON  Pitch: 74  Tick: 1056",
			"Event: NOTE_OFF Pitch: 74  Tick: 1080",
			"Event: NOTE_ON  Pitch: 71  Tick: 1080",
			"Event: NOTE_OFF Pitch: 71  Tick: 1116",
			"***** End of track *****   Tick: 1116"
		};
		
		assertArrayEquals(OUTPUT, CORRECT);
	}
}
