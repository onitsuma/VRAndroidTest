package vr.swgreenhouse.com.rajawaligvrapp;

import android.os.Bundle;

import com.google.vr.sdk.base.GvrActivity;

import java.util.ArrayList;
import java.util.List;

import vr.swgreenhouse.com.rajawaligvr.RajawaliGVRView;
import vr.swgreenhouse.com.rajawaligvrapp.model.PoiDefinition;

/**
 * Created by csuay on 02/09/2016.
 */
public class ScenarioActivity extends GvrActivity {

    private GVRRenderer renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RajawaliGVRView view = new RajawaliGVRView(this);
        setContentView(view);

        renderer = new GVRRenderer.Builder()
                .setScenarioPois(scenarioPois())
//                .setTextureResourceIds(scenarioTextures())
                .build(this);
        view.setRenderer(renderer);
        view.setSurfaceRenderer(renderer);

    }

    private List<Integer> scenarioTextures() {
        List<Integer> ids = new ArrayList<>();
//        ids.add(R.drawable.datacenter);
        return ids;
    }

    private List<PoiDefinition> scenarioPois() {
        List<PoiDefinition> pois = new ArrayList<>();
        pois.add(new PoiDefinition(60, 5, 0));
        pois.add(new PoiDefinition(0, 5, -60));
        pois.add(new PoiDefinition(0, 5, 60));
        pois.add(new PoiDefinition(-60, 5, 0));
        return pois;
    }


}

