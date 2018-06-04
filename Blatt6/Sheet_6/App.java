package Sheet_5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;


public class App 
{
    public static void main( String[] args ) throws Exception
    {
    	int k = 10;
    	File file = new File(args[0]);
    	File testFile;
    	
    	Dataset set;
    	Classifier c;
    	double accuracy;
    	
    	ArrayList<ArrayList<String>> text;
    	ArrayList<ArrayList<ArrayList<String>>> split;
    	ArrayList<ArrayList<String>> train;
    	ArrayList<ArrayList<String>> test;
    	ArrayList<ArrayList<String>> testSets;
    	
    	text = parse(file);
    	text = shuffle(text);
    	split = split (text); 
    	test = split.get(0);
    	train = split.get(1);
    	
    	c = stratifiedCrossValidation(k, train);
    	accuracy = testModel(test, c);
    	System.out.println("Final Accuracy: " + accuracy);
    	
    	
    	testFile = new File(args[1]);
    	testSets=parse(testFile);
    	System.out.println("Accuracy with provided test set: " + testModel(testSets,c));
    	

    }
    
    
    public static ArrayList<ArrayList<ArrayList<String>>> split(ArrayList<ArrayList<String>> text){
    	
    	ArrayList<ArrayList<ArrayList<String>>> result = new ArrayList<ArrayList<ArrayList<String>>> (2);
    	result.add(new ArrayList<ArrayList<String>>());
    	result.add(new ArrayList<ArrayList<String>>());
    	
    	for(int i=0; i<text.size();i++)
    		if(i<1000)
    			result.get(0).add(text.get(i));
    		else
    			result.get(1).add(text.get(i));
    	
    	return result;
    	
    }
    
    public static ArrayList<ArrayList<String>> parse(File file) throws IOException{ 
    	
		String line = null;
		BufferedReader r = new BufferedReader(new FileReader(file));
    	ArrayList<ArrayList<String>> text = new ArrayList<ArrayList<String>>();
		
    	
    	while ((line = r.readLine()) != null) {
    		
			String[] tmp;
			line=line.replaceAll("\"", "");
			tmp = line.split("\t");
    		text.add( new ArrayList<String>(Arrays.asList(tmp)));
    		
    	}
    	r.close();
    	return text;
    }
     
