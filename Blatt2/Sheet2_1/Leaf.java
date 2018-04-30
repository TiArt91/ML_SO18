package Group_S.Sheet2_1;

public class Leaf implements Node {
	String value;
	
	public Leaf(String value){
		this.value=value;
	}
	
	public String classify(Instance i){
		return value;
	}
}
