package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

import PipeLine.Operator;
import Scorboard.EnumFunctionUnits;
import Scorboard.EnumInstructionStatus;
import Scorboard.FunctionUnitStatus;
import Scorboard.Instruction;
import Scorboard.InstructionStatus;
import tomasulo.ALUStation;
import tomasulo.Clock;
import tomasulo.MemStation;
import tomasulo.RegisterFiles;
/**
 * 
 * @author Hesam
 * prova commit 123
 */
public class Main extends JFrame {

	private JPanel contentPane;
	private JTextField txtUserName;
	private JTextField txtPassword;
	JPanel pnlLogin;
	public boolean bolMode = false;
	private JMenuItem mntmProfessor;
	JPanel panelScoreboard;
	JPanel panelTomasulo;
	JPanel panelPipeline;
	JPanel panelSendEmail;
	// **************************************************************************************
	//// start scoreboard
	// **************************************************************************************
	private JTable tblInstructionsScoreStudent;

	private JTable tblInstructionsScore;
	private JTable tblFunctionUnitsScore;
	private JTable tblRegistersScore;

	JButton btnExecAllScore;

	JTextArea txtInstructionsScore;
	JRadioButton rdbtnStep;
	JCheckBox chckbxExeAllScore;
	JCheckBox chckbxForwardingScorboard;

	JTextArea txtDisplayScorboard;
	List<Instruction> lstInstructionsScorboard = new ArrayList<Instruction>();
	List<String> lstreg_resultScorboard1;// =new ArrayList<String>(32);
	List<String> lstreg_resultScorboard2 = new ArrayList<String>(32);
int[][] lstAsnswerStudet;
	
	List<InstructionStatus> lstInstructStatusScorboard = new ArrayList<InstructionStatus>();
	List<FunctionUnitStatus> _lstFuStatusTempScorboard;// =new
														// ArrayList<FunctionUnitStatus>();
	List<FunctionUnitStatus> _lstFuStatusScorboard2 = new ArrayList<FunctionUnitStatus>();

	List<EnumInstructionStatus> _StatusNextInstructionScorboard;// =new
																// ArrayList<EnumInstructionStatus>(max_no_instr);

	//functional units

	int mult1 = 1;
	int mult2 = 2;
	int MULT = 5;

	int clock = 0;
	int _NumInstruction, _currentIssue, instr_no, upper_limit, type_of_output, req_clock;
	boolean update_flag;
	int counter = 0;
	boolean flag;

	private JTextField txtDes;
	private JTextField txtSR1;
	private JTextField txtSR2;
	JComboBox cmbFunUnit;

	DefaultTableModel _InstructionModelScoreboardStudent;

	DefaultTableModel _InstructionModelScoreboard;
	DefaultTableModel _FunctionUnitsModelScoreboard;
	DefaultTableModel _RegistersModelScoreboard;
	private JScrollPane scrollPane_4;
	private JTable table;
	/*---------------------------------------------------------------------------------------------------*/
	/* END OF VARIABLE DECLARATION */
	/*--------------------------------------------------------------------------------------------------*/

	private int abs(int input) {
		if (input < 0) {
			input = input * (-1);
		}
		return input;
	}

	// **************************************************************************************

	private void Add_DependencyScoreboard(String input) {
		txtDisplayScorboard.setText(txtDisplayScorboard.getText() + input + "\n");
		System.out.println(txtDisplayScorboard.getText() + input);

	}
	// **************************************************************************************

	private void CheckDepencencyScoreboard() {
		// Reset the dependency list.
		txtDisplayScorboard.setText("");

		// Create a dependency variable
		int dep_exists = 0;
		int data_forwarding = 0;
		int non_forward_delay = 2;

		// i is the current instruction.
		for (int i = 0; i < lstInstructionsScorboard.size(); i++) {
			// j is one of the previous instructions.
			for (int j = 0; j < i; j++) {

				dep_exists = 0;

				String OPrandI = lstInstructionsScorboard.get(i).getFu().toString().toLowerCase();
				String OPrandJ = lstInstructionsScorboard.get(j).getFu().toString().toLowerCase();

				List<FunctionUnitStatus> resultJ = _lstFuStatusScorboard2.stream()
						.filter(p -> p.getFu_name().contains(OPrandI)).collect(Collectors.toList());
				int OPrandCycleJ = resultJ.get(0).getExec();

				List<FunctionUnitStatus> resultI = _lstFuStatusScorboard2.stream()
						.filter(p -> p.getFu_name().contains(OPrandJ)).collect(Collectors.toList());
				int OPrandCycleI = resultI.get(0).getExec();
				// String OPrandI= lstInstructions.get(i).getFu().toString();

				// int OPrandCycleI= _lstFuStatus2..get(OPrandI);
				// String OPrandI= lstInstructions.get(i).getFu().toString();

				// String OPrandJ= lstInstructions.get(j).getFu().toString();
				// int OPrandCycleJ= instruction_to_time.get(OPrandJ);

				// *** RAW ***
				if ((lstInstructionsScorboard.get(j).getDes().equals(lstInstructionsScorboard.get(i).getS1()))
						|| (lstInstructionsScorboard.get(j).getDes().equals(lstInstructionsScorboard.get(i).getS2()))) {
					// A load instruction will take one cycle longer than any
					// other instruction when forwarding is enabled.
					if ((lstInstructionsScorboard.get(j).getOper().toLowerCase().equals("ld"))
							&& (data_forwarding == 1)) {

						if ((i - j) < OPrandCycleJ + non_forward_delay + 1) {
							dep_exists = 1;
							Add_DependencyScoreboard(("RAW: Instructions " + j + " and " + i + ".  Register "
									+ lstInstructionsScorboard.get(j).getDes() + "."));
						}
					} else {

						if ((i - j) < OPrandCycleJ + non_forward_delay) {
							dep_exists = 1;
							Add_DependencyScoreboard(("RAW: Instructions " + j + " and " + i + ".  Register "
									+ lstInstructionsScorboard.get(j).getDes() + "."));
						}
					}
				}

				// *** Test for WAR ***
				if (((lstInstructionsScorboard.get(j).getS1().equals(lstInstructionsScorboard.get(i).getDes()))
						|| (lstInstructionsScorboard.get(j).getS2()
								.equals(lstInstructionsScorboard.get(i).getDes())))) {
					Add_DependencyScoreboard(("WAR: Instructions " + j + " and " + i + ".  Register "
							+ lstInstructionsScorboard.get(i).getDes() + "."));
				}

				// *** WAW ***
				if ((lstInstructionsScorboard.get(i).getDes().equals(lstInstructionsScorboard.get(j).getDes()))) {
					if (!lstInstructionsScorboard.get(j).getDes().equals("null")) {
						Add_DependencyScoreboard(("WAW: Instructions " + j + " and " + i + ".  Register "
								+ lstInstructionsScorboard.get(j).getDes() + "."));
					}
				}

				// *** Test for Structural Hazard ***
				// If two instructions use the same functional unit and the
				// difference between the two instructions is less than the
				// execution cycles of the earlier instruction.
				/*
				 * if
				 * ((instruction_array.get(i).operator.functional_unit.equals(
				 * instruction_array.get(j).operator.functional_unit)) && ((i -
				 * j) < OPrandCycleJ)) { Add_Dependency(
				 * "Structural: Instructions " + j + " and " + i + ".  Unit: " +
				 * lstInstructions.get(j).operator.display_value); }
				 */

				// *** simultaneous MEM/WB ***
				if ((dep_exists == 0) && ((i - j) == (OPrandCycleJ - OPrandCycleI))) {
					Add_DependencyScoreboard(
							"Instructions " + j + " and " + i + " will enter the MEM and WB stage at the same time.");
				}

			}
		} // End of outer for loop.

		// If no dependencies were found, output a message.
		if (txtDisplayScorboard.getText().equals("")) {
			txtDisplayScorboard.setText("No Hazards Found.");
		}
	}

	/*-------------------------------------------------------------------------------*/
	/* BOOK KEEPING FUNCTION FOR INSTRUCTION (INSTR_NO) */
	/*-------------------------------------------------------------------------------*/
	private String getRegValueScorboard(String strReg) {
		String _strTemp = "";
		if (strReg.contains("R") || strReg.contains("F")) {
			if (strReg.length() == 2) {
				_strTemp = strReg.substring(1, 2);
			} else if (strReg.length() == 3) {
				_strTemp = strReg.substring(1, 3);
			}
		}
		return _strTemp;
	}

	private void ExecuteInstructionScorboard(int instr_no) {
		boolean temp_flag, cond1, cond2, cond3, cond4;
		EnumFunctionUnits FU;

		Integer S1, S2, D, FI;
		int p, q;
		int rowIStr;
		FU = lstInstructionsScorboard.get(instr_no).getFu();

		switch (FU) {
		case integer:
			rowIStr = 0;
			break;
		case mult1:
			rowIStr = 1;
			break;
		case mult2:
			rowIStr = 2;
			break;
		case add:
			rowIStr = 3;
			break;
		case divid:
			rowIStr = 4;
			break;
		case MULT:
			rowIStr = 5;
			break;

		default:
			rowIStr = -1;
			break;
		}

		// TODO change to dynamic select size string to get reg num
		D = Integer.parseInt(lstInstructionsScorboard.get(instr_no).getDes().substring(1, 2));
		if ((lstInstructionsScorboard.get(instr_no).getOper().equals("LD"))
				|| (lstInstructionsScorboard.get(instr_no).getOper().equals("SD"))) {
			S1 = 31;
			S2 = 31;
		} else {
			S1 = Integer.parseInt(getRegValueScorboard(lstInstructionsScorboard.get(instr_no).getS1()));
			S2 = Integer.parseInt(getRegValueScorboard(lstInstructionsScorboard.get(instr_no).getS2()));
		}

		switch (lstInstructionsScorboard.get(instr_no).getStatus()) {
		case issue:
			if (FU == EnumFunctionUnits.MULT) {
				for (p = mult1; p <= mult2; p++) // Foreach of the multiplication unit do the loop
				{
					cond1 = !(_lstFuStatusTempScorboard.get(p).getBusy().equals("Yes")); // checks if FU is not busy
					cond2 = (lstreg_resultScorboard1.get(D) == ""); // checks if the dest.reg.is not being written(RAW)
					if (cond1 && cond2) // If both these condition satisfy then update the scoreboard
					{
						/*
						 * _lstFuStatus2.get(p).setBusy("Yes");
						 * _lstFuStatus2.get(p).setOp("Mult");
						 * _lstFuStatus2.get(p).setFi(lstInstructions.get(
						 * instr_no).getDes());
						 * _lstFuStatus2.get(p).setFj(lstInstructions.get(
						 * instr_no).getS1());
						 * _lstFuStatus2.get(p).setFk(lstInstructions.get(
						 * instr_no).getS2());
						 * _lstFuStatus2.get(p).setQj(lstreg_result1.get(S1));
						 * _lstFuStatus2.get(p).setQk(lstreg_result1.get(S2));
						 * 
						 * if(_lstFuStatus2.get(p).getQj()=="")
						 * _lstFuStatus2.get(p).setRj("Yes"); else
						 * _lstFuStatus2.get(p).setRj("No");
						 * 
						 * if(_lstFuStatus2.get(p).getQk()=="")
						 * _lstFuStatus2.get(p).setRk("Yes"); else
						 * _lstFuStatus2.get(p).setRk("No");
						 */
						FunctionUnitStatus FUTEMP = new FunctionUnitStatus();
						FUTEMP.setFu_name(_lstFuStatusScorboard2.get(p).getFu_name());

						FUTEMP.setBusy("Yes");
						FUTEMP.setOp("Mult");
						FUTEMP.setFi(lstInstructionsScorboard.get(instr_no).getDes());
						FUTEMP.setFj(lstInstructionsScorboard.get(instr_no).getS1());
						FUTEMP.setFk(lstInstructionsScorboard.get(instr_no).getS2());
						FUTEMP.setQj(lstreg_resultScorboard1.get(S1));
						FUTEMP.setQk(lstreg_resultScorboard1.get(S2));

						if (FUTEMP.getQj() == "")
							FUTEMP.setRj("Yes");
						else
							FUTEMP.setRj("No");

						if (FUTEMP.getQk() == "")
							FUTEMP.setRk("Yes");
						else
							FUTEMP.setRk("No");

						FUTEMP.setExec(_lstFuStatusScorboard2.get(p).getExec());

						_lstFuStatusScorboard2.set(p, FUTEMP);

						lstreg_resultScorboard2.set(D, _lstFuStatusTempScorboard.get(p).getFu_name());

						_StatusNextInstructionScorboard.set(instr_no, EnumInstructionStatus.readOp);

						lstInstructStatusScorboard.get(instr_no).setIssue(clock);
						_currentIssue = instr_no;
						if (p == mult1)
							lstInstructionsScorboard.get(instr_no).setFu(EnumFunctionUnits.mult1);
						if (p == mult2)
							lstInstructionsScorboard.get(instr_no).setFu(EnumFunctionUnits.mult2);

						update_flag = true;
						break;

					} else
						_StatusNextInstructionScorboard.set(instr_no, EnumInstructionStatus.issue);
				}
			} else {
				// p =FU;
				p = rowIStr;
				cond1 = !(_lstFuStatusTempScorboard.get(p).getBusy().equals("Yes")); // checks
																						// if
																						// FU
																						// is
																						// not
																						// busy
				cond2 = (lstreg_resultScorboard1.get(D) == ""); // checks if the
																// dest.reg.is
																// not being
																// written(RAW)
				if (cond1 && cond2) // If both these condition satisfy then
									// update the scoreboard
				{
					/*
					 * _lstFuStatus2.get(p).setBusy("Yes");
					 * _lstFuStatus2.get(p).setOp(lstInstructions.get(instr_no).
					 * getOp_name());
					 * _lstFuStatus2.get(p).setFi(lstInstructions.get(instr_no).
					 * getDes());
					 * _lstFuStatus2.get(p).setFj(lstInstructions.get(instr_no).
					 * getS1());
					 * _lstFuStatus2.get(p).setFk(lstInstructions.get(instr_no).
					 * getS2());
					 * _lstFuStatus2.get(p).setQj(lstreg_result1.get(S1));
					 * _lstFuStatus2.get(p).setQk(lstreg_result1.get(S2));
					 * 
					 * if(_lstFuStatus2.get(p).getQj()=="")
					 * _lstFuStatus2.get(p).setRj("Yes"); else
					 * _lstFuStatus2.get(p).setRj("No");
					 * 
					 * if(_lstFuStatus2.get(p).getQk()=="")
					 * _lstFuStatus2.get(p).setRk("Yes"); else
					 * _lstFuStatus2.get(p).setRk("No");
					 */

					FunctionUnitStatus FUTEMP = new FunctionUnitStatus();
					FUTEMP.setFu_name(_lstFuStatusScorboard2.get(p).getFu_name());
					FUTEMP.setBusy("Yes");
					FUTEMP.setOp(lstInstructionsScorboard.get(instr_no).getOp_name());
					FUTEMP.setFi(lstInstructionsScorboard.get(instr_no).getDes());
					FUTEMP.setFj(lstInstructionsScorboard.get(instr_no).getS1());
					FUTEMP.setFk(lstInstructionsScorboard.get(instr_no).getS2());
					FUTEMP.setQj(lstreg_resultScorboard1.get(S1));
					FUTEMP.setQk(lstreg_resultScorboard1.get(S2));

					if (FUTEMP.getQj().equals(""))
						FUTEMP.setRj("Yes");
					else
						FUTEMP.setRj("No");

					if (FUTEMP.getQk().equals(""))
						FUTEMP.setRk("Yes");
					else
						FUTEMP.setRk("No");

					FUTEMP.setExec(_lstFuStatusScorboard2.get(p).getExec());

					_lstFuStatusScorboard2.set(p, FUTEMP);

					lstreg_resultScorboard2.set(D, _lstFuStatusTempScorboard.get(p).getFu_name());

					_StatusNextInstructionScorboard.set(instr_no, EnumInstructionStatus.readOp);

					lstInstructStatusScorboard.get(instr_no).setIssue(clock);
					_currentIssue = instr_no;

					lstInstructionsScorboard.get(instr_no).setFu(FU);

					update_flag = true;
					break;

				} else
					_StatusNextInstructionScorboard.set(instr_no, EnumInstructionStatus.issue);
			}
			break;
		case readOp:
			if ((_lstFuStatusTempScorboard.get(rowIStr).getRj().equals("Yes"))
					&& (_lstFuStatusTempScorboard.get(rowIStr).getRk().equals("Yes"))) {
				// _lstFuStatus2.get(rowIStr).setRj("No");
				// _lstFuStatus2.get(rowIStr).setRk("No");
				// _lstFuStatus2.get(rowIStr).setQj("");
				// _lstFuStatus2.get(rowIStr).setQk("");

				FunctionUnitStatus FUTEMP = new FunctionUnitStatus();
				FUTEMP.setFu_name(_lstFuStatusScorboard2.get(rowIStr).getFu_name());
				FUTEMP.setBusy(_lstFuStatusScorboard2.get(rowIStr).getBusy());
				FUTEMP.setOp(_lstFuStatusScorboard2.get(rowIStr).getOp());
				FUTEMP.setFi(_lstFuStatusScorboard2.get(rowIStr).getFi());
				FUTEMP.setFj(_lstFuStatusScorboard2.get(rowIStr).getFj());
				FUTEMP.setFk(_lstFuStatusScorboard2.get(rowIStr).getFk());
				FUTEMP.setQj("");
				FUTEMP.setQk("");
				FUTEMP.setRj("No");
				FUTEMP.setRk("No");
				FUTEMP.setExec(_lstFuStatusScorboard2.get(rowIStr).getExec());

				_lstFuStatusScorboard2.set(rowIStr, FUTEMP);

				_StatusNextInstructionScorboard.set(instr_no, EnumInstructionStatus.execute);
				lstInstructStatusScorboard.get(instr_no).setReadop(clock);
				lstInstructionsScorboard.get(instr_no).setExec_cycle(_lstFuStatusTempScorboard.get(rowIStr).getExec());
				_lstFuStatusScorboard2.get(rowIStr).setTime(lstInstructionsScorboard.get(instr_no).getExec_cycle());

				update_flag = true;

			} else
				_StatusNextInstructionScorboard.set(instr_no, EnumInstructionStatus.readOp);
			break;

		case execute:
			lstInstructionsScorboard.get(instr_no)
					.setExec_cycle(lstInstructionsScorboard.get(instr_no).getExec_cycle() - 1);
			_lstFuStatusScorboard2.get(rowIStr).setTime(lstInstructionsScorboard.get(instr_no).getExec_cycle());
			if (lstInstructionsScorboard.get(instr_no).getExec_cycle() == 0) {

				_StatusNextInstructionScorboard.set(instr_no, EnumInstructionStatus.writeResult); // next
																									// state
																									// is
																									// write_result
				lstInstructStatusScorboard.get(instr_no).setExecute(clock); // record
																			// the
																			// cycle
																			// when
																			// it
																			// finished
																			// execution
				/// TODO Forwarding
				if (chckbxForwardingScorboard.isSelected())
					updateScorboard1();
				else
					update_flag = true;
			} else {
				_StatusNextInstructionScorboard.set(instr_no, EnumInstructionStatus.execute); // else
																								// wait
																								// in
																								// execution
																								// state
				update_flag = true;
			}

			break;

		case writeResult:
			temp_flag = true;
			for (int j = 0; j < 5; j++) {
				cond1 = (_lstFuStatusTempScorboard.get(j).getFj() != _lstFuStatusTempScorboard.get(rowIStr).getFi());
				cond2 = (_lstFuStatusTempScorboard.get(j).getRj().equals("No"));
				cond3 = (_lstFuStatusTempScorboard.get(j).getFk() != _lstFuStatusTempScorboard.get(rowIStr).getFi());
				cond4 = (_lstFuStatusTempScorboard.get(j).getRk().equals("No"));
				if ((cond1 || cond2) && (cond3 || cond4))
					temp_flag = temp_flag;
				else
					temp_flag = false;
			}

			if (temp_flag) {
				for (int j = 0; j < 5; j++) {
					/// TODO problem by changing value in _lstFuStatusScorboard2
					/// the value in _lstFuStatusTempScorboard will change wrong
					if (_lstFuStatusTempScorboard.get(j).getQj()
							.equals(_lstFuStatusTempScorboard.get(rowIStr).getFu_name()))
						_lstFuStatusScorboard2.get(j).setRj("Yes");
					if (_lstFuStatusTempScorboard.get(j).getQk()
							.equals(_lstFuStatusTempScorboard.get(rowIStr).getFu_name()))
						_lstFuStatusScorboard2.get(j).setRk("Yes");
				}
				// TODO dynamic
				FI = Integer.parseInt(getRegValueScorboard(_lstFuStatusTempScorboard.get(rowIStr).getFi()));
				lstreg_resultScorboard2.set(FI, "");
				FunctionUnitStatus FUTEMP = new FunctionUnitStatus();
				FUTEMP.setFu_name(_lstFuStatusScorboard2.get(rowIStr).getFu_name());
				FUTEMP.setBusy("No");
				FUTEMP.setOp("");
				FUTEMP.setFi("");
				FUTEMP.setFj("");
				FUTEMP.setFk("");
				FUTEMP.setQj("");
				FUTEMP.setQk("");
				FUTEMP.setRj("");
				FUTEMP.setRk("");
				FUTEMP.setExec(_lstFuStatusScorboard2.get(rowIStr).getExec());

				_lstFuStatusScorboard2.set(rowIStr, FUTEMP);

				/*
				 * _lstFuStatus2.get(rowIStr).setBusy("No");
				 * _lstFuStatus2.get(rowIStr).setOp("");
				 * _lstFuStatus2.get(rowIStr).setFi("");
				 * _lstFuStatus2.get(rowIStr).setFj("");
				 * _lstFuStatus2.get(rowIStr).setFk("");
				 * _lstFuStatus2.get(rowIStr).setQj("");
				 * _lstFuStatus2.get(rowIStr).setQk("");
				 * _lstFuStatus2.get(rowIStr).setRj("");
				 * _lstFuStatus2.get(rowIStr).setRk("");
				 */

				_StatusNextInstructionScorboard.set(instr_no, EnumInstructionStatus.complete);
				lstInstructStatusScorboard.get(instr_no).setWrite_result(clock);
				update_flag = true;
			} else
				_StatusNextInstructionScorboard.set(instr_no, EnumInstructionStatus.writeResult);

			break;

		case complete:
			break;

		}

	}
	/*---------------------------------------------------------------------------------------------------------------------------*/
	/*
	 * OPERATION FUNCTION: TAKES THE INSTRUCTION AND RETURNS THE FUNC. UNIT
	 * ASSOCIATED WITH IT ALONG WITH THE NAME OF OPERATION
	 */
	/*---------------------------------------------------------------------------------------------------------------------------*/

