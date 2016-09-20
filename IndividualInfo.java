import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class IndividualInfo {
	private String id;
	private String name;
	private String Gender;
	private Date birth;
	private boolean alive;
	private Date death;
	
	private List<String> childFamilyList = new ArrayList<String>();
	private List<String> spouseFamilyList = new ArrayList<>();
	
	
	public IndividualInfo() {
		alive=true;
	}
	
	
	

	public List<String> getChildFamilyList() {
		return childFamilyList;
	}

	public void setChildFamilyList(List<String> childFamilyList) {
		this.childFamilyList = childFamilyList;
	}

	public List<String> getSpouseFamilyList() {
		return spouseFamilyList;
	}

	public void setSpouseFamilyList(List<String> spouseFamilyList) {
		this.spouseFamilyList = spouseFamilyList;
	}

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public Date getDeath() {
		return death;
	}

	public void setDeath(Date death) {
		this.death = death;
	}
	
}
