package lv.venta.service;

import java.util.ArrayList;
import java.util.List;

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
            if (deeplApiKey == null || deeplApiKey.isEmpty() || deeplApiKey.equals("your_deepl_api_key_here")) {
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



    
}
