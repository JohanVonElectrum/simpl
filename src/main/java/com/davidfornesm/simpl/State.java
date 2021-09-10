package com.davidfornesm.simpl;

import java.util.HashMap;

class State {
    private HashMap<String, Integer> state = new HashMap<>();
    // private int state [ ]; // Variables are treated as indices to this vector; this simplifies the treatment

    public State()
    {  state = new HashMap<>();
    }

    int lookup(String name) {
        return state.get(name);
    }

    void setNewBinding(String name, int n) {
        state.put(name, n);
    }
}
