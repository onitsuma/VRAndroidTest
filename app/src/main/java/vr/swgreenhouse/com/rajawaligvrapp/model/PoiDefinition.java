package vr.swgreenhouse.com.rajawaligvrapp.model;

/**
 * Created by csuay on 02/09/2016.
 */
public class PoiDefinition {

    private Integer x;
    private Integer y;
    private Integer z;

    public Integer getZ() {
        return z;
    }

    public Integer getY() {
        return y;
    }

    public Integer getX() {
        return x;
    }


    public PoiDefinition(Integer x, Integer y, Integer z) {

        this.x = x;
        this.y = y;
        this.z = z;
    }
}
