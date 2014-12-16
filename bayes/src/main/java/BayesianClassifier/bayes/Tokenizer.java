package BayesianClassifier.bayes;

import java.util.LinkedList;

public class Tokenizer {

    public static String[] tokens(String input) {
        input = input.replaceAll("['\\\".,?/;:_()\\[\\]&-]", "");
        input = input.replace("$", " $ ");
        input = input.replace("€", " € ");
        input = input.replace("%", " % ");
        input = input.replace("!", " ! ");
        input = input.replace("@", " @ ");
        input = input.replace("#", " # ");
        input = input.replace("*", " * ");

        String[] tokens = input.split(" ");
        LinkedList<String> normalized = new LinkedList<>();
        int i;
        for (i = 0; i <= tokens.length - 1; i++) {
            if (!tokens[i].equals("")) {
                normalized.add(tokens[i].toLowerCase());
            }
        }
        String[] output = new String[normalized.size()];
        int j = 0;
        for (String s : normalized) {
            output[j] = s;
            j++;
        }
        return output;
    }
}
