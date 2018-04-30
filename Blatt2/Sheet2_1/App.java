package Group_S.Sheet2_1;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class App 
{
	
	
	public static void main( String[] args ) throws IOException
    {	
		ArrayList<Attribute> attributes_w = new ArrayList<Attribute>();
		ArrayList<Instance> instances_w = new ArrayList<Instance>();
		ArrayList<Attribute> attributes_c = new ArrayList<Attribute>();
		ArrayList<Instance> instances_c = new ArrayList<Instance>();
		ArrayList<Integer> test = new ArrayList<Integer>();
		ArrayList<Integer> train = new ArrayList<Integer>();
		ArrayList<Instance> inst= new ArrayList<Instance>();
		ArrayList<ArrayList<Integer>> set = new ArrayList<ArrayList<Integer>>();
		Node decisionTree;
		double[] std = new double[10] ;
		double mean = 0.0;
		
		
		File file_weather = new File(args[0]);
		File file_car = new File(args[1]);
		int depth = Integer.parseInt(args[2]);
		
		ArrayList<Integer> indices = new ArrayList<Integer>();
		for (int i=3; i<args.length; i++ )
			indices.add(Integer.parseInt(args[i]));
		
		Parser p = new Parser();
		p.parseArff(file_weather);		
		attributes_w = p.getAttributes();
		instances_w = p.getInstances();
		//System.out.println("Instances:"+instances_w.size());
		
		Parser q = new Parser();
		q.parseArff(file_car);		
		attributes_c = q.getAttributes();
		instances_c = q.getInstances();
		//System.out.println("Instances:"+instances_c.size());
		
	
		
		
		
		
	
		double entropy = entropyOnSubset(attributes_c.get(5),indices, instances_c);
		
		
		System.out.println("Entropy on Subset:");
		System.out.println(entropy);
	
		/*
		double infogain = informationGain(attributes.get(2),attributes.get(0),indices, instances);
		
		System.out.println("InfoGain on Subset:");
		System.out.println(infogain);
		*/
		int size = 0;
		int[] count = new int[10];
		
		for (int i = 0; i<10; i++){
			
			size = instances_c.size()/3;	
			set = redraw(instances_c, size);
			test.clear();
			train.clear();
			test.addAll(set.get(0));
			train.addAll(set.get(1));
			
			decisionTree = trainModelOnSubset(attributes_c, instances_c, attributes_c.get(6),train, 0);
			
			for (int j = 0; j<test.size(); j++){
				if(instances_c.get(test.get(j)).getValue(attributes_c.get(6)).equals(decisionTree.classify(instances_c.get(test.get(j))))){
					count[i]++;
				}

			}
			
			mean += count[i]; 
		}
		
		mean/= 10.0;
		mean/=size;
		
		for (int i = 0; i<10; i++){
			std[i]=(double)count[i]/size-mean;
			System.out.println("Std deviation wo depth step "+(i+1)+" :"+std[i]);
		}
		
		
		
		System.out.println("Mean Accuracy wo depth:");
		System.out.println(mean);
		
    }
	
	
	

    public static Node trainModel(ArrayList<Attribute> attributes, ArrayList<Instance> instances, Attribute goal, int depth){
    	
    	ArrayList<Integer> list = new ArrayList<Integer>();
    	for (int j=0; j < instances.size();j++){
    		list.add(j);
    	}
    	return trainModelOnSubset(attributes, instances, goal, list, depth);
    }
    
    public static Node trainModelOnSubset(ArrayList<Attribute> attributes, ArrayList<Instance> instances, Attribute goal, ArrayList<Integer> indices, int depth){
    	
    	Attribute bestAtt;
    	
    	
	    if(usedValuesInAttribute(goal, indices, instances).size() == 1) {
	    	 return leaf(usedValuesInAttribute(goal, indices, instances).get(0));
	    }
	    else if(attributes.size() == 0 || depth == 0) {
	    	return leaf(mostCommonValueOfAttribute(instances, indices, goal));
	    }
	    else { // all code after that belongs to this else block

	    	
		double maxInfoGain = -Double.MAX_VALUE;
		int index = -1;
		/**
		 * This for loop calculates the index of the attribute with highest informationGain
		 */
		for(int i = 0; i < attributes.size() - 1; i++) {
			if(maxInfoGain < informationGain(attributes.get(i), goal, indices, instances)) {
				maxInfoGain = informationGain(attributes.get(i), goal, indices, instances);
				index = i;
			}
		}
		
		bestAtt = attributes.get(index);
		attributes.remove(index);
		
		ArrayList<String> usedValues = usedValuesInAttribute(bestAtt, indices, instances);
		ArrayList<Integer> filter = new ArrayList<Integer>();
		HashMap<String, Node> children = new HashMap<String, Node> ();
		
		for (int i = 0 ; i < usedValues.size(); i++){
			filter = filterByValue(usedValues.get(i), bestAtt, indices, instances);
			children.put(usedValues.get(i),trainModelOnSubset(attributes, instances, goal, filter, depth -1));
		}

		 return nextNode(bestAtt, children);
		}
	  }

    public static Leaf leaf(String value){
    	return new Leaf(value);
    }
    
    public static Node nextNode(Attribute root, HashMap<String, Node> nodes){
    	return new InnerNode(root, nodes);
    }
    	
    //splits indices of instances in train and test set
	public static ArrayList<ArrayList<Integer>> redraw(ArrayList<Instance> inst, int size){
		
		ArrayList<Integer> indices = new ArrayList<Integer>();
		ArrayList<Integer> train = new ArrayList<Integer>();
		ArrayList<Integer> test= new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> set = new ArrayList<ArrayList<Integer>>();
		
		for(int i=0; i<inst.size(); i++)
			indices.add(i);
		
		Collections.shuffle(indices);
		test.addAll(indices.subList(0, size)); 
		train.addAll(indices.subList(size,inst.size()));
		
		set.add(test);
		set.add(train);
		
		return set;
		
	}

	public static double entropyOnSubset(Attribute att, ArrayList<Integer> indices, ArrayList<Instance> instances){
		
		double entropy=0;
		int count[] = new int[att.values.length];
		
		if (!instances.get(0).hasAttribute(att)){
			System.out.println("Attribute not part of Instance!");
			return 0;
		}
			
					
		for (int i: indices){
			if(i > instances.size()){
				System.out.println("Index is too large!");
				return 0;
			}
			else{
				for (int j=0; j < att.values.length;j++){
					//System.out.println(instances.get(i).getValue(att)+" - "+ att.values[j]);
					if (att.values[j].equals(instances.get(i).getValue(att))){
						count[j]++;
					}		
				}
			}
		}

		
		for (int j=0; j < att.values.length;j++){
			if(count[j]!=0){
				entropy+=(double)count[j]/indices.size()*(Math.log((double)count[j]/indices.size())/Math.log(2.0));
			}
		}
		entropy= -entropy;
		return entropy;
		
	}
	
	public static double informationGain(Attribute att, Attribute att2, ArrayList<Integer> indices, ArrayList<Instance> instances){
		
		
		
		ArrayList<Integer> sv = new ArrayList<Integer>();
		double infoGain = 0.0;
		double entropy = entropyOnSubset(att,indices, instances);
		
		if (!instances.get(0).hasAttribute(att2)){
			System.out.println("Attribute not part of Instance!");
			return 0;
		}
			
		for (int j=0; j < att2.values.length;j++){
			sv.clear();
			for (int i: indices){
				if(i > instances.size()){
					System.out.println("Index is too large!");
					return 0;
				}
				else{
					if (att2.values[j].equals(instances.get(i).getValue(att2))){
						sv.add(i);
					}		
				}
			}
			if (!sv.isEmpty())
				infoGain += (double)sv.size()/indices.size()*entropyOnSubset(att, sv, instances);
			//System.out.println(sv+" "+sv.size()+" "+entropyOnSubset(att, sv, instances));
			
		}
		return entropy-infoGain;	
	
	}
    
	public static ArrayList<String> usedValuesInAttribute( Attribute att, ArrayList<Integer> indices, ArrayList<Instance> instances){
		
		ArrayList<String> used = new ArrayList<String>();
		for (int i = 0; i<indices.size(); i++){
			for (int j = 0; j<att.values.length; j++){
				if(att.values[j].equals(instances.get(indices.get(i)).getValue(att)))
					used.add(att.values[j]);
			}
		}
		return used;
	}
	
	public static ArrayList<Integer> filterByValue (String value, Attribute att, ArrayList<Integer> indices, ArrayList<Instance> instances){
		
		ArrayList<Integer> filter = new ArrayList<Integer>();
		for (int i = 0; i<indices.size(); i++){
				if(value.equals(instances.get(indices.get(i)).getValue(att)))
					filter.add(indices.get(i));
		}
		return filter;
	}

	public static String mostCommonValueOfAttribute(ArrayList<Instance> instances, ArrayList<Integer> indices, Attribute att){
		
		int max=-1;
		int counter;
		int index = -1;
		for (int j = 0; j<att.values.length; j++){
			counter = 0;
			for (int i = 0; i<indices.size(); i++){
				if(att.values[j].equals(instances.get(indices.get(i)).getValue(att)))
					counter++;
			}
			if (counter > max){
				max = counter;
				index = j;
			}
		
		}
		return att.values[index];
		
	}
}
