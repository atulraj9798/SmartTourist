package com.smarttourist.agent.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tours")
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "start_location", nullable = false, length = 100)
    private String startLocation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_id", nullable = false)
    private Destination destination;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "number_of_days", nullable = false)
    private Integer numberOfDays;

    @Column(name = "num_adults", nullable = false)
    private Integer numAdults;

    @Column(name = "num_children", nullable = false)
    private Integer numChildren;

    @Column(name = "num_seniors", nullable = false)
    private Integer numSeniors;

    @Column(name = "total_travelers", nullable = false)
    private Integer totalTravelers;

    @Column(name = "special_requirements", length = 1000)
    private String specialRequirements;

    @Column(name = "total_cost")
    private Double totalCost;

    @Column(length = 50)
    private String status = "DRAFT"; // DRAFT, BOOKED, CANCELLED

    public Tour() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getStartLocation() { return startLocation; }
    public void setStartLocation(String startLocation) { this.startLocation = startLocation; }

    public Destination getDestination() { return destination; }
    public void setDestination(Destination destination) { this.destination = destination; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Integer getNumberOfDays() { return numberOfDays; }
    public void setNumberOfDays(Integer numberOfDays) { this.numberOfDays = numberOfDays; }

    public Integer getNumAdults() { return numAdults; }
    public void setNumAdults(Integer numAdults) { this.numAdults = numAdults; }

    public Integer getNumChildren() { return numChildren; }
    public void setNumChildren(Integer numChildren) { this.numChildren = numChildren; }

    public Integer getNumSeniors() { return numSeniors; }
    public void setNumSeniors(Integer numSeniors) { this.numSeniors = numSeniors; }

    public Integer getTotalTravelers() { return totalTravelers; }
    public void setTotalTravelers(Integer totalTravelers) { this.totalTravelers = totalTravelers; }

    public String getSpecialRequirements() { return specialRequirements; }
    public void setSpecialRequirements(String specialRequirements) { this.specialRequirements = specialRequirements; }

    public Double getTotalCost() { return totalCost; }
    public void setTotalCost(Double totalCost) { this.totalCost = totalCost; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