    public static Dataset buildDataset(ArrayList<ArrayList<String>> text) throws IOException{
    	
    	int n = text.get(0).size()+text.get(1).size()+text.get(2).size()+text.get(3).size();
		HashSet<String> vocabulary= new HashSet<String>();
		final Dataset d;
		int countA=text.get(0).size();
		int countB=text.get(1).size();
		int countE=text.get(2).size();
		int countV=text.get(3).size();
		
		//stopWords from github
		String[] stopwords = {"a", "as", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "aint", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "zero"};
		Set<String> stopWordSet = new HashSet<String>(Arrays.asList(stopwords));
		
		HashMap<String, Integer> a = new HashMap<String, Integer>();
		HashMap<String, Integer> b = new HashMap<String, Integer>();
		HashMap<String, Integer> e = new HashMap<String, Integer>();
		HashMap<String, Integer> v = new HashMap<String, Integer>();
		
		for (int j=0; j < text.get(0).size();j++) {
			String[] words;
		    words=text.get(0).get(j).split(" ");
		    for (int i=0; i<words.length; i++){
		    	if(a.containsKey(words[i]) & !stopWordSet.contains(words[i])){
		    		a.replace(words[i], a.get(words[i])+1);
		    	}
		    	else {
		    		if (!stopWordSet.contains(words[i])){
		    			a.put(words[i], 1);
		    		}
		    		}
		    	}
	    	}
		    	
		for (int j=0; j < text.get(1).size();j++) {
			String[] words;
	    	words=text.get(1).get(j).split(" ");
	    	for (int i=0; i<words.length; i++){
	    		if(b.containsKey(words[i]) & !stopWordSet.contains(words[i])){
	    			b.replace(words[i], b.get(words[i])+1);
	    		}
	    		else { 
	    			if(!stopWordSet.contains(words[i])){
	    				b.put(words[i], 1);
	    			}
	    		}
	    	}
    	}
	    	
		for (int j=0; j < text.get(2).size();j++) {
			String[] words;
	    	words=text.get(2).get(j).split(" ");
	    	for (int i=0; i<words.length; i++){
	    		if(e.containsKey(words[i])& !stopWordSet.contains(words[i])){
	    			e.replace(words[i], e.get(words[i])+1);
	    		}
	    		else { 
	    			if (!stopWordSet.contains(words[i])){
	    				e.put(words[i], 1);
	    			}
	    		}
	    	}
		}
			
		for (int j=0; j < text.get(3).size();j++) {
			String[] words;
	    	words=text.get(3).get(j).split(" ");
	    	for (int i=0; i<words.length; i++){
	    		if(v.containsKey(words[i])& !stopWordSet.contains(words[i])){
	    			v.replace(words[i], v.get(words[i])+1);
	    		}
	    		else { 
	    			if(!stopWordSet.contains(words[i])){
	    				v.put(words[i], 1);
	    			}
	    		}
	    	}
    	}	
//'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''		
// Here only the 1000 most frequent words per set were selected		

		
		if(a.size() > 1000){
			
			Map<String, Integer> list = sortByComparator(a, false);
//			printMap(list);
			int count = 0;
			
			Iterator it =  list.entrySet().iterator();
			a.clear();
			
		    while (it.hasNext()) {
		        Map.Entry<String,Integer> pair = (Map.Entry<String,Integer>)it.next();
		    	if (count<1000){
			        a.put(pair.getKey(), pair.getValue());
		    	}
		    	else{
		    		break;
		    	}
		    	it.remove(); // avoids a ConcurrentModificationException
		    	count++;
		    }
		    	
		}
		
		if(b.size() > 1000){
			
			Map<String, Integer> list = sortByComparator(b, false);
//			printMap(list);
			int count = 0;
			
			Iterator it = list.entrySet().iterator();
			b.clear();
			
		    while (it.hasNext()) {
		        Map.Entry<String,Integer> pair = (Map.Entry<String,Integer>)it.next();
		    	if (count<1000){
			        b.put(pair.getKey(), pair.getValue());
		    	}
		    	else{
		    		break;
		    	}
		    	it.remove(); // avoids a ConcurrentModificationException
		    	count++;
		    }
		    	
		}
		
		if(e.size() > 1000){
			
			Map<String, Integer> list = sortByComparator(e, false);
//			printMap(list);
			int count = 0;
			
			Iterator it =  list.entrySet().iterator();
			e.clear();
			
		    while (it.hasNext()) {
		        Map.Entry<String,Integer> pair = (Map.Entry<String,Integer>)it.next();
		    	if (count<1000){
			        e.put(pair.getKey(), pair.getValue());
		    	}
		    	else{
		    		break;
		    	}
		    	it.remove(); // avoids a ConcurrentModificationException
		    	count++;
		    }
		    	
		}
		
		if(v.size() > 1000){
			
			Map<String, Integer> list = sortByComparator(v, false);
//			printMap(list);
			int count = 0;
			
			Iterator it =  list.entrySet().iterator();
			v.clear();
			
		    while (it.hasNext()) {
		        Map.Entry<String,Integer> pair = (Map.Entry<String,Integer>)it.next();
		    	if (count<1000){
			        v.put(pair.getKey(), pair.getValue());
		    	}
		    	else{
		    		break;
		    	}
		    	it.remove(); // avoids a ConcurrentModificationException
		    	count++;
		    }
		    	
		}
		
		
		vocabulary.addAll(a.keySet());
		vocabulary.addAll(b.keySet());
		vocabulary.addAll(e.keySet());
		vocabulary.addAll(v.keySet());
		d=new Dataset(n,vocabulary,a,b,e,v,countA,countB,countE,countV);
		return d;
		
	}
    		
