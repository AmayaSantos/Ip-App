package meli.ipApp.services;

import meli.ipApp.dtos.IpInfoDto;

public interface IpService {

  IpInfoDto getIpInfo(String ip);
}
