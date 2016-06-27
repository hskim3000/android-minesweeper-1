package funkyapps.minesweeper;

import android.util.Log;
import java.util.Random;

/**
 * Structure representing the grid formed data used by the game,
 * including tile image calculations and mine randomization
 */
public class GameGrid {

    // constants representing each type of tile image
    public enum TileImageType {
        TILE_UNREVEALED,
        TILE_BLANK,
        TILE_MINE,
        TILE_COUNT_1,
        TILE_COUNT_2,
        TILE_COUNT_3,
        TILE_COUNT_4,
        TILE_COUNT_5,
        TILE_COUNT_6,
        TILE_COUNT_7,
        TILE_COUNT_8
    };

    // used in logging
    final static String TAG = GameGrid.class.getSimpleName();


    // number of tiles in each direction
    int mSize;

    // 2d grid of tiles
    GameTile mGrid[][];



    /**
     * Creates a new grid of tiles, sets up a map for easy access to source rectangles
     * for each tile type, and randomize mine placement on the board.
     *
     * @param size number of tiles in each direction
     */
    public GameGrid(int size) {
        mSize = size;
        mGrid = new GameTile[size][size];

        for(int y = 0 ; y < size ; y++) {
            for(int x = 0 ; x < size ; x++) {
                mGrid[y][x] = new GameTile();
            }
        }

        randomize();
    }



    /**
     * Place mines randomly
     * For simplicity the number of mines to be placed is equal to the number of tiles in one
     * axis (10 mines on a 10x10 grid)
     */
    protected void randomize() {

        int numMinesPlaced = 0;
        int numMinesTotal = mSize;
        Random random = new Random();

        // place the mines
        while(numMinesPlaced < numMinesTotal) {

            int x = random.nextInt(mSize);
            int y = random.nextInt(mSize);

            if(!isMine(x, y)) {
                placeMine(x, y);
                numMinesPlaced++;
            }
        }

        // calculate sum of mines surrounding a tile
        for(int y = 0 ; y < mSize ; y++) {
            for(int x = 0 ; x < mSize ; x++) {
                setMineCount(x, y);
            }
        }


        // DEBUG PRINT PLAYING FIELD - remove in final version if you don't like to cheat
        StringBuilder sb = new StringBuilder();
        Log.d(TAG, "Game grid:");
        for(int y = 0 ; y < mSize ; y++) {
            for(int x = 0 ; x < mSize ; x++) {
                sb.append(" " + (isMine(x, y) ? "*" : mGrid[y][x].count > 0 ? mGrid[y][x].count : " ") + " ");
            }
            Log.d(TAG, "[" + sb.toString() + "]");
            sb = new StringBuilder();
        }
    }


    /**
     * Returns whether the given coordinates correspond to a mine or not
     * @param x int tile x-value
     * @param y int tile y-value
     * @return Boolean whether it is a mine or not
     */
    public boolean isMine(int x, int y) {
        return mGrid[y][x].isMine;
    }

    /**
     * Sets a given tile as a mine
     * @param x int tile x-value
     * @param y int tile y-value
     */
    protected void placeMine(int x, int y) {
        mGrid[y][x].isMine = true;
    }


    /**
     * Handy helper to tell if a x,y pair is valid in the grid
     * @param x int tile x-value
     * @param y int tile y-value
     * @return Boolean whether the pair is valid or not
     */
    protected boolean isValidXY(int x, int y) {
        return (x >= 0 && y >= 0 && x < mSize && y < mSize);
    }

    /**
     * Calculates number of neighboring mines to the given x,y pair
     * @param centerX int tile x-value
     * @param centerY int tile y-value
     */
    protected void setMineCount(int centerX, int centerY) {

        if(!isValidXY(centerX, centerY)) {
            return;
        }

        int count = 0;
        for(int y = centerY - 1 ; y <= centerY + 1 ; y++) {
            for(int x = centerX - 1 ; x <= centerX + 1 ; x++) {
                if(x == centerX && y == centerY) {
                    // do skip own tile
                    continue;
                }

                if(isValidXY(x, y) && mGrid[y][x].isMine) {
                    count++;
                }
            }
        }

        mGrid[centerY][centerX].count = count;
    }

    /**
     * Helper method that returns the type of the image to be displayed
     * for a given tile
     * @param x int tile x-value
     * @param y int tile y-value
     * @return TileImageType the image type
     */
    protected TileImageType getTileImageType(int x, int y) {

        GameTile tile = mGrid[y][x];

        if(!tile.isRevealed) {
            return TileImageType.TILE_UNREVEALED;
        }

        if (tile.isMine) {
            return TileImageType.TILE_MINE;
        }

        switch(tile.count) {
            case 1: return TileImageType.TILE_COUNT_1;
            case 2: return TileImageType.TILE_COUNT_2;
            case 3: return TileImageType.TILE_COUNT_3;
            case 4: return TileImageType.TILE_COUNT_4;
            case 5: return TileImageType.TILE_COUNT_5;
            case 6: return TileImageType.TILE_COUNT_6;
            case 7: return TileImageType.TILE_COUNT_7;
            case 8: return TileImageType.TILE_COUNT_8;
            default: return TileImageType.TILE_BLANK;
        }
    }


}