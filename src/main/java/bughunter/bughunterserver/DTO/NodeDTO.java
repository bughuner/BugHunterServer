package bughunter.bughunterserver.DTO;

/**
 * @author sean
 * @date 2019-03-06.
 */
public class NodeDTO {


    private final static int infinite_dis = Integer.MAX_VALUE;

    private Long id;

    private String appKey;

    private String window;

    private String type;

    private boolean known; //此节点之前是否已知
    private int adjuDist; //此节点距离
    private String parent; //当前从初始节点到此节点的最短路径下，的父节点。

    public NodeDTO() {
        this.known = false;
        this.adjuDist = infinite_dis;
        this.parent = null;
    }

    public NodeDTO(String name) {
        this.known = false;
        this.adjuDist = infinite_dis;
        this.parent = null;
        this.window = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getWindow() {
        return window;
    }

    public void setWindow(String window) {
        this.window = window;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isKnown() {
        return known;
    }

    public void setKnown(boolean known) {
        this.known = known;
    }

    public int getAdjuDist() {
        return adjuDist;
    }

    public void setAdjuDist(int adjuDist) {
        this.adjuDist = adjuDist;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NodeDTO)) {
            throw new ClassCastException("an object to compare with a NodeDTO must be NodeDTO");
        }

        if (this.window == null) {
            throw new NullPointerException("name of NodeDTO to be compared cannot be null");
        }

        return this.window.equals(((NodeDTO) obj).getWindow());
    }

    @Override
    public int hashCode() {
        return this.getWindow().hashCode();
    }

}
