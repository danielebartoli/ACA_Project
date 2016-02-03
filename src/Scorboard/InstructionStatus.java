package Scorboard;

public class InstructionStatus {

	private int issue;
	private int readop;
	private int execute;
	private int write_result;
	private int complete;
	public InstructionStatus(int issue, int readop, int execute, int write_result, int complete) {
		super();
		this.issue = issue;
		this.readop = readop;
		this.execute = execute;
		this.write_result = write_result;
		this.complete = complete;
	}
	public int getIssue() {
		return issue;
	}
	public void setIssue(int issue) {
		this.issue = issue;
	}
	public int getReadop() {
		return readop;
	}
	public void setReadop(int readop) {
		this.readop = readop;
	}
	public int getExecute() {
		return execute;
	}
	public void setExecute(int execute) {
		this.execute = execute;
	}
	public int getWrite_result() {
		return write_result;
	}
	public void setWrite_result(int write_result) {
		this.write_result = write_result;
	}
	public int getComplete() {
		return complete;
	}
	public void setComplete(int complete) {
		this.complete = complete;
	}
	
	
	
}
