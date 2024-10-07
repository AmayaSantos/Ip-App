package meli.ipApp.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode
public class CountryLanguageDto {

  private String name;
  private String iso639_1;

  public CountryLanguageDto copy() {
    return CountryLanguageDto.builder()
        .name(name)
        .iso639_1(iso639_1)
        .build();
  }
}