	private void operationScorboard(int i) {

		switch (lstInstructionsScorboard.get(i).getOper()) {
		case "LD":
			lstInstructionsScorboard.get(i).setFu(EnumFunctionUnits.integer);
			lstInstructionsScorboard.get(i).setOp_name("Load");
			break;
		case "SD":
			lstInstructionsScorboard.get(i).setFu(EnumFunctionUnits.integer);
			lstInstructionsScorboard.get(i).setOp_name("Store");

			break;
		case "MULTD":
			lstInstructionsScorboard.get(i).setFu(EnumFunctionUnits.MULT);
			lstInstructionsScorboard.get(i).setOp_name("Mult");

			break;
		case "DIVD":
			lstInstructionsScorboard.get(i).setFu(EnumFunctionUnits.divid);
			lstInstructionsScorboard.get(i).setOp_name("Div");

			break;
		case "ADDD":
			lstInstructionsScorboard.get(i).setFu(EnumFunctionUnits.add);
			lstInstructionsScorboard.get(i).setOp_name("Add");

			break;
		case "SUBD":
			lstInstructionsScorboard.get(i).setFu(EnumFunctionUnits.add);
			lstInstructionsScorboard.get(i).setOp_name("Sub");

			break;

		default:
			break;
		}

	}

	/*-----------------------------------------------------------------------------*/
	/*
	 * TO DETERMINE WHETHER ALL INSTRUCTIONS HAVE FINISHED WRITING THEIR RESULTS
	 */
	/*----------------------------------------------------------------------------*/

	private boolean not_completeScorboard(int no_of_instruct) {

		boolean comp;
		comp = false;
		for (int p = 0; p < no_of_instruct; p++) {
			if ((lstInstructionsScorboard.get(p).getStatus() != EnumInstructionStatus.complete))
				comp = true;
		}
		return comp;
	}

	/* Copies the values in _lstFuStatus2 to _lstFuStatusTemp */
	private void updateScorboard1() {

		_lstFuStatusTempScorboard = new ArrayList<FunctionUnitStatus>();
		
		lstreg_resultScorboard1 = new ArrayList<String>(lstreg_resultScorboard2);

		
		  for(FunctionUnitStatus m: _lstFuStatusScorboard2){
		  _lstFuStatusTempScorboard.add((FunctionUnitStatus)m.clone()); }
		 

		 
		/*for (int i = 0; i < _lstFuStatusScorboard2.size(); i++) {
			_lstFuStatusTempScorboard.add((FunctionUnitStatus) _lstFuStatusScorboard2.get(i));
		}*/
		
		
		
		/*
		 * List<MyClass> lstMyClassTemp = new ArrayList<>(); for(MyClass m:
		 * lstMyClass){ lstMyClassTemp.add(m); }
		 */
		// Collections.copy(lstreg_result1, lstreg_result2);
		/*
		 * _lstFuStatusTemp.clear(); for (FunctionUnitStatus item :
		 * _lstFuStatus2) { _lstFuStatusTemp.add(item); }
		 * 
		 * lstreg_result1=lstreg_result2;
		 */
		/*
		 * for (int p=0;p<5;p++) { fu_status1[p][busy]=fu_status2[p][busy];
		 * fu_status1[p][op]=fu_status2[p][op];
		 * fu_status1[p][Fi]=fu_status2[p][Fi];
		 * fu_status1[p][Fj]=fu_status2[p][Fj];
		 * fu_status1[p][Fk]=fu_status2[p][Fk];
		 * fu_status1[p][Qj]=fu_status2[p][Qj];
		 * fu_status1[p][Qk]=fu_status2[p][Qk];
		 * fu_status1[p][Rj]=fu_status2[p][Rj];
		 * fu_status1[p][Rk]=fu_status2[p][Rk];
		 * fu_status1[p][fu_name]=fu_status2[p][fu_name]; fu_status1[p][exec] =
		 * fu_status2[p][exec]; } for (int p=0;p<32;p++)
		 * reg_result1[p]=reg_result2[p];
		 */

	}

	private void updateInstructionTableScorboardStudent() {
		int i = 0;
		for (Instruction instruction : lstInstructionsScorboard) {

			// InstructionModel.setColumnIdentifiers( new
			// Object[]{"Instruction"," ", "j", "k", "Issue", "Read Opr.","Exe
			// Com.", "Write Result"} );

			_InstructionModelScoreboardStudent.setValueAt(instruction.getOper(), i, 0);

			_InstructionModelScoreboardStudent.setValueAt(instruction.getDes(), i, 1);
			_InstructionModelScoreboardStudent.setValueAt(instruction.getS1(), i, 2);
			_InstructionModelScoreboardStudent.setValueAt(instruction.getS2(), i, 3);
			_InstructionModelScoreboardStudent.setValueAt("0", i, 4);
			_InstructionModelScoreboardStudent.setValueAt("0", i, 5);
			_InstructionModelScoreboardStudent.setValueAt("0", i, 6);
			_InstructionModelScoreboardStudent.setValueAt("0", i, 7);
			i++;
		}

	}

	private void updateInstructionTableScorboard() {
		int i = 0;
		for (Instruction instruction : lstInstructionsScorboard) {

			// InstructionModel.setColumnIdentifiers( new
			// Object[]{"Instruction"," ", "j", "k", "Issue", "Read Opr.","Exe
			// Com.", "Write Result"} );

			_InstructionModelScoreboard.setValueAt(instruction.getOper(), i, 0);

			_InstructionModelScoreboard.setValueAt(instruction.getDes(), i, 1);
			_InstructionModelScoreboard.setValueAt(instruction.getS1(), i, 2);
			_InstructionModelScoreboard.setValueAt(instruction.getS2(), i, 3);
			_InstructionModelScoreboard.setValueAt(
					(lstInstructStatusScorboard.size() > 0 ? lstInstructStatusScorboard.get(i).getIssue() : " "), i, 4);
			_InstructionModelScoreboard.setValueAt(
					(lstInstructStatusScorboard.size() > 0 ? lstInstructStatusScorboard.get(i).getReadop() : " "), i,
					5);
			_InstructionModelScoreboard.setValueAt(
					(lstInstructStatusScorboard.size() > 0 ? lstInstructStatusScorboard.get(i).getExecute() : " "), i,
					6);
			_InstructionModelScoreboard.setValueAt(
					(lstInstructStatusScorboard.size() > 0 ? lstInstructStatusScorboard.get(i).getWrite_result() : " "),
					i, 7);
			i++;
		}

	}

	private void updateRegisterTableScorboard() {

		Object[] objTemp = new Object[lstreg_resultScorboard2.size()];

		for (int i = 0; i < lstreg_resultScorboard2.size(); i++) {
			objTemp[i] = lstreg_resultScorboard2.get(i);

		}
		_RegistersModelScoreboard.removeRow(0);
		_RegistersModelScoreboard.addRow(objTemp);

	}

	///
	/// ALUStations Table Update Helper
	///
	private void updateALUTableScorboard() {
		// Get a copy of the memory stations
		// ALUStation[] temp_alu = alu_rs;

		// Update the table with current values for the stations
		for (int i = 0; i < _lstFuStatusScorboard2.size(); i++) {
			// generate a meaningfull representation of busy
			// String busy_desc = (temp_alu[i].isBusy() ? "Yes" : "No" );
			// _FunctionUnitsModel.setColumnIdentifiers( new Object[]{"Cycle",
			// "Name","Busy", "Op", "Fi", "Fj", "Fk", "Qj","Qk","Rj","Rk"} );

			_FunctionUnitsModelScoreboard.setValueAt(_lstFuStatusScorboard2.get(i).getTime(), i, 0);

			_FunctionUnitsModelScoreboard.setValueAt(_lstFuStatusScorboard2.get(i).getFu_name(), i, 1);
			_FunctionUnitsModelScoreboard.setValueAt(_lstFuStatusScorboard2.get(i).getBusy(), i, 2);
			_FunctionUnitsModelScoreboard.setValueAt(_lstFuStatusScorboard2.get(i).getOp(), i, 3);
			_FunctionUnitsModelScoreboard.setValueAt(_lstFuStatusScorboard2.get(i).getFi(), i, 4);
			_FunctionUnitsModelScoreboard.setValueAt(_lstFuStatusScorboard2.get(i).getFj(), i, 5);
			_FunctionUnitsModelScoreboard.setValueAt(_lstFuStatusScorboard2.get(i).getFk(), i, 6);
			_FunctionUnitsModelScoreboard.setValueAt(_lstFuStatusScorboard2.get(i).getQj(), i, 7);
			_FunctionUnitsModelScoreboard.setValueAt(_lstFuStatusScorboard2.get(i).getQk(), i, 8);
			_FunctionUnitsModelScoreboard.setValueAt(_lstFuStatusScorboard2.get(i).getRj(), i, 9);
			_FunctionUnitsModelScoreboard.setValueAt(_lstFuStatusScorboard2.get(i).getRk(), i, 10);
		}
	}

	private void initScoreboardStudent() {

		_InstructionModelScoreboardStudent = new DefaultTableModel();
		_InstructionModelScoreboardStudent.setColumnIdentifiers(
				new Object[] { "Instruction", " ", "j", "k", "Issue", "Read Opr.", "Exe Com.", "Write Result" });
				// add instruction rows

		// Create the tables
		tblInstructionsScoreStudent = new JTable(_InstructionModelScoreboardStudent);
		tblInstructionsScoreStudent.setRowSelectionAllowed(false);
		// tblInstructionsScoreStudent.setDefaultRenderer(Object.class, new
		// MonCellRenderer());

		/*****************************************/
		// Set Instruction Table Column Widths
		// Column 0 -(Instruction
		tblInstructionsScoreStudent.getColumnModel().getColumn(0).setMinWidth(70);
		tblInstructionsScoreStudent.getColumnModel().getColumn(0).setMaxWidth(70);
		tblInstructionsScoreStudent.getColumnModel().getColumn(0).setPreferredWidth(70);
		// Columns [1,3]
		for (int i = 1; i <= 3; i++) {
			tblInstructionsScoreStudent.getColumnModel().getColumn(i).setMinWidth(30);
			tblInstructionsScoreStudent.getColumnModel().getColumn(i).setMaxWidth(30);
			tblInstructionsScoreStudent.getColumnModel().getColumn(i).setPreferredWidth(30);
		}
		// Columns [4,6]
		for (int i = 4; i <= 7; i++) {
			tblInstructionsScoreStudent.getColumnModel().getColumn(i).setMinWidth(60);
			tblInstructionsScoreStudent.getColumnModel().getColumn(i).setMaxWidth(60);
			tblInstructionsScoreStudent.getColumnModel().getColumn(i).setPreferredWidth(60);
		}

		// Set Table Viewport size for Table JScrollPanes
		tblInstructionsScoreStudent.setPreferredScrollableViewportSize(tblInstructionsScoreStudent.getPreferredSize());

		/*****************************************/
	}

