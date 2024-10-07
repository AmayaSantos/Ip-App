package meli.ipApp.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micrometer.common.util.StringUtils;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class CountryInfoDto {

  @JsonIgnore
  private static final String OUT_COUNTRY_CODE = "NOT HAVE COUNTRY";
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss (VV)");
  private String alpha2Code;
  private String name;
  private List<Double> latlng = new ArrayList<>();
  private Double longitude;
  private Double latitude;
  private Double distBsAs;
  private Set<CountryLanguageDto> languages = new HashSet<>();
  private Set<CountryCoinInfoDto> currencies = new HashSet<>();
  private Set<String> supportCoins = new HashSet<>();
  private List<String> timezones = new ArrayList<>();
  private List<String> timezonesCustom = new ArrayList<>();

  public static CountryInfoDto OUT_COUNTRY() {
    return CountryInfoDto.builder()
        .alpha2Code(OUT_COUNTRY_CODE)
        .currencies(new HashSet<>(0))
        .languages(new HashSet<>(0))
        .supportCoins(new HashSet<>(0))
        .timezones(new ArrayList<>(0))
        .timezonesCustom(new ArrayList<>(0))
        .latlng(new ArrayList<>(0))
        .build();
  }

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
    this.currencies.stream()
        .filter(coinInfo -> supportCoins.contains(coinInfo.getCode()))
        .forEach(coinInfo ->
            coinInfo.updateCoin(coins.get(coinInfo.getCode())));
  }

  public void setSupportCoins(Set<String> supportCoins) {
    this.supportCoins = this.currencies.stream()
        .map(CountryCoinInfoDto::getCode)
        .filter(supportCoins::contains)
        .collect(Collectors.toSet());
  }

  public void setTime() {
    this.timezones
        .stream()
        .filter(StringUtils::isNotBlank)
        .map(s -> ZonedDateTime.now(ZoneId.of(s)))
        .forEach(time -> timezonesCustom.add(time.format(formatter)));
  }

  public CountryInfoDto copy() {
    return CountryInfoDto.builder()
        .alpha2Code(alpha2Code)
        .name(name)
        .longitude(longitude)
        .latitude(latitude)
        .distBsAs(distBsAs)
        .timezones(new ArrayList<>(timezones))
        .supportCoins(new HashSet<>(supportCoins))
        .timezonesCustom(new ArrayList<>(timezonesCustom))
        .latlng(new ArrayList<>(latlng))
        .languages(this.languages.stream()
            .map(CountryLanguageDto::copy)
            .collect(Collectors.toSet()))
        .currencies(this.currencies.stream()
            .map(CountryCoinInfoDto::copy)
            .collect(Collectors.toSet()))
        .build();
  }

  public boolean isOutCountry() {
    return this.getAlpha2Code().equals(OUT_COUNTRY_CODE);
  }
}
