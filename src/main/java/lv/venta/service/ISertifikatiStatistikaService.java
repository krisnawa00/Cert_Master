package lv.venta.service;

import java.util.Map;

public interface ISertifikatiStatistikaService {
    Map<String, Object> getGeneralStatistics() throws Exception;
    Map<String, Long> getStatisticsByMonth() throws Exception;
    Map<String, Long> getStatisticsByCourse() throws Exception;
    Map<String, Long> getStatisticsByCity() throws Exception;
}