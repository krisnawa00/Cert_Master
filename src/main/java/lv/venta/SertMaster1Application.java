package lv.venta;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lv.venta.model.EParakstaLogs;
import lv.venta.model.KursaDalibnieks;
import lv.venta.model.KursaDatumi;
import lv.venta.model.Kurss;
import lv.venta.model.Lietotajs;
import lv.venta.model.MacibuRezultati;
import lv.venta.model.Pasniedzeji;
import lv.venta.model.Sertifikatu_registracijas_tabula;
import lv.venta.model.Vertejums;
import lv.venta.model.sertifikati;
import lv.venta.model.enums.Limenis;
import lv.venta.model.enums.Pilseta;
import lv.venta.model.enums.Valsts;
import lv.venta.repo.IEParakstaLogsRepo;
import lv.venta.repo.IKursaDalibnieksRepo;
import lv.venta.repo.IKursaDatumiRepo;
import lv.venta.repo.IKurssRepo;
import lv.venta.repo.ILietotajsRepo;
import lv.venta.repo.IMacibuRezultatiRepo;

import lv.venta.repo.IPasniedzejiRepo;

import lv.venta.repo.ISertRegTab;
import lv.venta.repo.ISertifikatiRepo;
import lv.venta.repo.IVertejumsRepo;

@SpringBootApplication
public class SertMaster1Application {

	public static void main(String[] args) {
		SpringApplication.run(SertMaster1Application.class, args);
	};
	
	
	
