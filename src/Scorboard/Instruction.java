package Scorboard;

public class Instruction {

	private String oper;
	private String des;
	private String s1;
	private String s2;
	private EnumFunctionUnits fu;
	private String op_name;
	private EnumInstructionStatus status;
	private int exec_cycle;
	public Instruction(String oper, String des, String s1, 
			String s2, EnumFunctionUnits fu, String op_name, 
			EnumInstructionStatus status,int exec_cycle) {
		super();
		this.oper = oper;
		this.des = des;
		this.s1 = s1;
		this.s2 = s2;
		this.fu = fu;
		this.op_name = op_name;
		this.status = status;
		this.exec_cycle = exec_cycle;
	}
	public String getOper() {
		return oper;
	}
	public void setOper(String oper) {
		this.oper = oper;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getS1() {
		return s1;
	}
	public void setS1(String s1) {
		this.s1 = s1;
	}
	public String getS2() {
		return s2;
	}
	public void setS2(String s2) {
		this.s2 = s2;
	}
	public EnumFunctionUnits getFu() {
		return fu;
	}
	public void setFu(EnumFunctionUnits integer) {
		this.fu = integer;
	}
	public String getOp_name() {
		return op_name;
	}
	public void setOp_name(String op_name) {
		this.op_name = op_name;
	}
	public EnumInstructionStatus getStatus() {
		return status;
	}
	public void setStatus(EnumInstructionStatus status) {
		this.status = status;
	}
	public int getExec_cycle() {
		return exec_cycle;
	}
	public void setExec_cycle(int exec_cycle) {
		this.exec_cycle = exec_cycle;
	}
	
	
	
}
