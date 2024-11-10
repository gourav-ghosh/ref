package com.devstringx.mytylesstockcheck.datamodal.ordersTabData;

import java.io.Serializable;
import java.util.Map;

public class CustomEntry<K, V> {
    private K key;
    private V value;
    private boolean isSelected = false;

    public CustomEntry(K key, V value) {
        this.key = key;
        this.value = value;
        this.isSelected = false; // By default, not selected
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

