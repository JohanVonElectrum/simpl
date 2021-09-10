package com.davidfornesm.simpl;

import java.util.HashMap;

class State {
    private final HashMap<String, Integer> state;

    public State() {
        state = new HashMap<>();
    }

    int lookup(String name) {
        return state.get(name);
    }

    void setNewBinding(String name, int n) {
        state.put(name, n);
    }
}
