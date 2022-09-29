package system;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.util.LinkedList;
import java.util.Queue;

import java.awt.ComponentOrientation;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.time.LocalTime;

import javax.swing.JTextField;

import java.lang.System;
import java.rmi.server.ExportException;


public class NewClock implements java.awt.event.ActionListener{
    private JLabel label;
    private JFrame frame;
    private JPanel panel;
    private PlaceHolder hoursTextField;
    private PlaceHolder minutesTextField;
    private PlaceHolder secondsTextField;
    private JTextField hourMinuteColon;
    private JTextField minuteSecondColon;
    private JTextField breakEstimator;

    Integer actionListenerFlag = 0;
    Integer startTime = 300;
    LocalTime myObj = LocalTime.now();
    long oldStaticTime;
    long newStaticTime;
    int finalTimeCalc;
    long testVal = System.currentTimeMillis() - 60000;
    long newTestVal;

    long totalPomedoroTime;

    String[] stringSplitter = new String[2];

    long currenTime = System.currentTimeMillis();
    long timeDifference;
    long totalTime = 0;

    int hour;
    int minute;
    int second;

    String hourS = "00:";
    String minuteS = "00";
    String secondS = ":00";

    int count = 0;
    int totalTimeEver;

    boolean inInstance = false;
    boolean inBreak = false;

    char[] dynamicStringToChar = new char[8];;

    Font universalFont = new Font("Serif", Font.PLAIN, 48);
        