    // basic naive bayes algorithm
	@SuppressWarnings("unchecked")
	public static Classifier trainModel(Dataset set){
		
		Dataset d = new Dataset (set.n,set.vocabulary,set.a,set.b,set.e,set.v,set.countA,set.countB,set.countE,set.countV);
		Classifier c;
		
		int countA=d.countA; // number of all words in "A"
		int countB=d.countB;
		int countE=d.countE;
		int countV=d.countV;
		
		int nA=d.a.size(); //number of distinct words in "A"
		int nB=d.b.size();
		int nE=d.e.size();
		int nV=d.v.size();
		
//		System.out.println(d.countA);
//		System.out.println(set.countA);
		
		
		
		
		double pVa=(double)countA/d.n; // probability that text describes "A"
		double pVb=(double)countB/d.n;
		double pVe=(double)countE/d.n;
		double pVv=(double)countV/d.n;

		
		HashMap<String, Double> pBa = new HashMap<String, Double>(); // conditional probability (Wk|"A")
		HashMap<String, Double> pBb = new HashMap<String, Double>();
		HashMap<String, Double> pBe = new HashMap<String, Double>();
		HashMap<String, Double> pBv = new HashMap<String, Double>();
		
		
		Iterator it = d.a.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,Integer> pair = (Map.Entry<String,Integer>)it.next();
	        pBa.put(pair.getKey(), (double)(pair.getValue()+1)/(nA+d.vocabulary.size()));
	        //System.out.println(pair.getKey() + " = " + pair.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    
		it = d.b.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,Integer> pair = (Map.Entry<String,Integer>)it.next();
	        pBb.put(pair.getKey(), (double)(pair.getValue()+1)/(nB+d.vocabulary.size()));
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    
		it = d.e.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,Integer> pair = (Map.Entry<String,Integer>)it.next();
	        pBe.put(pair.getKey(), (double)(pair.getValue()+1)/(nE+d.vocabulary.size()));
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    
		it = d.v.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,Integer> pair = (Map.Entry<String,Integer>)it.next();
	        pBv.put(pair.getKey(), (double)(pair.getValue()+1)/(nV+d.vocabulary.size()));
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    c = new Classifier(	pVa, pVb, pVe, pVv, pBa, pBb, pBe, pBv);

	    return c;
		
	}
	
	public static double testModel(ArrayList<ArrayList<String>> test, Classifier classifier){
		
		double accuracy=0;
    	int elements=test.size();
    	int count=0;
    	
    	for(int j=0; j < test.size(); j++){
    		
		   	if(classify(classifier,test.get(j).get(1)).equals(test.get(j).get(0)))
		    	count++;
    	}
    	accuracy = (double)count/elements; 
	
		return accuracy;
	}
	
	public static String classify(Classifier c, String text){
		
		HashSet<String> vocabulary= new HashSet<String>();
		vocabulary.addAll(c.pBa.keySet());
		vocabulary.addAll(c.pBb.keySet());
		vocabulary.addAll(c.pBe.keySet());
		vocabulary.addAll(c.pBv.keySet());
		
		String value = "empty"; 
		String[] words = text.split(" ");
		double[] p = {c.pVa,c.pVb,c.pVe,c.pVv};
		double max=-10;
	
		for (int i=0; i<words.length;i++){
			if (vocabulary.contains(words[i])){
				if (c.pBa.containsKey(words[i])){
					p[0]*=c.pBa.get(words[i]);
				}
				else{
					p[0]*=(double)1.0/(c.pBa.size()+vocabulary.size());
				}
				if (c.pBb.containsKey(words[i]))
					p[1]*=c.pBb.get(words[i]);
				else
					p[1]*=(double)1.0/(c.pBb.size()+vocabulary.size());
				if (c.pBe.containsKey(words[i]))
					p[2]*=c.pBe.get(words[i]);
				else
					p[2]*=(double)1.0/(c.pBe.size()+vocabulary.size());
				if (c.pBv.containsKey(words[i]))
					p[3]*=c.pBv.get(words[i]);
				else
					p[3]*=(double)1.0/(c.pBv.size()+vocabulary.size());
			}
		}
		
		
//		System.out.println("P(A)"+p[0]);
//		System.out.println("P(B)"+p[1]);
//		System.out.println("P(E)"+p[2]);
//		System.out.println("P(V)"+p[3]);
		
		if (p[0]>max){
			max=p[0];
			value="A";
		}
		if (p[1]>max){
			max=p[1];
			value="B";
		}
		if (p[2]>max){
			max=p[2];
			value="E";
		}
		if (p[3]>max){
			max=p[3];
			value="V";
		}
		
		return value;
		
	}
		
