package Sheet_5;

import java.util.HashMap;

public class Classifier {

	double pVa; // probability that text describes "A"
	double pVb;
	double pVe;
	double pVv;

	
	HashMap<String, Double> pBa; // conditional probability (Wk|"A")
	HashMap<String, Double> pBb;
	HashMap<String, Double> pBe;
	HashMap<String, Double> pBv;
	
	
	
	public Classifier(	double pVa, 
									double pVb,
									double pVe,
									double pVv,
								
									HashMap<String, Double> pBa,
									HashMap<String, Double> pBb,
									HashMap<String, Double> pBe,
									HashMap<String, Double> pBv){
		this.pVa=pVa;
		this.pVb=pVb;
		this.pVe=pVe;
		this.pVv=pVv;
		
		this.pBa=pBa;
		this.pBb=pBb;
		this.pBe=pBe;
		this.pBv=pBv;
		
	}
	
	public Classifier(){
		this.pVa=0;
		this.pVb=0;
		this.pVe=0;
		this.pVv=0;
		
		this.pBa=null;
		this.pBb=null;
		this.pBe=null;
		this.pBv=null;
	}
	
}
	

