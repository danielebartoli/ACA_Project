package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

/**
 * 
 * @author Hesam
 *
 */
public class Help extends JFrame {

	private String strFileName;

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Help frame = new Help("Help");
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
	public Help(String strFileName2) {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(100, 100, 583, 525);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		this.strFileName = strFileName2;
		JEditorPane editorPane = null;
		try {
			File file = new File("config/" + strFileName + ".html");
			// JEditorPane htmlPane = new JEditorPane(file.toURI().toURL());

			editorPane = new JEditorPane();
			editorPane.setText(null);
			editorPane.setPage(file.toURI().toString());
			editorPane.setEditable(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// contentPane.add(editorPane, BorderLayout.CENTER);

		JScrollPane scrollPane_1 = new JScrollPane();
		contentPane.add(scrollPane_1, BorderLayout.CENTER);
		scrollPane_1.setViewportView(editorPane);
	}



}
