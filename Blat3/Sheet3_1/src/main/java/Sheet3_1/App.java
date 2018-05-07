package Sheet3_1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Iterator; 

import weka.core.Instances;
import weka.core.Instance;
import weka.classifiers.trees.RandomForest;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemovePercentage;
import weka.classifiers.Evaluation;

public class App 
{
    public static void main( String[] args ) throws Exception
    {
    	File file = new File(args[0]);
    	BufferedReader r = new BufferedReader(new FileReader(file));
    	Instances instances = new Instances (r);
    	instances.setClassIndex(instances.numAttributes() - 1);
    	ArrayList<Instances> set = new ArrayList<Instances>();
      	int[] iterations = new int[] {1, 5, 10, 20, 30, 40, 50, 100};
    	double[][] accuracy = new double[10][8];

      	for (int k=0; k<10; k++){
      			set=redraw(instances,30);
	    	
	    	Instances train = new Instances (set.get(0));
	    	Instances test = new Instances (set.get(1));
	    	
	    	RandomForest rf = new RandomForest();
 	    	
	    	rf.buildClassifier(train);
	 
//	    	System.out.println(test.numInstances());
	    	int count=0;
	    	
	    	for(int j=0; j < iterations.length ; j++){
	    	   	rf.setNumIterations(iterations[j]);
	    	   	count=0;
		    	for(int i=0; i < test.numInstances() ; i++){
		//    		System.out.println(test.classAttribute().value((int)test.get(i).value(test.classAttribute()))+ " _ "+test.classAttribute().value((int) rf.classifyInstance(test.get(i))));
		    		if((int)test.get(i).value(test.classAttribute()) == (int) rf.classifyInstance(test.get(i))){
		    			count++;
		    		}
		    	}
		    	accuracy[k][j] = (double)count/test.numInstances(); 
//		    	System.out.println("Number of trees: "+iterations[j]+" Accuracy: "+(double)count/test.numInstances());
	    	}
    	}
      	
      	double mean = 0;
      	double stdev=0;
     	for (int i=0; i<8; i++){
     		mean = 0;
     		System.out.println(" Number of trees: "+iterations[i]);
     	 	for (int j=0; j<10; j++){
     	 		System.out.println("Iteration: "+ j +" Accuracy: "+accuracy[j][i]);
     	 		mean +=accuracy[j][i];
     	 	}
     	 	mean /= 10;
     	 	System.out.println("Mean: "+mean);
     	 	stdev = 0;
     	 	for (int j=0; j<10; j++){
     	 		stdev += (accuracy[j][i]-mean)*(accuracy[j][i]-mean);
     	 	}
     	 	stdev/=9;
     	 	stdev= Math.sqrt(stdev);
     	 	System.out.println("StdDev: "+ stdev);
     	 	System.out.println();
     	}   	
    }
    
    
	public static ArrayList<Instances> redraw(Instances inst, double percentage) throws Exception{
		
	
		ArrayList<Instances> set = new ArrayList<Instances>();
		
		Random rand = new Random();
		inst.randomize(rand);
		
		RemovePercentage rm = new RemovePercentage();
		rm.setPercentage(percentage);
		rm.setInputFormat(inst);
		
		Instances train = new Instances(inst);
		Instances test = new Instances(inst);
		
		rm.setInvertSelection(false);
		train = Filter.useFilter(train, rm);
	    
		for (int i = (inst.numInstances()-train.numInstances()); i < inst.numInstances();i++)
			test.remove(inst.numInstances()-train.numInstances());
		
		set.add(train);
		set.add(test);
		return set;
		
	}
}
