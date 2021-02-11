package com.example.lo.myapplication;

import android.content.SearchRecentSuggestionsProvider;

public class ProvedorSugestao extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.example.lo.myapplication.ProvedorSugestao";
    public final static int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;

    public ProvedorSugestao() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
