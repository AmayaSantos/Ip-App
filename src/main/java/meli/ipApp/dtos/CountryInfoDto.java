package meli.ipApp.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode
public class CountryInfoDto {

  @JsonIgnore
  private static final String OUT_COUNTRY_CODE = "1234";
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss (VV)");
  private String alpha2Code;
  private String name;
  private List<Double> latlng = new ArrayList<>();
  private Double longitude;
  private Double latitude;
  private Double distBsAs;
  private Set<CountryLenguageDto> languages = new HashSet<>();
  private Set<CountryCoinInfoDto> currencies = new HashSet<>();
  private List<String> timezones = new ArrayList<>();
  private List<String> timezonesCustom = new ArrayList<>();

  public static CountryInfoDto OUT_COUNTRY() {
    CountryInfoDto notFoundCountry = new CountryInfoDto();
    notFoundCountry.setAlpha2Code(OUT_COUNTRY_CODE);
    notFoundCountry.setCurrencies(new HashSet<>());
    notFoundCountry.setTimezones(new ArrayList<>());
    return notFoundCountry;
  }

  @PostConstruct
  public void setCustomLatlng() {
    if (!this.latlng.isEmpty()) {
      latitude = latlng.get(0);
      longitude = latlng.get(1);
    }
  }

  public boolean hasLocalization() {
    return !this.latlng.isEmpty();
  }

  public void updateCoinsInCountry(final Map<String, Double> coins) {
    this.currencies
        .forEach(coinInfo ->
            coinInfo.setDollarEquivalent(coins.get(coinInfo.getCode())));
  }

  public void setTime() {
    this.timezones
        .stream()
        .filter(StringUtils::isNotBlank)
        .map(s -> ZonedDateTime.now(ZoneId.of(s)))
        .forEach(time -> timezonesCustom.add(time.format(formatter)));
  }
}
