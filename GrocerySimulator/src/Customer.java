import java.util.Random;

public class Customer {

    private int _itemCount = 0;
    private double _timingMultiplier = 1.0;
    private long _lineStartTime = 0;
    private long _lineEndTime = 0;

    public Customer(){
        // preliminary numbers
        _itemCount = getRandomInteger(50, 1);
        _lineStartTime = System.nanoTime();

    }

    //returns a long from the arrival time of this cusomter
    public long getArrivalTime() {
        return _lineStartTime;
    }
    //returns the time spent in line
    public long getLineTime() {
        return (_lineEndTime - _lineStartTime);
    }

    // used for timing the serving of a customer
    public void startedServing(){
        _lineEndTime = System.nanoTime();
    }

    // returns the time it will take to checkout this customer
    public int getCheckoutTime() {
        return (_itemCount * (int)(_timingMultiplier* 1000))/ 1000;
    }

    //returns a random integer
    private int getRandomInteger(int upperBound, int lowerBound) {
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());

        int randomNum = rand.nextInt((upperBound - lowerBound) + 1) + lowerBound;
        return randomNum;
    }
}
//b