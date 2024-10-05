package meli.ipApp.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CountryCoinInfoDto {

  private String code;
  private Double dollarEquivalent;
}
