package bughunter.bughunterserver.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author sean
 * @date 2019-01-22.
 */
@Entity(name = "edge")
public class Edge {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "source_node")
    private String sourceNode;

    @Column(name = "target_node")
    private String targetNode;

    @Column(name = "callbacks")
    private String callbacks;

    private static int weight = 1;
    /**
     * 0:未覆盖
     * 1:覆盖
     */
    @Column(name = "is_covered")
    private Integer isCovered;

    @Column(name = "app_key")
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

    public static int getWeight() {
        return weight;
    }

    public static void setWeight(int weight) {
        Edge.weight = weight;
    }
}
