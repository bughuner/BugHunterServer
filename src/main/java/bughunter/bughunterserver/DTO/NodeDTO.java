package bughunter.bughunterserver.DTO;

/**
 * @author sean
 * @date 2019-01-26.
 */
public class NodeDTO {

    private final static int infinite_dis = Integer.MAX_VALUE;

    private Long id;

    private String appKey;

    private String window;
    //此节点之前是否已知
    private boolean known;
    //保存从开始节点到此节点距离
    private int adjuDist = 0;
    //当前从初始节点到此节点的最短路径下的父节点
    private NodeDTO parent;

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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NodeDTO)) {
            throw new ClassCastException("an object to compare with a Vertext must be Vertex");
        }

        if (this.window == null) {
            throw new NullPointerException("name of Vertex to be compared cannot be null");
        }

        return this.window.equals(obj);
    }
}
