package calendarPack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.Toolkit;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

class CalendarDataManager{ // 6*7배열에 나타낼 달력 값을 구하는 class
	static final int CAL_WIDTH = 7;
	final static int CAL_HEIGHT = 6;
	
	int calDates[][] = new int[CAL_HEIGHT][CAL_WIDTH];
	int calYear;
	int calMonth;
	int calDayOfMon;
	final int calLastDateOfMonth[]={31,28,31,30,31,30,31,31,30,31,30,31};
	int calLastDate;
	Calendar today = Calendar.getInstance();
	Calendar cal;

	JComboBox<String> jcomtodayyear,jcomtodaymonth,jcomtodayday, jcomhour, jcomminute;
	
	public CalendarDataManager(){ 
		setToday(); 
	}
	public void setToday(){
		calYear = today.get(Calendar.YEAR); 
		calMonth = today.get(Calendar.MONTH);
		calDayOfMon = today.get(Calendar.DAY_OF_MONTH);
		makeCalData(today);
	}
	private void makeCalData(Calendar cal){
		// 1일의 위치와 마지막 날짜를 구함 
		int calStartingPos = (cal.get(Calendar.DAY_OF_WEEK)+7-(cal.get(Calendar.DAY_OF_MONTH))%7)%7;
		if(calMonth == 1) calLastDate = calLastDateOfMonth[calMonth] + leapCheck(calYear);
		else calLastDate = calLastDateOfMonth[calMonth];
		// 달력 배열 초기화
		for(int i = 0 ; i<CAL_HEIGHT ; i++){
			for(int j = 0 ; j<CAL_WIDTH ; j++){
				calDates[i][j] = 0;
			}
		}
		// 달력 배열에 값 채워넣기
		for(int i = 0, num = 1, k = 0 ; i<CAL_HEIGHT ; i++){
			if(i == 0) k = calStartingPos;
			else k = 0;
			for(int j = k ; j<CAL_WIDTH ; j++){
				if(num <= calLastDate) calDates[i][j]=num++;
			}
		}
	}
	private int leapCheck(int year){ // 윤년인지 확인하는 함수
		if(year%4 == 0 && year%100 != 0 || year%400 == 0) return 1;
		else return 0;
	}
	public void moveMonth(int mon){ // 현재달로 부터 n달 전후를 받아 달력 배열을 만드는 함수(1년은 +12, -12달로 이동 가능)
		calMonth += mon;
		if(calMonth>11) while(calMonth>11){
			calYear++;
			calMonth -= 12;
		} else if (calMonth<0) while(calMonth<0){
			calYear--;
			calMonth += 12;
		}
		cal = new GregorianCalendar(calYear,calMonth,calDayOfMon);
		makeCalData(cal);
	}
	
	public void selectBox(int y, int m, int d, int ht, int mt){
		// 날짜배열 선언
		ArrayList<String> yeararray; // 년도
		ArrayList<String> montharray; // 월
		ArrayList<String> hourarray; // 시
		ArrayList<String> minutearray; // 분

		// 현재 날짜
		int toyear = y;
		int tomonth = m+1;
		int today = d;

		// 년도
		yeararray = new ArrayList<String>();

		for(int i = toyear; i<= toyear+50; i++){
			yeararray.add(String.valueOf(i));
			//System.out.println(i);
		}  
		
		jcomtodayyear= new JComboBox<String>(yeararray.toArray(new String[yeararray.size()]));
		jcomtodayyear.setBounds(5, 5, 70, 30);
		jcomtodayyear.setSelectedItem(String.valueOf(toyear));

		// 월
		  
		montharray = new ArrayList<String>();
		  
		for(int i = 1; i <= 12; i++){
			montharray.add(addZeroString(i));
		   //System.out.println(i);
		}  
		jcomtodaymonth = new JComboBox<String>(montharray.toArray(new String[montharray.size()]));
		jcomtodaymonth.setBounds(80, 5, 70, 30);
		String mcom = tomonth >= 10?String.valueOf(tomonth):"0"+tomonth;
		jcomtodaymonth.setSelectedItem(mcom);

		// 일

		jcomtodayday = new JComboBox<String>();				
		DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) jcomtodayday.getModel();

		jcomtodayyear.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				model.removeAllElements();
				
