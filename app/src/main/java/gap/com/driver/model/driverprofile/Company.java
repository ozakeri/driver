
package gap.com.driver.model.driverprofile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Company {

    @SerializedName("tariffBusIsLive")
    @Expose
    private Boolean tariffBusIsLive;
    @SerializedName("dateCreation")
    @Expose
    private String dateCreation;
    @SerializedName("companyTypeIsLocal")
    @Expose
    private Boolean companyTypeIsLocal;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("ownerCompanyIs_text")
    @Expose
    private String ownerCompanyIsText;
    @SerializedName("nameFv")
    @Expose
    private String nameFv;
    @SerializedName("processStatus")
    @Expose
    private Integer processStatus;
    @SerializedName("ownerCompanyIs")
    @Expose
    private Integer ownerCompanyIs;
    @SerializedName("userSuperiorNoMax")
    @Expose
    private Integer userSuperiorNoMax;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("companyIsOperationalBus")
    @Expose
    private Boolean companyIsOperationalBus;
    @SerializedName("sOSCompanyId")
    @Expose
    private Integer sOSCompanyId;
    @SerializedName("mainCompanyId")
    @Expose
    private Integer mainCompanyId;
    @SerializedName("userNoMax")
    @Expose
    private Integer userNoMax;
    @SerializedName("carNo")
    @Expose
    private Integer carNo;
    @SerializedName("codeInTree")
    @Expose
    private String codeInTree;
    @SerializedName("companyIsSOS")
    @Expose
    private Boolean companyIsSOS;
    @SerializedName("legalTypeEn_text")
    @Expose
    private String legalTypeEnText;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("legalTypeSysParamValue")
    @Expose
    private String legalTypeSysParamValue;
    @SerializedName("companyIsMain")
    @Expose
    private Boolean companyIsMain;
    @SerializedName("autoCompleteLabel")
    @Expose
    private String autoCompleteLabel;
    @SerializedName("companyIsInternal")
    @Expose
    private Boolean companyIsInternal;
    @SerializedName("companyType")
    @Expose
    private Integer companyType;
    @SerializedName("companyType_text")
    @Expose
    private String companyTypeText;
    @SerializedName("processStatus_text")
    @Expose
    private String processStatusText;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("legalTypeEn")
    @Expose
    private Integer legalTypeEn;
    @SerializedName("status_text")
    @Expose
    private String statusText;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("disableForPrivateCoAndCityRegion")
    @Expose
    public boolean disableForPrivateCoAndCityRegion;
    @SerializedName("dateLastChange")
    @Expose
    public String dateLastChange;
    @SerializedName("nationalNo")
    @Expose
    public String nationalNo;
    @SerializedName("disableForOtherParent")
    @Expose
    public boolean disableForOtherParent;
    @SerializedName("disableForPrivateCo")
    @Expose
    public boolean disableForPrivateCo;
    @SerializedName("economicCode")
    @Expose
    public String economicCode;
    @SerializedName("capacityDoorToDoorNo")
    @Expose
    public int capacityDoorToDoorNo;

    public Boolean getTariffBusIsLive() {
        return tariffBusIsLive;
    }

    public void setTariffBusIsLive(Boolean tariffBusIsLive) {
        this.tariffBusIsLive = tariffBusIsLive;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Boolean getCompanyTypeIsLocal() {
        return companyTypeIsLocal;
    }

    public void setCompanyTypeIsLocal(Boolean companyTypeIsLocal) {
        this.companyTypeIsLocal = companyTypeIsLocal;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOwnerCompanyIsText() {
        return ownerCompanyIsText;
    }

    public void setOwnerCompanyIsText(String ownerCompanyIsText) {
        this.ownerCompanyIsText = ownerCompanyIsText;
    }

    public String getNameFv() {
        return nameFv;
    }

    public void setNameFv(String nameFv) {
        this.nameFv = nameFv;
    }

    public Integer getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
    }

    public Integer getOwnerCompanyIs() {
        return ownerCompanyIs;
    }

    public void setOwnerCompanyIs(Integer ownerCompanyIs) {
        this.ownerCompanyIs = ownerCompanyIs;
    }

    public Integer getUserSuperiorNoMax() {
        return userSuperiorNoMax;
    }

    public void setUserSuperiorNoMax(Integer userSuperiorNoMax) {
        this.userSuperiorNoMax = userSuperiorNoMax;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCompanyIsOperationalBus() {
        return companyIsOperationalBus;
    }

    public void setCompanyIsOperationalBus(Boolean companyIsOperationalBus) {
        this.companyIsOperationalBus = companyIsOperationalBus;
    }

    public Integer getsOSCompanyId() {
        return sOSCompanyId;
    }

    public void setsOSCompanyId(Integer sOSCompanyId) {
        this.sOSCompanyId = sOSCompanyId;
    }

    public Integer getMainCompanyId() {
        return mainCompanyId;
    }

    public void setMainCompanyId(Integer mainCompanyId) {
        this.mainCompanyId = mainCompanyId;
    }

    public Integer getUserNoMax() {
        return userNoMax;
    }

    public void setUserNoMax(Integer userNoMax) {
        this.userNoMax = userNoMax;
    }

    public Integer getCarNo() {
        return carNo;
    }

    public void setCarNo(Integer carNo) {
        this.carNo = carNo;
    }

    public String getCodeInTree() {
        return codeInTree;
    }

    public void setCodeInTree(String codeInTree) {
        this.codeInTree = codeInTree;
    }

    public Boolean getCompanyIsSOS() {
        return companyIsSOS;
    }

    public void setCompanyIsSOS(Boolean companyIsSOS) {
        this.companyIsSOS = companyIsSOS;
    }

    public String getLegalTypeEnText() {
        return legalTypeEnText;
    }

    public void setLegalTypeEnText(String legalTypeEnText) {
        this.legalTypeEnText = legalTypeEnText;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLegalTypeSysParamValue() {
        return legalTypeSysParamValue;
    }

    public void setLegalTypeSysParamValue(String legalTypeSysParamValue) {
        this.legalTypeSysParamValue = legalTypeSysParamValue;
    }

    public Boolean getCompanyIsMain() {
        return companyIsMain;
    }

    public void setCompanyIsMain(Boolean companyIsMain) {
        this.companyIsMain = companyIsMain;
    }

    public String getAutoCompleteLabel() {
        return autoCompleteLabel;
    }

    public void setAutoCompleteLabel(String autoCompleteLabel) {
        this.autoCompleteLabel = autoCompleteLabel;
    }

    public Boolean getCompanyIsInternal() {
        return companyIsInternal;
    }

    public void setCompanyIsInternal(Boolean companyIsInternal) {
        this.companyIsInternal = companyIsInternal;
    }

    public Integer getCompanyType() {
        return companyType;
    }

    public void setCompanyType(Integer companyType) {
        this.companyType = companyType;
    }

    public String getCompanyTypeText() {
        return companyTypeText;
    }

    public void setCompanyTypeText(String companyTypeText) {
        this.companyTypeText = companyTypeText;
    }

    public String getProcessStatusText() {
        return processStatusText;
    }

    public void setProcessStatusText(String processStatusText) {
        this.processStatusText = processStatusText;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLegalTypeEn() {
        return legalTypeEn;
    }

    public void setLegalTypeEn(Integer legalTypeEn) {
        this.legalTypeEn = legalTypeEn;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public boolean isDisableForPrivateCoAndCityRegion() {
        return disableForPrivateCoAndCityRegion;
    }

    public void setDisableForPrivateCoAndCityRegion(boolean disableForPrivateCoAndCityRegion) {
        this.disableForPrivateCoAndCityRegion = disableForPrivateCoAndCityRegion;
    }

    public String getDateLastChange() {
        return dateLastChange;
    }

    public void setDateLastChange(String dateLastChange) {
        this.dateLastChange = dateLastChange;
    }

    public String getNationalNo() {
        return nationalNo;
    }

    public void setNationalNo(String nationalNo) {
        this.nationalNo = nationalNo;
    }


    public boolean isDisableForOtherParent() {
        return disableForOtherParent;
    }

    public void setDisableForOtherParent(boolean disableForOtherParent) {
        this.disableForOtherParent = disableForOtherParent;
    }

    public boolean isDisableForPrivateCo() {
        return disableForPrivateCo;
    }

    public void setDisableForPrivateCo(boolean disableForPrivateCo) {
        this.disableForPrivateCo = disableForPrivateCo;
    }

    public String getCompanyType_text() {
        return companyTypeText;
    }

    public void setCompanyType_text(String companyType_text) {
        this.companyTypeText = companyType_text;
    }

    public String getEconomicCode() {
        return economicCode;
    }

    public void setEconomicCode(String economicCode) {
        this.economicCode = economicCode;
    }

    public int getCapacityDoorToDoorNo() {
        return capacityDoorToDoorNo;
    }

    public void setCapacityDoorToDoorNo(int capacityDoorToDoorNo) {
        this.capacityDoorToDoorNo = capacityDoorToDoorNo;
    }
}
