package meli.ipApp.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CountryLenguageDto {

  private String name;
  private String iso639_1;
}
