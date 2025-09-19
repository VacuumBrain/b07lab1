public class Polynomial {
	double[] coefficients;

	public Polynomial() {
		this.coefficients = new double[] {0};
	}

	public Polynomial(double[] array) {
		this.coefficients = array;
	}

	public Polynomial add(Polynomial polynomial) {
		int longerLength = Math.max(polynomial.coefficients.length, this.coefficients.length);
		double[] newCoefficients = new double[longerLength];
		if (polynomial.coefficients.length == longerLength) {
			newCoefficients = polynomial.coefficients;
			for (int i = 0; i < this.coefficients.length; i++) {
				newCoefficients[i] += this.coefficients[i];
			}
		}
		else {
			newCoefficients = this.coefficients;
			for (int i = 0; i < polynomial.coefficients.length; i++) {
				newCoefficients[i] += polynomial.coefficients[i];
			}
		}
		Polynomial newPolynomial = new Polynomial(newCoefficients);
		return newPolynomial;
	}

	public double evaluate(double x) {
		double sum = 0;
		for (int i = 0; i < this.coefficients.length; i++) {
			sum += Math.pow(x, i) * this.coefficients[i];
		}
		return sum;
	}

	public boolean hasRoot(double x) {
		double sum = evaluate(x);
		if (sum == 0) {
			return true;
		}
		else {
			return false;
		}
	}

}