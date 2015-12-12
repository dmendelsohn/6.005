package ast;

import java.util.ArrayList;
import java.util.List;


public class Song {
	/**
	 * Song fields:
	 * - header: holds meta song information
	 * - voices: list of different voices, which hold Measures, which hold notes and rests, etc
	 */
	private final Header header;
	private final List<Voice> voices;
	
	/**
	 * Song constructor
	 * 
	 * @param head
	 * @param parts
	 */
	public Song(Header head, List<Voice> parts){
		this.header = head;
		this.voices = parts;
	}
	
	public Header getHeader() {
		return header;
	}
	
	public List<Voice> getVoices() {
		return new ArrayList<Voice>(voices);
	}

	/**
	 * Recurses down into the voices level to 
	 * determine the equality of two songs
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Song))
			return false;
		Song that = (Song)obj;
		if (!this.header.equals(that.header))
			return false;
		if (this.voices.size() != that.voices.size())
			return false;
		for (int i = 0; i < this.voices.size(); i++) {
			if (!this.voices.get(i).equals(that.voices.get(i)))
				return false;
		}
		return true;
	}
}
