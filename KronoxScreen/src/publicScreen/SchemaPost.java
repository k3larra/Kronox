package publicScreen;

import java.util.ArrayList;
import java.util.Calendar;

public class SchemaPost implements Comparable<SchemaPost>{
	private String room;
	private String course;
	private String time;
	private Calendar startTime;
	private Calendar endTime;
	private ArrayList<String> teachers = new ArrayList<String>();
	private ArrayList<String> rooms = new ArrayList<String>();
	private ArrayList<String> courses = new ArrayList<String>();
  
	public SchemaPost(String room, String course, String time) {
		this.room = room;
		this.course = course;
		this.time = time;
	}
	
	public SchemaPost() {
	}

	public String getRoom() {
		return room;
	}
	
	public String getCourse() {
		return course;
	}
	
	public void setCourse(String courseName) {
		this.course = courseName;
	}
	
	public String getTime() {
		return time;
	}

	public Calendar getStartTime() {
		return startTime;
	}

	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}

	public Calendar getEndTime() {
		return endTime;
	}

	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}

	public ArrayList<String> getTeachers() {
		return teachers;
	}
	public String getTeachersAsString() {
		String s="";
		for (String teacher : teachers) {
			s= teacher+","+s;
		}
		if (s.length()>0){
			s= s.substring(0, s.length()-1);
		}
		return s;
	}

	public void setTeachers(ArrayList<String> teachers) {
		this.teachers = teachers;
	}

	public void addTeacher(String teacher) {
		this.teachers.add(teacher);
	}
	
	public ArrayList<String> getRooms() {
		return rooms;
	}
	
	public String getRoomsAsString() {
		String s="";
		for (String room : rooms) {
			s= room+","+s;
		}
		if (s.length()>0){
			s= s.substring(0, s.length()-1);
		}
		return s;
	}


	public void setRooms(ArrayList<String> rooms) {
		this.rooms = rooms;
	}
	public void addRoom(String room) {
		this.rooms.add(room);
	}

	public ArrayList<String> getCourses() {
		return courses;
	}
	public String getCoursesAsString() {
		String s="";
		for (String course : courses) {
			s= course +","+s;
		}
		if (s.length()>0){
			s= s.substring(0, s.length()-1);
		}
		return s;
	}

	public void setCourses(ArrayList<String> courses) {
		this.courses = courses;
	}
	public void addCourse(String course) {
		this.courses.add(course);
	}

	@Override
	public int compareTo(SchemaPost o) {
		int result=0;
		try{
			result = this.getStartTime().compareTo(o.getStartTime());
			if (result==0){
				if ((this.getCourses().size()>0)&&(o.getCourses().size()>0)){
					result = Constants.utb_kursinstans_grupper.get(this.getCourses().get(0)).compareTo(Constants.utb_kursinstans_grupper.get(o.getCourses().get(0)));
				}
			}
		}catch(Exception e){}
		return result;
	}

	
	
}
