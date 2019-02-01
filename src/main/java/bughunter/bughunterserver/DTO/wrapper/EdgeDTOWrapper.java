package bughunter.bughunterserver.DTO.wrapper;

import bughunter.bughunterserver.DTO.EdgeDTO;
import bughunter.bughunterserver.model.entity.Edge;
import org.springframework.stereotype.Service;

/**
 * @author sean
 * @date 2019-01-26.
 */
@Service
public class EdgeDTOWrapper extends BaseDTOWrapper<EdgeDTO, Edge> {

    @Override
    public EdgeDTO wrap(Edge edge) {

        EdgeDTO edgeDTO = new EdgeDTO();
        edgeDTO.setId(edge.getId());
        edgeDTO.setAppKey(edge.getAppKey());
        edgeDTO.setEventHandlers(edge.getEventHandlers());
        edgeDTO.setIsCovered(edge.getIsCovered());
        edgeDTO.setSourceNode(edge.getSourceNode());
        edgeDTO.setTargetNode(edge.getTargetNode());
        return edgeDTO;
    }

    @Override
    public Edge unwrap(EdgeDTO data) {
        Edge edge = new Edge();

        edge.setAppKey(data.getAppKey());
        edge.setTargetNode(data.getTargetNode());
        edge.setId(data.getId());
        edge.setSourceNode(data.getSourceNode());
        edge.setEventHandlers(data.getEventHandlers());
        edge.setIsCovered(data.getIsCovered());
        return edge;
    }
}
