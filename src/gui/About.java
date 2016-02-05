package gui;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
/**
 * 
 * @author Hesam
 *
 */
public class About extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					About frame = new About();
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
	public About() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);

		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(89, 40, 257, 157);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblSimulatorApplicationContain = new JLabel("Simulator application contain ");
		lblSimulatorApplicationContain.setHorizontalAlignment(SwingConstants.CENTER);
		lblSimulatorApplicationContain.setBounds(21, 22, 204, 14);
		panel.add(lblSimulatorApplicationContain);

		JLabel lblProviderHesamoddin = new JLabel("Provider: Hesamoddin");
		lblProviderHesamoddin.setBounds(56, 116, 135, 14);
		panel.add(lblProviderHesamoddin);

		JLabel lblPipeline = new JLabel("Pipeline");
		lblPipeline.setBounds(96, 41, 62, 14);
		panel.add(lblPipeline);

		JLabel lblScoreboard = new JLabel("Scoreboard");
		lblScoreboard.setBounds(96, 66, 76, 14);
		panel.add(lblScoreboard);

		JLabel lblTomasulo = new JLabel("Tomasulo");
		lblTomasulo.setBounds(96, 92, 62, 14);
		panel.add(lblTomasulo);
	}
}
