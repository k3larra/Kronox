package publicScreen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

public class PublicScreenGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel mainPanel;
	private JPanel LIBpanel;
	private JPanel fadePanel;
	private JLabel lblClocklabel;
	private JLabel lblDatelabel;
	private JScrollPane scroller;
    
	private KronoxParser parser;
	
	private Clock clock;
	
	private int libSpacing = 60;
	private int libHeight = 65;
	private int libWidth = 1080;
	private int scrollWindowHeight=1550;
	
	//private int topOffset = 242;
	private int topOffset = 182;
	private int bottomOffset = 135;
	
	private int YScroll = 0;
	private LIBscrolling libScrolling;
		
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PublicScreenGUI frame = new PublicScreenGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public PublicScreenGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1080, 1920);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setUndecorated(true);
		
		mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, 1080, 242);
		contentPane.add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(null);
		
		//fadePanel = new JPanel();
		//fadePanel.setBounds(0, libSpacing, libWidth, 1920-bottomOffset-libHeight);
		//fadePanel.setBackground(new Color(255, 255, 255, 0));
		//contentPane.add(fadePanel, BorderLayout.CENTER);
		//fadePanel.setLayout(null);
		
		drawTopBar();
		drawBottomBar();
		
		LIBpanel = new JPanel();
		LIBpanel.setBounds(0, 0, libWidth, libHeight*50);///FIX
		LIBpanel.setBackground(Color.WHITE);
		scroller = new JScrollPane(LIBpanel);
		scroller.setEnabled(true);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER); 
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.setBounds(0, topOffset+libHeight-5, libWidth, scrollWindowHeight);  //1860 libSpacing
		contentPane.add(scroller, BorderLayout.CENTER);
		LIBpanel.setLayout(new GridLayout(0, 1));
        
		
		 parser = new KronoxParser(this);
		 //libScrolling = new LIBscrolling();
		// libScrolling.start();
		 //libScrolling.stopScrolling();
		 clock = new Clock(this);
	}
	
	//Main program loop
	//
	public class LIBscrolling extends Thread {
		private boolean scroll=false;
		public void startScrolling(){
			scroll=true;
			YScroll = libSpacing;
		}
		public void stopScrolling(){
			scroll=false;
			YScroll = libSpacing;
		}
		@Override
		public void run() {
			//resyncSchedule();
			while(true){
				if (scroll){
					/*if (YScroll!=0){ //Not Firsttime
						LIBpanel.scrollRectToVisible(new Rectangle(0, 0, 1080, 1700));
						
					}*/
					YScroll = 0;
					LIBpanel.scrollRectToVisible(new Rectangle(0, -1080, 1080, scrollWindowHeight));
					//int max = (((Constants.bookingsList.size()*libHeight))-(1920-topOffset-bottomOffset));
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {}
					LIBpanel.scrollRectToVisible(new Rectangle(0, 24*libHeight, 1080, scrollWindowHeight));
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {}
					LIBpanel.scrollRectToVisible(new Rectangle(0,  48*libHeight, 1080, scrollWindowHeight));
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {}
				}else{
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {}
				}
			}
		}
	}
	
	public void drawTopBar() {
		JLabel picWelcomeToNiagara = new JLabel("");
		picWelcomeToNiagara.setIcon(new ImageIcon(PublicScreenGUI.class.getResource("/assets/Welcome_to_niagara.png")));
		picWelcomeToNiagara.setBounds(22, 24, 467, 160);
		mainPanel.add(picWelcomeToNiagara);
		
		JLabel picMalmoLogo = new JLabel("");
		picMalmoLogo.setIcon(new ImageIcon(PublicScreenGUI.class.getResource("/assets/Malmo_Hogskola_Logo.png")));
		picMalmoLogo.setBounds(905, 25, 155, 139);
		mainPanel.add(picMalmoLogo);
		
		JPanel panelBlackBox = new JPanel();
		panelBlackBox.setBackground(Color.BLACK);
		panelBlackBox.setBounds(0, 184, 1080, 60);
		mainPanel.add(panelBlackBox);
		panelBlackBox.setLayout(null);
		
		JLabel timeLabel = new JLabel("Time");
		timeLabel.setFont(new Font("Futura", Font.PLAIN, 30));
		timeLabel.setForeground(Color.WHITE);
		timeLabel.setBounds(26, 13, 94, 31);
		panelBlackBox.add(timeLabel);
		
		JLabel courseLabel = new JLabel("Course");
		courseLabel.setForeground(Color.WHITE);
		courseLabel.setFont(new Font("Futura", Font.PLAIN, 30));
		courseLabel.setBounds(158, 13, 112, 31);
		panelBlackBox.add(courseLabel);
		
		JLabel roomLabel = new JLabel("Room");
		roomLabel.setForeground(Color.WHITE);
		roomLabel.setFont(new Font("Futura", Font.PLAIN, 30));
		roomLabel.setBounds(930, 13, 112, 31);
		panelBlackBox.add(roomLabel);
		
		lblClocklabel = new JLabel("00:00");
		lblClocklabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblClocklabel.setBounds(652, 41, 228, 80);
		lblClocklabel.setFont(new Font("Futura", Font.PLAIN, 79));
		mainPanel.add(lblClocklabel);
		
		lblDatelabel = new JLabel("day, month, year");
		lblDatelabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblDatelabel.setBounds(637, 104, 256, 80);
		lblDatelabel.setFont(new Font("Futura", Font.PLAIN, 30));
		mainPanel.add(lblDatelabel);
	}
	
	public void drawBottomBar() {
		JPanel blackBarLower = new JPanel();
		blackBarLower.setBackground(Color.BLACK);
		blackBarLower.setBounds(0, 1785, 1080, 135);
		contentPane.add(blackBarLower);
		blackBarLower.setLayout(null);
		
		JLabel lblKronoxLogo = new JLabel("");
		lblKronoxLogo.setIcon(new ImageIcon(PublicScreenGUI.class.getResource("/assets/KronoxLogoSmall.png")));
		lblKronoxLogo.setBounds(42, 17, 211, 100);
		blackBarLower.add(lblKronoxLogo);
		
		JLabel lblGroupAvailable = new JLabel("Group study rooms available");
		lblGroupAvailable.setFont(new Font("Futura", Font.PLAIN, 25));
		lblGroupAvailable.setForeground(Color.BLACK);
		lblGroupAvailable.setBounds(708, 6, 366, 33);
		blackBarLower.add(lblGroupAvailable);
		
		JLabel lblGroupNiagara = new JLabel("Niagara:");
		lblGroupNiagara.setForeground(Color.BLACK);
		lblGroupNiagara.setFont(new Font("Futura", Font.PLAIN, 25));
		lblGroupNiagara.setBounds(708, 51, 115, 33);
		blackBarLower.add(lblGroupNiagara);
		
		JLabel lblGroupOrkanen = new JLabel("Orkanen:");
		lblGroupOrkanen.setForeground(Color.BLACK);
		lblGroupOrkanen.setFont(new Font("Futura", Font.PLAIN, 25));
		lblGroupOrkanen.setBounds(708, 84, 115, 33);
		blackBarLower.add(lblGroupOrkanen);
	}
	
	public void setTimeOnLabel(String currentTime, String currentDate) {
		lblClocklabel.setText(currentTime);
		lblDatelabel.setText(currentDate);
	}
	
	
	public void drawSchedule() {
		int lastNumber = 0; 
		int nbrExtraRows = 0;		
		for(int i = 0; i < Constants.bookingsList.size(); i++) {
			int j = (Constants.bookingsList.get(i).getStartTime().get(Calendar.DAY_OF_YEAR)-Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
			
			if (j!=lastNumber){
				lastNumber = j;
				//Add a special row:
				JPanel extraRow = new JPanel();
				extraRow.setLayout(new FlowLayout(FlowLayout.LEFT,15,15));
				extraRow.setBackground(Color.GRAY);
				extraRow.setBounds(0, 0, libWidth, libHeight);
				extraRow.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));				
				JLabel lblNewDay = new JLabel("Tomorrow");
				lblNewDay.setHorizontalAlignment(JLabel.LEFT);
				Calendar c = Calendar.getInstance();
		
				nbrExtraRows = nbrExtraRows + 1;
				if (nbrExtraRows>1||(Constants.bookingsList.get(i).getStartTime().get(Calendar.DAY_OF_YEAR)-c.get(Calendar.DAY_OF_YEAR))>1){
					
					String day;
					switch (Constants.bookingsList.get(i).getStartTime().get(Calendar.DAY_OF_WEEK)) {
					case 1:
						day="Sunday";
						break;
					case 2:
						day="Monday";
						break;
					case 3:
						day="Tuesday";
						break;
					case 4:
						day="Wednesday";
						break;
					case 5:
						day="Thursday";
						break;
					case 6:
						day="Friday";
						break;
					case 7:
						day="Saturday";
						break;
					default:
						day="NoDay";
						break;
					}
					//System.out.println("DAY: "+day);
					lblNewDay.setText(day);
				}
				lblNewDay.setFont(new Font("Futura", Font.BOLD, 30));
				lblNewDay.setBounds(24, 0, 500, libHeight);
				extraRow.add(lblNewDay);
				LIBpanel.add(extraRow);
				
			}
			
			//ArrayList<String> courseNames = Constants.bookingsList.get(i).getCourses();
				//System.out.println("NAMN: "+Constants.utb_kursinstans_grupper.get(courseNames.get(0)));
				String startTime= fix(Constants.bookingsList.get(i).getStartTime().get(Calendar.HOUR_OF_DAY))+":"+fix(Constants.bookingsList.get(i).getStartTime().get(Calendar.MINUTE));
	    		String endTime= fix(Constants.bookingsList.get(i).getEndTime().get(Calendar.HOUR_OF_DAY))+":"+fix(Constants.bookingsList.get(i).getEndTime().get(Calendar.MINUTE));
	    		
	    		Calendar now = Calendar.getInstance();
	    		Calendar minutesBefore = Calendar.getInstance();
	    		minutesBefore.setTime(Constants.bookingsList.get(i).getStartTime().getTime());
	    		minutesBefore.add(Calendar.MINUTE,-Constants.minutesBefore);
	    		Calendar minutesLate = Calendar.getInstance();
	    		minutesLate.setTime(Constants.bookingsList.get(i).getStartTime().getTime());
	    		minutesLate.add(Calendar.MINUTE, Constants.minutesLate);
				JPanel LIB = new JPanel();
				LIB.setLayout(new FlowLayout(FlowLayout.LEFT,15,15));	
				LIB.setPreferredSize(new Dimension(libWidth,libHeight));
	    		LIB.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
	        	if ((i & 1) == 0) 
	        	{
	        		if ( now.after(minutesBefore)&&now.before(Constants.bookingsList.get(i).getStartTime())){ //Startar snart
	        			LIB.setBackground(new Color(230,245,172,160));
	        		}else if(now.after(Constants.bookingsList.get(i).getStartTime())&&now.before(minutesLate)){
	        			LIB.setBackground(new Color(255,255,224,160)); //Yellow
	        		}else{
	        			LIB.setBackground(Color.WHITE);
	        		}
	        	}else{
	        		if ( now.after(minutesBefore)&&now.before(Constants.bookingsList.get(i).getStartTime())){ //Startar snart
	        			LIB.setBackground(new Color(186,206,106,160)); //Green
	        			
	        		}else if(now.after(Constants.bookingsList.get(i).getStartTime())&&now.before(minutesLate)){
	        			LIB.setBackground(new Color(238,232,170,160));//Yellow
	        		}else{
	        			LIB.setBackground(new Color(240,240,240,255));
	        		}
	        	}
	        	
	    		
	    		JLabel lblTime;
	    		if (now.before(Constants.bookingsList.get(i).getStartTime())){
	    			lblTime = new JLabel(startTime);
	    		}else{
	    			lblTime = new JLabel("-"+endTime);
	    		}
	    		lblTime.setFont(new Font("Futura", Font.BOLD, 30));
	    		lblTime.setPreferredSize(new Dimension(120,libHeight));
	    		lblTime.setVerticalAlignment(JLabel.TOP);
	    		lblTime.setHorizontalAlignment(JLabel.LEFT);
	    		LIB.add(lblTime);
	    		
	    		//Course
	    		
	    		String firstCourseName = "";
	    		if (Constants.bookingsList.size()>0){
	    			try {
						firstCourseName = Constants.bookingsList.get(i).getCourses().get(0);
					} catch (Exception e) {
						e.printStackTrace();
					}
	    		}
	    		JLabel lblCourse = new JLabel(Constants.utb_kursinstans_grupper.get(firstCourseName));
	    		lblCourse.setFont(new Font("Futura", Font.PLAIN, 30));
	    		lblCourse.setPreferredSize(new Dimension(715, libHeight));
	    		lblCourse.setVerticalAlignment(JLabel.TOP);
	    		lblCourse.setHorizontalAlignment(JLabel.LEFT);
	    		LIB.add(lblCourse);
	    		
	    		//Room
	    		ArrayList<String> rooms = Constants.bookingsList.get(i).getRooms();
	    		String firstRoomName = "";
	    		if (rooms.size()>0){
	    			firstRoomName = rooms.get(0);
	    		}
	    		JLabel lblRoom = new JLabel(firstRoomName);
	    		lblRoom.setFont(new Font("Futura", Font.BOLD, 30));
	    		lblRoom.setVerticalAlignment(JLabel.TOP);
	    		lblRoom.setHorizontalAlignment(JLabel.RIGHT);
	    		lblRoom.setPreferredSize(new Dimension(170, libHeight));
	    		LIB.add(lblRoom);
	    		LIBpanel.add(LIB);
	    		if ((i+nbrExtraRows)>24){
	    			//libScrolling.startScrolling();
	    		}
			}
	}
	/*
	public void initFonts() {
		try {
			Neou_Bold = Font.createFont(Font.TRUETYPE_FONT, new File("/assets/Neou-Bold.ttf")).deriveFont(79f);
		     GraphicsEnvironment ge = 
		         GraphicsEnvironment.getLocalGraphicsEnvironment();
		     ge.registerFont(Neou_Bold.createFont(Font.TRUETYPE_FONT, new File("/assets/Neou-Bold.ttf")));
		} catch (IOException|FontFormatException e) {
		     
		}
	}
	*/

	public void updateInfo() {
		LIBpanel.removeAll();	
		drawSchedule();
		contentPane.validate();
		contentPane.repaint();
		//LIBpanel.validate();
		//libScrolling.stopScrolling();
		//libScrolling.startScrolling();
	}
	
	public String fix(int i){
		if (i<10){
			return "0"+i;
		}else{
			return ""+i;
		}
	} 
}