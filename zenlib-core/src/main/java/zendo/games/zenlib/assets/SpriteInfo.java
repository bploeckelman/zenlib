package zendo.games.zenlib.assets;

import zendo.games.zenlib.utils.Point;
import zendo.games.zenlib.utils.RectI;

import java.util.Map;

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
    public Map<String, AnimFrameInfo[]> anim_frame_infos;

    public static class AnimFrameInfo {
        public String region_name;
        public RectI hitbox = null;
        public int region_index;
        public float duration;
    }
}