
package gap.com.driver.model.driverprofile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import gap.com.driver.database.entity.PersonTimeOff;

public class RESULTBean {

    @SerializedName("driverProfile")
    @Expose
    private DriverProfile driverProfile;

    @SerializedName("personTimeOffList")
    @Expose
    public List<PersonTimeOffList> personTimeOffList;

    @SerializedName("driverSACommentList")
    @Expose
    public List<DriverSACommentList> driverSACommentList;

    @SerializedName("complaintList")
    @Expose
    public List<ComplaintList> complaintList;

    @SerializedName("personTimeOffVO")
    @Expose
    public PersonTimeOff personTimeOffVO;
    @SerializedName("jsonArrayAttach")
    @Expose
    public List<JsonArrayAttach> jsonArrayAttach;



    public DriverProfile getDriverProfile() {
        return driverProfile;
    }

    public void setDriverProfile(DriverProfile driverProfile) {
        this.driverProfile = driverProfile;
    }

    public List<PersonTimeOffList> getPersonTimeOffList() {
        return personTimeOffList;
    }

    public void setPersonTimeOffList(List<PersonTimeOffList> personTimeOffList) {
        this.personTimeOffList = personTimeOffList;
    }

    public List<ComplaintList> getComplaintList() {
        return complaintList;
    }

    public void setComplaintList(List<ComplaintList> complaintList) {
        this.complaintList = complaintList;
    }

    public List<JsonArrayAttach> getJsonArrayAttach() {
        return jsonArrayAttach;
    }

    public void setJsonArrayAttach(List<JsonArrayAttach> jsonArrayAttach) {
        this.jsonArrayAttach = jsonArrayAttach;
    }
}
