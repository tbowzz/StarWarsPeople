package com.tbowdev.starwarspeople.Model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import apollo.AllPeopleQuery;

/**
 * Created by tylerbowers on 3/14/18.
 */

public class DataCache extends Observable {

    private static final String TAG = "DataCache";

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

    public void setAllPeople(List<AllPeopleQuery.person> people) {
        this.mAllPeople = new ArrayList<>();
        this.mAllPeople.addAll(people);

        Log.d(TAG, "mAllPeople updated.");
        sendToObservers(people);
    }

    // Observer Design Pattern functions
    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
    }

    @Override
    public synchronized void deleteObserver(Observer o) {
        super.deleteObserver(o);
    }

    public void sendToObservers(Object arg)
    {
        setChanged();
        notifyObservers(arg);
        clearChanged();
    }
}
