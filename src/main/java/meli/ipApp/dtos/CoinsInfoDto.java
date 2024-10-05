package meli.ipApp.dtos;

import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoinsInfoDto {

  private String success;
  private String base;
  private Map<String, Double> rates;
}
