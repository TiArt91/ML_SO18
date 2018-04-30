package Group_S.Sheet2_1;

import java.util.HashMap;

public class InnerNode implements Node{
	Attribute root;
	public HashMap<String, Node> nodes;
	
	public InnerNode(Attribute root, HashMap<String, Node> nodes){
		this.nodes=nodes;
		this.root=root;
	}

	public String classify(Instance i) {
		
		String value = i.getValue(root);
		Node subtree = nodes.get(value);
		return subtree.classify(i);
	}
	
}
