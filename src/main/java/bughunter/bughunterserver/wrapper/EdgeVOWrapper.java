package bughunter.bughunterserver.wrapper;

import bughunter.bughunterserver.model.entity.Edge;
import bughunter.bughunterserver.vo.EdgeVO;
import org.springframework.stereotype.Service;

/**
 * @author sean
 * @date 2019-01-23.
 */
@Service
public class EdgeVOWrapper extends BaseWrapper<EdgeVO, Edge> {

    @Override
    public EdgeVO wrap(Edge edge) {
        EdgeVO edgeVO = new EdgeVO();
        edgeVO.setId(edge.getId());
        edgeVO.setSourceNode(edge.getSourceNode());
        edgeVO.setTargetNode(edge.getTargetNode());
        edgeVO.setEventHandlers(edge.getEventHandlers());
        edgeVO.setIsCovered(edge.getIsCovered());
        edgeVO.setAppKey(edge.getAppKey());
        edgeVO.setEventType(edge.getEventType());
        edgeVO.setRepeat(edge.getRepeat());
        return edgeVO;
    }

    @Override
    public Edge unwrap(EdgeVO data) {
        Edge edge = new Edge();
        edge.setSourceNode(data.getSourceNode());
        edge.setTargetNode(data.getTargetNode());
        edge.setEventHandlers(data.getEventHandlers());
        edge.setEventType(data.getEventType());
        edge.setIsCovered(data.getIsCovered());
        edge.setAppKey(data.getAppKey());
        edge.setRepeat(data.getRepeat());
        return edge;
    }
}