    Queue<Long> newTimeInOldTimeOut = new LinkedList<>();
    public NewClock() {
        init();
    }
    public void init(){

    
    label = new JLabel();

        frame = new JFrame();
        panel = new JPanel();
        JButton buttonStart = new JButton("Start");
        JButton buttonStop = new JButton("Stop");
        hoursTextField = new PlaceHolder();
        minutesTextField = new PlaceHolder();
        secondsTextField = new PlaceHolder();
        hourMinuteColon = new JTextField();
        minuteSecondColon = new JTextField();
        breakEstimator = new JTextField();
        
        ActionListener listenerButtonStart = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              
                if(hasCorrectValues()){
                    actionListenerFlag = 1;
                }
                
            }
        };
        ActionListener listenerButtonStop = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionListenerFlag = 0;
            }
        };
        
        buttonStart.addActionListener(listenerButtonStart);
        buttonStop.addActionListener(listenerButtonStop);

        buttonStart.setSize(100, 25);
        buttonStart.setLocation(50, 150);
        
        buttonStop.setSize(100, 25);
        buttonStop.setLocation(150, 150);
    
        panel.add(buttonStart);
        panel.add(buttonStop);
        
        hoursTextField.setSize(55, 70);
        hoursTextField.setLocation(50, 50);
        hoursTextField.setFont(universalFont);
        hoursTextField.setDocument(new JTextFieldLimit(2));

        hourMinuteColon.setSize(20, 70);
        hourMinuteColon.setLocation(105, 50);
        hourMinuteColon.setFont(universalFont);

        minutesTextField.setSize(55, 70);
        minutesTextField.setLocation(125, 50);
        minutesTextField.setFont(universalFont);
        minutesTextField.setDocument(new JTextFieldLimit(2));

        minuteSecondColon.setSize(20, 70);
        minuteSecondColon.setLocation(180, 50);
        minuteSecondColon.setFont(universalFont);

        secondsTextField.setSize(55, 70);
        secondsTextField.setLocation(200, 50);
        secondsTextField.setFont(universalFont);
        secondsTextField.setDocument(new JTextFieldLimit(2));

        breakEstimator.setSize(55, 70);
        breakEstimator.setLocation(120, 200);

        hoursTextField.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        minutesTextField.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        secondsTextField.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        hourMinuteColon.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        minuteSecondColon.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        hourMinuteColon.setText(":");
        hourMinuteColon.setEditable(false);

        minuteSecondColon.setText(":");
        minuteSecondColon.setEditable(false);

        secondsTextField.setPlaceholder("00");
        minutesTextField.setPlaceholder("00");
        hoursTextField.setPlaceholder("00");

        panel.add(hoursTextField);
        panel.add(minutesTextField);
        panel.add(secondsTextField);
        panel.add(hourMinuteColon);
        panel.add(minuteSecondColon);
        panel.add(breakEstimator);

        label.setFont(new Font("Serif", Font.PLAIN, 28));
        label.setBounds(10, 20, 100, 100);
        label.setLocation(100, 100);

        panel.add(label);

        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(300, 300));
        frame.setVisible(true);
        frame.add(panel, BorderLayout.CENTER);
        frame.setTitle("TimeKeeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        
    }
    public String[] format(int finalTimeCalc){
        String[] formattedArray = new String[3];
        hour = finalTimeCalc/3600;
        minute = finalTimeCalc/60;
        second = finalTimeCalc;

        if(minute >= 60){
            minute -= (hour * 60);
        }
        if(second >= 60){
            second -= ((hour * 3600) + (minute * 60));
        }
        if(hour < 10){
            hourS = "0" + String.valueOf(hour);
        }
        if(minute < 10){
            minuteS = "0" + String.valueOf(minute);
        }
        if(second < 10){
            secondS = "0" + String.valueOf(second);
        }
        if(hour >= 10){
            hourS = String.valueOf(hour);
        }
        if(minute >= 10){
            minuteS = String.valueOf(minute);
        }
        if(second >= 10){
            secondS = String.valueOf(second);
        }
        formattedArray[0] = hourS;
        formattedArray[1] = minuteS;
        formattedArray[2] = secondS;

        return formattedArray;
    }
    
    public void changeTime() throws InterruptedException{
        
        hoursTextField.setEditable(false);
        minutesTextField.setEditable(false);
        secondsTextField.setEditable(false);
        breakEstimator.setEditable(false);

        inInstance = true;
        if(!inBreak){
           totalTimeEver = totalTimeEver + Integer.parseInt(minutesTextField.getText()); 
           System.out.println(totalTimeEver);
        }
        
        
      
        if(finalTimeCalc == 0){
            testVal =  System.currentTimeMillis() - ((Long.valueOf(hoursTextField.getText()) * 1000 * 3600)
             + (Long.valueOf(minutesTextField.getText()) * 1000 * 60)
             + (Long.valueOf(secondsTextField.getText()) * 1000)); 
            totalTime = 0;
     
            if(newTimeInOldTimeOut.size() >= 1){
                newTimeInOldTimeOut.remove();
            }
            
            currenTime = System.currentTimeMillis();
            oldStaticTime = System.currentTimeMillis();
            newTimeInOldTimeOut.add(oldStaticTime);
        }
        if(finalTimeCalc > 0){
            currenTime = System.currentTimeMillis() - newStaticTime;
        }  
        while(actionListenerFlag == 1){
            
            Thread.sleep(100);
            //current time - previous time marked
            newStaticTime = (System.currentTimeMillis() - currenTime);

            //add to line
            newTimeInOldTimeOut.add(newStaticTime);
            //remove from line
            oldStaticTime = newTimeInOldTimeOut.remove();
            //current difference
            timeDifference = oldStaticTime - newStaticTime;
            //new
            totalPomedoroTime = totalPomedoroTime + timeDifference;
            
            //add all differences
            totalTime = totalTime + timeDifference;
            
            //all differences - 10 minutes
            finalTimeCalc = (int)((totalTime - testVal) * 0.001);
       
            String[] formatter = format(finalTimeCalc);
            hoursTextField.setText(formatter[0]);
            minutesTextField.setText(formatter[1]);
            secondsTextField.setText(formatter[2]);

            if(isTimeUp()){
                hoursTextField.setText("");
                minutesTextField.setText("");
                secondsTextField.setText("");
                inInstance = false;
                if(Integer.parseInt(breakEstimator.getText()) >= 1){
                    JOptionPane.showMessageDialog(frame, "Time to take a Break!");
                    inBreak = true;
                    startBreak();
                }
                else{
                    hoursTextField.setEditable(true);
                    minutesTextField.setEditable(true);
                    secondsTextField.setEditable(true);
                    breakEstimator.setEditable(true);
                    inBreak = false;
                }
                break;
            }
        }    
    }
    
    public boolean isTimeUp(){
        if(finalTimeCalc == 0){
            return true;
        }
        return false;
    }
    public boolean setTime(){
        if(inInstance){
            return true;
        }
        return true;

    }
    public boolean hasCorrectValues(){
        Long parsedHour = (long) 0; 
        Long parsedMinute = (long) 0;
        Long parsedSecond = (long) 0;
        boolean flag = true;

        
        try {
            parsedHour = Long.parseLong(hoursTextField.getText());
            parsedMinute = Long.parseLong(minutesTextField.getText());
            parsedSecond = Long.parseLong(secondsTextField.getText());
            
        } catch (Exception e) {
            // TODO: handle exception
  
            flag = false;
            
        }
        if(hoursTextField.getText().length() > 0
        && minutesTextField.getText().length() > 0
        && secondsTextField.getText().length() > 0
        && parsedHour <= 59
        && parsedMinute <= 59
        && parsedSecond <= 59 && flag){
         
            return true;
        }
        return false;
        
    }
    public void updateBreakEstimator(){
        if(hasCorrectValues()){
      
        double setSeconds = 0;
        double setMinutes = 0;
        double setHours = 0;
        int setTotalTime;

        if(secondsTextField.getText().length() > 0){
            setSeconds = Double.parseDouble(secondsTextField.getText()) * .2;
        }
        if(minutesTextField.getText().length() > 0){
            setMinutes = (Double.parseDouble(minutesTextField.getText()) * 60) * .2;
        }
        if(hoursTextField.getText().length() > 0){
            setHours = (Double.parseDouble(hoursTextField.getText()) * 3600) * .2;
        }

        setTotalTime = (int) ((setSeconds + setMinutes + setHours) / 60);
        
     
            breakEstimator.setText("");   
            breakEstimator.setText(String.valueOf(setTotalTime));  
    
        }
    }

    public void startBreak(){
        hoursTextField.setText("00");
        minutesTextField.setText("0" + breakEstimator.getText());
        secondsTextField.setText("00");
    }
    
    
    public static void main(String[] args) throws InterruptedException {
        
        NewClock obj = new NewClock();
          
        while(true){
            Thread.sleep(100);
 
            if(obj.actionListenerFlag == 1 && obj.setTime() && obj.hasCorrectValues()){
         
                obj.changeTime();
                obj.actionListenerFlag = 0;
            }
            else{
       
                if(obj.setTime() && !obj.inInstance){
                    obj.updateBreakEstimator();
                }
            }
        }
        
        
        
    }
}
