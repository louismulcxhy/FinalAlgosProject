
public class Trip {

    //creates trip object to be able to handle file easier - breaks each line into trip object assigning details to individual variables

    String tripID;
    String departureTime;
    String stopID;
    String stopSequence;
    String stopHeadSign;
    String pickUpTypes;
    String dropOffType;
    String distanceTravelled;
    
    public Trip(String tripID, String departureTime, String stopID, String stopSequence, 
    		String stopHeadSign, String pickUpTypes, String dropOffType, String distanceTravelled){

        this.tripID = tripID;
        this.departureTime = departureTime.replace(" ","0");
        this.stopID = stopID;
        this.stopSequence = stopSequence;
        this.stopHeadSign = stopHeadSign;
        this.pickUpTypes =  pickUpTypes;
        this.dropOffType = dropOffType;
        this.distanceTravelled = distanceTravelled;

    }

}

