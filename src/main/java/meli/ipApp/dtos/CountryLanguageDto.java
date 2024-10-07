package meli.ipApp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class CountryLanguageDto {

  private String name;
  private String iso639_1;

  public CountryLanguageDto copy() {
    return new CountryLanguageDto(this.name, this.iso639_1);
  }
}
