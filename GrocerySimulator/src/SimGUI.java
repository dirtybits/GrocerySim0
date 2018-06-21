import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;

public class SimGUI extends JFrame{

    public boolean keepRunning = true;
    public static final int _CASHIERSLEEPINTERVAL = 100;
    public static final double LIKLYHOODOFACUSTOMER = 0.65;


    private JPanel panel1;
    private JProgressBar progressBar1;
    private JProgressBar progressBar5;
    private JProgressBar progressBar3;
    private JProgressBar progressBar4;
    private JProgressBar progressBar2;
    private JButton startSimulatorButton;
    private JCheckBox busy1CheckBox;
    private JCheckBox busy2CheckBox;
    private JCheckBox busy3CheckBox;
    private JCheckBox busy4CheckBox;
    private JCheckBox busy5CheckBox;
    private JButton stopButton;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextArea textArea1;

    private controlUpdater _myControlUpdater = null;

    private CashierManager myManager = null;

    public SimGUI() {
        startSimulatorButton.addActionListener(new ActionListener() {
            //@Override
            public void actionPerformed(ActionEvent e) {
                try{

                   //SimDriver.main(new String[0]);

                   _myControlUpdater = new controlUpdater(_CASHIERSLEEPINTERVAL);
                   _myControlUpdater.start();

                   startSimulation();

               } catch (Exception ex){
                   ex.printStackTrace();
               }
            }
        });
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _myControlUpdater.kill();
                _myControlUpdater = null;
            }
        });
    }

    public void updateControls(){

        progressBar1.setValue(myManager.cashiers.get(0).getPercentageDone());
        progressBar2.setValue(myManager.cashiers.get(1).getPercentageDone());
        progressBar3.setValue(myManager.cashiers.get(2).getPercentageDone());
        progressBar4.setValue(myManager.cashiers.get(3).getPercentageDone());
        progressBar5.setValue(myManager.cashiers.get(4).getPercentageDone());

        busy1CheckBox.setSelected(!(myManager.cashiers.get(0).isNotBusy()));
        busy2CheckBox.setSelected(!(myManager.cashiers.get(1).isNotBusy()));
        busy3CheckBox.setSelected(!(myManager.cashiers.get(2).isNotBusy()));
        busy4CheckBox.setSelected(!(myManager.cashiers.get(3).isNotBusy()));
        busy5CheckBox.setSelected(!(myManager.cashiers.get(4).isNotBusy()));



        Integer i = myManager.cashiers.get(0).line.size();
        textField1.setText(i.toString());

        i = myManager.cashiers.get(1).line.size();
        textField2.setText(i.toString());

        i = myManager.cashiers.get(2).line.size();
        textField3.setText(i.toString());

        i = myManager.cashiers.get(3).line.size();
        textField4.setText(i.toString());

        i = myManager.cashiers.get(4).line.size();
        textField5.setText(i.toString());
   }


    public static void main(String args[]){
        JFrame frame = new JFrame("Grocery Simulator");
        frame.setContentPane(new SimGUI().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);



    }

    private void startSimulation(){
        myManager = new CashierManager(_CASHIERSLEEPINTERVAL);
    }
    private void waitForCashiersToFinish(CashierManager manager){
        while(!manager.cashiersDone()){
            try {
                Thread.currentThread().wait(100);
            }catch(java.lang.InterruptedException e){}
    }}
    private void printStats(CashierManager manager){

        long durStats[] = manager.getStats();

        textArea1.setText("");

        Long l = durStats[0] / 1000000000;
        this.textArea1.append("Average Checkout Time: " + l.toString() + " Minute(s).\n");

        l = durStats[1] / 1000000000;
        this.textArea1.append("Average Line Time: " + l.toString() + " Minute(s).\n");

        l = durStats[0]  / 1000000000 + durStats[1] / 1000000000;
        this.textArea1.append("Average Total Time: " + l.toString() + " Minute(s).\n");

        l = durStats[3];
        this.textArea1.append("Customers Served: " + l.toString() + ".\n");
    }
    private class controlUpdater extends Thread{
        private int _sleepTime = 0;
        private boolean _killThread = false;

        controlUpdater(int sleepTime){
            _sleepTime = sleepTime;
        }

        public void start(){
            if (_sleepTime != 0){
                super.start();
            }
        }

        public void run(){

            while (_killThread == false){
                try {
                        Double value = Math.random();
                        if (value > LIKLYHOODOFACUSTOMER){
                            // add a new customer
                            Customer c = new Customer();
                            myManager.addCustomer(c);
                        }

                    updateControls();
                    Thread.sleep(_sleepTime);
                } catch (InterruptedException e) {}
                  catch (java.lang.NullPointerException f){
                  }
                }

                while (!myManager.cashiersDone()){
                    updateControls();
                }
            printStats(myManager);
        }
        public void kill(){
            _killThread = true;
        }
    }
    //this is a comment
    // this is a 2nd comment
}