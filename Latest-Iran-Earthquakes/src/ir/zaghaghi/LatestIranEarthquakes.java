package ir.zaghaghi;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.looksgood.ani.Ani;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
//import codeanticode.glgraphics.GLGraphics;
/**
 *
 * @author Hamed Zaghaghi
 */
public class LatestIranEarthquakes extends PApplet {

    Ani panelAni = null;
    MyMap map1;
    de.fhpotsdam.unfolding.Map map2;
    ArrayList<EarthQuake> earthquakes;
    PGraphics title;
    PFont font28, font20;
    int updateScreen = 10;

    float sMag = 2.5f;
    float eMag = 10.0f;

    float startMag = 2.5f;
    float endMag = 8;
    int headerHeight = 45;
    int footerHeight = 120;
    int helperliney1;
    int startx;
    public int dy;
    boolean isopen;

    @Override
    public void setup() {
        System.setProperty("java.net.preferIPv4Stack", "true");
        size(900, 600, JAVA2D);
        frameRate(20);
        Ani.init(this);
        dy = footerHeight;
        isopen = false;

        createMap();

        font28 = createFont("fonts/Comfortaa-Bold.ttf", 28, true);
        font20 = createFont("fonts/Comfortaa-Bold.ttf", 20, true);
        startx = 10;
        helperliney1 = height - footerHeight + 30;

        createTitle();
    }

    void createTitle() {
        title = createGraphics(width, headerHeight, JAVA2D);
        title.beginDraw();
        title.background(0, 150);
        title.textFont(font28);
        title.textAlign(LEFT, CENTER);
        title.fill(255, 200);
        title.text("IRAN Earth Quake Visualizer 0.3", 10, headerHeight / 2);
        float w = title.textWidth("IRAN Earth Quake Visualizer 0.1");
        title.textFont(font20, 14);
        title.text("Powered by Processing, irsc.ut.ac.ir, Bing", 10 + w + 10, headerHeight / 2);
        title.endDraw();
    }

    void createMap() {
        map1 = new MyMap(this, 0, 0, width, height, new Microsoft.RoadProvider());
        map1.setupHitRect(0, headerHeight, width, height - footerHeight - headerHeight);
        //map1 = new de.fhpotsdam.unfolding.Map(this, new Microsoft.RoadProvider());
        //map2 = new de.fhpotsdam.unfolding.Map(this, new Yahoo.RoadProvider());

        MapUtils.createDefaultEventDispatcher(this, map1);
        //MapUtils.createDefaultEventDispatcher(this, map2);
        map1.setTweening(true);
        map1.zoomToLevel(5);
        map1.panTo(new Location(33.358062f, 52.727783f));
    }

    void readDatafromIRSC() {
        earthquakes = new ArrayList<>();
        try {
            Document doc = Jsoup.connect("http://irsc.ut.ac.ir/currentearthq.php").get();
            Elements datarows1 = doc.select("tr[class=DataRow1]");
            datarows1.addAll(doc.select("tr[class=DataRow2]"));
            for (Element e : datarows1) {
                Elements tds = e.select("td");
                earthquakes.add(new EarthQuake(tds));
            }
        } catch (Exception e) {
            println(e.toString());
        }
    }

    void readDataFromScrapeWiki() {
        String[] jsonlines = loadStrings("https://api.scraperwiki.com/api/1.0/datastore/sqlite?format=jsondict&name=latest-earthquake-iran&query=select%20*%20from%20(select%20*%20from%20swdata%20order%20by%20mag%20desc)%20limit%2010");
        String jsonString = "{\"data\": " + join(jsonlines, "") + "}";
        try {
            JSONObject objs = new JSONObject(jsonString);

            JSONArray entries = objs.getJSONArray("data");
            earthquakes = new ArrayList<>();
            for (int i = 0; i < entries.length(); i++) {
                JSONObject obj = entries.getJSONObject(i);
                earthquakes.add(new EarthQuake(obj));
            }
        } catch (JSONException ex) {
            println("Ooops : " + ex.toString());
        }
    }

    @Override
    public void draw() {
        if (updateScreen > 0) {
            background(0);
            text("Loading...", width / 2, height / 2);
            updateScreen--;
            return;
        }
        if (updateScreen == 0) {
            readDatafromIRSC();
            background(0);
            text("Loaded.", width / 2, height / 2);
            updateScreen--;
            return;
        }
        background(0);
        smooth();
        map1.draw();
        image(title, 0, 0);
        noStroke();
        ellipseMode(CENTER);
        colorMode(HSB, 240, 100, 100);
        for (EarthQuake e : earthquakes) {
            if (!e.valid) {
                continue;
            }
            Location loc = new Location(e.latitude, e.longitude);
            float xy[] = map1.getScreenPositionFromLocation(loc);
            float rad = e.getRadius(5, 30);
            float chue = e.getHue(0, 45);
            fill(chue, 100, 80, 200);
            ellipse(xy[0], xy[1], rad, rad);
        }
        colorMode(RGB, 255, 255, 255);
        for (EarthQuake e : earthquakes) {
            if (!e.valid) {
                continue;
            }
            Location loc = new Location(e.latitude, e.longitude);
            float xy[] = map1.getScreenPositionFromLocation(loc);
            if (dist(mouseX, mouseY, xy[0], xy[1]) < e.getRadius(5, 30)) {
                String tooltip = e.location + "\nMag: " + e.mag + ", Depth: " + e.depth + "\nDate: " + e.date + " " + e.time;
                noStroke();
                fill(0, 0, 0, 100);
                rect(mouseX + 20, mouseY - 30, 200, 60);
                fill(255, 255, 255);
                text(tooltip, mouseX + 30, mouseY - 10);
                break;
            }
        }
    }
}
