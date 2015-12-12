package player;

public class Fraction implements Comparable {
	/**
	 * Rep invariant: Fractions are always in lowest terms
	 */
	public int num;
	public int denom;

	/** 
	 * Do not allow numerators of < 0, or denominators of <= 0
	 * 
	 * @param n, numerator
	 * @param d, denominator
	 */
	public Fraction(int n, int d) {
		if (d <= 0 || n < 0) {
			throw new RuntimeException("Invalid fraction.");
		}
		
		this.num = n;
		this.denom = d;
	}
	
	/**
	 * Constructor for Fraction given a string, parses string to extract 
	 * numerator and denominator, then reduces the fraction
	 * 
	 * @param frac
	 */
	public Fraction(String frac) {
		if (frac.trim().length() == 0)
			throw new RuntimeException("Invalid input to Fraction constructor (got whitespace)");
		String [] fracArr = frac.split("/");
		if (fracArr.length == 0) { 	// frac is "/" only
			this.num = 1;
			this.denom = 2;
		} else if (fracArr.length == 1) { //fraction is just a numerator
			this.num = Integer.parseInt(fracArr[0]);
			this.denom = 1;
		} else if (fracArr.length == 2) { //slash with denominator
			if (fracArr[0].length() == 0) //numerator is implicit 1 (e.g. "/4")
				this.num = 1;
			else
				this.num = Integer.parseInt(fracArr[0]);
			this.denom = Integer.parseInt(fracArr[1]);
		}
		this.reduce();
	}

	/**
	 * Returns a new Fraction that is the product of the
	 * caller and the param that
	 * 
	 * @param that
	 * @return new Fraction product
	 */
	public Fraction multiply(Fraction that) {
		Fraction f = new Fraction(1, 1);
		f.num = this.num * that.num;
		f.denom = this.denom * that.denom;
		f.reduce();
		return f;
	}
	
	public int getNum() {
		return num;
	}
	
	public int getDenom() {
		return denom;
	}

	/**
	 * Reduces this Fraction to its simplest form.
	 */
	public void reduce() {
		int gcd = Evaluator.gcd(num, denom);
		this.num /= gcd;
		this.denom /= gcd;
	}
	
	/**
	 * Returns numerator if this is a whole number, -1 otherwise
	 */
	public int toInt() {
		this.reduce();
		if (this.denom == 1)
			return this.num;
		else return -1;
	}

	/**
	 * Returns true if these two Fractions have the same reduced
	 * numerator and denominator. 
	 * 
	 * @param obj
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Fraction))
			return false;
		
		Fraction that = (Fraction) obj;
		that.reduce();
		this.reduce();
		
		return this.num == that.num && 
			that.denom == this.denom;
	}

	/**
	 * Returns 1 if caller is greater, 0 if same, and -1 else. This is used for comparing 
	 * element lengths when parsing and turning into sequence units
	 * 
	 * @param other Object
	 */
	@Override
	public int compareTo(Object other) {
		// for floating point math errors
		double TOLERANCE = 0.0001;
			
		// compute their decimal equivilents
		Fraction that = (Fraction) other;
		double thisDecimal = (double) this.num / (double) this.denom;
		double thatDecimal = (double) that.num / (double) that.denom;
		
		if (thisDecimal > thatDecimal) 
			return 1;
		else if (Math.abs(thisDecimal - thatDecimal) < TOLERANCE) 
			return 0;
		else return -1;
	}

	/**
	 * String display method
	 * 
	 * @return String representation of the Fraction
	 */
	@Override
	public String toString() {
		return ("" + num + "/" + denom);
	}
}
