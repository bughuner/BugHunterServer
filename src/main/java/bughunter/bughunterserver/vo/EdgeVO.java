package bughunter.bughunterserver.vo;

/**
 * @author sean
 * @date 2019-01-23.
 */
public class EdgeVO {

    private Long id;

    private String sourceNode;

    private String targetNode;

    private String callbacks;

    private Integer isCovered;

    private String appKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceNode() {
        return sourceNode;
    }

    public void setSourceNode(String sourceNode) {
        this.sourceNode = sourceNode;
    }

    public String getTargetNode() {
        return targetNode;
    }

    public void setTargetNode(String targetNode) {
        this.targetNode = targetNode;
    }

    public String getCallbacks() {
        return callbacks;
    }

    public void setCallbacks(String callbacks) {
        this.callbacks = callbacks;
    }

    public Integer getIsCovered() {
        return isCovered;
    }

    public void setIsCovered(Integer isCovered) {
        this.isCovered = isCovered;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}


