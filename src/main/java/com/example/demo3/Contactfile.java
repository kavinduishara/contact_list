package com.example.demo3;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Contactfile {

    private static final String FILE_PATH = "contact.xml";
    private static final File XML_FILE = new File(FILE_PATH);

    public static void write(String firstName, String middleName, String lastName, String number) {
        try {
            Document doc = start();

            Element rootElement = doc.getDocumentElement();
            // Add a contact
            Element contact = doc.createElement("Contact");
            rootElement.appendChild(contact);

            // Add name elements
            addElementWithValue(doc, contact, "fName", firstName);
            addElementWithValue(doc, contact, "mName", middleName);
            addElementWithValue(doc, contact, "lName", lastName);
            addElementWithValue(doc, contact, "phoneNumber", number);

            saveFile(doc);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addElementWithValue(Document doc, Element parentElement, String elementName, String value) {
        Element element = doc.createElement(elementName);
        element.appendChild(doc.createTextNode(value));
        parentElement.appendChild(element);
    }

    private static Document start() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(XML_FILE);
        doc.getDocumentElement().normalize();

        return doc;
    }

    private static void saveFile(Document doc) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","0"); // Adjust the indent amount here
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(XML_FILE);
        transformer.transform(source, result);
    }

    public static Map<String, String> read() {
        Map<String, String> numberlist = new TreeMap<>();
        try {
            Document doc = start();
            NodeList nList = doc.getElementsByTagName("Contact");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    String fname = eElement.getElementsByTagName("fName").item(0).getTextContent();
                    String mname = eElement.getElementsByTagName("mName").item(0).getTextContent();
                    String lname = eElement.getElementsByTagName("lName").item(0).getTextContent();
                    String phoneNumber = eElement.getElementsByTagName("phoneNumber").item(0).getTextContent();

                    String name = fname + " " + mname + " " + lname;
                    numberlist.put(name, phoneNumber);
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return numberlist;
    }

    public static void deleteNode(String nodeValue) {
        try {
            Document doc = start();
            NodeList nodeList = doc.getElementsByTagName("phoneNumber");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getTextContent().equals(nodeValue)) {
                    node.getParentNode().getParentNode().removeChild(node.getParentNode());
                }
            }
            saveFile(doc);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void editNode(String phonenumber,String fname,String mname,String lname,String newphonenumber) {
        try {
            Document doc = start();
            NodeList nodeList = doc.getElementsByTagName("phoneNumber");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getTextContent().equals(phonenumber)) {

                   NodeList child= node.getParentNode().getChildNodes();
                   child.item(1).setTextContent(fname);
                   child.item(3).setTextContent(mname);
                   child.item(5).setTextContent(lname);
                   child.item(7).setTextContent(newphonenumber);
                }
            }
            saveFile(doc);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Example usage:
//        write("oo", "Ishara", "Rathnayaka", "0765198733");
//        Map<String, String> contacts = read();
//        contacts.forEach((name, phone) -> System.out.println(name + ": " + phone));
//        deleteNode("078519834");
        editNode("0785198734","k","i","r","0785198739");
    }
}
