package BayesianClassifier.bayes.Arff;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class ArffGenerator {

	public Instances createDataset(String directoryPath) throws Exception {

		FastVector atributes = new FastVector(2);
		atributes.addElement(new Attribute("filename", (FastVector) null));
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
				} catch (Exception e) {
				}
			}
		}
		return data;
	}
}