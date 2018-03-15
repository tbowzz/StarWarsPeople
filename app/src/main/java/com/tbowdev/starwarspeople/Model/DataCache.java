package com.tbowdev.starwarspeople.Model;

import java.util.List;

import apollo.AllPeopleQuery;

/**
 * Created by tylerbowers on 3/14/18.
 */

public class DataCache {

    // Singleton instance
    private static DataCache mInstance;

    private List<AllPeopleQuery.person> mAllPeople;

    private DataCache() {}

    // Lazy loading of the Singleton instance
    public static DataCache getInstance() {
        if (mInstance == null) {
            mInstance = new DataCache();
        }

        return mInstance;
    }

    public List<AllPeopleQuery.person> getAllPeople() {
        return mAllPeople;
    }

    public void setAllPeople(List<AllPeopleQuery.person> mAllPeople) {
        this.mAllPeople = mAllPeople;
    }
}
