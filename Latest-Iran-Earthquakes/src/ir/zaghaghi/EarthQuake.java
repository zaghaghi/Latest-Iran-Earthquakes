package ir.zaghaghi;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.select.Elements;
import processing.core.PApplet;

/**
 *
 * @author Hamed Zaghaghi
 */
class EarthQuake {

    public float latitude;
    public float longitude;
    public String date;
    public String time;
    public float mag;
    public float depth;
    public String location;
    public boolean valid = false;
    public static final float maxMag = 10;
    public static final float minMag = 0;
    public static final float minDepth = 0;
    public static final float maxDepth = 30;

    public EarthQuake(Elements obj) {
        try {
            latitude = PApplet.parseFloat(PApplet.split(obj.get(2).text(), ' ')[0]);
            longitude = PApplet.parseFloat(PApplet.split(obj.get(3).text(), ' ')[0]);
            date = obj.get(1).text();
            time = obj.get(1).text();
            mag = PApplet.parseFloat(obj.get(5).text());
            depth = PApplet.parseFloat(obj.get(4).text());
            location = obj.get(6).text();
            valid = true;
        } catch (Exception e) {
            PApplet.println(e.toString());
        }
    }

    public EarthQuake(JSONObject obj) {
        try {
            latitude = (float) obj.getDouble("latitude");
            longitude = (float) obj.getDouble("longitude");
            date = obj.getString("date");
            time = obj.getString("time");
            mag = (float) obj.getDouble("mag");
            depth = (float) obj.getDouble("depth");
            location = obj.getString("location");
            valid = true;
        } catch (JSONException e) {
            PApplet.println(e.toString());
        }
    }

    public float getRadius(float minRad, float maxRad) {
        return PApplet.map(mag, minMag, maxMag, minRad, maxRad);
    }

    public float getHue(float minHue, float maxHue) {
        return PApplet.map(depth, minDepth, maxDepth, minHue, maxHue);
    }

    @Override
    public String toString() {
        return "Lat: " + latitude
                + "Long: " + longitude
                + "Date: " + date
                + "Time: " + time
                + "Mag: " + mag
                + "Depth: " + depth
                + "Location: " + location;
    }
}
