package Group_S.Sheet2_1;
import java.util.HashMap;


public class Instance {
	
	public HashMap<Attribute,String> vals;
	
	 public Instance() {
		this.vals = new HashMap<Attribute, String>();
		}

	public void addValue( Attribute a, String s){
		vals.put(a, s);
	}
	
	 public boolean hasAttribute(Attribute a) {
		return vals.containsKey(a);
	}
	
	 public String getValue(Attribute att) {
		return vals.get(att);
	 }

}

