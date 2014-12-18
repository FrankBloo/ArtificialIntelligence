package BayesianClassifier.bayes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class BayesClassifier {
	private String filename = null;
	private boolean isTrained = false;

	private HashMap<String, HashMap<String, HashMap<String, Integer>>> frequencyTable = new HashMap<>();
	private int totalInstanceCount = 0;
	private int totalClassCount = 0;
	private HashMap<String, Integer> instancesPerClass = new HashMap<>();

	public BayesClassifier(String filename) {
		this.filename = filename;

		try {
			File file = new File(filename);
			if (file.exists()) {
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				Object o = ois.readObject();
				if (o instanceof HashMap) {
					this.frequencyTable = (HashMap<String, HashMap<String, HashMap<String, Integer>>>) o;
				}
				this.isTrained = true;
			} else {
				this.frequencyTable = new HashMap<>();
			}
		} catch (ClassNotFoundException | IOException | ClassCastException e) {
			System.out.println("Crash. Error: " + e.getClass().getName() + ": "
					+ e.getLocalizedMessage());
			System.exit(1);
		}
	}

	public boolean getTrainedStatus() {
		return this.isTrained;
	}

	public HashMap<String, HashMap<String, HashMap<String, Integer>>> getDataset() {
		return this.frequencyTable;
	}

	public void removeDataset() {
		try {
			File file = new File(this.filename);
			if (file.exists()) {
				file.delete();
			}
			FileOutputStream fis = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fis);
			oos.writeObject(this.getDataset());
			oos.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void updateGroup(String classifier, List<String> words) {
		for (String word : words) {
			this.update(classifier, "word", word);
		}
	}

	public HashMap<String, List<String>> classifyGroup(List<String> words) {
		HashMap<String, List<String>> result = new HashMap<>();

		for (String word : words) {
			String classifier = this.classify("word", word);
			List<String> list = new LinkedList<>();
			if (result.get(classifier) != null) {
				list = result.get(classifier);
			}
			list.add(word);
			result.put(classifier, list);
		}

		return result;
	}

	public void update(String classifier, String attribute, String value) {
		// Increment total instance count
		this.totalInstanceCount++;

		if (this.instancesPerClass.get(classifier) == null) {
			// This class is not in the map yet. Add it and increment class
			// counter
			this.totalClassCount++;
			this.instancesPerClass.put(classifier, 1);
			this.frequencyTable.put(classifier,
					new HashMap<String, HashMap<String, Integer>>());
		}

		// Classifier now sure to exist.
		// Skip missing values
		if (!value.equals("?")) {
			if (this.frequencyTable.get(classifier).get(attribute) != null) {
				HashMap<String, Integer> val = this.frequencyTable.get(
						classifier).get(attribute);
				if (val.get(value) != null) {
					// Attribute and value exist. Increment it
					Integer freq = this.frequencyTable.get(classifier)
							.get(attribute).get(value);
					this.frequencyTable.get(classifier).get(attribute)
							.put(value, freq + 1);
				} else {
					// Attribute exists but value doesn't. Create it
					this.frequencyTable.get(classifier).get(attribute)
							.put(value, 1);
				}
			} else {
				// Attribute does not exist, add it with a new value.
				HashMap<String, Integer> val = new HashMap<>();
				val.put(value, 1);
				this.frequencyTable.get(classifier).put(attribute, val);
			}
		}

	}

	public String classify(String attribute, String value) {
		int m = 2; // Control for laplace-estimates
		int k = 1; // Control for M-estimates
		double likelihood = -100000; // Initial, impossible, likelihood
		String result = null;

		for (String classifier : this.instancesPerClass.keySet()) {
			Integer instances = this.instancesPerClass.get(classifier);

			double prior = ((double) instances + (double) k)
					/ ((double) this.totalInstanceCount + ((double) k * (double) this.totalClassCount));
			double temp = Math.log(prior); // Use logs for small values

			// Skip missing values
			if (!value.equals("?")) {
				Integer valueCount = this.frequencyTable.get(classifier)
						.get(attribute).get(value);
				if (valueCount == null) {
					valueCount = 0;
				}
				double inc = (valueCount + (m * prior)) / (instances + m); // P[Ei
																			// |
																			// H]
				temp += Math.log(inc); // Adding log s= multiplication
			}

			// Check if we have a better likelihood
			if (temp >= likelihood) {
				likelihood = temp;
				result = classifier;
			}
		}

		return result;
	}

	/**
	 * Returns a description of the classifier.
	 *
	 * @return a description of the classifier as a string.
	 */
	public String toString() {
		// These are just examples, modify to suit your algorithm

		// append valid text here.

		return "Naive Bayes Classifier";
	}
}
