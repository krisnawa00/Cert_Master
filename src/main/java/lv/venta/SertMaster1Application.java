package lv.venta;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import lv.venta.model.EParakstaLogs;
import lv.venta.model.KursaDalibnieks;
import lv.venta.model.KursaDatumi;
import lv.venta.model.Kurss;
import lv.venta.model.Lietotajs;
import lv.venta.model.MacibuRezultati;
import lv.venta.model.MyAuthority;
import lv.venta.model.MyUser;
import lv.venta.model.Pasniedzeji;
import lv.venta.model.Sertifikati;
import lv.venta.model.Sertifikatu_registracijas_tabula;
import lv.venta.model.Vertejums;
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
import lv.venta.repo.IMyAuthorityRepo;
import lv.venta.repo.IMyUserRepo;

@SpringBootApplication
public class SertMaster1Application {

	public static void main(String[] args) {
		SpringApplication.run(SertMaster1Application.class, args);
	}
	
	@Bean
	public CommandLineRunner testModel(IEParakstaLogsRepo EPRepo, IKursaDalibnieksRepo KDalRepo, 
			IKursaDatumiRepo KDatRepo, IKurssRepo KRepo, ILietotajsRepo LietRepo, 
			IMacibuRezultatiRepo MacRezRepo, IPasniedzejiRepo PasRepo, ISertifikatiRepo sertRepo, 
			ISertRegTab SertRegRepo, IVertejumsRepo VertRepo, IMyAuthorityRepo authRepo, IMyUserRepo userRepo) {
		
		return new CommandLineRunner() {
			
			@Override
			public void run(String... args) throws Exception {
				
				// ========== KURSA DALĪBNIEKI (24 PERSONAS) ==========
				KursaDalibnieks KD1 = new KursaDalibnieks("Jānis","Bērziņš","janis.berzins@example.com","+37120000001","12345678901",Pilseta.Rīga,Valsts.Latvija,"Brīvības iela 1","5","LV-1001");
				KursaDalibnieks KD2 = new KursaDalibnieks("Ilze","Kalniņa","ilze.kalnina@example.com","+37120000002","22334455667",Pilseta.Jelgava,Valsts.Latvija,"Lielā iela 10","12","LV-3001");
				KursaDalibnieks KD3 = new KursaDalibnieks("Pēteris","Ozoliņš","peteris.ozolins@example.com","+37120000003","33445566778",Pilseta.Liepāja,Valsts.Latvija,"Raiņa iela 20","3","LV-3401");
				KursaDalibnieks KD4 = new KursaDalibnieks("Santa","Krūmiņa","santa.krumina@example.com","+37120000004","44556677889",Pilseta.Ventspils,Valsts.Latvija,"Saulrietu iela 5","7","LV-3601");
				KursaDalibnieks KD5 = new KursaDalibnieks("Artūrs","Liepa","arturs.liepa@example.com","+37120000005","55566677890",Pilseta.Daugavpils,Valsts.Latvija,"Saules iela 15","8","LV-5401");
				KursaDalibnieks KD6 = new KursaDalibnieks("Kristīne","Vītola","kristine.vitola@example.com","+37120000006","66677788901",Pilseta.Jūrmala,Valsts.Latvija,"Jomas iela 25","14","LV-2015");
				KursaDalibnieks KD7 = new KursaDalibnieks("Mārtiņš","Egle","martins.egle@example.com","+37120000007","77788899012",Pilseta.Rēzekne,Valsts.Latvija,"Atbrīvošanas aleja 30","2","LV-4601");
				KursaDalibnieks KD8 = new KursaDalibnieks("Laura","Apse","laura.apse@example.com","+37120000008","88899900123",Pilseta.Valmiera,Valsts.Latvija,"Rīgas iela 42","9","LV-4201");
				KursaDalibnieks KD9 = new KursaDalibnieks("Edgars","Kļava","edgars.klava@example.com","+37120000009","99900011234",Pilseta.Jēkabpils,Valsts.Latvija,"Brīvības bulvāris 18","6","LV-5201");
				KursaDalibnieks KD10 = new KursaDalibnieks("Agnese","Priede","agnese.priede@example.com","+37120000010","10011122345",Pilseta.Ogre,Valsts.Latvija,"Meža prospekts 7","11","LV-5001");
				
				KursaDalibnieks KD11 = new KursaDalibnieks("Roberts","Ozols","roberts.ozols@example.com","+37120000011","11122233456",Pilseta.Rīga,Valsts.Latvija,"Maskavas iela 100","22","LV-1003");
				KursaDalibnieks KD12 = new KursaDalibnieks("Ieva","Liepiņa","ieva.liepina@example.com","+37120000012","12233344567",Pilseta.Jelgava,Valsts.Latvija,"Dobeles šoseja 45","3","LV-3002");
				KursaDalibnieks KD13 = new KursaDalibnieks("Andris","Kalns","andris.kalns@example.com","+37120000013","13344455678",Pilseta.Liepāja,Valsts.Latvija,"Graudu iela 68","15","LV-3405");
				KursaDalibnieks KD14 = new KursaDalibnieks("Līga","Pūce","liga.puce@example.com","+37120000014","14455566789",Pilseta.Cēsis,Valsts.Latvija,"Lenču iela 9","4","LV-4101");
				KursaDalibnieks KD15 = new KursaDalibnieks("Gatis","Straujš","gatis.straujs@example.com","+37120000015","15566677890",Pilseta.Talsi,Valsts.Latvija,"Kareivju iela 12","7","LV-3201");
				KursaDalibnieks KD16 = new KursaDalibnieks("Sanda","Meža","sanda.meza@example.com","+37120000016","16677788901",Pilseta.Tukums,Valsts.Latvija,"Pils iela 5","10","LV-3101");
				KursaDalibnieks KD17 = new KursaDalibnieks("Valdis","Straume","valdis.straume@example.com","+37120000017","17788899012",Pilseta.Dobele,Valsts.Latvija,"Uzvaras iela 23","8","LV-3701");
				KursaDalibnieks KD18 = new KursaDalibnieks("Dace","Sala","dace.sala@example.com","+37120000018","18899900123",Pilseta.Bauska,Valsts.Latvija,"Plūdoņa iela 17","6","LV-3901");
				KursaDalibnieks KD19 = new KursaDalibnieks("Jānis","Vilks","janis.vilks@example.com","+37120000019","19900011234",Pilseta.Aizkraukle,Valsts.Latvija,"Gaismas iela 31","12","LV-5101");
				KursaDalibnieks KD20 = new KursaDalibnieks("Zane","Upīte","zane.upite@example.com","+37120000020","20011122345",Pilseta.Madona,Valsts.Latvija,"Skolas iela 14","5","LV-4801");
				
				KursaDalibnieks KD21 = new KursaDalibnieks("Kaspars","Birze","kaspars.birze@example.com","+37120000021","21122233456",Pilseta.Gulbene,Valsts.Latvija,"Ābeļu iela 8","9","LV-4401");
				KursaDalibnieks KD22 = new KursaDalibnieks("Elīna","Saulīte","elina.saulite@example.com","+37120000022","22233344567",Pilseta.Sigulda,Valsts.Latvija,"Pils iela 16","3","LV-2150");
				KursaDalibnieks KD23 = new KursaDalibnieks("Normunds","Ziemeļnieks","normunds.ziemelnieks@example.com","+37120000023","23344455678",Pilseta.Salaspils,Valsts.Latvija,"Acones iela 2","11","LV-2169");
				KursaDalibnieks KD24 = new KursaDalibnieks("Madara","Rudzīte","madara.rudzite@example.com","+37120000024","24455566789",Pilseta.Saldus,Valsts.Latvija,"Striķu iela 21","4","LV-3801");

				KDalRepo.saveAll(Arrays.asList(KD1, KD2, KD3, KD4, KD5, KD6, KD7, KD8, KD9, KD10,
						KD11, KD12, KD13, KD14, KD15, KD16, KD17, KD18, KD19, KD20, KD21, KD22, KD23, KD24));
				
				// ========== KURSI ==========
				Kurss K1 = new Kurss("Java fundamentals", 40, Limenis.Beginner);
				Kurss K2 = new Kurss("Spring framework", 60, Limenis.Intermediate);
				Kurss K3 = new Kurss("Docker fundamentals", 30, Limenis.Junior);
				Kurss K4 = new Kurss("React fundamentals", 50, Limenis.other);
				Kurss K5 = new Kurss("Python basics", 35, Limenis.Beginner);
				Kurss K6 = new Kurss("JavaScript advanced", 45, Limenis.Advanced);

				KRepo.saveAll(Arrays.asList(K1, K2, K3, K4, K5, K6));
				
				// ========== MĀCĪBU REZULTĀTI ==========
				MacibuRezultati MacR1 = new MacibuRezultati(K1, true);
				MacibuRezultati MacR2 = new MacibuRezultati(K2, false);
				MacibuRezultati MacR3 = new MacibuRezultati(K3, true);
				MacibuRezultati MacR4 = new MacibuRezultati(K4, true);
				MacibuRezultati MacR5 = new MacibuRezultati(K5, true);
				MacibuRezultati MacR6 = new MacibuRezultati(K6, false);

				MacRezRepo.saveAll(Arrays.asList(MacR1, MacR2, MacR3, MacR4, MacR5, MacR6));  
				
				// ========== PASNIEDZĒJI ==========
				Pasniedzeji P1 = new Pasniedzeji("Jānis", "Kalniņš");
				Pasniedzeji P2 = new Pasniedzeji("Ilze", "Bērziņa");
				Pasniedzeji P3 = new Pasniedzeji("Pēteris", "Ozoliņš");
				Pasniedzeji P4 = new Pasniedzeji("Santa", "Krūmiņa");
				Pasniedzeji P5 = new Pasniedzeji("Andris", "Liepa");
				Pasniedzeji P6 = new Pasniedzeji("Laura", "Vītoliņa");

				PasRepo.saveAll(Arrays.asList(P1, P2, P3, P4, P5, P6));    
				
				// ========== KURSA DATUMI ==========
				KursaDatumi kursaDatums1 = new KursaDatumi(K1, P1, "2025-01-15", "2025-01-30");
				KursaDatumi kursaDatums2 = new KursaDatumi(K2, P2, "2025-02-01", "2025-02-15");
				KursaDatumi kursaDatums3 = new KursaDatumi(K3, P3, "2025-02-16", "2025-02-28");
				KursaDatumi kursaDatums4 = new KursaDatumi(K4, P4, "2025-03-01", "2025-03-15");
				KursaDatumi kursaDatums5 = new KursaDatumi(K5, P5, "2025-03-16", "2025-03-31");
				KursaDatumi kursaDatums6 = new KursaDatumi(K6, P6, "2025-04-01", "2025-04-15");
				
				KDatRepo.saveAll(Arrays.asList(kursaDatums1, kursaDatums2, kursaDatums3, kursaDatums4, kursaDatums5, kursaDatums6));
				
				// ========== SERTIFIKĀTI (24 SERTIFIKĀTI) ==========
				Sertifikati Sert1 = new Sertifikati(KD1, kursaDatums1, LocalDate.of(2025, 2, 1), true);
				Sertifikati Sert2 = new Sertifikati(KD2, kursaDatums2, LocalDate.of(2025, 2, 16), false);
				Sertifikati Sert3 = new Sertifikati(KD3, kursaDatums3, LocalDate.of(2025, 3, 1), true);
				Sertifikati Sert4 = new Sertifikati(KD4, kursaDatums4, LocalDate.of(2025, 3, 16), false);
				Sertifikati Sert5 = new Sertifikati(KD5, kursaDatums5, LocalDate.of(2025, 4, 1), true);
				Sertifikati Sert6 = new Sertifikati(KD6, kursaDatums6, LocalDate.of(2025, 4, 16), true);
				Sertifikati Sert7 = new Sertifikati(KD7, kursaDatums1, LocalDate.of(2025, 2, 1), false);
				Sertifikati Sert8 = new Sertifikati(KD8, kursaDatums2, LocalDate.of(2025, 2, 16), true);
				Sertifikati Sert9 = new Sertifikati(KD9, kursaDatums3, LocalDate.of(2025, 3, 1), true);
				Sertifikati Sert10 = new Sertifikati(KD10, kursaDatums4, LocalDate.of(2025, 3, 16), false);
				Sertifikati Sert11 = new Sertifikati(KD11, kursaDatums5, LocalDate.of(2025, 4, 1), true);
				Sertifikati Sert12 = new Sertifikati(KD12, kursaDatums6, LocalDate.of(2025, 4, 16), true);
				Sertifikati Sert13 = new Sertifikati(KD13, kursaDatums1, LocalDate.of(2025, 2, 1), false);
				Sertifikati Sert14 = new Sertifikati(KD14, kursaDatums2, LocalDate.of(2025, 2, 16), true);
				Sertifikati Sert15 = new Sertifikati(KD15, kursaDatums3, LocalDate.of(2025, 3, 1), true);
				Sertifikati Sert16 = new Sertifikati(KD16, kursaDatums4, LocalDate.of(2025, 3, 16), false);
				Sertifikati Sert17 = new Sertifikati(KD17, kursaDatums5, LocalDate.of(2025, 4, 1), true);
				Sertifikati Sert18 = new Sertifikati(KD18, kursaDatums6, LocalDate.of(2025, 4, 16), true);
				Sertifikati Sert19 = new Sertifikati(KD19, kursaDatums1, LocalDate.of(2025, 2, 1), false);
				Sertifikati Sert20 = new Sertifikati(KD20, kursaDatums2, LocalDate.of(2025, 2, 16), true);
				Sertifikati Sert21 = new Sertifikati(KD21, kursaDatums3, LocalDate.of(2025, 3, 1), true);
				Sertifikati Sert22 = new Sertifikati(KD22, kursaDatums4, LocalDate.of(2025, 3, 16), false);
				Sertifikati Sert23 = new Sertifikati(KD23, kursaDatums5, LocalDate.of(2025, 4, 1), true);
				Sertifikati Sert24 = new Sertifikati(KD24, kursaDatums6, LocalDate.of(2025, 4, 16), true);

				sertRepo.saveAll(Arrays.asList(Sert1, Sert2, Sert3, Sert4, Sert5, Sert6, Sert7, Sert8, 
						Sert9, Sert10, Sert11, Sert12, Sert13, Sert14, Sert15, Sert16, Sert17, Sert18,
						Sert19, Sert20, Sert21, Sert22, Sert23, Sert24));
				
				// ========== VĒRTĒJUMI ==========
				Vertejums V1 = new Vertejums(kursaDatums1, KD1, "10", LocalDate.of(2025, 1, 30));
				Vertejums V2 = new Vertejums(kursaDatums2, KD2, "9", LocalDate.of(2025, 2, 15));
				Vertejums V3 = new Vertejums(kursaDatums3, KD3, "8", LocalDate.of(2025, 2, 28));
				Vertejums V4 = new Vertejums(kursaDatums4, KD4, "10", LocalDate.of(2025, 3, 15));
				Vertejums V5 = new Vertejums(kursaDatums5, KD5, "7", LocalDate.of(2025, 3, 31));
				Vertejums V6 = new Vertejums(kursaDatums6, KD6, "9", LocalDate.of(2025, 4, 15));
				Vertejums V7 = new Vertejums(kursaDatums1, KD7, "6", LocalDate.of(2025, 1, 30));
				Vertejums V8 = new Vertejums(kursaDatums2, KD8, "8", LocalDate.of(2025, 2, 15));

				VertRepo.saveAll(Arrays.asList(V1, V2, V3, V4, V5, V6, V7, V8));
				
				// ========== LIETOTĀJI ==========
				Lietotajs Liet1 = new Lietotajs("password123", "lietotajs1@example.com");
				Lietotajs Liet2 = new Lietotajs("password234", "lietotajs2@example.com");
				Lietotajs Liet3 = new Lietotajs("password345", "lietotajs3@example.com");
				Lietotajs Liet4 = new Lietotajs("password456", "lietotajs4@example.com");

				LietRepo.saveAll(Arrays.asList(Liet1, Liet2, Liet3, Liet4));
				
				// ========== SERTIFIKĀTU REĢISTRĀCIJAS TABULA ==========
				Sertifikatu_registracijas_tabula SRT1 = new Sertifikatu_registracijas_tabula(Liet1, Sert1, "2025-02-01");
				Sertifikatu_registracijas_tabula SRT2 = new Sertifikatu_registracijas_tabula(Liet2, Sert2, "2025-02-16");
				Sertifikatu_registracijas_tabula SRT3 = new Sertifikatu_registracijas_tabula(Liet3, Sert3, "2025-03-01");
				Sertifikatu_registracijas_tabula SRT4 = new Sertifikatu_registracijas_tabula(Liet4, Sert4, "2025-03-16");

				SertRegRepo.saveAll(Arrays.asList(SRT1, SRT2, SRT3, SRT4));
				
				// ========== E-PARAKSTA LOGS ==========
				EParakstaLogs log1 = new EParakstaLogs(Sert1, LocalDate.of(2025, 2, 2), "Parakstīts");
				EParakstaLogs log2 = new EParakstaLogs(Sert2, LocalDate.of(2025, 2, 17), "Neparakstīts");
				EParakstaLogs log3 = new EParakstaLogs(Sert3, LocalDate.of(2025, 3, 2), "Parakstīts");
				EParakstaLogs log4 = new EParakstaLogs(Sert4, LocalDate.of(2025, 3, 17), "Neparakstīts");
				EParakstaLogs log5 = new EParakstaLogs(Sert5, LocalDate.of(2025, 4, 2), "Parakstīts");
				EParakstaLogs log6 = new EParakstaLogs(Sert6, LocalDate.of(2025, 4, 17), "Parakstīts");
				EParakstaLogs log7 = new EParakstaLogs(Sert7, LocalDate.of(2025, 2, 2), "Neparakstīts");
				EParakstaLogs log8 = new EParakstaLogs(Sert8, LocalDate.of(2025, 2, 17), "Parakstīts");

				EPRepo.saveAll(Arrays.asList(log1, log2, log3, log4, log5, log6, log7, log8));

				// ========== AUTORIZĀCIJA ==========
				MyAuthority auth1 = new MyAuthority("USER");
				authRepo.save(auth1);
				MyAuthority auth2 = new MyAuthority("ADMIN");
				authRepo.save(auth2);



				PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
				// Admin with 2FA (kristers)
				MyUser u1 = new MyUser("kristers", encoder.encode("1234"), auth2);
				u1.setTwoFactorEnabled(false); // Will be enabled after scanning QR code
				userRepo.save(u1);
				
				// Regular user without 2FA (janis)
				MyUser u2 = new MyUser("janis", encoder.encode("4321"), auth1);
				userRepo.save(u2);
				
				// MyUser u1 = new MyUser("kristers", encoder.encode("1234"), auth2); // admin
				// userRepo.save(u1);
				// MyUser u2 = new MyUser("janis", encoder.encode("4321"), auth1); // user
				// userRepo.save(u2);
				
				
			}
		};
	}
}