				int a = calLastDateOfMonth[Integer.parseInt(jcomtodaymonth.getSelectedItem().toString())-1];
				if(Integer.parseInt(jcomtodaymonth.getSelectedItem().toString()) == 2) a += leapCheck(Integer.parseInt(jcomtodayyear.getSelectedItem().toString()));
				for(int i = 1; i <= a; i++){         
					model.addElement(addZeroString(i));
				}
				jcomtodayday.setModel(model);
			}
		});
		
		jcomtodaymonth.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				model.removeAllElements();
				
				int a = calLastDateOfMonth[Integer.parseInt(jcomtodaymonth.getSelectedItem().toString())-1];
				if(Integer.parseInt(jcomtodaymonth.getSelectedItem().toString()) == 2) a += leapCheck(Integer.parseInt(jcomtodayyear.getSelectedItem().toString()));
				for(int i = 1; i <= a; i++){         
					model.addElement(addZeroString(i));
				}
				jcomtodayday.setModel(model);
			}
		});

		model.removeAllElements();
		
		int a = calLastDateOfMonth[Integer.parseInt(jcomtodaymonth.getSelectedItem().toString())-1];
		if(Integer.parseInt(jcomtodaymonth.getSelectedItem().toString()) == 2) a += leapCheck(Integer.parseInt(jcomtodayyear.getSelectedItem().toString()));
		for(int i = 1; i <= a; i++){         
			model.addElement(addZeroString(i));
		}
		jcomtodayday.setModel(model);
		String dcom = today >= 10?String.valueOf(today):"0"+today;
		jcomtodayday.setSelectedItem(dcom);
		
		// 시
		hourarray = new ArrayList<String>();
		  
		for(int i = 0; i < 24; i++){
			hourarray.add(addZeroString(i));
		}  
		jcomhour = new JComboBox<String>(hourarray.toArray(new String[hourarray.size()]));
		jcomhour.setBounds(80, 5, 70, 30);
		String hcom = ht >= 10?String.valueOf(ht):"0"+ht;
		jcomhour.setSelectedItem(hcom);
		
		//분
		minutearray = new ArrayList<String>();
		  
		for(int i = 0; i < 60; i++){
			minutearray.add(addZeroString(i));
		}  
		jcomminute = new JComboBox<String>(minutearray.toArray(new String[minutearray.size()]));
		jcomminute.setBounds(80, 5, 70, 30);
		String tcom = mt >= 10?String.valueOf(mt):"0"+mt;
		jcomminute.setSelectedItem(tcom);

		
		
	}
	private String addZeroString(int k){
		String value=Integer.toString(k);
	    if(value.length()==1) {
	    	value="0"+value;
	    }
	    return value;
	}

}

public class calendar extends CalendarDataManager{ // CalendarDataManager의 GUI + 메모기능 + 시계
	// 창 구성요소와 배치도
	JFrame mainFrame;
	ImageIcon icon = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
		
	JPanel calOpPanel;
	JButton todayBut;
	JLabel todayLab;
	JButton lYearBut;
	JButton lMonBut;
	JLabel curMMYYYYLab;
	JButton nMonBut;
	JButton nYearBut;
	ListenForCalOpButtons lForCalOpButtons = new ListenForCalOpButtons();
	
	JPanel calPanel;
	JButton weekDaysName[];
	JButton dateButs[][] = new JButton[6][7];
	listenForDateButs lForDateButs = new listenForDateButs(); 
	
	JPanel infoPanel;
	JLabel infoClock;

	// 선택 날짜의 스케줄 목록을 보여주는 UI
	JPanel indexPanel;
	JLabel selectedDate;
	JPanel indexSchedulePanel;

	JButton makeBut;
	
	// 일정을 등록하기 위한 UI : Dialog 창을 통해서 보여지고, 등록 가능하다.
	JPanel memoPanel;
	JPanel memoTextPanel;
	JLabel setName;
	JTextField scheduleName;
	JTextArea memoArea;
	JScrollPane memoAreaSP;
	JPanel memoNamePanel;
	JPanel memoSubPanel;
	
	JButton saveBut; 
	JButton delBut; 
	JButton clearBut;
	JButton alarmBut;

	
	JPanel frameBottomPanel;
	JLabel bottomInfo = new JLabel("Welcome to Memo Calendar!");
	
	ArrayList<String> alarmIndex;
	ArrayList<String> alarmTime;
	
	//상수, 메세지
	final String WEEK_DAY_NAME[] = { "일", "월", "화", "수", "목", "금", "토" };
	final String title = "Scheduler";
	
	final String SaveButMsg1 = "를 저장하였습니다.";
	final String SaveButMsg2 = "일정 내용을 작성해 주세요.";
	final String SaveButMsg3 = "<html><font color=red>ERROR : 파일 쓰기 실패</html>";
	final String DelButMsg1 = "저장된 스케줄을 삭제하였습니다.";
	final String DelButMsg2 = "작성되지 않았거나 이미 삭제된 스케줄입니다.";
	final String DelButMsg3 = "<html><font color=red>ERROR : 파일 삭제 실패</html>";
	final String ClrButMsg1 = "입력된 스케줄을 지웠습니다.";
	
