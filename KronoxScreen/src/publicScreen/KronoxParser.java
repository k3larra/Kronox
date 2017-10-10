package publicScreen;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

public class KronoxParser {
	private PublicScreenGUI gui;
	private Timer t;
	public KronoxParser(PublicScreenGUI gui) {
		this.gui = gui;
		t = new Timer();
		t.schedule(new ParseKronox(), 0, Constants.SECONDS_BETWEEN_KRONOXPARSING*1000);
	}
	public void parseFromKronox() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(Constants.kronoxStringNiagaraTodayAndTomorrowMedForklaringar);
			NodeList bookings = doc.getElementsByTagName("schemaPost");
			for (int i = 0; i < bookings.getLength(); i++) {
				SchemaPost thisBooking = new SchemaPost();
				Node p = bookings.item(i);
				if (p.getNodeType() == Node.ELEMENT_NODE) {
					Element doc1 = (Element) p;
					NodeList items2 = doc1.getElementsByTagName("bokadeDatum");
					for (int t = 0; t < items2.getLength(); t++) {
						Node n1 = items2.item(t);
						if (n1.getNodeType() == Node.ELEMENT_NODE) {							
							Element eElement = (Element) n1;
							//Parse Start and Enddates
							SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss"); // first example
							String startDateTime = eElement.getAttribute("startDatumTid");
							Date d1=format1.parse("1961-04-29 23:14:20");
							//format1.get
							try {
								d1 = format1.parse(startDateTime);
							} catch (Exception e) {}
							Calendar startTimeCal = Calendar.getInstance();
							startTimeCal.setTime(d1);
							String endDateTime = eElement.getAttribute("slutDatumTid");
							Date d2 = format1.parse( "1961-04-29 23:14:20");
							try {
								d2 = format1.parse( endDateTime );
							} catch (Exception e) {e.printStackTrace();}
							Calendar endTimeCal = Calendar.getInstance();
							endTimeCal.setTime(d2);
							thisBooking.setStartTime(startTimeCal);
							thisBooking.setEndTime(endTimeCal);
							
						}
					}
				}
				//Who and where
				if (p.getNodeType() == Node.ELEMENT_NODE) {
					Element booking = (Element) p;
					NodeList resource = booking.getElementsByTagName("resursNod");
					for (int j = 0; j < resource.getLength(); j++) {
						Node n = resource.item(j);
						if (n.getNodeType() == Node.ELEMENT_NODE) {
							Element classType = (Element) n;
							String id = classType.getAttribute("resursTypId");
							NodeList classResource = classType.getElementsByTagName("resursId");
							for (int k = 0; k < classResource.getLength(); k++) {
								Node specifiedClass = classResource.item(k);
								if (specifiedClass.getNodeType() == Node.ELEMENT_NODE) {
									Element name = (Element) specifiedClass;
									if (id.contains("UTB_KURSINSTANS_GRUPPER")) {
										thisBooking.addCourse(name.getTextContent());
									}
									if (id.contains("RESURSER_LOKALER")) {
										thisBooking.addRoom(name.getTextContent());
									}
									if (id.contains("RESURSER_SIGNATURER")) {
										thisBooking.addTeacher(name.getTextContent());
									}
								}
							}
						}
					}
				//Add if there are UTB_KURSINSTANS_GRUPPER else it is grupproom
					if (thisBooking.getCourses().size()>0){
						//Is it only bookings that are within limit 
						Calendar now  = Calendar.getInstance();
						Calendar addEvenIfLate  = Calendar.getInstance();
						addEvenIfLate.setTime(thisBooking.getStartTime().getTime());
						addEvenIfLate.add(Calendar.MINUTE, Constants.minutesLate);
						//c.add(Calendar.MINUTE,Constants.minutesBefore);
						if (thisBooking.getStartTime().after(now)){ //StartTimeless than Constants.minutesbefore
							if((thisBooking.getStartTime().get(Calendar.DAY_OF_YEAR)-Calendar.getInstance().get(Calendar.DAY_OF_YEAR)==1)){
								//Tomorrow and list not full
								if (Constants.bookingsList.size()<Constants.maxNumberPosts){ //Fill it up
									Constants.bookingsList.add(thisBooking);						
								}else if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)>18){
									Constants.bookingsList.add(thisBooking); //Lägg till alla i morgon om det är kväll
								}
							}else if ((thisBooking.getStartTime().get(Calendar.DAY_OF_YEAR)-Calendar.getInstance().get(Calendar.DAY_OF_YEAR)>1)){
								//After tomorrow
								if (Constants.bookingsList.size()<Constants.maxNumberPosts){ //Fill it up
									Constants.bookingsList.add(thisBooking);
								}
							}
							else{
								if (Constants.bookingsList.size()<Constants.maxNumberPosts){
									Constants.bookingsList.add(thisBooking);
								}
							}
						}else{
							if(thisBooking.getEndTime().after(now)){  //Has not finished yet
								if(now.before(addEvenIfLate)){  //And only Constants.minutesLate after start
									if (Constants.addOngoingCourses){
										Constants.bookingsList.add(thisBooking);
									}
								}
							}
						}
							
					}
				}
			}
			
			//ResurseMapping
			NodeList forklaringsraders = doc.getElementsByTagName("forklaringsrader");
			for (int i = 0; i < forklaringsraders.getLength(); i++) {
				Node p = forklaringsraders.item(i);
				if (p.getNodeType() == Node.ELEMENT_NODE) {
					Element doc1 = (Element) p;
					//System.out.println("typ: "+doc1.getAttribute("typ"));
					if (doc1.getAttribute("typ").equals("RESURSER_SIGNATURER")){
						NodeList rad = doc1.getElementsByTagName("rad");
						for(int j = 0; j<rad.getLength();j++){
							String firstName="";
							String lastName="";
							String id="";
							Node n1 = rad.item(j);
							Element e2 = (Element)n1;
							NodeList kolumn = e2.getElementsByTagName("kolumn");
							for(int k = 0; k<kolumn.getLength();k++){
								
								Element e3 = (Element)kolumn.item(k);
								if (e3.getAttribute("rubrik").equals("Id")){
									id = e3.getTextContent().toLowerCase();
								}
								if (e3.getAttribute("rubrik").equals("ForNamn")){
									firstName = e3.getTextContent();
								}
								if (e3.getAttribute("rubrik").equals("EfterNamn")){
									lastName = e3.getTextContent();
								}
							}
							Constants.resurser_signaturer.put(id, htmlDecode(firstName)+" "+htmlDecode(lastName));
						}
					}
					if (doc1.getAttribute("typ").equals("UTB_KURSINSTANS_GRUPPER")){
						NodeList rad = doc1.getElementsByTagName("rad");
						for(int j = 0; j<rad.getLength();j++){
							String kursNamn="";
							String id="";
							Node n1 = rad.item(j);
							Element e2 = (Element)n1;
							NodeList kolumn = e2.getElementsByTagName("kolumn");
							for(int k = 0; k<kolumn.getLength();k++){
								
								Element e3 = (Element)kolumn.item(k);
								if (e3.getAttribute("rubrik").equals("Id")){
									//System.out.println("Id= "+e3.getTextContent());
									id = e3.getTextContent();
								}
								if (e3.getAttribute("rubrik").equals("KursNamn_SV")){
									//System.out.println("ForNamn= "+e3.getTextContent());
									kursNamn = e3.getTextContent();
								}
							}
							Constants.utb_kursinstans_grupper.put(id, htmlDecode(kursNamn));
						}
					}
		       }
		  }
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(Constants.bookingsList);

	}
	
	private String htmlDecode(String text){
		text = text.replace("&#197;", "Å");
		text = text.replace("&#196;", "Å");
		text = text.replace("&#214;", "Ö");
		text = text.replace("&#229;", "å");
		text = text.replace("&#228;", "ä");
		text = text.replace("&#246;", "ö");
		text = text.replace("&#8211;","-"); 
		return text;
	}
	
	private class ParseKronox extends TimerTask{
		@Override
		public void run() {
			//System.out.println("PARSING");
			Constants.bookingsList.clear();
			parseFromKronox();
			gui.updateInfo();
		}		
	}

}


