package random;

public class TriangleProb {
	public boolean isEqui(int a, int b, int c) {
		if ((a == b) && (b == c)) {
			return true;
		} else
			return false;
	}

	public boolean isIso(int a, int b, int c) {
		if ((a == b) || (b == c) || (a == c)) {
			return true;
		} else
			return false;
	}

	public boolean isScal(int a, int b, int c) {
		if ((a != b) && (b != c) && (a != c)) {
			return true;
		} else
			return false;
	}

	public boolean isRight(int a, int b, int c) {
		if (((a * a) == ((b * b) + (c * c)))
				|| ((b * b) == ((a * a) + (c * c)))
				|| ((c * c) == ((a * a) + (b * b))))
			return true;
		else
			return false;
	}

	
}
