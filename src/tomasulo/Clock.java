package tomasulo;

public class Clock {
	   private int time;                     
	    static Clock clockInstance = null; 
	    
	    ///
	    ///class is a singleton so constructor is private.
	    ///
	    private Clock(){
	        time = 0;
	    }
	    
	    ///
	    ///returns singleton instance
	    ///
	    public static Clock getInstance(){
	        if (clockInstance == null)
	        	clockInstance = new Clock();
	        
	        	
	        
	        return clockInstance;
	    }
	    
	    ///
	    ///returns current cycles time
	    ///

	   public int getClock(){
	        return time;
	    }
	    
	    ///
	    ///Add Clock
	    ///
	   public void AddClock(){
	        time++;
	    }
	   
	   public void ResetClock(){
	        time=0;
	    }
	   
}
