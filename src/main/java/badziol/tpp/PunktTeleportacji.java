package badziol.tpp;

public class PunktTeleportacji {
    private final String nazwa;
    private final String swiat;
    private double x = 0;
    private double y = 0;
    private double z = 0;
    private  float pitch = 0;
    private  float yaw = 0;

    /**
     * Konstruktor punktu
     * @param nazwa - moja nazwa lokalizacji , stanowi klucz "po nazwie" do usuwania - nazwa musi byc unikalna
     * @param swiat - nazwa "biomu"
     * @param x - koordynat  w przestrzeni
     * @param y - koordynat w przestrzeni
     * @param z - koordynat w przestrzeni
     * @param pitch - kierunek patrzenia ??
     * @param yaw - kerunek patrzenia ??
     */
    public PunktTeleportacji(String nazwa, String swiat, double x, double y, double z, float pitch, float yaw) {
        this.nazwa = nazwa;
        this.swiat = swiat;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public String getSwiat() {
        return swiat;
    }

    public String getNazwa() {
        return nazwa;
    }

    public  String toString(){
        String ret="";
        ret = "Nazwa:"+ nazwa;
        ret += "\n Swiat: "+swiat;
        ret += "\n X:"+ x +" , Y: "+ y +" , Z:"+ z;
        ret += "\n pitch: "+pitch+" yaw: "+yaw;
        return ret;
    };


};