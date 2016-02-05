package tomasulo;

/**
 * 
 * @author Hesam
 *
 */

public class Instruction {
	private String oper;
	private String des;
	private String s1;
	private String s2;
	private String op_name;
	private int exec_cycle;
     private int exec_start, exec_end;
     private int time_write;
     private int issue;               
     
     private boolean scheduled;    
     
 public Instruction(){}
     
     
     public Instruction( String oper, String des, String s1, String s2){
          this.oper      = oper;
          this.des=des;
          this.s1=s1;
          this.s2=s2;
          exec_start = exec_end = time_write = -1;
          scheduled = false;
          issue = 0;
     }     

         
     public Instruction( String oper, String des, String s1 ){
          String temp = s1;   
          String operand_3;
          
          int temp_index;        
          temp_index = temp.indexOf("(");
          
          operand_3 = temp.substring( temp_index+1, temp.indexOf(")") );
          s1 = temp.substring( 0 , temp_index );
          
          
          this.oper      = oper;
          this.s1        =s1;
          this.s2=operand_3;          
          
          exec_start = exec_end = time_write = -1;
          scheduled = false;
          issue = 0;
     }
          
     ///
     /// Construct an Operation object given an existing Operation object.
     ///     
     public Instruction( Instruction to_copy ){
          this.oper   = to_copy.oper;
          
          this.des=to_copy.des;
          this.s1=to_copy.s1;
          this.s2=to_copy.s2;
          this.issue = to_copy.issue;          
     }
     
    
     
    /* ///
     ///Return the oper -- example gratia LOAD, SD, DADDI. 
     ///
     public String getoper(){
          return oper;
     }
     
     
     ///
     ///Get the operand at the specified position. Positions start at "1".
     ///
     public String getOperand( int number ){   
          return operands[ number - 1 ];
     }
     
     ///
     ///Return the number of operands
     ///
     public int getNumberOfOperands(){
          return operands.length;
     }*/
     
     ///
     ///Return a String containing all operands.
     ///
//     public String getOperands(){
//          String to_return = "";;
//          
//          for( int i =0; i < (operands.length - 1); i++){
//               to_return += operands[i] + " ";
//          }
//          to_return += operands[operands.length - 1];
//          
//          return to_return;
//     } 
     
     public String getOper() {
		return oper;
	}


	public void setOper(String oper) {
		this.oper = oper;
	}


	///
     ///Get execution start
     ///
     public int getExecStart(){
          return exec_start;
     }
     
     ///
     ///Get execution end
     ///
     public int getExecEnd(){
          return exec_end;
     }
     
     ///
     ///Get the issue number
     ///
     public int getIssueNum(){
          return issue;
     }
     
     ///
     ///Get execution description
     ///
     public String getExecution(){
          String to_return = "";
          
          if( exec_start > 0 ){
               to_return += exec_start;
               
			// Hesam

			if (exec_end > -1) {
				to_return += "--" + exec_end;
			}

          }
          
          return to_return;
     }
     
     ///
     ///Get the time the result was written
     ///
     public int getWriteTime(){
          return time_write;
     }   

     ///
     /// Return whether the instruction has been scheduled
     ///
   public  boolean isScheduled(){
          return scheduled;
     }
     
     ///
     ///Set the Operation oper.
     ///
     public void setoper( String oper){
          this.oper = oper;
     }
     
   
     ///
     ///Set the operand at the specified position. Positions start at "1".
     ///Return an error if the position is invalid.
     ///
     /*public void setOperand( int number, String operand_in ) throws Exception{          
          if( number < 1 || number > operands.length ){
               throw new Exception(){
                    public String toString(){
                         return "Error: Manipulation of Invalid Operand";
                    }
               };
          }         
          
          operands[ number - 1 ] = operand_in;
     }   */
     
     
     
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


	///
     ///Set execution start
     ///
     public void setExecStart( int n ){
          exec_start = n;
     }
     
     ///
     ///Set execution end
     ///
     public void setExecEnd( int n ){
          exec_end = n;
     }
     
     ///
     ///Set the time the result was written
     ///
     public void setWriteTime( int n ){
          time_write = n;
     }
     
     ///
     /// Set the scheduled flag
     ///
     public void setScheduled(){
          scheduled = true;
     }
     
     ///
     /// Set the issue number
     ///
     public void setIssueNum( int i ){
          issue = i;
     }
     
     ///
     ///Return the string represeation of the Operation object.
     ///
     public String toString(){
          String to_return = ""+oper;
          
          to_return+=" "+des;
          to_return+=" "+s1;
          to_return+=" "+s2;
        /*  for( int i =0; i < operands.length; i++){
               to_return += " " + operands[i];
          }          
          
          if( has_comment ){
               to_return += " ; " + comment;
          }*/
          
          return to_return;
     }
	
}
