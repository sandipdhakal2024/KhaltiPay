package com.sandip.khaltipay.pojo;

import javax.annotation.processing.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Verification {

@SerializedName("pidx")
@Expose
private String pidx;
@SerializedName("total_amount")
@Expose
private Integer totalAmount;
@SerializedName("status")
@Expose
private String status;
@SerializedName("transaction_id")
@Expose
private String transactionId;
@SerializedName("fee")
@Expose
private Integer fee;
@SerializedName("refunded")
@Expose
private Boolean refunded;

public String getPidx() {
return pidx;
}

public void setPidx(String pidx) {
this.pidx = pidx;
}

public Integer getTotalAmount() {
return totalAmount;
}

public void setTotalAmount(Integer totalAmount) {
this.totalAmount = totalAmount;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public String getTransactionId() {
return transactionId;
}

public void setTransactionId(String transactionId) {
this.transactionId = transactionId;
}

public Integer getFee() {
return fee;
}

public void setFee(Integer fee) {
this.fee = fee;
}

public Boolean getRefunded() {
return refunded;
}

public void setRefunded(Boolean refunded) {
this.refunded = refunded;
}

}
