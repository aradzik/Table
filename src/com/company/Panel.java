package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.net.*;
import javax.swing.table.TableModel;
import javax.xml.stream.*;

import javax.xml.parsers.*;    //DocumentBuilder, Factory

import org.w3c.dom.*;          //Document, Element
import org.xml.sax.SAXException;

import javax.xml.transform.*;  //Transformer, Factory
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;


public class Panel extends JFrame {

    public JPanel panel1;
    private JButton uploadButton;
    private JButton saveButton;
    public  JTable table1;
    private JButton buttonExportDB;
    private JButton buttonImportBD;
    private JButton importFormXMLButton;
    private JButton exportToXMLButton;
    private Connection conn = null;
    private Statement st;
    private ResultSet rs;



    public Panel()  {
        initComponents();

        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                buttonUpload(evt);
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                buttonSave(evt);
            }
        });

        buttonImportBD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {

                importBD(evt);

            }
        });
        buttonExportDB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {

               exportDB(evt);

            }
        });
        importFormXMLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {

                try {
                    importXML(evt);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }

            }
        });
        exportToXMLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    exportXML(evt);
                } catch (IOException | TransformerConfigurationException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @SuppressWarnings("unchecked")
    private void initComponents() {

        table1.setModel(new DefaultTableModel(

                new Object[][]{
                        {}, {}, {}, {}, {},
                        {}, {}, {}, {}, {},
                        {}, {}, {}, {}, {},
                        {}, {}, {}, {}, {},
                        {}, {}, {}, {}
                },
                new String[]{
                        "Producent", "Cale", "Wymiary", "Matryca",
                        "Klawiatura", "Procesor", "Rdzeń", "Taktowanie",
                        "Ram", "Pojemność", "Dysk", "Grafika", "Pamięć", "System", "Napęd"
                }
        ));

    }

    private void connectionDB() {

        conn = null;
        try {
            String userName = "root";
            String password = "";
            String url = "jdbc:mysql://localhost/systemsintegration";
            Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance();
            conn = DriverManager.getConnection(url, userName, password);
            // System.out.println("Database connection established");
            //conn.close();
        } catch (Exception e) {
            System.out.print("Do not connect to DB - Error:" + e);
        }
    }

    private void importXML(ActionEvent evt) throws IOException, XMLStreamException{

        try {
            String producent = null;
            String size = null;
            String rozdzielczosc = null;
            String matryca = null;
            String ekranyDotykowy = null;
            String procesor = null;
            String rdzenie = null;
            String taktowanie = null;
            String ram = null;
            String iloscPamieci = null;
            String dysk = null;
            String kartaGraficzna = null;
            String vRam = null;
            String system = null;
            String naped = null;

            File file = new File(selectFile());
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = (Document) builder.parse(file);
            document.getDocumentElement().normalize();

            Element root = document.getDocumentElement();

            NodeList nodelist1 = root.getChildNodes();

            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();

            for (int j = 1; j <= nodelist1.getLength(); j++) {

                XPathExpression expr = xpath.compile("//laptops/laptop[" + j + "]");
                NodeList nl = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

                for (int i = 0; i < nl.getLength(); i++) {
                    Node node = nl.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {

                        Element element = (Element) node;
                        producent = element.getElementsByTagName("manufacturer").item(0).getTextContent();
                        size = element.getElementsByTagName("size").item(0).getTextContent();
                        rozdzielczosc = element.getElementsByTagName("resolution").item(0).getTextContent();
                        matryca = element.getElementsByTagName("type").item(0).getTextContent();
                        ekranyDotykowy = element.getElementsByTagName("touchscreen").item(0).getTextContent();
                        procesor = element.getElementsByTagName("name").item(0).getTextContent();
                        rdzenie = element.getElementsByTagName("physical_cores").item(0).getTextContent();
                        taktowanie = element.getElementsByTagName("clock_speed").item(0).getTextContent();
                        ram = element.getElementsByTagName("ram").item(0).getTextContent();
                        iloscPamieci = element.getElementsByTagName("storage").item(0).getTextContent();
                        dysk = element.getElementsByTagName("type").item(1).getTextContent(); //1
                        kartaGraficzna = element.getElementsByTagName("name").item(1).getTextContent();
                        vRam = element.getElementsByTagName("type").item(2).getTextContent();
                        system = element.getElementsByTagName("os").item(0).getTextContent();
                        naped = element.getElementsByTagName("disc_reader").item(0).getTextContent();

                        table1.setValueAt(producent, j-1, 0);
                        table1.setValueAt(size, j-1, 1);
                        table1.setValueAt(rozdzielczosc, j-1, 2);
                        table1.setValueAt(matryca, j-1, 3);
                        table1.setValueAt(ekranyDotykowy, j-1, 4);
                        table1.setValueAt(procesor, j-1, 5);
                        table1.setValueAt(rdzenie, j-1, 6);
                        table1.setValueAt(taktowanie, j-1, 7);
                        table1.setValueAt(ram, j-1, 8);
                        table1.setValueAt(iloscPamieci, j-1, 9);
                        table1.setValueAt(dysk, j-1, 10);
                        table1.setValueAt(kartaGraficzna, j-1, 11);
                        table1.setValueAt(vRam, j-1, 12);
                        table1.setValueAt(system, j-1, 13);
                        table1.setValueAt(naped, j-1, 14);

                    }
                }
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
}

    private void exportXML(ActionEvent evt) throws IOException, TransformerConfigurationException {
        System.out.println("Export");
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 'T' HH:mm:ss");
        String formatDateTime = localDateTime.format(formatter);
        Transformer t = TransformerFactory.newInstance().newTransformer();//dodaje wcięcia

        try {

            DocumentBuilder builderXML;
            DocumentBuilderFactory fabryka = DocumentBuilderFactory.newInstance();
            builderXML = fabryka.newDocumentBuilder();
            Document dok = builderXML.newDocument();
            Element laptops = dok.createElement("laptops");
            laptops.setAttribute("moddate", formatDateTime);
            for (int i = 0; i < table1.getRowCount(); i++) {
                int j = 0;
                    Element laptop = dok.createElement("laptop");
                    laptop.setAttribute("id", String.valueOf(i));

                    Element manufacturer = dok.createElement("manufacturer");
                    manufacturer.setTextContent((String) table1.getValueAt(i, j));
                    laptop.appendChild(manufacturer);
                    j++;

                    Element screen = dok.createElement("screen");
                    laptop.appendChild(screen);

                    Element size = dok.createElement("size");
                    size.setTextContent((String) table1.getValueAt(i, j));
                    screen.appendChild(size);
                    j++;

                    Element resolution = dok.createElement("resolution");
                    resolution.setTextContent((String) table1.getValueAt(i, j));
                    screen.appendChild(resolution);
                    j++;

                    Element type = dok.createElement("type");
                    type.setTextContent((String) table1.getValueAt(i, j));
                    screen.appendChild(type);
                    j++;

                    Element touchscreen = dok.createElement("touchscreen");
                    touchscreen.setTextContent((String) table1.getValueAt(i, j));
                    screen.appendChild(touchscreen);
                    j++;

                    Element processor = dok.createElement("processor");
                    laptop.appendChild(processor);

                    Element name = dok.createElement("name");
                    name.setTextContent((String) table1.getValueAt(i, j));
                    processor.appendChild(name);
                    j++;

                    Element physical_cores = dok.createElement("physical_cores");
                    physical_cores.setTextContent((String) table1.getValueAt(i, j));
                    processor.appendChild(physical_cores);
                    j++;

                    Element clock_speed = dok.createElement("clock_speed");
                    clock_speed.setTextContent((String) table1.getValueAt(i, j));
                    processor.appendChild(clock_speed);
                    j++;

                    Element ram = dok.createElement("ram");
                    ram.setTextContent((String) table1.getValueAt(i, j));
                    laptop.appendChild(ram);
                    j++;

                    Element disc = dok.createElement("disc");
                    laptop.appendChild(disc);

                    Element storage = dok.createElement("storage");
                    storage.setTextContent((String) table1.getValueAt(i, j));
                    disc.appendChild(storage);
                    j++;

                    Element typeDisc = dok.createElement("type");
                    typeDisc.setTextContent((String) table1.getValueAt(i, j));
                    disc.appendChild(typeDisc);
                    j++;

                    Element graphic_card = dok.createElement("graphic_card");
                    laptop.appendChild(graphic_card);

                    Element nameGraphic = dok.createElement("name");
                    nameGraphic.setTextContent((String) table1.getValueAt(i, j));
                    graphic_card.appendChild(nameGraphic);
                    j++;

                    Element memory = dok.createElement("type");
                    memory.setTextContent((String) table1.getValueAt(i, j));
                    graphic_card.appendChild(memory);
                    j++;

                    Element os = dok.createElement("os");
                    os.setTextContent((String) table1.getValueAt(i, j));
                    laptop.appendChild(os);
                    j++;

                Element disc_reader = dok.createElement("disc_reader");
                disc_reader.setTextContent((String) table1.getValueAt(i, j));
                laptop.appendChild(disc_reader);

                    laptops.appendChild(laptop);
                    if (j >= table1.getColumnCount()) j = 0;

            }
            dok.appendChild(laptops);

            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty(OutputKeys.METHOD, "xml");

            t.transform(new DOMSource(dok), new StreamResult(new FileOutputStream("katalog.xml")));

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }

    private void importBD(ActionEvent evt) {

        connectionDB();
        try {
            st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String query = "select count(*) from `computers`";
            rs = st.executeQuery(query);
            rs.next();

            int rows = rs.getInt(1); //pobranie i wykonanie zapytania wprowadzonego przez użytkownika

            query = "select producent, cale, wymiary, matryca, klawiatura, procesor, rdzen, taktowanie, ram, pojemnosc, dysk, grafika, pamiec, system,drive from computers";
            rs = st.executeQuery(query);

            ResultSetMetaData meta = rs.getMetaData();
            int columns = meta.getColumnCount(); //odczytanie ilości kolumn danych

            Object[][] data = new Object[rows][columns];
            int i = 0;
            while (rs.next()) {

                for (int j = 0; j < columns; j++) {
                    data[i][j] = rs.getString(j + 1);
                }
                i++;
                //utworzenie nowego modelu i dołączenie go do tabelki table1

            }
            DefaultTableModel model = new DefaultTableModel(data, new String[]{
                    "Producent", "Cale", "Wymiary", "Matryca",
                    "Klawiatura", "Procesor", "Rdzeń", "Taktowanie",
                    "Ram", "Pojemność", "Dysk", "Grafika", "Pamięć", "System", "Naped"
            });
            table1.setModel(model);
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void exportDB(ActionEvent evt) {
        connectionDB();

        try {

            int rows = table1.getRowCount();
            int columns = table1.getColumnCount();
            String[][] data = new String[rows][columns];

            //usunięcie rekordów z bd
            st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery("TRUNCATE TABLE computers");


            String query = " insert into computers (`producent`, `cale`, `wymiary`, `matryca`, `klawiatura`, `procesor`, `rdzen`, `taktowanie`, `ram`, `pojemnosc`, `dysk`, `grafika`, `pamiec`, `system`,`drive`)"
                    + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
            for (int i = 0; i < table1.getRowCount(); i++) {
                for (int j = 0; j < table1.getColumnCount(); j++) {
                    // dana = table1.getValueAt(i, j);
                    data[i][j] = (String) table1.getValueAt(i, j);
                }
            }

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            for (int i = 0; i < table1.getRowCount(); i++) {
                for (int j = 0; j < table1.getColumnCount(); j++) {

                    preparedStmt.setString(j + 1, data[i][j]);
                }
                preparedStmt.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void buttonSave(ActionEvent evt) {
        try {
            FileWriter w = new FileWriter("katalog1.txt");

            for (int i = 0; i < table1.getRowCount(); i++) {
                for (int j = 0; j < table1.getColumnCount(); j++) {
                    w.write(table1.getValueAt(i, j) + ";");
                }
                w.write("\n");
            }
            w.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private String selectFile() {
        String path = "";
        JFileChooser chooser = new JFileChooser("C:Users/Olcia/Documents/IdeaProjects/table");
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: "
                    + chooser.getSelectedFile().getName());
        }
        path = chooser.getSelectedFile().getAbsolutePath();
        return path;
    }

    private void buttonUpload(ActionEvent evt) {

        updateTable(selectFile());
    }

    private void updateTable(String path) {

        try (FileReader reader = new FileReader(path);
             BufferedReader br = new BufferedReader(reader)) {
            String line;

            int j = 0;
            while ((line = br.readLine()) != null) {

                String[] tab = line.split(";", 15);

                int i = 0;
                for (String a : tab) {

                    if (i == 16) {
                        i = 0;
                    }
                    table1.setValueAt(a, j, i);
                    i++;
                }
                j++;
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    private void readTxt(String path) {
        try (FileReader reader = new FileReader(path);
             BufferedReader br = new BufferedReader(reader)) {

            String line = "";

            while ((line = br.readLine()) != null) {
                String[] tab = line.split(";");
                for (String a : tab) {

                    System.out.print(" " + a + "\t");
                    if (a.equals("")) {
                        System.out.print("---");
                    }
                }
                System.out.println();
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Panel");
        frame.setContentPane(new Panel().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(900, 530));

        frame.pack();
        frame.setVisible(true);
    }
}
