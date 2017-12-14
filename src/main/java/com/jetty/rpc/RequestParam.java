package com.jetty.rpc;

import java.io.Serializable;
import java.util.Date;

public class RequestParam implements Serializable{
    private static final long serialVersionUID = 42L;

    private int intParam;

    private String strParam;
    
    private Object objParam;
    
    private Date dateParam;

    public int getIntParam() {
        return intParam;
    }

    public void setIntParam(int intParam) {
        this.intParam = intParam;
    }

    public String getStrParam() {
        return strParam;
    }

    public void setStrParam(String strParam) {
        this.strParam = strParam;
    }

    public Object getObjParam() {
        return objParam;
    }

    public void setObjParam(Object objParam) {
        this.objParam = objParam;
    }

    public Date getDateParam() {
        return dateParam;
    }

    public void setDateParam(Date dateParam) {
        this.dateParam = dateParam;
    }

    @Override
    public String toString() {
        return "RequestParam{" +
                "intParam=" + intParam +
                ", strParam='" + strParam + '\'' +
                ", objParam='" + objParam + '\'' +
                ", dateParam='" + dateParam + '\'' +
                '}';
    }
}
