package BayesianClassifier.bayes;

public class tokenizer {

	public static String[] tokens(String input) {
		String[] tokens = input.split(" ");
		int i;
		for (i = 0; i <= tokens.length; i++) {
			tokens[i] = tokens[i].toLowerCase();
		}
		return tokens;
	}
}
