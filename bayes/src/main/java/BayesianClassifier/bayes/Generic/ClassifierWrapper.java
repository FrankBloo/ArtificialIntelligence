package BayesianClassifier.bayes.Generic;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

public class ClassifierWrapper {
	private String name;
	private String classifierFileName;
	private String trainFolder;
	private String trainExtension;
	
	public ClassifierWrapper(String name) {
		this.name = name;
	}
	
	public void setFileName (String filename) throws FileExtensionException {
		if(filename.endsWith(".bys"))
			this.classifierFileName = filename;
		else {
			System.out.println("Currently only .byc files are  supported.");
			throw new FileExtensionException();
		}
	}
	
	public String getFileName () {
		return classifierFileName;
	}
	
	public void setTrainFolder (String foldername, String fileExtension) throws FileExtensionException {
		this.trainFolder = foldername;
		this.trainExtension = fileExtension;
		if((StringUtils.countMatches(fileExtension, ".") == 1) && (!fileExtension.matches("[/\\:*?\"<>|]")) &&
				(!fileExtension.endsWith("."))){
			throw new FileExtensionException();
		}
	}
	
	 
	
	
	public class FileExtensionException extends IOException {
		private static final long serialVersionUID = 2722453510154691277L;
	}
}
