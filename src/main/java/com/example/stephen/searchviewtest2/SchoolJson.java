package com.example.stephen.searchviewtest2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by stephen on 17-5-29.
 */

public class SchoolJson {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("mes")
    @Expose
    private String mes;
    @SerializedName("data")
    @Expose
    private List<School> data = null;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public List<School> getData() {
        return data;
    }

    public void setData(List<School> data) {
        this.data = data;
    }
}
