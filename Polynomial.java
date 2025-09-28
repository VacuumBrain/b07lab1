import java.nio.file.Files;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Polynomial {
	double[] coefficients;
	int[] exponents;

	public Polynomial() {
		this.coefficients = new double[] {0};
		this.exponents = new int[] {0};
	}

	public Polynomial(double[] coefficientArray, int[] exponentArray) {
		this.coefficients = coefficientArray;
		this.exponents = exponentArray;
	}

	public Polynomial(File file) {
		double[] coefficientArray = new double[100];
		int[] exponentArray = new int[100];

		String content = "";
		try {
			content = Files.readString(file.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (content.length() == 0) {
			this.coefficients = new double[] {0};
			this.exponents = new int[] {0};
		} else {
			int[] boundaries = new int[content.length()+1];
			boundaries[0] = 0;
			int index = 1;

			for (int i = 1; i < content.length(); i++) {
				if (content.charAt(i) == '+' || content.charAt(i) == '-') {
					boundaries[index] = i;
					index++;
				}
			}

			boundaries[index] = content.length();

			if (boundaries.length == 1) {
				String[] parts = content.split("x", -1);
				if (parts.length == 2) {
					if (parts[0].equals("")) {
						coefficientArray[0] = 1;
					} else {
						coefficientArray[0] = Double.parseDouble(parts[0]);
					}
					if (parts[1].equals("")) {
						exponentArray[0] = 1;
					} else {
						exponentArray[0] = Integer.parseInt(parts[1]);
					}
				} else {
					coefficientArray[0] = Double.parseDouble(parts[0]);
					exponentArray[0] = 0;
				}
			} else {
				int loopIndex = 0;
				while(boundaries[loopIndex+1] != 0) {
					String currentTerm = "";
					if (boundaries[loopIndex+1] == 0) {
						currentTerm = content.substring(boundaries[loopIndex]);
					} else {
						currentTerm = content.substring(boundaries[loopIndex], boundaries[loopIndex+1]);
					}
					if (currentTerm.length() > 1 && currentTerm.charAt(1) == 'x') {
						if (currentTerm.charAt(0) == '+' || currentTerm.charAt(0) == '-') {
							if (currentTerm.length() > 2) {
								exponentArray[loopIndex] = Integer.parseInt(currentTerm.substring(2));
							} else {
								exponentArray[loopIndex] = 1;
							}
							coefficientArray[loopIndex] = Double.parseDouble(currentTerm.charAt(0) + "1");
						} else {
							String[] parts = currentTerm.split("x", -1);
							coefficientArray[loopIndex] = Double.parseDouble(parts[0]);
							if (parts[1].equals("")) {
								exponentArray[loopIndex] = 1;
							} else {
								exponentArray[loopIndex] = Integer.parseInt(parts[1]);
							}
						}
					} else {
						String[] parts = currentTerm.split("x", -1);
						if (parts.length == 2) {
							if (parts[0].equals("")) {
								coefficientArray[loopIndex] = 1;
							} else {
								coefficientArray[loopIndex] = Double.parseDouble(parts[0]);
							}
							if (parts[1].equals("")) {
								exponentArray[loopIndex] = 1;
							} else {
								exponentArray[loopIndex] = Integer.parseInt(parts[1]);
							}
						} else {
							coefficientArray[loopIndex] = Double.parseDouble(parts[0]);
							exponentArray[loopIndex] = 0;
						}
					}
					loopIndex++;
				}
			}

			int size = 0;
			for (int i = 0; i < 100; i++) {
				if (coefficientArray[i] == 0) {
					size = i;
					break;
				}
			}

			double[] finalCArray = new double[size];
			int[] finalEArray = new int[size];

			for (int i = 0; i < size; i++) {
				finalCArray[i] = coefficientArray[i];
				finalEArray[i] = exponentArray[i];
			}

			this.coefficients = finalCArray;
			this.exponents = finalEArray;
		}
	}

	public void saveToFile(String filePath) {
		String finalString = "";
		for (int i = 0; i < this.coefficients.length; i++) {
			if (this.coefficients[i] > 0) {
				if (i == 0) {
					if (this.exponents[i] == 0) {
						finalString = finalString + this.coefficients[i];
					} else {
						finalString = finalString + this.coefficients[i] + "x" + this.exponents[i];
					}
				} else {
					if (this.exponents[i] == 0) {
						finalString = finalString + "+" + this.coefficients[i];
					} else {
						finalString = finalString + "+" + this.coefficients[i] + "x" + this.exponents[i];
					}
				}
			}
			else {
				if (this.exponents[i] == 0) {
					finalString = finalString + this.coefficients[i];
				} else {
					finalString = finalString + this.coefficients[i] + "x" + this.exponents[i];
				}
			}
		}
		try {
			FileWriter writer = new FileWriter(filePath);
			writer.write(finalString);
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Polynomial add(Polynomial polynomial) {
		// Step 1.
		// Creates a temporary arrays by "merging" the two polynomials
		int tempSize = this.coefficients.length + polynomial.coefficients.length;
		double[] tempCoefficients = new double[tempSize];
		int[] tempExponents = new int[tempSize];

		for (int i = 0; i < this.exponents.length; i++) {
			tempCoefficients[i] = this.coefficients[i];
			tempExponents[i] = this.exponents[i];
		}

		for (int i = 0; i < polynomial.coefficients.length; i++) {
			tempCoefficients[this.coefficients.length + i] = polynomial.coefficients[i];
			tempExponents[this.exponents.length + i] = polynomial.exponents[i];
		}

		// Step 2.
		// Collects "like-terms" and sets them to "null" values
		for (int i = 0; i < this.exponents.length; i++) {
			for (int j = this.coefficients.length; j < tempSize; j++) {
				if (tempExponents[i] == tempExponents[j]) {
					tempCoefficients[i] += tempCoefficients[j];
					tempCoefficients[j] = -1;
					tempExponents[j] = -1;
				}
			}
		}

		// Step 3.
		// Counts the number of zeroes and "null" terms
		// Used to determine the correct size of the final arrays
		int numZeroes = 0;
		for (int i = 0; i < tempSize; i++) {
			if (tempCoefficients[i] == 0 || tempCoefficients[i] == -1) {
				numZeroes++;
			}
		}

		// Step 4.
		// Creates the final arrays by selecting "proper" terms from the temporary arrays
		double[] newCoefficients = new double[tempSize-numZeroes];
		int[] newExponents = new int[tempSize-numZeroes];
		int newIndex = 0;
		for (int i = 0; i < tempSize; i++) {
			if (tempCoefficients[i] != 0 && tempCoefficients[i] != -1) {
				newCoefficients[newIndex] = tempCoefficients[i];
				newExponents[newIndex] = tempExponents[i];
				newIndex++;
			}
		}
		
		// Step 5.
		// Creates and returns the new added polynomial
		Polynomial newPolynomial = new Polynomial(newCoefficients, newExponents);
		return newPolynomial;
	}

	public double evaluate(double x) {
		double sum = 0;
		for (int i = 0; i < this.coefficients.length; i++) {
			sum += Math.pow(x, this.exponents[i]) * this.coefficients[i];
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

	public Polynomial multiply(Polynomial polynomial) {
		int tempSize = this.coefficients.length * polynomial.coefficients.length;
		double[] tempCoefficients = new double[tempSize];
		int[] tempExponents = new int[tempSize];

		int tempIndex = 0;
		for (int i = 0; i < this.coefficients.length; i++) {
			for (int j = 0; j < polynomial.coefficients.length; j++) {
				tempCoefficients[tempIndex] = this.coefficients[i] * polynomial.coefficients[j];
				tempExponents[tempIndex] = this.exponents[i] + polynomial.exponents[j];
				tempIndex++;
			}
		}

		// Step 2.
		// Collects "like-terms" and sets them to "null" values
		for (int i = 0; i < tempSize; i++) {
			if (tempCoefficients[i] != -1) {
				for (int j = i+1; j < tempSize; j++) {
					if (tempExponents[i] == tempExponents[j]) {
						tempCoefficients[i] += tempCoefficients[j];
						tempCoefficients[j] = -1;
						tempExponents[j] = -1;
					}
				}
			}
		}

		// Step 3.
		// Counts the number of zeroes and "null" terms
		// Used to determine the correct size of the final arrays
		int numZeroes = 0;
		for (int i = 0; i < tempSize; i++) {
			if (tempCoefficients[i] == 0 || tempCoefficients[i] == -1) {
				numZeroes++;
			}
		}

		// Step 4.
		// Creates the final arrays by selecting "proper" terms from the temporary arrays
		double[] newCoefficients = new double[tempSize-numZeroes];
		int[] newExponents = new int[tempSize-numZeroes];
		int newIndex = 0;
		for (int i = 0; i < tempSize; i++) {
			if (tempCoefficients[i] != 0 && tempCoefficients[i] != -1) {
				newCoefficients[newIndex] = tempCoefficients[i];
				newExponents[newIndex] = tempExponents[i];
				newIndex++;
			}
		}
		
		// Step 5.
		// Creates and returns the new added polynomial
		Polynomial newPolynomial = new Polynomial(newCoefficients, newExponents);
		return newPolynomial;
	}

}