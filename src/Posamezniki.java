import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.Date;

public class Posamezniki {
    private JFrame window;
    private Container container;
    private JLabel mainTitle;
    private JTable table;
    private DefaultTableModel model;
    private DB db;

    public Posamezniki() {
        try {
            db = DB.getInstance(); // Pridobimo instanco razreda PostgreSQL
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Napaka pri povezavi s podatkovno bazo.", "Napaka", JOptionPane.ERROR_MESSAGE);
        }

        window = new JFrame("Posamezniki Živali"); // Ustvarimo novo okno
        window.setPreferredSize(new Dimension(1024, 768)); // Nastavimo velikost okna
        window.setBounds(10, 10, 1024, 768); // Nastavimo pozicijo in velikost okna
        window.setLayout(new BorderLayout()); // Nastavimo postavitev okna
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Nastavimo akcijo ob zaprtju okna
        window.setLocationRelativeTo(null); // Nastavimo pozicijo okna na sredino
        window.setResizable(false); // Omogočimo spreminjanje velikosti okna

        container = window.getContentPane(); // Ustvarimo nov container
        container.setLayout(new BorderLayout()); // Nastavimo postavitev panela

        mainTitle = new JLabel("Posamezniki Živali"); // Ustvarimo nov label
        mainTitle.setFont(new Font("Arial", Font.BOLD, 48)); // Nastavimo velikost in obliko pisave
        mainTitle.setAlignmentX(Component.CENTER_ALIGNMENT); // Nastavimo poravnavo
        container.add(mainTitle); // Dodamo label v panel

        model = new DefaultTableModel(); // Ustvarimo nov model
        model.addColumn("ID"); // Dodamo stolpec
        model.addColumn("Ime"); // Dodamo stolpec
        model.addColumn("Datum rojstva"); // Dodamo stolpec
        model.addColumn("Datum smrti"); // Dodamo stolpec
        model.addColumn("Živalska vrsta"); // Dodamo stolpec
        model.addColumn("Kraj"); // Dodamo stolpec
        model.addColumn("Uporabnik"); // Dodamo stolpec

        try {
            String query = "SELECT p.*, (zv.ime) AS zivalska_vrsta, (k.ime) AS kraj, (u.ime || ' ' || u.primek) AS uporabnik FROM posamezniki_zivali p, zivalska_vrsta zv, \"Kraji\" k, userji u WHERE p.id_zv = zv.id AND p.id_k = k.id AND p.id_u = u.id";
            ResultSet rs = db.executeQuery(query); // Izvedemo poizvedbo
            while (rs.next()) { // Gremo čez vse vrstice
                // Dodamo vrstico v model
                model.addRow(new Object[]{
                        rs.getString("id"),
                        rs.getString("ime"),
                        new Date(rs.getDate("datum_roj").getTime()),
                        rs.getDate("datum_smrt").getTime() == -3600000 ? "" : new Date(rs.getDate("datum_smrt").getTime()),
                        rs.getString("zivalska_vrsta"),
                        rs.getString("kraj"),
                        rs.getString("uporabnik")
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
        columnModel.getColumn(2).setPreferredWidth(200);
        columnModel.getColumn(3).setPreferredWidth(200);
        columnModel.getColumn(4).setPreferredWidth(200);
        columnModel.getColumn(5).setPreferredWidth(200);
        columnModel.getColumn(6).setPreferredWidth(200);

        // Ustvarimo drsnega okna za tabelo
        JScrollPane scrollPane = new JScrollPane(table);

        // Ustvarimo panel z gumbi
        JPanel buttonsPanel = new JPanel();
        JButton addButton = new JButton("Dodaj novega posameznika živali");
        JButton editButton = new JButton("Uredi posameznika živali");
        JButton deleteButton = new JButton("Izbriši posameznika živali");
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
                    String query = "SELECT p.*, (zv.ime) AS zivalska_vrsta, (k.ime) AS kraj, (u.ime || ' ' || u.primek) AS uporabnik FROM posamezniki_zivali p, zivalska_vrsta zv, \"Kraji\" k, userji u WHERE p.id_zv = zv.id AND p.id_k = k.id AND p.id_u = u.id";
                    ResultSet rs = db.executeQuery(query); // Izvedemo poizvedbo
                    while (rs.next()) { // Gremo čez vse vrstice
                        // Dodamo vrstico v model
                        System.out.println(rs.getDate("datum_smrt").getTime());
                        model.addRow(new Object[]{
                                rs.getString("id"),
                                rs.getString("ime"),
                                new Date(rs.getDate("datum_roj").getTime()),
                                rs.getDate("datum_smrt").getTime() == -3600000 ? "" : new Date(rs.getDate("datum_smrt").getTime()),
                                rs.getString("zivalska_vrsta"),
                                rs.getString("kraj"),
                                rs.getString("uporabnik")
                        });
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Napaka pri pridobivanju podatkov iz baze.", "Napaka", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Dodamo poslušalce dogodkov za gumb "Dodaj novega posameznika živali"
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Dodajanje noveega posameznika živali
                // Tukaj bi morali odpreti novo okno za dodajanje posameznika živali
                PosameznikiForm obrazec = new PosameznikiForm(0);
                obrazec.show();
            }
        });

        // Dodamo poslušalce dogodkov za gumb "Uredi posameznika živali"
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Urejanje posameznika živali
                // Tukaj bi morali odpreti novo okno za urejanje izbranega posameznika živali
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Pridobimo ID posameznika živali iz izbrane vrstice
                    String employeeID = model.getValueAt(selectedRow, 0).toString();
                    PosameznikiForm obrazec = new PosameznikiForm(Integer.parseInt(employeeID));
                    obrazec.show();
                } else {
                    JOptionPane.showMessageDialog(container, "Prosimo, izberite posameznika živali za urejanje.");
                }
            }
        });

        // Dodamo poslušalce dogodkov za gumb "Izbriši posameznika živali"
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Brisanje posameznika živali
                // To bo izbrisalo izbranega posameznika živali
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Odstranimo izbrano vrstico iz tabele
                    try {
                        String employeeID = model.getValueAt(selectedRow, 0).toString();
                        String query = "SELECT delete_posamezniki_zivali(" + employeeID + ");";
                        db.executeQuery(query);
                        model.removeRow(selectedRow);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Napaka pri brisanju posameznika živali.", "Napaka", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(container, "Prosimo, izberite posameznika živali za brisanje.");
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
