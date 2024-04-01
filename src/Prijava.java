import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Prijava {

    private JFrame window;
    private Container container;
    private JLabel mainTitle;
    private JLabel usernameLabel;
    private JTextField usernameField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public Prijava() {
        window = new JFrame("Prijava"); // Ustvarimo novo okno
        window.setPreferredSize(new Dimension(1024, 768)); // Nastavimo velikost okna
        window.setBounds(10, 10, 1024, 768); // Nastavimo pozicijo in velikost okna
        window.setLayout(new BorderLayout()); // Nastavimo postavitev okna
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Nastavimo akcijo ob zaprtju okna
        window.setLocationRelativeTo(null); // Nastavimo pozicijo okna na sredino
        window.setResizable(false); // Omogočimo spreminjanje velikosti okna

        container = window.getContentPane(); // Ustvarimo nov container
        container.setLayout(null); // Nastavimo postavitev panela

        mainTitle = new JLabel("Prijava"); // Ustvarimo nov label
        mainTitle.setFont(new Font("Arial", Font.BOLD, 48)); // Nastavimo velikost in obliko pisave
        mainTitle.setAlignmentX(Component.CENTER_ALIGNMENT); // Nastavimo poravnavo
        mainTitle.setBounds(10, 50, 1004, 50); // Nastavimo pozicijo in velikost
        container.add(mainTitle); // Dodamo label v panel

        usernameLabel = new JLabel("Uporabniško ime:"); // Ustvarimo nov label
        usernameLabel.setBounds(10, 150, 1004, 40); // Nastavimo pozicijo in velikost
        container.add(usernameLabel); // Dodamo label v panel

        usernameField = new JTextField(); // Ustvarimo nov textfield
        usernameField.setBounds(10, 190, 1004, 40); // Nastavimo pozicijo in velikost
        container.add(usernameField); // Dodamo textfield v panel

        passwordLabel = new JLabel("Geslo:"); // Ustvarimo nov label
        passwordLabel.setBounds(10, 240, 1004, 40); // Nastavimo pozicijo in velikost
        container.add(passwordLabel); // Dodamo label v panel

        passwordField = new JPasswordField(); // Ustvarimo nov textfield
        passwordField.setBounds(10, 280, 1004, 40); // Nastavimo pozicijo in velikost
        container.add(passwordField); // Dodamo textfield v panel

        loginButton = new JButton("Prijavi se"); // Ustvarimo nov gumb
        loginButton.setBounds(10, 330, 1004, 40); // Nastavimo pozicijo in velikost
        loginButton.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed();
            }
        }); // Dodamo listener na gumb
        container.add(loginButton); // Dodamo gumb v panel

        registerButton = new JButton("Pojdi na registracijo"); // Ustvarimo nov gumb
        registerButton.setBounds(10, 650, 1004, 40); // Nastavimo pozicijo in velikost
        registerButton.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerButtonActionPerformed(evt);
            }
        });  // Nastavimo akcijo ob kliku na gumb
        container.add(registerButton); // Dodamo gumb v panel
    }

    private void loginButtonActionPerformed() {
        String username = usernameField.getText(); // Preberemo vrednost iz textfielda
        String password = new String(passwordField.getPassword()); // Preberemo vrednost iz textfielda
        try {
            DB database = DB.getInstance(); // Ustvarimo povezavo na bazo
            ResultSet result = database.executeQuery("SELECT * FROM userji WHERE uporabniskoime = '" + username + "' AND geslo = '" + password + "';"); // Izvedemo poizvedbo
            if (result.next()) { // Če je uporabnik najden
                JOptionPane.showMessageDialog(window, "Uspešno ste se prijavili!"); // Izpišemo sporočilo
                Shramba.getInstance().uporabnikId = result.getInt("id"); // Shranimo id uporabnika
                Home home = new Home(); // Ustvarimo novo okno
                home.show(); // Pokažemo novo okno
                window.dispose(); // Zapremo trenutno okno
            } else { // Če uporabnik ni najden
                JOptionPane.showMessageDialog(window, "Napačno uporabniško ime ali geslo!"); // Izpišemo sporočilo
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(window, "Napaka pri prijavi!"); // Izpišemo sporočilo
        }
    }

    private void registerButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Registracija registracija = new Registracija(); // Ustvarimo novo okno
        registracija.show(); // Pokažemo novo okno
        window.dispose(); // Zapremo trenutno okno
    }

    public void show() {
        window.setVisible(true); // Naredimo okno vidno
    }
}
