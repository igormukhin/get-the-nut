package de.igormukhin.getthenut;

import static java.util.Objects.requireNonNull;

public class Land {

    private final SpotType[][] spots;
    private final int height;
    private final int width;

    public Land(SpotType[][] spots) {
        this.spots = requireNonNull(spots);
        this.height = spots.length;
        this.width = spots[0].length;
    }

    public int height() {
        return height;
    }

    public int width() {
        return width;
    }

    public SpotType spotTypeAt(Pos pos) {
        return spots[pos.row()][pos.col()];
    }

    public boolean inBounds(Pos pos) {
        return pos.row() >= 0 && pos.row() < height
                && pos.col() >= 0 && pos.col() < width;
    }
}
