package bughunter.bughunterserver.DTO.wrapper;

import bughunter.bughunterserver.DTO.NodeDTO;
import bughunter.bughunterserver.model.entity.Node;
import org.springframework.stereotype.Service;

/**
 * @author sean
 * @date 2019-01-26.
 */
@Service
public class NodeDTOWrapper extends BaseDTOWrapper<NodeDTO, Node> {

    @Override
    public NodeDTO wrap(Node node) {
        NodeDTO nodeDTO = new NodeDTO();
        nodeDTO.setId(node.getId());
        nodeDTO.setAppKey(node.getAppKey());
        nodeDTO.setWindow(node.getWindow());
        nodeDTO.setAdjuDist(Integer.MAX_VALUE);
        nodeDTO.setKnown(false);
        nodeDTO.setParent(null);
        return nodeDTO;
    }

    @Override
    public Node unwrap(NodeDTO data) {
        Node node = new Node();
        node.setId(data.getId());
        node.setWindow(data.getWindow());
        node.setAppKey(data.getAppKey());
        node.setAdjuDist(data.getAdjuDist());
        node.setKnown(data.isKnown());
        node.setParent(data.getParent());
        return node;
    }
}
