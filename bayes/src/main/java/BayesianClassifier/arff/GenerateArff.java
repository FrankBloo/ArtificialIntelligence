package BayesianClassifier.arff;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class GenerateArff {

	private HashMap<String, HashMap<String, HashMap<String, Integer>>> dataSet;

	
	/**
	 * Returns the dataset from the file.
	 * @param filename The input filename from which the Arff file must be generated.
	 */
	public HashMap<String, HashMap<String, HashMap<String, Integer>>> Start(String filename){
		try{
			File file = new File(filename);
			if(file.exists()) {
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				this.dataSet = (HashMap<String, HashMap<String, HashMap<String, Integer>>>) ois.readObject();
			}else{
				System.out.println("File not found!");
			}
		}catch(ClassNotFoundException | IOException | ClassCastException e){
			System.out.println("Crash. Error: "+e.getClass().getName()+": "+e.getLocalizedMessage());
			System.exit(1);
		}
		return dataSet;
	}

	public void genereateArff(HashMap<String, HashMap<String, HashMap<String, Integer>>> dataSet) {
		ArrayList<Attribute> atts = new ArrayList<Attribute>();
		List<Instance> instances = new ArrayList<Instance>();
		int numDimensions;
		for(int dim = 0; dim < numDimensions; dim++)
		{
		    Attribute current = new Attribute("Attribute" + dim, dim);
		    if(dim == 0)
		    {
		        for(int obj = 0; obj < numInstances; obj++)
		        {
		            instances.add(new SparseInstance(numDimensions));
		        }
		    }

		    for(int obj = 0; obj < numInstances; obj++)
		    {
		        instances.get(obj).setValue(current, data[dim][obj]);
		    }

		    atts.add(current);
		}

		Instances newDataset = new Instances("Dataset", atts, instances.size());

		for(Instance inst : instances)
		    newDataset.add(inst);
	}

	//Map<String classifier, Map<String attribute, Map<String value, Integer numOccurrence>>>
	//HashMap<String, HashMap<String, HashMap<String, Integer>>>
	
	
}
