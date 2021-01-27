package zendo.games.zenlib.assets;

import com.badlogic.gdx.utils.Array;

public abstract class Content {

    protected static Array<Sprite> sprites = new Array<>();

    public static void load() {
        // override to load sprites and other assets
    }

    public static void unload() {
        sprites.clear();
    }

    public static Sprite findSprite(String name) {
        for (Sprite sprite : sprites) {
            if (sprite.name.equals(name)) {
                return sprite;
            }
        }
        return null;
    }

}
