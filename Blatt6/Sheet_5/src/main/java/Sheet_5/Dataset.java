package Sheet_5;

import java.util.HashMap;
import java.util.HashSet;

public class Dataset {

	int n;
	public HashSet<String> vocabulary= new HashSet<String>();
	public HashMap<String, Integer> a = new HashMap<String, Integer>();
	public HashMap<String, Integer> b = new HashMap<String, Integer>();
	public HashMap<String, Integer> e = new HashMap<String, Integer>();
	public HashMap<String, Integer> v = new HashMap<String, Integer>();
	int countA=0;
	int countB=0;
	int countE=0;
	int countV=0;
	
	
	
	public Dataset(	int n,
						HashSet<String> vocabulary,
						HashMap<String, Integer> a ,
						HashMap<String, Integer> b,
						HashMap<String, Integer> e ,
						HashMap<String, Integer> v,
						int countA,
						int countB,
						int countE,
						int countV
				){
		
		this.a=a;
		this.b=b;
		this.e=e;
		this.v=v;
		this.vocabulary=vocabulary;
		this.n=n;
		this.countA=countA;
		this.countB=countB;
		this.countE=countE;
		this.countV=countV;
			
	}
	
	public Dataset (Dataset d){
		new Dataset(d.n,d.vocabulary,d.a,d.b,d.e,d.v,d.countA,d.countB,d.countE,d.countV);
	}
	
	
	public HashMap<String, Integer> getA(){
		return a;
	}
	
	public void clean (){
		
	}
}

