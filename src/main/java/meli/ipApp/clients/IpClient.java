package meli.ipApp.clients;

import meli.ipApp.dtos.IpInfoDto;

public interface IpClient {

  IpInfoDto getIpInfo(String ip);
}
