package com.nextdots.airbnb.models;

/**
 * Created by Mariexi Hostienda on 06/12/16.
 */
public class ResDetail {
    Listing listing;

    public ResDetail(Listing listing) {
        this.listing = listing;
    }

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

}
