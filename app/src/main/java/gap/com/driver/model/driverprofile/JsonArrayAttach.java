package gap.com.driver.model.driverprofile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JsonArrayAttach {
    @SerializedName("attachFileJsonArray")
    @Expose
    public List<Integer> attachFileJsonArray = null;
    @SerializedName("attachFileId")
    @Expose
    public Integer attachFileId;

    public List<Integer> getAttachFileJsonArray() {
        return attachFileJsonArray;
    }

    public void setAttachFileJsonArray(List<Integer> attachFileJsonArray) {
        this.attachFileJsonArray = attachFileJsonArray;
    }

    public Integer getAttachFileId() {
        return attachFileId;
    }

    public void setAttachFileId(Integer attachFileId) {
        this.attachFileId = attachFileId;
    }
}
