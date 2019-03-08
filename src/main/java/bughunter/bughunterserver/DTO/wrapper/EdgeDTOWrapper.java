package bughunter.bughunterserver.DTO.wrapper;

import bughunter.bughunterserver.DTO.EdgeDTO;
import bughunter.bughunterserver.dao.NodeDao;
import bughunter.bughunterserver.model.entity.Edge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author sean
 * @date 2019-01-26.
 */
@Service
public class EdgeDTOWrapper extends BaseDTOWrapper<EdgeDTO, Edge> {

    @Autowired
    NodeDao nodeDao;

    @Autowired
    NodeDTOWrapper nodeDTOWrapper;

    @Override
    public EdgeDTO wrap(Edge edge) {

        EdgeDTO edgeDTO = new EdgeDTO();
        edgeDTO.setId(edge.getId());
        edgeDTO.setAppKey(edge.getAppKey());
        edgeDTO.setEventHandlers(edge.getEventHandlers());
        edgeDTO.setIsCovered(edge.getIsCovered());
        edgeDTO.setSourceNode(nodeDTOWrapper.wrap(nodeDao.findByWindow(edge.getSourceNode())));
        edgeDTO.setTargetNode(nodeDTOWrapper.wrap(nodeDao.findByWindow(edge.getTargetNode())));
        edgeDTO.setNumber(edge.getNumber());
        edgeDTO.setWeight(1);
        return edgeDTO;
    }

    @Override
    public Edge unwrap(EdgeDTO data) {
        Edge edge = new Edge();

        edge.setAppKey(data.getAppKey());
        edge.setTargetNode(data.getTargetNode().getWindow());
        edge.setSourceNode(data.getSourceNode().getWindow());
        edge.setEventHandlers(data.getEventHandlers());
        edge.setIsCovered(data.getIsCovered());
        edge.setNumber(data.getNumber());
        edge.setEventType(data.getEventType());
        edge.setEventHandlers(data.getEventHandlers());
        return edge;
    }
}
