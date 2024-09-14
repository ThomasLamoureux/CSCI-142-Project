package mainMenu;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import main.Window;

public class Menu {

    public static void main(String[] args) {
        // Creating a window
        Window window = Window.getWindow();

        // Throws an error if window cannot open
        if (window == null) {
            System.out.println("Error: The window could not be created.");
        } else {
            
            // Properties for the window
            window.setDefaultCloseOperation(Window.EXIT_ON_CLOSE);
            //window.setSize(400, 300);

            // Use a GridLayout with 3 rows and 1 column
            JPanel panel = new JPanel(new GridLayout(4, 1));

            // Creating a title for the Main Menu
            JLabel titleLabel = new JLabel("ATORAN", JLabel.CENTER);
            // Gives color and font for the Menu
            //titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
            //titleLabel.setForeground(Color.BLUE);  // Customize the color
            //titleLabel.setHorizontalAlignment(JLabel.CENTER);
    		titleLabel.setFont(new Font("Calibri", Font.PLAIN, Window.scaleInt(125)));
    		panel.add(titleLabel);
           


            // Adding space between the labels and title
           // panel.add(new JLabel());

            // Create and add the "Play" button
            JButton playButton = new JButton("Play");
            playButton.setFont(new Font("Calibri", Font.PLAIN, 30));  
            panel.add(playButton);
            

            // Add the panel to the window and set the window visible
            window.add(panel);
            window.setVisible(true);
        }
    }
}

