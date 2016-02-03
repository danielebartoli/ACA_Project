package PipeLine;

public class Operator {

	public String name;
	public int execution_cycles;
	public String functional_unit;
	public String display_value;
	
	public Operator(String name, int execution_cycles,
			String functional_unit, String display_value) {
		super();
		this.name = name;
		this.execution_cycles = execution_cycles;
		this.functional_unit = functional_unit;
		this.display_value = display_value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getExecution_cycles() {
		return execution_cycles;
	}

	public void setExecution_cycles(int execution_cycles) {
		this.execution_cycles = execution_cycles;
	}

	public String getFunctional_unit() {
		return functional_unit;
	}

	public void setFunctional_unit(String functional_unit) {
		this.functional_unit = functional_unit;
	}

	public String getDisplay_value() {
		return display_value;
	}

	public void setDisplay_value(String display_value) {
		this.display_value = display_value;
	}
	    
	
	
	
	
}
