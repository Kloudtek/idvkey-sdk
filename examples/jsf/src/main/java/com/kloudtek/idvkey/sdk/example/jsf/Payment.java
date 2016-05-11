package com.kloudtek.idvkey.sdk.example.jsf;

import java.io.Serializable;

/**
 * Created by yannick on 11/5/16.
 */
public class Payment implements Serializable {
    private String to;
    private int amount;

    public Payment(String to, int amount) {
        this.to = to;
        this.amount = amount;
    }

    public Payment() {
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
