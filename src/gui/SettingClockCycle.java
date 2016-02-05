package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;
/**
 * 
 * @author Hesam
 *
 */
public class SettingClockCycle extends JFrame {

	private JPanel contentPane;
	private JSpinner spinnerLoadTom;
	private JSpinner spinnerStoreTom;
	private JLabel lblMult;
	private JLabel lblDivide_1;
	private JPanel panel_2;
	private JButton btnSave;
	private JSpinner spinnerNumIntegerTom;
	private JSpinner spinnerNumAddSubTom;
	private JSpinner spinnerNumMultTom;
	private JSpinner spinnerNumDivTom;
	private JSpinner spinnerScoreIntegerCLK ;
	private JSpinner spinnerScoreAddSubCLK ;
	private JSpinner spinnerScoreMultCLK ;
	private JSpinner spinnerScoreDivideCLK ;
	
	private JSpinner spinnerNumIntegerScore;
	private JSpinner spinnerNumAddSubScore;
	private JSpinner spinnerNumMultScore;
	private JSpinner spinnerNumDivScore;
	private JSpinner spinnerTomIntegerCLK;
	private JSpinner spinnerTomAddICLK;
	private JSpinner spinnerTomAddSubCLK;
	private JSpinner spinnerTomMulCLK;
	private JSpinner spinnerTomDivCLK;
	private JLabel lblIntegerUnit_1;
	private JLabel lblAddsubUnit_1;
	private JLabel lblMulUnit_1;
	private JLabel lblDivUnit_1;
	private JSpinner spinnerPipeFPMultCLK;
	private JSpinner spinnerPipeFPDivideCLK;
	private JSpinner spinnerPipeIntDivideCLK;
	private JSpinner spinnerPipeFPAddSubCLK;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SettingClockCycle frame = new SettingClockCycle();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void readINI()
	{
		
		try{
			/*[pipeline]
					PipeFPAddSub=1
					PipeFPMult=1
					PipeFPDivide=1
					PipeIntDivide=1
					[scorboard]
					ScoreInteger=1
					ScoreAddSub=2
					ScoreMult=10
					ScoreDivide=40
					[tomasulo]
					TomInteger=2
					TomAddI=1
					TomAddSub=4
					TomMult=7
					TomDivide=25*/
			   Wini ini;
			   /* Load the ini file. */
			   ini = new Wini(new File("config/settings.ini"));

			   int PipeFPAddSub = ini.get("pipeline", "PipeFPAddSub", int.class);
				spinnerPipeFPAddSubCLK.setValue(PipeFPAddSub);
				int PipeFPMult = ini.get("pipeline", "PipeFPMult", int.class);
				spinnerPipeFPMultCLK.setValue(PipeFPMult);
				int PipeFPDivide = ini.get("pipeline", "PipeFPDivide", int.class);
				spinnerPipeFPDivideCLK.setValue(PipeFPDivide);
				int PipeIntDivide = ini.get("pipeline", "PipeIntDivide", int.class);
				spinnerPipeIntDivideCLK.setValue(PipeIntDivide);

			   int ScoreInteger = ini.get("scorboard", "ScoreInteger", int.class);
				spinnerScoreIntegerCLK.setValue(ScoreInteger);
				int ScoreAddSub = ini.get("scorboard", "ScoreAddSub", int.class);
				spinnerScoreAddSubCLK.setValue(ScoreAddSub);
				int ScoreMult = ini.get("scorboard", "ScoreMult", int.class);
				spinnerScoreMultCLK.setValue(ScoreMult);
				int ScoreDivide = ini.get("scorboard", "ScoreDivide", int.class);
				spinnerScoreDivideCLK.setValue(ScoreDivide);

			int ScoreNumInteger = ini.get("scorboard", "ScoreNumInteger", int.class);
			spinnerNumIntegerScore.setValue(ScoreNumInteger);
			int ScoreNumAddSub = ini.get("scorboard", "ScoreNumAddSub", int.class);
			spinnerNumAddSubScore.setValue(ScoreNumAddSub);
			int ScoreNumMult = ini.get("scorboard", "ScoreNumMult", int.class);
			spinnerNumMultScore.setValue(ScoreNumMult);
			int ScoreNumDivide = ini.get("scorboard", "ScoreNumDivide", int.class);
			spinnerNumDivScore.setValue(ScoreNumDivide);

			   int TomInteger = ini.get("tomasulo", "TomInteger", int.class);
				spinnerTomIntegerCLK.setValue(TomInteger);
				int TomAddI = ini.get("tomasulo", "TomAddI", int.class);
				spinnerTomAddICLK.setValue(TomAddI);
				int TomAddSub = ini.get("tomasulo", "TomAddSub", int.class);
				spinnerTomAddSubCLK.setValue(TomAddSub);
				int TomMult = ini.get("tomasulo", "TomMult", int.class);
				spinnerTomMulCLK.setValue(TomMult);
				int TomDivide = ini.get("tomasulo", "TomDivide", int.class);
				spinnerTomDivCLK.setValue(TomDivide);
   
			int TomNumInteger = ini.get("tomasulo", "TomNumInteger", int.class);
			spinnerNumIntegerTom.setValue(TomNumInteger);
			int TomNumAddSub = ini.get("tomasulo", "TomNumAddSub", int.class);
			spinnerNumAddSubTom.setValue(TomNumAddSub);
			int TomNumMult = ini.get("tomasulo", "TomNumMult", int.class);
			spinnerNumMultTom.setValue(TomNumMult);
			int TomNumDiv = ini.get("tomasulo", "TomNumDiv", int.class);
			spinnerNumDivTom.setValue(TomNumDiv);

			int TomNumLoad = ini.get("tomasulo", "TomNumLoad", int.class);
			spinnerLoadTom.setValue(TomNumLoad);
			int TomNumStore = ini.get("tomasulo", "TomNumStore", int.class);
			spinnerStoreTom.setValue(TomNumStore);



			  } catch (InvalidFileFormatException e) {
			   System.out.println("Invalid file format.");
			  } catch (IOException e) {
			   System.out.println("Problem reading file.");
			  }
		
	}
	
