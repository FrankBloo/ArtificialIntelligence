/*
 *    TextDirectoryToArff.java
 *    Copyright (C) 2002 Richard Kirkby
 *
 *	  This script is based on the work of Richard Kirby.
 *
 */

package BayesianClassifier.bayes.Arff;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class ArffGenerator {

	public Instances createDataset(String directoryPath) throws IOException {

		FastVector atributes = new FastVector(2);
		atributes.addElement(new Attribute("fileName", (FastVector) null));
		atributes.addElement(new Attribute("contents", (FastVector) null));
		Instances data = new Instances("text_files_in_" + directoryPath,
				atributes, 0);

		File directory = new File(directoryPath);
		String[] files = directory.list();
		for (int i = 0; i < files.length; i++) {
			if (files[i].endsWith(".txt")) {
				try {
					double[] newInst = new double[2];
					newInst[0] = (double) data.attribute(0).addStringValue(
							files[i]);
					File txt = new File(directoryPath + File.separator
							+ files[i]);
					InputStreamReader is;
					is = new InputStreamReader(new FileInputStream(txt));
					StringBuffer text = new StringBuffer();
					int k;
					while ((k = is.read()) != -1) {
						text.append((char) k);
					}
					newInst[1] = (double) data.attribute(1).addStringValue(
							text.toString());
					data.add(new Instance(1.0, newInst));
				} catch (IOException e) {
				}
			}
		}
		return data;
	}

	public static void main(String[] args) {
		if (args.length == 2) {
			NewArffGenerator tdta = new NewArffGenerator();
			try {
				Instances dataset = tdta.createDataset(args[0]);
				ArffSaver saver = new ArffSaver();
				saver.setInstances(dataset);
				File outputFile = new File(args[1]);
				FileOutputStream fos = new FileOutputStream(outputFile);
				// saver.setFile(outputFile);
				saver.setDestination(fos);
				saver.writeBatch();

				System.out.println(dataset);
			} catch (Exception e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.out
					.println("Usage: java TextDirectoryToArff <directory name>");
		}
	}
}