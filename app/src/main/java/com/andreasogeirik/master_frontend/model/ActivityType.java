package com.andreasogeirik.master_frontend.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andrena on 05.05.2016.
 */
public enum ActivityType {
    WALK(0), RUN(1), BIKE(2), SKI(3), SWIM(4), OTHER(5);

    private int id;

    ActivityType(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    private static final Map<Integer, ActivityType> idToTypeMap = new HashMap<Integer, ActivityType>();

    static {
        for (ActivityType type : ActivityType.values()) {
            idToTypeMap.put(type.id, type);
        }
    }

    public static ActivityType getTypeById(int id) {
        return idToTypeMap.get(Integer.valueOf(id));
    }
}