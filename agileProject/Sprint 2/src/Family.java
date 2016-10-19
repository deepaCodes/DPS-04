import java.util.ArrayList;
import java.util.List;


public class Family {
	private String fId;
    private List<IndividualInfo> child  = new ArrayList<IndividualInfo>();
    private List<IndividualInfo> spouse = new ArrayList<IndividualInfo>();
    public String getfId() {
        return fId;
    }
    public void setfId(String fId) {
        this.fId = fId;
    }
    public List<IndividualInfo> getChild() {
        return child;
    }
    public void addChild(IndividualInfo c) {
        child.add(c);
    }
    public List<IndividualInfo> getSpouse() {
        return spouse;
    }
    public void addSpouse(IndividualInfo s) {
        spouse.add(s);
    }
	
}
