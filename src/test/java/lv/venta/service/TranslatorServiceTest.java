package lv.venta.service;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.test.util.ReflectionTestUtils;

import com.deepl.api.DeepLClient;
import com.deepl.api.TextResult;

/**
 * One parameterized test that gives strong coverage for translateBatch():
 * - targetLang == null
 * - targetLang == "lv"
 * - translator == null
 * - texts == null
 * - texts is empty
 * - success path (maps TextResult -> String)
 * - exception path (DeepL throws -> returns original texts)
 */
class TranslatorServiceTranslateBatchParameterizedTest {

    private TranslatorService service;

    @BeforeEach
    void setUp() {
        service = new TranslatorService();
    }

    static Stream<Case> translateBatch_cases() {
        return Stream.of(
                // Guard clauses
                Case.guard("targetLang null", List.of("a", "b"), null),
                Case.guard("targetLang lv", List.of("a", "b"), "lv"),
                Case.guard("translator null", List.of("a", "b"), "en"),
                Case.guard("texts null", null, "en"),
                Case.guard("texts empty", List.of(), "en"),

                // Success path
                Case.success("success translate", List.of("hello", "world"), "en",
                        List.of("HELLO", "WORLD")),

                // Exception path
                Case.deepLThrows("deepl throws", List.of("x"), "en")
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("translateBatch_cases")
    void translateBatch_parameterized_fullCoverage(Case c) throws Exception {
        // Arrange
        if (c.behavior == Behavior.SUCCESS) {
            DeepLClient translatorMock = mock(DeepLClient.class);
            ReflectionTestUtils.setField(service, "translator", translatorMock);

            TextResult r1 = mock(TextResult.class);
            when(r1.getText()).thenReturn(c.translated.get(0));

            if (c.translated.size() == 1) {
                when(translatorMock.translateText(eq(c.texts), isNull(), eq(c.targetLang.toUpperCase())))
                        .thenReturn(List.of(r1));
            } else {
                TextResult r2 = mock(TextResult.class);
                when(r2.getText()).thenReturn(c.translated.get(1));

                when(translatorMock.translateText(eq(c.texts), isNull(), eq(c.targetLang.toUpperCase())))
                        .thenReturn(List.of(r1, r2));
            }
        } else if (c.behavior == Behavior.THROW) {
            DeepLClient translatorMock = mock(DeepLClient.class);
            ReflectionTestUtils.setField(service, "translator", translatorMock);

            when(translatorMock.translateText(anyList(), any(), anyString()))
                    .thenThrow(new RuntimeException("fail"));
        }
        // else GUARD: leave translator null (default)

        // Act
        List<String> out = service.translateBatch(c.texts, c.targetLang);

        // Assert
        switch (c.behavior) {
            case GUARD -> assertEquals(c.expectedGuardReturn, out);
            case SUCCESS -> {
                assertEquals(c.translated, out);
                DeepLClient translatorMock = (DeepLClient) ReflectionTestUtils.getField(service, "translator");
                verify(translatorMock, times(1))
                        .translateText(eq(c.texts), isNull(), eq(c.targetLang.toUpperCase()));
            }
            case THROW -> assertEquals(c.texts, out);
        }
    }

    enum Behavior { GUARD, SUCCESS, THROW }

    static class Case {
        final String name;
        final List<String> texts;
        final String targetLang;
        final Behavior behavior;

        // for GUARD
        final List<String> expectedGuardReturn;

        // for SUCCESS
        final List<String> translated;

        private Case(String name,
                     List<String> texts,
                     String targetLang,
                     Behavior behavior,
                     List<String> expectedGuardReturn,
                     List<String> translated) {
            this.name = name;
            this.texts = texts;
            this.targetLang = targetLang;
            this.behavior = behavior;
            this.expectedGuardReturn = expectedGuardReturn;
            this.translated = translated;
        }

        static Case guard(String name, List<String> texts, String targetLang) {
            // guard returns exactly "texts" (may be null or empty)
            return new Case(name, texts, targetLang, Behavior.GUARD, texts, null);
        }

        static Case success(String name, List<String> texts, String targetLang, List<String> translated) {
            return new Case(name, texts, targetLang, Behavior.SUCCESS, null, translated);
        }

        static Case deepLThrows(String name, List<String> texts, String targetLang) {
            return new Case(name, texts, targetLang, Behavior.THROW, null, null);
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
