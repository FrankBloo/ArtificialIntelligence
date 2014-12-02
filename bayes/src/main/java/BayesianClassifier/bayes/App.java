package BayesianClassifier.bayes;

/**
 * Hello world!
 *
 */
public class App 
{
	public static BayesClassifier[] classifiers;
    public static void main( String[] args )
    {
    	classifiers = new BayesClassifier[2];
    	classifiers[0] = new BayesClassifier("M");
    	classifiers[1] = new BayesClassifier("F");
    	
    	
    }
}
