/********
 NAME OF STUDENTS                         ID NUMBER
 1,TEKLEHAYMANOT PAWLOS                  UGR/178643/12
 2,YARED         TEWELDE                 UGR/170112/12
 3,ABRHA        TEKLAY                   UGR/178597/12
 4,TEKLE         H/SLASIE                UGR/170263/12

*/
package calendarit;

/**
 *
 * @author user
 */
import java.text.*; 
import java.util.*;
import java.util.Calendar.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class CalendarIT extends JFrame {
    JPanel center = new JPanel();
    static JLabel weekLabels[] = new JLabel[7];
    static JLabel[][] dayLabels = new JLabel[6][7];

    private static void updateTimeLabel(JLabel timeLabel) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        timeLabel.setText(dateFormat.format(new Date()));
        new javax.swing.Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateTimeLabel(timeLabel);
            }
        }).start();
    }

    private static void constructDateTable(JPanel center, int month, int year){
        Boolean isToday = Calendar.getInstance().get(Calendar.MONTH)+1 == 
                month && Calendar.getInstance().get(Calendar.YEAR) == year;
        String strWeeks[]  = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for(int i=0; i<7; i++){
            weekLabels[i].setText(strWeeks[i]);
            weekLabels[i].setFont(new Font("Arial", Font.BOLD, 16));
            Dimension size = weekLabels[i].getPreferredSize();
            weekLabels[i].setBounds(80*i+10, 0, size.width, size.height);
            weekLabels[i].setForeground(Color.BLACK);
            if(isToday && i+1 == Calendar.getInstance().get(Calendar.DAY_OF_WEEK))
                weekLabels[i].setForeground(Color.BLUE);
            center.add(weekLabels[i]);
        }

        GregorianCalendar firstDay = new GregorianCalendar(year, month-1, 1);
        int totalCurrentMonthDays = firstDay.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String presentDay = sdf.format(firstDay.getTime());
        int numWeek=0;
        for (int i=0; i<strWeeks.length; i++) {
            if(presentDay.equals(strWeeks[i])){
                numWeek=i;
                break;
            }
        }   
        int d=0;
        int startingRow=numWeek, row=7-numWeek;

        GregorianCalendar prevFirstDay = new GregorianCalendar(year, month-2, 1);
        int totalPreviousMonthDay = prevFirstDay.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        for (int i=0; i<startingRow; i++){
            JLabel dayElement = dayLabels[0][i];
            dayElement.setText(Integer.toString(totalPreviousMonthDay-startingRow+i+1));
            Dimension size = dayElement.getPreferredSize();
            dayElement.setBounds(80*i+15, 40, size.width+2, size.height);
            dayElement.setFont(new Font("Arial", Font.PLAIN, 14));
            dayElement.setForeground(Color.GRAY);
            center.add(dayElement);
        }
        for(int i=0; i<6; i++){
            for (int j=0; j<row; j++ ) {
                JLabel dayElement = dayLabels[i][startingRow+j];
                dayElement.setText(Integer.toString(d++%totalCurrentMonthDays+1));
                dayElement.setFont(new Font("italic", Font.PLAIN, 14));
                Dimension size = dayElement.getPreferredSize();
                dayElement.setBounds(80*(startingRow+j)+15, 40*(i+1), size.width, size.height);
                dayElement.setOpaque(false);
                dayElement.setForeground(Color.BLACK);
                if(d>totalCurrentMonthDays)
                    dayElement.setForeground(Color.GRAY);
                if(isToday && d==Calendar.getInstance().get(Calendar.DATE)){
                    dayElement.setOpaque(true);
                    dayElement.setBackground(Color.BLUE);
                    dayElement.setForeground(Color.gray);
                }
                center.add(dayElement);
            }
            startingRow=0;
            row=7;
        }
    }

    public CalendarIT() {
        /***********NORTH stuffs***********/
        JPanel north = new JPanel();
        //current real-time
        JLabel timeLabel  = new JLabel();
        timeLabel.setFont(new Font("italic", Font.PLAIN, 20));
        timeLabel.setForeground(new Color(128, 128, 0));
        updateTimeLabel(timeLabel);
        //month drop-down
        Integer[] months = new Integer[12];
        for(int i=0; i<months.length; i++) months[i] = i+1;
        JComboBox<Integer> monthDropdown = new JComboBox<>(months);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
        monthDropdown.setSelectedItem(currentMonth);
        //year drop-down
        Integer[] years = new Integer[200];
        for(int i=0; i<years.length; i++) years[i] = 1924+i;
        JComboBox<Integer> yearDropdown = new JComboBox<>(years);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        yearDropdown.setSelectedItem(currentYear);
        //listener for month and year drop-down
        monthDropdown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedMonth = (Integer) monthDropdown.getSelectedItem();
                int selectedYear = (Integer) yearDropdown.getSelectedItem();
                constructDateTable(center, selectedMonth, selectedYear);
            }
        });
        yearDropdown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedMonth = (Integer) monthDropdown.getSelectedItem();
                int selectedYear = (Integer) yearDropdown.getSelectedItem();
               constructDateTable(center, selectedMonth, selectedYear);
            }
        });
        //current date
        SimpleDateFormat simpleDF = new SimpleDateFormat ("EEEE MMMM dd, yyyy");
        GregorianCalendar date = new GregorianCalendar();
        JLabel currentDate = new JLabel(simpleDF.format(date.getTime()));
        currentDate.setFont(new Font("italic", Font.PLAIN, 14));
        /***********CENTER stuffs***********/
        center.setLayout(null);
        center.setBorder(new LineBorder(Color.BLACK));
        for(int i=0; i<weekLabels.length; i++) weekLabels[i] = new JLabel();
        for(int i=0; i<dayLabels.length; i++) 
            for(int j=0; j<dayLabels[1].length; j++)
                dayLabels[i][j] = new JLabel();
        /***********SOUTH stuffs***********/
        JPanel south = new JPanel(); 
        JButton jbtPrevious = new JButton("Previous");
        JButton jbtNext = new JButton("Next");
        JButton jbtToday = new JButton("Today");
        jbtPrevious.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedMonth = (Integer) monthDropdown.getSelectedItem();
                int selectedYear = (Integer) yearDropdown.getSelectedItem();
                if(selectedMonth==1){
                    monthDropdown.setSelectedItem(selectedMonth=12);
                    yearDropdown.setSelectedItem(--selectedYear);
                }
                else
                    monthDropdown.setSelectedItem(--selectedMonth);
                constructDateTable(center, selectedMonth, selectedYear);;
            }
        });
        jbtNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                  yearDropdown.setSelectedItem(currentYear);
                int selectedMonth = (Integer) monthDropdown.getSelectedItem();
                int selectedYear = (Integer) yearDropdown.getSelectedItem();
                if(selectedMonth==12){
                    monthDropdown.setSelectedItem(selectedMonth=1);
                    yearDropdown.setSelectedItem(++selectedYear);
                }
                else
                    monthDropdown.setSelectedItem(++selectedMonth);
                constructDateTable(center, selectedMonth, selectedYear);;
            }
        });
        jbtToday.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                monthDropdown.setSelectedItem(currentMonth);
                yearDropdown.setSelectedItem(currentYear);
                constructDateTable(center, currentMonth, currentYear);
            }
        });

        south.add(jbtPrevious);
        south.add(jbtNext);
        south.add(jbtToday);

        north.add(timeLabel);
        north.add(new JLabel("Month"));
        north.add(monthDropdown);
        north.add(new JLabel("Year"));
        north.add(yearDropdown);
        north.add(currentDate);

        constructDateTable(center, currentMonth, currentYear);

        add(north, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);
    }

    public static void main(String []args){

        CalendarIT frame = new CalendarIT();
        frame.setTitle("Calendar");
        frame.setSize(600, 405);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

