
package gap.com.driver.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SalaryAttributeResponseBean implements Parcelable
{

    @SerializedName("SUCCESS")
    @Expose
    private String sUCCESS;
    @SerializedName("salaryAttribute")
    @Expose
    private SalaryAttribute salaryAttribute;
    @SerializedName("processStatus")
    @Expose
    private int processStatus;
    @SerializedName("salaryAttributeId")
    @Expose
    private int salaryAttributeId;


    public String getsUCCESS() {
        return sUCCESS;
    }

    public void setsUCCESS(String sUCCESS) {
        this.sUCCESS = sUCCESS;
    }

    public SalaryAttribute getSalaryAttribute() {
        return salaryAttribute;
    }

    public void setSalaryAttribute(SalaryAttribute salaryAttribute) {
        this.salaryAttribute = salaryAttribute;
    }

    public int getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(int processStatus) {
        this.processStatus = processStatus;
    }

    public int getSalaryAttributeId() {
        return salaryAttributeId;
    }

    public void setSalaryAttributeId(int salaryAttributeId) {
        this.salaryAttributeId = salaryAttributeId;
    }

    public static Creator<SalaryAttributeResponseBean> getCREATOR() {
        return CREATOR;
    }

    protected SalaryAttributeResponseBean(Parcel in) {
        sUCCESS = in.readString();
        salaryAttribute = in.readParcelable(SalaryAttribute.class.getClassLoader());
        processStatus = in.readInt();
        salaryAttributeId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sUCCESS);
        dest.writeParcelable(salaryAttribute, flags);
        dest.writeInt(processStatus);
        dest.writeInt(salaryAttributeId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SalaryAttributeResponseBean> CREATOR = new Creator<SalaryAttributeResponseBean>() {
        @Override
        public SalaryAttributeResponseBean createFromParcel(Parcel in) {
            return new SalaryAttributeResponseBean(in);
        }

        @Override
        public SalaryAttributeResponseBean[] newArray(int size) {
            return new SalaryAttributeResponseBean[size];
        }
    };
}
