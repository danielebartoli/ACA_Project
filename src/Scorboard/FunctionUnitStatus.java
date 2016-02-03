package Scorboard;

public class FunctionUnitStatus implements Cloneable {
	private String fu_name;
	private String busy;
	private String op;
	private String Fi;
	private String Fj;
	private String Fk;
	private String Qj;
	private String Qk;
	private String Rj;
	private String Rk;
	private int exec;
	private int time;
	
	public FunctionUnitStatus() {
		//super();
	}
	public FunctionUnitStatus(String fu_name, String busy, String op, String fi, String fj, 
			String fk, String qj, String qk,
			String rj, String rk, int exec) {
		//super();
		this.fu_name = fu_name;
		this.busy = busy;
		this.op = op;
		this.Fi = fi;
		this.Fj = fj;
		this.Fk = fk;
		this.Qj = qj;
		this.Qk = qk;
		this.Rj = rj;
		this.Rk = rk;
		this.exec = exec;
		
	}
	public String getFu_name() {
		return fu_name;
	}
	public void setFu_name(String fu_name) {
		this.fu_name = fu_name;
	}
	public String getBusy() {
		return busy;
	}
	public void setBusy(String busy) {
		this.busy = busy;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public String getFi() {
		return Fi;
	}
	public void setFi(String fi) {
		Fi = fi;
	}
	public String getFj() {
		return Fj;
	}
	public void setFj(String fj) {
		Fj = fj;
	}
	public String getFk() {
		return Fk;
	}
	public void setFk(String fk) {
		Fk = fk;
	}
	public String getQj() {
		return Qj;
	}
	public void setQj(String qj) {
		Qj = qj;
	}
	public String getQk() {
		return Qk;
	}
	public void setQk(String qk) {
		Qk = qk;
	}
	public String getRj() {
		return Rj;
	}
	public void setRj(String rj) {
		Rj = rj;
	}
	public String getRk() {
		return Rk;
	}
	public void setRk(String rk) {
		Rk = rk;
	}
	public int getExec() {
		return exec;
	}
	public void setExec(int exec) {
		this.exec = exec;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	
	
	 
    public Object clone() {  
        try { 
       return super.clone(); 
 
        } catch (CloneNotSupportedException e) { 
 
            e.printStackTrace();  
            return null; 

        } 
 
    } 

	
	
}
