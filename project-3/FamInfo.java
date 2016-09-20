import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FamInfo {
	private String fid;
	private String husbId;
	private String wifeId;
	private List<String> childId= new ArrayList<String>();
	private Date marr;
	private Date div;
	
	public String getFid() {
		return fid;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public String getHusbId() {
		return husbId;
	}
	public Date getMarr() {
		return marr;
	}
	public void setMarr(Date marr) {
		this.marr = marr;
	}
	public Date getDiv() {
		return div;
	}
	public void setDiv(Date div) {
		this.div = div;
	}
	public void setHusbId(String husbId) {
		this.husbId = husbId;
	}
	public String getWifeId() {
		return wifeId;
	}
	public void setWifeId(String wifeId) {
		this.wifeId = wifeId;
	}
	public List<String> getChildId() {
		return childId;
	}
	
	
	
	

}
