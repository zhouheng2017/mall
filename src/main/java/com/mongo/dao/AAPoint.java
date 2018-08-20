package com.mongo.dao;

import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "aaPoint")
@ToString
public class AAPoint {
    @Id
    private Integer id;

    private String number;

    private String name;

    private String currentUsedVersion;

    private String associationVerification;

    public AAPoint(Integer id, String number, String name, String currentUsedVersion, String associationVerification) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.currentUsedVersion = currentUsedVersion;
        this.associationVerification = associationVerification;
    }

    public AAPoint() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomber() {
        return number;
    }

    public void setNomber(String number) {
        this.number = number == null ? null : number.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCurrentUsedVersion() {
        return currentUsedVersion;
    }

    public void setCurrentUsedVersion(String currentUsedVersion) {
        this.currentUsedVersion = currentUsedVersion == null ? null : currentUsedVersion.trim();
    }

    public String getAssocationVerification() {
        return associationVerification;
    }

    public void setAssocationVerification(String associationVerification) {
        this.associationVerification = associationVerification == null ? null : associationVerification.trim();
    }
}