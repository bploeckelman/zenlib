package zendo.games.zenlib.assets;

import com.badlogic.gdx.utils.OrderedMap;
import zendo.games.zenlib.utils.Point;
import zendo.games.zenlib.utils.RectI;

/**
 * Data required to create a Sprite from an Aseprite file and packed textures

 * TODO:
 * - add saveJson to write sprite info during the AsepritePacker process
 * - add loadJson to read sprite info during game loading process (maybe as static method in Content?)
 */
public class SpriteInfo {
    public String path;
    public String name;
    public Point slice_pivot;
    public OrderedMap<String, AnimFrameInfo[]> anim_frame_infos;

    public static class AnimFrameInfo {
        public String region_name;
        public RectI hitbox;
        public int region_index;
        public float duration;

        public AnimFrameInfo() {
            region_name = null;
            hitbox = null;
            region_index = -1;
            duration = 0f;
        }
    }

    public SpriteInfo() {
        path = null;
        name = null;
        slice_pivot = new Point();
        anim_frame_infos = new OrderedMap<>();
    }
}
