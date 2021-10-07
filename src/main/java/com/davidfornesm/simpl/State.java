package com.davidfornesm.simpl;

import java.util.HashMap;
import java.util.Set;

class State {
    private final HashMap<String, Integer> state = new HashMap<>();

    Integer lookup(String name) {
        return state.get(name);
    }

    Boolean contains(String name) {
        return state.containsKey(name);
    }

    Set<String> keys() {
        return state.keySet();
    }

    void setNewBinding(String name, int n) {
        state.put(name, n);
    }
}
