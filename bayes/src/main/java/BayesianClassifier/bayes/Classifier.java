package BayesianClassifier.bayes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

public class Classifier {
	public Map<Integer, String> dataSet;
	
	public void Start () {
		try {
			File file = new File("classifications.bys");
			if(file.exists()) {
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				try {
					dataSet = (Map<Integer, String>) ois.readObject();
				} catch (ClassCastException ex) {
					System.err.println("Can not read classifications.bys!");
				}
			}
			else {
				//CREATE A NEW DATASET!!!
			}
		}
		catch (ClassNotFoundException | IOException e) {
			
		}
	}
	
	public void Exit () {
		try {
			File file = new File("classifications.bys");
			if(file.exists()) {
				file.delete();
			}
			FileOutputStream fis = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fis);
			oos.writeObject(dataSet);
			oos.flush();
		}
		catch (IOException e) {
			
		}
	}
}
