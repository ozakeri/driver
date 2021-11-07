package gap.com.driver.model.driverprofile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ComplaintList {

    @SerializedName("requestDescription")
    @Expose
    public String requestDescription;
    @SerializedName("requestDate")
    @Expose
    public String requestDate;
    @SerializedName("car2")
    @Expose
    public Car2 car2;

    public String getRequestDescription() {
        return requestDescription;
    }

    public void setRequestDescription(String requestDescription) {
        this.requestDescription = requestDescription;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public Car2 getCar2() {
        return car2;
    }

    public void setCar2(Car2 car2) {
        this.car2 = car2;
    }
}
