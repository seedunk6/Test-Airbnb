package com.nextdots.airbnb.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Mariexi Hostienda on 05/12/16.
 */
public class PricingQuote implements Serializable{
    public PricingQuote(String nightly_price, String listing_currency) {
        this.nightly_price = nightly_price;
        this.listing_currency = listing_currency;
    }

    public String getListing_currency() {
        return listing_currency;
    }

    public void setListing_currency(String listing_currency) {
        this.listing_currency = listing_currency;
    }

    public String getNightly_price() {
        return nightly_price;
    }

    public void setNightly_price(String nightly_price) {
        this.nightly_price = nightly_price;
    }

    @SerializedName("listing_currency")
    private String listing_currency;
    @SerializedName("nightly_price")
    private String nightly_price;

}
