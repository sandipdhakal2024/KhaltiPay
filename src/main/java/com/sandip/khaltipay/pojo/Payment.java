package com.sandip.khaltipay.pojo;

import javax.annotation.processing.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Payment {

    @SerializedName("pidx")
    @Expose
    private String pidx;
    @SerializedName("payment_url")
    @Expose
    private String paymentUrl;
    @SerializedName("expires_at")
    @Expose
    private String expiresAt;
    @SerializedName("expires_in")
    @Expose
    private Integer expiresIn;

    public String getPidx() {
        return pidx;
    }

    public void setPidx(String pidx) {
        this.pidx = pidx;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

}
