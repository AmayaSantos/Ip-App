package meli.ipApp.dtos;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatisticCountryInfoDto {

  private static final BigDecimal INIT_CANT = BigDecimal.ZERO;
  private BigDecimal cantCalled;
  private CountryInfoDto country;
  private BigDecimal totalDistance;

  @JsonIgnore
  private Double outCountryFarthestDistance;
  @JsonIgnore
  private Double outCountryNearestDistance;


  public StatisticCountryInfoDto(CountryInfoDto country) {
    this.cantCalled = INIT_CANT;
    this.country = country;
    this.totalDistance = INIT_CANT;

  }

  public String getCountryCode() {
    return country.getAlpha2Code();
  }

  public void incrementCant() {
    this.cantCalled = this.cantCalled.add(BigDecimal.ONE);
  }

  public boolean hasBeenCalled() {
    return !this.cantCalled.equals(INIT_CANT);
  }

  public Double getDistBsAs() {
    return country.getDistBsAs();
  }

  public BigDecimal getTotalDist() {
    if (CountryInfoDto.OUT_COUNTRY().getAlpha2Code().equals(country.getAlpha2Code())) {
      return totalDistance;
    }
    return BigDecimal.valueOf(country.getDistBsAs())
        .multiply(cantCalled);
  }

  private boolean moreClose(StatisticCountryInfoDto other) {
    if (isNull(outCountryNearestDistance) && isNull(other.outCountryNearestDistance)) {
      return getDistBsAs() < other.getDistBsAs();
    }
    if (isNull(outCountryNearestDistance)) {
      return getDistBsAs() < other.outCountryNearestDistance;
    }
    return outCountryNearestDistance < other.getDistBsAs();
  }

  private boolean moreFar(StatisticCountryInfoDto other) {
    if (isNull(outCountryFarthestDistance) && isNull(other.outCountryFarthestDistance)) {
      return getDistBsAs() > other.getDistBsAs();
    }
    if (isNull(outCountryFarthestDistance)) {
      return getDistBsAs() > other.outCountryFarthestDistance;
    }
    return outCountryFarthestDistance > other.getDistBsAs();
  }

  public StatisticCountryInfoDto getNearest(StatisticCountryInfoDto other) {
    return this.moreClose(other) ? this : other;
  }

  public StatisticCountryInfoDto getFurthest(StatisticCountryInfoDto other) {
    return this.moreFar(other) ? this : other;
  }

  public void updateOutCountryDistance(Double newDistance) {
    cantCalled = BigDecimal.ONE;
    totalDistance = totalDistance.add(BigDecimal.valueOf(newDistance));
    if (isNull(outCountryFarthestDistance)) {
      outCountryNearestDistance = newDistance;
      outCountryFarthestDistance = newDistance;
    }

    if (outCountryNearestDistance > newDistance) {
      outCountryNearestDistance = newDistance;
    }

    if (outCountryFarthestDistance < newDistance) {
      outCountryFarthestDistance = newDistance;
    }
  }
}