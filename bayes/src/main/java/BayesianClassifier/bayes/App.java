package BayesianClassifier.bayes;

import BayesianClassifier.bayes.Generic.ClassifierWrapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Hello world!
 */
public class App {
    public static BayesClassifier[] classifiers;

    public static void main(String[] args) {
        try {
            // Read settings from a XML File
            File file = new File("settings.xml");
            ArrayList<ClassifierWrapper> classifierList = new ArrayList<>();
            if (!file.exists()) {
                System.out.println("The settings file could not be found!");
                System.exit(1);
            }
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            try {
                Document doc = dBuilder.parse(file);
                NodeList dataNodes = doc.getElementsByTagName("dataset");
                for (int i = 0; i < dataNodes.getLength(); i++) {
                    Node dataNode = dataNodes.item(i);
                    if (dataNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element dataElem = (Element) dataNode;
                        classifierList.add(
                                i,
                                new ClassifierWrapper(dataElem
                                        .getAttribute("name")));
                        classifierList.get(i).setFileName(dataElem
                                .getAttribute("classifierFile"));
                        classifierList.get(i).setTrainFolder(dataElem
                                .getAttribute("trainFolder"), dataElem
                                .getAttribute("trainFormat"));
                    }
                }
            } catch (SAXException | IOException e) {
                System.err.println("Something went wrong reading the settings.xml file");
                System.exit(1);
            }

            //Try outputting the data to the Classifier now

			/*classifiers = new BayesClassifier[2];
			classifiers[0] = new BayesClassifier("M");
			classifiers[1] = new BayesClassifier("F");

			if (classifiers[0].getTrainedStatus()) {

			}*/
        } catch (ParserConfigurationException e) {
            System.out.println(e.getMessage());
        }
    }
}
