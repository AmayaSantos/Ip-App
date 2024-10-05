package meli.ipApp.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class HaversineCalculator {

  private static final BigDecimal R = new BigDecimal("6371.0"); // Radio de la Tierra en kilómetros

  public static Double haversine(double lat1, double lon1, double lat2, double lon2) {
    // Convertir grados a radianes
    BigDecimal dLat = toRadians(BigDecimal.valueOf(lat2 - lat1));
    BigDecimal dLon = toRadians(BigDecimal.valueOf(lon2 - lon1));

    BigDecimal lat1Rad = toRadians(BigDecimal.valueOf(lat1));
    BigDecimal lat2Rad = toRadians(BigDecimal.valueOf(lat2));

    // Fórmula Haversine
    BigDecimal a = haversineFormula(dLat, lat1Rad, lat2Rad, dLon);
    BigDecimal c = BigDecimal.valueOf(2).multiply(
        BigDecimal.valueOf(Math.atan2(Math.sqrt(a.doubleValue()), Math.sqrt(1 - a.doubleValue()))));

    return R.multiply(c).setScale(0, RoundingMode.HALF_UP).doubleValue(); // Distancia en kilómetros
  }

  private static BigDecimal haversineFormula(BigDecimal dLat, BigDecimal lat1Rad,
      BigDecimal lat2Rad, BigDecimal dLon) {
    BigDecimal sinDLat = BigDecimal.valueOf(Math.sin(dLat.doubleValue() / 2)).pow(2);
    BigDecimal sinDLon = BigDecimal.valueOf(Math.sin(dLon.doubleValue() / 2)).pow(2);

    BigDecimal cosLat1 = BigDecimal.valueOf(Math.cos(lat1Rad.doubleValue()));
    BigDecimal cosLat2 = BigDecimal.valueOf(Math.cos(lat2Rad.doubleValue()));

    return sinDLat.add(cosLat1.multiply(cosLat2).multiply(sinDLon));
  }

  private static BigDecimal toRadians(BigDecimal deg) {
    return deg.multiply(BigDecimal.valueOf(Math.PI))
        .divide(BigDecimal.valueOf(180), RoundingMode.HALF_UP);
  }
}
