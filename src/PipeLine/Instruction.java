package PipeLine;

public class Instruction {

	public String name ;
	public String source_register1 ;
	public String source_register2 ;
	public String destination_register ;
	public Operator operator ;
	public String issue_cycle ;
	public String pipeline_stage = null;
	public int execute_counter = 0;
	
	public Instruction(String name, String source_register1,
			String source_register2, String destination_register,
			Operator operator) {
		super();
		this.name = name;
		this.source_register1 = source_register1;
		this.source_register2 = source_register2;
		this.destination_register = destination_register;
		this.operator = operator;
		this.issue_cycle =null;
		this.pipeline_stage = null;
		this.execute_counter = 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSource_register1() {
		return source_register1;
	}

	public void setSource_register1(String source_register1) {
		this.source_register1 = source_register1;
	}

	public String getSource_register2() {
		return source_register2;
	}

	public void setSource_register2(String source_register2) {
		this.source_register2 = source_register2;
	}

	public String getDestination_register() {
		return destination_register;
	}

	public void setDestination_register(String destination_register) {
		this.destination_register = destination_register;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public String getIssue_cycle() {
		return issue_cycle;
	}

	public void setIssue_cycle(String issue_cycle) {
		this.issue_cycle = issue_cycle;
	}

	public String getPipeline_stage() {
		return pipeline_stage;
	}

	public void setPipeline_stage(String pipeline_stage) {
		this.pipeline_stage = pipeline_stage;
	}

	public int getExecute_counter() {
		return execute_counter;
	}

	public void setExecute_counter(int execute_counter) {
		this.execute_counter = execute_counter;
	}
	
	
	
	
}
