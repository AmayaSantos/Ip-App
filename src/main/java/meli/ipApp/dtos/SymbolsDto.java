package meli.ipApp.dtos;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SymbolsDto {
  private Boolean success;
  private Map<String, String> symbols;
}
