package tomasulo;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

public class RegisterFiles {
	 
	     private LinkedHashMap<String, String> INTregisters; ///< Consists of all integer registers.
	     private LinkedHashMap<String, String> FPregisters;  ///< Consists of all floating point registers.
	     
	     ///
	     ///Constructs the register files; initializes all floating point and integer registers to "0";
	     ///
	     public RegisterFiles(){
	    	 INTregisters = new LinkedHashMap<String, String>();
	    	 FPregisters  = new LinkedHashMap<String, String>();
	          
	          
	          //Initialize R0
	          INTregisters.put( "R0", "0" );
	          
	          //Initialize the Integer Registers R1 to Rn
	          for( int i = 0; i < 32; i++ ){
	               String temp_reg =  "R" + ( i + 1 );
	               INTregisters.put( temp_reg, ""  );
	          }
	          
	          //Initialize the FP Registers f0 to Fn
	          for( int i = 0; i < 32; i++ ){
	               String temp_reg =  "F" + (  2*i  );
	               FPregisters.put( temp_reg, "" );
	          }
	     }
	     
	     ///
	     ///Generates a copy of an existing RegisterFiles object.
	     ///
	     public RegisterFiles( RegisterFiles to_copy ){
	    	 INTregisters = new LinkedHashMap<String, String>();
	    	 FPregisters  = new LinkedHashMap<String, String>();

	          //Copy Integer and FP Register Files
	          this.INTregisters = new LinkedHashMap<String, String>( to_copy.INTregisters ) ;
	          this.FPregisters  = new LinkedHashMap<String, String>( to_copy.FPregisters ) ;
	     }
	     
	     ///
	     /// Returns a LinkedHashMap that conatins a copy of the current integer register file.
	     ///
	     public LinkedHashMap<String, String> getIntegerRegisters(){
	          return new LinkedHashMap<String, String>( INTregisters );
	     }

	     ///
	     /// Returns a LinkedHashMap that conatins a copy of the current floating point register file.
	     ///
	     public LinkedHashMap<String, String> getFPRegisters(){
	          return new LinkedHashMap<String, String>( FPregisters );
	     }
	     
	     ///
	     ///Returns the current value of the specified register. An "Invalid register ID" exception is thrown if 
	     ///the specified register does not exist. 
	     ///
	     public String getRegister( String RegID ) /*throws Exception*/{
	          String returnRegID="";
	          
	          if( RegID.charAt(0) == 'R' ){
	        	  returnRegID = INTregisters.get( RegID );
	          }
	          else if( RegID.charAt(0) == 'F' ){
	        	  returnRegID = FPregisters.get( RegID );
	          }
	          /*else{
	               throw new Exception(){
	                    public String toString(){
	                         return "Invalid Register ID.";
	                    }
	               };
	          }*/
	          
	          if( returnRegID.equals("") ){
	        	  returnRegID = RegID;
	          }
	          System.out.println("returnRegID :"+returnRegID);
	          return returnRegID;
	     }
	     
	     ///
	     ///Sets a new value for the specified register. An "Invalid register ID" exception is thrown if 
	     ///the specified register does not exist. 
	     ///
	     public void setRegister( String RegID, String ValIN ) /*throws Exception*/{
	          if( RegID.charAt(0) == 'R' && INTregisters.containsKey( RegID ) ){
	        	  INTregisters.put( RegID, ValIN );
	          }
	          else if( RegID.charAt(0) == 'F' && FPregisters.containsKey( RegID )){
	        	  FPregisters.put( RegID, ValIN );
	          }/*
	          else{
	               throw new Exception(){
	                    public String toString(){
	                         return "Invalid Register ID.";
	                    }
	               };
	          }*/
	          
	          System.out.println( RegID + " " + ValIN );
	     }
	     
	     ///
	     ///Generates the string representation of the RegisterFiles Object.
	     ///
	     public String toString(){
	          return "Integer Registers: " + 32 + "\n" +
	                 "    Values: \n" + INTregisters.toString() + "\n" +
	                 "FP Registers:      " + 32 + "\n" +
	                 "    Values: \n" + FPregisters.toString() + "\n" ;
	     } 
}
