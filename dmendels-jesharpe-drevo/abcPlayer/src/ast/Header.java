package ast;

import java.util.ArrayList;
import java.util.List;

public class Header {
	/**
	 * Fields that we can possibly grab from the ABC file Header
	 */
    public final String index;         // X: index number; always comes first   
    public final String title;      // T: title; always comes second
    public final String composer;   // C: 
    public final String length;        // L:
    public final String meter;         // M:
    public final String tempo;         // Q:
    private List<String> voiceNames;
    public final String key;   // K: key; Always comes last
    
    /**
     * Constructor for the Header object
     * 
     * @param i, index
     * @param t, title
     * @param c, composer
     * @param l, length
     * @param m, meter
     * @param q, tempo
     * @param v, voice names List
     * @param k, key string
     */
    public Header(String i, String t, String c, String l, String m, String q, List<String> v, String k) {
        index = i;
        title = t;
        composer = c;
        length = l;
        meter = m;
        tempo = q;
        voiceNames = v;
        key = k;
    }
    
    /**
     * String representation of this Header
     * 
     * @return String
     */
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("index: ").append(index).append("\n");
    	sb.append("title: ").append(title).append("\n");
    	sb.append("composer: ").append(composer).append("\n");
    	sb.append("length: ").append(length).append("\n");
    	sb.append("meter: ").append(meter).append("\n");
    	sb.append("tempo: ").append(tempo).append("\n");
    	for (String voice : voiceNames)
    		sb.append("voice: ").append(voice).append("\n");
    	sb.append("key: ").append(key).append("\n");
    	return sb.toString();
    }
    
    public List<String> getVoiceNames() {
    	return new ArrayList<String>(voiceNames);
    }

    /**
     * Tests all fields for equality
     * 
     * Does NOT test equality in cut time ("C|") case. However this functionality has
     * little practical use, since even in testing we never compare the same
     * song with different measures for equality. 
     */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Header))
			return false;
		Header that = (Header)obj;
		if (!this.index.equals(that.index) ||
				!this.title.equals(that.title) ||
				!this.composer.equals(that.composer) ||
				!this.length.equals(that.length) ||
				!this.meter.equals(that.meter) ||
				!this.tempo.equals(that.tempo) ||
				!this.key.equals(that.key))
			return false;
		if (this.voiceNames.size() != that.voiceNames.size())
			return false;
		for (String name : this.voiceNames) {
			if (!that.voiceNames.contains(name))
				return false;
		}
		return true;
	}
    
}
