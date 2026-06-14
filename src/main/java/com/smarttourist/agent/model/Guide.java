package com.smarttourist.agent.model;

import jakarta.persistence.*;

@Entity
@Table(name = "guides")
public class Guide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "photo_url", length = 255)
    private String photoUrl;

    @Column(length = 500)
    private String languages; // JSON list of languages or comma-separated string

    @Column(name = "experience_years", nullable = false)
    private Integer experienceYears;

    @Column(columnDefinition = "DOUBLE PRECISION DEFAULT 5.0")
    private Double rating = 5.0;

    @Column(length = 1000)
    private String certifications; // JSON list or string

    @Column(name = "charges_per_day", nullable = false)
    private Double chargesPerDay;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @Column(name = "current_location", length = 100)
    private String currentLocation;

    public Guide() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public String getLanguages() { return languages; }
    public void setLanguages(String languages) { this.languages = languages; }

    public Integer getExperienceYears() { return experienceYears; }
    public void setExperienceYears(Integer experienceYears) { this.experienceYears = experienceYears; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public String getCertifications() { return certifications; }
    public void setCertifications(String certifications) { this.certifications = certifications; }

    public Double getChargesPerDay() { return chargesPerDay; }
    public void setChargesPerDay(Double chargesPerDay) { this.chargesPerDay = chargesPerDay; }

    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }

    public String getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(String currentLocation) { this.currentLocation = currentLocation; }
}
