package com.jobhunter.jobhunter.domain.dto.attach;

public class General_ {
    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public General_(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public General_() {
    }

}
