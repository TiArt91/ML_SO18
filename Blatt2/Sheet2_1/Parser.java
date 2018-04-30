package Group_S.Sheet2_1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {

	ArrayList<Attribute> attributes = new ArrayList<Attribute>();
	ArrayList<Instance> instances = new ArrayList<Instance>();
	
	public Parser(){
		
	}
	
	public ArrayList<Attribute> getAttributes(){
		return this.attributes;
	}
	
	public ArrayList<Instance> getInstances(){
		return this.instances;
	}
	
	public void parseArff(File file) throws IOException{
		
		String line = null;
		BufferedReader r = new BufferedReader(new FileReader(file));
		boolean data =  false;
		
		attributes.clear();
		instances.clear();
		
		while ((line = r.readLine()) != null) {
		    line = line.trim();
		    
		    if (!line.startsWith("%")){		    	
			    if (line.startsWith("@attribute") && !data) {
			    	attributes.add(parseAtt(line));
			    	}
			    
			    if (data)
			    	instances.add(parseInst(line));
		   
			    if (line.startsWith("@data")) {
			    	data = true;
			    }
		    }
	
		}
		r.close();
	}


	public static Attribute parseAtt(String line) {
		line = line.trim();
	
		int indexName = line.indexOf(" ")+1;
		int indexArg = line.indexOf(" ", indexName)+1;
		
		String attributeName = line.substring(indexName, indexArg-1);
		String arguments = line.substring(indexArg+1);
		arguments = arguments.replaceAll(" ","");
		arguments = arguments.replace("}","");
		String[] values = arguments.split(","); 
		Attribute attribute = new Attribute(attributeName, values);
		
	
		return attribute;
	}

	public Instance parseInst(String line) {
	
		line = line.trim();
		if (line.length() == 0) {
		    return null;
		}
	
		Instance inst = new Instance();
		String[] values = line.split(",");
		//System.out.println(attributes.get(0).name);
		//System.out.println(values.length);
		if (values.length != attributes.size()) {
		    System.out.println("no match");
			return null;
		}
		
		else {
		    for (int i = 0; i < attributes.size(); i++) {	
		    	inst.addValue(attributes.get(i), values[i]);
		    }
		}
			
		return inst;
	}
	


}
