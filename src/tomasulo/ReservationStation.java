package tomasulo;
/**
 * 
 * @author Hesam
 *
 */
public abstract class ReservationStation {
    protected String sname;              ///< name of reservation station
    protected boolean busy;              ///< flag indicating whether station holding an operation
    protected Instruction instruction;       ///< type of operation
    protected String result;             ///< used to hold result
    protected long cycle;             ///< holds the durations of the instruction
    protected boolean resultReady;       ///< flag indicating result is ready to be written
    protected boolean resultWritten;     ///< flag indicating the result has been written
	boolean notStart;
    ///
    /// Construct an Reservationstation object and initialize sname,busy and operation
    ///
    public ReservationStation(String sname){
         this.sname = sname;
         busy = false;
         instruction=null;
         
         resultReady = false;
         resultWritten = false;
         result = "";
		notStart = false;
    }

    ///
    /// Abstract function to clear the reservation station
    ///
    abstract void clear();
    
    ///
    /// Abstract Function to determine whether the Station is Ready for use
    ///
    abstract boolean isReady();
   
    ///
    /// Abstract Function to schedule the instruction
    ///
    abstract void scheduleInstruction(Instruction op, RegisterFiles reg_in, int cycles);
    
    ///
    /// Return the result
    ///
   public String getResult(){
         if( instruction.getWriteTime() == -1 ){
        	 instruction.setWriteTime( Clock.getInstance().getClock() );
         }
         
         resultWritten = true;
         
         return result;
    }
    
    
    ///
    /// Function return the value of resultready
    ///
    public boolean isResultReady(){
         return resultReady;
    }
   
    ///
    /// Function returns the value of resultwritten
    ///
    public boolean isResultWritten(){
         return resultWritten;
    }
    
    ///
    /// Function return the value of busy
    ///
    public boolean isBusy(){
         return busy;
    }
    
    ///
    /// Function returns the value of duration
    ///
    public long getCycle(){
         return cycle;
    }
    
    ///
    /// Function sets the value of duration
    ///
    public void setCycle(long d){
    	cycle = d;
    }
   
    ///
    /// Function returns the value of name of reservation station
    ///
    public String getName(){
         return sname;
    }
    
    ///
    /// Function sets the name of the reservation station
    ///
    public void setName(String name){
         sname = name;
    }     

    /**
     * Decrease cycle for instruction
     * */
    public void ExecuteCycleInstruction(){
         //set the execution start of the Operation
		if (!notStart) {
			if (instruction.getExecStart() == -1) {
        	 instruction.setExecStart( Clock.getInstance().getClock() );
			System.out.println("setExecStart : " + Clock.getInstance().getClock() + " Inst:" + instruction.getOper()
					+ " " + instruction.getDes() + " " + instruction.getS1() + " " + instruction.getS2() + " ");
         }

         cycle--;
         resultReady = ( cycle == 0 );
         System.out.println("resultReady :"+ resultReady);
         if( resultReady ){
        	 instruction.setExecEnd( Clock.getInstance().getClock() );
        	 System.out.println("setExecEnd : "+Clock.getInstance().getClock());
         }
		} // if !notStart
		else
			notStart = false;
    }
    
    ///
    /// Utility function toc check if the Register Value is an alias
    ///
    protected boolean isPlaceHolder( String to_check ){
         return ( to_check.equals("Add1") || to_check.equals("Add2") || 
                  to_check.equals("Mul1") || to_check.equals("Mul1") ||
                  to_check.equals("Div1") || to_check.equals("Div2") ||
                  to_check.equals("Load1")|| to_check.equals("Load2")||
                  to_check.equals("Int1") );
                
    }
}

