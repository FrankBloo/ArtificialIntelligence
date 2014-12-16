/*
 *    TextDirectoryToArff.java
 *    Copyright (C) 2002 Richard Kirkby
 *
 *	  This script is based on the work of Richard Kirby.
 *
 */


package BayesianClassifier.bayes.Arff;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class ArffGenerator {

    public Instances createDataset(String directoryPath) {

        FastVector attributes = new FastVector(2);
        attributes.addElement(new Attribute("filename", (FastVector) null));
        attributes.addElement(new Attribute("contents", (FastVector) null));
        Instances data = new Instances("text_files_in_" + directoryPath,
                attributes, 0);

        File directory = new File(directoryPath);
        String[] files = directory.list();
        for (String file : files) {
            if (file.endsWith(".txt")) {
                try {
                    double[] newInst = new double[2];
                    newInst[0] = (double) data.attribute(0).addStringValue(
                            file);
                    File txt = new File(directoryPath + File.separator
                            + file);
                    InputStreamReader is;
                    is = new InputStreamReader(new FileInputStream(txt));
                    StringBuilder text = new StringBuilder();
                    int k;
                    while ((k = is.read()) != -1) {
                        text.append((char) k);
                    }
                    newInst[1] = (double) data.attribute(1).addStringValue(
                            text.toString());
                    data.add(new Instance(1.0, newInst));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return data;
    }
}