	public static ArrayList<ArrayList<String>> shuffle (ArrayList<ArrayList<String>> list) {
    	
		Collections.shuffle(list);
		return list;
    }
  
    public static ArrayList<ArrayList<String>> stratification (ArrayList<ArrayList<String>> text){
    	
    	ArrayList<ArrayList<String>> res = new ArrayList<ArrayList<String>>(4);
    	   	
    	for (int j=0; j < 4; j++)
    		res.add(new ArrayList<String>());
    	
		for (int j=0; j < text.size(); j++){

			if(text.get(j).get(0).equals("A"))
				res.get(0).add(text.get(j).get(1));
			if(text.get(j).get(0).equals("B"))
				res.get(1).add(text.get(j).get(1));
			if(text.get(j).get(0).equals("E"))
				res.get(2).add(text.get(j).get(1));
			if(text.get(j).get(0).equals("V"))
				res.get(3).add(text.get(j).get(1));			
    			
    		}
    	return res;
    }
    
    public static ArrayList<ArrayList<String>> trainSet(int i, int k, ArrayList<ArrayList<String>> text){
    	
    	ArrayList<ArrayList<String>> train = new ArrayList<ArrayList<String>>();
    	int numValues; // number of values for the ClassAttribute
    	
    	ArrayList<ArrayList<String>> set = new ArrayList<ArrayList<String>>();
    	
    	int n = text.size();
  	  	int numInst; // number of instances in k-ths portion
  	  	set=stratification(text);
  	  	numValues = set.size(); 

  	  	
  	    for (int l=0; l<numValues ; l++){
		  train.add(new ArrayList<String>());
  	    }
  	  	if (n % k == 0 )
  		  numInst = n / k;
  	  	else
  		  numInst = n / k+1;
  	  
  	  	int pos = i*numInst;
  	  	
  	  	int offset = pos % numValues;
 
//  	  	System.out.println("pos: "+pos);
  	  	pos /= numValues;
  	  	
  	  	
  	  //	System.out.println("num: "+numValues);
  	  //	System.out.println("offset: "+offset);
  	  
  	  	
  	  	//equal distribution of the classes (if classes are also equal distributed
  	  	int count =0;
  	  	for(int j=0;  j < n ;j++){
  	  		for (int l=0; l<numValues ; l++){
  	  			if (j<set.get(l).size()& (j*numValues+l<pos+offset | j*numValues+l > pos+offset+numInst-1) & count <n-numInst  ){
  	  				train.get(l).add(set.get(l).get(j));	
  	  				count++;
  	  			}
  	  		}
  	  	}
  	  return train;  	
  	  	
    	
    }
   
