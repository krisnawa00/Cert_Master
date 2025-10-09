package lv.venta.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.deepl.api.DeepLClient;
import com.deepl.api.TextResult;

import jakarta.annotation.PostConstruct;


@Service
public class TranslatorService {

    private DeepLClient  translator;
    
    
    @Value("${deepl.api.key}")
    private String deeplApiKey;
    
    @PostConstruct
    public void init(){

        try{
            if (deeplApiKey == null || deeplApiKey.isEmpty() || deeplApiKey.equals("1ea2f080-91ab-49a6-8fe6-abb2844c6043:fx")) {
                System.err.println("DeepL API key is not set. Please set the deeplApiKey variable.");
                return;
            }
            translator = new DeepLClient(deeplApiKey);
            System.out.println("DeepL Translator initialized successfully");
        } catch (Exception e) {
            System.err.println("Failed to initialize DeepL translator: " + e.getMessage());
        }
    }
    
    

    public List<String> translateBatch(List<String> texts, String targetLang)
    {
        if(targetLang == null || targetLang.equals("lv") || translator == null || texts == null || texts.isEmpty()){
            return texts;
        }
        
        try{
            List<TextResult> results = translator.translateText(texts, null, targetLang.toUpperCase());
             List<String> translated = new ArrayList<>();
             for (TextResult result : results){
                translated.add(result.getText());
             }
             return translated;
        }catch (Exception e) {
            System.err.println("Translation failed: " + e.getMessage());
            return texts;
        }
    }



    public Map<String, String> getAvailableLanguages(){
        Map<String, String> langs = new LinkedHashMap<>();
        langs.put("LV", "Latviešu");
        langs.put("EN-GB", "English (British)");
        langs.put("EN-US", "English (American)");
        langs.put("DE", "Deutsch");
        langs.put("RU", "Русский");
        langs.put("ES", "Español");
        langs.put("FR", "Français");
        return langs;




    }


    
}
