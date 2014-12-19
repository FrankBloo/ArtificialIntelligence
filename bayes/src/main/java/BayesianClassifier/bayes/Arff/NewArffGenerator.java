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

public class NewArffGenerator {

	public Instances createDataset(String directoryPath) throws IOException {

		boolean firstRun = true;

		FastVector atributes = new FastVector();

		Instances data = new Instances("text_files_in_" + directoryPath,
				(FastVector) atributes, 0);

		File directory = new File(directoryPath);
		String[] files = directory.list();

		for (int i = 0; i < files.length; i++) {
			if (files[i].endsWith(".txt")) {
				try {

					File txt = new File(directoryPath + File.separator
							+ files[i]);

					InputStreamReader is;
					is = new InputStreamReader(new FileInputStream(txt));
					StringBuffer text = new StringBuffer();
					int k;
					while ((k = is.read()) != -1) {
						text.append((char) k);

					}

					String[] tokens = BayesianClassifier.bayes.Tokenizer
							.tokens(text.toString());

					double[] newInst = new double[tokens.length];

					// newInst[0] = (double)
					// data.attribute(0).addStringValue(files[i]);

					for (int j = 0; j < tokens.length; j++) {
						// add the @attribute elements for each distinctive
						// word
						if (!atributes.contains(new Attribute(tokens[j]))) {
							atributes.addElement(new Attribute(tokens[j]));
						}
						int temp = atributes.indexOf(new Attribute(tokens[j]));
						String tempString = String.valueOf(temp);

						newInst[j] = (double) data.attribute(temp)
								.addStringValue(tempString);

						// System.out.println(tempString);
						// System.out.println(newInst[j]);
					}
					/*
					 * } else { String temp = ""; for (String t : tokens) { //
					 * add the word variables to the @data segment
					 * 
					 * temp += atributes.indexOf(t);
					 * 
					 * } // data.add(new Instance(atributes.indexOf(new //
					 * Attribute(t)), newInst));
					 * 
					 * 
					 * }
					 */

					// newInst[1] = (double)
					// data.attribute(1).addStringValue(text.toString());
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