	@Bean
	public CommandLineRunner testModel(IEParakstaLogsRepo EPRepo, IKursaDalibnieksRepo KDalRepo, IKursaDatumiRepo KDatRepo, IKurssRepo KRepo, ILietotajsRepo LietRepo, 
			IMacibuRezultatiRepo MacRezRepo, IPasniedzejiRepo PasRepo, ISertifikatiRepo sertRepo, ISertRegTab SertRegRepo, IVertejumsRepo VertRepo) {
		
		return new CommandLineRunner() {
			
			@Override
			public void run(String... args) throws Exception {
				
				

				
				KursaDalibnieks KD1 = new KursaDalibnieks("Jānis","Bērziņš","janis.berzins@example.com","+37120000001","12345678901",Pilseta.Rīga,Valsts.Latvija,"Brīvības iela 1","5","LV-1001");
				KursaDalibnieks KD2 = new KursaDalibnieks("Ilze","Kalniņa","ilze.kalnina@example.com","+37120000002","22334455667",Pilseta.Jelgava,Valsts.Latvija,"Lielā iela 10","12","LV-3001");
				KursaDalibnieks KD3 = new KursaDalibnieks("Pēteris","Ozoliņš","peteris.ozolins@example.com","+37120000003","33445566778",Pilseta.Liepāja,Valsts.Latvija,"Raiņa iela 20","3","LV-3401");
				KursaDalibnieks KD4 = new KursaDalibnieks("Santa","Krūmiņa","santa.krumina@example.com","+37120000004","44556677889",Pilseta.Ventspils,Valsts.Latvija,"Saulrietu iela 5","7","LV-3601");

				KDalRepo.saveAll(Arrays.asList(KD1,KD2,KD3,KD4));
				
				
				 Kurss K1 = new Kurss("Java fundamentals", 40, Limenis.Beginner);
			     Kurss K2 = new Kurss("Spring framework", 60, Limenis.Intermediate);
			     Kurss K3 = new Kurss("Docker fundamentals", 30, Limenis.Junior);
			     Kurss K4 = new Kurss("React fundamentals", 50, Limenis.other);

			        KRepo.saveAll(Arrays.asList(K1, K2, K3, K4));
				
			     MacibuRezultati MacR1 = new MacibuRezultati(K1, true);
			     MacibuRezultati MacR2 = new MacibuRezultati(K2, false);
			     MacibuRezultati MacR3 = new MacibuRezultati(K3, true);
			     MacibuRezultati MacR4 = new MacibuRezultati(K4, true);

			        MacRezRepo.saveAll(Arrays.asList(MacR1, MacR2, MacR3, MacR4));  
			        
			        
			        
			     Pasniedzeji P1 = new Pasniedzeji("Jānis", "Kalniņš");
			     Pasniedzeji P2 = new Pasniedzeji("Ilze", "Bērziņa");
			     Pasniedzeji P3 = new Pasniedzeji("Pēteris", "Ozoliņš");
			     Pasniedzeji P4 = new Pasniedzeji("Santa", "Krūmiņa");

			        PasRepo.saveAll(Arrays.asList(P1, P2, P3, P4));    
			        
			        
			        
				KursaDatumi kursaDatums1 = new KursaDatumi(K1, P1, "2025-06-15", "2025-06-30");
		        KursaDatumi kursaDatums2 = new KursaDatumi(K1, P2, "2025-07-01", "2025-07-15");
		        KursaDatumi kursaDatums3 = new KursaDatumi(K1, P3, "2025-07-16", "2025-07-31");
		        KursaDatumi kursaDatums4 = new KursaDatumi(K1, P4, "2025-08-01", "2025-08-15");
		        
		        
		        	KDatRepo.saveAll(Arrays.asList(kursaDatums1, kursaDatums2, kursaDatums3, kursaDatums4));
				
			        sertifikati Sert1 = new sertifikati(KD1, kursaDatums1, LocalDate.of(2024, 5, 10), true);
			        sertifikati Sert2 = new sertifikati(KD2, kursaDatums2, LocalDate.of(2024, 5, 11), false);
			        sertifikati Sert3 = new sertifikati(KD3, kursaDatums3, LocalDate.of(2024, 5, 12), true);
			        sertifikati Sert4 = new sertifikati(KD4, kursaDatums4, LocalDate.of(2024, 5, 13), false);

			        sertRepo.saveAll(Arrays.asList(Sert1, Sert2, Sert3, Sert4));
				
				
		        Vertejums V1 = new Vertejums(kursaDatums1, KD1, "10", LocalDate.of(2025, 6, 15));
		        Vertejums V2 = new Vertejums(kursaDatums2, KD2, "9", LocalDate.of(2025, 6, 15));
		        Vertejums V3 = new Vertejums(kursaDatums3, KD3, "8", LocalDate.of(2025, 6, 15));
		        Vertejums V4 = new Vertejums(kursaDatums4, KD4, "10", LocalDate.of(2025, 6, 15));

		        VertRepo.saveAll(Arrays.asList(V1, V2, V3, V4));
		        
		        
		        
		        Lietotajs Liet1 = new Lietotajs("password123", "lietotajs1@example.com");
		        Lietotajs Liet2 = new Lietotajs("password234", "lietotajs2@example.com");
		        Lietotajs Liet3 = new Lietotajs("password345", "lietotajs3@example.com");
		        Lietotajs Liet4 = new Lietotajs("password456", "lietotajs4@example.com");

		        LietRepo.saveAll(Arrays.asList(Liet1, Liet2, Liet3, Liet4));

		        
		        Sertifikatu_registracijas_tabula SRT1 = new Sertifikatu_registracijas_tabula(Liet1, Sert1, "2024-05-15");
		        Sertifikatu_registracijas_tabula SRT2 = new Sertifikatu_registracijas_tabula(Liet2, Sert2, "2024-05-16");
		        Sertifikatu_registracijas_tabula SRT3 = new Sertifikatu_registracijas_tabula(Liet3, Sert3, "2024-05-17");
		        Sertifikatu_registracijas_tabula SRT4 = new Sertifikatu_registracijas_tabula(Liet4, Sert4, "2024-05-18");

		        SertRegRepo.saveAll(Arrays.asList(SRT1, SRT2, SRT3, SRT4));
		        
		        
		        
		        EParakstaLogs log1 = new EParakstaLogs(Sert1, LocalDate.of(2024, 6, 1), "Parakstīts");
		        EParakstaLogs log2 = new EParakstaLogs(Sert2, LocalDate.of(2024, 6, 2), "Neparakstīts");
		        EParakstaLogs log3 = new EParakstaLogs(Sert3, LocalDate.of(2024, 6, 3), "Parakstīts");
		        EParakstaLogs log4 = new EParakstaLogs(Sert4, LocalDate.of(2024, 6, 4), "Neparakstīts");

		        EPRepo.saveAll(Arrays.asList(log1, log2, log3, log4));

				
				
				
				
			}
		};
		
	}
	
}
