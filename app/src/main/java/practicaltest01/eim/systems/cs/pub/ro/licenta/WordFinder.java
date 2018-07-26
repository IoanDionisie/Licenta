package practicaltest01.eim.systems.cs.pub.ro.licenta;

import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.Format.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.nio.charset.Charset;
import java.text.Format.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


// Gaseste toate cuvintele care incep cu 2 litere (word)
public class WordFinder {

    private String twoLetters;

    public WordFinder(String twoLetters) {
        this.twoLetters  = twoLetters;
    }

    public List findWord() throws IOException {

        String wordLink = "http://dex.anidescoala.ro/care-incep-cu/" + twoLetters;
        String wordLink2 = "http://dex.anidescoala.ro/care-incep-cu/" + twoLetters + "/2";
        String wordLink3 = "http://dex.anidescoala.ro/care-incep-cu/" + twoLetters + "/3";

        String foundWord = null;
        Document doc = Jsoup.connect(wordLink).get();
        Elements words = doc.select(".spacer .result strong:first-of-type a");

        String wordsList = words.text();
        List<String> list = new ArrayList<String>(Arrays.asList(wordsList.split(" ")));

        doc = Jsoup.connect(wordLink2).get();
        words = doc.select(".spacer .result strong:first-of-type a");

        if (words != null) {
            wordsList = words.text();
            List<String> list2 = new ArrayList<String>(Arrays.asList(wordsList.split(" ")));
            list.addAll(list2);
        }

        doc = Jsoup.connect(wordLink3).get();
        words = doc.select(".spacer .result strong:first-of-type a");

        if (words != null) {
            wordsList = words.text();
            List<String> list3 = new ArrayList<String>(Arrays.asList(wordsList.split(" ")));
            list.addAll(list3);
        }

        return list;
    }

    public String pickToContinue(List list) {

        if (list.size() > 3) {
            int max = list.size() - 1;
            Random rand = new Random();
            int listIndex = rand.nextInt(max);

            String enemyWord = list.get(listIndex).toString();
            enemyWord = enemyWord.toLowerCase();
            if (enemyWord.length() >= 2)
                return enemyWord;
            else
                return pickToContinue(list);
        } else {
            return "notFound";
        }
    }

    public String pickToWin(List list) {
        if (list.size() > 3) {
            int pickIndex = -1;
            long seed = System.nanoTime();
            Collections.shuffle(list, new Random(seed));

            String [] suff = {"nt", "ns", "rb", "rt", "lf"};
            ArrayList<String> suffixes = new ArrayList<String>(Arrays.asList(suff));
            Collections.shuffle(suffixes);

            for ( int i = 0; i < list.size() - 1; ++i) {
                if (list.get(i).toString().endsWith(suffixes.get(0)) ||
                        list.get(i).toString().endsWith(suffixes.get(1)) ||
                        list.get(i).toString().endsWith(suffixes.get(2)) ||
                        list.get(i).toString().endsWith(suffixes.get(3)) ||
                        list.get(i).toString().endsWith(suffixes.get(4))) {
                    pickIndex = i;
                    break;
                }
            }

            if (pickIndex != -1) {
                String enemyWord = list.get(pickIndex).toString();
                enemyWord = enemyWord.toLowerCase();
                return enemyWord;
            } else {
                Random rand = new Random();
                int listIndex = rand.nextInt(list.size() - 1);
                String enemyWord = list.get(listIndex).toString();
                enemyWord = enemyWord.toLowerCase();
                return enemyWord;
            }
        } else {
            return "notFound";
        }
    }

}