package com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client.message.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class YnBooleanAdapter extends XmlAdapter<String, Boolean> {

    @Override
    public String marshal(final Boolean v) throws Exception {
        if (v == null) {
            return null;
        }
        return v.equals(Boolean.TRUE) ? "Y" : "N";
    }

    @Override
    public Boolean unmarshal(final String v) throws Exception {
        if (v == null) {
            return null;
        }
        return v.trim().equalsIgnoreCase("Y") ? Boolean.TRUE : Boolean.FALSE;
    }
}
