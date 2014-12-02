package BayesianClassifier.bayes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class BayesClassifier {
	private Map<Integer, String> dataSet;
	private String filename = null;
	private boolean isTrained = false;

	public BayesClassifier(String filename){
		this.filename = filename;

		try{
			File file = new File(filename);
			if(file.exists()) {
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				this.dataSet = (Map<Integer, String>) ois.readObject();
				this.isTrained = true;
			}else{
				this.dataSet = new HashMap<>();
			}
		}catch(ClassNotFoundException | IOException | ClassCastException e){
			System.out.println("Crash. Error: "+e.getClass().getName()+": "+e.getLocalizedMessage());
			System.exit(1);
		}
	}

	public boolean getTrainedStatus(){
		return this.isTrained;
	}

	public Map<Integer, String> getDataset(){
		return this.dataSet;
	}
	
	public void removeDataset() {
		try {
			File file = new File(this.filename);
			if(file.exists()) {
				file.delete();
			}
			FileOutputStream fis = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fis);
			oos.writeObject(this.getDataset());
			oos.flush();
		}
		catch (IOException e) {
			
		}
	}

	/**
	 * Returns a description of the classifier.
	 *
	 * @return a description of the classifier as a string.
	 */
	public String toString() {
		//These are just examples, modify to suit your algorithm
		StringBuffer text = new StringBuffer();

		text.append("Naive Bayes Classifier");

		// append valid text here.

		return text.toString();
	}
}
