package edu.caltech.cs2.project04;

import edu.caltech.cs2.datastructures.ArrayDeque;
import edu.caltech.cs2.datastructures.TrieMap;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.ITrieMap;


public class TrieMovieAutoCompleter extends AbstractMovieAutoCompleter {
    private static ITrieMap<String, IDeque<String>, IDeque<String>> titles = new TrieMap<>((IDeque<String> s) -> s);

    public static void populateTitles() {
        for (String movie : idMap.keySet()) {
            String[] words = movie.split(" ");
            IDeque<String> movieParts = new ArrayDeque<>();
            for (String word : words) {
                movieParts.addBack(word.toLowerCase());
            }
            int size = movieParts.size();
            for (int i = 0; i < size; i++) {
                if (titles.get(movieParts) == null) {
                    IDeque<String> movieTitle = new ArrayDeque<>();
                    movieTitle.addBack(movie);
                    titles.put(movieParts, movieTitle);
                }
                else {
                    IDeque<String> movies = titles.get(movieParts);
                    movies.addBack(movie);

                }
                movieParts.removeFront();
            }
        }
    }

    public static IDeque<String> complete(String term) {
        IDeque<String> key = new ArrayDeque<>();
        String[] keyParts = term.toLowerCase().split(" ");
        for (String keyPart : keyParts) {
            key.addBack(keyPart);
        }

        IDeque<IDeque<String>> temp = titles.getCompletions(key);
        key.clear();
        for (IDeque<String> title : temp) {
            for (String movie : title) {
                if (!key.contains(movie)) {
                    key.addBack(movie);
                }
            }
        }
        return key;
    }
}