	private void writeINI()
	{
		
		 Wini ini;
		 try {
		  ini = new Wini(new File("config/settings.ini"));

		   ini.put("pipeline", "PipeFPAddSub", spinnerPipeFPAddSubCLK.getValue());
		   ini.put("pipeline", "PipeFPMult", spinnerPipeFPMultCLK.getValue());
		   ini.put("pipeline", "PipeFPDivide", spinnerPipeFPDivideCLK.getValue());
		   ini.put("pipeline", "PipeIntDivide", spinnerPipeIntDivideCLK.getValue());
		  
		   ini.put("scorboard", "ScoreInteger", spinnerScoreIntegerCLK.getValue());
		   ini.put("scorboard", "ScoreAddSub", spinnerScoreAddSubCLK.getValue());
		   ini.put("scorboard", "ScoreMult", spinnerScoreMultCLK.getValue());
		   ini.put("scorboard", "ScoreDivide", spinnerScoreDivideCLK.getValue());
		   
			ini.put("scorboard", "ScoreNumInteger", spinnerNumIntegerScore.getValue());
			ini.put("scorboard", "ScoreNumAddSub", spinnerNumAddSubScore.getValue());
			ini.put("scorboard", "ScoreNumMult", spinnerNumMultScore.getValue());
			ini.put("scorboard", "ScoreNumDivide", spinnerNumDivScore.getValue());

		   ini.put("tomasulo", "TomInteger", spinnerTomIntegerCLK.getValue());
		   ini.put("tomasulo", "TomAddI", spinnerTomAddICLK.getValue());
		   ini.put("tomasulo", "TomAddSub", spinnerTomAddSubCLK.getValue());
		   ini.put("tomasulo", "TomMult", spinnerTomMulCLK.getValue());
		   ini.put("tomasulo", "TomDivide", spinnerTomDivCLK.getValue());

			ini.put("tomasulo", "TomNumInteger", spinnerNumIntegerTom.getValue());
			ini.put("tomasulo", "TomNumAddSub", spinnerNumAddSubTom.getValue());
			ini.put("tomasulo", "TomNumMult", spinnerNumMultTom.getValue());
			ini.put("tomasulo", "TomNumDiv", spinnerNumDivTom.getValue());
			ini.put("tomasulo", "TomNumLoad", spinnerLoadTom.getValue());
			ini.put("tomasulo", "TomNumStore", spinnerStoreTom.getValue());
		  ini.store();
		 } catch (InvalidFileFormatException e) {
		  System.out.println("Invalid file format.");
		 } catch (IOException e) {
		  System.out.println("Problem reading file.");
		 }
		
	}
	
