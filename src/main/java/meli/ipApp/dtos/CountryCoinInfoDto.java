package meli.ipApp.dtos;

import static java.util.Objects.isNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import meli.ipApp.exepctions.AppException;
import meli.ipApp.exepctions.errors.CoinError;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class CountryCoinInfoDto {

  private String code;
  private Double dollarEquivalent;

  public CountryCoinInfoDto copy() {
    return CountryCoinInfoDto.builder()
        .code(code)
        .dollarEquivalent(dollarEquivalent)
        .build();
  }

  public void updateCoin(Double dollarEquivalent) {
    if (isNull(dollarEquivalent)) {
      throw new AppException(CoinError.REQUIRED_COIN_DOLLAR_EQUIVALENT);
    }
    this.dollarEquivalent = dollarEquivalent;
  }

}
