package meli.ipApp.dtos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AverageDataDto {

  private BigDecimal totalCalls;
  private BigDecimal totalDist;
  private BigDecimal average;

  public AverageDataDto(StatisticCountryInfoDto statistic) {
    this.totalCalls = statistic.getCantCalled();
    this.totalDist = statistic.getTotalDist();
  }

  public AverageDataDto reduce(AverageDataDto averageDataDto) {
    this.totalCalls = this.totalCalls.add(averageDataDto.getTotalCalls());
    this.totalDist = this.totalDist.add(averageDataDto.getTotalDist());
    return this;
  }

  public AverageDataDto setAverage() {
    this.average=totalDist.divide(totalCalls, RoundingMode.CEILING);
    return this;
  }
}
