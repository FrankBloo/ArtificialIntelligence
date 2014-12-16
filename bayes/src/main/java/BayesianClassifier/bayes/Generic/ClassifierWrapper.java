package BayesianClassifier.bayes.Generic;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class ClassifierWrapper {
    private String classifierFileName;

    public ClassifierWrapper(String name) {
        String name1 = name;
    }

    public void setFileName(String filename) throws FileExtensionException {
        if (filename.endsWith(".bys"))
            this.classifierFileName = filename;
        else {
            System.out.println("Currently only .byc files are  supported.");
            throw new FileExtensionException();
        }
    }

    public String getFileName() {
        return classifierFileName;
    }

    public void setTrainFolder(String folderName, String fileExtension) throws FileExtensionException {
        String trainFolder = folderName;
        String trainExtension = fileExtension;
        if ((StringUtils.countMatches(fileExtension, ".") == 1) && (!fileExtension.matches("[/\\:*?\"<>|]")) &&
                (!fileExtension.endsWith("."))) {
            throw new FileExtensionException();
        }
    }


    public class FileExtensionException extends IOException {
        private static final long serialVersionUID = 2722453510154691277L;
    }
}
