import java.io.File;
import java.io.FileNotFoundException;

public class Driver {
	public static void main(String [] args) {
		
		/*
		Polynomial p = new Polynomial();
		System.out.println(p.evaluate(3));
		*/

		double[] c1 = {-6, 9, -13};
		int[] e1 = {2, 0 ,5};
		Polynomial p1 = new Polynomial(c1, e1);
		System.out.println(p1.evaluate(3));

		double[] c2 = {5, 10};
		int[] e2 = {2, 3};
		Polynomial p2 = new Polynomial(c2, e2);
		System.out.println(p2.evaluate(2));

		Polynomial s = p1.add(p2);
		System.out.println(s.evaluate(2));

		Polynomial m = p1.multiply(p2);
		System.out.println(m.evaluate(1));

		String filePath = "C:\\Users\\chris\\OneDrive\\Documents\\test.txt";
		//File file = new File(filePath);
		//Polynomial f = new Polynomial(file);
		//System.out.println(f.evaluate(2));

		p1.saveToFile(filePath);
		/*try {
			File file = new File(filePath);
			Polynomial f = new Polynomial(file);
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
            e.printStackTrace();
		} */

		/*
		double [] c1 = {6,0,0,5};
		Polynomial p1 = new Polynomial(c1);
		double [] c2 = {0,-2,0,0,-9};
		Polynomial p2 = new Polynomial(c2);
		Polynomial s = p1.add(p2);
		System.out.println("s(0.1) = " + s.evaluate(0.1));
		if(s.hasRoot(1))
			System.out.println("1 is a root of s");
		else
			System.out.println("1 is not a root of s");
		*/
	}
}