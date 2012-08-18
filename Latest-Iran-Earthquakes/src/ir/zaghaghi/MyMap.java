package ir.zaghaghi;

import de.fhpotsdam.unfolding.events.MapEvent;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;

/**
 *
 * @author Hamed Zaghaghi
 */
public class MyMap extends de.fhpotsdam.unfolding.Map {

    float hitX;
    float hitY;
    float hitWidth;
    float hitHeight;

    public MyMap(processing.core.PApplet p) {
        super(p);
    }

    public MyMap(processing.core.PApplet p, AbstractMapProvider provider) {
        super(p, provider);
    }

    public MyMap(processing.core.PApplet p, float x, float y, float width, float height) {
        super(p, x, y, width, height);
    }

    public MyMap(processing.core.PApplet p, float x, float y, float width, float height, AbstractMapProvider provider) {
        super(p, x, y, width, height, provider);
    }

    public MyMap(processing.core.PApplet p, java.lang.String id) {
        super(p, id);
    }

    public MyMap(processing.core.PApplet p, java.lang.String id, float x, float y, float width, float height) {
        super(p, id, x, y, width, height);
    }

    public MyMap(processing.core.PApplet p, java.lang.String id, float x, float y, float width, float height, boolean useDistortion) {
        super(p, id, x, y, width, height, useDistortion);
    }

    public MyMap(processing.core.PApplet p, java.lang.String id, float x, float y, float width, float height, boolean useMask, boolean useDistortion, AbstractMapProvider provider) {
        super(p, id, x, y, width, height, useMask, useDistortion, provider);
    }

    public void setupHitRect(float hitX, float hitY, float hitWidth, float hitHeight) {
        this.hitX = hitX;
        this.hitY = hitY;
        this.hitWidth = hitWidth;
        this.hitHeight = hitHeight;
    }

    @Override
    public boolean isHit(float checkX, float checkY) {
        return (checkX > hitX && checkX < hitX + hitWidth
                && checkY > hitY && checkY < hitY + hitHeight);
    }

    @Override
    public void onManipulation(MapEvent e) {
        super.onManipulation(e);
    }
}
