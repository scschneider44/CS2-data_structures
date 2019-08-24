package edu.caltech.cs2.lab01;


import edu.caltech.cs2.lab01.libraries.Wikipedia;

public class WikipediaPage {
    // create the attributes
    private String title;
    private boolean followRedirects;
    private String text;

    public WikipediaPage(String title, boolean followRedirects) {
        this.title = title;
        this.followRedirects = followRedirects;
        this.text = Wikipedia.getPageText(title);
        // if there is a redirect and follow redirects is true then redirect
        if (this.isRedirect() && followRedirects) {
            this.text = Wikipedia.getPageText(Wikipedia.parseMarkup(this.text));
        }

    }

    public WikipediaPage(String title) {
        this(title, true);
    }

    private static String convertToTitleCase(String s) {
        // split title into array of words
        String[] words = s.split(" ");
        int numWords = words.length;
        String finalTitle = "";
        for (int i = 0; i < numWords; i++) {
            // Add the first letter and capitalize it
            finalTitle += words[i].substring(0, 1).toUpperCase();
            // Add the rest of the word lowercase
            finalTitle += words[i].substring(1, words[i].length()).toLowerCase();
            // add a space
            finalTitle += " ";
        }
        // strip the final space away
        finalTitle = finalTitle.strip();
        return finalTitle;
    }

    public String getTitle() {
        String spacedTitle = this.title.replace("_", " ");
        return convertToTitleCase(spacedTitle);
    }

    public String getText() {
        return this.text;
    }

    public boolean isRedirect() {
        if (this.text != null) {
            String pageText = this.text.toLowerCase();
            if (pageText.startsWith("#redirect")) {
                return true;
            }
        }
        return false;
    }

    public boolean isValid() {
        if (Wikipedia.getPageText(this.title) == null) {
            return false;
        }
        return true;
    }

    public boolean isGalaxy() {
        if (this.text == null) {
            return false;
        } else if (this.text.toLowerCase().contains("infobox galaxy")) {
            return true;
        }
        System.out.println(Wikipedia.parseMarkup(Wikipedia.getPageText("Sunflower_Galaxy")));
        return false;
    }

    public boolean hasNextLink() {
        if (this.nextLink() == null) {
            return false;
        }
        return true;
    }

    public String nextLink() {
        WikipediaPage w = new WikipediaPage(this.title, this.followRedirects);
        int linkIndexStart = 0;
        int linkIndexEnd = 0;
        linkIndexStart = w.getText().indexOf("[[");
        linkIndexEnd = w.getText().indexOf("]]");
        if (linkIndexStart == -1 || linkIndexEnd == -1) {
            return null;
        }
        if (Wikipedia.isSpecialLink(w.getText().substring(linkIndexStart, linkIndexEnd))) {
            return null;
        }
        return Wikipedia.parseMarkup(w.getText().substring(linkIndexStart, linkIndexEnd));
    }
}