import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.Instant;

public class PosameznikiForm {

    private JFrame window;
    private Container container;
    private JLabel mainTitle;
    private JLabel imeLabel;
    private JTextField imeField;
    private JLabel datumRojstvaLabel;
    private JTextField datumRojstvaField;

    private JLabel datumSmrtiLabel;
    private JTextField datumSmrtiField;
    private JLabel zivalskaVrstaIdLabel;
    private JComboBox<ZivalskaVrstaItem> zivalskaVrstaIdComboBox;
    private JLabel krajIdLabel;
    private JComboBox<KrajItem> krajIdComboBox;
    private JLabel uporanbikIdLabel;
    private JComboBox<UporabnikItem> uporabnikIdComboBox;
    private JButton shraniButton;
    private int posameznikZivaliId;
    private DB db;

    public PosameznikiForm(int posameznikZivaliId) {
        this.posameznikZivaliId = posameznikZivaliId;

        try {
            db = DB.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Napaka pri povezavi s podatkovno bazo.", "Napaka", JOptionPane.ERROR_MESSAGE);
        }

        window = new JFrame("Posamezniki Živali Obrazec"); // Ustvarimo novo okno
        window.setPreferredSize(new Dimension(1024, 768)); // Nastavimo velikost okna
        window.setBounds(10, 10, 1024, 768); // Nastavimo pozicijo in velikost okna
        window.setLayout(null); // Nastavimo postavitev okna
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Nastavimo akcijo ob zaprtju okna
        window.setLocationRelativeTo(null); // Nastavimo pozicijo okna na sredino
        window.setResizable(false); // Omogočimo spreminjanje velikosti okna

        container = window.getContentPane(); // Ustvarimo nov container

        mainTitle = new JLabel("Posamezniki Živali Obrazec"); // Ustvarimo nov label
        mainTitle.setFont(new Font("Arial", Font.BOLD, 48)); // Nastavimo velikost in obliko pisave
        mainTitle.setBounds(10, 50, 1004, 50); // Nastavimo pozicijo in velikost
        container.add(mainTitle); // Dodamo label v container

        imeLabel = new JLabel("Ime posameznika živali:"); // Ustvarimo nov label
        imeLabel.setBounds(10, 150, 200, 40); // Nastavimo pozicijo in velikost
        container.add(imeLabel); // Dodamo label v container

        imeField = new JTextField(); // Ustvarimo nov textfield
        imeField.setBounds(220, 150, 200, 40); // Nastavimo pozicijo in velikost
        container.add(imeField); // Dodamo textfield v container

        datumRojstvaLabel = new JLabel("Datum rojstva:"); // Ustvarimo nov label
        datumRojstvaLabel.setBounds(10, 200, 200, 40); // Nastavimo pozicijo in velikost
        container.add(datumRojstvaLabel); // Dodamo label v container

        datumRojstvaField = new JTextField(); // Ustvarimo nov textfield
        datumRojstvaField.setBounds(220, 200, 200, 40); // Nastavimo pozicijo in velikost
        container.add(datumRojstvaField); // Dodamo textfield v container

        datumSmrtiLabel = new JLabel("Datum smrti:"); // Ustvarimo nov label
        datumSmrtiLabel.setBounds(10, 250, 200, 40); // Nastavimo pozicijo in velikost
        container.add(datumSmrtiLabel); // Dodamo label v container

        datumSmrtiField = new JTextField(); // Ustvarimo nov textfield
        datumSmrtiField.setBounds(220, 250, 200, 40); // Nastavimo pozicijo in velikost
        container.add(datumSmrtiField); // Dodamo textfield v container

        zivalskaVrstaIdLabel = new JLabel("Živalska vrsta:"); // Ustvarimo nov label
        zivalskaVrstaIdLabel.setBounds(10, 300, 200, 40); // Nastavimo pozicijo in velikost
        container.add(zivalskaVrstaIdLabel); // Dodamo label v container

        zivalskaVrstaIdComboBox = new JComboBox<ZivalskaVrstaItem>(); // Ustvarimo nov izbirni seznam
        zivalskaVrstaIdComboBox.setBounds(220, 300, 200, 40); // Nastavimo pozicijo in velikost
        try {
            String query = "SELECT * FROM zivalska_vrsta";
            ResultSet rs = db.executeQuery(query); // Izvedemo poizvedbo
            while (rs.next()) { // Gremo čez vse vrstice
                // Dodamo vrstico v model
                zivalskaVrstaIdComboBox.addItem(new ZivalskaVrstaItem(rs.getInt("id"), rs.getString("ime")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Napaka pri branju podatkov iz podatkovne baze.", "Napaka", JOptionPane.ERROR_MESSAGE);
        }
        container.add(zivalskaVrstaIdComboBox); // Dodamo izbirni seznam v container

        krajIdLabel = new JLabel("Kraj:"); // Ustvarimo nov label
        krajIdLabel.setBounds(10, 350, 200, 40); // Nastavimo pozicijo in velikost
        container.add(krajIdLabel); // Dodamo label v container

        krajIdComboBox = new JComboBox<KrajItem>(); // Ustvarimo nov izbirni seznam
        krajIdComboBox.setBounds(220, 350, 200, 40); // Nastavimo pozicijo in velikost
        try {
            String query = "SELECT * FROM \"Kraji\"";
            ResultSet rs = db.executeQuery(query); // Izvedemo poizvedbo
            while (rs.next()) { // Gremo čez vse vrstice
                // Dodamo vrstico v model
                krajIdComboBox.addItem(new KrajItem(rs.getInt("id"), rs.getString("ime"), rs.getString("postna_st")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Napaka pri branju podatkov iz podatkovne baze.", "Napaka", JOptionPane.ERROR_MESSAGE);
        }
        container.add(krajIdComboBox); // Dodamo izbirni seznam v container

        uporanbikIdLabel = new JLabel("Uporabnik:"); // Ustvarimo nov label
        uporanbikIdLabel.setBounds(10, 400, 200, 40); // Nastavimo pozicijo in velikost
        container.add(uporanbikIdLabel); // Dodamo label v container

        uporabnikIdComboBox = new JComboBox<UporabnikItem>(); // Ustvarimo nov izbirni seznam
        uporabnikIdComboBox.setBounds(220, 400, 200, 40); // Nastavimo pozicijo in velikost
        try {
            String query = "SELECT * FROM userji";
            ResultSet rs = db.executeQuery(query); // Izvedemo poizvedbo
            while (rs.next()) { // Gremo čez vse vrstice
                // Dodamo vrstico v model
                uporabnikIdComboBox.addItem(new UporabnikItem(rs.getInt("id"), rs.getString("ime"), rs.getString("primek"), rs.getString("uporabniskoime")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Napaka pri branju podatkov iz podatkovne baze.", "Napaka", JOptionPane.ERROR_MESSAGE);
        }
        container.add(uporabnikIdComboBox); // Dodamo izbirni seznam v container

        shraniButton = new JButton("Shrani"); // Ustvarimo nov gumb
        shraniButton.setBounds(10, 450, 100, 40); // Nastavimo pozicijo in velikost
        shraniButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                shraniPosameznika();
            }
        });
        container.add(shraniButton); // Dodamo gumb v container

        window.setVisible(true); // Naredimo okno vidno

        if (posameznikZivaliId > 0) {
            try {
                String query = "SELECT * FROM posamezniki_zivali WHERE id = " + posameznikZivaliId;
                ResultSet rs = db.executeQuery(query); // Izvedemo poizvedbo
                if (rs.next()) {
                    imeField.setText(rs.getString("ime"));
                    datumRojstvaField.setText(rs.getString("datum_roj"));
                    datumSmrtiField.setText(rs.getString("datum_smrt"));
                    for (int i = 0; i < zivalskaVrstaIdComboBox.getItemCount(); i++) {
                        if (zivalskaVrstaIdComboBox.getItemAt(i).id == rs.getInt("id_zv")) {
                            zivalskaVrstaIdComboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                    for (int i = 0; i < krajIdComboBox.getItemCount(); i++) {
                        if (krajIdComboBox.getItemAt(i).id == rs.getInt("id_k")) {
                            krajIdComboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                    for (int i = 0; i < uporabnikIdComboBox.getItemCount(); i++) {
                        if (uporabnikIdComboBox.getItemAt(i).id == rs.getInt("id_u")) {
                            uporabnikIdComboBox.setSelectedIndex(i);
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

    private void shraniPosameznika() {
        String ime = imeField.getText();
        String datumRojstva = datumRojstvaField.getText();
        String datumSmrti = datumSmrtiField.getText();
        String zivalskaVrstaId = zivalskaVrstaIdComboBox.getItemAt(zivalskaVrstaIdComboBox.getSelectedIndex()).id + "";
        String krajId = krajIdComboBox.getItemAt(krajIdComboBox.getSelectedIndex()).id + "";
        String uporabnikId = uporabnikIdComboBox.getItemAt(uporabnikIdComboBox.getSelectedIndex()).id + "";

        Instant datumRojstvaTimestamp;
        Instant datumSmrtiTimestamp = Instant.EPOCH;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(datumRojstva);
            datumRojstvaTimestamp = Instant.ofEpochMilli(date.getTime());
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Datum rojstva ni pravilno oblikovan.", "Napaka", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!datumSmrti.isEmpty()) {
            try {
                Date date = sdf.parse(datumSmrti);
                datumSmrtiTimestamp = Instant.ofEpochMilli(date.getTime());
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Datum smrti ni pravilno oblikovan.", "Napaka", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (ime.isEmpty() || datumRojstva.isEmpty() || zivalskaVrstaId.isEmpty() || krajId.isEmpty() || uporabnikId.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ime, datum rojstva, živalska vrsta, kraj in uporabnik ne smejo biti prazni.", "Napaka", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (posameznikZivaliId > 0) {
                String query = "SELECT update_posamezniki_zivali(" + posameznikZivaliId + ", '" + ime + "', '" + datumRojstvaTimestamp + "', '" + datumSmrtiTimestamp + "', " + zivalskaVrstaId + ", " + krajId + ", " + uporabnikId + ")";
                db.executeQuery(query);
                JOptionPane.showMessageDialog(null, "Posameznik živali uspešno posodobljen.", "Obvestilo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                String query = "SELECT insert_posamezniki_zivali('" + ime + "', '" + datumRojstvaTimestamp + "', '" + datumSmrtiTimestamp  + "', " + zivalskaVrstaId + ", " + krajId + ", " + uporabnikId + ")";
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

    private class ZivalskaVrstaItem {
        public int id;
        public String ime;

        public ZivalskaVrstaItem(int id, String ime) {
            this.id = id;
            this.ime = ime;
        }

        public String toString() {
            return this.ime;
        }
    }

    private class KrajItem {
        public int id;
        public String ime;
        public String postnaStevilka;

        public KrajItem(int id, String ime, String postnaStevilka) {
            this.id = id;
            this.ime = ime;
            this.postnaStevilka = postnaStevilka;
        }

        public String toString() {
            return this.ime + " (" + this.postnaStevilka + ")";
        }
    }

    private class UporabnikItem {
        public int id;
        public String ime;
        public String priimek;
        public String uporabniskoIme;
        public UporabnikItem(int id, String ime, String priimek, String uporabniskoIme) {
            this.id = id;
            this.ime = ime;
            this.priimek = priimek;
            this.uporabniskoIme = uporabniskoIme;
        }

        public String toString() {
            return this.ime + " " + this.priimek + " (" + this.uporabniskoIme + ")";
        }
    }
}