	private void initScoreboard() {

		_InstructionModelScoreboard = new DefaultTableModel();

		_InstructionModelScoreboard.setColumnIdentifiers(
				new Object[] { "Instruction", " ", "j", "k", "Issue", "Read Opr.", "Exe Com.", "Write Result" });
				// add instruction rows
				/*
				 * for( int i = 1; i <= 7; i++){ InstructionModel.addRow( new
				 * Object[]{ " ", " ", " ", " ", " ", " ", " "} ); }
				 */

		/*
		 * private String fu_name; private String busy; private String op;
		 * private String Fi; private String Fj; private String Fk; private
		 * String Qj; private String Qk; private String Rj; private String Rk;
		 * private int exec;
		 */

		// Create The Reservation Station Table Model
		_FunctionUnitsModelScoreboard = new DefaultTableModel();

		_FunctionUnitsModelScoreboard.setColumnIdentifiers(
				new Object[] { "Cycle", "Name", "Busy", "Op", "Fi", "Fj", "Fk", "Qj", "Qk", "Rj", "Rk" });
		// Add Reservation Station Table Rows
		_FunctionUnitsModelScoreboard
				.addRow(new Object[] { "" + 0, "integer", " ", " ", " ", " ", " ", " ", " ", " ", " " });
		_FunctionUnitsModelScoreboard
				.addRow(new Object[] { "" + 0, "mult1", " ", " ", " ", " ", " ", " ", " ", " ", " " });
		_FunctionUnitsModelScoreboard
				.addRow(new Object[] { "" + 0, "mult2", " ", " ", " ", " ", " ", " ", " ", " ", " " });
		_FunctionUnitsModelScoreboard
				.addRow(new Object[] { "" + 0, "add", " ", " ", " ", " ", " ", " ", " ", " ", " " });
		_FunctionUnitsModelScoreboard
				.addRow(new Object[] { "" + 0, "divide", " ", " ", " ", " ", " ", " ", " ", " ", " " });

		_RegistersModelScoreboard = new DefaultTableModel();
		for (int i = 0; i < 14; i++) {
			_RegistersModelScoreboard.addColumn("F" + i);
		}
		// _RegistersModel.addColumn("FP");
		// _RegistersModel.addColumn("Value");

		// Ad Initial Register Table Rows
		// for( int i = 0; i < 14; i++ ){
		_RegistersModelScoreboard
				.addRow(new Object[] { " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " " });
				// }

		// Create the tables
		tblInstructionsScore = new JTable(_InstructionModelScoreboard);

		tblInstructionsScore.setRowSelectionAllowed(false);
		tblFunctionUnitsScore = new JTable(_FunctionUnitsModelScoreboard);
		tblRegistersScore = new JTable(_RegistersModelScoreboard);

		/*****************************************/
		// Set Instruction Table Column Widths
		// Column 0 -(Instruction
		tblInstructionsScore.getColumnModel().getColumn(0).setMinWidth(70);
		tblInstructionsScore.getColumnModel().getColumn(0).setMaxWidth(70);
		tblInstructionsScore.getColumnModel().getColumn(0).setPreferredWidth(70);
		// Columns [1,3]
		for (int i = 1; i <= 3; i++) {
			tblInstructionsScore.getColumnModel().getColumn(i).setMinWidth(30);
			tblInstructionsScore.getColumnModel().getColumn(i).setMaxWidth(30);
			tblInstructionsScore.getColumnModel().getColumn(i).setPreferredWidth(30);
		}
		// Columns [4,6]
		for (int i = 4; i <= 7; i++) {
			tblInstructionsScore.getColumnModel().getColumn(i).setMinWidth(60);
			tblInstructionsScore.getColumnModel().getColumn(i).setMaxWidth(60);
			tblInstructionsScore.getColumnModel().getColumn(i).setPreferredWidth(60);
		}

		// Set the first columnt -- leave default for the remaining columns
		tblFunctionUnitsScore.getColumnModel().getColumn(0).setMinWidth(80);
		tblFunctionUnitsScore.getColumnModel().getColumn(0).setMaxWidth(80);
		tblFunctionUnitsScore.getColumnModel().getColumn(0).setPreferredWidth(80);

		/*
		 * //Set Register Table Column widths //F Name Column
		 * tblRegisters.getColumnModel().getColumn(0).setMinWidth(60);
		 * tblRegisters.getColumnModel().getColumn(0).setMaxWidth(60);
		 * tblRegisters.getColumnModel().getColumn(0).setPreferredWidth(60); //F
		 * Value Column
		 * tblRegisters.getColumnModel().getColumn(1).setMinWidth(100);
		 * tblRegisters.getColumnModel().getColumn(1).setMaxWidth(100);
		 * tblRegisters.getColumnModel().getColumn(1).setPreferredWidth(100);
		 */

		// Set Table Viewport size for Table JScrollPanes
		tblInstructionsScore.setPreferredScrollableViewportSize(tblInstructionsScore.getPreferredSize());
		tblFunctionUnitsScore.setPreferredScrollableViewportSize(tblFunctionUnitsScore.getPreferredSize());
		tblRegistersScore.setPreferredScrollableViewportSize(tblRegistersScore.getPreferredSize());

		/*****************************************/
	}

	// **************************************************************************************
	// ************************************end
	// Scoreboard**************************************************

	/// *************************************************************************************
	/// start Tomasulo
	/// *************************************************************************************
	JScrollPane scrollPaneMemTomasulo;
	JScrollPane scrollPaneRegTomasulo;
	JScrollPane scrollPaneALU;
	JScrollPane scrollPaneInstStateTomasulo;
	JScrollPane scrollPaneInstStateTomasuloStudent;
	JScrollPane scrollPaneDisplayTomasulo;

	JTextArea txtInstructionsTomasulo;
	JLabel lblClockCycleTomasulo;
	JTextArea txtDisplayTomasulo;
	List<tomasulo.Instruction> lstInstructionsTomasulo = new ArrayList<tomasulo.Instruction>();
	int[][] lstAsnswerStudetTomasulo;
	
	private RegisterFiles registersTomasulo;
	private ALUStation[] alu_rsTomasulo;
	private MemStation[] MemReservationTomasulo;

	private HashMap<String, Integer[]> instruction_to_stationTomasulo;
	private HashMap<String, Integer> instruction_to_timeTomasulo;
	private HashMap<String, String> alias_to_registerTomasulo;

	private HashMap<String, Integer> MemoryBufferTomasulo;

	private Clock clockTomasulo;
	private int CurrentInstTomasulo = 0;

	int cuurentOPTomasulo;

	JPanel status_panelTomasulo;

	JTable InstructionTableTomasuloState;
	JTable InstructionTableTomasuloStateStudent;

	JTable ReservationStationTable;
	JTable MemoryTableTomasulo;
	JTable irs_tableTomasulo;
	JTable RegisterTableTomasulo;

	DefaultTableModel InstructionModelTomasulo;
	DefaultTableModel InstructionModelTomasuloStudent;
	DefaultTableModel ReservationStationModelTomasulo;
	DefaultTableModel MemoryModelTomasulo;
	DefaultTableModel irs_modelTomasulo;
	DefaultTableModel RegisterModelTomasulo;

	private JButton btnStepTomasulo;
	private JScrollPane scrollPaneInstTomasulo;

	//// *******************************************************************************************
	private void initTomasulo() {
		status_panelTomasulo = new JPanel();

		InstructionModelTomasulo = new DefaultTableModel();
		InstructionModelTomasulo.setColumnIdentifiers(
				new Object[] { "Instruction", " ", "j", "k", "Issue", "Start-Complete", "Write Result" });


				// add instruction rows
				/*
				 * for( int i = 1; i <= 7; i++){ InstructionModel.addRow( new
				 * Object[]{ " ", " ", " ", " ", " ", " ", " "} ); }
				 */

		// Create The Reservation Station Table Model
		ReservationStationModelTomasulo = new DefaultTableModel();
		ReservationStationModelTomasulo
				.setColumnIdentifiers(new Object[] { "Cycle", "Name", "Busy", "Op", "Vj", "Vk", "Qj", "Qk" });
		// Add Reservation Station Table Rows

		// Create The Reservation Station Table Model
		MemoryModelTomasulo = new DefaultTableModel();
		MemoryModelTomasulo.setColumnIdentifiers(new Object[] { "Name", "Busy", "Address" });
		// Add Reservation Station Table Rows
		Wini ini;
		/* Load the ini file. */
		try {
			ini = new Wini(new File("config/settings.ini"));

			int TomNumInteger = ini.get("tomasulo", "TomNumInteger", int.class);
			int TomNumAddSub = ini.get("tomasulo", "TomNumAddSub", int.class);
			int TomNumMult = ini.get("tomasulo", "TomNumMult", int.class);
			int TomNumDiv = ini.get("tomasulo", "TomNumDiv", int.class);

			int TomNumLoad = ini.get("tomasulo", "TomNumLoad", int.class);
			int TomNumStore = ini.get("tomasulo", "TomNumStore", int.class);

			/*ReservationStationModelTomasulo.addRow(new Object[] { "" + 0, "Int1", " ", " ", " ", " ", " ", " " });
			ReservationStationModelTomasulo.addRow(new Object[] { "" + 0, "Add1", " ", " ", " ", " ", " ", " " });
			ReservationStationModelTomasulo.addRow(new Object[] { "" + 0, "Add2", " ", " ", " ", " ", " ", " " });
			ReservationStationModelTomasulo.addRow(new Object[] { "" + 0, "Mult1", " ", " ", " ", " ", " ", " " });
			ReservationStationModelTomasulo.addRow(new Object[] { "" + 0, "Mult2", " ", " ", " ", " ", " ", " " });
			ReservationStationModelTomasulo.addRow(new Object[] { "" + 0, "Div1", " ", " ", " ", " ", " ", " " });
			ReservationStationModelTomasulo.addRow(new Object[] { "" + 0, "Div2", " ", " ", " ", " ", " ", " " });
			ReservationStationModelTomasulo.addRow(new Object[] { "" + 0, "Div3", " ", " ", " ", " ", " ", " " });
*/
			// Create and intialize Reservation Stations

			for (int i = 0; i < TomNumInteger; i++) {
				ReservationStationModelTomasulo
						.addRow(new Object[] { "" + 0, "Int" + (i + 1), " ", " ", " ", " ", " ", " " });
			}
			for (int i = TomNumInteger; i < TomNumInteger + TomNumAddSub; i++) {
				ReservationStationModelTomasulo
						.addRow(new Object[] { "" + 0, "Add" + (i - TomNumInteger + 1), " ", " ", " ", " ", " ", " " });

			}
			for (int i = TomNumInteger + TomNumAddSub; i < TomNumInteger + TomNumAddSub + TomNumMult; i++) {
				ReservationStationModelTomasulo
.addRow(new Object[] { "" + 0,
						"Mult" + (i - TomNumInteger - TomNumAddSub + 1), " ", " ", " ", " ", " ", " " });

			}
			for (int i = TomNumInteger + TomNumAddSub + TomNumMult; i < TomNumInteger + TomNumAddSub + TomNumMult
					+ TomNumDiv; i++) {
				ReservationStationModelTomasulo
						.addRow(new Object[] { "" + 0, "Div" + (i-TomNumInteger - TomNumAddSub - TomNumMult
 + 1), " ", " ", " ", " ", " ", " " });

			}


		for (int i = 0; i < TomNumLoad; i++) {
				MemoryModelTomasulo.addRow(new Object[] { "Load" + (i + 1), " ", " " });
		}
			for (int i = TomNumLoad; i < TomNumLoad + TomNumStore; i++) {
				MemoryModelTomasulo.addRow(new Object[] { "Store" + (i + 1), " ", " " });
		}

		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * MemoryModelTomasulo.addRow(new Object[] { "Load1", " ", " " });
		 * MemoryModelTomasulo.addRow(new Object[] { "Load2", " ", " " });
		 * MemoryModelTomasulo.addRow(new Object[] { "Store1", " ", " " });
		 * MemoryModelTomasulo.addRow(new Object[] { "Store2", " ", " " });
		 */

		// Create the Register Table Model
		RegisterModelTomasulo = new DefaultTableModel();
		RegisterModelTomasulo.addColumn("FP");
		RegisterModelTomasulo.addColumn("Value");
		RegisterModelTomasulo.addColumn("Int");
		RegisterModelTomasulo.addColumn("Value");

		// Ad Initial Register Table Rows
		for (int i = 0; i < 14; i++) {
			RegisterModelTomasulo.addRow(new Object[] { " ", " ", " ", " " });
		}

		// Create the tables
		InstructionTableTomasuloState = new JTable(InstructionModelTomasulo);
		InstructionTableTomasuloStateStudent = new JTable(InstructionModelTomasuloStudent);
		ReservationStationTable = new JTable(ReservationStationModelTomasulo);
		MemoryTableTomasulo = new JTable(MemoryModelTomasulo);
		RegisterTableTomasulo = new JTable(RegisterModelTomasulo);

		/*****************************************/
		// Set Instruction Table Column Widths
		// Column 0 -(Instruction
		InstructionTableTomasuloState.getColumnModel().getColumn(0).setMinWidth(70);
		InstructionTableTomasuloState.getColumnModel().getColumn(0).setMaxWidth(70);
		InstructionTableTomasuloState.getColumnModel().getColumn(0).setPreferredWidth(70);
		// Columns [1,3]
		for (int i = 1; i <= 3; i++) {
			InstructionTableTomasuloState.getColumnModel().getColumn(i).setMinWidth(30);
			InstructionTableTomasuloState.getColumnModel().getColumn(i).setMaxWidth(30);
			InstructionTableTomasuloState.getColumnModel().getColumn(i).setPreferredWidth(30);
		}
		// Columns [4,6]
		for (int i = 4; i <= 6; i++) {
			InstructionTableTomasuloState.getColumnModel().getColumn(i).setMinWidth(80);
			InstructionTableTomasuloState.getColumnModel().getColumn(i).setMaxWidth(80);
			InstructionTableTomasuloState.getColumnModel().getColumn(i).setPreferredWidth(80);
		}

		// Set Reservation Station Table Column Widths
		// Set the first columnt -- leave default for the remaining columns
		ReservationStationTable.getColumnModel().getColumn(0).setMinWidth(80);
		ReservationStationTable.getColumnModel().getColumn(0).setMaxWidth(80);
		ReservationStationTable.getColumnModel().getColumn(0).setPreferredWidth(80);

		// Set Register Table Column widths
		// FP Name Column
		RegisterTableTomasulo.getColumnModel().getColumn(0).setMinWidth(60);
		RegisterTableTomasulo.getColumnModel().getColumn(0).setMaxWidth(60);
		RegisterTableTomasulo.getColumnModel().getColumn(0).setPreferredWidth(60);
		// FP Value Column
		RegisterTableTomasulo.getColumnModel().getColumn(1).setMinWidth(100);
		RegisterTableTomasulo.getColumnModel().getColumn(1).setMaxWidth(100);
		RegisterTableTomasulo.getColumnModel().getColumn(1).setPreferredWidth(100);
		// Int Name Column
		RegisterTableTomasulo.getColumnModel().getColumn(2).setMinWidth(60);
		RegisterTableTomasulo.getColumnModel().getColumn(2).setMaxWidth(60);
		RegisterTableTomasulo.getColumnModel().getColumn(2).setPreferredWidth(60);
		// Int Value Column
		RegisterTableTomasulo.getColumnModel().getColumn(3).setMinWidth(100);
		RegisterTableTomasulo.getColumnModel().getColumn(3).setMaxWidth(100);
		RegisterTableTomasulo.getColumnModel().getColumn(3).setPreferredWidth(100);

		// Set Table Viewport size for Table JScrollPanes
		InstructionTableTomasuloState
				.setPreferredScrollableViewportSize(InstructionTableTomasuloState.getPreferredSize());
		ReservationStationTable.setPreferredScrollableViewportSize(ReservationStationTable.getPreferredSize());
		MemoryTableTomasulo.setPreferredScrollableViewportSize(MemoryTableTomasulo.getPreferredSize());
		RegisterTableTomasulo.setPreferredScrollableViewportSize(RegisterTableTomasulo.getPreferredSize());
	}

	private void initTomasuloStudent() {


		InstructionModelTomasuloStudent = new DefaultTableModel();
		InstructionModelTomasuloStudent.setColumnIdentifiers(
				new Object[] { "Instruction", " ", "j", "k", "Issue", "Start-Complete", "Write Result" });

		InstructionTableTomasuloStateStudent = new JTable(InstructionModelTomasuloStudent);

		/*****************************************/
		// Set Instruction Table Column Widths
		// Column 0 -(Instruction
		InstructionTableTomasuloStateStudent.getColumnModel().getColumn(0).setMinWidth(70);
		InstructionTableTomasuloStateStudent.getColumnModel().getColumn(0).setMaxWidth(70);
		InstructionTableTomasuloStateStudent.getColumnModel().getColumn(0).setPreferredWidth(70);
		// Columns [1,3]
		for (int i = 1; i <= 3; i++) {
			InstructionTableTomasuloStateStudent.getColumnModel().getColumn(i).setMinWidth(30);
			InstructionTableTomasuloStateStudent.getColumnModel().getColumn(i).setMaxWidth(30);
			InstructionTableTomasuloStateStudent.getColumnModel().getColumn(i).setPreferredWidth(30);
		}
		// Columns [4,6]
		for (int i = 4; i <= 6; i++) {
			InstructionTableTomasuloStateStudent.getColumnModel().getColumn(i).setMinWidth(80);
			InstructionTableTomasuloStateStudent.getColumnModel().getColumn(i).setMaxWidth(80);
			InstructionTableTomasuloStateStudent.getColumnModel().getColumn(i).setPreferredWidth(80);
		}

		// Set Table Viewport size for Table JScrollPanes
		InstructionTableTomasuloStateStudent
				.setPreferredScrollableViewportSize(InstructionTableTomasuloStateStudent.getPreferredSize());

	}

	// **************************************************************************************

	/*
	 * private int abs(int input) { if (input < 0) { input = input * (-1); }
	 * return input; }
	 */

	// **************************************************************************************

	private void Add_DependencyTomasulo(String input) {
		txtDisplayTomasulo.setText(txtDisplayTomasulo.getText() + input + "\n");
		System.out.println(txtDisplayTomasulo.getText() + input);

	}
	// **************************************************************************************

	private void CheckDepencencyTomasulo() {
		// Reset the dependency list.
		txtDisplayTomasulo.setText("");

		// Create a dependency variable
		int dep_exists = 0;
		int data_forwarding = 0;
		int non_forward_delay = 2;

		// i is the current instruction.
		for (int i = 0; i < lstInstructionsTomasulo.size(); i++) {
			// j is one of the previous instructions.
			for (int j = 0; j < i; j++) {

				dep_exists = 0;
				String OPrandI = lstInstructionsTomasulo.get(i).getOper();
				int OPrandCycleI = instruction_to_timeTomasulo.get(OPrandI);

				String OPrandJ = lstInstructionsTomasulo.get(j).getOper();
				int OPrandCycleJ = instruction_to_timeTomasulo.get(OPrandJ);

				// *** RAW ***
				if ((lstInstructionsTomasulo.get(j).getDes().equals(lstInstructionsTomasulo.get(i).getS1()))
						|| (lstInstructionsTomasulo.get(j).getDes().equals(lstInstructionsTomasulo.get(i).getS2()))) {

					if ((lstInstructionsTomasulo.get(j).getOper().toLowerCase().equals("ld"))
							&& (data_forwarding == 1)) {

						if ((i - j) < OPrandCycleJ + non_forward_delay + 1) {
							dep_exists = 1;
							Add_DependencyTomasulo(("RAW: Instructions " + j + " and " + i + ".  Register "
									+ lstInstructionsTomasulo.get(j).getDes() + "."));
						}
					} else {

						if ((i - j) < OPrandCycleJ + non_forward_delay) {
							dep_exists = 1;
							Add_DependencyTomasulo(("RAW: Instructions " + j + " and " + i + ".  Register "
									+ lstInstructionsTomasulo.get(j).getDes() + "."));
						}
					}
				}

				// *** WAR ***
				if (((lstInstructionsTomasulo.get(j).getS1().equals(lstInstructionsTomasulo.get(i).getDes()))
						|| (lstInstructionsTomasulo.get(j).getS2().equals(lstInstructionsTomasulo.get(i).getDes())))) {
					Add_DependencyTomasulo(("WAR: Instructions " + j + " and " + i + ".  Register "
							+ lstInstructionsTomasulo.get(i).getDes() + "."));
				}

				// *** WAW ***
				if ((lstInstructionsTomasulo.get(i).getDes().equals(lstInstructionsTomasulo.get(j).getDes()))) {
					if (!lstInstructionsTomasulo.get(j).getDes().equals("null")) {
						Add_DependencyTomasulo(("WAW: Instructions " + j + " and " + i + ".  Register "
								+ lstInstructionsTomasulo.get(j).getDes() + "."));
					}
				}

				// *** Test for Structural Hazard ***
				// If two instructions use the same functional unit and the
				// difference between the two instructions is less than the
				// execution cycles of the earlier instruction.
				/*
				 * if
				 * ((instruction_array.get(i).operator.functional_unit.equals(
				 * instruction_array.get(j).operator.functional_unit)) && ((i -
				 * j) < OPrandCycleJ)) { Add_Dependency(
				 * "Structural: Instructions " + j + " and " + i + ".  Unit: " +
				 * lstInstructions.get(j).operator.display_value); }
				 */

				// *** simultaneous MEM/WB ***
				if ((dep_exists == 0) && ((i - j) == (OPrandCycleJ - OPrandCycleI))) {
					Add_DependencyTomasulo(
							"Instructions " + j + " and " + i + " will enter the MEM and WB stage at the same time.");
				}

			}
		} // End of outer for loop.

		// If no dependencies were found, output a message.
		if (txtDisplayTomasulo.getText().equals("")) {
			txtDisplayTomasulo.setText("No Hazards Found.");
		}
	}
	// **************************************************************************************

	///
	/// Initialize the simulation
	///
	public void initVariables() throws Exception {

		clockTomasulo = clockTomasulo.getInstance();
		clockTomasulo.ResetClock();
		// OperationFileParser file_parser = new OperationFileParser( data_file
		// );

		// Iniatialize containers for instructions and registers
		// operations = new OperationList();
		registersTomasulo = new RegisterFiles();

		// file_parser.parseFile( operations );

		/*
		 * // Create and intialize Reservation Stations alu_rsTomasulo = new
		 * ALUStation[8];
		 * 
		 * 
		 * alu_rsTomasulo[0] = new ALUStation("Int1"); alu_rsTomasulo[1] = new
		 * ALUStation("Add1"); alu_rsTomasulo[2] = new ALUStation("Add2");
		 * alu_rsTomasulo[3] = new ALUStation("Mul1"); alu_rsTomasulo[4] = new
		 * ALUStation("Mul2"); alu_rsTomasulo[5] = new ALUStation("Div1");
		 * alu_rsTomasulo[6] = new ALUStation("Div2"); alu_rsTomasulo[7] = new
		 * ALUStation("Div3");
		 */
		/*
		 * MemReservationTomasulo[0] = new MemStation("Load1");
		 * MemReservationTomasulo[1] = new MemStation("Load2");
		 * MemReservationTomasulo[2] = new MemStation("Store1");
		 * MemReservationTomasulo[3] = new MemStation("Store2");
		 */

		instruction_to_stationTomasulo = new HashMap<String, Integer[]>();
		// Memory Indices
		instruction_to_stationTomasulo.put("LD", new Integer[] { 0, 1 });
		instruction_to_stationTomasulo.put("SD", new Integer[] { 2, 3 });

		/*
		 * // ALU Indices instruction_to_stationTomasulo.put("DADDI", new
		 * Integer[] { 0 }); instruction_to_stationTomasulo.put("ADDD", new
		 * Integer[] { 1, 2 }); instruction_to_stationTomasulo.put("SUBD", new
		 * Integer[] { 1, 2 }); instruction_to_stationTomasulo.put("MULD", new
		 * Integer[] { 3, 4, 5 }); instruction_to_stationTomasulo.put("DIVD",
		 * new Integer[] { 6, 7, 8, 9 });
		 */

		try {
			/*
			 * [pipeline] PipeFPAddSub=1 PipeFPMult=1 PipeFPDivide=1
			 * PipeIntDivide=1 [scorboard] ScoreInteger=1 ScoreAddSub=2
			 * ScoreMult=10 ScoreDivide=40 [tomasulo] TomInteger=2 TomAddI=1
			 * TomAddSub=4 TomMult=7 TomDivide=25
			 */
			Wini ini;
			/* Load the ini file. */
			ini = new Wini(new File("config/settings.ini"));

			int TomInteger = ini.get("tomasulo", "TomInteger", int.class);
			int TomAddI = ini.get("tomasulo", "TomAddI", int.class);
			int TomAddSub = ini.get("tomasulo", "TomAddSub", int.class);
			int TomMult = ini.get("tomasulo", "TomMult", int.class);
			int TomDivide = ini.get("tomasulo", "TomDivide", int.class);

			int TomNumInteger = ini.get("tomasulo", "TomNumInteger", int.class);
			int TomNumAddSub = ini.get("tomasulo", "TomNumAddSub", int.class);
			int TomNumMult = ini.get("tomasulo", "TomNumMult", int.class);
			int TomNumDiv = ini.get("tomasulo", "TomNumDiv", int.class);
			int TomNumLoad = ini.get("tomasulo", "TomNumLoad", int.class);
			int TomNumStore = ini.get("tomasulo", "TomNumStore", int.class);

			Integer[] arrayTomNumInteger;
			Integer[] arrayTomNumAddSub;
			Integer[] arrayTomNumMult;
			Integer[] arrayTomNumDiv;

			// Create a mapping of instructions to execution time
			instruction_to_timeTomasulo = new HashMap<String, Integer>();
			// Memory Instructions
			instruction_to_timeTomasulo.put("LD", new Integer(TomInteger));
			instruction_to_timeTomasulo.put("SD", new Integer(TomInteger));

			// ALU Instructions
			instruction_to_timeTomasulo.put("DADDI", new Integer(TomAddI));
			instruction_to_timeTomasulo.put("DADD", new Integer(TomAddSub));
			instruction_to_timeTomasulo.put("ADDD", new Integer(TomAddSub));
			instruction_to_timeTomasulo.put("SUBD", new Integer(TomAddSub));
			instruction_to_timeTomasulo.put("MULD", new Integer(TomMult));
			instruction_to_timeTomasulo.put("DIVD", new Integer(TomDivide));

			// Create and intialize Reservation Stations
			alu_rsTomasulo = new ALUStation[TomNumInteger + TomNumAddSub + TomNumMult + TomNumDiv];

			// ALU Indices
			arrayTomNumInteger = new Integer[TomNumInteger];
			for (int i = 0; i < TomNumInteger; i++) {
				alu_rsTomasulo[i] = new ALUStation("Int" + (i + 1));
				arrayTomNumInteger[i] = i;
			}
			if (arrayTomNumInteger.length > 0)
				instruction_to_stationTomasulo.put("DADDI", arrayTomNumInteger);

			arrayTomNumAddSub = new Integer[TomNumAddSub];
			for (int i = TomNumInteger; i < TomNumInteger + TomNumAddSub; i++) {
				alu_rsTomasulo[i] = new ALUStation("Add" + (i - TomNumInteger + 1));
				arrayTomNumAddSub[i - TomNumInteger] = i;
			}
			if (arrayTomNumAddSub.length > 0) {
				instruction_to_stationTomasulo.put("ADDD", arrayTomNumAddSub);
				instruction_to_stationTomasulo.put("SUBD", arrayTomNumAddSub);
			}

			arrayTomNumMult = new Integer[TomNumMult];
			for (int i = TomNumInteger + TomNumAddSub; i < TomNumInteger + TomNumAddSub + TomNumMult; i++) {
				alu_rsTomasulo[i] = new ALUStation("Mul" + (i - TomNumInteger - TomNumAddSub + 1));
				arrayTomNumMult[i - TomNumInteger - TomNumAddSub] = i;
			}
			if (arrayTomNumMult.length > 0)
				instruction_to_stationTomasulo.put("MULD", arrayTomNumMult);

			arrayTomNumDiv = new Integer[TomNumDiv];
			for (int i = TomNumInteger + TomNumAddSub + TomNumMult; i < TomNumInteger + TomNumAddSub + TomNumMult
					+ TomNumDiv; i++) {
				alu_rsTomasulo[i] = new ALUStation("Div" + (i - TomNumInteger - TomNumAddSub - TomNumMult + 1));
				arrayTomNumDiv[i - TomNumInteger - TomNumAddSub - TomNumMult] = i;
			}
			if (arrayTomNumDiv.length > 0)
				instruction_to_stationTomasulo.put("DIVD", arrayTomNumDiv);

			/*
			 * alu_rsTomasulo[0] = new ALUStation("Int1"); alu_rsTomasulo[1] =
			 * new ALUStation("Add1"); alu_rsTomasulo[2] = new
			 * ALUStation("Add2"); alu_rsTomasulo[3] = new ALUStation("Mul1");
			 * alu_rsTomasulo[4] = new ALUStation("Mul2"); alu_rsTomasulo[5] =
			 * new ALUStation("Div1"); alu_rsTomasulo[6] = new
			 * ALUStation("Div2"); alu_rsTomasulo[7] = new ALUStation("Div3");
			 */

			// array contain of number of load andstore units
			MemReservationTomasulo = new MemStation[TomNumLoad + TomNumStore];
			for (int i = 0; i < TomNumLoad; i++) {
				MemReservationTomasulo[i] = new MemStation("Load" + (i + 1));

			}
			for (int i = TomNumLoad; i < TomNumLoad + TomNumStore; i++) {
				MemReservationTomasulo[i] = new MemStation("Store" + (i - TomNumLoad + 1));

			}
			// MemReservationTomasulo[0] = new MemStation("Load1");
			// MemReservationTomasulo[1] = new MemStation("Load2");
			// MemReservationTomasulo[2] = new MemStation("Store1");
			// MemReservationTomasulo[3] = new MemStation("Store2");

		} catch (InvalidFileFormatException e) {
			System.out.println("Invalid file format.");
		} catch (IOException e) {
			System.out.println("Problem reading file.");
		}

		// Mapping of aliases to Registers
		alias_to_registerTomasulo = new HashMap<String, String>();

		// Initalize the Memory Buffer
		MemoryBufferTomasulo = new HashMap<String, Integer>();

		CurrentInstTomasulo = 1;

	}

	///
	/// Returns true when the simulation has finished
	///
	public boolean isComplete() {
		boolean complete = false;
		complete = !(hasInstInQueue());

		for (int i = 0; i < MemReservationTomasulo.length && complete; i++) {
			complete = !MemReservationTomasulo[i].isBusy();
		}

		for (int i = 0; i < alu_rsTomasulo.length && complete; i++) {
			complete = !alu_rsTomasulo[i].isBusy();
		}

		return complete;
	}

	///
	/// Returns true when there is at least one Operation that hs not been
	/// scheduled.
	/// Returns false if there is not an Operation to schedule.
	///
	public boolean hasInstInQueue() {
		return CurrentInstTomasulo <= lstInstructionsTomasulo.size();
	}

	public void ExecuteInstruction() {
		tomasulo.Instruction to_schedule;
		boolean op_scheduled = false;

		// increase the clock
		clockTomasulo.AddClock();

		// Broadcast data(Results) over Common Data BUS Reservation Station
		for (ALUStation it : alu_rsTomasulo) {

			if (it.isResultReady()) {
				it.finalizeResult();
				CDB(it.getName(), it.getResult());
			}
			if (it.isResultWritten()) {
				it.clear();
			}
		}
		for (MemStation it : MemReservationTomasulo) {
			if (it.isResultReady()) {
				CDB(it.getName(), it.getResult());
			}
			if (it.isResultWritten()) {
				// System.out.prtinln
				it.clear();
			}
		}

		/*
		 * // Broadcast data(Results) over Common Data BUS Reservation Station
		 * for (ALUStation it : alu_rsTomasulo) {
		 * 
		 * if (it.isResultReady()) { it.finalizeResult(); CDB(it.getName(),
		 * it.getResult()); } if (it.isResultWritten()) { it.clear(); } } for
		 * (MemStation it : MemReservationTomasulo) { if (it.isResultReady()) {
		 * CDB(it.getName(), it.getResult()); } if (it.isResultWritten()) { //
		 * System.out.prtinln it.clear(); } }
		 */
		// Update Reservation Stations -- Execute Cycle Instruction
		for (ALUStation it : alu_rsTomasulo) {
			System.out.println(
"alu_rs: " + it.getCycle() + " " + it.getName() + " " + it.getOperation() + " "
					+ it.getVj() + " " + it.getVk() + " " + it.getQj() + " " + it.getQk());
			if (it.isReady() && it.isBusy()) {
				it.ExecuteCycleInstruction();
			}
		}
		for (MemStation it : MemReservationTomasulo) {
			if (it.isBusy() && it.hasPriority(MemoryBufferTomasulo) && it.isReady()) {
				it.ExecuteCycleInstruction();
			}

		}

		// Get an instruction from the head of the list
		// if the list has not been exhausted
		if (hasInstInQueue()) {
			to_schedule = NextInstruction();

			Integer[] rs_indices = instruction_to_stationTomasulo.get(to_schedule.getOper());
			// Memory Stations
			if (IsLDorSD(to_schedule.getOper())) {
				for (int i = rs_indices[0]; i <= rs_indices[(rs_indices.length - 1)] && !op_scheduled; i++) {
					if (!MemReservationTomasulo[i].isBusy()) {
						MemReservationTomasulo[i].scheduleInstruction(to_schedule, registersTomasulo, 2);
						op_scheduled = true;

						// Set the placeholder in the Register Files
						// if the instruction is not a store
						if (!MemStation.isStore(to_schedule.getOper())) {
							registersTomasulo.setRegister(to_schedule.getDes(), MemReservationTomasulo[i].getName());
							alias_to_registerTomasulo.put(MemReservationTomasulo[i].getName(), to_schedule.getDes());

						}
						MemReservationTomasulo[i].hasPriority(MemoryBufferTomasulo);
					}
				}
			}
			// ALU Stations
			else {
				for (int i = rs_indices[0]; i <= rs_indices[(rs_indices.length - 1)] && !op_scheduled; i++) {
					if (!alu_rsTomasulo[i].isBusy()) {
						alu_rsTomasulo[i].scheduleInstruction(to_schedule, registersTomasulo,
								instruction_to_timeTomasulo.get(to_schedule.getOper()));
						op_scheduled = true;

						registersTomasulo.setRegister(to_schedule.getDes(), alu_rsTomasulo[i].getName());
						alias_to_registerTomasulo.put(alu_rsTomasulo[i].getName(), to_schedule.getDes());
					}
				}
			}

			if (op_scheduled) {
				CurrentInstTomasulo++;
			}
		}

		lblClockCycleTomasulo.setText(Integer.toString(clockTomasulo.getClock()));

	}

	public tomasulo.Instruction NextInstruction() {
		return lstInstructionsTomasulo.get(CurrentInstTomasulo - 1);
	}

	private boolean IsLDorSD(String oprand) {
		return (oprand.equals("LD") || oprand.equals("SD"));

	}

	/**
	 * Common data BUS
	 * 
	 * @param alias
	 * @param result
	 */
	private void CDB(String alias, String result) {
		String register; // the register to update
		System.out.println("CDB = alias :" + alias + " --- result:" + result);
		// remove the mapping from the memory buffer
		if (MemoryBufferTomasulo.containsKey(result)) {
			MemoryBufferTomasulo.remove(result);
		}

		// broadcast to all Reservation Stations
		for (ALUStation it : alu_rsTomasulo) {
			if (it.getQj().equals(alias)) {
				it.setVj(result);
				it.setQj(null);
				System.out.println("set Vj:" + result);
				it.setNotStart(true);
			}
			if (it.getQk().equals(alias)) {
				it.setVk(result);
				it.setQk(null);
				System.out.println("set Vk:" + result);
				it.setNotStart(true);
			}

		}

		for (MemStation it : MemReservationTomasulo) {
			if (it.isBusy()) {
				it.updateAddrComponents(alias, result);
			}
		}

		// Update the Registers
		if (alias_to_registerTomasulo.containsKey(alias)) {

			// ger the register that is mapped to the alias
			register = alias_to_registerTomasulo.get(alias);

			// overwrite the current register value if the station name matched
			// the alias
			if (alias.equals(registersTomasulo.getRegister(register))) {
				registersTomasulo.setRegister(register, result);
			}
			alias_to_registerTomasulo.remove(alias);
		}

	}

	public tomasulo.Instruction getLastInstruction() {
		return lstInstructionsTomasulo.get(lstInstructionsTomasulo.size() - 1);
	}

	private void updateInstructionTableTomasuloStudent() {
		int i = 0;
		for (tomasulo.Instruction instruction : lstInstructionsTomasulo) {
			int writeTime = instruction.getWriteTime();
			// new Object[]{"Instruction"," ", "j", "k", "Issue", "Complete",
			// "Write"}
			// InstructionModel.addRow(new
			// Object[]{"","","","","","","","",""});
			InstructionModelTomasuloStudent.setValueAt(instruction.getOper(), i, 0);

			InstructionModelTomasuloStudent.setValueAt(instruction.getDes(), i, 1);
			InstructionModelTomasuloStudent.setValueAt(instruction.getS1(), i, 2);
			InstructionModelTomasuloStudent.setValueAt(instruction.getS2(), i, 3);
			InstructionModelTomasuloStudent.setValueAt("0", i, 4);
			InstructionModelTomasuloStudent.setValueAt("0", i, 5);
			InstructionModelTomasuloStudent.setValueAt("0", i, 6);

			i++;
		}

	}

	private void updateInstructionTableTomasulo() {
		int i = 0;
		for (tomasulo.Instruction instruction : lstInstructionsTomasulo) {
			int writeTime = instruction.getWriteTime();
			// new Object[]{"Instruction"," ", "j", "k", "Issue", "Complete",
			// "Write"}
			// InstructionModel.addRow(new
			// Object[]{"","","","","","","","",""});
			InstructionModelTomasulo.setValueAt(instruction.getOper(), i, 0);

			InstructionModelTomasulo.setValueAt(instruction.getDes(), i, 1);
			InstructionModelTomasulo.setValueAt(instruction.getS1(), i, 2);
			InstructionModelTomasulo.setValueAt(instruction.getS2(), i, 3);
			InstructionModelTomasulo.setValueAt((instruction.isScheduled() ? instruction.getIssueNum() : " "), i, 4);
			InstructionModelTomasulo.setValueAt(instruction.getExecution(), i, 5);
			InstructionModelTomasulo.setValueAt((writeTime == -1 ? " " : writeTime), i, 6);

			i++;
		}

		/*
		 * OperationList update_list = sim_instance.getOperationList();
		 * 
		 * //Populate Operations table with the instructions for( int i = 0; i <
		 * update_list.getNumberOfOperations(); i++){ Operation it_op =
		 * update_list.getOperation( i+1 ); //obtain the write time int
		 * write_time = it_op.getWriteTime();
		 * 
		 * //Opcode op_model.setValueAt( it_op.getOpcode(), i, 0); //Operands
		 * op_model.setValueAt( it_op.getOperand(1), i, 1); op_model.setValueAt(
		 * it_op.getOperand(2), i, 2);
		 * 
		 * if( it_op.getNumberOfOperands() > 2 ){ op_model.setValueAt(
		 * it_op.getOperand(3), i, 3); }
		 * 
		 * op_model.setValueAt( it_op.getExecution(), i, 5);
		 * op_model.setValueAt( (write_time == -1 ? " " : write_time ), i, 6);
		 * 
		 * //Issue Number - display once the instruction has been scheduled
		 * op_model.setValueAt( (it_op.isScheduled() ? it_op.getIssueNum() : " "
		 * ), i, 4); }
		 */

	}

	///
	/// Register Table Update Helper
	///
	private void updateRegisterTableTomasulo() {
		int temp_it_index = 0; // tempory index tracker

		// Get the Integer Registers
		Map<String, String> temp_int_reg = registersTomasulo.getIntegerRegisters();
		// Get the FP Registers
		Map<String, String> temp_fp_reg = registersTomasulo.getFPRegisters();

		// add rows if needed
		while ((temp_int_reg.size() > RegisterModelTomasulo.getRowCount())
				|| (temp_fp_reg.size() > RegisterModelTomasulo.getRowCount())) {
			RegisterModelTomasulo.addRow(new Object[] { " ", " ", " ", " " });
		}

		// Update Integer Table Values
		for (Map.Entry<String, String> int_entry : temp_int_reg.entrySet()) {
			RegisterModelTomasulo.setValueAt(int_entry.getKey(), temp_it_index, 2);
			RegisterModelTomasulo.setValueAt(int_entry.getValue(), temp_it_index, 3);

			temp_it_index++;
		}

		temp_it_index = 0;
		// Update Integer Table Values
		for (Map.Entry<String, String> fp_entry : temp_fp_reg.entrySet()) {
			RegisterModelTomasulo.setValueAt(fp_entry.getKey(), temp_it_index, 0);
			RegisterModelTomasulo.setValueAt(fp_entry.getValue(), temp_it_index, 1);

			temp_it_index++;
		}
	}

	///
	/// MemStations Table Update Helper
	///
	private void updateMemTableTomasulo() {
		// Get a copy of the memory stations
		MemStation[] temp_ms = MemReservationTomasulo;

		// Update the table with current values for the stations
		for (int i = 0; i < temp_ms.length; i++) {
			// generate a meaningfull representation of busy
			String busy_desc = (temp_ms[i].isBusy() ? "Yes" : "No");

			MemoryModelTomasulo.setValueAt(temp_ms[i].getName(), i, 0);
			MemoryModelTomasulo.setValueAt(busy_desc, i, 1);
			MemoryModelTomasulo.setValueAt(temp_ms[i].getAddress(), i, 2);
		}
	}

	///
	/// ALUStations Table Update Helper
	///
	private void updateALUTableTomasulo() {
		// Get a copy of the memory stations
		ALUStation[] temp_alu = alu_rsTomasulo;

		// Update the table with current values for the stations
		for (int i = 0; i < temp_alu.length; i++) {
			// generate a meaningfull representation of busy
			String busy_desc = (temp_alu[i].isBusy() ? "Yes" : "No");

			ReservationStationModelTomasulo
					.setValueAt(((temp_alu[i].isReady() && temp_alu[i].isBusy()) ? temp_alu[i].getCycle() : "0"), i, 0);

			ReservationStationModelTomasulo.setValueAt(temp_alu[i].getName(), i, 1);
			ReservationStationModelTomasulo.setValueAt(busy_desc, i, 2);
			ReservationStationModelTomasulo.setValueAt(((temp_alu[i].isBusy()) ? temp_alu[i].getOperation() : " "), i,
					3);
			ReservationStationModelTomasulo.setValueAt(temp_alu[i].getVj(), i, 4);
			ReservationStationModelTomasulo.setValueAt(temp_alu[i].getVk(), i, 5);
			ReservationStationModelTomasulo.setValueAt(temp_alu[i].getQj(), i, 6);
			ReservationStationModelTomasulo.setValueAt(temp_alu[i].getQk(), i, 7);
		}
	}

	/// *************************************************************************************
	/// end tomasulo
	/// *************************************************************************************

	/// *************************************************************************************
	/// start pipeline
	/// *************************************************************************************
	private HashMap<String, String> mapAnswerStudent;
	private HashMap<String, String> mapAnswer;
	JScrollPane scrollPaneScorInstStateStudent;
	JScrollPane scrollPaneScorInstState;
	JScrollPane scrollPaneInstScore;
	JScrollPane scrollPaneScorFuncUnit;
	JScrollPane scrollPaneScorRegUnit;


	JCheckBox chckbxForwardingPipeline;

	JTextArea txtDisplayPipeline;
	JScrollPane scrollPaneStudent;
	int instructionArraySize = 50;
	int NumInstructions = 0;
	String strInsLbl = "";
	int row_x = 150;
	int x = 150;
	int y = 60;
	int w = 40;
	int h = 60;
	int stall;
	int numberOfRows = 0;
	int numberOfColumns = 0;
	// Start with the current cycle being 0.
	int currentCycle = 0;
	int dataForwarding = 0;
	int nonForwardDelay = 2;
	Color col_IF = new Color(255, 218, 185);
	Color col_ID = new Color(255, 192, 203);
	Color col_EXE = new Color(255, 69, 0);
	Color col_MEM = new Color(152, 251, 152);
	Color col_WB = new Color(100, 149, 237);
	Color col_Stall = new Color(216, 191, 216);
	JScrollPane scrollpane;
	JPanel panelShowResult;


	// Create all of the operators.
	Operator OPfpMult = new Operator("muld", 1, "fp_mult_unit", "f *");
	Operator OPfpAdd = new Operator("addd", 1, "fp_add_sub_unit", "f +|-");
	Operator OPfpSub = new Operator("subd", 1, "fp_add_sub_unit", "f +|-");
	Operator OPfpDiv = new Operator("divd", 1, "fp_div_unit", "f /");
	Operator OPfpLd = new Operator("ld", 1, "int_unit", "EX");
	Operator OPfpSd = new Operator("sd", 1, "int_unit", "EX");
	Operator OPintMult = new Operator("muldi", 1, "int_mult_unit", "i *");
	Operator OPintAdd = new Operator("daddi", 1, "int_unit", "i +|-");
	Operator OPintSub = new Operator("subdi", 1, "int_unit", "i +|-");
	Operator OPintDiv = new Operator("divdi", 1, "int_div_unit", "i /");
	Operator OPintLd = new Operator("ldi", 1, "int_unit", "EX");
	Operator OPintSd = new Operator("sdi", 1, "int_unit", "EX");
	Operator OPbrTaken = new Operator("br_taken", 1, "br_add", " ");
	Operator OPbrUntaken = new Operator("br_untaken", 1, "br_add", " ");

	// Create the instruction array.
	List<PipeLine.Instruction> lstInstructionsPipeLine = new ArrayList<PipeLine.Instruction>();
	List<JTextField> listOfTextField = new ArrayList<JTextField>();
	int wrongAttemptPipe=0;
	
	private JButton btnStep;
	private JButton btnExeAll;
	private JMenuItem mntmClockCycle;
	private JButton btnCompareScore;
	private JButton btnProfessor;
	private JPanel panelShowResultStudent;
	private JMenuItem menuItem;
	private JMenuItem mntmAbout;
	private JTextField txtStudentEmail;
	private JTextField txtStudentNo;
	private JButton btnCancelsend;
	private JButton btnSendScore;
	// **************************************************************************************

	// **************************************************************************************
	private void Display_Stall(int row, int col) {

		System.out.println("STALL row=" + row + " col=" + col);
		JPanel pnl_Stall_tmp = new JPanel();
		pnl_Stall_tmp.setBackground(col_Stall);
		pnl_Stall_tmp.setBounds(col * 40 + (50 + 100), row * 20 + (20), 40, 20);// (w+row_x)
																				// currentCycle*40+(40+100),
																				// x*20+20
		panelShowResult.add(pnl_Stall_tmp);
		JLabel lblStall_tmp = new JLabel("Stall");
		pnl_Stall_tmp.add(lblStall_tmp);
		panelShowResult.revalidate();
		panelShowResult.repaint();
		mapAnswer.put("txt_" + col + "_" + row, "Stall");/// k , v
	}

	private void Step() {
		// Do not do anything if we have already gone through all of the cycles.
		if (numberOfColumns == currentCycle) {
			return;
		}
		if (numberOfRows != 1) {

			stall = 0;
			x = 0;

			while (x < numberOfRows) {
				if (lstInstructionsPipeLine.get(x).issue_cycle == null) {
					break;
				}
				if (stall == 1) {
					Display_Stall(x, currentCycle);
				} else {
					switch (lstInstructionsPipeLine.get(x).pipeline_stage) {

					case "IF":
						lstInstructionsPipeLine.get(x).pipeline_stage = "ID";
						break;

					case "ID":
						for (int i = (x - 1); i >= 0; i--) {

							if (lstInstructionsPipeLine.get(i).pipeline_stage.equals("EX")
									&& lstInstructionsPipeLine.get(i).operator.functional_unit
											.equals(lstInstructionsPipeLine.get(x).operator.functional_unit)) {
								// *** Structural Hazard ***
								stall = 1;
								break;
							}

							else if ((lstInstructionsPipeLine.get(i).destination_register
									.equals(lstInstructionsPipeLine.get(x).source_register1)
									|| lstInstructionsPipeLine.get(i).destination_register
											.equals(lstInstructionsPipeLine.get(x).source_register2))) {
								if (dataForwarding == 1) {

									if (lstInstructionsPipeLine.get(i).operator.name.equals("ld")
											|| lstInstructionsPipeLine.get(i).operator.name.equals("sd")) {
										if (!lstInstructionsPipeLine.get(i).pipeline_stage.equals("WB")
												&& !lstInstructionsPipeLine.get(i).pipeline_stage.equals(" ")) {
											// ** RAW Hazard **
											stall = 1;
											break;
										}
									}

									else {
										if (!lstInstructionsPipeLine.get(i).pipeline_stage.equals("MEM")
												&& !lstInstructionsPipeLine.get(i).pipeline_stage.equals("WB")
												&& !lstInstructionsPipeLine.get(i).pipeline_stage.equals(" ")) {
											// *** RAW Hazard **
											stall = 1;
											break;
										}
									}
								}

								else if (dataForwarding == 0) {
									// If forwarding is disabled and the
									// previous instruction is not completed,
									// stall.
									if (!lstInstructionsPipeLine.get(i).pipeline_stage.equals(" ")) {
										// *** RAW Hazard **
										stall = 1;
										break;
									}
								}
							}

							else if ((lstInstructionsPipeLine.get(i).destination_register
									.equals(lstInstructionsPipeLine.get(x).destination_register))
									&& (!lstInstructionsPipeLine.get(i).destination_register.equals("null"))) {

								if ((lstInstructionsPipeLine.get(i).pipeline_stage.equals("EX"))
										&& ((lstInstructionsPipeLine.get(i).operator.execution_cycles
												- lstInstructionsPipeLine
														.get(i).execute_counter) >= (lstInstructionsPipeLine
																.get(x).operator.execution_cycles - 1))) {
									// *** WAW Hazard ***
									stall = 1;
									break;
								}

							}

							else if ((lstInstructionsPipeLine.get(i).pipeline_stage.equals("EX"))
									&& ((lstInstructionsPipeLine.get(i).operator.execution_cycles
											- lstInstructionsPipeLine
													.get(i).execute_counter) == (lstInstructionsPipeLine
															.get(x).operator.execution_cycles - 1))) {

								// *** WB will happen at the same time ***
								stall = 1;
								break;
							}
						}

						if (stall != 1) {
							if (lstInstructionsPipeLine.get(x).operator.name == "br_taken") {
								lstInstructionsPipeLine.get(x).pipeline_stage = " ";
								lstInstructionsPipeLine.get(x).execute_counter++;
								if ((x + 1) < numberOfRows) {
									lstInstructionsPipeLine.get(x + 1).pipeline_stage = " ";
								}

							}
							// Complete the execution of the branch.
							else if (lstInstructionsPipeLine.get(x).operator.name == "br_untaken") {
								lstInstructionsPipeLine.get(x).pipeline_stage = " ";
								lstInstructionsPipeLine.get(x).execute_counter++;
							}
							// Move the instruction into the EX stage.
							else {
								lstInstructionsPipeLine.get(x).pipeline_stage = "EX";
								lstInstructionsPipeLine.get(x).execute_counter++;
							}
						}

						break;

					case "EX":
						if (lstInstructionsPipeLine
								.get(x).execute_counter < lstInstructionsPipeLine.get(x).operator.execution_cycles) {
							lstInstructionsPipeLine.get(x).pipeline_stage = "EX";
							lstInstructionsPipeLine.get(x).execute_counter++;
						} else {
							lstInstructionsPipeLine.get(x).pipeline_stage = "MEM";
						}
						break;

					case "MEM":
						lstInstructionsPipeLine.get(x).pipeline_stage = "WB";
						break;

					case "WB":
						lstInstructionsPipeLine.get(x).pipeline_stage = " ";
						break;

					case " ":
						lstInstructionsPipeLine.get(x).pipeline_stage = " ";
						break;

					default:

						JOptionPane.showMessageDialog(new JFrame(), "Unrecognized Pipeline Stage!", "Dialog",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					if (stall == 1) {
						Display_Stall(x, currentCycle);
					} else if (lstInstructionsPipeLine.get(x).pipeline_stage == "EX") {
						System.out.println(lstInstructionsPipeLine.get(x).operator.display_value + " - row=" + x
								+ " col=" + currentCycle);
						JPanel pnl_EXE_tmp = new JPanel();
						pnl_EXE_tmp.setBackground(col_EXE);
						pnl_EXE_tmp.setBounds(currentCycle * 40 + (50 + 100), x * 20 + 20, 40, 20);// (w+row_x)
						panelShowResult.add(pnl_EXE_tmp);
						JLabel lblExe_tmp = new JLabel(lstInstructionsPipeLine.get(x).operator.display_value);
						pnl_EXE_tmp.add(lblExe_tmp);
						panelShowResult.revalidate();
						panelShowResult.repaint();

						mapAnswer.put("txt_" + currentCycle + "_" + x,"EX");/// k,v
					}
					// Output the pipeline stage.
					else {
						JPanel pnl_tmp = new JPanel();
						switch (lstInstructionsPipeLine.get(x).pipeline_stage) {
						case "ID":
							pnl_tmp.setBackground(col_ID);
							break;
						case "MEM":
							pnl_tmp.setBackground(col_MEM);
							break;
						case "WB":
							pnl_tmp.setBackground(col_WB);
							break;
						default:
							break;
						}
						// pnl_tmp.setBackground(col_EXE);
						pnl_tmp.setBounds(currentCycle * 40 + (50 + 100), x * 20 + 20, 40, 20);// (w+row_x)
						panelShowResult.add(pnl_tmp);
						JLabel lbl_tmp = new JLabel(lstInstructionsPipeLine.get(x).pipeline_stage);
						pnl_tmp.add(lbl_tmp);
						panelShowResult.revalidate();
						panelShowResult.repaint();

						mapAnswer.put("txt_" + currentCycle + "_" + x, lstInstructionsPipeLine.get(x).pipeline_stage);/// k
																														/// ,
																														/// v

						System.out.println(
								lstInstructionsPipeLine.get(x).pipeline_stage + " - row=" + x + " col=" + currentCycle);
					}
					;
				}
				System.out.println("x:" + x);
				x++;
			} // End of while loop

			if (stall != 1 && x < numberOfRows) {
				// Issue a new instruction.
				lstInstructionsPipeLine.get(x).issue_cycle = Integer.toString(currentCycle);
				lstInstructionsPipeLine.get(x).pipeline_stage = "IF";
				JPanel pnl_IF_tmp = new JPanel();
				pnl_IF_tmp.setBackground(col_IF);
				pnl_IF_tmp.setBounds(currentCycle * 40 + (50 + 100), x * 20 + 20, 40, 20);// (w+row_x)
				panelShowResult.add(pnl_IF_tmp);
				JLabel lblIF_tmp = new JLabel(lstInstructionsPipeLine.get(x).pipeline_stage);
				pnl_IF_tmp.add(lblIF_tmp);
				panelShowResult.revalidate();
				panelShowResult.repaint();
				mapAnswer.put("txt_" + currentCycle + "_" + x, lstInstructionsPipeLine.get(x).pipeline_stage);/// k
																												/// ,v

				System.out.println(
						lstInstructionsPipeLine.get(x).pipeline_stage + " - row=" + x + " col=" + currentCycle);
			}

			System.out.println("Clk:" + currentCycle);
			currentCycle++;
		} else {

			if (currentCycle == 0) {
				lstInstructionsPipeLine.get(0).issue_cycle = Integer.toString(currentCycle);
				lstInstructionsPipeLine.get(0).pipeline_stage = "IF";
				JPanel pnl_IF_tmp = new JPanel();
				pnl_IF_tmp.setBackground(col_IF);
				pnl_IF_tmp.setBounds(x * 40 + x, currentCycle * 20 + (20), 40, 20);// (w+row_x)
				panelShowResult.add(pnl_IF_tmp);
				JLabel lblIF_tmp = new JLabel(lstInstructionsPipeLine.get(0).getPipeline_stage());
				pnl_IF_tmp.add(lblIF_tmp);
				panelShowResult.revalidate();
				panelShowResult.repaint();
				mapAnswer.put("txt_" + currentCycle + "_" + x, lstInstructionsPipeLine.get(0).pipeline_stage);/// k
																												/// ,
																												/// v

				System.out.println(
						lstInstructionsPipeLine.get(0).pipeline_stage + " - row=" + x + " col=" + currentCycle);
				System.out.println("Clk:" + currentCycle);
				currentCycle++;
			} else {
				switch (lstInstructionsPipeLine.get(0).pipeline_stage) {
				case "IF":
					lstInstructionsPipeLine.get(0).pipeline_stage = "ID";
					break;

				case "ID":

					// If branch is taken, complete this instruction.
					if (lstInstructionsPipeLine.get(0).operator.name.substring(0, 2) == "br") {
						lstInstructionsPipeLine.get(0).pipeline_stage = " ";
						lstInstructionsPipeLine.get(0).execute_counter++;
					}
					// Move the instruction into the EX stage.
					else {
						lstInstructionsPipeLine.get(0).pipeline_stage = "EX";
						lstInstructionsPipeLine.get(0).execute_counter++;
					}

					break;

				case "EX":
					// If the instruction hasn't completed.
					if (lstInstructionsPipeLine
							.get(0).execute_counter < lstInstructionsPipeLine.get(0).operator.execution_cycles) {
						lstInstructionsPipeLine.get(0).pipeline_stage = "EX";
						lstInstructionsPipeLine.get(0).execute_counter++;
					}
					// Move the instruction to the MEM stage.
					else {
						lstInstructionsPipeLine.get(0).pipeline_stage = "MEM";
					}
					break;

				case "MEM":
					lstInstructionsPipeLine.get(0).pipeline_stage = "WB";
					break;

				case "WB":
					lstInstructionsPipeLine.get(0).pipeline_stage = " ";
					break;

				case " ":
					lstInstructionsPipeLine.get(0).pipeline_stage = " ";
					break;

				default:

					JOptionPane.showMessageDialog(new JFrame(), "Unrecognized Pipeline Stage!", "Dialog",
							JOptionPane.ERROR_MESSAGE);
				}

				// If the instruction is in the EX stage, display the functional
				// unit.
				if (lstInstructionsPipeLine.get(0).pipeline_stage == "EX") {
					JPanel pnl_IF_tmp = new JPanel();
					pnl_IF_tmp.setBackground(col_EXE);
					pnl_IF_tmp.setBounds(x * 40 + x, currentCycle * 20 + (20), 40, 20);// (w+row_x)
					panelShowResult.add(pnl_IF_tmp);
					JLabel lblIF_tmp = new JLabel(lstInstructionsPipeLine.get(0).pipeline_stage);
					pnl_IF_tmp.add(lblIF_tmp);
					panelShowResult.revalidate();
					panelShowResult.repaint();
					mapAnswer.put("txt_" + currentCycle + "_" + x,"EX");/// k
																					/// ,v

					System.out.println(lstInstructionsPipeLine.get(0).operator.display_value + " - row=" + x + " col="
							+ currentCycle);
				} else {
					JPanel pnl_IF_tmp = new JPanel();
					switch (lstInstructionsPipeLine.get(0).pipeline_stage) {
					case "ID":
						pnl_IF_tmp.setBackground(col_ID);
						break;
					case "MEM":
						pnl_IF_tmp.setBackground(col_MEM);
						break;
					case "WB":
						pnl_IF_tmp.setBackground(col_WB);
						break;
					default:
						break;
					}

					// pnl_IF_tmp.setBackground(col_IF);
					pnl_IF_tmp.setBounds(x * 40 + x, currentCycle * 20 + (20), 40, 20);// (w+row_x)
					panelShowResult.add(pnl_IF_tmp);
					JLabel lblIF_tmp = new JLabel(lstInstructionsPipeLine.get(0).pipeline_stage);
					pnl_IF_tmp.add(lblIF_tmp);
					panelShowResult.revalidate();
					panelShowResult.repaint();
					mapAnswer.put("txt_" + currentCycle + "_" + x, lstInstructionsPipeLine.get(x).pipeline_stage);/// k
																													/// ,
																													/// v

					// s = "document.instruction_table.column" + currentCycle +
					// ".value =
					// parent.top_frame.lstInstructions[0].pipeline_stage;";
					System.out.println(
							lstInstructionsPipeLine.get(0).pipeline_stage + " - row=" + x + " col=" + currentCycle);
				}
				// eval(s);
				System.out.println("Clk:" + currentCycle);
				currentCycle++;
			}
		}
	}

	// **************************************************************************************

	/*
	 * private int abs(int input) { if (input < 0) { input = input * (-1); }
	 * return input; }
	 */
	// **************************************************************************************

	private void Add_Dependency(String input) {
		txtDisplayPipeline.setText(txtDisplayPipeline.getText() + input + "\n");
		System.out.println(txtDisplayPipeline.getText() + input);

	}
	// **************************************************************************************

	private void CheckDepencencyPipeline() {
		// Reset the dependency list.
		txtDisplayPipeline.setText("");

		// Create a dependency variable
		int dep_exists = 0;

		// i is the current instruction.
		for (int i = 0; i < lstInstructionsPipeLine.size(); i++) {
			// j is one of the previous instructions.
			for (int j = 0; j < i; j++) {

				dep_exists = 0;

				// *** RAW ***
				if ((lstInstructionsPipeLine.get(j).destination_register.equals(lstInstructionsPipeLine.get(i).source_register1))
						|| (lstInstructionsPipeLine.get(j).destination_register.equals(lstInstructionsPipeLine.get(i).source_register2))) {
					if ((lstInstructionsPipeLine.get(j).operator.name.equals("ld")
							|| lstInstructionsPipeLine.get(j).operator.name.equals("ldi"))
							&& (dataForwarding == 1)) {
						if ((i - j) < lstInstructionsPipeLine.get(j).operator.execution_cycles + nonForwardDelay + 1) {
							dep_exists = 1;
							Add_Dependency(("RAW: Instructions " + j + " and " + i + ".  Register "
									+ lstInstructionsPipeLine.get(j).destination_register + "."));
						}
					} else {
						if ((i - j) < lstInstructionsPipeLine.get(j).operator.execution_cycles + nonForwardDelay) {
							dep_exists = 1;
							Add_Dependency(("RAW: Instructions " + j + " and " + i + ".  Register "
									+ lstInstructionsPipeLine.get(j).destination_register + "."));
						}
					}
				}

				// *** WAR ***
				if (((lstInstructionsPipeLine.get(j).source_register1.equals(lstInstructionsPipeLine.get(i).destination_register))
						|| (lstInstructionsPipeLine.get(j).source_register2.equals(lstInstructionsPipeLine.get(i).destination_register)))
						&& (i - j) < abs(lstInstructionsPipeLine.get(j).operator.execution_cycles - lstInstructionsPipeLine.get(i).operator.execution_cycles)) {
					Add_Dependency(("WAR: Instructions " + j + " and " + i + ".  Register "+ lstInstructionsPipeLine.get(i).destination_register + "."));
				}

				// ***WAW ***
				if ((lstInstructionsPipeLine.get(i).destination_register.equals(lstInstructionsPipeLine.get(j).destination_register))
						&& ((i - j) <= abs(lstInstructionsPipeLine.get(j).operator.execution_cycles - lstInstructionsPipeLine.get(i).operator.execution_cycles))) {
					if (!lstInstructionsPipeLine.get(j).destination_register.equals("null")) {
						Add_Dependency(("WAW: Instructions " + j + " and " + i + ".  Register "+ lstInstructionsPipeLine.get(j).destination_register + "."));
					}
				}

				// *** Structural Hazard ***
				if ((lstInstructionsPipeLine.get(i).operator.functional_unit.equals(lstInstructionsPipeLine.get(j).operator.functional_unit))
						&& ((i - j) < lstInstructionsPipeLine.get(j).operator.execution_cycles)) {
					Add_Dependency("Structural: Instructions " + j + " and " + i + ".  Unit: "+ lstInstructionsPipeLine.get(j).operator.display_value);
				}

				// *** simultaneous MEM/WB ***
				if ((dep_exists == 0) && ((i - j) == (lstInstructionsPipeLine.get(j).operator.execution_cycles - lstInstructionsPipeLine.get(i).operator.execution_cycles))) {
					Add_Dependency("Instructions " + j + " and " + i + " will enter the MEM and WB stage at the same time.");
				}
			}
		} // End of outer for loop.

		// If no dependencies were found, output a message.
		if (txtDisplayPipeline.getText().equals("")) {
			txtDisplayPipeline.setText("No Hazards Found.");
		}
	}
	// **************************************************************************************

	private void BuildPipeline() {
		panelShowResult.removeAll();

		Dimension dimPanel = new Dimension(numberOfColumns * 40 + 200, NumInstructions * 25);
		panelShowResult.setPreferredSize(dimPanel);

		for (int c = 0; c < numberOfColumns; c++) {
			int num = c + 1;
			JLabel lblNewLabel = new JLabel("C#" + num + "\t");
			lblNewLabel.setBackground(col_ID);

			lblNewLabel.setBounds(40 * (c) + 152, 0, 39, 20);
			panelShowResult.add(lblNewLabel);
		}

		for (int i = 0; i < lstInstructionsPipeLine.size(); i++) {
			strInsLbl = "";
			if (lstInstructionsPipeLine.get(i).destination_register == "null") {
				if (lstInstructionsPipeLine.get(i).operator.name == "sd"
						|| lstInstructionsPipeLine.get(i).operator.name == "sdi") {
					strInsLbl = lstInstructionsPipeLine.get(i).operator.name + " ("
							+ lstInstructionsPipeLine.get(i).source_register1 + ", Offset, "
							+ lstInstructionsPipeLine.get(i).source_register2 + ") "
							+ lstInstructionsPipeLine.get(i).operator.execution_cycles;
				} else {
					strInsLbl = lstInstructionsPipeLine.get(i).operator.name + " ("
							+ lstInstructionsPipeLine.get(i).source_register1 + ", "
							+ lstInstructionsPipeLine.get(i).source_register2 + ") "
							+ lstInstructionsPipeLine.get(i).operator.execution_cycles;
				}
			} else {
				strInsLbl = lstInstructionsPipeLine.get(i).operator.name + " ("
						+ lstInstructionsPipeLine.get(i).destination_register + ", "
						+ lstInstructionsPipeLine.get(i).source_register1 + ", "
						+ lstInstructionsPipeLine.get(i).source_register2 + ") "
						+ lstInstructionsPipeLine.get(i).operator.execution_cycles;
			}

			int num = i;
			// String ins= codeList.get(i);
			JLabel lblNewLabel = new JLabel("I#" + num + " : " + strInsLbl.toUpperCase() + "\t");
			lblNewLabel.setBounds(5, 20 * (i + 1), 150, 20);
			panelShowResult.add(lblNewLabel);
			panelShowResult.revalidate();
			panelShowResult.repaint();
		}

	}

	private void BuildPipelineStudent() {
		panelShowResultStudent.removeAll();
		mapAnswerStudent = new HashMap<String, String>();
		mapAnswer = new HashMap<String, String>();
		Dimension dimPanel = new Dimension(numberOfColumns * 40 + 200, NumInstructions * 25);
		panelShowResultStudent.setPreferredSize(dimPanel);

		for (int c = 0; c < numberOfColumns; c++) {
			int num = c + 1;
			JLabel lblNewLabel = new JLabel("C#" + num + "\t");
			lblNewLabel.setBackground(col_ID);

			lblNewLabel.setBounds(40 * (c) + 152, 0, 39, 20);
			panelShowResultStudent.add(lblNewLabel);

			for (int row = 0; row < NumInstructions; row++) {
			JTextField txt_tmp = new JTextField();
				txt_tmp.setBounds(c * 40 + (50 + 100), row * 20 + 20, 40, 20);// c:=
																				// rows
																				// and
																				// row:=columns
				// txt_tmp.setText(c + "_" + row);
				txt_tmp.setName("txt_" + c + "_" + row);
				txt_tmp.addKeyListener(new KeyAdapter() {
					public void keyReleased(KeyEvent e) {
						JTextField textField = (JTextField) e.getSource();
						String text = textField.getText().toUpperCase();
						textField.setText(text);

						mapAnswerStudent.put(textField.getName(), text);
						String strAnswer = mapAnswer.get(textField.getName());
						String strAnswerStudent = mapAnswerStudent.get(textField.getName());
						if (!strAnswer.toLowerCase().equals(strAnswerStudent.toLowerCase())) {
							textField.setBackground(Color.pink);
							wrongAttemptPipe++;
						} else {
							textField.setBackground(Color.white);
						}

					}

					public void keyTyped(KeyEvent e) {
						// TODO: Do something for the keyTyped event
					}

					public void keyPressed(KeyEvent e) {
						// TODO: Do something for the keyPressed event
					}
				});
			panelShowResultStudent.add(txt_tmp);
				listOfTextField.add(txt_tmp);
				
				mapAnswerStudent.put(txt_tmp.getName(), " ");/// k , v
				mapAnswer.put(txt_tmp.getName(), " ");/// k , v
			}
		}

		for (int i = 0; i < lstInstructionsPipeLine.size(); i++) {
			strInsLbl = "";
			if (lstInstructionsPipeLine.get(i).destination_register == "null") {
				if (lstInstructionsPipeLine.get(i).operator.name == "sd"
						|| lstInstructionsPipeLine.get(i).operator.name == "sdi") {
					strInsLbl = lstInstructionsPipeLine.get(i).operator.name + " ("
							+ lstInstructionsPipeLine.get(i).source_register1 + ", Offset, "
							+ lstInstructionsPipeLine.get(i).source_register2 + ") "
							+ lstInstructionsPipeLine.get(i).operator.execution_cycles;
				} else {
					strInsLbl = lstInstructionsPipeLine.get(i).operator.name + " ("
							+ lstInstructionsPipeLine.get(i).source_register1 + ", "
							+ lstInstructionsPipeLine.get(i).source_register2 + ") "
							+ lstInstructionsPipeLine.get(i).operator.execution_cycles;
				}
			} else {
				strInsLbl = lstInstructionsPipeLine.get(i).operator.name + " ("
						+ lstInstructionsPipeLine.get(i).destination_register + ", "
						+ lstInstructionsPipeLine.get(i).source_register1 + ", "
						+ lstInstructionsPipeLine.get(i).source_register2 + ") "
						+ lstInstructionsPipeLine.get(i).operator.execution_cycles;
			}

			int num = i;
			// String ins= codeList.get(i);
			JLabel lblNewLabel = new JLabel("I#" + num + " : " + strInsLbl.toUpperCase() + "\t");
			lblNewLabel.setBounds(5, 20 * (i + 1), 150, 20);
			panelShowResultStudent.add(lblNewLabel);
			panelShowResultStudent.revalidate();
			panelShowResultStudent.repaint();



		}

	}

	// **************************************************************************************

	private void UpdateDisplay() {
		if (NumInstructions > 0) {
			CheckDepencencyPipeline();
			BuildPipeline();
			BuildPipelineStudent();
		}

	}


	/// *************************************************************************************
	/// end pipeline
	/// *************************************************************************************

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
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
	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1139, 753);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnMode = new JMenu("Mode");
		menuBar.add(mnMode);

		JMenuItem mntmStudent = new JMenuItem("Student");
		mntmStudent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		mnMode.add(mntmStudent);

		mntmProfessor = new JMenuItem("Professor");
		mntmProfessor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panelPipeline.setVisible(false);
				panelScoreboard.setVisible(false);
				panelTomasulo.setVisible(false);
				pnlLogin.setVisible(false);
wrongAttemptPipe=0;
				pnlLogin.setVisible(true);
				pnlLogin.setBounds(10, 10, 240, 120);
				setBounds(10, 10, 275, 200);

			}
		});
		mnMode.add(mntmProfessor);

		JMenu mnMethod = new JMenu("Method");
		menuBar.add(mnMethod);

		JMenuItem mntmPipeline = new JMenuItem("Pipeline");
		mntmPipeline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelPipeline.setVisible(true);
				panelScoreboard.setVisible(false);
				panelTomasulo.setVisible(false);
				pnlLogin.setVisible(false);
				panelPipeline.setBounds(10, 0, 856, 536);
				setBounds(10, 10, 896, 610);
				scrollPaneStudent.setVisible(false);
				wrongAttemptPipe=0;
			}
		});
		mnMethod.add(mntmPipeline);

		JMenuItem mntmScoreboard = new JMenuItem("Scoreboard");
		mntmScoreboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelPipeline.setVisible(false);
				panelScoreboard.setVisible(true);
				panelTomasulo.setVisible(false);
				pnlLogin.setVisible(false);

				panelScoreboard.setBounds(10, 10, 750, 500);
				setBounds(10, 30, 790, 590);
				wrongAttemptPipe=0;
				if (bolMode) {
					scrollPaneScorFuncUnit.setVisible(true);
					scrollPaneScorRegUnit.setVisible(true);
					scrollPaneScorInstStateStudent.setVisible(false);
					scrollPaneScorInstStateStudent.setBounds(275, 277, 455, 166);
					scrollPaneScorInstState.setBounds(275, 104, 455, 166);
					btnExecAllScore.setBounds(127, 29, 89, 23);
					btnCompareScore.setVisible(false);
				} else {
					scrollPaneScorFuncUnit.setVisible(false);
					scrollPaneScorRegUnit.setVisible(false);
					scrollPaneScorInstStateStudent.setVisible(true);
					scrollPaneScorInstState.setBounds(275, 277, 455, 166);
					scrollPaneScorInstStateStudent.setBounds(275, 104, 455, 166);
					btnExecAllScore.setBounds(90, 280, 89, 23);
					btnCompareScore.setBounds(90, 310, 89, 23);

				}
			}
		});
		mnMethod.add(mntmScoreboard);

		JMenuItem mntmTomasulo = new JMenuItem("Tomasulo");
		mntmTomasulo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelPipeline.setVisible(false);
				panelScoreboard.setVisible(false);
				panelTomasulo.setVisible(true);
				pnlLogin.setVisible(false);
				wrongAttemptPipe=0;
				panelTomasulo.setBounds(10, 0, 800, 660);
				setBounds(10, 30, 835, 740);
				if (bolMode) {
					scrollPaneMemTomasulo.setVisible(true);
					scrollPaneRegTomasulo.setVisible(true);
					scrollPaneALU.setVisible(true);
					scrollPaneInstStateTomasulo.setVisible(true);
					scrollPaneInstStateTomasuloStudent.setVisible(false);
					scrollPaneInstStateTomasuloStudent.setBounds(456, 189, 411, 177);

					scrollPaneInstStateTomasulo.setBounds(371, 3, 411, 177);
					scrollPaneDisplayTomasulo.setBounds(10, 289, 440, 202);
					// scrollPaneDisplayTomasulo.setVisible(false);
				} else {
					scrollPaneMemTomasulo.setVisible(false);
					scrollPaneRegTomasulo.setVisible(false);
					scrollPaneALU.setVisible(false);
					scrollPaneInstStateTomasuloStudent.setVisible(true);
					scrollPaneInstStateTomasuloStudent.setBounds(10, 189, 411, 177);

					scrollPaneInstStateTomasulo.setVisible(true);
					scrollPaneInstStateTomasulo.setBounds(10, 380, 411, 177);
					// scrollPaneDisplayTomasulo.setVisible(false);
					scrollPaneDisplayTomasulo.setVisible(true);
					scrollPaneDisplayTomasulo.setBounds(371, 3, 411, 177);
				}

			}
		});
		mnMethod.add(mntmTomasulo);

		JMenu mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);

		mntmClockCycle = new JMenuItem("Clock Cycle");
		mntmClockCycle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SettingClockCycle frame = new SettingClockCycle();
			}
		});
		mnSettings.add(mntmClockCycle);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmHelp = new JMenuItem("Help");
		mntmHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Help frame = null;
				if (panelPipeline.isVisible())
					frame = new Help("HelpPipeline");
				else if (panelScoreboard.isVisible())
					frame = new Help("HelpScoreboard");
				else if (panelTomasulo.isVisible())
					frame = new Help("HelpTomasulo");
				frame.setVisible(true);
			}
		});
		mnHelp.add(mntmHelp);

		mnHelp.add(new JSeparator());

		mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				About frameAbout = new About();
			}
		});
		mnHelp.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		initScoreboard();
		initScoreboardStudent();
		
		 panelSendEmail = new JPanel();
		 panelSendEmail.setBounds(450, 40, 310, 135);
		 contentPane.add(panelSendEmail);
		 panelSendEmail.setVisible(false);
		 panelSendEmail.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		 panelSendEmail.setBackground(new Color(230, 230, 250));
		 panelSendEmail.setLayout(null);
		 
		 txtStudentEmail = new JTextField();
		 txtStudentEmail.setBounds(84, 11, 205, 20);
		 panelSendEmail.add(txtStudentEmail);
		 txtStudentEmail.setColumns(10);
		 
		 txtStudentNo = new JTextField();
		 txtStudentNo.setBounds(84, 42, 205, 20);
		 panelSendEmail.add(txtStudentNo);
		 txtStudentNo.setColumns(10);
		 
		 JButton btnNewButton = new JButton("Send answer");
		 btnNewButton.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent arg0) {
		 		StringBuilder sb=new StringBuilder();
		 		if(panelPipeline.isVisible())
		 		{
		 			int from=0,to=0;
		 			
		 			
		 			sb.append("<!DOCTYPE html><html><head><title>Answer Pipeline</title></head><body>");
		 			sb.append("<table style=\"width:300px;\">");
		 			sb.append("<tr><td>Student No.</td><td>"+txtStudentNo.getText()+"</td></tr>");
		 			sb.append("<tr><td>Email</td><td>"+txtStudentEmail.getText()+"</td></tr>");
		 			sb.append("</table>");
		 			sb.append("<table style=\"width:100%;border:1px solid black;\">");
		 			sb.append("<tr><th>Instructions&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>");
		 			for (int i = 0; i < numberOfColumns; i++) {
		 				sb.append("<th >C#"+(i+1)+"</th>");
		 			}
		 			sb.append("</tr>");
		 			
		 			
		 			for (int i = 0; i < numberOfRows; i++) {
		 				sb.append("<tr>");
		 				strInsLbl = "";
		 				if (lstInstructionsPipeLine.get(i).destination_register == "null") {
		 					if (lstInstructionsPipeLine.get(i).operator.name == "sd"
		 							|| lstInstructionsPipeLine.get(i).operator.name == "sdi") {
		 						strInsLbl = lstInstructionsPipeLine.get(i).operator.name + " ("
		 								+ lstInstructionsPipeLine.get(i).source_register1 + ", Offset, "
		 								+ lstInstructionsPipeLine.get(i).source_register2 + ") "
		 								+ lstInstructionsPipeLine.get(i).operator.execution_cycles;
		 					} else {
		 						strInsLbl = lstInstructionsPipeLine.get(i).operator.name + " ("
		 								+ lstInstructionsPipeLine.get(i).source_register1 + ", "
		 								+ lstInstructionsPipeLine.get(i).source_register2 + ") "
		 								+ lstInstructionsPipeLine.get(i).operator.execution_cycles;
		 					}
		 				} else {
		 					strInsLbl = lstInstructionsPipeLine.get(i).operator.name + " ("
		 							+ lstInstructionsPipeLine.get(i).destination_register + ", "
		 							+ lstInstructionsPipeLine.get(i).source_register1 + ", "
		 							+ lstInstructionsPipeLine.get(i).source_register2 + ") "
		 							+ lstInstructionsPipeLine.get(i).operator.execution_cycles;
		 				}

		 				int num = i;
		 				sb.append("<td  style=\"width:100%;border:1px solid black;\">I#" + num + " : " + strInsLbl.toUpperCase() + "</td>");
		 				to+=numberOfColumns;
		 				int select=0+i;
		 				int col=0;
		 				for (int j = from; j < to; j++) {
		 					select= col*numberOfRows+i;
		 					JTextField txttemp=new JTextField();
		 					txttemp=listOfTextField.get(select);
		 					sb.append("<td  style=\"width:100%;border:1px solid black;\">" + txttemp.getText() + "</td>");
		 					col++;
		 				}
		 				from+=numberOfColumns;
		 			    sb.append("</tr>");
		 			}
		 			
		 			
		 			
		 	/*		 <tr>
		 			    <td>Eve</td>
		 			     <td>Jackson</td> 
		 			    <td>94</td>
		 			   </tr>
	*/
		 			sb.append("</table>");
		 			sb.append("<h4>Number Of  wrong attempt: "+wrongAttemptPipe+"</h4>");
		 			sb.append("</body></html> ");
		 			
		 			
		 		}
		 		else if(panelScoreboard.isVisible())
		 		{

		 			
		 			sb.append("<!DOCTYPE html><html><head><title>Answer Pipeline</title></head><body>");
		 			sb.append("<table style=\"width:300px;\">");
		 			sb.append("<tr><td>Student No.</td><td>"+txtStudentNo.getText()+"</td></tr>");
		 			sb.append("<tr><td>Email</td><td>"+txtStudentEmail.getText()+"</td></tr>");
		 			sb.append("</table>");
		 			sb.append("<table style=\"width:600px;border:1px solid black;\">");
		 			sb.append("<tr><th>Instructions</th><th ></th><th >J</th><th >K</th><th >Issue</th><th >Read Oper.</th><th >Exe Com.</th><th >Write Res.</th>");
		 			for (int i = 0; i < numberOfColumns; i++) {
		 				sb.append("<th >C#"+(i+1)+"</th>");
		 			}
		 			sb.append("</tr>");
		 			
		 			
		 			//tblInstructionsScoreStudent
		 			for (int i = 0; i < tblInstructionsScoreStudent.getRowCount(); i++) {
						for (int j = 0; j <tblInstructionsScoreStudent.getColumnCount(); j++) {
						System.out.println(tblInstructionsScoreStudent.getValueAt(i, j));	
						}
						
						sb.append("<tr>");
		 				
		 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + tblInstructionsScoreStudent.getValueAt(i, 0) + "</td>");
		 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + tblInstructionsScoreStudent.getValueAt(i, 1) + "</td>");
		 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + tblInstructionsScoreStudent.getValueAt(i, 2) + "</td>");
		 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + tblInstructionsScoreStudent.getValueAt(i, 3) + "</td>");
		 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + tblInstructionsScoreStudent.getValueAt(i, 4) + "</td>");
		 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + tblInstructionsScoreStudent.getValueAt(i, 5) + "</td>");
		 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + tblInstructionsScoreStudent.getValueAt(i, 6) + "</td>");
		 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + tblInstructionsScoreStudent.getValueAt(i, 7) + "</td>");
		 				 sb.append("</tr>");

					}
		 			
		
		 			
		 			sb.append("</table>");
		 			//sb.append("<h4>Number Of  wrong attempt: "+wrongAttemptPipe+"</h4>");
		 			sb.append("</body></html> ");
		 		}
		 		else if(panelTomasulo.isVisible())
		 		{
		 			
		 			sb.append("<!DOCTYPE html><html><head><title>Answer Pipeline</title></head><body>");
		 			sb.append("<table style=\"width:300px;\">");
		 			sb.append("<tr><td>Student No.</td><td>"+txtStudentNo.getText()+"</td></tr>");
		 			sb.append("<tr><td>Email</td><td>"+txtStudentEmail.getText()+"</td></tr>");
		 			sb.append("</table>");
		 			sb.append("<table style=\"border:1px solid black;\">");
		 			sb.append("<tr><th>Instructions</th><th ></th><th >J</th><th >K</th><th >Issue</th><th >Start-Complete</th><th >Write Res.</th>");
		 			for (int i = 0; i < numberOfColumns; i++) {
		 				sb.append("<th >C#"+(i+1)+"</th>");
		 			}
		 			sb.append("</tr>");
		 			
		 			for (int i = 0; i < InstructionTableTomasuloStateStudent.getRowCount(); i++) {
						for (int j = 0; j <InstructionTableTomasuloStateStudent.getColumnCount(); j++) {
						System.out.println(InstructionTableTomasuloStateStudent.getValueAt(i, j));	
						}
						
						sb.append("<tr>");
		 				
		 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + InstructionTableTomasuloStateStudent.getValueAt(i, 0) + "</td>");
		 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + InstructionTableTomasuloStateStudent.getValueAt(i, 1) + "</td>");
		 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + InstructionTableTomasuloStateStudent.getValueAt(i, 2) + "</td>");
		 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + InstructionTableTomasuloStateStudent.getValueAt(i, 3) + "</td>");
		 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + InstructionTableTomasuloStateStudent.getValueAt(i, 4) + "</td>");
		 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + InstructionTableTomasuloStateStudent.getValueAt(i, 5) + "</td>");
		 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + InstructionTableTomasuloStateStudent.getValueAt(i, 6) + "</td>");
		 				
		 				 sb.append("</tr>");

					}
		 			
		 		
		 			
		 			
		 			sb.append("</table>");
		 		//	sb.append("<h4>Number Of  wrong attempt: "+wrongAttemptPipe+"</h4>");
		 			sb.append("</body></html> ");
		 			
		 		}
		 		// TODO here at the next should be change to dynamic email to receiver 
		 		MailSender ssender=new MailSender();
		 		ssender.sendEmail("hesamacatest@gmail.com","Answer ACA",sb.toString());
		 	}
		 });
		 btnNewButton.setBounds(140, 90, 131, 23);
		 panelSendEmail.add(btnNewButton);
		 
		 JLabel lblNewLabel_2 = new JLabel("Email:");
		 lblNewLabel_2.setBounds(22, 14, 63, 14);
		 panelSendEmail.add(lblNewLabel_2);
		 
		 JLabel lblNewLabel_3 = new JLabel("Student N.:");
		 lblNewLabel_3.setBounds(22, 45, 63, 14);
		 panelSendEmail.add(lblNewLabel_3);
		 
		 btnCancelsend = new JButton("Cancel Send");
		 btnCancelsend.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent e) {
		 		panelSendEmail.setVisible(false);
		 	}
		 });
		 btnCancelsend.setBounds(24, 90, 106, 23);
		 panelSendEmail.add(btnCancelsend);
		///// ******************************pipeline******************************************

		panelPipeline = new JPanel();
		panelPipeline.setBackground(new Color(204, 255, 255));
		panelPipeline.setBounds(10, 0, 856, 536);
		contentPane.add(panelPipeline);
		panelPipeline.setLayout(null);



		chckbxForwardingPipeline = new JCheckBox("Forwarding");
		chckbxForwardingPipeline.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					dataForwarding = 1;
					// Data is forwarded so there is no additional delay.
					nonForwardDelay = 0;
				} else {
					dataForwarding = 0;
					nonForwardDelay = 2;
				}
				;

			}
		});

		chckbxForwardingPipeline.setBounds(261, 143, 97, 23);
		panelPipeline.add(chckbxForwardingPipeline);

		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				instructionArraySize = 50;
				NumInstructions = 0;
				strInsLbl = "";

				stall = 0;
				numberOfRows = 0;
				numberOfColumns = 0;
				// Start with the current cycle being 0.
				currentCycle = 0;
				dataForwarding = 0;
				nonForwardDelay = 2;

				lstInstructionsPipeLine = new ArrayList<PipeLine.Instruction>();// =
																				// new
																				// Instruction(lstInstructions_size);

				txtDisplayPipeline.setText("");
				panelShowResult.removeAll();
				panelShowResult.repaint();
				panelShowResult.revalidate();
			}
		});
		btnReset.setBounds(261, 90, 89, 23);
		panelPipeline.add(btnReset);

		// LoadFPRegisters(cmbSourceRegister1);
		// LoadFPRegisters(cmbSourceRegister2);
		// LoadFPRegisters(cmbdestination_register);

		// LoadExecutionTime(cmbfpAddSubNew);
		// LoadExecutionTime(cmbfpMultNew);
		// LoadExecutionTime(cmbfpDivNew);
		// LoadExecutionTime(cmbintDivNew);
		// LoadOperstors(cmboperator);

		btnStep = new JButton("Step");
		btnStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Step();
			}
		});
		btnStep.setBounds(261, 39, 89, 23);
		panelPipeline.add(btnStep);

		btnExeAll = new JButton("EXE All");
		btnExeAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentCycle = 0;
				for (int i = 0; i < numberOfColumns; i++) {
					Step();
				}
			}
		});
		btnExeAll.setBounds(261, 66, 89, 23);
		panelPipeline.add(btnExeAll);

		JScrollPane scrollPaneDisplayPipeline = new JScrollPane();
		scrollPaneDisplayPipeline.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneDisplayPipeline.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPaneDisplayPipeline.setBounds(397, 11, 427, 145);
		panelPipeline.add(scrollPaneDisplayPipeline);

		txtDisplayPipeline = new JTextArea();
		scrollPaneDisplayPipeline.setViewportView(txtDisplayPipeline);

		JScrollPane scrollPanePipeline = new JScrollPane();
		scrollPanePipeline.setBounds(10, 11, 216, 145);
		panelPipeline.add(scrollPanePipeline);

		JTextArea txtInstructionsPipeline = new JTextArea();
		txtInstructionsPipeline.setText(
				"LD\tF6, 34, R2\r\nLD\tF2, 45, R3\r\nMULD\tF0, F2, F4\r\nSUBD\tF8, F6, F2\r\nDIVD\tF10, F0, F6\r\nADDD\tF6, F8, F2");
		scrollPanePipeline.setViewportView(txtInstructionsPipeline);

		JButton btnLoadPipeline = new JButton("Load");
		btnLoadPipeline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO load pipeline


				for (JTextField f : listOfTextField) {
					System.out.println(f.getText());
				}
				Operator currentop = null;
				String[] split_1, split_2, operands;
				String op_portion, comment_portion = "", des = "", s1 = "", s2 = "";
				String operation;
				int issue = 0;
				lstInstructionsPipeLine = new ArrayList<PipeLine.Instruction>();

				for (String line : txtInstructionsPipeline.getText().split("\\n")) {
					// trim line
					line = line.trim();

					// only process non-empty lines
					if (line != null && !line.equals("")) {
						// increment the issue_number
						issue++;

						split_1 = new String[1];
						split_1[0] = line;

						op_portion = line;

						// Split/Prse the operation & operands
						split_2 = (split_1[0].trim()).split("\\s+");
						operation = split_2[0].trim();

						operands = new String[(split_2.length - 1)];

						for (int i = 1; i < split_2.length; i++) {
							// temporary index
							int temp_index = split_2[i].indexOf(",");
							// check for a comma
							temp_index = (temp_index == -1 ? split_2[i].length() : temp_index);
							if (i == 1)
								des = split_2[i].substring(0, temp_index);
							else if (i == 2)
								s1 = split_2[i].substring(0, temp_index);
							else if (i == 3)
								s2 = split_2[i].substring(0, temp_index);
						}

						//////////////////////////////////////////////////////////////////

						switch (operation.toLowerCase()) {
						case "muld":
							currentop = OPfpMult;
							break;
						case "addd":
							currentop = OPfpAdd;
							break;
						case "subd":
							currentop = OPfpSub;
							break;
						case "divd":
							currentop = OPfpDiv;
							break;
						case "ld":
							currentop = OPfpLd;
							break;
						case "sd":
							currentop = OPfpSd;
							break;
						case "muldi":
							currentop = OPintMult;
							break;
						case "daddi":
							currentop = OPintAdd;
							break;
						case "subdi":
							currentop = OPintSub;
							break;
						case "divdi":
							currentop = OPintDiv;
							break;
						case "ldi":
							currentop = OPintLd;
							break;
						case "sdi":
							currentop = OPintSd;
							break;
						case "br_taken":
							currentop = OPbrTaken;
							break;
						case "br_untaken":
							currentop = OPbrUntaken;
							break;
						default:
							break;
						}

						if (operation.toLowerCase() == "sd" || operation.toLowerCase() == "sdi") {

							lstInstructionsPipeLine.add(new PipeLine.Instruction("", des, s2, "null", currentop));
						} else {
							lstInstructionsPipeLine.add(new PipeLine.Instruction("", s1, s2, des, currentop));

						}

						NumInstructions++;
						numberOfRows = lstInstructionsPipeLine.size();
						numberOfColumns += currentop.execution_cycles + 4;
						currentCycle = 0;


						// PipeLine.Instruction inst=new
						// PipeLine.Instruction(operation,des,s1,s2);
						// lstInstructionsPipeLine.add(inst);

						// lstInstructionsPipeLine.get(
						// lstInstructionsPipeLine.size() - 1
						// ).setIssueNum(issue);
						// table
					}

				}
				UpdateDisplay();

			}
		});
		btnLoadPipeline.setBounds(261, 13, 89, 23);
		panelPipeline.add(btnLoadPipeline);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 154, 814, 361);
		panelPipeline.add(tabbedPane);

		JScrollPane scrollPanePipeLineShow = new JScrollPane();
		tabbedPane.addTab("Solution", null, scrollPanePipeLineShow, null);
		scrollPanePipeLineShow.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPanePipeLineShow.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		panelShowResult = new JPanel();
		scrollPanePipeLineShow.setViewportView(panelShowResult);
		panelShowResult.setLayout(null);

		scrollPaneStudent = new JScrollPane();
		tabbedPane.addTab("Student", null, scrollPaneStudent, null);

		panelShowResultStudent = new JPanel();
		scrollPaneStudent.setViewportView(panelShowResultStudent);
		panelShowResultStudent.setLayout(null);
		
		JButton btnSendEmailPipe = new JButton("Send");
		btnSendEmailPipe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
				//panelSendEmail.setVisible(true);
				StringBuilder sb=new StringBuilder();

				int from=0,to=0;
	 			
	 			sb.append("<table style=\"width:100%;border:1px solid black;\">");
	 			sb.append("<tr><th>Instructions&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>");
	 			for (int i = 0; i < numberOfColumns; i++) {
	 				sb.append("<th >C#"+(i+1)+"</th>");
	 			}
	 			sb.append("</tr>");
	 			
	 			
	 			for (int i = 0; i < numberOfRows; i++) {
	 				sb.append("<tr>");
	 				strInsLbl = "";
	 				if (lstInstructionsPipeLine.get(i).destination_register == "null") {
	 					if (lstInstructionsPipeLine.get(i).operator.name == "sd"
	 							|| lstInstructionsPipeLine.get(i).operator.name == "sdi") {
	 						strInsLbl = lstInstructionsPipeLine.get(i).operator.name + " ("
	 								+ lstInstructionsPipeLine.get(i).source_register1 + ", Offset, "
	 								+ lstInstructionsPipeLine.get(i).source_register2 + ") "
	 								+ lstInstructionsPipeLine.get(i).operator.execution_cycles;
	 					} else {
	 						strInsLbl = lstInstructionsPipeLine.get(i).operator.name + " ("
	 								+ lstInstructionsPipeLine.get(i).source_register1 + ", "
	 								+ lstInstructionsPipeLine.get(i).source_register2 + ") "
	 								+ lstInstructionsPipeLine.get(i).operator.execution_cycles;
	 					}
	 				} else {
	 					strInsLbl = lstInstructionsPipeLine.get(i).operator.name + " ("
	 							+ lstInstructionsPipeLine.get(i).destination_register + ", "
	 							+ lstInstructionsPipeLine.get(i).source_register1 + ", "
	 							+ lstInstructionsPipeLine.get(i).source_register2 + ") "
	 							+ lstInstructionsPipeLine.get(i).operator.execution_cycles;
	 				}

	 				int num = i;
	 				sb.append("<td  style=\"width:100%;border:1px solid black;\">I#" + num + " : " + strInsLbl.toUpperCase() + "</td>");
	 				to+=numberOfColumns;
	 				int select=0+i;
	 				int col=0;
	 				for (int j = from; j < to; j++) {
	 					select= col*numberOfRows+i;
	 					JTextField txttemp=new JTextField();
	 					txttemp=listOfTextField.get(select);
	 					sb.append("<td  style=\"width:100%;border:1px solid black;\">" + txttemp.getText() + "</td>");
	 					col++;
	 				}
	 				from+=numberOfColumns;
	 			    sb.append("</tr>");
	 			}
	 			

	 			sb.append("</table>");
	 			sb.append("<h4>Number Of  wrong attempt: "+wrongAttemptPipe+"</h4>");
	 	
	 			
	 			
				SendEmail sndEmail=new SendEmail(sb.toString());
				
			
			}
		});
		btnSendEmailPipe.setBounds(261, 115, 89, 23);
		panelPipeline.add(btnSendEmailPipe);



		/*
		 * textField_1.getDocument().addDocumentListener(new DocumentListener()
		 * {
		 * 
		 * @Override public void changedUpdate(DocumentEvent arg0) { // TODO
		 * Auto-generated method stub int i = 0; }
		 * 
		 * @Override public void insertUpdate(DocumentEvent arg0) { // TODO
		 * Auto-generated method stub int i = 0;
		 * 
		 * Document document = (Document)arg0.getDocument(); int changeLength =
		 * arg0.getLength(); changeLog.append(e.getType().toString() + ": " +
		 * changeLength + " character" + ((changeLength == 1) ? ". " : "s. ") +
		 * " Text length = " + document.getLength() + "." + newline);
		 * 
		 * }
		 * 
		 * @Override public void removeUpdate(DocumentEvent arg0) { // TODO
		 * Auto-generated method stub int i = 0; } // implement the methods });
		 */


		pnlLogin = new JPanel();
		pnlLogin.setBounds(837, 104, 240, 118);
		contentPane.add(pnlLogin);
		pnlLogin.setVisible(false);
		pnlLogin.setBackground(new Color(230, 230, 250));
		pnlLogin.setLayout(null);

		txtUserName = new JTextField();
		txtUserName.setBounds(131, 11, 86, 20);
		pnlLogin.add(txtUserName);
		txtUserName.setColumns(10);

		txtPassword = new JTextField();
		txtPassword.setBounds(131, 42, 86, 20);
		pnlLogin.add(txtPassword);
		txtPassword.setColumns(10);

		JLabel lblNewLabel = new JLabel("User Name");
		lblNewLabel.setBounds(58, 14, 63, 14);
		pnlLogin.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Password");
		lblNewLabel_1.setBounds(58, 45, 63, 14);
		pnlLogin.add(lblNewLabel_1);

		JButton btnStudent = new JButton("Student");
		btnStudent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String csvFile = "config/users.csv";
				BufferedReader br = null;
				String line = "";
				String cvsSplitBy = ",";

				try {

					br = new BufferedReader(new FileReader(csvFile));
					while ((line = br.readLine()) != null) {

					        // use comma as separator
						String[] User = line.split(cvsSplitBy);
if(User[0].equals(txtUserName.getText())&&User[1].equals(txtPassword.getText()))
{
	

						System.out.println("User [code= " + User[0] 
			                                 + " , name=" + User[1] + "]");
						bolMode = false;
						pnlLogin.setVisible(false);
						panelPipeline.setVisible(true);
						panelScoreboard.setVisible(false);
						panelTomasulo.setVisible(false);
						setBounds(10, 10, 896, 610);
						panelPipeline.setBounds(10, 0, 856, 536);
					}


					}

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (br != null) {
						try {
							br.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

				if(pnlLogin.isVisible())
					JOptionPane.showMessageDialog(new JFrame(), "Your username and password is not currect!", "Dialog", JOptionPane.ERROR_MESSAGE);

				
				
			}
		});
		btnStudent.setBounds(21, 84, 100, 23);
		pnlLogin.add(btnStudent);

		btnProfessor = new JButton("Professor");
		btnProfessor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bolMode = true;
				pnlLogin.setVisible(false);
				panelPipeline.setVisible(true);
				panelScoreboard.setVisible(false);
				panelTomasulo.setVisible(false);
				setBounds(10, 10, 896, 610);
				panelPipeline.setBounds(10, 0, 856, 536);

			}
		});
		btnProfessor.setBounds(128, 84, 100, 23);
		pnlLogin.add(btnProfessor);

		panelScoreboard = new JPanel();
		panelScoreboard.setVisible(false);
		panelScoreboard.setBackground(new Color(255, 248, 220));
		panelScoreboard.setBounds(700, 0, 799, 512);
		contentPane.add(panelScoreboard);
		panelScoreboard.setLayout(null);

		btnExecAllScore = new JButton("Exec");
		btnExecAllScore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// [LD,F6,34,R2,0,Load,0,0,
				// LD,F2,45,R3,0,Load,0,0,
				// MULTD,F0,F2,F4,5,Mult,0,0,
				// SUBD,F8,F6,F2,3,Sub,0,0,
				// DIVD,F10,F0,F6,4,Div,0,0,
				// ADDD,F6,F8,F2,3,Add,0,0]
				/*
				 * lstInstructions.add(new
				 * Instruction("LD","F6","34","R2",EnumFunctionUnits.integer,
				 * "Load",EnumInstructionStatus.issue,0));
				 * lstInstructions.add(new
				 * Instruction("LD","F2","45","R3",EnumFunctionUnits.integer,
				 * "Load",EnumInstructionStatus.issue,0));
				 * lstInstructions.add(new
				 * Instruction("MULTD","F0","F2","F4",EnumFunctionUnits.MULT,
				 * "Mult",EnumInstructionStatus.issue,0)); //
				 * lstInstructions.add(new
				 * Instruction("MULTD","F0","F0","F5",EnumFunctionUnits.MULT,
				 * "Mult",EnumInstructionStatus.issue,0));
				 * lstInstructions.add(new
				 * Instruction("SUBD","F8","F6","F2",EnumFunctionUnits.add,"Sub"
				 * ,EnumInstructionStatus.issue,0)); lstInstructions.add(new
				 * Instruction("DIVD","F10","F0","F6",EnumFunctionUnits.divid,
				 * "Div",EnumInstructionStatus.issue,0));
				 * lstInstructions.add(new
				 * Instruction("ADDD","F6","F8","F2",EnumFunctionUnits.add,"Sub"
				 * ,EnumInstructionStatus.issue,0));
				 */
				/*
				 * _NumInstruction=lstInstructions.size();
				 * 
				 * for (int i=0;i<_NumInstruction;i++) lstInstructStatus.add(new
				 * InstructionStatus(0, 0, 0, 0,0));
				 * 
				 * lstreg_result2=new ArrayList<String>(Collections.nCopies(32,
				 * "")); _StatusNextInstruction=new
				 * ArrayList<EnumInstructionStatus>(Collections.nCopies(
				 * _NumInstruction, EnumInstructionStatus.none));
				 * 
				 * // here number of function is 5 TODO in the future it will be
				 * better to change that to dynamic // TODO get the cycle from
				 * from _lstFuStatus2=new ArrayList<FunctionUnitStatus>();
				 * _lstFuStatus2.add(new FunctionUnitStatus("integer", "No", "",
				 * "", "", "", "", "", "", "", 1)); _lstFuStatus2.add(new
				 * FunctionUnitStatus("mult1", "No", "", "", "", "", "", "", "",
				 * "", 10)); _lstFuStatus2.add(new FunctionUnitStatus("mult2",
				 * "No", "", "", "", "", "", "", "", "", 10));
				 * _lstFuStatus2.add(new FunctionUnitStatus("add", "No", "", "",
				 * "", "", "", "", "", "", 2)); _lstFuStatus2.add(new
				 * FunctionUnitStatus("divide", "No", "", "", "", "", "", "",
				 * "", "", 40));
				 * 
				 * update1(); clock=1;
				 * 
				 * 
				 * -------------------------------------------------------------
				 * -------------------------------------------------------------
				 * ----- SCHEDULING THE INSTRUCTION USING SCOREBOARDING
				 * -------------------------------------------------------------
				 * -------------------------------------------------------------
				 * -----
				 * 
				 * 
				 * Determine the operation that is to be performed for each
				 * instruction
				 * 
				 * for (int i=0;i<_NumInstruction;i++) operation(i);
				 * 
				 * _currentIssue = -1;
				 */

				flag = false;
				// no_of_instruction=instru.size();
				while (not_completeScorboard(_NumInstruction) && !flag) {
					System.out.print(" clock:" + clock);
					upper_limit = _currentIssue + 1;
					upper_limit = (upper_limit == _NumInstruction) ? (_NumInstruction - 1) : upper_limit;
					update_flag = false;
					for (int i = 0; i <= upper_limit; i++) {
						System.out.print(" current operator: " + lstInstructionsScorboard.get(i).getOper());
						ExecuteInstructionScorboard(i);
						lstInstructionsScorboard.get(i).setStatus(_StatusNextInstructionScorboard.get(i));
						System.out.println(" next_status: " + _StatusNextInstructionScorboard.get(i) + " ints: " + i);
					}
					if (update_flag) {
						updateScorboard1();
						if (!chckbxExeAllScore.isSelected() && clock > counter) {
							flag = true;
							counter = clock;
						}

					}

					clock = clock + 1;

					// updateInstructionTableScorboardStudent();

					updateInstructionTableScorboard();
					updateRegisterTableScorboard();
					updateALUTableScorboard();

				}

				// print_out();
				if (!(not_completeScorboard(_NumInstruction))) {
					JOptionPane.showMessageDialog(new JFrame(), "complete!", "Dialog", JOptionPane.INFORMATION_MESSAGE);

					counter = 0;
				}

			}
		});

		scrollPaneScorInstStateStudent = new JScrollPane();
		scrollPaneScorInstStateStudent.setBounds(275, 277, 455, 166);
		panelScoreboard.add(scrollPaneScorInstStateStudent);

		// tblInstructionsScoreStudent = new JTable();
		scrollPaneScorInstStateStudent.setViewportView(tblInstructionsScoreStudent);
		btnExecAllScore.setBounds(115, 29, 89, 23);
		panelScoreboard.add(btnExecAllScore);

		scrollPaneScorInstState = new JScrollPane();
		scrollPaneScorInstState.setBounds(275, 104, 455, 166);
		panelScoreboard.add(scrollPaneScorInstState);

		// tblInstructionsScore = new JTable((TableModel) null);
		// tblInstructionsScore.setPreferredScrollableViewportSize(new
		// Dimension(0, 0));
		scrollPaneScorInstState.setViewportView(tblInstructionsScore);

		scrollPaneInstScore = new JScrollPane();
		scrollPaneInstScore.setBounds(10, 104, 250, 166);
		panelScoreboard.add(scrollPaneInstScore);

		txtInstructionsScore = new JTextArea();
		txtInstructionsScore.setText(
				"LD\tF6, 34, R2\r\nLD\tF2, 45, R3\r\nMULTD\tF0, F2, F4\r\nSUBD\tF8, F6, F2\r\nDIVD\tF10, F0, F6\r\nADDD\tF6, F8, F2");
		scrollPaneInstScore.setViewportView(txtInstructionsScore);

		JButton btnLoadScore = new JButton("Load");
		btnLoadScore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				

				// initScoreboard();
				String[] split_1, split_2, operands;
				String op_portion, comment_portion = "", des = "", s1 = "", s2 = "";
				String operation;
				int issue = 0;
				lstInstructionsScorboard = new ArrayList<Instruction>();

				for (String line : txtInstructionsScore.getText().split("\\n")) {
					// trim line
					line = line.trim();

					// only process non-empty lines
					if (line != null && !line.equals("")) {
						// increment the issue_number
						issue++;

						split_1 = new String[1];
						split_1[0] = line;

						op_portion = line;

						// Split/Prse the operation & operands
						split_2 = (split_1[0].trim()).split("\\s+");
						operation = split_2[0].trim();

						operands = new String[(split_2.length - 1)];

						for (int i = 1; i < split_2.length; i++) {
							// temporary index
							int temp_index = split_2[i].indexOf(",");
							// check for a comma
							temp_index = (temp_index == -1 ? split_2[i].length() : temp_index);
							if (i == 1)
								des = split_2[i].substring(0, temp_index);
							else if (i == 2)
								s1 = split_2[i].substring(0, temp_index);
							else if (i == 3)
								s2 = split_2[i].substring(0, temp_index);
							// operands[ i-1 ] = split_2[i].substring( 0,
							// temp_index );
						}

						// if( operands.length == 3 ){
						// [LD,F6,34,R2,0,Load,0,0,
						// LD,F2,45,R3,0,Load,0,0,
						// MULTD,F0,F2,F4,5,Mult,0,0,
						// SUBD,F8,F6,F2,3,Sub,0,0,
						// DIVD,F10,F0,F6,4,Div,0,0,
						// ADDD,F6,F8,F2,3,Add,0,0]
						// lstInstructions.add(new
						// Instruction("ADDD","F6","F8","F2",EnumFunctionUnits.add,"Sub",EnumInstructionStatus.issue,0));
						/*
						 * function operation(i) { if (instr[i][oper] == "LD") {
						 * instr[i][fu]=integer; instr[i][op_name]="Load"; } if
						 * (instr[i][oper] == "SD") { instr[i][fu]=integer;
						 * instr[i][op_name]="Store"; } if (instr[i][oper] ==
						 * "MULTD") { instr[i][fu]=MULT;
						 * instr[i][op_name]="Mult"; } if (instr[i][oper] ==
						 * "DIVD") { instr[i][fu]=divide;
						 * instr[i][op_name]="Div"; } if (instr[i][oper] ==
						 * "ADDD") { instr[i][fu]=add; instr[i][op_name]="Add";
						 * } if (instr[i][oper] == "SUBD") { instr[i][fu]=add;
						 * instr[i][op_name]="Sub"; } }
						 */

						EnumFunctionUnits enumTemp = null;
						String strTempOpName = "";
						switch (operation.toLowerCase()) {
						case "ld":
							enumTemp = EnumFunctionUnits.integer;
							strTempOpName = "Load";
							break;
						case "sd":
							enumTemp = EnumFunctionUnits.integer;
							strTempOpName = "Store";
							break;
						case "multd":
							enumTemp = EnumFunctionUnits.MULT;
							strTempOpName = "Mult";
							break;
						case "divd":
							enumTemp = EnumFunctionUnits.divid;
							strTempOpName = "Div";
							break;
						case "addd":
							enumTemp = EnumFunctionUnits.add;
							strTempOpName = "Add";
							break;
						case "subd":
							enumTemp = EnumFunctionUnits.add;
							strTempOpName = "Sub";
							break;
						default:
							break;
						}
						lstInstructionsScorboard.add(new Instruction(operation, des, s1, s2, enumTemp, strTempOpName,
								EnumInstructionStatus.issue, 0));

						/*
						 * Instruction inst=new
						 * Instruction(operation,des,s1,s2);
						 * lstInstructions.add(inst);
						 */

						// oplist.addOperation( new Operation( operation,
						// operands[0], operands[1], operands[2], comment_exists
						// ) );
						// }
						// else if( operands.length == 2 ){
						// oplist.addOperation( new Operation( operation,
						// operands[0], operands[1], comment_exists ) );
						// }
						// else{
						/*
						 * throw new Exception(){ public String toString(){
						 * return "File Parse Error: Malformed Operation"; } };
						 */
						// }

						// oplist.getLastOperation().setIssueNum( issue );

						// lstInstructions.get( lstInstructions.size() - 1
						// ).setIssueNum(issue);

					}

				}

				try {

					_NumInstruction = lstInstructionsScorboard.size();
					lstInstructStatusScorboard = new ArrayList<InstructionStatus>();
					for (int i = 0; i < _NumInstruction; i++)
						lstInstructStatusScorboard.add(new InstructionStatus(0, 0, 0, 0, 0));

					lstAsnswerStudet=new int[_NumInstruction][8];
					
					lstreg_resultScorboard2 = new ArrayList<String>(Collections.nCopies(32, ""));
					_StatusNextInstructionScorboard = new ArrayList<EnumInstructionStatus>(
							Collections.nCopies(_NumInstruction, EnumInstructionStatus.none));

					try {
						/*
						 * [pipeline] PipeFPAddSub=1 PipeFPMult=1 PipeFPDivide=1
						 * PipeIntDivide=1 [scorboard] ScoreInteger=1
						 * ScoreAddSub=2 ScoreMult=10 ScoreDivide=40 [tomasulo]
						 * TomInteger=2 TomAddI=1 TomAddSub=4 TomMult=7
						 * TomDivide=25
						 */
						Wini ini;
						/* Load the ini file. */
						ini = new Wini(new File("config/settings.ini"));

						int ScoreInteger = ini.get("scorboard", "ScoreInteger", int.class);
						int ScoreAddSub = ini.get("scorboard", "ScoreAddSub", int.class);
						int ScoreMult = ini.get("scorboard", "ScoreMult", int.class);
						int ScoreDivide = ini.get("scorboard", "ScoreDivide", int.class);

						// here number of function is 5 TODO in the future it
						// will be better to change that to dynamic
						// TODO get the cycle from from
						_lstFuStatusScorboard2 = new ArrayList<FunctionUnitStatus>();
						_lstFuStatusScorboard2.add(
								new FunctionUnitStatus("integer", "No", "", "", "", "", "", "", "", "", ScoreInteger));// 1
						_lstFuStatusScorboard2
								.add(new FunctionUnitStatus("mult1", "No", "", "", "", "", "", "", "", "", ScoreMult));// 10
						_lstFuStatusScorboard2
								.add(new FunctionUnitStatus("mult2", "No", "", "", "", "", "", "", "", "", ScoreMult));// 10
						_lstFuStatusScorboard2
								.add(new FunctionUnitStatus("add", "No", "", "", "", "", "", "", "", "", ScoreAddSub));// 2
						_lstFuStatusScorboard2.add(
								new FunctionUnitStatus("divide", "No", "", "", "", "", "", "", "", "", ScoreDivide));// 40

					} catch (InvalidFileFormatException ex) {
						System.out.println("Invalid file format.");
					} catch (IOException ex) {
						System.out.println("Problem reading file.");
					}

					updateScorboard1();
					clock = 1;

					/*-------------------------------------------------------------------------------------------------------------------------------*/
					/* SCHEDULING THE INSTRUCTION USING SCOREBOARDING */
					/*-------------------------------------------------------------------------------------------------------------------------------*/

					/*
					 * Determine the operation that is to be performed for each
					 * instruction
					 */

					for (int i = 0; i < _NumInstruction; i++)
						operationScorboard(i);

					_currentIssue = -1;

					while (_InstructionModelScoreboard.getRowCount() < lstInstructionsScorboard.size()) {
						_InstructionModelScoreboard.addRow(new Object[] { " ", " ", " ", " ", " ", " ", " ", " " });
					}

					while (_InstructionModelScoreboardStudent.getRowCount() < lstInstructionsScorboard.size()) {
						_InstructionModelScoreboardStudent
								.addRow(new Object[] { " ", " ", " ", " ", " ", " ", " ", " " });
					}

					updateInstructionTableScorboardStudent();

					updateInstructionTableScorboard();
					updateRegisterTableScorboard();
					updateALUTableScorboard();

					CheckDepencencyScoreboard();

				} catch (Exception ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}

			}
		});
		btnLoadScore.setBounds(10, 29, 89, 23);
		panelScoreboard.add(btnLoadScore);

		scrollPaneScorFuncUnit = new JScrollPane();
		scrollPaneScorFuncUnit.setBounds(10, 277, 720, 130);
		panelScoreboard.add(scrollPaneScorFuncUnit);

		// tblFunctionUnitsScore = new JTable((TableModel) null);
		// tblFunctionUnitsScore.setPreferredScrollableViewportSize(new
		// Dimension(0, 0));
		scrollPaneScorFuncUnit.setViewportView(tblFunctionUnitsScore);

		scrollPaneScorRegUnit = new JScrollPane();
		scrollPaneScorRegUnit.setBounds(10, 418, 720, 65);
		panelScoreboard.add(scrollPaneScorRegUnit);

		// tblRegistersScore = new JTable((TableModel) null);
		// tblRegistersScore.setPreferredScrollableViewportSize(new Dimension(0,
		// 0));
		scrollPaneScorRegUnit.setViewportView(tblRegistersScore);

		chckbxExeAllScore = new JCheckBox("Exe. All");
		chckbxExeAllScore.setBounds(28, 59, 69, 23);
		panelScoreboard.add(chckbxExeAllScore);

		JScrollPane scrollPaneDisplayTextScorboard = new JScrollPane();
		scrollPaneDisplayTextScorboard.setBounds(275, 0, 455, 93);
		panelScoreboard.add(scrollPaneDisplayTextScorboard);

		txtDisplayScorboard = new JTextArea();
		scrollPaneDisplayTextScorboard.setViewportView(txtDisplayScorboard);

		chckbxForwardingScorboard = new JCheckBox("Forwarding");
		chckbxForwardingScorboard.setBounds(127, 59, 97, 23);
		panelScoreboard.add(chckbxForwardingScorboard);

		btnCompareScore = new JButton("Compare");
		btnCompareScore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				for (int i = 0; i < tblInstructionsScore.getRowCount(); i++) {
					for (int j = 4; j < tblInstructionsScore.getColumnCount(); j++) {
						System.out.println("i:" + i);
						System.out.println("j:" + j);
						int intProf = Integer.parseInt(tblInstructionsScore.getModel().getValueAt(i, j).toString());// rowIndex,
																													// columnIndex
						int intStudent = Integer
								.parseInt(tblInstructionsScoreStudent.getModel().getValueAt(i, j).toString());// rowIndex,
																												// columnIndex
						if (intProf == intStudent)
							System.out.println("equal");

						tblInstructionsScoreStudent.setDefaultRenderer(Object.class, new ScorCellRenderer());

					}

				}
				tblInstructionsScoreStudent.repaint();

			}
		});
		btnCompareScore.setBounds(127, 2, 89, 23);
		panelScoreboard.add(btnCompareScore);
		
		btnSendScore = new JButton("Send");
		btnSendScore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				flag = false;
				// no_of_instruction=instru.size();
				while (not_completeScorboard(_NumInstruction) && !flag) {
					System.out.print(" clock:" + clock);
					upper_limit = _currentIssue + 1;
					upper_limit = (upper_limit == _NumInstruction) ? (_NumInstruction - 1) : upper_limit;
					update_flag = false;
					for (int i = 0; i <= upper_limit; i++) {
						System.out.print(" current operator: " + lstInstructionsScorboard.get(i).getOper());
						ExecuteInstructionScorboard(i);
						lstInstructionsScorboard.get(i).setStatus(_StatusNextInstructionScorboard.get(i));
						System.out.println(" next_status: " + _StatusNextInstructionScorboard.get(i) + " ints: " + i);
					}
					if (update_flag) {
						updateScorboard1();
						if (!chckbxExeAllScore.isSelected() && clock > counter) {
							flag = true;
							counter = clock;
						}

					}

					clock = clock + 1;


				//	updateInstructionTableScorboard();


				}
				
				
				
				
				
				
				
				StringBuilder sb=new StringBuilder();
				sb.append("<table style=\"width:600px;border:1px solid black;\">");
	 			sb.append("<tr><th>Instructions</th><th ></th><th >J</th><th >K</th><th >Issue</th><th >Read Oper.</th><th >Exe Com.</th><th >Write Res.</th>");
	 			for (int i = 0; i < numberOfColumns; i++) {
	 				sb.append("<th >C#"+(i+1)+"</th>");
	 			}
	 			sb.append("</tr>");
	 			
	 			wrongAttemptPipe=0;
	 			//tblInstructionsScoreStudent
	 			for (int i = 0; i < tblInstructionsScoreStudent.getRowCount(); i++) {
					for (int j = 0; j <tblInstructionsScoreStudent.getColumnCount(); j++) {
					System.out.println(tblInstructionsScoreStudent.getValueAt(i, j));	
					}
					
					sb.append("<tr>");
	 				
	 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + tblInstructionsScoreStudent.getValueAt(i, 0) + "</td>");
	 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + tblInstructionsScoreStudent.getValueAt(i, 1) + "</td>");
	 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + tblInstructionsScoreStudent.getValueAt(i, 2) + "</td>");
	 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + tblInstructionsScoreStudent.getValueAt(i, 3) + "</td>");
	 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + tblInstructionsScoreStudent.getValueAt(i, 4) + "</td>");
	 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + tblInstructionsScoreStudent.getValueAt(i, 5) + "</td>");
	 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + tblInstructionsScoreStudent.getValueAt(i, 6) + "</td>");
	 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + tblInstructionsScoreStudent.getValueAt(i, 7) + "</td>");
	 				 sb.append("</tr>");
	 				 
	 				InstructionStatus instructionStatus = lstInstructStatusScorboard.get(i);
	 				if( instructionStatus.getIssue()!=Integer.parseInt(tblInstructionsScoreStudent.getValueAt(i, 4).toString()))
	 					wrongAttemptPipe++;
	 				
	 				if(instructionStatus.getReadop()!=Integer.parseInt(tblInstructionsScoreStudent.getValueAt(i, 5).toString()))
	 					wrongAttemptPipe++;
	 				if(instructionStatus.getExecute()!=Integer.parseInt(tblInstructionsScoreStudent.getValueAt(i, 6).toString()))
	 					wrongAttemptPipe++;
	 				if(instructionStatus.getWrite_result()!=Integer.parseInt(tblInstructionsScoreStudent.getValueAt(i, 7).toString()))
	 					wrongAttemptPipe++;
	 				
				}
	 			
	
	 			
	 			sb.append("</table>");
	 			sb.append("<h4>Number Of wrong CLK: "+wrongAttemptPipe+"</h4>");
				
	 			SendEmail sndEmail=new SendEmail(sb.toString());
				
				
				
				
				
				
				
			}
		});
		btnSendScore.setBounds(210, 29, 64, 23);
		panelScoreboard.add(btnSendScore);

		/// **************************************Tomasulo********************************************
		panelTomasulo = new JPanel();
		panelTomasulo.setBackground(new Color(211, 211, 211));
		panelTomasulo.setBounds(314, 496, 799, 658);
		contentPane.add(panelTomasulo);
		panelTomasulo.setLayout(null);

		initTomasulo();
		initTomasuloStudent();
		/*****************************************/

		JButton btnLoadTomasulo = new JButton("Load");
		btnLoadTomasulo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String[] split_1, split_2, operands;
				String op_portion, comment_portion = "", des = "", s1 = "", s2 = "";
				String operation;
				int issue = 0;
				lstInstructionsTomasulo = new ArrayList<tomasulo.Instruction>();

				for (String line : txtInstructionsTomasulo.getText().split("\\n")) {
					// trim line
					line = line.trim();

					// only process non-empty lines
					if (line != null && !line.equals("")) {
						// increment the issue_number
						issue++;

						split_1 = new String[1];
						split_1[0] = line;

						op_portion = line;

						// Split/Prse the operation & operands
						split_2 = (split_1[0].trim()).split("\\s+");
						operation = split_2[0].trim();

						operands = new String[(split_2.length - 1)];

						for (int i = 1; i < split_2.length; i++) {
							// temporary index
							int temp_index = split_2[i].indexOf(",");
							// check for a comma
							temp_index = (temp_index == -1 ? split_2[i].length() : temp_index);
							if (i == 1)
								des = split_2[i].substring(0, temp_index);
							else if (i == 2)
								s1 = split_2[i].substring(0, temp_index);
							else if (i == 3)
								s2 = split_2[i].substring(0, temp_index);
							// operands[ i-1 ] = split_2[i].substring( 0,
							// temp_index );
						}

						// if( operands.length == 3 ){
						tomasulo.Instruction inst = new tomasulo.Instruction(operation, des, s1, s2);
						lstInstructionsTomasulo.add(inst);

						// oplist.addOperation( new Operation( operation,
						// operands[0], operands[1], operands[2], comment_exists
						// ) );
						// }
						// else if( operands.length == 2 ){
						// oplist.addOperation( new Operation( operation,
						// operands[0], operands[1], comment_exists ) );
						// }
						// else{
						/*
						 * throw new Exception(){ public String toString(){
						 * return "File Parse Error: Malformed Operation"; } };
						 */
						// }

						// oplist.getLastOperation().setIssueNum( issue );

						lstInstructionsTomasulo.get(lstInstructionsTomasulo.size() - 1).setIssueNum(issue);

					}

				}

				try {

					initVariables();

					while (InstructionModelTomasuloStudent.getRowCount() < lstInstructionsTomasulo.size()) {
						InstructionModelTomasuloStudent.addRow(new Object[] { " ", " ", " ", " ", " ", " ", " " });
					}
					lstAsnswerStudetTomasulo =new int[lstInstructionsTomasulo.size()][8] ;
					
					while (InstructionModelTomasulo.getRowCount() < lstInstructionsTomasulo.size()) {
						InstructionModelTomasulo.addRow(new Object[] { " ", " ", " ", " ", " ", " ", " " });
					}
					updateInstructionTableTomasuloStudent();
					updateInstructionTableTomasulo();
					updateRegisterTableTomasulo();
					updateMemTableTomasulo();
					updateALUTableTomasulo();

					CheckDepencencyTomasulo();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnLoadTomasulo.setActionCommand("Load");
		btnLoadTomasulo.setBounds(265, 12, 100, 23);
		panelTomasulo.add(btnLoadTomasulo);

		btnStepTomasulo = new JButton("Step");
		btnStepTomasulo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO
				if (!isComplete()) {
					ExecuteInstruction();
					updateInstructionTableTomasuloStudent();
					updateInstructionTableTomasulo();
					updateRegisterTableTomasulo();
					updateMemTableTomasulo();
					updateALUTableTomasulo();
				} else {
					javax.swing.JOptionPane.showMessageDialog(new JFrame(), "Complete!");

				}

			}
		});
		btnStepTomasulo.setBounds(265, 40, 100, 23);
		panelTomasulo.add(btnStepTomasulo);

		scrollPaneMemTomasulo = new JScrollPane();
		scrollPaneMemTomasulo.setBounds(10, 189, 440, 88);
		panelTomasulo.add(scrollPaneMemTomasulo);

		scrollPaneRegTomasulo = new JScrollPane();
		scrollPaneRegTomasulo.setBounds(456, 189, 326, 302);
		panelTomasulo.add(scrollPaneRegTomasulo);

		scrollPaneALU = new JScrollPane();
		scrollPaneALU.setBounds(10, 502, 780, 136);
		panelTomasulo.add(scrollPaneALU);

		scrollPaneInstStateTomasulo = new JScrollPane();
		scrollPaneInstStateTomasulo.setBounds(371, 3, 411, 177);
		panelTomasulo.add(scrollPaneInstStateTomasulo);

		scrollPaneInstStateTomasuloStudent = new JScrollPane();
		scrollPaneInstStateTomasuloStudent.setBounds(456, 189, 411, 177);
		panelTomasulo.add(scrollPaneInstStateTomasuloStudent);

		JLabel lblClockTomasulo = new JLabel("Clock:");
		lblClockTomasulo.setBounds(263, 104, 102, 14);
		panelTomasulo.add(lblClockTomasulo);

		lblClockCycleTomasulo = new JLabel("0");
		lblClockCycleTomasulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblClockCycleTomasulo.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblClockCycleTomasulo.setSize(new Dimension(50, 40));
		lblClockCycleTomasulo.setBounds(292, 102, 73, 16);
		panelTomasulo.add(lblClockCycleTomasulo);

		scrollPaneInstTomasulo = new JScrollPane();
		scrollPaneInstTomasulo.setBounds(10, 0, 249, 180);
		panelTomasulo.add(scrollPaneInstTomasulo);

		JButton btnAllTomasulo = new JButton("All");
		btnAllTomasulo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				while (!isComplete()) {
					ExecuteInstruction();

					updateInstructionTableTomasulo();
					updateRegisterTableTomasulo();
					updateMemTableTomasulo();
					updateALUTableTomasulo();
				}
				if (!isComplete()) {

				} else {
					javax.swing.JOptionPane.showMessageDialog(new JFrame(), "Complete!");

				}

			}
		});
		btnAllTomasulo.setBounds(265, 69, 100, 23);
		panelTomasulo.add(btnAllTomasulo);

		scrollPaneDisplayTomasulo = new JScrollPane();

		/*
		 * GroupLayout gl_contentPaneTomasulo = new GroupLayout(panelTomasulo);
		 * gl_contentPaneTomasulo.setHorizontalGroup(
		 * gl_contentPaneTomasulo.createParallelGroup(Alignment.LEADING)
		 * .addGroup(gl_contentPaneTomasulo.createSequentialGroup()
		 * .addContainerGap()
		 * .addGroup(gl_contentPaneTomasulo.createParallelGroup(Alignment.
		 * LEADING) .addGroup(gl_contentPaneTomasulo.createSequentialGroup()
		 * .addComponent(scrollPaneALU, GroupLayout.PREFERRED_SIZE, 780,
		 * GroupLayout.PREFERRED_SIZE) .addContainerGap())
		 * .addGroup(gl_contentPaneTomasulo.createSequentialGroup()
		 * .addGroup(gl_contentPaneTomasulo.createParallelGroup(Alignment.
		 * TRAILING) .addGroup(gl_contentPaneTomasulo.createSequentialGroup()
		 * .addComponent(scrollPaneInstTomasulo, GroupLayout.PREFERRED_SIZE,
		 * 249, GroupLayout.PREFERRED_SIZE)
		 * .addGroup(gl_contentPaneTomasulo.createParallelGroup(Alignment.
		 * TRAILING) .addGroup(gl_contentPaneTomasulo.createSequentialGroup()
		 * .addGap(5)
		 * .addGroup(gl_contentPaneTomasulo.createParallelGroup(Alignment.
		 * LEADING, false) .addComponent(btnLoadTomasulo,
		 * GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		 * .addComponent(btnStepTomasulo, GroupLayout.DEFAULT_SIZE,
		 * GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
		 * .addGroup(gl_contentPaneTomasulo.createSequentialGroup()
		 * .addPreferredGap(ComponentPlacement.RELATED)
		 * .addComponent(lblClockTomasulo, GroupLayout.DEFAULT_SIZE, 102,
		 * Short.MAX_VALUE))
		 * .addGroup(gl_contentPaneTomasulo.createSequentialGroup()
		 * .addPreferredGap(ComponentPlacement.RELATED)
		 * .addComponent(btnAllTomasulo, GroupLayout.DEFAULT_SIZE, 100,
		 * Short.MAX_VALUE))
		 * .addGroup(gl_contentPaneTomasulo.createSequentialGroup()
		 * .addGap(33).addComponent(lblClockCycleTomasulo,
		 * GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)))
		 * .addPreferredGap(ComponentPlacement.RELATED)
		 * .addComponent(scrollPaneInstStateTomasulo, GroupLayout.DEFAULT_SIZE,
		 * 411, Short.MAX_VALUE))
		 * .addGroup(gl_contentPaneTomasulo.createSequentialGroup()
		 * .addGroup(gl_contentPaneTomasulo.createParallelGroup(Alignment.
		 * TRAILING) .addComponent(scrollPaneDisplayTomasulo,
		 * GroupLayout.PREFERRED_SIZE, 440, GroupLayout.PREFERRED_SIZE)
		 * .addComponent(scrollPaneMemTomasulo, GroupLayout.DEFAULT_SIZE, 440,
		 * Short.MAX_VALUE)) .addPreferredGap(ComponentPlacement.RELATED)
		 * .addComponent(scrollPaneRegTomasulo, GroupLayout.PREFERRED_SIZE, 326,
		 * GroupLayout.PREFERRED_SIZE))) .addGap(21)))) );
		 * gl_contentPaneTomasulo.setVerticalGroup(
		 * gl_contentPaneTomasulo.createParallelGroup(Alignment.TRAILING)
		 * .addGroup(gl_contentPaneTomasulo.createSequentialGroup()
		 * .addGroup(gl_contentPaneTomasulo .createParallelGroup(
		 * Alignment.TRAILING)
		 * .addGroup(gl_contentPaneTomasulo.createSequentialGroup()
		 * .addComponent(btnLoadTomasulo) .addGap(5)
		 * .addComponent(btnStepTomasulo)
		 * .addPreferredGap(ComponentPlacement.RELATED)
		 * .addComponent(btnAllTomasulo) .addGap(12)
		 * .addComponent(lblClockTomasulo)
		 * .addPreferredGap(ComponentPlacement.RELATED)
		 * .addComponent(lblClockCycleTomasulo).addGap(40))
		 * .addComponent(scrollPaneInstStateTomasulo,
		 * GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE)
		 * .addComponent(scrollPaneInstTomasulo, GroupLayout.PREFERRED_SIZE,
		 * 180, GroupLayout.PREFERRED_SIZE)) .addGap(9)
		 * .addGroup(gl_contentPaneTomasulo.createParallelGroup(Alignment.
		 * BASELINE) .addComponent(scrollPaneRegTomasulo,
		 * GroupLayout.PREFERRED_SIZE, 302, GroupLayout.PREFERRED_SIZE)
		 * .addGroup(gl_contentPaneTomasulo.createSequentialGroup()
		 * .addComponent(scrollPaneMemTomasulo, GroupLayout.PREFERRED_SIZE, 88,
		 * GroupLayout.PREFERRED_SIZE)
		 * .addPreferredGap(ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
		 * .addComponent(scrollPaneDisplayTomasulo, GroupLayout.PREFERRED_SIZE,
		 * 202, GroupLayout.PREFERRED_SIZE)))
		 * .addPreferredGap(ComponentPlacement.UNRELATED)
		 * .addComponent(scrollPaneALU, GroupLayout.PREFERRED_SIZE, 136,
		 * GroupLayout.PREFERRED_SIZE) .addGap(76)) );
		 */

		txtDisplayTomasulo = new JTextArea();
		scrollPaneDisplayTomasulo.setViewportView(txtDisplayTomasulo);
		scrollPaneDisplayTomasulo.setBounds(10, 289, 440, 202);
		panelTomasulo.add(scrollPaneDisplayTomasulo);

		txtInstructionsTomasulo = new JTextArea();
		scrollPaneInstTomasulo.setViewportView(txtInstructionsTomasulo);
		txtInstructionsTomasulo.setText(
				"LD\tF6, 34, R2\r\nLD\tF2, 45, R3\r\nMULD\tF0, F2, F4\r\nSUBD\tF8, F6, F2\r\nDIVD\tF10, F0, F6\r\nADDD\tF6, F8, F2");

		scrollPaneInstStateTomasulo.setViewportView(InstructionTableTomasuloState);

		scrollPaneInstStateTomasuloStudent.setViewportView(InstructionTableTomasuloStateStudent);

		scrollPaneALU.setViewportView(ReservationStationTable);

		scrollPaneRegTomasulo.setViewportView(RegisterTableTomasulo);

		scrollPaneMemTomasulo.setViewportView(MemoryTableTomasulo);

		JButton btnCompareTomasulo = new JButton("Compare");
		btnCompareTomasulo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				while (!isComplete()) {
					ExecuteInstruction();

					updateInstructionTableTomasulo();
					updateRegisterTableTomasulo();
					updateMemTableTomasulo();
					updateALUTableTomasulo();
				}
				
				for (int i = 0; i < InstructionTableTomasuloState.getRowCount(); i++) {
					for (int j = 4; j < InstructionTableTomasuloState.getColumnCount(); j++) {
						System.out.println("i:" + i);
						System.out.println("j:" + j);
						/*
						 * int intProf = Integer
						 * .parseInt(InstructionTableTomasuloState.getModel().
						 * getValueAt(i, j).toString());// rowIndex, //
						 * columnIndex int intStudent = Integer
						 * .parseInt(InstructionTableTomasuloStateStudent.
						 * getModel().getValueAt(i, j) .toString());// rowIndex,
						 * // columnIndex if (intProf == intStudent)
						 * System.out.println("equal");
						 */

						InstructionTableTomasuloStateStudent.setDefaultRenderer(Object.class, new TomCellRenderer());

					}

				}
				InstructionTableTomasuloStateStudent.repaint();

			}
		});
		btnCompareTomasulo.setBounds(265, 155, 100, 23);
		panelTomasulo.add(btnCompareTomasulo);
		
		JButton btnSendTomasulo = new JButton("Send");
		btnSendTomasulo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//panelSendEmail.setVisible(true);
				StringBuilder sb=new StringBuilder();
				
				sb.append("<table style=\"border:1px solid black;\">");
	 			sb.append("<tr><th>Instructions</th><th ></th><th >J</th><th >K</th><th >Issue</th><th >Start-Complete</th><th >Write Res.</th>");
	 			for (int i = 0; i < numberOfColumns; i++) {
	 				sb.append("<th >C#"+(i+1)+"</th>");
	 			}
	 			sb.append("</tr>");
	 			wrongAttemptPipe=0;
	 			for (int i = 0; i < InstructionTableTomasuloStateStudent.getRowCount(); i++) {
					for (int j = 0; j <InstructionTableTomasuloStateStudent.getColumnCount(); j++) {
					System.out.println(InstructionTableTomasuloStateStudent.getValueAt(i, j));	
					}
					
					sb.append("<tr>");
	 				
	 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + InstructionTableTomasuloStateStudent.getValueAt(i, 0) + "</td>");
	 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + InstructionTableTomasuloStateStudent.getValueAt(i, 1) + "</td>");
	 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + InstructionTableTomasuloStateStudent.getValueAt(i, 2) + "</td>");
	 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + InstructionTableTomasuloStateStudent.getValueAt(i, 3) + "</td>");
	 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + InstructionTableTomasuloStateStudent.getValueAt(i, 4) + "</td>");
	 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + InstructionTableTomasuloStateStudent.getValueAt(i, 5) + "</td>");
	 				sb.append("<td  style=\"width:100%;border:1px solid black;\">" + InstructionTableTomasuloStateStudent.getValueAt(i, 6) + "</td>");
	 				
	 				 sb.append("</tr>");
	 				tomasulo.Instruction instructionStatus = lstInstructionsTomasulo.get(i);
	 				
	 				if( instructionStatus.getIssueNum()!=Integer.parseInt(InstructionTableTomasuloStateStudent.getValueAt(i, 4).toString()))
	 					wrongAttemptPipe++;
	 				
	 				if( ! InstructionTableTomasuloStateStudent.getValueAt(i, 5).toString().equals(Integer.toString(instructionStatus.getIssueNum())))
	 					wrongAttemptPipe++;
	 				
	 				if( instructionStatus.getIssueNum()!=Integer.parseInt(InstructionTableTomasuloStateStudent.getValueAt(i, 6).toString()))
	 					wrongAttemptPipe++;
	 				 
			}
	 			
	 			sb.append("</table>");
	 			sb.append("<h4>Number Of wrong CLK: "+wrongAttemptPipe+"</h4>");
				
	 			
	 			SendEmail sndEmail=new SendEmail(sb.toString());
	 			
	 			
	 			
	 			int i = 0;
	 			for (tomasulo.Instruction instruction : lstInstructionsTomasulo) {
	 				int writeTime = instruction.getWriteTime();
	 				// new Object[]{"Instruction"," ", "j", "k", "Issue", "Complete",
	 				// "Write"}
	 				// InstructionModel.addRow(new
	 				// Object[]{"","","","","","","","",""});
	 				InstructionModelTomasulo.setValueAt(instruction.getOper(), i, 0);

	 				InstructionModelTomasulo.setValueAt(instruction.getDes(), i, 1);
	 				InstructionModelTomasulo.setValueAt(instruction.getS1(), i, 2);
	 				InstructionModelTomasulo.setValueAt(instruction.getS2(), i, 3);
	 				InstructionModelTomasulo.setValueAt((instruction.isScheduled() ? instruction.getIssueNum() : " "), i, 4);
	 				InstructionModelTomasulo.setValueAt(instruction.getExecution(), i, 5);
	 				InstructionModelTomasulo.setValueAt((writeTime == -1 ? " " : writeTime), i, 6);

	 				i++;
	 			}

	 			
	 			
	 			
	 			
	 			
	 			
				
			}
		});
		btnSendTomasulo.setBounds(265, 129, 100, 23);
		panelTomasulo.add(btnSendTomasulo);
		// panelTomasulo.setLayout(gl_contentPaneTomasulo);

		panelPipeline.setVisible(false);
		panelScoreboard.setVisible(false);
		panelTomasulo.setVisible(false);

		pnlLogin.setVisible(true);
		pnlLogin.setBounds(10, 10, 240, 120);
		// setBounds(10, 10, 275, 200);

	}

	
	public class ScorCellRenderer extends DefaultTableCellRenderer {

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (column > 3) {
				int intProf = Integer.parseInt(tblInstructionsScore.getModel().getValueAt(row, column).toString());// rowIndex,
																													// columnIndex
				int intStudent = Integer.parseInt(value.toString());// rowIndex,
																	// columnIndex
				if (intProf != intStudent) {
					// javax.swing.JOptionPane.showMessageDialog( new JFrame(),
					// "Complete!" );
					setBackground(Color.pink);
					
				} else {
					setBackground(Color.WHITE);
				}
				lstAsnswerStudet[row][column]=intStudent;
				setText(Integer.toString( intStudent));
			} else
				setBackground(Color.WHITE);
			
			tblInstructionsScoreStudent.repaint();
			/*
			 * if (column == 1 || notFound.contains(row)) {
			 * setBackground(Color.RED); } else { setBackground(Color.WHITE); }
			 */
			return this;
		}
	}

	public class TomCellRenderer extends DefaultTableCellRenderer {

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (column > 3) {
				try{
					String[] strProf = new String[2];
					String strProfValue = InstructionTableTomasuloState.getModel().getValueAt(row, column).toString();
					if(strProfValue.equals("")||strProfValue.equals(" "))
						strProfValue="0";
					if (strProfValue.contains("--")) {
						strProf = strProfValue.split("--");
					} else {
						strProf[0] = strProfValue;
						strProf[1] = "0";
					}
					int intProfStart = Integer.parseInt(strProf[0]);// rowIndex,
					int intProfEnd = Integer.parseInt(strProf[1]);// rowIndex,columnIndex

					String[] strStudent = new String[2];
					String strStudentValue = value.toString();
					if (strStudentValue.contains("--")) {
						strStudent = strStudentValue.split("--");
					}
					else if(strStudentValue.contains("-")){
						strStudent = strStudentValue.split("-");
					}					
					else {
						strStudent[0] = strStudentValue;
						strStudent[1] = "0";
					}

					int intStudentStart = Integer.parseInt(strStudent[0]);// rowIndex,
					int intStudentEnd = Integer.parseInt(strStudent[1]);// rowIndex,
					
					 switch(column)
					 {
					 case 4:
					
						 lstAsnswerStudetTomasulo[row][4]=intStudentStart;
						 setText(Integer.toString( intStudentStart));
						 break;
					 case 5:
						 lstAsnswerStudetTomasulo[row][5]=intStudentStart;
						 lstAsnswerStudetTomasulo[row][6]=intStudentEnd;
						 setText(Integer.toString( intStudentStart)+"--"+Integer.toString( intStudentEnd));
						 break;

					 case 6:
						 lstAsnswerStudetTomasulo[row][7]=intStudentStart;
						 setText(Integer.toString( intStudentStart));
						 break;
					  }
					
					
					if ((intProfStart != intStudentStart) || (intProfEnd != intStudentEnd)) {
					// javax.swing.JOptionPane.showMessageDialog( new JFrame(),
					// "Complete!" );
					setBackground(Color.pink);
					
				} else {
					setBackground(Color.WHITE);
				}
					
					
					
				}
				catch(Exception ex)
				{
					setBackground(Color.pink);

				}
			} else
				setBackground(Color.WHITE);

			InstructionTableTomasuloStateStudent.repaint();
			/*
			 * if (column == 1 || notFound.contains(row)) {
			 * setBackground(Color.RED); } else { setBackground(Color.WHITE); }
			 */
			return this;
		}
	}

	
}
