

public class CheckInDesk {
    /*
    This class is used to simulate check-in process
    need a queue, include the waiting passengers' info: name, booking code, flight code, luggage[dimension, weight], state
    is one of the thread
     */
    Passenger p = new Passenger();
    public int deskNum(){
        int desknum = 1;
        desknum += 1;
        return desknum;
    }

    public boolean gatState(boolean state){
        boolean checkornot = state;
        return checkornot;
    }

    public void baggage(){
        float dimension = p.getLuggageSize();
        float weight = p.getWeight();
        float fee = Flight.calulatefee(dimension,weight);

    }

    public static void genrateDesk(){

    }

    public static boolean checkAPassenger(Passenger p){
        boolean newState = p.state();
        return newState;
    }
}
