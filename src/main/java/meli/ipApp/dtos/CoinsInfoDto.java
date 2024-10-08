package meli.ipApp.dtos;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoinsInfoDto {
  private Boolean success;
  private String base;
  private Map<String, Double> rates;
}
