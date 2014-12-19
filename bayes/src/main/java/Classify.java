import BayesianClassifier.bayes.BayesClassifier;
import BayesianClassifier.bayes.Tokenizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Trains a classifier with files in the train/F and train/M folders
 * and then classifies all files in the test/F and test/M folders, then shows the confusion matrix.
 *
 * Created by kevin on 18-12-14.
 */
public class Classify {

    public static final String PARENT_DIR = "./bayes/res";
    public static final String TEST_DIR = "/blogstest";
    public static final String TRAIN_DIR = "/blogstrain";

    public static HashMap<String,LinkedList<String>> getTrainingFiles() {
        LinkedList<String> dirs = new LinkedList<>();

        for(String unit : new File(PARENT_DIR+TRAIN_DIR).list()){
            if(new File(PARENT_DIR+TRAIN_DIR+"/"+unit).isDirectory()){
                dirs.add(unit);
            }
        }

        HashMap<String,LinkedList<String>> filesInTraining = new HashMap<>();

        for(String dir : dirs){
            LinkedList<String> files = new LinkedList<>();

            for(String file : new File(PARENT_DIR+TRAIN_DIR+"/"+dir).list()){
                if(new File(PARENT_DIR+TRAIN_DIR+"/"+dir+"/"+file).isFile()){
                    files.add(PARENT_DIR+TRAIN_DIR+"/"+dir+"/"+file);
                }
            }

            filesInTraining.put(dir, files);
        }

        return filesInTraining;
    }

    private static HashMap<String, LinkedList<String>> getTestingFiles() {
        LinkedList<String> dirs = new LinkedList<>();

        for(String unit : new File(PARENT_DIR+TEST_DIR).list()){
            if(new File(PARENT_DIR+TEST_DIR+"/"+unit).isDirectory()){
                dirs.add(unit);
            }
        }

        HashMap<String,LinkedList<String>> filesInTraining = new HashMap<>();

        for(String dir : dirs){
            LinkedList<String> files = new LinkedList<>();

            for(String file : new File(PARENT_DIR+TEST_DIR+"/"+dir).list()){
                if(new File(PARENT_DIR+TEST_DIR+"/"+dir+"/"+file).isFile()){
                    files.add(PARENT_DIR+TEST_DIR+"/"+dir+"/"+file);
                }
            }

            filesInTraining.put(dir, files);
        }

        return filesInTraining;
    }

    public static void main(String[] args) {

        HashMap<String, LinkedList<String>> filesInTraining = getTrainingFiles();
        HashMap<String, LinkedList<String>> filesInTesting = getTestingFiles();


        System.out.println("filesInTraining = " + String.valueOf(filesInTraining));
        System.out.println("filesInTesting = " + String.valueOf(filesInTesting));

        // Training stage
        BayesClassifier classifier = new BayesClassifier("ai_assignment_classifier.byc");
        //HashMap<String, BayesClassifier> classifiers = new HashMap<>();

        for(String type : filesInTraining.keySet()){
            try {
                trainFiles(type, filesInTraining.get(type), classifier);
            } catch (FileNotFoundException e) {
                System.err.println("File not found: "+e.getMessage());
            }
        }

        System.out.println("\nTraining results:");
        System.out.println(String.valueOf(classifier.getDataset()));

        // Testing stage
        HashMap<String, HashMap<String, LinkedList<String>>> types = new HashMap<>();
        for(String type : filesInTesting.keySet()){
            try{
                types.put(type, classifyFiles(type, filesInTesting.get(type), classifier));
            } catch (FileNotFoundException e) {
                System.err.println("File not found: "+e.getMessage());
            }
        }

        // Prepare confusion matrix
        HashMap<String, HashMap<String,Integer>> counters = new HashMap<>();
        LinkedList<String> typeStrings = new LinkedList<>();
        for(String type : types.keySet()){
            typeStrings.add(type);
            HashMap<String, LinkedList<String>> classifications = types.get(type);
            for(String file : classifications.keySet()){
                LinkedList<String> classification = classifications.get(file);
                String expected = classification.get(0);
                String predicted = classification.get(1);
                if(counters.containsKey(expected)){
                    if(counters.get(expected).containsKey(predicted)){
                        Integer c = counters.get(expected).get(predicted);
                        c++;
                        counters.get(expected).put(predicted, c);
                    }else{
                        counters.get(expected).put(predicted, 1);
                    }
                }else{
                    HashMap<String,Integer> c = new HashMap<>();
                    c.put(predicted, 1);
                    counters.put(expected, c);
                }
            }
        }

        // Print confusion matrix
        System.out.printf("%20s%10s%10s\n", "expected\\predicted", typeStrings.get(0), typeStrings.get(1));
        for(String expected : counters.keySet()){
            HashMap<String,Integer> predicted = counters.get(expected);
            System.out.printf("%20s%10s%10s\n", expected, predicted.get(typeStrings.get(0)), predicted.get(typeStrings.get(1)));
        }


    }

    private static HashMap<String, LinkedList<String>> classifyFiles(final String type, LinkedList<String> strings, BayesClassifier classifier) throws FileNotFoundException {
        HashMap<String, LinkedList<String>> classifications = new HashMap<>();

        for(String file : strings){
            String content = new Scanner(new File(file)).useDelimiter("\\Z").next();
            HashMap<String, LinkedList<String>> words = new HashMap<>();

            for(final String word : Tokenizer.tokens(content)){
                final String classification = classifier.classify("word", word);
                words.put(word, new LinkedList<String>(){{add(type);add(classification);}});
            }

            int totalWordCount = words.keySet().size();
            HashMap<String,Integer> classificationCounts = new HashMap<>();

            for(String word : words.keySet()){
                String classification = words.get(word).get(1);
                if(classificationCounts.containsKey(classification)){
                    Integer c = classificationCounts.get(classification)+1;
                    classificationCounts.put(classification, c);
                }else{
                    classificationCounts.put(classification, 1);
                }
            }

            int maxCount = 0;
            String maxClassification = "";

            for(String classification : classificationCounts.keySet()){
                int count = classificationCounts.get(classification);
                if(count > maxCount){
                    maxCount = count;
                    maxClassification = classification;
                }
            }

            final String classification = maxClassification;

            System.out.println("File " + file + " classified as " + maxClassification + " because " + maxCount + " of " + totalWordCount + " words were " + maxClassification + ".");



            classifications.put(file, new LinkedList<String>(){{add(type);add(classification);}});
        }
        return classifications;
    }

    private static void trainFiles(String type, LinkedList<String> strings, BayesClassifier classifier) throws FileNotFoundException {
        for(String file : strings){
            String content = new Scanner(new File(file)).useDelimiter("\\Z").next();
            //System.out.println(content);
            List<String> words = new LinkedList<>();
            Collections.addAll(words, Tokenizer.tokens(content));
            classifier.updateGroup(type, words);
        }
    }
}
