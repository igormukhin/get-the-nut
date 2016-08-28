package de.igormukhin.getthenut;

public enum Direction {
    NORTH(-1, 0),
    EAST(0, 1),
    SOUTH(1, 0),
    WEST(0, -1);

    private final int yAxisDelta;
    private final int xAxisDelta;

    Direction(int yAxisDelta, int xAxisDelta) {
        this.yAxisDelta = yAxisDelta;
        this.xAxisDelta = xAxisDelta;
    }

    public Pos moveFrom(Pos from) {
        return Pos.of(from.row() + yAxisDelta, from.col() + xAxisDelta);
    }
}
