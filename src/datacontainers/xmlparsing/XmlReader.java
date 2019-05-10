package datacontainers.xmlparsing;//refrence https://www.youtube.com/watch?v=8MJJ7MWX8Qs

import datacontainers.staticcontainers.*;
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
import java.util.List;

public class XmlReader {
    private List<StaticServer> staticServers = new ArrayList<>();

    public List<StaticServer> getStaticServers() {
        return staticServers;
    }


    public XmlReader() {
        try{

            File inputFile = new File("system.xml");//the file must be in the same directory

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("server");//element server holds static server information

            for(int temp = 0; temp < nList.getLength(); temp ++){// for each of the nodes in nlist
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE){ //create a new Static server and add to staticServers
                    Element eElement = (Element) nNode;
                    String type = eElement.getAttribute("type");
                    int limit = Integer.parseInt(eElement.getAttribute("limit"));
                    int bootupTime = Integer.parseInt(eElement.getAttribute("bootupTime"));
                    float hourlyRate = Float.parseFloat(eElement.getAttribute("rate"));
                    int coreCount = Integer.parseInt(eElement.getAttribute("coreCount"));
                    int memory = Integer.parseInt(eElement.getAttribute("memory"));
                    int diskSpace = Integer.parseInt(eElement.getAttribute("disk"));
                    staticServers.add(new StaticServer(type, limit, bootupTime, hourlyRate, coreCount, memory, diskSpace));
                }

            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

}
