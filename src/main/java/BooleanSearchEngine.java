import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class BooleanSearchEngine implements SearchEngine {
    private Map <String, List<PageEntry>> index;

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        index = new HashMap<>();
        File [] files = pdfsDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            var doc = new PdfDocument(new PdfReader(files[i]));
            for (int k = 1; k <doc.getNumberOfPages()+1 ; k++) {
                String text = PdfTextExtractor.getTextFromPage(doc.getPage(k));
                String [] words  = text.split("\\P{IsAlphabetic}+");

                Map<String, Integer> freqs = new HashMap<>(); // мапа, где ключом будет слово, а значением - частота
                for (var word : words) { // перебираем слова
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                }
                for (var word : words) { // перебираем слова
                    word = word.toLowerCase();
                    if (word.isEmpty()) {
                        continue;
                    }
                    if (index.containsKey(word)){
                        boolean first = true;
                        for (int j = 0; j < index.get(word).size(); j++) {
                            if (index.get(word).get(j).getPdfName().equals(files[i].getName()) && index.get(word).get(j).getPage() == k) {
                                first = false;
                            }
                        }
                        if (first){
                            index.get(word).add(new PageEntry(files[i].getName(),k,freqs.get(word)));
                        }
                    } else {
                        List <PageEntry> values = new ArrayList<>();
                        values.add(new PageEntry(files[i].getName(),k,freqs.get(word)));
                        index.put(word,values);
                    }
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
       return index.get(word);
    }
}