    public static ArrayList<ArrayList<String>> testSet(int i, int k, ArrayList<ArrayList<String>> text){
    	ArrayList<ArrayList<String>> test = new ArrayList<ArrayList<String>>();
    	int numValues; // number of values for the ClassAttribute
    	ArrayList<ArrayList<String>> set = new ArrayList<ArrayList<String>>();
    	
    	int n = text.size();
    	
  	  	int numInst; // number of instances in k-ths portion
  	  	set=stratification(text);
  	  	numValues = set.size();
  	  	
  	    for (int l=0; l<numValues ; l++){
		  test.add(new ArrayList<String>());
  	    }
  	  	if (n % k == 0 )
  		  numInst = n / k;
  	  	else
  		  numInst = n / k+1;
  	  
  	  	int pos = i*numInst;
  	  	int offset = pos % numValues;
  	  	pos /= numValues;		
  	  	
  	  	//equal distribution of the classes (if classes are also equal distributed
  	  	int count=0;
  	  	for(int j=0; count<numInst & j < numInst ;j++){
  	  		for (int l=offset; l<numValues & count<numInst; l++){
  	  			if (pos+j<set.get(l).size()){
  	  				test.get(l).add(set.get(l).get(pos+j));
  	  				count++;	
  	  			}
  	  		}
  	  		offset=0;
  	  	}
  	  	return test;
  	  
    }

    
    public static Classifier stratifiedCrossValidation (int k, ArrayList<ArrayList<String>> text) throws Exception{
    	
    	double[] res = new double[2] ;
    	double[] accuracy = new double[k] ;
    	ArrayList<ArrayList<String>> train, test;
    	Dataset trainDS;
    	text=shuffle(text);
    	Classifier[] classifier = new Classifier[k];
    	Classifier bestClassifier = new Classifier();
    	double max = -100;
    	
    	for (int i=0; i< k;i++ ){

 	    	train = trainSet(i,k,text); 
 	    	test = testSet(i,k,text);
 	    	trainDS = buildDataset(train);
 	    	
 	    	classifier[i]=trainModel(trainDS);
 	    	
 	    	int elements=0;
	    	int count=0;
	    	
	    	for(int j=0; j < test.size(); j++){
	    		for(int m=0; m < test.get(j).size(); m++){
	    			
	    			elements++;
	    		   	if (j==0)
	    		   		if(classify(classifier[i],test.get(j).get(m)).equals("A"))
	    		    			count++;
	    		   	if (j==1)
	    		   		if(classify(classifier[i],test.get(j).get(m)).equals("B"))
	    		    			count++;
	    		   	if (j==2)
	    		   		if(classify(classifier[i],test.get(j).get(m)).equals("E"))
	    		    			count++;
	    		   	if (j==3)
	    		   		if(classify(classifier[i],test.get(j).get(m)).equals("V"))
	    		    			count++;
	    		    		
	    		    }
	    	}
	    	accuracy[i] = (double)count/elements; 
	    	if (accuracy[i] > max){
	    		max = accuracy[i];
	    		bestClassifier = classifier[i];
	    	}
    	}
	
  	
    	double mean = 0;
    	double stdev=0;
 	 	for (int j=0; j<k; j++){
 	 		System.out.println("Iteration: "+ j +" Accuracy: "+accuracy[j]);
 	 		mean +=accuracy[j];
 	 	}
 	 	mean /= k;
 	 	System.out.println("Mean: "+mean);
 	 	stdev = 0;
 	 	for (int j=0; j<k; j++){
 	 		stdev += (accuracy[j]-mean)*(accuracy[j]-mean);
 	 	}
 	 	stdev/=(k-1);
 	 	stdev= Math.sqrt(stdev);
 	 	System.out.println("StdDev: "+ stdev);
 	 	System.out.println();

    	return bestClassifier;
    }
    
    public static void printMap(Map<String, Integer> map)
    {
    	int count=0;
    	System.out.println("Start: "+ map.size());
        for (Entry<String, Integer> entry : map.entrySet())
        {
            if (count<10)
            	System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
            else
            	break;
            count++;
        }
    }
	
    public static Map<String, Integer> sortByComparator(HashMap<String, Integer> unsortMap, final boolean order)
    {
    	
        List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Integer>>()
        {
            public int compare(Entry<String, Integer> o1,
                    Entry<String, Integer> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Entry<String, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

}
