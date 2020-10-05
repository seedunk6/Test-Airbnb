package com.nextdots.airbnb.models;

import java.util.ArrayList;

/**
 * Created by Mariexi Hostienda on 05/12/16.
 */
public class ResHotel {
    public ResHotel(ArrayList<SearchResults> search_results) {
        this.search_results = search_results;
    }

    public ArrayList<SearchResults> getSearch_results() {
        return search_results;
    }

    public void setSearch_results(ArrayList<SearchResults> search_results) {
        this.search_results = search_results;
    }

    ArrayList<SearchResults> search_results;
}
