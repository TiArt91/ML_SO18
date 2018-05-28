package Sheet_5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	File file = new File(args[0]);
    	File testFile = new File(args[0]);
    	Dataset set;
    	Classifier c;
    	HashMap<String, Integer> a = new HashMap<String, Integer>();
    	
    	set = parseTrain(file,true);
      	c = trainModel(set);
    	
    	Iterator it = set.e.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,Integer> pair = (Map.Entry<String,Integer>)it.next();
	        if (pair.getValue()>30){
	        	//System.out.println(pair);
	        	a.put(pair.getKey(), pair.getValue());
	        }
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    
	    System.out.println(a.size());
	    System.out.println(a.containsKey("the"));
	    System.out.println(a.containsKey("density"));
    }
    
    
    
    
    
    
    
    
    
    // the parser is basically wordcount
	public static Dataset parseTrain(File file, boolean prefilter) throws IOException{
		
		String line = null;
		int n = 0;
		BufferedReader r = new BufferedReader(new FileReader(file));
		HashSet<String> vocabulary= new HashSet<String>();
		final Dataset d;
		int countA=0;
		int countB=0;
		int countE=0;
		int countV=0;
		
		//stopWords from github
		String[] stopwords = {"a", "as", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "aint", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "zero"};
		Set<String> stopWordSet = new HashSet<String>(Arrays.asList(stopwords));
		
		HashMap<String, Integer> a = new HashMap<String, Integer>();
		HashMap<String, Integer> b = new HashMap<String, Integer>();
		HashMap<String, Integer> e = new HashMap<String, Integer>();
		HashMap<String, Integer> v = new HashMap<String, Integer>();
		
		while ((line = r.readLine()) != null) {
			String[] words;
			String[] tmp;
			line=line.replaceAll("\"", "");
			tmp = line.split("\t");
			

	    	if(tmp[0].equals("A")) {
		    	n++;
		    	countA++;
		    	words=tmp[1].split(" ");
		    	for (int i=0; i<words.length; i++){
		    		if (!vocabulary.contains(words[i]) & !stopWordSet.contains(words[i]));
		    			vocabulary.add(words[i]);
		    		if(a.containsKey(words[i]) & !stopWordSet.contains(words[i])){
		    			a.replace(words[i], a.get(words[i])+1);
		    		}
		    		else {
		    			if (!stopWordSet.contains(words[i]))
		    				a.put(words[i], 1);
		    		}
		    	}
	    	}
		    	
		    	if(tmp[0].equals("B")) {
			    	n++;
			    	countB++;
			    	words=tmp[1].split(" ");
			    	for (int i=0; i<words.length; i++){
			    		if (!vocabulary.contains(words[i]) & !stopWordSet.contains(words[i]));
			    			vocabulary.add(words[i]);
			    		if(b.containsKey(words[i]) & !stopWordSet.contains(words[i])){
			    			b.replace(words[i], b.get(words[i])+1);
			    		}
			    		else { 
			    			if(!stopWordSet.contains(words[i]))
			    				b.put(words[i], 1);
			    		}
			    	}
		    	}
		    	
				if(tmp[0].equals("E")) {
			    	n++;
			    	countE++;
			    	words=tmp[1].split(" ");
			    	for (int i=0; i<words.length; i++){
			    		if (!vocabulary.contains(words[i]) & !stopWordSet.contains(words[i]));
			    			vocabulary.add(words[i]);
			    		if(e.containsKey(words[i])& !stopWordSet.contains(words[i])){
			    			e.replace(words[i], e.get(words[i])+1);
			    		}
			    		else { 
			    			if (!stopWordSet.contains(words[i]))
			    				e.put(words[i], 1);
			    		}
			    	}
				}
		    	if(tmp[0].equals("V")) {
			    	n++;
			    	countV++;
			    	words=tmp[1].split(" ");
			    	for (int i=0; i<words.length; i++){
			    		if (!vocabulary.contains(words[i]) & !stopWordSet.contains(words[i]));
			    			vocabulary.add(words[i]);
			    		if(v.containsKey(words[i])& !stopWordSet.contains(words[i])){
			    			v.replace(words[i], v.get(words[i])+1);
			    		}
			    		else { 
			    			if(!stopWordSet.contains(words[i]))
			    			v.put(words[i], 1);
			    		}
			    	}
		    	}
		    	
		}		    
		
			r.close();
			d=new Dataset(n,vocabulary,a,b,e,v,countA,countB,countE,countV);
		return d;
		
	}

    // basic naive bayes algorithm
	@SuppressWarnings("unchecked")
	public static Classifier trainModel(Dataset set){
		
		Dataset d = new Dataset (set);
		Classifier c;
		
		int examples = d.countA+d.countB+d.countE+d.countV; // number of all words in Example
		
		int countA=d.countA; // number of all words in "A"
		int countB=d.countB;
		int countE=d.countE;
		int countV=d.countV;
		
		int nA=d.a.size(); //number of distinct words in "A"
		int nB=d.b.size();
		int nE=d.e.size();
		int nV=d.v.size();

		double pVa=(double)countA/examples; // probability that text describes "A"
		double pVb=(double)countB/examples;
		double pVe=(double)countE/examples;
		double pVv=(double)countV/examples;

		
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
	
	
	
	
	public static void classify(Classifier c){
		
	}
	
	
	
	
	
	
    
    
    
    
}
