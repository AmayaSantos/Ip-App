package meli.ipApp.dtos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AverageDistanceDto {

  private BigDecimal totalCalls;
  private BigDecimal totalDist;

  public AverageDistanceDto(StatisticCountryInfoDto statistic) {
    this.totalCalls = statistic.getCantCalled();
    this.totalDist = statistic.getTotalDist();
  }

  public AverageDistanceDto reduce(AverageDistanceDto averageDistanceDto) {
    this.totalCalls = this.totalCalls.add(averageDistanceDto.getTotalCalls());
    this.totalDist = this.totalDist.add(averageDistanceDto.getTotalDist());
    return this;
  }

  public BigDecimal getAverage() {
    return totalDist.divide(totalCalls, RoundingMode.CEILING);
  }
}
