package com.nextdots.airbnb.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Mariexi Hostienda on 05/12/16.
 */
public class SearchResults implements Serializable{

    @SerializedName("listing")
    private Listing listing;
    @SerializedName("pricing_quote")
    private PricingQuote pricing_quote;
    @SerializedName("marker_id")
    private String marker_id;


    public SearchResults(Listing listing, PricingQuote pricing_quote) {
        this.listing = listing;
        this.pricing_quote = pricing_quote;
    }

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public PricingQuote getPricing_quote() {
        return pricing_quote;
    }

    public void setPricing_quote(PricingQuote pricing_quote) {
        this.pricing_quote = pricing_quote;
    }

    public String getMarkerId() {
        return marker_id;
    }

    public void setMarkerId(String marker_id) {
        this.marker_id = marker_id;
    }
}
