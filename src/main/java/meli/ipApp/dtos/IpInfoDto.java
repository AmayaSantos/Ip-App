package meli.ipApp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IpInfoDto {

  private String ip;
  @JsonProperty("country_code")
  private String countryCode;
  private CountryInfoDto countryInfoDto;
  private Double longitude;
  private Double latitude;

  public void setTime() {
    countryInfoDto.setTime();
  }

  public boolean isOutCountry() {
    return countryInfoDto.isOutCountry();
  }

  public void setOutCountryCode() {
    this.countryCode= countryInfoDto.getAlpha2Code();
  }
}
