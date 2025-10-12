package lv.venta.service.impl;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.openpdf.text.Document;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.FontFactory;
import org.openpdf.text.Image;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfWriter;



import lv.venta.model.Vertejums;
import lv.venta.repo.IKursaDalibnieksRepo;
import lv.venta.repo.IKurssRepo;
import lv.venta.repo.IVertejumsRepo;
import lv.venta.service.IPDFCreatorService;

public class PDFCreatorServiceImpl implements IPDFCreatorService {


    @Autowired
    private IKurssRepo kurssRepo;

    @Autowired
    private IKursaDalibnieksRepo kursaDalibnieksRepo;

    @Autowired
    private IVertejumsRepo vertejumsRepo;



    @Override
    public void createCertificateAsPDF(int dalibnieksId, int kurssId) throws Exception {
        
        if(!kurssRepo.existsById((long) kurssId)){
			throw new Exception("Kurss ar id: " + kurssId + " neeksistē");
		}

        if(!kursaDalibnieksRepo.existsById((long) dalibnieksId)) {
			throw new Exception("Kursa dalībnieks ar id: " + dalibnieksId + " neeksistē");
		}

        
        
        Vertejums vertejumsNoDB = vertejumsRepo.findByStudentKD_IDAndCourseK_ID(dalibnieksId, kurssId);

        if(vertejumsNoDB == null) {
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

            
                document.open();
            
                Image img = Image.getInstance("src/main/resources/Img/tdl_school_logo.png");
            
                document.add(img);


                Paragraph p1 = new Paragraph("TDL School", 
                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 36, Font.BOLD));


                p1.setAlignment(Element.ALIGN_CENTER);
                document.add(p1);
                
                document.add(new Paragraph("Sertifikats", FontFactory.getFont(FontFactory.HELVETICA,25,Font.BOLD))); 
                document.add(new Paragraph("No" + certicateNo, FontFactory.getFont(FontFactory.HELVETICA,20))); 
                document.add(new Paragraph(dalibniekaVardsUnUzvards, FontFactory.getFont(FontFactory.HELVETICA,20))); 
                document.add(new Paragraph("ir pabeidzis programmu",FontFactory.getFont(FontFactory.HELVETICA,15))); 
                document.add(new Paragraph(kursaNosaukums, FontFactory.getFont(FontFactory.HELVETICA,20))); 
                
                document.add(new Paragraph(stundas + "stundas\n\n\n", FontFactory.getFont(FontFactory.HELVETICA,15))); 
                
                document.add(new Paragraph("Atdzime" + vertejums + "no 10", FontFactory.getFont(FontFactory.HELVETICA,17)));
                document.add(new Paragraph(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),FontFactory.getFont(FontFactory.HELVETICA,15)));
                

                document.close();
            
            
            
            
            
            
            
            
            
            
            }
        }
    }
}
