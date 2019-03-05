package bughunter.bughunterserver.DTO;

/**
 * @author sean
 * @date 2019-03-05.
 */
public class EdgeDTO {

    private Long id;

    private NodeDTO sourceNode;

    private NodeDTO targetNode;

    private String eventHandlers;

    private String eventType;

    private Integer number;

    private static int weight = 1;

    private Integer isCovered;

    private String appKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NodeDTO getSourceNode() {
        return sourceNode;
    }

    public void setSourceNode(NodeDTO sourceNode) {
        this.sourceNode = sourceNode;
    }

    public NodeDTO getTargetNode() {
        return targetNode;
    }

    public void setTargetNode(NodeDTO targetNode) {
        this.targetNode = targetNode;
    }

    public String getEventHandlers() {
        return eventHandlers;
    }

    public void setEventHandlers(String eventHandlers) {
        this.eventHandlers = eventHandlers;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public static int getWeight() {
        return weight;
    }

    public static void setWeight(int weight) {
        EdgeDTO.weight = weight;
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
