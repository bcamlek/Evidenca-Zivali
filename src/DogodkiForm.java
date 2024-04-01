import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.Instant;

public class DogodkiForm {

    private JFrame window;
    private Container container;
    private JLabel mainTitle;
    private JLabel tipDogodkaLabel;
    private JTextField tipDogodkaField;
    private JLabel opisLabel;
    private JTextArea opisArea;
    private JLabel datumDogodkaLabel;
    private JTextField datumDogodkaField;
    private JLabel posameznikZivaliIdLabel;
    private JComboBox<PosameznikZivaliItem> posameznikZivaliIdComboBox;
    private JButton shraniButton;
    private int dogodekId;
    private DB db;

    public DogodkiForm(int dogodekId) {
        this.dogodekId = dogodekId;

        try {
            db = DB.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Napaka pri povezavi s podatkovno bazo.", "Napaka", JOptionPane.ERROR_MESSAGE);
        }

        window = new JFrame("Dogodki Obrazec"); // Ustvarimo novo okno
        window.setPreferredSize(new Dimension(1024, 768)); // Nastavimo velikost okna
        window.setBounds(10, 10, 1024, 768); // Nastavimo pozicijo in velikost okna
        window.setLayout(null); // Nastavimo postavitev okna
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Nastavimo akcijo ob zaprtju okna
        window.setLocationRelativeTo(null); // Nastavimo pozicijo okna na sredino
        window.setResizable(false); // Omogočimo spreminjanje velikosti okna

        container = window.getContentPane(); // Ustvarimo nov container

        mainTitle = new JLabel("Dogodki Obrazec"); // Ustvarimo nov label
        mainTitle.setFont(new Font("Arial", Font.BOLD, 48)); // Nastavimo velikost in obliko pisave
        mainTitle.setBounds(10, 50, 1004, 50); // Nastavimo pozicijo in velikost
        container.add(mainTitle); // Dodamo label v container

        tipDogodkaLabel = new JLabel("Tip dogodka:"); // Ustvarimo nov label
        tipDogodkaLabel.setBounds(10, 150, 200, 40); // Nastavimo pozicijo in velikost
        container.add(tipDogodkaLabel); // Dodamo label v container

        tipDogodkaField = new JTextField(); // Ustvarimo nov textfield
        tipDogodkaField.setBounds(220, 150, 200, 40); // Nastavimo pozicijo in velikost
        container.add(tipDogodkaField); // Dodamo textfield v container

        opisLabel = new JLabel("Opis dogodka:"); // Ustvarimo nov label
        opisLabel.setBounds(10, 200, 200, 40); // Nastavimo pozicijo in velikost
        container.add(opisLabel); // Dodamo label v container

        opisArea = new JTextArea(); // Ustvarimo novo text area
        opisArea.setBounds(220, 200, 600, 200); // Nastavimo pozicijo in velikost
        container.add(opisArea); // Dodamo text area v container

        datumDogodkaLabel = new JLabel("Datum dogodka:"); // Ustvarimo nov label
        datumDogodkaLabel.setBounds(10, 400, 200, 40); // Nastavimo pozicijo in velikost
        container.add(datumDogodkaLabel); // Dodamo label v container

        datumDogodkaField = new JTextField(); // Ustvarimo nov textfield
        datumDogodkaField.setBounds(220, 400, 200, 40); // Nastavimo pozicijo in velikost
        container.add(datumDogodkaField); // Dodamo textfield v container

        posameznikZivaliIdLabel = new JLabel("Posameznik živali:"); // Ustvarimo nov label
        posameznikZivaliIdLabel.setBounds(10, 450, 200, 40); // Nastavimo pozicijo in velikost
        container.add(posameznikZivaliIdLabel); // Dodamo label v container

        posameznikZivaliIdComboBox = new JComboBox<PosameznikZivaliItem>(); // Ustvarimo nov izbirni seznam
        posameznikZivaliIdComboBox.setBounds(220, 450, 200, 40); // Nastavimo pozicijo in velikost
        try {
            String query = "SELECT pz.*, (u.ime || ' ' || u.primek) AS uporabniskoime FROM posamezniki_zivali pz, userji u WHERE pz.id_u = u.id";
            ResultSet rs = db.executeQuery(query); // Izvedemo poizvedbo
            while (rs.next()) { // Gremo čez vse vrstice
                // Dodamo vrstico v model
                posameznikZivaliIdComboBox.addItem(new PosameznikZivaliItem(rs.getInt("id"), rs.getString("ime"), rs.getString("uporabniskoime")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Napaka pri branju podatkov iz podatkovne baze.", "Napaka", JOptionPane.ERROR_MESSAGE);
        }
        container.add(posameznikZivaliIdComboBox); // Dodamo izbirni seznam v container

        shraniButton = new JButton("Shrani"); // Ustvarimo nov gumb
        shraniButton.setBounds(10, 500, 100, 40); // Nastavimo pozicijo in velikost
        shraniButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                shraniDogodek();
            }
        });
        container.add(shraniButton); // Dodamo gumb v container

        window.setVisible(true); // Naredimo okno vidno

        if (dogodekId > 0) {
            try {
                String query = "SELECT * FROM dogodki WHERE id = " + dogodekId;
                ResultSet rs = db.executeQuery(query); // Izvedemo poizvedbo
                if (rs.next()) {
                    tipDogodkaField.setText(rs.getString("tip_dogodka"));
                    opisArea.setText(rs.getString("opis"));
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    datumDogodkaField.setText(sdf.format(new Date(rs.getDate("datum_dog").getTime())));

                    for (int i = 0; i < posameznikZivaliIdComboBox.getItemCount(); i++) {
                        if (posameznikZivaliIdComboBox.getItemAt(i).id == rs.getInt("id_pz")) {
                            posameznikZivaliIdComboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Napaka pri branju podatkov iz podatkovne baze.", "Napaka", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void shraniDogodek() {
        String tipDogodka = tipDogodkaField.getText();
        String opis = opisArea.getText();
        String datumDogodka = datumDogodkaField.getText();
        String posameznikZivaliId = posameznikZivaliIdComboBox.getItemAt(posameznikZivaliIdComboBox.getSelectedIndex()).id + "";

        Instant datumDogodkaTimestamp;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(datumDogodka);
            datumDogodkaTimestamp = Instant.ofEpochMilli(date.getTime());
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Datum dogodka ni pravilno oblikovan.", "Napaka", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (tipDogodka.isEmpty() || opis.isEmpty() || datumDogodka.isEmpty() || posameznikZivaliId.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Prosimo, izpolnite vsa polja.", "Nepopolni podatki", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (dogodekId > 0) {
                String query = "SELECT update_dogodki(" + dogodekId + ", '" + tipDogodka + "', '" + opis + "', '" + datumDogodkaTimestamp + "', " + posameznikZivaliId + ")";
                db.executeQuery(query);
                JOptionPane.showMessageDialog(null, "Posameznik živali uspešno posodobljen.", "Obvestilo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                String query = "SELECT insert_dogodki('" + tipDogodka + "', '" + opis + "', '" + datumDogodkaTimestamp + "', " + posameznikZivaliId + ")";
                db.executeQuery(query);
                JOptionPane.showMessageDialog(null, "Posameznik živali uspešno dodan.", "Obvestilo", JOptionPane.INFORMATION_MESSAGE);
            }
            window.dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Napaka pri shranjevanju podatkov v podatkovno bazo.", "Napaka", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void show() {
        window.setVisible(true); // Naredimo okno vidno
    }

    private class PosameznikZivaliItem {
        public int id;
        public String ime;
        public String uporabniskoIme;

        public PosameznikZivaliItem(int id, String ime, String uporabniskoIme) {
            this.id = id;
            this.ime = ime;
            this.uporabniskoIme = uporabniskoIme;
        }

        @Override
        public String toString() {
            return ime + " (" + uporabniskoIme + ")";
        }
    }
}
