package com.adburke.java1_final.json;

/**
 * Created by aaronburke on 11/7/13.
 * Java1 Week 3
 */
public enum Games {
    QUAKE("Quake", new String[] {"PC"}, "1997"),
    ZELDA("The Legend of Zelda", new String[] {"NES","GBA","WIIU"}, "1986"),
    MARIO("Super Mario Bros.", new String[] {"NES","GBA","WIIU"}, "1985");

    private final String _name;
    private final String[] _platforms;
    private final String _releaseDate;

    private Games(String name, String[] platforms, String releaseDate) {
        _name = name;
        _platforms = platforms;
        _releaseDate = releaseDate;
    }

    // Getters
    public String getName() {
        return _name;
    }
    public String[] getPlatforms() {
        return _platforms;
    }
    public String getReleaseDate() {
        return _releaseDate;
    }
}
