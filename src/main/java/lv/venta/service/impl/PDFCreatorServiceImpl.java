package lv.venta.service.impl;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.openpdf.text.Document;
import org.openpdf.text.Element;
import org.openpdf.text.FontFactory;
import org.openpdf.text.Image;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import lv.venta.model.Vertejums;
import lv.venta.repo.IKursaDalibnieksRepo;
import lv.venta.repo.IKurssRepo;
import lv.venta.repo.IVertejumsRepo;
import lv.venta.service.IPDFCreatorService;

@Slf4j
@Service
public class PDFCreatorServiceImpl implements IPDFCreatorService {


    @Autowired
    private IKurssRepo kurssRepo;

    @Autowired
    private IKursaDalibnieksRepo kursaDalibnieksRepo;

    @Autowired
    private IVertejumsRepo vertejumsRepo;



    @Override
    public void createCertificateAsPDF(int dalibnieksId, int kurssId) throws Exception {
    	
    	log.info("Sākam ģenerēt PDF sertifikātu: dalībnieksID={}, kurssID={}", dalibnieksId, kurssId);
    	
        if(!kurssRepo.existsById((long) kurssId)){
        	log.error("Kurss ar ID {} neeksistē", kurssId);
			throw new Exception("Kurss ar id: " + kurssId + " neeksistē");
		}

        if(!kursaDalibnieksRepo.existsById((long) dalibnieksId)) {
        	log.error("Kursa dalībnieks ar ID {} neeksistē", dalibnieksId);
			throw new Exception("Kursa dalībnieks ar id: " + dalibnieksId + " neeksistē");
		}

        
        
        Vertejums vertejumsNoDB = vertejumsRepo.findByDalibnieks_KdIdAndKursaDatums_Kurss_kId((long)dalibnieksId, (long)kurssId);

        if(vertejumsNoDB == null) {
        	log.error("Nav atrasts vērtējums dalībniekam ID={} un kursam ID={}", dalibnieksId, kurssId);
            throw new Exception("Nav pieejams vērtējums šim kursa dalībniekam" + dalibnieksId +  "un kursam" + kurssId);
        }
        else 
        {
        int gradeValue = Integer.parseInt(vertejumsNoDB.getVertejums());

        if(gradeValue < 4) {
				throw new Exception("Dalībniekam ar id: " + dalibnieksId + 
						" nav sekmīgi nokārtots kurss ar id: " + kurssId);
			}else 
            {
                String dalibniekaVardsUnUzvards = vertejumsNoDB.getDalibnieks().getVards() + " " + vertejumsNoDB.getDalibnieks().getUzvards();

                String kursaNosaukums = vertejumsNoDB.getKursaDatums().getKurss().getNosaukums();

                
                
                int vertejums = Integer.parseInt(vertejumsNoDB.getVertejums());
                //mybe stundas needs a change and has it do smh with creditpoints but not sure
                int stundas = vertejumsNoDB.getKursaDatums().getKurss().getStundas();
                LocalDate date = LocalDate.now();
            
                //change this part in the future mybe
                Random random = new Random();
                int certicateNo = 100000 + random.nextInt(900000);
                //////////////////////////////////////////////////

                Document document = new Document();

                PdfWriter writer = PdfWriter.getInstance(document, 
						new FileOutputStream(certicateNo + "_" + dalibniekaVardsUnUzvards+".pdf"));

                
                log.debug("PDF dokuments izveidots, sākam rakstīt saturu");
                document.open();
            
                Image img = Image.getInstance("src/main/resources/Img/tdl_school_logov2.png");
                document.add(img);
                log.debug("Pievienots logo");


                Paragraph p1 = new Paragraph( "TestDevLab Skola", 
                        FontFactory.getFont(FontFactory.HELVETICA, 11));
                Paragraph p2 = new Paragraph("CERTIFICATE", 
                        FontFactory.getFont(FontFactory.HELVETICA, 23));
                Paragraph p3 = new Paragraph("No." + certicateNo, FontFactory.getFont(FontFactory.HELVETICA, 13));
                
                Paragraph p4 = new Paragraph(dalibniekaVardsUnUzvards, FontFactory.getFont(FontFactory.HELVETICA_BOLD,38));
                
                Paragraph p5 = new Paragraph("has mastered non-formal education program", 
                        FontFactory.getFont(FontFactory.HELVETICA, 18));
                
                Paragraph p6 = new Paragraph(kursaNosaukums, FontFactory.getFont(FontFactory.HELVETICA, 28));
                
                Paragraph p7 = new Paragraph(stundas + " hours", FontFactory.getFont(FontFactory.HELVETICA, 13));
                
                Paragraph p8 = new Paragraph("Assessment: " + vertejums + " out of 10", FontFactory.getFont(FontFactory.HELVETICA, 18));
                
                Paragraph p12 = new Paragraph("________________________________", FontFactory.getFont(FontFactory.HELVETICA, 13));

                Paragraph p9 = new Paragraph("Raita Rollande", FontFactory.getFont(FontFactory.HELVETICA, 13));
                
                Paragraph p10 = new Paragraph("Head of eductional instution", FontFactory.getFont(FontFactory.HELVETICA, 13));
                
                Paragraph p11 = new Paragraph(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), FontFactory.getFont(FontFactory.HELVETICA, 13));
                
                Paragraph p13 = new Paragraph("\n");
                                
                // Center-aligned paragraphs
                Paragraph[] centerParagraphs = {p13,p1,p13, p2, p3,p13, p4,p13, p5, p6, p7};
                for (Paragraph p : centerParagraphs) {
                    p.setAlignment(Element.ALIGN_CENTER);
                    document.add(p);
                }

                // Left-aligned paragraphs
                Paragraph[] leftParagraphs = {p8,p13,p12, p9, p10,p13, p11};
                for (Paragraph p : leftParagraphs) {
                    p.setAlignment(Element.ALIGN_LEFT);
                    document.add(p);
                }
                
                Image img2 = Image.getInstance("src/main/resources/Img/footer.png");
                img2.scaleToFit(500, 100);
                img2.setAlignment(Element.FOOTNOTE);
                
                document.add(img2);


                document.close();
            
                log.debug("PDF sertifikāts veiksmīgi izveidots, PDF detaļas - Dalībnieks: {}, Kurss: {}, Vērtējums: {}/10", 
                    dalibniekaVardsUnUzvards, kursaNosaukums, vertejums);
            
            
            
            
            
            
            
            
            }
        }
    }
}
