
public class Trip {

    /**
     * This, class allows us to easily split and sort each line item in the array, by breaking each item into an index. We create an
     * object called trip which stores the relevant values in the constructor.
     **/

    String tripId;
    String departureTime;
    String stopId;
    String stopSequence;
    String stopHeadSign;
    String pickUpTypes;
    String dropOffType;
    String distanceTravelled;

    public Trip(String tripId, String departureTime, String stopId, String stopSequence, String stopHeadSign, String pickUpTypes, String dropOffType, String distanceTravelled){

        this.tripId = tripId;
        this.departureTime = departureTime.replace(" ","0");
        this.stopId = stopId;
        this.stopSequence = stopSequence;
        this.stopHeadSign = stopHeadSign;
        this.pickUpTypes =  pickUpTypes;
        this.dropOffType = dropOffType;
        this.distanceTravelled = distanceTravelled;

    }

}

