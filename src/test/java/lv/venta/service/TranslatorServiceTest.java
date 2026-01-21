package lv.venta.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.test.util.ReflectionTestUtils;

import com.deepl.api.DeepLClient;
import com.deepl.api.TextResult;

/**
 * Coverage for:
 * - translateBatch() guard/success/catch (parameterized)
 * - translateText() early return + normal path
 * - getAvailableLanguages() LinkedHashMap puts + return
 * - init() (deeplApiKey null/empty branch + catch branch)
 */
class TranslatorServiceTranslateTest {

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

    // -------- Added coverage for translateText() --------

    @Test
    void translateText_earlyReturns_whenNullBlank_orTargetLangLv() {
        assertNull(service.translateText(null, "en"));
        assertEquals("", service.translateText("", "en"));
        assertEquals("   ", service.translateText("   ", "en"));
        assertEquals("abc", service.translateText("abc", "lv")); // hits: targetLang.equals("lv")
    }

    @Test
    void translateText_callsBatchAndReturnsFirstElement() throws Exception {
        DeepLClient translatorMock = mock(DeepLClient.class);
        ReflectionTestUtils.setField(service, "translator", translatorMock);

        TextResult r1 = mock(TextResult.class);
        when(r1.getText()).thenReturn("Sveiki");

        when(translatorMock.translateText(eq(List.of("Hello")), isNull(), eq("EN")))
                .thenReturn(List.of(r1));

        String out = service.translateText("Hello", "en");

        assertEquals("Sveiki", out);
        verify(translatorMock, times(1)).translateText(eq(List.of("Hello")), isNull(), eq("EN"));
    }

    // -------- Added coverage for getAvailableLanguages() --------

    @Test
    void getAvailableLanguages_buildsAndReturnsLinkedHashMap() {
        Map<String, String> langs = service.getAvailableLanguages();

        // ensures code path executed: new LinkedHashMap + all puts + return
        assertEquals(7, langs.size());
        assertEquals("Latviešu", langs.get("LV"));
        assertEquals("English (British)", langs.get("EN-GB"));
        assertEquals("English (American)", langs.get("EN-US"));
        assertEquals("Deutsch", langs.get("DE"));
        assertEquals("Русский", langs.get("RU"));
        assertEquals("Español", langs.get("ES"));
        assertEquals("Français", langs.get("FR"));

        // optional: ensure LinkedHashMap (keeps insertion order)
        assertTrue(langs instanceof LinkedHashMap);
    }

    // -------- Added coverage for init() null/empty + catch --------

    @Test
    void init_returnsEarly_whenApiKeyNullOrEmpty() {
        ReflectionTestUtils.setField(service, "deeplApiKey", null);
        service.init(); // hits: deeplApiKey == null -> return
        assertNull(ReflectionTestUtils.getField(service, "translator"));

        ReflectionTestUtils.setField(service, "deeplApiKey", "");
        service.init(); // hits: isEmpty -> return
        assertNull(ReflectionTestUtils.getField(service, "translator"));
    }

    @Test
    void init_hitsCatch_whenDeepLClientConstructionThrows() {
        ReflectionTestUtils.setField(service, "deeplApiKey", "any-non-empty");

        // Requires Mockito inline mock maker (dependency: org.mockito:mockito-inline)
        // This forces an exception during "new DeepLClient(...)" so the catch block executes.
        try (var mocked = mockConstruction(DeepLClient.class, (mock, context) -> {
            throw new RuntimeException("boom");
        })) {
            service.init(); // executes catch in init()
        }

        // translator should remain null because construction failed
        assertNull(ReflectionTestUtils.getField(service, "translator"));
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
