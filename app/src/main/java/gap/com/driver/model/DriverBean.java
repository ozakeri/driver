package gap.com.driver.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DriverBean implements Parcelable {

    private String driverId;
    private String alreadyRegistered;
    private String companyName;
    private String name;
    private String family;
    private String username;
    private String password;
    private int driverCode;
    public String shiftTypeBaseEnNameFv;
    public String dateCreation;
    public String trainingDescription;
    public String nameFv;
    public int processStatus;
    public String trainingDate;
    public String driverLicenceStatus_text;
    public String dateLChangeProcessStatus;
    public int trainingStatus;
    public int driverEcardStatus;
    public String trainingStatus_text;
    public int watchingIsLChProcessStatus;
    public String shiftGroupGEn_text;
    public String driverEcardStatus_text;
    public int id;
    public int shiftTypeBaseEn;
    public int requestedDriveLicenceEn;
    public String shiftTypeBaseEn_text;
    public String autoCompleteLabel;
    public String watchingIsLChProcessStatus_text;
    public String employmentDate;
    public int driverLicenceStatus;
    public String dateWatchingLChProcessStatus;
    public boolean showTimeRemainingUpForExpire;
    public String requestedDriveLicenceEn_text;
    public String processStatus_text;
    public int shiftGroupGEn;
    public String nameFv1;
    public int backIsLChProcessStatus;
    public String backIsLChProcessStatus_text;
    public String status_text;
    public int status;

    protected DriverBean(Parcel in) {
        driverId = in.readString();
        alreadyRegistered = in.readString();
        companyName = in.readString();
        name = in.readString();
        family = in.readString();
        username = in.readString();
        password = in.readString();
        driverCode = in.readInt();
        shiftTypeBaseEnNameFv = in.readString();
        dateCreation = in.readString();
        trainingDescription = in.readString();
        nameFv = in.readString();
        processStatus = in.readInt();
        trainingDate = in.readString();
        driverLicenceStatus_text = in.readString();
        dateLChangeProcessStatus = in.readString();
        trainingStatus = in.readInt();
        driverEcardStatus = in.readInt();
        trainingStatus_text = in.readString();
        watchingIsLChProcessStatus = in.readInt();
        shiftGroupGEn_text = in.readString();
        driverEcardStatus_text = in.readString();
        id = in.readInt();
        shiftTypeBaseEn = in.readInt();
        requestedDriveLicenceEn = in.readInt();
        shiftTypeBaseEn_text = in.readString();
        autoCompleteLabel = in.readString();
        watchingIsLChProcessStatus_text = in.readString();
        employmentDate = in.readString();
        driverLicenceStatus = in.readInt();
        dateWatchingLChProcessStatus = in.readString();
        showTimeRemainingUpForExpire = in.readByte() != 0;
        requestedDriveLicenceEn_text = in.readString();
        processStatus_text = in.readString();
        shiftGroupGEn = in.readInt();
        nameFv1 = in.readString();
        backIsLChProcessStatus = in.readInt();
        backIsLChProcessStatus_text = in.readString();
        status_text = in.readString();
        status = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(driverId);
        dest.writeString(alreadyRegistered);
        dest.writeString(companyName);
        dest.writeString(name);
        dest.writeString(family);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeInt(driverCode);
        dest.writeString(shiftTypeBaseEnNameFv);
        dest.writeString(dateCreation);
        dest.writeString(trainingDescription);
        dest.writeString(nameFv);
        dest.writeInt(processStatus);
        dest.writeString(trainingDate);
        dest.writeString(driverLicenceStatus_text);
        dest.writeString(dateLChangeProcessStatus);
        dest.writeInt(trainingStatus);
        dest.writeInt(driverEcardStatus);
        dest.writeString(trainingStatus_text);
        dest.writeInt(watchingIsLChProcessStatus);
        dest.writeString(shiftGroupGEn_text);
        dest.writeString(driverEcardStatus_text);
        dest.writeInt(id);
        dest.writeInt(shiftTypeBaseEn);
        dest.writeInt(requestedDriveLicenceEn);
        dest.writeString(shiftTypeBaseEn_text);
        dest.writeString(autoCompleteLabel);
        dest.writeString(watchingIsLChProcessStatus_text);
        dest.writeString(employmentDate);
        dest.writeInt(driverLicenceStatus);
        dest.writeString(dateWatchingLChProcessStatus);
        dest.writeByte((byte) (showTimeRemainingUpForExpire ? 1 : 0));
        dest.writeString(requestedDriveLicenceEn_text);
        dest.writeString(processStatus_text);
        dest.writeInt(shiftGroupGEn);
        dest.writeString(nameFv1);
        dest.writeInt(backIsLChProcessStatus);
        dest.writeString(backIsLChProcessStatus_text);
        dest.writeString(status_text);
        dest.writeInt(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DriverBean> CREATOR = new Creator<DriverBean>() {
        @Override
        public DriverBean createFromParcel(Parcel in) {
            return new DriverBean(in);
        }

        @Override
        public DriverBean[] newArray(int size) {
            return new DriverBean[size];
        }
    };

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getAlreadyRegistered() {
        return alreadyRegistered;
    }

    public void setAlreadyRegistered(String alreadyRegistered) {
        this.alreadyRegistered = alreadyRegistered;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDriverCode() {
        return driverCode;
    }

    public void setDriverCode(int driverCode) {
        this.driverCode = driverCode;
    }

    public String getShiftTypeBaseEnNameFv() {
        return shiftTypeBaseEnNameFv;
    }

    public void setShiftTypeBaseEnNameFv(String shiftTypeBaseEnNameFv) {
        this.shiftTypeBaseEnNameFv = shiftTypeBaseEnNameFv;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getTrainingDescription() {
        return trainingDescription;
    }

    public void setTrainingDescription(String trainingDescription) {
        this.trainingDescription = trainingDescription;
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

    public String getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(String trainingDate) {
        this.trainingDate = trainingDate;
    }

    public String getDriverLicenceStatus_text() {
        return driverLicenceStatus_text;
    }

    public void setDriverLicenceStatus_text(String driverLicenceStatus_text) {
        this.driverLicenceStatus_text = driverLicenceStatus_text;
    }

    public String getDateLChangeProcessStatus() {
        return dateLChangeProcessStatus;
    }

    public void setDateLChangeProcessStatus(String dateLChangeProcessStatus) {
        this.dateLChangeProcessStatus = dateLChangeProcessStatus;
    }

    public int getTrainingStatus() {
        return trainingStatus;
    }

    public void setTrainingStatus(int trainingStatus) {
        this.trainingStatus = trainingStatus;
    }

    public int getDriverEcardStatus() {
        return driverEcardStatus;
    }

    public void setDriverEcardStatus(int driverEcardStatus) {
        this.driverEcardStatus = driverEcardStatus;
    }

    public String getTrainingStatus_text() {
        return trainingStatus_text;
    }

    public void setTrainingStatus_text(String trainingStatus_text) {
        this.trainingStatus_text = trainingStatus_text;
    }

    public int getWatchingIsLChProcessStatus() {
        return watchingIsLChProcessStatus;
    }

    public void setWatchingIsLChProcessStatus(int watchingIsLChProcessStatus) {
        this.watchingIsLChProcessStatus = watchingIsLChProcessStatus;
    }

    public String getShiftGroupGEn_text() {
        return shiftGroupGEn_text;
    }

    public void setShiftGroupGEn_text(String shiftGroupGEn_text) {
        this.shiftGroupGEn_text = shiftGroupGEn_text;
    }

    public String getDriverEcardStatus_text() {
        return driverEcardStatus_text;
    }

    public void setDriverEcardStatus_text(String driverEcardStatus_text) {
        this.driverEcardStatus_text = driverEcardStatus_text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShiftTypeBaseEn() {
        return shiftTypeBaseEn;
    }

    public void setShiftTypeBaseEn(int shiftTypeBaseEn) {
        this.shiftTypeBaseEn = shiftTypeBaseEn;
    }

    public int getRequestedDriveLicenceEn() {
        return requestedDriveLicenceEn;
    }

    public void setRequestedDriveLicenceEn(int requestedDriveLicenceEn) {
        this.requestedDriveLicenceEn = requestedDriveLicenceEn;
    }

    public String getShiftTypeBaseEn_text() {
        return shiftTypeBaseEn_text;
    }

    public void setShiftTypeBaseEn_text(String shiftTypeBaseEn_text) {
        this.shiftTypeBaseEn_text = shiftTypeBaseEn_text;
    }

    public String getAutoCompleteLabel() {
        return autoCompleteLabel;
    }

    public void setAutoCompleteLabel(String autoCompleteLabel) {
        this.autoCompleteLabel = autoCompleteLabel;
    }

    public String getWatchingIsLChProcessStatus_text() {
        return watchingIsLChProcessStatus_text;
    }

    public void setWatchingIsLChProcessStatus_text(String watchingIsLChProcessStatus_text) {
        this.watchingIsLChProcessStatus_text = watchingIsLChProcessStatus_text;
    }

    public String getEmploymentDate() {
        return employmentDate;
    }

    public void setEmploymentDate(String employmentDate) {
        this.employmentDate = employmentDate;
    }

    public int getDriverLicenceStatus() {
        return driverLicenceStatus;
    }

    public void setDriverLicenceStatus(int driverLicenceStatus) {
        this.driverLicenceStatus = driverLicenceStatus;
    }

    public String getDateWatchingLChProcessStatus() {
        return dateWatchingLChProcessStatus;
    }

    public void setDateWatchingLChProcessStatus(String dateWatchingLChProcessStatus) {
        this.dateWatchingLChProcessStatus = dateWatchingLChProcessStatus;
    }

    public boolean isShowTimeRemainingUpForExpire() {
        return showTimeRemainingUpForExpire;
    }

    public void setShowTimeRemainingUpForExpire(boolean showTimeRemainingUpForExpire) {
        this.showTimeRemainingUpForExpire = showTimeRemainingUpForExpire;
    }

    public String getRequestedDriveLicenceEn_text() {
        return requestedDriveLicenceEn_text;
    }

    public void setRequestedDriveLicenceEn_text(String requestedDriveLicenceEn_text) {
        this.requestedDriveLicenceEn_text = requestedDriveLicenceEn_text;
    }

    public String getProcessStatus_text() {
        return processStatus_text;
    }

    public void setProcessStatus_text(String processStatus_text) {
        this.processStatus_text = processStatus_text;
    }

    public int getShiftGroupGEn() {
        return shiftGroupGEn;
    }

    public void setShiftGroupGEn(int shiftGroupGEn) {
        this.shiftGroupGEn = shiftGroupGEn;
    }

    public String getNameFv1() {
        return nameFv1;
    }

    public void setNameFv1(String nameFv1) {
        this.nameFv1 = nameFv1;
    }

    public int getBackIsLChProcessStatus() {
        return backIsLChProcessStatus;
    }

    public void setBackIsLChProcessStatus(int backIsLChProcessStatus) {
        this.backIsLChProcessStatus = backIsLChProcessStatus;
    }

    public String getBackIsLChProcessStatus_text() {
        return backIsLChProcessStatus_text;
    }

    public void setBackIsLChProcessStatus_text(String backIsLChProcessStatus_text) {
        this.backIsLChProcessStatus_text = backIsLChProcessStatus_text;
    }

    public String getStatus_text() {
        return status_text;
    }

    public void setStatus_text(String status_text) {
        this.status_text = status_text;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
