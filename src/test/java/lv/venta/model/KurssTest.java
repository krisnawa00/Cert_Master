package lv.venta.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import lv.venta.model.enums.Limenis;

class KurssTest {

    @Test
    void testKurssConstructor_WithValidData_ShouldCreateObject() {
        // Given
        String nosaukums = "Java fundamentals";
        int stundas = 40;
        Limenis limenis = Limenis.Beginner;
        
        // When
        Kurss kurss = new Kurss(nosaukums, stundas, limenis);
        
        // Then
        assertNotNull(kurss);
        assertEquals(nosaukums, kurss.getNosaukums());
        assertEquals(stundas, kurss.getStundas());
        assertEquals(limenis, kurss.getLimenis());
    }

    @Test
    void testKurssConstructor_WithAllLimenisValues_ShouldCreateObject() {
        // Test with Beginner
        Kurss kurss1 = new Kurss("Course 1", 20, Limenis.Beginner);
        assertEquals(Limenis.Beginner, kurss1.getLimenis());
        
        // Test with Junior
        Kurss kurss2 = new Kurss("Course 2", 30, Limenis.Junior);
        assertEquals(Limenis.Junior, kurss2.getLimenis());
        
        // Test with Intermediate
        Kurss kurss3 = new Kurss("Course 3", 40, Limenis.Intermediate);
        assertEquals(Limenis.Intermediate, kurss3.getLimenis());
        
        // Test with Advanced
        Kurss kurss4 = new Kurss("Course 4", 50, Limenis.Advanced);
        assertEquals(Limenis.Advanced, kurss4.getLimenis());
        
        // Test with other
        Kurss kurss5 = new Kurss("Course 5", 60, Limenis.other);
        assertEquals(Limenis.other, kurss5.getLimenis());
    }

    @Test
    void testKurssConstructor_WithMinimumStundas_ShouldCreateObject() {
        // Given
        String nosaukums = "Short Course";
        int stundas = 1;
        Limenis limenis = Limenis.Beginner;
        
        // When
        Kurss kurss = new Kurss(nosaukums, stundas, limenis);
        
        // Then
        assertEquals(1, kurss.getStundas());
    }

    @Test
    void testKurssConstructor_WithMaximumStundas_ShouldCreateObject() {
        // Given
        String nosaukums = "Long Course";
        int stundas = 100;
        Limenis limenis = Limenis.Advanced;
        
        // When
        Kurss kurss = new Kurss(nosaukums, stundas, limenis);
        
        // Then
        assertEquals(100, kurss.getStundas());
    }

    @Test
    void testKurssConstructor_WithLatvianCharacters_ShouldCreateObject() {
        // Given
        String nosaukums = "Programmēšanas pamati";
        int stundas = 35;
        Limenis limenis = Limenis.Beginner;
        
        // When
        Kurss kurss = new Kurss(nosaukums, stundas, limenis);
        
        // Then
        assertEquals("Programmēšanas pamati", kurss.getNosaukums());
    }

    @Test
    void testKurssConstructor_WithDifferentStundasValues_ShouldCreateObject() {
        // Test various hour values
        Kurss kurss1 = new Kurss("Course A", 15, Limenis.Beginner);
        assertEquals(15, kurss1.getStundas());
        
        Kurss kurss2 = new Kurss("Course B", 45, Limenis.Intermediate);
        assertEquals(45, kurss2.getStundas());
        
        Kurss kurss3 = new Kurss("Course C", 80, Limenis.Advanced);
        assertEquals(80, kurss3.getStundas());
    }

    @Test
    void testKurssNoArgsConstructor_ShouldCreateEmptyObject() {
        // When
        Kurss kurss = new Kurss();
        
        // Then
        assertNotNull(kurss);
        assertNull(kurss.getNosaukums());
        assertEquals(0, kurss.getStundas());
        assertNull(kurss.getLimenis());
    }

    @Test
    void testKurssBuilder_ShouldWorkCorrectly() {
        // Given & When
        Kurss kurss = Kurss.builder()
                .nosaukums("Spring Framework")
                .stundas(60)
                .līmenis(Limenis.Intermediate)
                .build();
        
        // Then
        assertEquals("Spring Framework", kurss.getNosaukums());
        assertEquals(60, kurss.getStundas());
        assertEquals(Limenis.Intermediate, kurss.getLimenis());
    }

    @Test
    void testKurssSetters_ShouldModifyFields() {
        // Given
        Kurss kurss = new Kurss();
        
        // When
        kurss.setNosaukums("React fundamentals");
        kurss.setStundas(50);
        kurss.setLimenis(Limenis.other);
        
        // Then
        assertEquals("React fundamentals", kurss.getNosaukums());
        assertEquals(50, kurss.getStundas());
        assertEquals(Limenis.other, kurss.getLimenis());
    }

    @Test
    void testKurssToString_ShouldContainFieldValues() {
        // Given
        Kurss kurss = new Kurss("Test Course", 25, Limenis.Junior);
        
        // When
        String toString = kurss.toString();
        
        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("Test Course"));
        assertTrue(toString.contains("25"));
        assertTrue(toString.contains("Junior"));
    }

    @Test
    void testKurssConstructor_WithEmptyString_ShouldCreateObject() {
        // Given
        String nosaukums = "";
        int stundas = 20;
        Limenis limenis = Limenis.Beginner;
        
        // When
        Kurss kurss = new Kurss(nosaukums, stundas, limenis);
        
        // Then
        assertEquals("", kurss.getNosaukums());
    }

    @Test
    void testKurssConstructor_WithLongCourseName_ShouldCreateObject() {
        // Given
        String nosaukums = "Advanced Object-Oriented Programming with Design Patterns and Best Practices";
        int stundas = 75;
        Limenis limenis = Limenis.Advanced;
        
        // When
        Kurss kurss = new Kurss(nosaukums, stundas, limenis);
        
        // Then
        assertEquals(nosaukums, kurss.getNosaukums());
    }
}