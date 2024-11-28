import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.CardLayout;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu frame = new MainMenu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainMenu() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1000, 700);
		contentPane = new JPanel();

		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Pacman pac = new Pacman(); 
				pac.setVisible(true);
				pac.setTitle("Pacman");
				pac.setSize(700, 750);
				pac.setDefaultCloseOperation(EXIT_ON_CLOSE);
				pac.setLocationRelativeTo(null);
				pac.setVisible(true);
				
				dispose();
			}
		});
		
		JLabel lblNewLabel_2_1 = new JLabel("");
		lblNewLabel_2_1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
		
		JLabel lblNewLabel_2_2 = new JLabel("");
		lblNewLabel_2_2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Instructions how = new Instructions();
				how.setVisible(true);
				dispose();
			}
			
			
		});
		lblNewLabel_2_2.setIcon(new ImageIcon(MainMenu.class.getResource("/images/howtoplay.png")));
		lblNewLabel_2_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_2.setBounds(235, 399, 517, 73);
		contentPane.add(lblNewLabel_2_2);
		lblNewLabel_2_1.setIcon(new ImageIcon(MainMenu.class.getResource("/images/text-1732764626862.png")));
		lblNewLabel_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_1.setBounds(235, 481, 517, 73);
		contentPane.add(lblNewLabel_2_1);
		
		
		lblNewLabel_2.setIcon(new ImageIcon(MainMenu.class.getResource("/images/PLAY_BTN.png")));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(235, 303, 517, 73);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(MainMenu.class.getResource("/images/PACMAN_LOGO.png")));
		lblNewLabel_1.setForeground(new Color(255, 255, 0));
		lblNewLabel_1.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("Segoe UI Black", Font.BOLD, 47));
		lblNewLabel_1.setBounds(98, 43, 776, 116);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(0, 0, 984, 661);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setIcon(new ImageIcon(MainMenu.class.getResource("/images/PACMAN_BG.png")));
		contentPane.add(lblNewLabel);
	}
}
