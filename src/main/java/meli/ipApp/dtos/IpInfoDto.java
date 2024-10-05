package meli.ipApp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IpInfoDto {

  private String ip;
  private String countryCode;
  private CountryInfoDto countryInfoDto;

  public void setTime() {
    countryInfoDto.setTime();
  }
}
