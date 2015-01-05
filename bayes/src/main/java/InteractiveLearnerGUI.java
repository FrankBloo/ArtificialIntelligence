import BayesianClassifier.bayes.BayesClassifier;
import BayesianClassifier.bayes.Tokenizer;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

/**
 * Created by kevin on 5-1-15.
 */
public class InteractiveLearnerGUI {
    private JTextField directoryField;
    private JTextField fileField;
    private JButton chooseDirectoryButton;
    private JButton chooseFileButton;
    private JRadioButton trainThisFileRadioButton;
    private JRadioButton categorizeThisFileRadioButton;
    private JTextArea categoryField;
    private JButton executeButton;
    private JPanel learnerGuiPanel;
    private JButton trainButton;
    private JComboBox categorySelector;

    BayesClassifier classifier = new BayesClassifier("ILGUI.byc");

    public InteractiveLearnerGUI() {

        categoryField.setBorder(new LineBorder(Color.black, 1));

        chooseDirectoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                JFileChooser chooser = new JFileChooser(new File(".").getAbsolutePath());
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = chooser.showOpenDialog(chooseDirectoryButton);
                String folder = chooser.getSelectedFile().getAbsolutePath();
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    System.out.println("Folder: " + folder);
                }

                directoryField.setText(folder);

            }
        });
        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser chooser = new JFileChooser(new File(".").getAbsolutePath());
                int returnVal = chooser.showOpenDialog(chooseFileButton);
                String file = chooser.getSelectedFile().getAbsolutePath();
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    System.out.println("File: " + file);
                }

                fileField.setText(file);
            }
        });
        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(trainThisFileRadioButton.isSelected()){
                    final String file = fileField.getText();
                    LinkedList<String> fileList = new LinkedList<String>(){{add(file);}};
                    String type = categorySelector.getSelectedItem().toString();

                    try {
                        trainFiles(type, fileList, classifier);
                    } catch (FileNotFoundException e) {
                        System.err.println("File not found: "+e.getMessage());
                    }

                    categoryField.setText("Training of file " + file + " complete!");
                }else if(categorizeThisFileRadioButton.isSelected()){
                    final String file = fileField.getText();
                    String type = "unknown";

                    try{
                        type = classifyFiles(type, file, classifier);
                    } catch (FileNotFoundException e) {
                        System.err.println("File not found: "+e.getMessage());
                    }

                    categoryField.setText("File " + file + " classified as: " + type);
                }
            }
        });
        trainThisFileRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                categoryField.setEnabled(true);
            }
        });
        categorizeThisFileRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                categoryField.setEnabled(true);
            }
        });
        trainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String dir = directoryField.getText();
                HashMap<String, LinkedList<String>> filesInTraining = getTrainingFiles(dir);

                for(String type : filesInTraining.keySet()){
                    try {
                        trainFiles(type, filesInTraining.get(type), classifier);
                    } catch (FileNotFoundException e) {
                        System.err.println("File not found: "+e.getMessage());
                    }
                }

                categoryField.setText("Training of directory " + dir + " complete!");

                // Update dropdown
                HashMap<String, HashMap<String, HashMap<String, Integer>>> dataset = classifier.getDataset();
                for(String key : dataset.keySet()){
                    categorySelector.addItem(key);
                }

            }
        });
    }

    private static String classifyFiles(final String type, String file, BayesClassifier classifier) throws FileNotFoundException {

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



        return classification;
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

    public static HashMap<String,LinkedList<String>> getTrainingFiles(String train_dir) {
        LinkedList<String> dirs = new LinkedList<>();

        for(String unit : new File(train_dir).list()){
            if(new File(train_dir+"/"+unit).isDirectory()){
                dirs.add(unit);
            }
        }

        HashMap<String,LinkedList<String>> filesInTraining = new HashMap<>();

        for(String dir : dirs){
            LinkedList<String> files = new LinkedList<>();

            for(String file : new File(train_dir+"/"+dir).list()){
                if(new File(train_dir+"/"+dir+"/"+file).isFile()){
                    files.add(train_dir+"/"+dir+"/"+file);
                }
            }

            filesInTraining.put(dir, files);
        }

        return filesInTraining;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("InteractiveLearnerGUI");
        frame.setContentPane(new InteractiveLearnerGUI().learnerGuiPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
