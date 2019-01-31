package bughunter.bughunterserver.DTO;

/**
 * @author sean
 * @date 2019-01-26.
 */
public class NodeDTO {

    private Long id;

    private String appKey;

    private String window;

    private boolean known; //此节点之前是否已知

    private int adjuDist; //此节点距离

    private NodeDTO parent; //当前从初始节点到此节点的最短路径下，的父节点。

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

    public NodeDTO getParent() {
        return parent;
    }

    public void setParent(NodeDTO parent) {
        this.parent = parent;
    }
}
