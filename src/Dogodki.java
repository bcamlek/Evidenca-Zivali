import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.Date;

public class Dogodki {
    private JFrame window;
    private Container container;
    private JLabel mainTitle;
    private JTable table;
    private DefaultTableModel model;
    private DB db;

    public Dogodki() {
        try {
            db = DB.getInstance(); // Pridobimo instanco razreda PostgreSQL
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Napaka pri povezavi s podatkovno bazo.", "Napaka", JOptionPane.ERROR_MESSAGE);
        }

        window = new JFrame("Dogodki"); // Ustvarimo novo okno
        window.setPreferredSize(new Dimension(1024, 768)); // Nastavimo velikost okna
        window.setBounds(10, 10, 1024, 768); // Nastavimo pozicijo in velikost okna
        window.setLayout(new BorderLayout()); // Nastavimo postavitev okna
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Nastavimo akcijo ob zaprtju okna
        window.setLocationRelativeTo(null); // Nastavimo pozicijo okna na sredino
        window.setResizable(false); // Omogočimo spreminjanje velikosti okna

        container = window.getContentPane(); // Ustvarimo nov container
        container.setLayout(new BorderLayout()); // Nastavimo postavitev panela

        mainTitle = new JLabel("Dogodki"); // Ustvarimo nov label
        mainTitle.setFont(new Font("Arial", Font.BOLD, 48)); // Nastavimo velikost in obliko pisave
        mainTitle.setAlignmentX(Component.CENTER_ALIGNMENT); // Nastavimo poravnavo
        container.add(mainTitle); // Dodamo label v panel

        model = new DefaultTableModel(); // Ustvarimo nov model
        model.addColumn("ID"); // Dodamo stolpec
        model.addColumn("Tip dogodka"); // Dodamo stolpec
        model.addColumn("Opis dogodka"); // Dodamo stolpec
        model.addColumn("Datum dogodka"); // Dodamo stolpec
        model.addColumn("Posameznik živali"); // Dodamo stolpec
        try {
            String query = "SELECT d.*, (p.ime) AS posameznik_zivali FROM dogodki d, posamezniki_zivali p WHERE d.id_pz = p.id";
            ResultSet rs = db.executeQuery(query); // Izvedemo poizvedbo
            while (rs.next()) { // Gremo čez vse vrstice
                // Dodamo vrstico v model
                model.addRow(new Object[]{
                        rs.getString("id"),
                        rs.getString("tip_dogodka"),
                        rs.getString("opis"),
                        new Date(rs.getDate("datum_dog").getTime()),
                        rs.getString("posameznik_zivali")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Napaka pri pridobivanju podatkov iz baze.", "Napaka", JOptionPane.ERROR_MESSAGE);
        }

        table = new JTable(model); // Ustvarimo novo tabelo
        table.setFont(new Font("Arial", Font.PLAIN, 24)); // Nastavimo pisavo
        table.setRowHeight(30); // Nastavimo visino vrstice
        table.setDefaultEditor(Object.class, null); // Onemogočimo urejanje celic
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Onemogočimo samodejno prilagajanje velikosti

        // Nastavimo preferirane širine stolpcev
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(200);
        columnModel.getColumn(2).setPreferredWidth(400);
        columnModel.getColumn(3).setPreferredWidth(200);
        columnModel.getColumn(4).setPreferredWidth(200);

        // Ustvarimo drsnega okna za tabelo
        JScrollPane scrollPane = new JScrollPane(table);

        // Ustvarimo panel z gumbi
        JPanel buttonsPanel = new JPanel();
        JButton addButton = new JButton("Dodaj nov dogodek");
        JButton editButton = new JButton("Uredi dogodek");
        JButton deleteButton = new JButton("Izbriši dogodek");
        JButton refreshButton = new JButton("Osveži");
        buttonsPanel.add(refreshButton);
        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Osvežimo tabelo
                model.setRowCount(0); // Odstranimo vse vrstice iz tabele
                try {
                    String query = "SELECT d.*, (p.ime) AS posameznik_zivali FROM dogodki d, posamezniki_zivali p WHERE d.id_pz = p.id";
                    ResultSet rs = db.executeQuery(query); // Izvedemo poizvedbo
                    while (rs.next()) { // Gremo čez vse vrstice
                        // Dodamo vrstico v model
                        model.addRow(new Object[]{
                                rs.getString("id"),
                                rs.getString("tip_dogodka"),
                                rs.getString("opis"),
                                new Date(rs.getDate("datum_dog").getTime()),
                                rs.getString("posameznik_zivali")
                        });
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Napaka pri pridobivanju podatkov iz baze.", "Napaka", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Dodamo poslušalce dogodkov za gumb "Dodaj nov dogodek"
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Dodajanje novega dogodka
                // Tukaj bi morali odpreti novo okno za dodajanje dogodka
                DogodkiForm obrazec = new DogodkiForm(0);
                obrazec.show();
            }
        });

        // Dodamo poslušalce dogodkov za gumb "Uredi dogodek"
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Urejanje dogodka
                // Tukaj bi morali odpreti novo okno za urejanje izbranega dogodka
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Pridobimo ID dogodka iz izbrane vrstice
                    String employeeID = model.getValueAt(selectedRow, 0).toString();
                    DogodkiForm obrazec = new DogodkiForm(Integer.parseInt(employeeID));
                    obrazec.show();
                } else {
                    JOptionPane.showMessageDialog(container, "Prosimo, izberite dogodek za urejanje.");
                }
            }
        });

        // Dodamo poslušalce dogodkov za gumb "Izbriši dogodek"
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Brisanje dogodka
                // To bo izbrisalo izbranega dogodka
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Odstranimo izbrano vrstico iz tabele
                    try {
                        String employeeID = model.getValueAt(selectedRow, 0).toString();
                        String query = "SELECT delete_dogodki(" + employeeID + ");";
                        db.executeQuery(query);
                        model.removeRow(selectedRow);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Napaka pri brisanju dogodka.", "Napaka", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(container, "Prosimo, izberite dogodek za brisanje.");
                }
            }
        });

        container.add(scrollPane, BorderLayout.CENTER); // Dodamo tabelo v panel
        container.add(buttonsPanel, BorderLayout.SOUTH); // Dodamo panel z gumbi v panel
    }

    public void show() {
        window.setVisible(true); // Naredimo okno vidno
    }
}
