package meli.ipApp.dtos;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticDto {

  private StatisticCountryInfoDto closestCountry;
  private StatisticCountryInfoDto furthestCountry;
  private BigDecimal promDist;
}