	LunarDateAndHoliday lunarDateAndHoliday = new LunarDateAndHoliday();
	private JDialog scheduleDialog;

	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				new calendar();
			}
		});
	}
	public calendar(){ //구성요소 순으로 정렬되어 있음. 각 판넬 사이에 빈줄로 구별
		
		//mediaPlayer = new MediaPlayer(m);
		mainFrame = new JFrame(title);
		//mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(1000,700);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setResizable(false);
		mainFrame.setIconImage(icon.getImage());
		try{
			UIManager.setLookAndFeel ("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");//LookAndFeel Windows 스타일 적용
			SwingUtilities.updateComponentTreeUI(mainFrame) ;
		}catch(Exception e){
			bottomInfo.setText("ERROR : LookAndFeel setting failed");
		}
		
		calOpPanel = new JPanel();
		todayBut = new JButton("Today");
		todayBut.setToolTipText("Today");
		todayBut.addActionListener(lForCalOpButtons);
		todayLab = new JLabel(today.get(Calendar.MONTH)+1+"/"+today.get(Calendar.DAY_OF_MONTH)+"/"+today.get(Calendar.YEAR));
		lYearBut = new JButton("<<");
		lYearBut.setToolTipText("Previous Year");
		lYearBut.addActionListener(lForCalOpButtons);
		lMonBut = new JButton("<");
		lMonBut.setToolTipText("Previous Month");
		lMonBut.addActionListener(lForCalOpButtons);
		curMMYYYYLab = new JLabel("<html><table width=100><tr><th><font size=5>"+((calMonth+1)<10?"&nbsp;":"")+(calMonth+1)+" / "+calYear+"</th></tr></table></html>");
		nMonBut = new JButton(">");
		nMonBut.setToolTipText("Next Month");
		nMonBut.addActionListener(lForCalOpButtons);
		nYearBut = new JButton(">>");
		nYearBut.setToolTipText("Next Year");
		nYearBut.addActionListener(lForCalOpButtons);
		calOpPanel.setLayout(new GridBagLayout());
		GridBagConstraints calOpGC = new GridBagConstraints();
		calOpGC.gridx = 1;
		calOpGC.gridy = 1;
		calOpGC.gridwidth = 2;
		calOpGC.gridheight = 1;
		calOpGC.weightx = 1;
		calOpGC.weighty = 1;
		calOpGC.insets = new Insets(5,5,0,0);
		calOpGC.anchor = GridBagConstraints.WEST;
		calOpGC.fill = GridBagConstraints.NONE;
		calOpPanel.add(todayBut,calOpGC);
		calOpGC.gridwidth = 3;
		calOpGC.gridx = 2;
		calOpGC.gridy = 1;
		calOpPanel.add(todayLab,calOpGC);
		calOpGC.anchor = GridBagConstraints.CENTER;
		calOpGC.gridwidth = 1;
		calOpGC.gridx = 1;
		calOpGC.gridy = 2;
		calOpPanel.add(lYearBut,calOpGC);
		calOpGC.gridwidth = 1;
		calOpGC.gridx = 2;
		calOpGC.gridy = 2;
		calOpPanel.add(lMonBut,calOpGC);
		calOpGC.gridwidth = 2;
		calOpGC.gridx = 3;
		calOpGC.gridy = 2;
		calOpPanel.add(curMMYYYYLab,calOpGC);
		calOpGC.gridwidth = 1;
		calOpGC.gridx = 5;
		calOpGC.gridy = 2;
		calOpPanel.add(nMonBut,calOpGC);
		calOpGC.gridwidth = 1;
		calOpGC.gridx = 6;
		calOpGC.gridy = 2;
		calOpPanel.add(nYearBut,calOpGC);
		
		calPanel=new JPanel();
		weekDaysName = new JButton[7];
		for(int i=0 ; i<CAL_WIDTH ; i++){
			weekDaysName[i]=new JButton(WEEK_DAY_NAME[i]);
			weekDaysName[i].setBorderPainted(false);
			weekDaysName[i].setContentAreaFilled(false);
			weekDaysName[i].setForeground(Color.WHITE);
			if(i == 0) weekDaysName[i].setBackground(new Color(200, 50, 50));
			else if (i == 6) weekDaysName[i].setBackground(new Color(50, 100, 200));
			else weekDaysName[i].setBackground(new Color(150, 150, 150));
			weekDaysName[i].setOpaque(true);
			weekDaysName[i].setFocusPainted(false);
			calPanel.add(weekDaysName[i]);
		}
		for(int i=0 ; i<CAL_HEIGHT ; i++){
			for(int j=0 ; j<CAL_WIDTH ; j++){
				dateButs[i][j]=new JButton();
				dateButs[i][j].setBorderPainted(false);
				dateButs[i][j].setContentAreaFilled(false);
				dateButs[i][j].setBackground(Color.WHITE);
				dateButs[i][j].setOpaque(true);
				dateButs[i][j].addActionListener(lForDateButs);
				calPanel.add(dateButs[i][j]);
			}
		}
		calPanel.setLayout(new GridLayout(0,7,2,2));
		calPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		showCal(); // 달력을 표시
						
		infoPanel = new JPanel();
		infoPanel.setLayout(new BorderLayout());
		infoClock = new JLabel("", SwingConstants.RIGHT);
		infoClock.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		infoPanel.add(infoClock, BorderLayout.NORTH);

		
		
		// Schedule Index;
		selectedDate = new JLabel("<Html><font size=3>"+today.get(Calendar.YEAR)+"-"+(today.get(Calendar.MONTH)+1)+"-"+today.get(Calendar.DAY_OF_MONTH)+"&nbsp;(Today)</html>", SwingConstants.LEFT);
		selectedDate.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

		Dimension selectedDateSize=selectedDate.getPreferredSize();
		selectedDateSize.height = 20;
		selectedDate.setPreferredSize(selectedDateSize);
		
		indexPanel = new JPanel();
		indexPanel.setBorder(BorderFactory.createTitledBorder("Schdule Index"));
		indexPanel.setLayout(new BorderLayout());
		
		indexSchedulePanel = new JPanel();
		//indexSchedulePanel.setLayout(new BoxLayout(indexSchedulePanel, BoxLayout.Y_AXIS));
		indexSchedulePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		makeScheduleIndex(calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDayOfMon<10?"0":"")+calDayOfMon);

		makeBut = new JButton("New Schedule");
		makeBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				makeScheduleDialog();
			}
		});

		indexPanel.add(selectedDate, BorderLayout.NORTH);
		indexPanel.add(indexSchedulePanel, BorderLayout.CENTER);
		indexPanel.add(makeBut, BorderLayout.SOUTH);
		
		// 일정 추가 Part
		scheduleDialog = new JDialog(mainFrame);
		scheduleDialog.setSize(500, 400);
		scheduleDialog.setLocationRelativeTo(null);
		
		memoPanel=new JPanel();
		memoPanel.setBorder(BorderFactory.createTitledBorder("Schedule Contents"));
		
		setName = new JLabel("Schedule Name");
		scheduleName = new JTextField(32);

		memoArea = new JTextArea();
		memoArea.setLineWrap(true);
		memoArea.setWrapStyleWord(true);
		memoAreaSP = new JScrollPane(memoArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		memoNamePanel = new JPanel();
		memoNamePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		memoNamePanel.add(setName);
		memoNamePanel.add(scheduleName);

		
		memoPanel.setLayout(new BorderLayout());
		memoPanel.add(memoNamePanel, BorderLayout.NORTH);
		memoPanel.add(memoAreaSP,BorderLayout.CENTER);
		scheduleDialog.add(memoPanel);
		//
		
		//calOpPanel, calPanel을  frameSubPanelWest에 배치
		JPanel frameSubPanelWest = new JPanel();
		
		Dimension calOpPanelSize = calOpPanel.getPreferredSize();
		calOpPanelSize.height = 90;
		calOpPanel.setPreferredSize(calOpPanelSize);
		
		frameSubPanelWest.setLayout(new BorderLayout());
		frameSubPanelWest.add(calOpPanel,BorderLayout.NORTH);
		frameSubPanelWest.add(calPanel,BorderLayout.CENTER);

		//infoPanel, memoPanel을  frameSubPanelEast에 배치
		
		JPanel frameSubPanelEast = new JPanel();
		
		Dimension infoPanelSize=infoPanel.getPreferredSize();
		infoPanelSize.height = 65;
		infoPanel.setPreferredSize(infoPanelSize);
		
		frameSubPanelEast.setLayout(new BorderLayout());
		frameSubPanelEast.add(infoPanel,BorderLayout.NORTH);
		frameSubPanelEast.add(indexPanel,BorderLayout.CENTER);

		Dimension frameSubPanelWestSize = frameSubPanelWest.getPreferredSize();
		frameSubPanelWestSize.width = 600;
		frameSubPanelWest.setPreferredSize(frameSubPanelWestSize);
		
		//bottom Panel..
		frameBottomPanel = new JPanel();
		frameBottomPanel.add(bottomInfo);
		
		
		//frame에 전부 배치
		mainFrame.setLayout(new BorderLayout());
		mainFrame.add(frameSubPanelWest, BorderLayout.WEST);
		mainFrame.add(frameSubPanelEast, BorderLayout.CENTER);
		mainFrame.add(frameBottomPanel, BorderLayout.SOUTH);
		mainFrame.setVisible(true);

		// 트레이 아이콘으로 변환됨.
		
		TrayIconHandler.registerTrayIcon(
				 Toolkit.getDefaultToolkit().getImage("src\\calendarPack\\icon.png"),
				"Scheduler",
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
						mainFrame.setVisible(true);
						mainFrame.setExtendedState(JFrame.NORMAL);
					}
				}
			);
		
		TrayIconHandler.addMenu("알람 목록");
		TrayIconHandler.addItem("Exit", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		focusToday(); //현재 날짜에 focus를 줌 (mainFrame.setVisible(true) 이후에 배치해야함)
		
		//알람 데이터 가져옴.
		alarmIndex = new ArrayList<String>();
		alarmTime = new ArrayList<String>();
		getAlarmTime();
		
		//Thread 작동(시계, bottomMsg 일정시간후 삭제)
		ThreadConrol threadCnl = new ThreadConrol();
		threadCnl.start();	
		
	} 
	// 알람 삭제 함수
	private void deleteAlarm(String aName) {
		int index;
		if(alarmIndex.contains(aName)) {
			index = alarmIndex.indexOf(aName);
			alarmIndex.remove(index);
			alarmTime.remove(index);
			try {
				File f = new File("AlarmData");
				if(!f.isDirectory()) f.mkdir();
				
				BufferedWriter buf = new BufferedWriter(new FileWriter("AlarmData/alarm.txt"));
				
				for(int i=0; i<alarmTime.size(); i++) {
					buf.write(alarmTime.get(i)+alarmIndex.get(i));
				}
				buf.flush();
				buf.close();
			}catch(FileNotFoundException e) {
			}catch(IOException e) {
			}
		}
	}

	private void getAlarmTime() {
		alarmIndex.clear();
		alarmTime.clear();
		try {
			BufferedReader buf = new BufferedReader(new FileReader("AlarmData/alarm.txt"));
			String line = "";
			while((line = buf.readLine()) != null) {
				TrayIconHandler.addItemToMenu("알람 목록", line.substring(14, line.length()), new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						int r = JOptionPane.showConfirmDialog(mainFrame, "알람을 삭제할까요?","알람 삭제",JOptionPane.YES_NO_OPTION);		
						if(r == JOptionPane.YES_OPTION) {
							// 알람 삭제 함수.
							deleteAlarm(((MenuItem)e.getSource()).getLabel());
							((MenuItem)e.getSource()).getParent().remove((MenuItem)e.getSource());
						}
					}
				});
				alarmIndex.add(line.substring(14, line.length()));
				alarmTime.add(line.substring(0, 14));
			}
			buf.close();
		}catch(FileNotFoundException e) {
		}catch(IOException e) {
			System.out.println(e);
		}
	}
	
	private void focusToday(){
		if(today.get(Calendar.DAY_OF_WEEK) == 1)
			dateButs[today.get(Calendar.WEEK_OF_MONTH)][today.get(Calendar.DAY_OF_WEEK)-1].requestFocusInWindow();
		else
			dateButs[today.get(Calendar.WEEK_OF_MONTH)-1][today.get(Calendar.DAY_OF_WEEK)-1].requestFocusInWindow();
	}
	
	// 일정 기록을 위한 다이얼로그 창 만들기 - New Schedule 생성을 위해 메서드 오버로딩.
	private void makeScheduleDialog() {
		makeScheduleDialog("");
	}
	
	private void makeScheduleDialog(String Name) {
		
		scheduleDialog.setTitle(calYear+"-"+((calMonth+1)<10?"0":"")+(calMonth+1)+"-"+(calDayOfMon<10?"0":"")+calDayOfMon+"/"+Name+" Add Schedule");
		String path = "MemoData/"+calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDayOfMon<10?"0":"")+calDayOfMon;
		scheduleDialog.setVisible(true);
		scheduleName.setText(Name);

		saveBut = new JButton("Save"); 
		delBut = new JButton("Delete");
		clearBut = new JButton("Clear");
		alarmBut = new JButton("Alarm");

		memoSubPanel = new JPanel();

		memoSubPanel.add(saveBut);
		memoSubPanel.add(delBut);
		memoSubPanel.add(clearBut);
		memoSubPanel.add(alarmBut);

		memoPanel.add(memoSubPanel,BorderLayout.SOUTH);
		
		// 일정을 추가했을 때, 텍스트 파일로 저장함.
		saveBut.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String path = "MemoData/"+calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDayOfMon<10?"0":"")+calDayOfMon;

				try {
					File f= new File("MemoData");
					if(!f.isDirectory()) f.mkdir();
					f = new File(path);
					if(!f.isDirectory()) f.mkdir();
					
					String setScheduleName = scheduleName.getText();
					String memo = memoArea.getText();
					if(memo.length()>0){
						BufferedWriter out = new BufferedWriter(new FileWriter(path+"/"+setScheduleName+".txt"));
						String str = memoArea.getText();
						out.write(str);  
						out.close();
						showCal();
						
						makeScheduleIndex(calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDayOfMon<10?"0":"")+calDayOfMon);
						scheduleDialog.setVisible(false);
						bottomInfo.setText(path+"/"+setScheduleName+".txt"+SaveButMsg1);
					}
					else 
						JOptionPane.showMessageDialog(scheduleDialog, SaveButMsg2);		
				} catch (IOException e) {
					bottomInfo.setText(SaveButMsg3);
				}
			}					
		});
		//일정 삭제
		delBut.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String path = "MemoData/"+calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDayOfMon<10?"0":"")+calDayOfMon;

				memoArea.setText("");
				String setScheduleName = scheduleName.getText();
				File f =new File(path+"/"+setScheduleName+".txt");
				if(f.exists()){
					f.delete();
					showCal();

					makeScheduleIndex(calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDayOfMon<10?"0":"")+calDayOfMon);
					scheduleDialog.setVisible(false);
					bottomInfo.setText(DelButMsg1);
				}
				else 
					bottomInfo.setText(DelButMsg2);					
			}					
		});
		// textArea 지우기
		clearBut.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				memoArea.setText(null);
				bottomInfo.setText(ClrButMsg1);
			}
		});
		// 알람 추가
		alarmBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				selectBox(calYear, calMonth, calDayOfMon, today.get(Calendar.HOUR), today.get(Calendar.MINUTE));
				
				JDialog dialog = new JDialog(mainFrame, "Alarm Setting");
				JLabel setName = new JLabel("알람 이름");
				JTextField alarmName = new JTextField(30);
				alarmName.setText(scheduleName.getText());
				
				JLabel setTime = new JLabel("알람 시간");
				
				JButton okButton = new JButton("알람 추가");
				
				JPanel dialogPanel = new JPanel();
				dialogPanel.setLayout(new GridLayout(1,5));
				dialogPanel.add(jcomtodayyear);
				dialogPanel.add(jcomtodaymonth);
				dialogPanel.add(jcomtodayday);
				dialogPanel.add(jcomhour);
				dialogPanel.add(jcomminute);
				
				okButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						try {
							File f = new File("AlarmData");
							if(!f.isDirectory()) f.mkdir();
							
							BufferedWriter buf = new BufferedWriter(new FileWriter("AlarmData/alarm.txt",true));
							// 알람 기록 : YYYYMMDDHHMM00
							String yyyy = (String)jcomtodayyear.getSelectedItem();
							String mm = (String)jcomtodaymonth.getSelectedItem();
							String dd = (String)jcomtodayday.getSelectedItem();
							String ht = (String)jcomhour.getSelectedItem();
							String mt = (String)jcomminute.getSelectedItem();
							
							PrintWriter pw = new PrintWriter(buf,true);
							pw.write(yyyy+mm+dd+ht+mt+"00"+alarmName.getText()+"\n");
							
							pw.flush();
							pw.close();
							buf.close();
							
						}catch(FileNotFoundException ex) {
						}catch(IOException ex) {
							System.out.println(ex);
						}
						getAlarmTime();
						dialog.setVisible(false);
					}
				});
				
				dialog.setLayout(new GridLayout(5,1));
				dialog.add(setName);
				dialog.add(alarmName);
				dialog.add(setTime);
				dialog.add(dialogPanel);
				dialog.add(okButton);
				
				dialog.setSize(300, 300);
				dialog.setVisible(true);
				dialog.setLocationRelativeTo(null);
				
			}
		});
		
		// 처음 실행할 때 불러오는 오늘 일정 목록
		try{
			String setScheduleName = scheduleName.getText();
			File f = new File(path+"/"+setScheduleName+".txt");
			if(f.exists()){
				BufferedReader in = new BufferedReader(new FileReader(path+"/"+setScheduleName+".txt"));
				String memoAreaText= new String();
				while(true){
					String tempStr = in.readLine();
					if(tempStr == null) break;
					memoAreaText = memoAreaText + tempStr + System.getProperty("line.separator");
				}
				memoArea.setText(memoAreaText);
				in.close();	
			}
			else memoArea.setText("");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	// 스케줄 버튼 동적 생성 메서드.
	private void makeScheduleIndex(String toDate) {
		indexSchedulePanel.removeAll();
		indexSchedulePanel.repaint();
		File[] fList;
		File dir = new File("MemoData/"+toDate+"/");
		
		if(dir.exists()) {
			fList = dir.listFiles();
			
			Arrays.sort(fList, new Comparator<Object>() {
				@Override
				public int compare(Object obj1, Object obj2) {
					String s1 = ((File)obj1).lastModified()+"";
					String s2 = ((File)obj2).lastModified()+"";
					return s1.compareTo(s2);
				}
			});
			try {
				for(int i=0; i< fList.length; i++) {
					String fName = fList[i].getName().substring(0, fList[i].getName().length()-4);
					
					JButton temp = new JButton(fName);
					temp.setHorizontalAlignment(SwingConstants.LEFT);
					temp.setBorderPainted(false);
					temp.setContentAreaFilled(false);
					temp.setOpaque(true);
					
					Dimension buttonSize=temp.getPreferredSize();
					buttonSize.height = 20;
					buttonSize.width = 350;
					temp.setPreferredSize(buttonSize);
					temp.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							makeScheduleDialog(fName);
						}
					});
					
					indexSchedulePanel.add(temp);
					
				}
			}catch(Exception e) {
			}
		}
		else {
			indexSchedulePanel.add(new JLabel("스케줄이 없습니다."));
		}
		
	}
	
	// 달력을 출력하는 메서드.
	private void showCal(){
		//SearchFile();
		for(int i=0;i<CAL_HEIGHT;i++){
			for(int j=0;j<CAL_WIDTH;j++){
				String fontColor="black";
				if(j==0) fontColor="red";
				else if(j==6) fontColor="blue";
				
				JLabel setDate;		
				JLabel sch_inf;
				JLabel holiday = new JLabel(lunarDateAndHoliday.checkSolar(calMonth+1, calDates[i][j]));
				JLabel Lunarholiday = new JLabel(lunarDateAndHoliday.checkLunar(calYear, calMonth, calDates[i][j]));
				JLabel todayMark = new JLabel("<html><font color=green>Today</html>");
				
				if(lunarDateAndHoliday.red) fontColor="red";
				
				File f = new File("MemoData/"+calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDates[i][j]<10?"0":"")+calDates[i][j]+"/");
				if(f.isDirectory() && f.listFiles().length > 0){
					// 해당 날짜에 저장된 일정이 존재할 때.
					String x = f.listFiles()[0].toString();
					String y = x.substring(18,x.length()-4);
					sch_inf = new JLabel(y);
					setDate = new JLabel("<html><p><b><font color="+fontColor+">"+calDates[i][j]+"</font></b></p></html>");
				}
				else {
					sch_inf = new JLabel();
					setDate = new JLabel("<html><p><font color="+fontColor+">"+calDates[i][j]+"</font></p></html>");
				}
				
				dateButs[i][j].removeAll();
				dateButs[i][j].setLayout(new GridLayout(4,1));
				dateButs[i][j].add(holiday);
				dateButs[i][j].add(Lunarholiday);
				dateButs[i][j].add(setDate);
				if(calMonth == today.get(Calendar.MONTH) &&
						calYear == today.get(Calendar.YEAR) &&
						calDates[i][j] == today.get(Calendar.DAY_OF_MONTH)){
					dateButs[i][j].add(todayMark);
					dateButs[i][j].setToolTipText("Today");
				}
				else {
					dateButs[i][j].add(sch_inf);
				}
				
				if(calDates[i][j] == 0) dateButs[i][j].setVisible(false);
				else dateButs[i][j].setVisible(true);
				

				final Dimension size = sch_inf.getPreferredSize();
				sch_inf.setMinimumSize(size);
				sch_inf.setPreferredSize(size);
			}
		}
	}
	
	// 달력 연월 이동할 때 동작하는 Action
	private class ListenForCalOpButtons implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == todayBut){
				setToday();
				lForDateButs.actionPerformed(e);
				focusToday();
			}
			else if(e.getSource() == lYearBut) moveMonth(-12);
			else if(e.getSource() == lMonBut) moveMonth(-1);
			else if(e.getSource() == nMonBut) moveMonth(1);
			else if(e.getSource() == nYearBut) moveMonth(12);
			
			curMMYYYYLab.setText("<html><table width=100><tr><th><font size=5>"+((calMonth+1)<10?"&nbsp;":"")+(calMonth+1)+" / "+calYear+"</th></tr></table></html>");
			showCal();
		}
	}
	
	// 달력에 있는 날짜 선택할 때 동작하는 Action 
	private class listenForDateButs implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			int k=-1,l=-1;
			String solar = "";
			String lunar = "";
			for(int i=0 ; i<CAL_HEIGHT ; i++){
				for(int j=0 ; j<CAL_WIDTH ; j++){
					if(e.getSource() == dateButs[i][j]){ 
						k=i;
						l=j;
						solar = ((JLabel)(dateButs[i][j]).getComponent(0)).getText();
						lunar = ((JLabel)(dateButs[i][j]).getComponent(1)).getText();
						if(solar == null) solar = "";
						if(lunar == null) lunar = "";
					}
				}
			}
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			toolkit.beep();
			
			if(!(k ==-1 && l == -1)) {
				calDayOfMon = calDates[k][l]; 
			}
			//today버튼을 눌렀을때도 이 actionPerformed함수가 실행되기 때문에 넣은 부분

			cal = new GregorianCalendar(calYear,calMonth,calDayOfMon);
			
			String dDayString = new String();
			int dDay=((int)((cal.getTimeInMillis() - today.getTimeInMillis())/1000/60/60/24));
			if(dDay == 0 && (cal.get(Calendar.YEAR) == today.get(Calendar.YEAR)) 
					&& (cal.get(Calendar.MONTH) == today.get(Calendar.MONTH))
					&& (cal.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH))) dDayString = "Today"; 
			else if(dDay >=0) dDayString = "D-"+(dDay+1);
			else if(dDay < 0) dDayString = "D+"+(dDay)*(-1);
			
			
			selectedDate.setText("<Html><font size=3>"+calYear+"-"+(calMonth+1)+"-"+calDayOfMon+"&nbsp;("+dDayString+") "+solar+" "+lunar+"</html>");
			makeScheduleIndex(calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDayOfMon<10?"0":"")+calDayOfMon);

		}
	}
	public void Play(String fileName)
    {
        try
        {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(fileName));
            Clip clip = AudioSystem.getClip();
            clip.stop();
            clip.open(ais);
            clip.start();
        }
        catch (Exception ex)
        {
        }
    }
	
	private class ThreadConrol extends Thread{
		public void run(){
			boolean msgCntFlag = false;
			int num = 0;
			String curStr = new String();
			while(true){
				try{
					today = Calendar.getInstance();
					String amPm = (today.get(Calendar.AM_PM)==0?"AM":"PM");
					String hour;
							if(today.get(Calendar.HOUR) == 0) hour = "12"; 
							else if(today.get(Calendar.HOUR) == 12) hour = " 0";
							else hour = (today.get(Calendar.HOUR)<10?" ":"")+today.get(Calendar.HOUR);
					String min = (today.get(Calendar.MINUTE)<10?"0":"")+today.get(Calendar.MINUTE);
					String sec = (today.get(Calendar.SECOND)<10?"0":"")+today.get(Calendar.SECOND);
					infoClock.setText(amPm+" "+hour+":"+min+":"+sec);

					sleep(1000);
					String infoStr = bottomInfo.getText();
					
					if(infoStr != " " && (msgCntFlag == false || curStr != infoStr)){
						num = 5;
						msgCntFlag = true;
						curStr = infoStr;
					}
					else if(infoStr != " " && msgCntFlag == true){
						if(num > 0) num--;
						else{
							msgCntFlag = false;
							bottomInfo.setText(" ");
						}
					}
					
					for(int i=0; i<alarmTime.size(); i++) {
						String time = alarmTime.get(i);
						
						if(time.equals(calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDayOfMon<10?"0":"")+calDayOfMon
							+(today.get(Calendar.HOUR)<10?"0":"")+today.get(Calendar.HOUR)+min+sec)) {
							String alarmString = alarmIndex.get(i);
							TrayIconHandler.removeMenuItem("알람 목록", alarmString);
							deleteAlarm(alarmString);
							System.out.println("알람");
							
							Play("src/calendarPack/sound.wav");
							TrayIconHandler.displayMessage("설정하신 알람이 실행되었습니다.", alarmString, MessageType.INFO);
						}
					}
				}
				catch(InterruptedException e){
					System.out.println("Thread:Error");
				}
			}
		}
	}
}