package edu.ucsb.cs156.gauchoride.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.AccessLevel;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "riderequests")
public class RideRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String day; // One of [Monday, Tuesday,..., Sunday]
  private String course;
  private String startTime;
  private String stopTime;
  private String building;
  private String room;
  private String pickupLocation;

  @ManyToOne
  @JoinColumn(name="riderId")
  private User rider;

  @Getter
  @Setter
  class RiderInfo {
    long id;
    String fullName;
    public RiderInfo(long id, String fullName){
      this.id = id;
      this.fullName = fullName;
    }
  }
  @Transient 
  public Object getRider(){
    return new RiderInfo(rider.getId(), rider.getFullName());
  }
}
