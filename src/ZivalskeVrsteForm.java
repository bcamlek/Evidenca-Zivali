import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ZivalskeVrsteForm {

    private JFrame window;
    private Container container;
    private JLabel mainTitle;
    private JLabel imeLabel;
    private JTextField imeField;
    private JLabel opisLabel;
    private JTextArea opisArea;
    private JLabel naravniHabitatLabel;
    private JTextField naravniHabitatField;
    private JButton shraniButton;
    private int zivalskaVrstaId;

    public ZivalskeVrsteForm(int zivalskaVrstaId) {
        this.zivalskaVrstaId = zivalskaVrstaId;

        window = new JFrame("Živalske Vrste Obrazec"); // Ustvarimo novo okno
        window.setPreferredSize(new Dimension(1024, 768)); // Nastavimo velikost okna
        window.setBounds(10, 10, 1024, 768); // Nastavimo pozicijo in velikost okna
        window.setLayout(null); // Nastavimo postavitev okna
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Nastavimo akcijo ob zaprtju okna
        window.setLocationRelativeTo(null); // Nastavimo pozicijo okna na sredino
        window.setResizable(false); // Omogočimo spreminjanje velikosti okna

        container = window.getContentPane(); // Ustvarimo nov container

        mainTitle = new JLabel("Živalske Vrste Obrazec"); // Ustvarimo nov label
        mainTitle.setFont(new Font("Arial", Font.BOLD, 48)); // Nastavimo velikost in obliko pisave
        mainTitle.setBounds(10, 50, 1004, 50); // Nastavimo pozicijo in velikost
        container.add(mainTitle); // Dodamo label v container

        imeLabel = new JLabel("Ime živalske vrste:"); // Ustvarimo nov label
        imeLabel.setBounds(10, 150, 200, 40); // Nastavimo pozicijo in velikost
        container.add(imeLabel); // Dodamo label v container

        imeField = new JTextField(); // Ustvarimo nov textfield
        imeField.setBounds(220, 150, 200, 40); // Nastavimo pozicijo in velikost
        container.add(imeField); // Dodamo textfield v container

        opisLabel = new JLabel("Opis živalske vrste:"); // Ustvarimo nov label
        opisLabel.setBounds(10, 200, 200, 40); // Nastavimo pozicijo in velikost
        container.add(opisLabel); // Dodamo label v container

        opisArea = new JTextArea(); // Ustvarimo novo text area
        opisArea.setBounds(220, 200, 600, 200); // Nastavimo pozicijo in velikost
        container.add(opisArea); // Dodamo text area v container

        naravniHabitatLabel = new JLabel("Naravni habitat:"); // Ustvarimo nov label
        naravniHabitatLabel.setBounds(10, 400, 200, 40); // Nastavimo pozicijo in velikost
        container.add(naravniHabitatLabel); // Dodamo label v container

        naravniHabitatField = new JTextField(); // Ustvarimo nov textfield
        naravniHabitatField.setBounds(220, 400, 200, 40); // Nastavimo pozicijo in velikost
        container.add(naravniHabitatField); // Dodamo textfield v container

        shraniButton = new JButton("Shrani"); // Ustvarimo nov gumb
        shraniButton.setBounds(10, 450, 100, 40); // Nastavimo pozicijo in velikost
        shraniButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shraniZivalskoVrsto();
            }
        });
        container.add(shraniButton); // Dodamo gumb v container

        window.setVisible(true); // Naredimo okno vidno

        if (zivalskaVrstaId != 0) {
            try {
                DB db = DB.getInstance();
                String query = "SELECT * FROM zivalska_vrsta WHERE id = " + zivalskaVrstaId + ";";
                ResultSet resultSet = db.executeQuery(query);
                if (resultSet.next()) {
                    imeField.setText(resultSet.getString("ime"));
                    opisArea.setText(resultSet.getString("opis"));
                    naravniHabitatField.setText(resultSet.getString("naravni_habitat"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void shraniZivalskoVrsto() {
        String ime = imeField.getText();
        String opis = opisArea.getText();
        String naravniHabitat = naravniHabitatField.getText();

        if (ime.isEmpty() || opis.isEmpty() || naravniHabitat.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vsa polja morajo biti izpolnjena.", "Napaka", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            DB db = DB.getInstance();

            if (zivalskaVrstaId <= 0) {
                String query = "SELECT insert_zivalska_vrsta('" + ime + "', '" + opis + "', '" + naravniHabitat + "');";
                db.executeQuery(query);
            } else {
                String query = "SELECT update_zivalska_vrsta(" + zivalskaVrstaId + ", '" + ime + "', '" + opis + "', '" + naravniHabitat + "');";
                db.executeQuery(query);
            }
            window.dispose();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void show() {
        window.setVisible(true); // Naredimo okno vidno
    }
}
