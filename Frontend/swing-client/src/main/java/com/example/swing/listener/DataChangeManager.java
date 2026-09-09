package com.example.swing.listener;

import java.util.ArrayList;
import java.util.List;

public class DataChangeManager {
    private static DataChangeManager instance;
    private List<DataChangeListener> listeners = new ArrayList<>();

    private DataChangeManager() {}

    public static DataChangeManager getInstance() {
        if (instance == null) {
            instance = new DataChangeManager();
        }
        return instance;
    }

    public void addListener(DataChangeListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
            System.out.println("✅ Listener ajouté: " + listener.getClass().getSimpleName());
        }
    }

    public void removeListener(DataChangeListener listener) {
        listeners.remove(listener);
    }

    public void notifyDataChanged() {
        System.out.println("🔄 Notification de changement envoyée à " + listeners.size() + " listeners");
        for (DataChangeListener listener : listeners) {
            try {
                listener.onDataChanged();
            } catch (Exception e) {
                System.err.println("❌ Erreur lors de la notification: " + e.getMessage());
            }
        }
    }

    public void clear() {
        listeners.clear();
    }
}