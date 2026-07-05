package com.example.evisitorai.service;

import com.example.evisitorai.dto.TouristForm;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Component
@SessionScope
public class CheckInDraft {

    private final List<TouristForm> tourists = new ArrayList<>();

    public void add(TouristForm tourist) {
        this.tourists.add(tourist);
    }

    public List<TouristForm> getAll() {
        return this.tourists;
    }

    public void removeAt(int index) {
        if (index >= 0 && index < this.tourists.size()) {
            this.tourists.remove(index);
        }
    }

    public void clear() {
        this.tourists.clear();
    }

    public boolean isEmpty() {
        return this.tourists.isEmpty();
    }
}
