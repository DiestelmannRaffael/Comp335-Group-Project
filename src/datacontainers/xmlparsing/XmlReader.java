package datacontainers.xmlparsing;//refrence https://www.youtube.com/watch?v=8MJJ7MWX8Qs

import datacontainers.staticcontainers.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

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

//    public List<StaticJob> getStaticJobs() {
//        return staticJobs;
//    }
//
//    public List<Workload> getWorkload() {
//        return workload;
//    }
//
//    public List<Condition> getConditions() {
//        return conditions;
//    }
//
//    public Termination getTermination() {
//        return termination;
//    }

//    private List<StaticJob> staticJobs = new ArrayList<>();
//    private List<Workload> workload = new ArrayList<>();
//    private List<Condition> conditions = new ArrayList<>();
//    private Termination termination = null;

    public XmlReader() {
//        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        try{

            File inputFile = new File("system.xml");
//            File inputFile = new File("/src/system.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
//            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("server");

            for(int temp = 0; temp < nList.getLength(); temp ++){
                Node nNode = nList.item(temp);
//                System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE){
                    Element eElement = (Element) nNode;
                    String type = eElement.getAttribute("type");
                    int limit = Integer.parseInt(eElement.getAttribute("limit"));
                    int bootupTime = Integer.parseInt(eElement.getAttribute("bootupTime"));
                    float hourlyRate = Float.parseFloat(eElement.getAttribute("rate"));
                    int coreCount = Integer.parseInt(eElement.getAttribute("coreCount"));
                    int memory = Integer.parseInt(eElement.getAttribute("memory"));
                    int diskSpace = Integer.parseInt(eElement.getAttribute("disk"));
                    staticServers.add(new StaticServer(type, limit, bootupTime, hourlyRate, coreCount, memory, diskSpace));
//                    System.out.println(staticServers.get(temp));
                }

            }
//            System.out.println(nList.getLength());
//            DocumentBuilder dbuilder = builderFactory.newDocumentBuilder();
//            //Document document = dbuilder.parse(XMLReader.class.getResourceAsStream("/ds-config1.xml"));
//            Document document = dbuilder.parse(XMLReader.class.getResourceAsStream("/system.xml"));
//            document.normalize();


//            NodeList rootNodes = document.getElementsByTagName("config");
//            Node rootNode = rootNodes.item(0);
//
//            Element rootElement = (Element)rootNode;
//            NodeList  serverNodeList = rootElement.getElementsByTagName("server");
//            for(int i = 0; i < serverNodeList.getLength(); i++){
//                Node  currentNode = serverNodeList.item(i);
//                Element serverElement = (Element) currentNode;
//
//                String type = serverElement.getAttribute("type");
//                int limit = Integer.parseInt(serverElement.getAttribute("limit"));
//                int bootupTime = Integer.parseInt(serverElement.getAttribute("bootupTime"));
//                float hourlyRate = Float.parseFloat(serverElement.getAttribute("hourlyRate"));
//                int coreCount = Integer.parseInt(serverElement.getAttribute("coreCount"));
//                int memory = Integer.parseInt(serverElement.getAttribute("memory"));
//                int diskSpace = Integer.parseInt(serverElement.getAttribute("disk"));
//
//               staticServers.add(new StaticServer(type, limit, bootupTime, hourlyRate, coreCount, memory, diskSpace));
//                System.out.println(staticServers.get(i));
//
//            }
//
//            NodeList jobNodeList = rootElement.getElementsByTagName("job");
//
//            for(int i = 0; i < jobNodeList.getLength(); i++) {
//                Node currentNode = jobNodeList.item(i);
//                Element jobElement = (Element) currentNode;
//
//                String type = jobElement.getAttribute("type");
//                int minRunTime = Integer.parseInt(jobElement.getAttribute("minRunTime"));
//                int maxRunTime = Integer.parseInt(jobElement.getAttribute("maxRunTime"));
//                int populationRate = Integer.parseInt(jobElement.getAttribute("populationRate"));
//
//                staticJobs.add(new StaticJob(type, minRunTime, maxRunTime, populationRate));
//                System.out.println(staticJobs.get(i));
//
//            }
//
//            NodeList workloadNodeList = rootElement.getElementsByTagName("workload");
//            Node currentNode = workloadNodeList.item(0);
//            Element workloadElement = (Element) currentNode;
//            String type = workloadElement.getAttribute("type");
//            int minLoad = Integer.parseInt(workloadElement.getAttribute("minLoad"));
//            int maxLoad = Integer.parseInt(workloadElement.getAttribute("maxLoad"));
//
//            workload.add(new Workload(type, minLoad, maxLoad));
//            System.out.println(workload.get(0));
//
//            NodeList conditionNodeList = rootElement.getElementsByTagName("condition");
//            for(int i = 0; i < conditionNodeList.getLength(); i++) {
//                currentNode = conditionNodeList.item(i);
//                Element conditionElement = (Element) currentNode;
//
//                type = conditionElement.getAttribute("type");
//                int value = Integer.parseInt(conditionElement.getAttribute("value"));
//
//                conditions.add(new Condition(type, value));
//                System.out.println(conditions.get(i));
//            }
//
//            termination = new Termination(conditions);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

}
