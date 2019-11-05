package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class Panel extends JFrame implements KeyListener {

    private JPanel panel1;
    private JButton uploadButton;
    private JButton saveButton;
    private JTable table1;
    private JButton buttonExportDB;
    private JButton buttonImportBD;
    private Connection conn = null;
    private Statement st;
    private ResultSet rs;

    public Panel() {
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
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent evt) {
        System.out.println("enter");
    }

    @Override
    public void keyReleased(KeyEvent e) {

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
                        "Ram", "Pojemność", "Dysk", "Grafika", "Pamięć", "System"
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

    private void importBD(ActionEvent evt) {

        connectionDB();
        try {
            st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String query = "select count(*) from `computers`";
            rs = st.executeQuery(query);
            rs.next();

            int rows = rs.getInt(1); //pobranie i wykonanie zapytania wprowadzonego przez użytkownika

            query = "select producent, cale, wymiary, matryca, klawiatura, procesor, rdzen, taktowanie, ram, pojemnosc, dysk, grafika, pamiec, system from computers";
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
                    "Ram", "Pojemność", "Dysk", "Grafika", "Pamięć", "System"
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


            String query = " insert into computers (`producent`, `cale`, `wymiary`, `matryca`, `klawiatura`, `procesor`, `rdzen`, `taktowanie`, `ram`, `pojemnosc`, `dysk`, `grafika`, `pamiec`, `system`)"
                    + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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

    private void buttonUpload(ActionEvent evt) {

        JFileChooser chooser = new JFileChooser("C:Users/Olcia/Documents/IdeaProjects/table");
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: "
                    + chooser.getSelectedFile().getName());
        }
        String path = chooser.getSelectedFile().getAbsolutePath();
        // readTxt(path);
        updateTable(path);
    }

    private void updateTable(String path) {

        try (FileReader reader = new FileReader(path);
             BufferedReader br = new BufferedReader(reader)) {
            String line;

            int j = 0;
            while ((line = br.readLine()) != null) {

                String[] tab = line.split(";", 14);

                int i = 0;
                for (String a : tab) {

                    if (i == 15) {
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
