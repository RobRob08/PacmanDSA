import java.awt.Component;
import java.awt.Menu;

import javax.swing.JFrame; 

public class Pacman extends JFrame{


	public Pacman() {
		add(new Model()); 
	}

	public static void main(String[] args) {
	       
        
		Pacman pac = new Pacman(); 
		pac.setVisible(true);
		pac.setTitle("Pacman");
		pac.setSize(700, 750);
		pac.setDefaultCloseOperation(EXIT_ON_CLOSE);
		pac.setLocationRelativeTo(null);
		pac.setVisible(true);
		
	}
}