	/**
	 * Create the frame.
	 */
	public SettingClockCycle() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
		setBounds(100, 100, 337, 598);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panelPipeline = new JPanel();
		panelPipeline.setBounds(10, 21, 307, 122);
	      panelPipeline.setBorder(new TitledBorder(null, "Pipeline", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelPipeline);
		panelPipeline.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("FP Add/Sub");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setBounds(21, 19, 71, 14);
		panelPipeline.add(lblNewLabel);
		
		JLabel lblFpMultiple = new JLabel("FP Multiple");
		lblFpMultiple.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFpMultiple.setBounds(26, 44, 66, 14);
		panelPipeline.add(lblFpMultiple);
		
		JLabel lblFpDivide = new JLabel("FP Divide");
		lblFpDivide.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFpDivide.setBounds(33, 69, 59, 14);
		panelPipeline.add(lblFpDivide);
		
		JLabel lblIntAdd = new JLabel("int Divide");
		lblIntAdd.setHorizontalAlignment(SwingConstants.RIGHT);
		lblIntAdd.setBounds(33, 94, 59, 14);
		panelPipeline.add(lblIntAdd);
		
		spinnerPipeFPAddSubCLK = new JSpinner();
		spinnerPipeFPAddSubCLK.setBounds(95, 16, 52, 20);
		panelPipeline.add(spinnerPipeFPAddSubCLK);
		
		spinnerPipeFPMultCLK = new JSpinner();
		spinnerPipeFPMultCLK.setBounds(95, 41, 52, 20);
		panelPipeline.add(spinnerPipeFPMultCLK);
		
		spinnerPipeFPDivideCLK = new JSpinner();
		spinnerPipeFPDivideCLK.setBounds(95, 66, 52, 20);
		panelPipeline.add(spinnerPipeFPDivideCLK);
		
		spinnerPipeIntDivideCLK = new JSpinner();
		spinnerPipeIntDivideCLK.setBounds(95, 91, 52, 20);
		panelPipeline.add(spinnerPipeIntDivideCLK);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Scoreboard", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 153, 307, 130);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblInt = new JLabel("# integer CLK");
		lblInt.setHorizontalAlignment(SwingConstants.RIGHT);
		lblInt.setBounds(10, 16, 82, 14);
		panel.add(lblInt);
		
		JLabel lblAddsub = new JLabel("# Add/Sub CLK");
		lblAddsub.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAddsub.setBounds(0, 41, 92, 14);
		panel.add(lblAddsub);
		
		JLabel lblMultiple = new JLabel("# Multiple CLK");
		lblMultiple.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMultiple.setBounds(10, 66, 82, 14);
		panel.add(lblMultiple);
		
		JLabel lblDivide = new JLabel("# Divide CLK");
		lblDivide.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDivide.setBounds(10, 91, 82, 14);
		panel.add(lblDivide);
		
		spinnerNumIntegerScore = new JSpinner();
		spinnerNumIntegerScore.setVisible(false);
		spinnerNumIntegerScore.setBounds(248, 16, 49, 20);
		panel.add(spinnerNumIntegerScore);

		spinnerNumAddSubScore = new JSpinner();
		spinnerNumAddSubScore.setVisible(false);
		spinnerNumAddSubScore.setBounds(248, 41, 49, 20);
		panel.add(spinnerNumAddSubScore);

		spinnerNumMultScore = new JSpinner();
		spinnerNumMultScore.setVisible(false);
		spinnerNumMultScore.setBounds(248, 66, 49, 20);
		panel.add(spinnerNumMultScore);

		spinnerNumDivScore = new JSpinner();
		spinnerNumDivScore.setVisible(false);
		spinnerNumDivScore.setBounds(248, 91, 49, 20);
		panel.add(spinnerNumDivScore);
		
		 spinnerScoreIntegerCLK = new JSpinner();
		spinnerScoreIntegerCLK.setBounds(95, 13, 52, 20);
		panel.add(spinnerScoreIntegerCLK);
		
		 spinnerScoreAddSubCLK = new JSpinner();
		spinnerScoreAddSubCLK.setBounds(95, 38, 52, 20);
		panel.add(spinnerScoreAddSubCLK);
		
		 spinnerScoreMultCLK = new JSpinner();
		spinnerScoreMultCLK.setBounds(95, 63, 52, 20);
		panel.add(spinnerScoreMultCLK);
		
		 spinnerScoreDivideCLK = new JSpinner();
		spinnerScoreDivideCLK.setBounds(95, 88, 52, 20);
		panel.add(spinnerScoreDivideCLK);
		
		lblIntegerUnit_1 = new JLabel("# integer Unit");
		lblIntegerUnit_1.setVisible(false);
		lblIntegerUnit_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblIntegerUnit_1.setBounds(164, 19, 82, 14);
		panel.add(lblIntegerUnit_1);
		
		lblAddsubUnit_1 = new JLabel("# Add/Sub Unit");
		lblAddsubUnit_1.setVisible(false);
		lblAddsubUnit_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAddsubUnit_1.setBounds(164, 44, 82, 14);
		panel.add(lblAddsubUnit_1);
		
		lblMulUnit_1 = new JLabel("# Mul Unit");
		lblMulUnit_1.setVisible(false);
		lblMulUnit_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMulUnit_1.setBounds(173, 69, 73, 14);
		panel.add(lblMulUnit_1);
		
		lblDivUnit_1 = new JLabel("# Div Unit");
		lblDivUnit_1.setVisible(false);
		lblDivUnit_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDivUnit_1.setBounds(173, 94, 73, 14);
		panel.add(lblDivUnit_1);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Tomasulo", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 294, 307, 198);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblIntAdd_1 = new JLabel("# integer CLK");
		lblIntAdd_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblIntAdd_1.setBounds(10, 16, 82, 14);
		panel_1.add(lblIntAdd_1);
		
		JLabel lblNewLabel_1 = new JLabel("# AddI CLK");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1.setBounds(20, 41, 72, 14);
		panel_1.add(lblNewLabel_1);
		
		JLabel lblAddsub_1 = new JLabel("# Add/Sub CLK");
		lblAddsub_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAddsub_1.setBounds(0, 70, 92, 14);
		panel_1.add(lblAddsub_1);
		
		lblMult = new JLabel("# Multiple CLK");
		lblMult.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMult.setBounds(10, 95, 82, 14);
		panel_1.add(lblMult);
		
		lblDivide_1 = new JLabel("# Divide CLK");
		lblDivide_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDivide_1.setBounds(20, 120, 72, 14);
		panel_1.add(lblDivide_1);
		
		spinnerLoadTom = new JSpinner();
		spinnerLoadTom.setBounds(245, 142, 52, 20);
		panel_1.add(spinnerLoadTom);

		spinnerStoreTom = new JSpinner();
		spinnerStoreTom.setBounds(245, 167, 52, 20);
		panel_1.add(spinnerStoreTom);

		JLabel lblLoad = new JLabel("# Load");
		lblLoad.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLoad.setBounds(184, 145, 59, 14);
		panel_1.add(lblLoad);

		JLabel lblStore = new JLabel("# Store");
		lblStore.setHorizontalAlignment(SwingConstants.RIGHT);
		lblStore.setBounds(191, 170, 52, 14);
		panel_1.add(lblStore);

		spinnerNumIntegerTom = new JSpinner();
		spinnerNumIntegerTom.setBounds(245, 16, 52, 20);
		panel_1.add(spinnerNumIntegerTom);

		spinnerNumAddSubTom = new JSpinner();
		spinnerNumAddSubTom.setBounds(245, 67, 52, 20);
		panel_1.add(spinnerNumAddSubTom);

		spinnerNumMultTom = new JSpinner();
		spinnerNumMultTom.setBounds(245, 92, 52, 20);
		panel_1.add(spinnerNumMultTom);

		spinnerNumDivTom = new JSpinner();
		spinnerNumDivTom.setBounds(245, 117, 52, 20);
		panel_1.add(spinnerNumDivTom);
		
		spinnerTomIntegerCLK = new JSpinner();
		spinnerTomIntegerCLK.setBounds(94, 13, 52, 20);
		panel_1.add(spinnerTomIntegerCLK);
		
		spinnerTomAddICLK = new JSpinner();
		spinnerTomAddICLK.setBounds(94, 38, 52, 20);
		panel_1.add(spinnerTomAddICLK);
		
		spinnerTomAddSubCLK = new JSpinner();
		spinnerTomAddSubCLK.setBounds(94, 66, 52, 20);
		panel_1.add(spinnerTomAddSubCLK);
		
		spinnerTomMulCLK = new JSpinner();
		spinnerTomMulCLK.setBounds(94, 92, 52, 20);
		panel_1.add(spinnerTomMulCLK);
		
		spinnerTomDivCLK = new JSpinner();
		spinnerTomDivCLK.setBounds(94, 120, 52, 20);
		panel_1.add(spinnerTomDivCLK);
		
		JLabel lblIntegerUnit = new JLabel("# integer Unit");
		lblIntegerUnit.setHorizontalAlignment(SwingConstants.RIGHT);
		lblIntegerUnit.setBounds(161, 16, 82, 14);
		panel_1.add(lblIntegerUnit);
		
		JLabel lblAddsubUnit = new JLabel("# Add/Sub Unit");
		lblAddsubUnit.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAddsubUnit.setBounds(161, 70, 82, 14);
		panel_1.add(lblAddsubUnit);
		
		JLabel lblMulUnit = new JLabel("# Mul Unit");
		lblMulUnit.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMulUnit.setBounds(174, 95, 69, 14);
		panel_1.add(lblMulUnit);
		
		JLabel lblDivUnit = new JLabel("# Div Unit");
		lblDivUnit.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDivUnit.setBounds(184, 120, 59, 14);
		panel_1.add(lblDivUnit);

		panel_2 = new JPanel();
		panel_2.setBounds(10, 503, 307, 45);
		contentPane.add(panel_2);
		panel_2.setLayout(null);
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				writeINI();
			}
		});
		btnSave.setBounds(196, 11, 71, 23);
		panel_2.add(btnSave);
		readINI();
	}
}
