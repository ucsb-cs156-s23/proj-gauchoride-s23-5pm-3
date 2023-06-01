package edu.ucsb.cs156.gauchoride.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.AccessLevel;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "drivershifts")
public class DriverShift {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private Long driverId;
  private String day; // format: one of [Monday, Tuesday,..., Sunday]
  private String startTime; // format: "HH:MM XM"
  private String stopTime; // format: "HH:MM XM"
}
