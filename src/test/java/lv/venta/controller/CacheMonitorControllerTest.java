package lv.venta.controller;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.cache.CacheManager;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

class CacheMonitorControllerTest {

    @Test
    void getCacheStatus_populatesModelAndReturnsView() {
        // Arrange (no Spring context)
        CacheManager cacheManager = mock(CacheManager.class);
        when(cacheManager.getCacheNames()).thenReturn(List.of("usersCache", "coursesCache"));

        CacheMonitorController controller = new CacheMonitorController();
        ReflectionTestUtils.setField(controller, "cacheManager", cacheManager);

        Model model = new ExtendedModelMap();

        // Act
        String view = controller.getCacheStatus(model);

        // Assert: return
        assertEquals("cache-status-page", view);

        // Assert: model attributes
        Object cachesObj = model.getAttribute("caches");
        assertNotNull(cachesObj);
        assertTrue(cachesObj instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, String> caches = (Map<String, String>) cachesObj;

        assertEquals(2, caches.size());
        assertEquals("Active", caches.get("usersCache"));
        assertEquals("Active", caches.get("coursesCache"));
        assertEquals(2, model.getAttribute("totalCaches"));

        // Verify interaction
        verify(cacheManager, times(1)).getCacheNames();
    }
}
