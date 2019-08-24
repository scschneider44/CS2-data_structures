package edu.caltech.cs2.project04;

import edu.caltech.cs2.datastructures.ArrayDeque;
import edu.caltech.cs2.datastructures.LinkedDeque;
import edu.caltech.cs2.interfaces.IDeque;

import java.util.HashMap;
import java.util.Map;

public class HashMovieAutoCompleter extends AbstractMovieAutoCompleter {
    private static Map<String, IDeque<String>> titles = new HashMap<>();

    public static void populateTitles() {
        //populates titles with suffixes of the movies
        IDeque<String> temp = new ArrayDeque<>();
        for (Map.Entry<String, String> entry : idMap.entrySet()) {
            IDeque<String> suffixes = new ArrayDeque<>();
            String movie = entry.getKey();
            String movieLower = movie.toLowerCase();
            String[] words = movieLower.split(" ");
            for (String word: words) {
                temp.addBack(word);
            }
            while (temp.size() > 0) {
                suffixes.addBack(temp.toString());
                temp.removeFront();
            }
            // put suffixes in a linked deque
            titles.put(movie, suffixes);
        }
    }

    public static IDeque<String> complete(String term) {
        // check if term is a prefix to titles in map and return linked deque of completed movie titles
        IDeque<String> possible = new ArrayDeque<>();
        for (Map.Entry<String, IDeque<String>> entry : titles.entrySet()) {
            for (String phrase : entry.getValue()) {
                if (entry.getValue().contains(term.toLowerCase() + " " + phrase) ||
                        entry.getValue().contains(term.toLowerCase())) {
                    if (!possible.contains(entry.getKey())) {
                        possible.addBack(entry.getKey());
                    }
                }
            }

        }
        return possible;
    }
}