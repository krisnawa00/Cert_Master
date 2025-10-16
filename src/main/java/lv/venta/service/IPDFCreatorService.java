package lv.venta.service;

public interface IPDFCreatorService {

    void createCertificateAsPDF(int dalibnieksId, int kurssId) throws Exception;
}
