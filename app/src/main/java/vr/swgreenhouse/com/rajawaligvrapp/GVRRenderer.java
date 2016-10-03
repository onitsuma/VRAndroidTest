package vr.swgreenhouse.com.rajawaligvrapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.opengl.Matrix;

import com.google.vr.sdk.base.HeadTransform;

import org.rajawali3d.Object3D;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.StreamingTexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Cube;
import org.rajawali3d.primitives.Plane;
import org.rajawali3d.primitives.RectangularPrism;
import org.rajawali3d.primitives.ScreenQuad;
import org.rajawali3d.primitives.Sphere;

import java.util.ArrayList;
import java.util.List;

import vr.swgreenhouse.com.rajawaligvr.RajawaliGVRRenderer;
import vr.swgreenhouse.com.rajawaligvrapp.model.PoiDefinition;

/**
 * Created by csuay on 02/09/2016.
 */
public class GVRRenderer extends RajawaliGVRRenderer {

    private static final float YAW_LIMIT = 0.2f;
    private static final float PITCH_LIMIT = 0.2f;
    private float[] mModelView;

    private List<Integer> textureResoureceIds;
    private List<Bitmap> textureBitmaps;
    private List<Material> scenarioMaterials;
    private List<Object3D> scenarioPois;
    private List<PoiDefinition> defPois;


    private MediaPlayer mMediaPlayer;
    private StreamingTexture mVideoTexture;


    private Sphere scenario;

    public static class Builder {
        private List<Integer> textureResourceIds;
        private List<Bitmap> textureBitmaps;
        private List<PoiDefinition> defPois;

        public Builder setTextureResourceIds(List<Integer> textureResourceIds) {
            this.textureResourceIds = textureResourceIds;
            return this;
        }

        public Builder setTextureBitmaps(List<Bitmap> textureBitmaps) {
            this.textureBitmaps = textureBitmaps;
            return this;
        }

        public Builder setScenarioPois(List<PoiDefinition> defPois) {
            this.defPois = defPois;
            return this;
        }

        public GVRRenderer build(Context context) {
            return new GVRRenderer(context, this.textureResourceIds, this.textureBitmaps, this.defPois);
        }

        public GVRRenderer build(Context context, boolean registerForResources) {
            return new GVRRenderer(context, registerForResources, this.textureResourceIds, this.textureBitmaps, this.defPois);
        }

    }

    private GVRRenderer(Context context, boolean registerForResources, List<Integer> textureResoureceIds, List<Bitmap> textureBitmaps, List<PoiDefinition> defPois) {
        super(context, registerForResources);
        construct(textureResoureceIds, textureBitmaps, defPois);
    }

    private GVRRenderer(Context context, List<Integer> textureResoureceIds, List<Bitmap> textureBitmaps, List<PoiDefinition> defPois) {
        super(context);
        construct(textureResoureceIds, textureBitmaps, defPois);
    }


    private void construct(List<Integer> textureResoureceIds, List<Bitmap> textureBitmaps, List<PoiDefinition> defPois) {
        this.mModelView = new float[16];
        this.textureResoureceIds = textureResoureceIds;
        this.textureBitmaps = textureBitmaps;
        this.defPois = defPois;
    }


    @Override
    protected void onRender(long ellapsedRealTime, double deltaTime) {
        super.onRender(ellapsedRealTime, deltaTime);
        mVideoTexture.update();
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        super.onNewFrame(headTransform);

        for (Object3D poi : scenarioPois) {
            isLookingAtObject(poi);
        }

    }

    @Override
    protected void initScene() {
        this.scenario = createScenario();
        addPois();
//        scenario.addChild(createVideoScreen(0, 5, 60));
        getCurrentScene().addChild(scenario);
        getCurrentCamera().setPosition(Vector3.ZERO);
        getCurrentCamera().setFieldOfView(100);
        mMediaPlayer.start();


    }

    private Sphere createScenario() {

        Sphere sphere = new Sphere(1, 100, 100);
        sphere.setPosition(0, 0, 0);
        // invert the sphere normals
        // factor "1" is two small and result in rendering glitches
        sphere.setScaleX(100);
        sphere.setScaleY(100);
        sphere.setScaleZ(-100);
        scenarioMaterials = new ArrayList<>();
//        try {
//            if (textureBitmaps != null) {
//                Material m = new Material();
//                m.setColor(0);
//                for (Bitmap b : textureBitmaps) {
//                    m.addTexture(new Texture("scenarioTexture", b));
//                    scenarioMaterials.add(m);
//                }
//            } else if (textureResoureceIds != null) {
//                Material m = new Material();
//                m.setColor(0);
//                for (Integer id : textureResoureceIds) {
//                    m.addTexture(new Texture("scenarioTexture", id));
//                    scenarioMaterials.add(m);
//                }
//            } else {
//                Material m = new Material();
//                m.setColor(0);
//                m.addTexture(new Texture("scenarioTexture"));
//                scenarioMaterials.add(m);
//            }
//        } catch (ATexture.TextureException e) {
//            e.printStackTrace();
//        }
//        sphere.setMaterial(scenarioMaterials.get(0));
        /**/
        Material m = new Material();
        m.setColor(0);
        m.setColorInfluence(0);

        mMediaPlayer = MediaPlayer.create(getContext(), R.raw.shark);
        mMediaPlayer.setLooping(true);
        mVideoTexture = new StreamingTexture("video", mMediaPlayer);


        try {
            m.addTexture(mVideoTexture);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }
        /**/
        sphere.setMaterial(m);
        return sphere;
    }


    private void addPois() {
        if (defPois != null) {
            scenarioPois = new ArrayList<>();
            for (PoiDefinition poi : defPois) {
                this.scenario.addChild(createButtonCube(poi.getX(), poi.getY(), poi.getZ()));
            }
        }
    }

    private Cube createButtonCube(int x, int y, int z) {
        Cube c = new Cube(10);
        c.setPosition(x, y, z);
        Material m = new Material();
        m.setColor(Color.GREEN);
        c.setMaterial(m);
        return c;
    }


    private Object3D createVideoScreen(int x, int y, int z) {
        Plane cube = new Plane(64, 27, 1, 1, 1);
        cube.setPosition(x, y, z);
        Material m = new Material();
        m.setColorInfluence(0);

        mMediaPlayer = MediaPlayer.create(getContext(), R.raw.schn);
        mMediaPlayer.setLooping(true);
        mVideoTexture = new StreamingTexture("video", mMediaPlayer);


        try {
            m.addTexture(mVideoTexture);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }
        cube.setMaterial(m);
        return cube;

    }

    private boolean isLookingAtObject(Object3D object) {
        float[] initVec = {0, 0, 0, 1.0f};
        float[] objPositionVec = new float[4];
        float[] objectMatrix = new float[16];

        object.getModelMatrix().toFloatArray(objectMatrix);

        // Convert object space to camera space. Use the headView from onNewFrame.
        Matrix.multiplyMM(mModelView, 0, getCurrentCamera().getViewMatrix().getFloatValues(), 0, objectMatrix, 0);
        Matrix.multiplyMV(objPositionVec, 0, mModelView, 0, initVec, 0);

        float pitch = (float) Math.atan2(objPositionVec[1], -objPositionVec[2]);
        float yaw = (float) Math.atan2(objPositionVec[0], -objPositionVec[2]);
        if (Math.abs(pitch) < PITCH_LIMIT && Math.abs(yaw) < YAW_LIMIT) {
            object.getMaterial().setColor(Color.WHITE);
            return true;

        } else {
            object.getMaterial().setColor(Color.TRANSPARENT);

            return false;
        }
    }

}
