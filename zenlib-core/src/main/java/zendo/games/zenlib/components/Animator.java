package zendo.games.zenlib.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import zendo.games.zenlib.assets.Content;
import zendo.games.zenlib.assets.Sprite;
import zendo.games.zenlib.ecs.Component;
import zendo.games.zenlib.utils.RectI;

public class Animator extends Component {

    public Vector2 scale;
    public float rotation;
    public float speed;

    private Color tint;
    private Sprite sprite;
    private int animationIndex;
    private int frameIndex;
    private float frameCounter;

    public Animator() {
        reset();
    }

    public Animator(String spriteName) {
        reset();
        sprite = Content.findSprite(spriteName);
        tint = new Color(1f, 1f, 1f, 1f);
    }

    public Animator(String spriteName, String animationName) {
        reset();
        sprite = Content.findSprite(spriteName);
        tint = new Color(1f, 1f, 1f, 1f);
        play(animationName);
    }

    @Override
    public void reset() {
        super.reset();
        if (scale == null) {
            scale = new Vector2();
        }
        scale.set(1, 1);
        rotation = 0;
        speed = 1;
        tint = null;
        sprite = null;
        animationIndex = 0;
        frameIndex = 0;
        frameCounter = 0;
    }

    @Override
    public <T extends Component> void copyFrom(T other) {
        super.copyFrom(other);
        if (other instanceof Animator) {
            Animator animator = (Animator) other;
            this.scale.set(animator.scale);
            this.rotation       = animator.rotation;
            this.speed          = animator.speed;
            this.tint           = animator.tint;
            this.sprite         = animator.sprite;
            this.animationIndex = animator.animationIndex;
            this.frameIndex     = animator.frameIndex;
            this.frameCounter   = animator.frameCounter;
        }
    }

    public Sprite sprite() {
        return sprite;
    }

    public Sprite.Anim animation() {
        if (sprite != null && animationIndex >= 0 && animationIndex < sprite.animations.size()) {
            return sprite.animations.get(animationIndex);
        }
        return null;
    }

    public Sprite.Frame frame() {
        Sprite.Anim anim = animation();
        return anim.frames.get(frameIndex);
    }

    public Color tint() {
        return tint;
    }

    public void setAlpha(float a) {
        tint.a = a;
    }

    public void setRGB(float r, float g, float b) {
        tint.set(r, g, b, tint.a);
    }

    public void setColor(float r, float g, float b, float a) {
        tint.set(r, g, b, a);
    }

    public void play(String animation) {
        play(animation, false);
    }

    public void play(String animation, boolean restart) {
        assert(sprite != null) : "No Sprite assigned!";

        for (int i = 0; i < sprite.animations.size(); i++) {
            if (sprite.animations.get(i).name.equals(animation)) {
                if (animationIndex != i || restart) {
                    animationIndex = i;
                    frameIndex = 0;
                    frameCounter = 0;

                    // update collider if appropriate
                    Collider collider = get(Collider.class);
                    if (collider != null && collider.shape() == Collider.Shape.rect) {
                        RectI hitbox = frame().hitbox;
                        if (hitbox != null) {
                            collider.setRect(hitbox);
                        }
                    }
                }
                break;
            }
        }
    }

    @Override
    public void update(float dt) {
        if (!inValidState()) return;

        Sprite.Anim anim = sprite.animations.get(animationIndex);
        Sprite.Frame frame = anim.frames.get(frameIndex);

        // increment frame counter
        frameCounter += speed * dt;

        // move to next frame after duration
        while (frameCounter >= frame.duration) {
            // reset frame counter
            frameCounter -= frame.duration;

            // TODO: add play modes, pingpong, reversed, etc...
            // increment frame, move back if we're at the end
            frameIndex++;
            if (frameIndex >= anim.frames.size()) {
                frameIndex = 0;
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!inValidState()) return;

        Sprite.Anim anim = sprite.animations.get(animationIndex);
        Sprite.Frame frame = anim.frames.get(frameIndex);

        batch.setColor(tint);
        batch.draw(frame.image,
                entity().position.x - sprite.origin.x,
                entity().position.y - sprite.origin.y,
                sprite.origin.x,
                sprite.origin.y,
                frame.image.getRegionWidth(),
                frame.image.getRegionHeight(),
                scale.x, scale.y,
                rotation
        );
        batch.setColor(1f, 1f, 1f, 1f);
    }

    private boolean inValidState() {
        return (sprite != null
             && animationIndex >= 0
             && animationIndex < sprite.animations.size()
             && frameIndex >= 0
             && frameIndex < sprite.animations.get(animationIndex).frames.size()
        );
    }

}
