
package gap.com.driver.model.driverprofile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import gap.com.driver.model.DriverBean;

public class PersonTimeOffList {

    @SerializedName("reason")
    @Expose
    public String reason;
    @SerializedName("nameFv")
    @Expose
    public String nameFv;
    @SerializedName("processStatus")
    @Expose
    public int processStatus;
    @SerializedName("requestTypeEn_text")
    @Expose
    public String requestTypeEn_text;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("dutyIs")
    @Expose
    public boolean dutyIs;
    @SerializedName("processStatus_text")
    @Expose
    public String processStatus_text;
    @SerializedName("absentIsFV")
    @Expose
    public String absentIsFV;
    @SerializedName("requestTypeEn")
    @Expose
    public int requestTypeEn;
    @SerializedName("groupIs")
    @Expose
    public boolean groupIs;
    @SerializedName("driver")
    @Expose
    public DriverBean driver;
    @SerializedName("absentIs")
    @Expose
    public boolean absentIs;
    @SerializedName("offOnDay")
    @Expose
    public boolean offOnDay;
    @SerializedName("person")
    @Expose
    public Person person;
    @SerializedName("personTimeOffTypeEn_text")
    @Expose
    public String personTimeOffTypeEn_text;
    @SerializedName("finishDate")
    @Expose
    public String finishDate;
    @SerializedName("company")
    @Expose
    public Company company;
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("status_text")
    @Expose
    public String status_text;
    @SerializedName("startDate")
    @Expose
    public String startDate;
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("personTimeOffTypeEn")
    @Expose
    public int personTimeOffTypeEn;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNameFv() {
        return nameFv;
    }

    public void setNameFv(String nameFv) {
        this.nameFv = nameFv;
    }

    public int getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(int processStatus) {
        this.processStatus = processStatus;
    }

    public String getRequestTypeEn_text() {
        return requestTypeEn_text;
    }

    public void setRequestTypeEn_text(String requestTypeEn_text) {
        this.requestTypeEn_text = requestTypeEn_text;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDutyIs() {
        return dutyIs;
    }

    public void setDutyIs(boolean dutyIs) {
        this.dutyIs = dutyIs;
    }

    public String getProcessStatus_text() {
        return processStatus_text;
    }

    public void setProcessStatus_text(String processStatus_text) {
        this.processStatus_text = processStatus_text;
    }

    public String getAbsentIsFV() {
        return absentIsFV;
    }

    public void setAbsentIsFV(String absentIsFV) {
        this.absentIsFV = absentIsFV;
    }

    public int getRequestTypeEn() {
        return requestTypeEn;
    }

    public void setRequestTypeEn(int requestTypeEn) {
        this.requestTypeEn = requestTypeEn;
    }

    public boolean isGroupIs() {
        return groupIs;
    }

    public void setGroupIs(boolean groupIs) {
        this.groupIs = groupIs;
    }

    public DriverBean getDriver() {
        return driver;
    }

    public void setDriver(DriverBean driver) {
        this.driver = driver;
    }

    public boolean isAbsentIs() {
        return absentIs;
    }

    public void setAbsentIs(boolean absentIs) {
        this.absentIs = absentIs;
    }

    public boolean isOffOnDay() {
        return offOnDay;
    }

    public void setOffOnDay(boolean offOnDay) {
        this.offOnDay = offOnDay;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getPersonTimeOffTypeEn_text() {
        return personTimeOffTypeEn_text;
    }

    public void setPersonTimeOffTypeEn_text(String personTimeOffTypeEn_text) {
        this.personTimeOffTypeEn_text = personTimeOffTypeEn_text;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus_text() {
        return status_text;
    }

    public void setStatus_text(String status_text) {
        this.status_text = status_text;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPersonTimeOffTypeEn() {
        return personTimeOffTypeEn;
    }

    public void setPersonTimeOffTypeEn(int personTimeOffTypeEn) {
        this.personTimeOffTypeEn = personTimeOffTypeEn;
    }
}
