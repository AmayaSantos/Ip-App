package meli.ipApp.dtos;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class StatisticCountryInfoDto {

  private static final BigDecimal INIT_CANT = BigDecimal.ZERO;
  private BigDecimal cantCalled;
  private CountryInfoDto country;

  public StatisticCountryInfoDto(CountryInfoDto country) {
    this.cantCalled = INIT_CANT;
    this.country = country;
  }

  public String getCountryCode() {
    return country.getAlpha2Code();
  }

  public void incrementCant() {
    this.cantCalled.add(BigDecimal.ONE);
  }

  public boolean hasBeenCalled() {
    return !this.cantCalled.equals(INIT_CANT);
  }

  public Double getDistBsAs() {
    return country.getDistBsAs();
  }

  public BigDecimal getTotalDist() {
    return BigDecimal.valueOf(country.getDistBsAs())
        .multiply(cantCalled);
  }

  public boolean moreClose(StatisticCountryInfoDto other) {
    return getDistBsAs() < other.getDistBsAs();
  }

  public StatisticCountryInfoDto getNearest(StatisticCountryInfoDto other) {
    return this.moreClose(other) ? this : other;
  }

  public StatisticCountryInfoDto getFurthest(StatisticCountryInfoDto other) {
    return this.moreClose(other) ? other : this;
  }
}