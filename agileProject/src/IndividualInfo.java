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
    private int age;
    private String lastName;
    private IndividualInfo father;
    private IndividualInfo mother;

    private List<String> childFamilyList = new ArrayList<String>();
    private List<String> spouseFamilyList = new ArrayList<>();
	public boolean dead;


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

    public int getAge(){
    	return age;
    }

    public void setAge(int age){
    	this.age=age;
    }
    
    public IndividualInfo getFather(){
    	return this.father;
    }

    public void setFather(IndividualInfo father){
    	this.father=father;
    }
    
    public IndividualInfo getMother(){
    	return this.mother;
    }

    public void setMother(IndividualInfo mother){
    	this.mother=mother;
    }
    
    public String getLastName(){
    	return lastName;
    }

    public void setLastName(String lastName){
    	this.lastName=lastName;
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
    
    public boolean isDead() {
    	
        return this.getDeath().compareTo(new Date())<0;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public Date getDeath() {
        return death;
    }

    public void setDeath(Date death) {
        this.death = death;
    }


	

}
