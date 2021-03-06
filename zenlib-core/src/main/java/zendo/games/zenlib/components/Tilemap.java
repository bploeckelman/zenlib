package zendo.games.zenlib.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import zendo.games.zenlib.ecs.Component;
import zendo.games.zenlib.utils.Point;

public class Tilemap extends Component {

    private int tileSize;
    private int columns;
    private int rows;
    protected TextureRegion[] grid;

    public Point origin = Point.zero();

    public Tilemap() {
        reset();
    }

    public Tilemap(int tileSize, int columns, int rows) {
        init(tileSize, columns, rows);
    }

    public void init(int tileSize, int columns, int rows) {
        this.tileSize = tileSize;
        this.columns  = columns;
        this.rows     = rows;
        this.grid     = new TextureRegion[columns * rows];
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getNumRows() {
        return rows;
    }

    public int getNumCols() {
        return columns;
    }

    @Override
    public <T extends Component> void copyFrom(T other) {
        super.copyFrom(other);
        if (other instanceof Tilemap) {
            Tilemap tilemap = (Tilemap) other;
            this.tileSize = tilemap.tileSize;
            this.columns  = tilemap.columns;
            this.rows     = tilemap.rows;
            this.origin   = tilemap.origin;
            this.grid     = tilemap.grid;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                if (grid[x + y * columns] != null) {
                    batch.draw(grid[x + y * columns],
                            origin.x + x * tileSize + entity().position.x,
                            origin.y + y * tileSize + entity().position.y,
                            tileSize, tileSize);
                }
            }
        }
    }

    public void setCell(int x, int y, TextureRegion texture) {
        assert(x >= 0 && y >= 0 && x < columns && y < rows) : "Tilemap indices out of bounds";
        grid[x + y * columns] = texture;
    }

    public void setCells(int x, int y, int w, int h, TextureRegion texture) {
        assert(x >= 0 && y >= 0 && x + w < columns && y + h < rows) : "Tilemap indices out of bounds";
        for (int ix = x; ix < x + w; ix++) {
            for (int iy = y; iy < y + h; iy++) {
                grid[ix + iy * columns] = texture;
            }
        }
    }

}
