import java.util.ArrayList;

public class CashierManager {
    private int _cashierCount = 5;
    private static ArrayList<Long> _lineTime = new ArrayList<>();
    private static ArrayList<Long> _checkoutTime = new ArrayList<>();

    public ArrayList<Cashier> cashiers = new ArrayList<>();

    // constructor
    CashierManager(int cashierSleepInterval){
        for (int i = 0; i < _cashierCount; i++){
            Cashier c = new Cashier(cashierSleepInterval, this);
            cashiers.add(c);
            c.start();
        }
    }

    // a logger function for keeping track of times
    public static void times(long lineTime, long checkoutTime){
        _lineTime.add(lineTime);
        _checkoutTime.add(checkoutTime);
    }

    // ads a customer to the end of the shortest line
    public void addCustomer(Customer c){
        int shortestLineLength = Integer.MAX_VALUE;
        Cashier shortestLine = null;

        for(Cashier cash: cashiers){
            if (cash.line.size() < shortestLineLength){
                shortestLineLength = cash.line.size();
                shortestLine = cash;
            }
        }


        shortestLine.addCustomerToLine(c);
    }

    // the logic behind rearranging the lines
    public void rearrangeLines() {
        while (shouldBeRearranged()) {

            int longestLine = 0;
            Cashier longestLineCash = null;

            int shortestLine = Integer.MAX_VALUE;
            Cashier shortestLineCash = null;


            for(Cashier cash: cashiers){
                if (cash.line.size() > longestLine){
                    longestLine = cash.line.size();
                    longestLineCash = cash;
                }else if(cash.line.size() < shortestLine){
                    shortestLine = cash.line.size();
                    shortestLineCash = cash;
                }
            }
            Customer customer = longestLineCash.getCustomerFromEndOfLine();
            if (customer != null){
                shortestLineCash.addCustomerToLine(customer);
            }
        }
    }

    //returns a boolean value indicating wether the lines should be rearranged
    private boolean shouldBeRearranged(){
        int longestLine = 0;
        int shortestLine = Integer.MAX_VALUE;

        for(Cashier cash: cashiers){
            if (cash.line.size() > longestLine){
                longestLine = cash.line.size();
            }else if(cash.line.size() < shortestLine){
                shortestLine = cash.line.size();
            }
        }

        return ((longestLine - shortestLine) > 1);
    }
    // returns the stats of the cashiers as of the current time
    public long[] getStats(){

        long averageLineTime = 0;
        long averageCheckoutTime = 0;
        long averageTotalTime;

        int size = _lineTime.size();

        //populate the average variables
        for (int i = 0; i<= size - 1; i++){
            averageLineTime += _lineTime.get(i);
            averageCheckoutTime +=_checkoutTime.get(i);
        }
        //divide by the number of indecies

        averageLineTime /= size;
        averageCheckoutTime /= size;

        averageTotalTime = averageLineTime + averageCheckoutTime;

        long customersServed = 0;
        for (int i = 0; i<= _cashierCount - 1; i++){
            customersServed += cashiers.get(i).getServedCustomers();
        }

        long retVal[] = new long[4];

        retVal[0] = averageLineTime;
        retVal[1] = averageCheckoutTime;
        retVal[2] = averageTotalTime;
        retVal[3] = customersServed;

        return retVal;
    }

    //returns true if all cashiers have finished
    public boolean cashiersDone(){
        boolean flag = true;

        for (int i = cashiers.size() - 1; i >= 0; i--){
            if(!cashiers.get(i).isNotBusy()){
                flag = false;
            }
        }

        return flag;
    }

    // returns true is the cashier threads were all successfully destroyed.
    public boolean endSimulation(){
        try {
            for (int i = cashiers.size() - 1; i >= 0; i--) {
                cashiers.get(i).kill();
            }
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
//c